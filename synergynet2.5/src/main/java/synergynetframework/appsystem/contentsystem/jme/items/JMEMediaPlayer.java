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

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.llama.jmf.JMFVideoImage;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.WrapMode;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

import controlbar.ControlbarResources;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ControlBar;
import synergynetframework.appsystem.contentsystem.items.MediaPlayer;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.RoundImageLabel;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.VideoPlayer;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IMediaPlayerImplementation;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;
import synergynetframework.appsystem.contentsystem.jme.UpdateableJMEContentItemImplementation;
import synergynetframework.appsystem.contentsystem.jme.items.utils.controlbar.ControlBarMover.ControlBarMoverListener;

public class JMEMediaPlayer extends JMEOrthoContainer implements IMediaPlayerImplementation, UpdateableJMEContentItemImplementation{
	
	protected MediaPlayer mediaPlayer;
	
	protected VideoPlayer videoPlayer;
	protected ControlBar progressBar;
	protected ControlBar speedBar;
	protected RoundImageLabel speedLabel;
	protected ControlBar soundVolumeBar;
	protected RoundImageLabel soundLabel;
	protected RoundImageLabel soundOffLabel;
	protected RoundImageLabel playButton;
	protected RoundImageLabel stopButton;
	protected TextLabel timeLabel;
	protected Quad frame;
	
	protected float controlPanelShowTime = 0;
	
	protected JMFVideoImage jMEVideoImage;
	protected boolean isControlPanelOn=true;
	protected boolean isStarted = false;
	protected List<PlayListener> playListeners = new ArrayList<PlayListener>();
	
	public JMEMediaPlayer(ContentItem contentItem) {
		super(contentItem);		
		mediaPlayer = (MediaPlayer)contentItem;
	}
	
	public void update(float interpolation) {
			
		super.update(interpolation);
		
		if (jMEVideoImage==null || !isControlPanelOn ||!isStarted) return;
		
		if (controlPanelShowTime<10){
			controlPanelShowTime=(float) (controlPanelShowTime+interpolation);
		}
		else{
			this.hideControlPanel();
			controlPanelShowTime = 0;
		} 
		
		if (isControlPanelOn){
			progressBar.setCurrentPosition((float)(jMEVideoImage.getMediaTime().getSeconds()/jMEVideoImage.getDuration().getSeconds()));
			timeLabel.setText(getLeftTimeString());
		}
	}

	public void init(){
		super.init();
		
		buildVideo();
		buildProgressBar();
		buildTimeLabel();	
		//buildSpeedBar();
		buildSoundVolumeBar();
		buildPlayButton();
		buildStopButton();
		buildFrame();
			
		mediaPlayer.addSubItem(progressBar);
		//mediaPlayer.addSubItem(speedBar);
		//mediaPlayer.addSubItem(speedLabel);
		mediaPlayer.addSubItem(soundVolumeBar);
		mediaPlayer.addSubItem(soundLabel);
		mediaPlayer.addSubItem(soundOffLabel);
		mediaPlayer.addSubItem(playButton);
		mediaPlayer.addSubItem(stopButton);
		mediaPlayer.addSubItem(timeLabel);
		mediaPlayer.addSubItem(videoPlayer);
		((Spatial)mediaPlayer.getImplementationObject()).updateRenderState();
	}
	
	public void render(){
		jMEVideoImage = (JMFVideoImage)videoPlayer.getMediaImplementationObject();		
			
		float mediaNodeWidth=150;
		float mediaNodeHeight = 100;
		if (jMEVideoImage!=null){
			mediaNodeWidth = jMEVideoImage.getVideoWidth()/videoPlayer.getPixelsPerUnit();
			mediaNodeHeight = jMEVideoImage.getVideoHeight()/videoPlayer.getPixelsPerUnit();
		}
		
		placeProgressBar(mediaNodeWidth, mediaNodeHeight);
		//placeSpeedBar(mediaNodeWidth, mediaNodeHeight);
		placeSoundVolumeBar(mediaNodeWidth, mediaNodeHeight);		
		placeFrame(mediaNodeWidth, mediaNodeHeight);
		
		timeLabel.setText(this.getLeftTimeString());
		
		soundVolumeBar.updateControlBar();
		
	}
	
	public void showControlPanel(){
		
		isControlPanelOn=true;
		progressBar.setVisible(true);
		//speedBar.setVisible(true);
		//speedLabel.setVisible(true);
		soundVolumeBar.setVisible(true);
		if (!jMEVideoImage.isMute()){
			soundLabel.setVisible(true);
			soundOffLabel.setVisible(false);
		}
		else{
			soundLabel.setVisible(false);
			soundOffLabel.setVisible(true);
		}
		timeLabel.setVisible(true);
		if (videoPlayer.isPlaying())
			stopButton.setVisible(true);
		else
			playButton.setVisible(true);
	}
	
	public void hideControlPanel(){
		isControlPanelOn=false;
		progressBar.setVisible(false);
		//speedBar.setVisible(false);
		//speedLabel.setVisible(false);
		soundVolumeBar.setVisible(false);
		soundLabel.setVisible(false);
		soundOffLabel.setVisible(false);
		playButton.setVisible(false);
		stopButton.setVisible(false);
		timeLabel.setVisible(false);
	}
	
	private void buildVideo(){
		videoPlayer = (VideoPlayer)this.contentItem.getContentSystem().createContentItem(VideoPlayer.class);
	
		videoPlayer.addItemListener(new ItemEventAdapter(){		
			public void cursorDoubleClicked(ContentItem item, long id, float x, float y,
					float pressure) {	
				Rectangle rightBottomCorner = new Rectangle(videoPlayer.getWidth()-40, videoPlayer.getHeight()-40, videoPlayer.getWidth(), videoPlayer.getHeight());
				if (rightBottomCorner.contains(x, videoPlayer.getHeight()-y))return;
				if (isControlPanelOn){
					hideControlPanel();
				}
				else{
					showControlPanel();
				}
			}
		});
	
	}
	
	private void buildProgressBar(){
		progressBar = (ControlBar)this.contentItem.getContentSystem().createContentItem(ControlBar.class);
		//progressBar.setControlBarMoverEnabled(false);
		
		progressBar.addControlBarMoverListener(new ControlBarMoverListener(){
			public void controlBarChanged(float oldPosition, float newPosition) {
				//jMEVideoImage = (JMFVideoImage)videoPlayer.getMediaImplementationObject();	
				//Time time = new Time(newPosition*jMEVideoImage.getDuration().getNanoseconds());
				//jMEVideoImage.seek(time);
			}
			public void cursorPressed() {}
			public void cursorReleased() {}		
		});
		
	}
	
	private void buildSoundVolumeBar(){
		soundLabel = (RoundImageLabel)this.contentItem.getContentSystem().createContentItem(RoundImageLabel.class);
		soundLabel.setImageInfo(ControlbarResources.class.getResource("sound.png"));
		soundLabel.setBorderSize(4);
		soundLabel.setRadius(15);
		soundLabel.addItemListener(new ItemEventAdapter(){
			public void cursorClicked(ContentItem item, long id, float x, float y,
					float pressure) {	
				jMEVideoImage.setMute(true);
				soundLabel.setVisible(false);
				soundOffLabel.setVisible(true);
				
				controlPanelShowTime=0;
				
			}
		});
		
		soundOffLabel = (RoundImageLabel)this.contentItem.getContentSystem().createContentItem(RoundImageLabel.class);
		soundOffLabel.setImageInfo(ControlbarResources.class.getResource("mute.png"));
		soundOffLabel.setBorderSize(4);
		soundOffLabel.setRadius(15);
		soundOffLabel.addItemListener(new ItemEventAdapter(){
			public void cursorClicked(ContentItem item, long id, float x, float y,
					float pressure) {	
				jMEVideoImage.setMute(false);
				soundLabel.setVisible(true);
				soundOffLabel.setVisible(false);
				
				controlPanelShowTime=0;
				
			}
		});
		
		soundVolumeBar = (ControlBar)this.contentItem.getContentSystem().createContentItem(ControlBar.class);
		soundVolumeBar.addControlBarMoverListener(new ControlBarMoverListener(){
			public void controlBarChanged(float oldPosition, float newPosition) {
					if (jMEVideoImage!=null)
						jMEVideoImage.setSoundVolumeLevel(newPosition*0.7f);
					
					controlPanelShowTime=0;
			}	
			public void cursorPressed() {}
			public void cursorReleased() {}	
		});
	}
	
	@SuppressWarnings("unused")
	private void buildSpeedBar(){
		speedLabel = (RoundImageLabel)this.contentItem.getContentSystem().createContentItem(RoundImageLabel.class);
		speedLabel.setImageInfo(ControlbarResources.class.getResource("speed.png"));
		speedLabel.setBorderSize(1);
		speedLabel.addItemListener(new ItemEventAdapter(){
			public void cursorClicked(ContentItem item, long id, float x, float y,
					float pressure) {			
				
			}
		});
		
		speedBar = (ControlBar)this.contentItem.getContentSystem().createContentItem(ControlBar.class);
		speedBar.addControlBarMoverListener(new ControlBarMoverListener(){
			public void controlBarChanged(float oldPosition, float newPosition) {
							
			}	
			public void cursorPressed() {}
			public void cursorReleased() {}	
		});
	}
	
	private void buildTimeLabel(){
		timeLabel = (TextLabel)this.contentItem.getContentSystem().createContentItem(TextLabel.class);
		timeLabel.setWidth(50);
		timeLabel.setHeight(30);
		timeLabel.setBackgroundColour(Color.black);
		timeLabel.setTextColour(Color.white);
		timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		timeLabel.setBorderSize(1);
	}
	
	private void buildPlayButton(){
		playButton = (RoundImageLabel)this.contentItem.getContentSystem().createContentItem(RoundImageLabel.class);		
		playButton.setRadius(30);
		playButton.setImageInfo(ControlbarResources.class.getResource("playbutton.png"));
		playButton.setBorderSize(4);
		playButton.addItemListener(new ItemEventAdapter(){
			public void cursorClicked(ContentItem item, long id, float x, float y,
					float pressure) {			
				JMEMediaPlayer.this.setPlaying(true);
				for(PlayListener l: playListeners){
					l.videoPlayed();
				}
			}
		});
	}
	
	private void buildStopButton(){
		stopButton = (RoundImageLabel)this.contentItem.getContentSystem().createContentItem(RoundImageLabel.class);		
		stopButton.setRadius(30);
		stopButton.setImageInfo(ControlbarResources.class.getResource("stopbutton.png"));
		stopButton.setBorderSize(4);
		stopButton.addItemListener(new ItemEventAdapter(){
			public void cursorClicked(ContentItem item, long id, float x, float y,
					float pressure) {			
				JMEMediaPlayer.this.setPlaying(false);
				for(PlayListener l: playListeners){
					l.videoStopped();
				}
			}
		});
	}
	
	private void buildFrame(){
			
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);		
		Texture texture = TextureManager.loadTexture(ControlbarResources.class.getResource("frame.png"), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		ts.setTexture( texture );
		ts.apply();
		
		frame = new Quad(mediaPlayer.getName()+"-TopBar", 1, 1);
		
		frame.setRenderState(ts );	
		frame.updateRenderState();
		frame.setModelBound(new OrthogonalBoundingBox());
		frame.updateGeometricState(0f, false);
		frame.updateModelBound();
		
		Node node = (Node)mediaPlayer.getImplementationObject();
		node.attachChild(frame);
		node.updateGeometricState(0f, false);
		node.updateModelBound();
	}
	
	private void placeFrame(float mediaNodeWidth, float mediaNodeHeight ){
		frame.updateGeometry(mediaNodeWidth+mediaPlayer.getBorderSize()*2, mediaNodeHeight+mediaPlayer.getBorderSize()*2);
		frame.updateModelBound();
		
		
	}
	
	public OrthoContentItem getPlayerFrame(){
		return videoPlayer;
	}
	
	private void placeProgressBar(float mediaNodeWidth, float mediaNodeHeight ){	
		timeLabel.setLocalLocation(mediaNodeWidth-20-timeLabel.getWidth()/2-mediaNodeWidth/2, (float)((0.1-1)*mediaNodeHeight/2));
		progressBar.setControlBarLength(mediaNodeWidth-38-timeLabel.getWidth()*2);
		progressBar.setLocalLocation((float)(progressBar.getControlBarLength()/2+8-mediaNodeWidth/2) , (float)((0.1-1)*mediaNodeHeight/2));
		
	}
	
	@SuppressWarnings("unused")
	private void placeSpeedBar(float mediaNodeWidth, float mediaNodeHeight){
		speedBar.setControlBarLength((float)0.35*mediaNodeWidth);
		speedBar.setLocalLocation((float)(speedBar.getControlBarLength()/2+0.11*mediaNodeWidth-mediaNodeWidth/2) , (float)((0.9)*mediaNodeHeight/2));
			
		speedLabel.getImageInfo().setHeight((int)(mediaNodeWidth*0.02));
		speedLabel.setLocalLocation((float)(speedLabel.getRadius()+0.04*mediaNodeWidth-mediaNodeWidth/2) , (float)((0.9)*mediaNodeHeight/2));
	}
	
	private void placeSoundVolumeBar(float mediaNodeWidth, float mediaNodeHeight ){
		soundVolumeBar.setControlBarLength((float)0.25*mediaNodeWidth);
		soundVolumeBar.setLocalLocation((float)(soundVolumeBar.getControlBarLength()/2+0.65*mediaNodeWidth-mediaNodeWidth/2) , (float)((0.8)*mediaNodeHeight/2));
		
		soundLabel.getImageInfo().setHeight((int)(mediaNodeWidth*0.02));
		soundLabel.setLocalLocation((float)(soundLabel.getRadius()+0.54*mediaNodeWidth-mediaNodeWidth/2) , (float)((0.8)*mediaNodeHeight/2));
		
		soundOffLabel.getImageInfo().setHeight((int)(mediaNodeWidth*0.02));
		soundOffLabel.setLocalLocation((float)(soundLabel.getRadius()+0.54*mediaNodeWidth-mediaNodeWidth/2) , (float)((0.8)*mediaNodeHeight/2));
	}

	@Override
	public void setMediaURL(URL url) {
		videoPlayer.setVideoURL(url);
		render();	

		if (jMEVideoImage!=null)
			this.soundVolumeBar.setCurrentPosition(jMEVideoImage.getSoundVolumeLevel());
		
		videoPlayer.stop();
		
	}

	@Override
	public void setPixelsPerUnit(float ppu) {
		videoPlayer.setPixelsPerUnit(ppu);
		render();		
		videoPlayer.stop();
	}

	@Override
	public void setPlaying(boolean b) {
		if(b){
			videoPlayer.play();
			playButton.setVisible(false);	
			stopButton.setVisible(true);
			
			hideControlPanel();
			isStarted=true;
		}else{
			videoPlayer.stop();
			stopButton.setVisible(false);	
			playButton.setVisible(true);
		}
	}

	//UNUSED
	@Override
	public void setAutoFitSize(boolean isEnabled) {
		 
		
	}

	@Override
	public void setHeight(int height) {
		 
		
	}

	@Override
	public void setWidth(int width) {
		 
		
	}

	@Override
	public Spatial getSpatial() {
		return null;
	}	
	
	private String getLeftTimeString(){
		int leftTime = (int)(jMEVideoImage.getDuration().getSeconds()-jMEVideoImage.getMediaTime().getSeconds());
		int leftMin = leftTime/60;
		int leftSec = leftTime%60;
		String leftMinString="";
		String leftSecString="";
		if (leftMin<10) 
			leftMinString="0"+leftMin;
		else
			leftMinString= String.valueOf(leftMin);
		if (leftSec<10)
			leftSecString="0"+leftSec;
		else
			leftSecString = String.valueOf(leftSec);
		return leftMinString+":"+leftSecString;
	}
	
	@Override
	public void makeFlickable(float deceleration){
		videoPlayer.makeFlickable(deceleration);
	}

	@Override
	public VideoPlayer getVideoPlayer() {
		return videoPlayer;
	}
	
	public interface PlayListener{
		public void videoPlayed();
		public void videoStopped();
	}

	@Override
	public void addPlayerListener(PlayListener l) {
		playListeners.add(l);
	}

	@Override
	public void removePlayListener(PlayListener l) {
		playListeners.remove(l);		
	}

	@Override
	public void removePlayListeners() {
		playListeners.clear();		
	}
}
