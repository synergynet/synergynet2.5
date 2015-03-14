/* Copyright (c) 2008 University of Durham, England
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


package synergynetframework.appsystem.contentsystem.jme.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContainer;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IOrthoContentItemImplementation;
import synergynetframework.appsystem.contentsystem.items.listener.BringToTopListener;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoFlickListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoSnapListener;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.appsystem.contentsystem.items.utils.Background;
import synergynetframework.appsystem.contentsystem.items.utils.Border;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import synergynetframework.appsystem.contentsystem.jme.JMEContentSystem;
import synergynetframework.jme.cursorsystem.MultiTouchElementRegistry;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.elements.twod.IndependentChildOrthoCPRTS;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoCursorEventDispatcher;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoSnap;
import synergynetframework.jme.cursorsystem.elements.twod.SingleTouchRotateTranslate;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop.OrthoBringToTopListener;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale.RotateTranslateScaleListener;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoCursorEventDispatcher.CommonCursorEventListener;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoSnap.SnapListener;
import synergynetframework.jme.cursorsystem.fixutils.FixLocationStatus;
import synergynetframework.jme.cursorsystem.flicksystem.FlickMover;
import synergynetframework.jme.cursorsystem.flicksystem.FlickSystem;
import synergynetframework.jme.cursorsystem.flicksystem.FlickMover.FlickListener;
import synergynetframework.mtinput.LongHoldDetector;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

public class JMEOrthoContentItem extends JMEContentItem implements 
IOrthoContentItemImplementation, CommonCursorEventListener, RotateTranslateScaleListener, OrthoBringToTopListener, SnapListener, FlickListener, ScreenCursorListener  {

	protected int order;

	protected boolean bringToTopable;
	protected boolean rotateTranslateScalable;
	protected boolean snapable;
	protected boolean allowMoreThanTwoToRotateAndScale = false;
	protected boolean allowSingleTouchFreeMove = false;
	protected boolean attachToParent = true;

	protected OrthoCursorEventDispatcher orthoCursorEventDispatcher;
	protected OrthoControlPointRotateTranslateScale orthoControlPointRotateTranslateScale;
	protected OrthoBringToTop orthoBringtoTop;
	protected OrthoSnap orthoSnap;
	protected SingleTouchRotateTranslate singleTouch;

	protected List<ItemListener> itemListeners = new ArrayList<ItemListener>();
	protected List<ScreenCursorListener> screenCursorListeners = new ArrayList<ScreenCursorListener>();
	protected List<OrthoControlPointRotateTranslateScaleListener> orthoControlPointRotateTranslateScaleListeners = new ArrayList<OrthoControlPointRotateTranslateScaleListener>();
	protected List<BringToTopListener> bringToTopListeners = new ArrayList<BringToTopListener>();
	protected List<OrthoSnapListener> snapListeners = new ArrayList<OrthoSnapListener>();
	protected List<OrthoFlickListener> flickListeners = new ArrayList<OrthoFlickListener>();

	protected LongHoldDetector longHoldDetector = new LongHoldDetector(1000L, 2f, itemListeners, this.contentItem);

	private boolean singleTouchEnabled;

	public JMEOrthoContentItem(ContentItem contentItem, Spatial spatial) {
		super(contentItem, spatial);

		this.rotateTranslateScalable = ((OrthoContentItem)contentItem).isRotateTranslateScaleEnabled();
		this.bringToTopable = ((OrthoContentItem)contentItem).isBringToTopEnabled();
		orthoCursorEventDispatcher = new OrthoCursorEventDispatcher(spatial);
		if (rotateTranslateScalable)
			orthoControlPointRotateTranslateScale = new OrthoControlPointRotateTranslateScale(spatial);
		if (bringToTopable)
			orthoBringtoTop = new OrthoBringToTop(spatial);
		if (snapable)
			orthoSnap = new OrthoSnap(spatial);
	}

	public void addMultitouchListener(){
		super.addMultitouchListener();
		orthoCursorEventDispatcher = new OrthoCursorEventDispatcher(spatial);
			orthoCursorEventDispatcher.addMultiTouchListener(this);
		if (orthoControlPointRotateTranslateScale!=null)
			orthoControlPointRotateTranslateScale.addRotateTranslateScaleListener(this);
		if (orthoBringtoTop!=null)
			orthoBringtoTop.addOrthoBringToTopListener(this);
		if (orthoSnap!=null)
			orthoSnap.addSnapListener(this);

	}

	@Override
	public void setName(String name){
		super.setName(name);
		if(orthoControlPointRotateTranslateScale != null) orthoControlPointRotateTranslateScale.setName(name);
		if(orthoBringtoTop != null) {
			OrthoBringToTop.unRegister(spatial);
			orthoBringtoTop.setName(name);
		}
		if(orthoSnap != null) orthoSnap.setName(name);
		if(orthoCursorEventDispatcher != null) orthoCursorEventDispatcher.setName(name);
	}

	@Override
	public void setOrder(int order) {
		if (((OrthoContentItem)contentItem).getParent()!=null){
			OrthoContainer parent = (OrthoContainer)(((OrthoContentItem)contentItem).getParent());
			parent.updateOrder(order);
		}
		else
			this.spatial.setZOrder(order, true);

	}


	@Override
	public void setRotateTranslateScalable(boolean isEnabled) {
		this.rotateTranslateScalable = isEnabled;

		//enable multitouch element
		if (this.rotateTranslateScalable) {
			if (this.orthoControlPointRotateTranslateScale != null){
				if (MultiTouchElementRegistry.getInstance().isRegistered(orthoControlPointRotateTranslateScale))
					MultiTouchElementRegistry.getInstance().unregister(this.orthoControlPointRotateTranslateScale);
				this.orthoControlPointRotateTranslateScale = null;
			}

			if (this.orthoControlPointRotateTranslateScale == null){
				if (((OrthoContentItem)contentItem).getParent()!=null){
					if(attachToParent)
						orthoControlPointRotateTranslateScale = new OrthoControlPointRotateTranslateScale(spatial, this.getTopLevelNode(spatial.getParent()));
					else
						orthoControlPointRotateTranslateScale = new IndependentChildOrthoCPRTS(spatial, this.getTopLevelNode(spatial.getParent()));
				}
					else
						orthoControlPointRotateTranslateScale = new OrthoControlPointRotateTranslateScale(spatial);
				orthoControlPointRotateTranslateScale.addRotateTranslateScaleListener(this);
			}
			else
				MultiTouchElementRegistry.getInstance().register(this.orthoControlPointRotateTranslateScale);

		}

		//disable multitouch element
		else {
			if (this.orthoControlPointRotateTranslateScale != null && MultiTouchElementRegistry.getInstance().isRegistered(orthoControlPointRotateTranslateScale)){
				MultiTouchElementRegistry.getInstance().unregister(this.orthoControlPointRotateTranslateScale);
				this.orthoControlPointRotateTranslateScale = null;
			}
		}
	}

	@Override
	public void setRotateTranslateScalable(boolean isEnabled, boolean attachToParent, ContentItem targetItem){
		this.attachToParent = attachToParent;
		setRotateTranslateScalable(isEnabled);
	}

	@Override
	public void allowMoreThanTwoToRotateAndScale(boolean b) {
		this.allowMoreThanTwoToRotateAndScale = b;
		if (orthoControlPointRotateTranslateScale!=null)
			this.orthoControlPointRotateTranslateScale.allowMoreThanTwoToRotateAndScale(b);
	}

	@Override
	public void allowSingleTouchFreeMove(boolean b) {
		this.allowSingleTouchFreeMove = b;
		if (orthoControlPointRotateTranslateScale!=null)
			this.orthoControlPointRotateTranslateScale.allowSingleTouchFreeMove(b);
	}

	@Override
	public void setScaleLimit(float min, float max) {
//		if (steadfastLimit >= 1 && BorderUtility.getDefaultSteadfastLimit() >= 1 && !BorderUtility.isDefaultBorder()){
//			min *= BorderUtility.getVirtualRectangleEnvironmentScale();
//			max *= BorderUtility.getVirtualRectangleEnvironmentScale();
//		}
		
	
		if (orthoControlPointRotateTranslateScale!=null)
			this.orthoControlPointRotateTranslateScale.setScaleLimits(min, max);
	}

	@Override
	public void setZRotateLimit(float min, float max) {
		if (orthoControlPointRotateTranslateScale!=null)
			this.orthoControlPointRotateTranslateScale.setRotateLimits(min, max);
	}

	public void turnOffEventDispatcher(){
		this.orthoCursorEventDispatcher = null;
	}

	public void setRightClickDistance(float distance){
		if (this.orthoCursorEventDispatcher!=null)
			this.orthoCursorEventDispatcher.setRightClickDistance(distance);
	}

	@Override
	public void setBringToTopable(boolean isEnabled) {
		this.bringToTopable = isEnabled;
		if (this.bringToTopable){

			if (this.orthoBringtoTop != null && ((OrthoContentItem)contentItem).getParent()!=null){
				if (MultiTouchElementRegistry.getInstance().isRegistered(orthoBringtoTop)){
					MultiTouchElementRegistry.getInstance().unregister(this.orthoBringtoTop);
					OrthoBringToTop.unRegister(this.orthoBringtoTop);
				}
				this.orthoBringtoTop = null;
			}

			if (this.orthoBringtoTop == null){
				if (((OrthoContentItem)contentItem).getParent()!=null)
					orthoBringtoTop = new OrthoBringToTop(spatial, this.getTopLevelNode(spatial));
				else
					orthoBringtoTop = new OrthoBringToTop(spatial);

				orthoBringtoTop.addOrthoBringToTopListener(this);
			}
			else
				MultiTouchElementRegistry.getInstance().register(this.orthoBringtoTop);

		}
		else{
			if (this.orthoBringtoTop != null && MultiTouchElementRegistry.getInstance().isRegistered(orthoBringtoTop)){
				MultiTouchElementRegistry.getInstance().unregister(this.orthoBringtoTop);
				OrthoBringToTop.unRegister(this.orthoBringtoTop);
			}
			this.orthoBringtoTop =null;
		}
	}

	public void setAsTopObjectAndBroadCastEvent(){
		if (this.orthoBringtoTop!=null){
			this.orthoBringtoTop.setTopObject(this.orthoBringtoTop, true);
		}
	}

	public void setAsTopObject(){
		if (this.orthoBringtoTop!=null){
			this.orthoBringtoTop.setTopObject(this.orthoBringtoTop, false);
		}
	}

	public void setAsBottomObject(){
		if (this.orthoBringtoTop!=null){
			this.orthoBringtoTop.setBottomObject(this.orthoBringtoTop, false);
		}
	}

	public void setSingleTouchRotateTranslate(boolean isEnabled) throws Exception {
		if(!(spatial instanceof TriMesh)) {
			throw new Exception("Can't allow this for non-trimesh");
		}
		this.singleTouchEnabled = isEnabled;
		if(this.singleTouchEnabled) {
			if(this.singleTouch != null && ((OrthoContentItem)contentItem).getParent() != null) {
				if(MultiTouchElementRegistry.getInstance().isRegistered(singleTouch)) {
					MultiTouchElementRegistry.getInstance().unregister(this.singleTouch);
				}
				this.singleTouch = null;
			}

			if(this.singleTouch == null) {
				if(((OrthoContentItem)contentItem).getParent() != null) {
					singleTouch = new SingleTouchRotateTranslate((TriMesh)spatial, this.getTopLevelNode(spatial));
				}else{
					singleTouch = new SingleTouchRotateTranslate((TriMesh)spatial);
				}

				singleTouch.addRotateTranslateScaleListener(this);
			}else{
				MultiTouchElementRegistry.getInstance().register(this.singleTouch);
			}

		}else{
			if(this.singleTouch != null && MultiTouchElementRegistry.getInstance().isRegistered(singleTouch)) {
				MultiTouchElementRegistry.getInstance().unregister(this.singleTouch);
				this.singleTouch = null;
			}
		}
	}

	@Override
	public void setSnapable(boolean isEnabled) {
		this.snapable = isEnabled;
		if (this.snapable){

			if (this.orthoSnap != null && ((OrthoContentItem)contentItem).getParent()!=null){
				if (MultiTouchElementRegistry.getInstance().isRegistered(orthoSnap)){
					MultiTouchElementRegistry.getInstance().unregister(this.orthoSnap);
				}
				this.orthoSnap = null;
			}

			if (this.orthoSnap == null){
				if (((OrthoContentItem)contentItem).getParent()!=null)
					orthoSnap = new OrthoSnap(spatial, this.getTopLevelNode(spatial));
				else
					orthoSnap = new OrthoSnap(spatial);

				orthoSnap.addSnapListener(this);
			}
			else
				MultiTouchElementRegistry.getInstance().register(this.orthoSnap);

		}
		else{
			if (this.orthoSnap != null && MultiTouchElementRegistry.getInstance().isRegistered(orthoSnap))
				MultiTouchElementRegistry.getInstance().unregister(this.orthoSnap);
				this.orthoSnap =null;

		}

	}

	@Override
	public void allowSnapToOccupiedLocation(boolean allowSnapToOccupiedLocation) {
		this.orthoSnap.allowSnapToOccupiedLocation(allowSnapToOccupiedLocation);
	}

	@Override
	public void setFixLocations(List<FixLocationStatus> fixLocations) {
		if (this.orthoSnap!=null)
			this.orthoSnap.setFixLocations(fixLocations);
	}

	@Override
	public void setTolerance(float tolerance) {
		if (this.orthoSnap!=null)
			this.orthoSnap.setTolerance(tolerance);
	}

	public void setLocalLocation(Location location) {
		float x = location.getX();
		float y = location.getY();
		float z = location.getZ();
		
		if (this.isBoundaryEnabled){
			if (x<0) x = 0;
			if (y<0) y = 0;
			if (x > this.screenWidth) x = this.screenWidth;
			if (y > this.screenHeigth) y = this.screenHeigth;
		}
		this.spatial.setLocalTranslation(x, y, z);
		this.spatial.updateGeometricState(0f, true);
	}



	public void centerItem() {
		this.contentItem.setLocalLocation(this.screenWidth/2, this.screenHeigth/2);
	}

	public void placeRandom() {
		Random r = new Random();
		int x = r.nextInt(this.screenWidth);
		int y = r.nextInt(this.screenHeigth);
		this.contentItem.setLocalLocation(x, y);
	}

	public void rotateRandom() {
		Random r = new Random();
		this.contentItem.setAngle((float)(Math.PI * 2 * r.nextFloat()));
		this.spatial.updateGeometricState(0f, true);
	}



	//add listener
	@Override
	public void addBringToTopListener(BringToTopListener l) {

		if (this.bringToTopListeners==null)
			bringToTopListeners = new ArrayList<BringToTopListener>();
		this.bringToTopListeners.add(l);

	}

	@Override
	public void addItemListener(ItemListener l) {
		this.itemListeners.add(l);

	}

	@Override
	public void addScreenCursorListener(ScreenCursorListener l) {
		this.screenCursorListeners.add(l);

	}

	@Override
	public void removeScreenCursorListeners() {
		this.screenCursorListeners.clear();

	}



	@Override
	public void addOrthoControlPointRotateTranslateScaleListener(
			OrthoControlPointRotateTranslateScaleListener l) {
		if (this.orthoControlPointRotateTranslateScaleListeners==null)
			orthoControlPointRotateTranslateScaleListeners = new ArrayList<OrthoControlPointRotateTranslateScaleListener>();
		this.orthoControlPointRotateTranslateScaleListeners.add(l);

	}

	@Override
	public void removeBringToTopListeners(BringToTopListener l) {
		if (this.bringToTopListeners.contains(l))
			this.bringToTopListeners.remove(l);

	}

	@Override
	public void removeFlickListeners(OrthoFlickListener l) {
		this.flickListeners.add(l);

	}

	@Override
	public void removeOrthoControlPointRotateTranslateScaleListeners(
			OrthoControlPointRotateTranslateScaleListener l) {
		if (this.orthoControlPointRotateTranslateScaleListeners.contains(l))
			this.orthoControlPointRotateTranslateScaleListeners.remove(l);

	}

	@Override
	public void removeSnapListeners(OrthoSnapListener l) {
		this.snapListeners.add(l);

	}

	@Override
	public void addSnapListener(OrthoSnapListener l) {
		this.snapListeners.add(l);

	}


	@Override
	public void addFlickListener(OrthoFlickListener l) {
		this.flickListeners.add(l);
	}

	@Override
	public void removeItemListerner(ItemListener l) {
		if (this.itemListeners.contains(l))
			this.itemListeners.remove(l);

	}
	
	//item cursor events
	@Override
	public void cursorChanged(
			
			OrthoCursorEventDispatcher commonCursorEventDispatcher,
			ScreenCursor c, MultiTouchCursorEvent event) {			
		for (ItemListener l: this.itemListeners)
			l.cursorChanged(this.contentItem, event.getCursorID(), event.getPosition().x, event.getPosition().y, event.getPressure());

	}

	@Override
	public void cursorClicked(
			OrthoCursorEventDispatcher commonCursorEventDispatcher,
			ScreenCursor c, MultiTouchCursorEvent event) {
		for (ItemListener l: this.itemListeners){
			if (event.getClickCount()==2){
				l.cursorDoubleClicked(this.contentItem, event.getCursorID(), event.getPosition().x, event.getPosition().y, event.getPressure());
			}
			else{
				l.cursorClicked(this.contentItem, event.getCursorID(), event.getPosition().x, event.getPosition().y, event.getPressure());
			}
		}
	}

	@Override
	public void cursorPressed(
			OrthoCursorEventDispatcher commonCursorEventDispatcher,
			ScreenCursor c, MultiTouchCursorEvent event) {
		for (ItemListener l: this.itemListeners)
			l.cursorPressed(this.contentItem, event.getCursorID(), event.getPosition().x, event.getPosition().y, event.getPressure());
	}

	@Override
	public void cursorReleased(
			OrthoCursorEventDispatcher commonCursorEventDispatcher,
			ScreenCursor c, MultiTouchCursorEvent event) {

		for (ItemListener l: this.itemListeners)
			l.cursorReleased(this.contentItem, event.getCursorID(), event.getPosition().x, event.getPosition().y, event.getPressure());

	}


	@Override
	public void cursorRightClicked(
			OrthoCursorEventDispatcher commonCursorEventDispatcher,
			ScreenCursor c, MultiTouchCursorEvent event) {
		for (ItemListener l: this.itemListeners)
			l.cursorRightClicked(this.contentItem, event.getCursorID(), event.getPosition().x, event.getPosition().y, event.getPressure());

	}


	@Override
	public void screenCursorChanged(ContentItem item, long id, float x,
			float y, float pressure) {
		((OrthoContentItem) item).fireScreenCursorChanged(id, x, y, pressure);

	}

	@Override
	public void screenCursorClicked(ContentItem item, long id, float x,
			float y, float pressure) {
		((OrthoContentItem) item).fireScreenCursorClicked(id, x, y, pressure);
	}

	@Override
	public void screenCursorPressed(ContentItem item, long id, float x,
			float y, float pressure) {
		((OrthoContentItem) item).fireScreenCursorPressed(id, x, y, pressure);

	}

	@Override
	public void screenCursorReleased(ContentItem item, long id, float x,
			float y, float pressure) {
		((OrthoContentItem) item).fireScreenCursorReleased(id, x, y, pressure);
	}


	//rotate, move, scale event
	@Override
	public void itemMoved(
			OrthoControlPointRotateTranslateScale multiTouchElement, Spatial targetSpatial,
			float newLocationX, float newLocationY, float oldLocationX,
			float oldLocationY) {

		ContentItem item = ((JMEContentSystem)(contentItem.getContentSystem())).getContentItem(targetSpatial);

		item.setLocalLocation(newLocationX, newLocationY);
		for (OrthoControlPointRotateTranslateScaleListener l: this.orthoControlPointRotateTranslateScaleListeners)
			l.itemTranslated(item, newLocationX, newLocationY, oldLocationX, oldLocationY);
	}

	@Override
	public void itemRotated(
			OrthoControlPointRotateTranslateScale multiTouchElement, Spatial targetSpatial,
			float newAngle, float oldAngle) {

		ContentItem item = ((JMEContentSystem)(contentItem.getContentSystem())).getContentItem(targetSpatial);
		item.setAngle(newAngle);
		for (OrthoControlPointRotateTranslateScaleListener l: this.orthoControlPointRotateTranslateScaleListeners)
			l.itemRotated(item, newAngle, oldAngle);
	}

	@Override
	public void itemScaled(
			OrthoControlPointRotateTranslateScale multiTouchElement, Spatial targetSpatial,
			float scaleChange) {

		ContentItem item = ((JMEContentSystem)(contentItem.getContentSystem())).getContentItem(targetSpatial);
		float oldScale = item.getScale();
		for (OrthoControlPointRotateTranslateScaleListener l: this.orthoControlPointRotateTranslateScaleListeners)
			l.itemScaled(item, oldScale*scaleChange, oldScale);
		item.setScale(oldScale*scaleChange);
	}

	@Override
	public void ItemBringToTop() {
		List<Spatial> allSpatialsWithOrthoBringToTop = OrthoBringToTop.getAllTopLevelContainersWithOrthoBringToTop();
		JMEContentSystem contentSystem =(JMEContentSystem)contentItem.getContentSystem();
		for (Spatial spatial: allSpatialsWithOrthoBringToTop){
			OrthoContentItem item =(OrthoContentItem)contentSystem.getContentItem(spatial);
			if (item!=null){
				item.setOrder(spatial.getZOrder());
				if (item instanceof OrthoContainer){
					((OrthoContainer)item).updateOrder(0);
				}
			}
		}

		for (BringToTopListener l: this.bringToTopListeners)
			l.itemBringToToped(contentItem);
	}

	@Override
	public void itemSnapped(OrthoSnap multiTouchElement, Spatial targetSpatial,
			FixLocationStatus fixLocationStatus) {
		ContentItem item = ((JMEContentSystem)(contentItem.getContentSystem())).getContentItem(targetSpatial);
		item.setLocalLocation(targetSpatial.getLocalTranslation().x, targetSpatial.getLocalTranslation().y);
		for (OrthoSnapListener l: this.snapListeners)
			l.itemSnapped(item, fixLocationStatus);

	}

	@Override
	public void itemFlicked(FlickMover multiTouchElement, Spatial targetSpatial,float newLocationX, float newLocationY, float oldLocationX,	float oldLocationY){
		ContentItem item = ((JMEContentSystem)(contentItem.getContentSystem())).getContentItem(targetSpatial);
		item.setLocalLocation(newLocationX, newLocationY);
		for (OrthoControlPointRotateTranslateScaleListener l: this.orthoControlPointRotateTranslateScaleListeners)
			l.itemTranslated(item, newLocationX, newLocationY, oldLocationX, oldLocationY);
	}

	protected Spatial getTopLevelNode(Spatial spatial){

		if (contentItem.getContentSystem().isTopLevelContainer(spatial))
			return spatial;
		else
			return this.getTopLevelNode(spatial.getParent());
	}

	public void makeFlickable(float deceleration){

		FlickSystem.getInstance().makeFlickable(this.spatial, this.getTopLevelNode(spatial), this.orthoControlPointRotateTranslateScale, deceleration);
		FlickSystem.getInstance().getMovingElement(this.getTopLevelNode(spatial)).addFlickListener(this);
	}

	public void makeUnflickable(){
		if(this.isFlickable()){
			FlickSystem.getInstance().getMovingElement(this.spatial).removeFlickListener(this);
			FlickSystem.getInstance().makeUnflickable(this.spatial);
		}
	}

	public boolean isFlickable(){
		return FlickSystem.getInstance().isFlickable(this.spatial);
	}

	public void flick(float velocityX, float velocityY, float deceleration){
		FlickSystem.getInstance().flick(this.spatial, new Vector3f(velocityX, velocityY, 0), deceleration);
	}

	protected void updateContainerGestureStatus(){
		this.setRotateTranslateScalable(((OrthoContentItem)contentItem).isRotateTranslateScaleEnabled());
		this.setBringToTopable(((OrthoContentItem)contentItem).isBringToTopEnabled());
		this.setSnapable(((OrthoContentItem)contentItem).isSnapEnabled());
	}

	private ContentItem getTopLevelContainer(ContentItem item){

		if (((OrthoContentItem)item).getParent()==null)
			return item;
		else
			return getTopLevelContainer(((OrthoContentItem)item).getParent());

	}

	protected void updateContainerListeners(ContentItem item){

		if (((OrthoContentItem)item).getParent()!=null){
			System.out.println(((OrthoContentItem)item).getParent());
			if (!((OrthoContentItem)item).getOrthoControlPointRotateTranslateScaleListeners().contains((OrthoContentItem)(this.getTopLevelContainer(contentItem))))
				((OrthoContentItem)item).addOrthoControlPointRotateTranslateScaleListener((OrthoContentItem)(this.getTopLevelContainer(contentItem)));
			if (!((OrthoContentItem)item).getBringToTopListeners().contains((OrthoContentItem)(this.getTopLevelContainer(contentItem))))
				((OrthoContentItem)item).addBringToTopListener(((OrthoContentItem)(this.getTopLevelContainer(contentItem))));
			if (!((OrthoContentItem)item).getOrthoSnapListeners().contains((OrthoContentItem)(this.getTopLevelContainer(contentItem))))
				((OrthoContentItem)item).addSnapListener((OrthoContentItem)(this.getTopLevelContainer(contentItem)));
		}
		else{
			if (this.getTopLevelContainer(item).hashCode()!= item.hashCode()){
				if (!((OrthoContentItem)item).getOrthoControlPointRotateTranslateScaleListeners().contains((OrthoContentItem)(this.getTopLevelContainer(contentItem))))
					((OrthoContentItem)item).removeOrthoControlPointRotateTranslateScaleListeners((OrthoContentItem)(this.getTopLevelContainer(contentItem)));
				if (!((OrthoContentItem)item).getBringToTopListeners().contains((OrthoContentItem)(this.getTopLevelContainer(contentItem))))
					((OrthoContentItem)item).removeBringToTopListeners(((OrthoContentItem)(this.getTopLevelContainer(contentItem))));
				if (!((OrthoContentItem)item).getOrthoSnapListeners().contains((OrthoContentItem)(this.getTopLevelContainer(contentItem))))
					((OrthoContentItem)item).removeSnapListeners((OrthoContentItem)(this.getTopLevelContainer(contentItem)));
			}
		}
	}

	public void update(){
		this.setOrder(((OrthoContentItem)contentItem).getOrder());
	}

	@Override
	public void setBackGround(Background backGround) {}

	@Override
	public void setBorder(Border border) {}

	@Override
	public void update(float interpolation) {
		super.update(interpolation);
		this.longHoldDetector.update(interpolation);
	}

	@Override
	public void reset() {}

	@Override
	public float getMaxScale() {
		if(orthoControlPointRotateTranslateScale == null) return 0;
		return orthoControlPointRotateTranslateScale.getScaleMax();
	}

	@Override
	public float getMinScale() {
		if(orthoControlPointRotateTranslateScale == null) return 0;
		return orthoControlPointRotateTranslateScale.getScaleMin();
	}

}
