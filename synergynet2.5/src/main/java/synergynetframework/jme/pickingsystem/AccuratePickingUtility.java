/*
 * Copyright (c) 2008 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.jme.pickingsystem;

import java.util.ArrayList;
import java.util.List;

import com.jme.intersection.PickData;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

public class AccuratePickingUtility {

	public static Vector3f getPointOfIntersection(Node rootNode, Vector2f mousePos, Spatial spatial) {
		AccurateTrianglePickResults pickResults = new AccurateTrianglePickResults();
		Vector3f worldCoords = DisplaySystem.getDisplaySystem().getWorldCoordinates(mousePos, 0);
		Vector3f worldCoords2 = DisplaySystem.getDisplaySystem().getWorldCoordinates(mousePos, 1);
		Ray mouseRay = new Ray(worldCoords, worldCoords2.subtractLocal(worldCoords).normalizeLocal());
		pickResults.clear();		
		rootNode.calculatePick(mouseRay, pickResults);	
		
		PickData pd;
		for(int i = 0; i < pickResults.getNumber(); i++) {
			pd = pickResults.getPickData(i);

			if(!Float.isInfinite(pd.getDistance())) {
				Spatial s = pd.getTargetMesh();
				if(s == spatial)
					return  pickResults.getPointOfSelection();
			}
		}
		return null;
	}
	
	public static List<PickedSpatial> pickAll(Node rootNode, Vector2f mousePos) {
		AccurateTrianglePickResults pickResults = new AccurateTrianglePickResults();
		Vector3f worldCoords = DisplaySystem.getDisplaySystem().getWorldCoordinates(mousePos, 0);
		Vector3f worldCoords2 = DisplaySystem.getDisplaySystem().getWorldCoordinates(mousePos, 1);
		Ray mouseRay = new Ray(worldCoords, worldCoords2.subtractLocal(worldCoords).normalizeLocal());
		pickResults.clear();		
		rootNode.calculatePick(mouseRay, pickResults);	
		
		PickData pd;
		
		List<PickedSpatial> spatials = new ArrayList<PickedSpatial>();
		for(int i = 0; i < pickResults.getNumber(); i++) {
			pd = pickResults.getPickData(i);
			if(!Float.isInfinite(pd.getDistance())) {
				Spatial s = pd.getTargetMesh();
				PickedSpatial pickedSpatial = new PickedSpatial(s, pickResults.getPointOfSelection());
				spatials.add(pickedSpatial);
			}
		}
		
		return spatials;
	}
	
	public static List<PickedSpatial> pickAllOrthogonal(Node rootNode, Vector2f mousePos) {
		AccurateOrthogonalTrianglePickResults pickResults = new AccurateOrthogonalTrianglePickResults(mousePos);
		Vector3f worldCoords = DisplaySystem.getDisplaySystem().getWorldCoordinates(mousePos, 0);
		Vector3f worldCoords2 = DisplaySystem.getDisplaySystem().getWorldCoordinates(mousePos, 1);
		Ray mouseRay = new Ray(worldCoords, worldCoords2.subtractLocal(worldCoords).normalizeLocal());
		pickResults.clear();		
		rootNode.calculatePick(mouseRay, pickResults);
		return pickResults.getPickedItems();
	}

	public static void printPickResults(AccurateOrthogonalTrianglePickResults pickResults) {
		for(int i = 0; i < pickResults.getNumber(); i++) {
			System.out.println(pickResults.getPickData(i));
		}
	}
	

}
