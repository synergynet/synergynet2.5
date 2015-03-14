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

import java.util.List;

import com.jme.scene.Node;

import synergynetframework.jme.pickingsystem.data.PickRequest;
import synergynetframework.jme.pickingsystem.data.PickResultData;

/**
 * Defines the picking system for jMonkeyEngine.  Each pick request
 * should result in a List of pick results for that location.  Each
 * pick result is a spatial that was found to be under the request
 * location.  If the pick request asks for all results to be returned,
 * they will, otherwise, only the closest will be returned.
 * 
 * @author dcs0ah1
 */
public interface IJMEMultiTouchPicker {
	
	public void setPickingRootNode(Node rootNode) throws PickSystemException;
	public void setOrthogonalPickingRoot(Node twod) throws PickSystemException;
	public List<PickResultData> doPick(PickRequest pickRequest) throws PickSystemException;
	public List<PickResultData> doPick(PickRequest pickRequest, boolean worldNodeOnly) throws PickSystemException;
	
		
}
