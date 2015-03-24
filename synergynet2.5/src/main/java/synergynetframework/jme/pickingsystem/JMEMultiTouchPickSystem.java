/*
 * Copyright (c) 2008 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.jme.pickingsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import synergynetframework.jme.cursorsystem.elements.twod.ClipRegion;
import synergynetframework.jme.cursorsystem.elements.twod.ClipRegistry;
import synergynetframework.jme.pickingsystem.data.OrthogonalPickResultData;
import synergynetframework.jme.pickingsystem.data.PickRequest;
import synergynetframework.jme.pickingsystem.data.PickResultData;
import synergynetframework.jme.pickingsystem.data.ThreeDPickResultData;

import com.jme.intersection.BoundingPickResults;
import com.jme.intersection.PickData;
import com.jme.intersection.PickResults;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;
import com.jme.system.DisplaySystem;

/**
 * Default implementation of IJMEMultiTouchPickSystem. Pick requests are screen
 * locations. Pick results are names of Spatials that are picked at that screen
 * location.
 *
 * @author dcs0ah1
 */

public class JMEMultiTouchPickSystem implements IJMEMultiTouchPicker {

	/** The instance. */
	private static JMEMultiTouchPickSystem instance;

	/** The ortho comparator. */
	private static Comparator<OrthogonalPickResultData> orthoComparator;
	
	static {
		orthoComparator = new Comparator<OrthogonalPickResultData>() {
			public int compare(OrthogonalPickResultData o1,
					OrthogonalPickResultData o2) {
				if (o1 == o2) {
					return 0;
				}
				if (o1.getDistanceWhenPicked() > o2.getDistanceWhenPicked()) {
					if (o1.getPickedSpatial().getZOrder() > o2
							.getPickedSpatial().getZOrder()) {
						return 1;
					} else {
						return -1;
					}
				} else {
					if (o1.getPickedSpatial().getZOrder() < o2
							.getPickedSpatial().getZOrder()) {
						return 1;
					} else {
						return -1;
					}
				}
			}
		};
	}

	/**
	 * Gets the single instance of JMEMultiTouchPickSystem.
	 *
	 * @return single instance of JMEMultiTouchPickSystem
	 */
	public static JMEMultiTouchPickSystem getInstance() {
		if (instance == null) {
			instance = new JMEMultiTouchPickSystem();
		}
		return instance;
	}

	/**
	 * Prints the pick results.
	 *
	 * @param pr2
	 *            the pr2
	 */
	public static void printPickResults(List<PickResultData> pr2) {
		System.out.println("========\n#picks: " + pr2.size());
		for (PickResultData prd : pr2) {
			System.out.println(prd.getPickedSpatialName() + ", d="
					+ prd.getDistanceWhenPicked());
		}
		System.out.println("--------");
	}

	/**
	 * Prints the pick results.
	 *
	 * @param pr2
	 *            the pr2
	 */
	public static void printPickResults(PickResults pr2) {
		System.out.println("========\n#picks: " + pr2.getNumber());
		for (int i = 0; i < pr2.getNumber(); i++) {
			System.out.println(pr2.getPickData(i).getTargetMesh().getName()
					+ " " + pr2.getPickData(i).getDistance());
		}
		System.out.println("--------");
	}

	/** The orthogonal picking root. */
	protected Node orthogonalPickingRoot;

	/** The ortho pick results. */
	protected PickResults orthoPickResults = null;

	/** The pick root node. */
	protected Node pickRootNode;

	/** The three d pick results. */
	protected PickResults threeDPickResults = null;

	/**
	 * Instantiates a new JME multi touch pick system.
	 */
	private JMEMultiTouchPickSystem() {
		threeDPickResults = new PointSelectionTrianglePickResults();
		threeDPickResults.setCheckDistance(true);
		orthoPickResults = new BoundingPickResults();
		orthoPickResults.setCheckDistance(true);
	}
	
	/**
	 * Do orthogonal pick.
	 *
	 * @param pickRequest
	 *            the pick request
	 * @return the list
	 */
	private List<OrthogonalPickResultData> doOrthogonalPick(
			PickRequest pickRequest) {
		orthoPickResults.clear();

		List<PickedSpatial> ps = AccuratePickingUtility.pickAllOrthogonal(
				orthogonalPickingRoot, pickRequest.getCursorPosition());
		ArrayList<OrthogonalPickResultData> prd = new ArrayList<OrthogonalPickResultData>();
		for (PickedSpatial p : ps) {
			if (ClipRegistry.getInstance().isRegistered(p.getSpatial())) {
				ClipRegion clipRegion = ClipRegistry.getInstance()
						.getClipRegionForSpatial(p.getSpatial());
				if (!clipRegion.isPicked(new Vector2f(
						p.getPointOfSelection().x, p.getPointOfSelection().y))) {
					continue;
				}
			}
			OrthogonalPickResultData entry = new OrthogonalPickResultData(
					pickRequest.getRequestingCursorID(),
					pickRequest.getCursorPosition(), p.getSpatial());
			prd.add(entry);
		}
		Collections.sort(prd, orthoComparator);
		return prd;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.jme.pickingsystem.IJMEMultiTouchPicker#doPick(
	 * synergynetframework.jme.pickingsystem.data.PickRequest)
	 */
	public List<PickResultData> doPick(PickRequest pickRequest)
			throws PickSystemException {

		List<PickResultData> results = new ArrayList<PickResultData>();
		
		if (orthogonalPickingRoot != null) {
			results.addAll(doOrthogonalPick(pickRequest));
		}
		
		results.addAll(doScenePick(pickRequest));
		return results;
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.jme.pickingsystem.IJMEMultiTouchPicker#doPick(
	 * synergynetframework.jme.pickingsystem.data.PickRequest, boolean)
	 */
	public List<PickResultData> doPick(PickRequest pickRequest,
			boolean worldNodeOnly) throws PickSystemException {
		List<PickResultData> results = new ArrayList<PickResultData>();
		results.addAll(doScenePick(pickRequest));
		if (worldNodeOnly) {
			return results;
		}
		if (orthogonalPickingRoot != null) {
			results.addAll(doOrthogonalPick(pickRequest));
		}
		return results;
	}

	/**
	 * Do scene pick.
	 *
	 * @param pickRequest
	 *            the pick request
	 * @return the list
	 * @throws PickSystemException
	 *             the pick system exception
	 */
	private List<ThreeDPickResultData> doScenePick(PickRequest pickRequest)
			throws PickSystemException {
		if (pickRootNode == null) {
			throw new PickSystemException(
					"Cannot use the pick system with a null root picking node.  Use setPickingRootNode()");
		}
		threeDPickResults.clear();
		Vector3f worldCoords = DisplaySystem.getDisplaySystem()
				.getWorldCoordinates(pickRequest.getCursorPosition(), 0);
		Vector3f worldCoords2 = DisplaySystem.getDisplaySystem()
				.getWorldCoordinates(pickRequest.getCursorPosition(), 1);
		Ray cursorRay = new Ray(worldCoords, worldCoords2.subtractLocal(
				worldCoords).normalizeLocal());
		pickRootNode.calculatePick(cursorRay, threeDPickResults);
		if (threeDPickResults.getNumber() > 0) {
			return getAllPicked(pickRequest, threeDPickResults);
		}
		return new ArrayList<ThreeDPickResultData>();
	}
	
	/**
	 * Gets the all picked.
	 *
	 * @param pickRequest
	 *            the pick request
	 * @param pickResults
	 *            the pick results
	 * @return the all picked
	 */
	private List<ThreeDPickResultData> getAllPicked(PickRequest pickRequest,
			PickResults pickResults) {
		List<ThreeDPickResultData> prd = new ArrayList<ThreeDPickResultData>();
		PickData pd;
		for (int i = 0; i < pickResults.getNumber(); i++) {
			pd = pickResults.getPickData(i);
			
			// if(!Float.isInfinite(pd.getDistance())) {
			Spatial pickedSpatial = pd.getTargetMesh();
			// is there a way to get default picking to not find culled
			// spatials?!
			if (pickedSpatial.getCullHint() != CullHint.Always) {
				ThreeDPickResultData entry = new ThreeDPickResultData(
						pickRequest.getRequestingCursorID(),
						pickRequest.getCursorPosition(), pickedSpatial);
				if (pickResults instanceof PointSelectionTrianglePickResults) {
					entry.setPointOfSelection(((PointSelectionTrianglePickResults) pickResults)
							.getPointOfSelection());
					entry.setDistanceWhenPicked(pd.getDistance());
				}
				prd.add(entry);
			}
			// }
		}
		return prd;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.jme.pickingsystem.IJMEMultiTouchPicker#
	 * setOrthogonalPickingRoot(com.jme.scene.Node)
	 */
	public void setOrthogonalPickingRoot(Node twod) throws PickSystemException {
		if (twod == null) {
			throw new PickSystemException(
					"Cannot set a null root node to pick from!");
		}
		this.orthogonalPickingRoot = twod;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.pickingsystem.IJMEMultiTouchPicker#setPickingRootNode
	 * (com.jme.scene.Node)
	 */
	public void setPickingRootNode(Node rootNode) throws PickSystemException {
		if (rootNode == null) {
			throw new PickSystemException(
					"Cannot set a null root node to pick from!");
		}
		this.pickRootNode = rootNode;
	}
}
