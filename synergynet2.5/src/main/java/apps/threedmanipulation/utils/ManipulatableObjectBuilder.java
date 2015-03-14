package apps.threedmanipulation.utils;

import java.util.Map;
import java.util.UUID;

import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.elements.threed.ControlPointRotateTranslateScale;
import synergynetframework.jme.cursorsystem.elements.threed.ControlPointRotateTranslateScale.RotateTranslateScaleListener;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

import apps.threedmanipulation.gestures.ObjectRightClick;
import apps.threedmanipulation.gestures.ObjectRotateTranslate;
import apps.threedmanipulation.gestures.ObjectRightClick.CursorEventListener;
import apps.threedmanipulation.gestures.ObjectRotateTranslate.ObjectRotateTranslateScaleListener;
import apps.threedmanipulation.tools.TouchPad;
import apps.threedmanipulation.tools.TwinObject;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class ManipulatableObjectBuilder {
	
	public static void buildManipulatedObject(final Context context, final Spatial object, final Spatial clonedObject, boolean isOneFingerMove, boolean isManipulatedOjbectNode){
						
		final Map<String, TouchPad> touchPads = context.getTouchPads();;
		final Map<String, TwinObject> twinObjects = context.getTwinObjects();
		
		clonedObject.setLocalTranslation(10000, 10000, -10000);
		clonedObject.setModelBound(null);
		clonedObject.updateModelBound();
	        
		//multitouch event for clonedObject
		ObjectRotateTranslate twinObjectRotateTranslate;
		if (isManipulatedOjbectNode)
			twinObjectRotateTranslate = new ObjectRotateTranslate(((Node)clonedObject).getChild(0), clonedObject, object);
		else
			twinObjectRotateTranslate = new ObjectRotateTranslate(clonedObject, clonedObject, object);
	    twinObjectRotateTranslate.setPickMeOnly(true);
	    twinObjectRotateTranslate.addRotateTranslateScaleListener(new ObjectRotateTranslateScaleListener(){
			@Override
			public void itemMoved(
					Spatial targetSpatial, float newLocationX,
					float newLocationY, float oldLocationX, float oldLocationY) {
						
				if (twinObjects.keySet().contains(object.getName())){					
					TwinObject twinObject = twinObjects.get(object.getName());
					twinObject.updateLine();
				}
									
			}
			
		});
	        	    
		//multi-touch event for object
	    if (isOneFingerMove){
	    	ControlPointRotateTranslateScale cprts1;
	    	if (isManipulatedOjbectNode)
	    		cprts1 = new ControlPointRotateTranslateScale(((Node)object).getChild(0), object);
	    	else
	    		cprts1 = new ControlPointRotateTranslateScale(object);
	    	cprts1.setPickMeOnly(true);
	    	cprts1.addRotateTranslateScaleListener(new RotateTranslateScaleListener(){
	    		@Override
	    		public void itemMoved(
					Spatial targetSpatial, float newLocationX,
					float newLocationY, float oldLocationX, float oldLocationY) {
					
	    			if (context.getIndirectManipulationMode().equals("touchpad")){
						if (touchPads.keySet().contains(object.getName())){
						
							TouchPad touchPad = touchPads.get(object.getName());
							touchPad.updateLine();
						}
					}
					else if (context.getIndirectManipulationMode().equals("twinobject")){
						if (twinObjects.keySet().contains(object.getName())){
						
							TwinObject twinObject = twinObjects.get(object.getName());
							twinObject.updateLine();
						}
					}
									
				}
			
			});
	    }
	    else {
	    	ObjectRotateTranslate cprts1;
	    	if (!isManipulatedOjbectNode)
	    		cprts1 = new ObjectRotateTranslate(object);
	    	else
	    		cprts1 = new ObjectRotateTranslate(((Node)object).getChild(0), object);
	    	cprts1.setPickMeOnly(true);
	    	cprts1.addRotateTranslateScaleListener(new ObjectRotateTranslateScaleListener(){
	    		@Override
	    		public void itemMoved(
					Spatial targetSpatial, float newLocationX,
					float newLocationY, float oldLocationX, float oldLocationY) {
					
	    			if (context.getIndirectManipulationMode().equals("touchpad")){
						if (touchPads.keySet().contains(object.getName())){
						
							TouchPad touchPad = touchPads.get(object.getName());
							touchPad.updateLine();
						}
					}
					else if (context.getIndirectManipulationMode().equals("twinobject")){
						if (twinObjects.keySet().contains(object.getName())){
						
							TwinObject twinObject = twinObjects.get(object.getName());
							twinObject.updateLine();
						}
					}
									
				}
			
			});
	    }
	    ObjectRightClick objectDoubleClick;
	    if (!isManipulatedOjbectNode)
	    	objectDoubleClick = new ObjectRightClick(object);
	    else
	    	objectDoubleClick = new ObjectRightClick(((Node)object).getChild(0), object);
		objectDoubleClick.addMultiTouchListener(new CursorEventListener(){

			@Override
			public void cursorRightClicked(ScreenCursor c,
						MultiTouchCursorEvent event) {
					
				if (context.getIndirectManipulationMode().equals("touchpad")){
					if (!touchPads.keySet().contains(object.getName())){
						TouchPad touchPad = new TouchPad(UUID.randomUUID().toString()+"touchPad", context.getContentSystem(), context.getWorldNode(), context.getOrthoNode(), 150, object, new Vector2f(400, 400));
						touchPads.put(object.getName(), touchPad);
					}
					else{
						TouchPad touchPad = touchPads.get(object.getName());
						touchPad.cleanup();
						touchPads.remove(object.getName());
						
					}
				}
				else if (context.getIndirectManipulationMode().equals("twinobject")){
					if (!twinObjects.keySet().contains(object.getName())){
						TwinObject twinObject = new TwinObject(UUID.randomUUID().toString()+"twinObject", context.getContentSystem(), context.getWorldNode(), object, clonedObject, new Vector3f(10, 5, 110));
						twinObjects.put(object.getName(), twinObject);
					}
					else{
						TwinObject twinObject = twinObjects.get(object.getName());
						twinObject.cleanup();
						twinObjects.remove(object.getName());			
					}
				}
			}
				
		});
			
		context.getManipulatableOjbects().add(object);
		context.getManipulatableOjbects().add(clonedObject);	
	}
}
