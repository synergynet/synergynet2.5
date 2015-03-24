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

package synergynetframework.appsystem.contentsystem.jme.items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.llama.jmf.JMFVideoImage;

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

/**
 * The Class JMEMediaPlayer.
 */
public class JMEMediaPlayer extends JMEOrthoContainer implements
		IMediaPlayerImplementation, UpdateableJMEContentItemImplementation {

	/**
	 * The listener interface for receiving play events. The class that is
	 * interested in processing a play event implements this interface, and the
	 * object created with that class is registered with a component using the
	 * component's <code>addPlayListener<code> method. When
	 * the play event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see PlayEvent
	 */
	public interface PlayListener {

		/**
		 * Video played.
		 */
		public void videoPlayed();

		/**
		 * Video stopped.
		 */
		public void videoStopped();
	}

	/** The control panel show time. */
	protected float controlPanelShowTime = 0;

	/** The frame. */
	protected Quad frame;

	/** The is control panel on. */
	protected boolean isControlPanelOn = true;

	/** The is started. */
	protected boolean isStarted = false;

	/** The j me video image. */
	protected JMFVideoImage jMEVideoImage;

	/** The media player. */
	protected MediaPlayer mediaPlayer;

	/** The play button. */
	protected RoundImageLabel playButton;

	/** The play listeners. */
	protected List<PlayListener> playListeners = new ArrayList<PlayListener>();

	/** The progress bar. */
	protected ControlBar progressBar;

	/** The sound label. */
	protected RoundImageLabel soundLabel;

	/** The sound off label. */
	protected RoundImageLabel soundOffLabel;

	/** The sound volume bar. */
	protected ControlBar soundVolumeBar;

	/** The speed bar. */
	protected ControlBar speedBar;

	/** The speed label. */
	protected RoundImageLabel speedLabel;

	/** The stop button. */
	protected RoundImageLabel stopButton;

	/** The time label. */
	protected TextLabel timeLabel;

	/** The video player. */
	protected VideoPlayer videoPlayer;

	/**
	 * Instantiates a new JME media player.
	 *
	 * @param contentItem
	 *            the content item
	 */
	public JMEMediaPlayer(ContentItem contentItem) {
		super(contentItem);
		mediaPlayer = (MediaPlayer) contentItem;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IMediaPlayerImplementation#addPlayerListener(synergynetframework.appsystem
	 * .contentsystem.jme.items.JMEMediaPlayer.PlayListener)
	 */
	@Override
	public void addPlayerListener(PlayListener l) {
		playListeners.add(l);
	}

	/**
	 * Builds the frame.
	 */
	private void buildFrame() {
		
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);
		Texture texture = TextureManager.loadTexture(
				ControlbarResources.class.getResource("frame.png"),
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		ts.setTexture(texture);
		ts.apply();

		frame = new Quad(mediaPlayer.getName() + "-TopBar", 1, 1);

		frame.setRenderState(ts);
		frame.updateRenderState();
		frame.setModelBound(new OrthogonalBoundingBox());
		frame.updateGeometricState(0f, false);
		frame.updateModelBound();

		Node node = (Node) mediaPlayer.getImplementationObject();
		node.attachChild(frame);
		node.updateGeometricState(0f, false);
		node.updateModelBound();
	}

	/**
	 * Builds the play button.
	 */
	private void buildPlayButton() {
		playButton = (RoundImageLabel) this.contentItem.getContentSystem()
				.createContentItem(RoundImageLabel.class);
		playButton.setRadius(30);
		playButton.setImageInfo(ControlbarResources.class
				.getResource("playbutton.png"));
		playButton.setBorderSize(4);
		playButton.addItemListener(new ItemEventAdapter() {
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				JMEMediaPlayer.this.setPlaying(true);
				for (PlayListener l : playListeners) {
					l.videoPlayed();
				}
			}
		});
	}

	/**
	 * Builds the progress bar.
	 */
	private void buildProgressBar() {
		progressBar = (ControlBar) this.contentItem.getContentSystem()
				.createContentItem(ControlBar.class);
		// progressBar.setControlBarMoverEnabled(false);

		progressBar.addControlBarMoverListener(new ControlBarMoverListener() {
			public void controlBarChanged(float oldPosition, float newPosition) {
				// jMEVideoImage =
				// (JMFVideoImage)videoPlayer.getMediaImplementationObject();
				// Time time = new
				// Time(newPosition*jMEVideoImage.getDuration().getNanoseconds());
				// jMEVideoImage.seek(time);
			}
			
			public void cursorPressed() {
			}
			
			public void cursorReleased() {
			}
		});

	}

	/**
	 * Builds the sound volume bar.
	 */
	private void buildSoundVolumeBar() {
		soundLabel = (RoundImageLabel) this.contentItem.getContentSystem()
				.createContentItem(RoundImageLabel.class);
		soundLabel.setImageInfo(ControlbarResources.class
				.getResource("sound.png"));
		soundLabel.setBorderSize(4);
		soundLabel.setRadius(15);
		soundLabel.addItemListener(new ItemEventAdapter() {
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				jMEVideoImage.setMute(true);
				soundLabel.setVisible(false);
				soundOffLabel.setVisible(true);

				controlPanelShowTime = 0;

			}
		});

		soundOffLabel = (RoundImageLabel) this.contentItem.getContentSystem()
				.createContentItem(RoundImageLabel.class);
		soundOffLabel.setImageInfo(ControlbarResources.class
				.getResource("mute.png"));
		soundOffLabel.setBorderSize(4);
		soundOffLabel.setRadius(15);
		soundOffLabel.addItemListener(new ItemEventAdapter() {
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				jMEVideoImage.setMute(false);
				soundLabel.setVisible(true);
				soundOffLabel.setVisible(false);

				controlPanelShowTime = 0;

			}
		});

		soundVolumeBar = (ControlBar) this.contentItem.getContentSystem()
				.createContentItem(ControlBar.class);
		soundVolumeBar
				.addControlBarMoverListener(new ControlBarMoverListener() {
					public void controlBarChanged(float oldPosition,
							float newPosition) {
						if (jMEVideoImage != null) {
							jMEVideoImage
									.setSoundVolumeLevel(newPosition * 0.7f);
						}
						
						controlPanelShowTime = 0;
					}
					
					public void cursorPressed() {
					}
					
					public void cursorReleased() {
					}
				});
	}

	/**
	 * Builds the speed bar.
	 */
	@SuppressWarnings("unused")
	private void buildSpeedBar() {
		speedLabel = (RoundImageLabel) this.contentItem.getContentSystem()
				.createContentItem(RoundImageLabel.class);
		speedLabel.setImageInfo(ControlbarResources.class
				.getResource("speed.png"));
		speedLabel.setBorderSize(1);
		speedLabel.addItemListener(new ItemEventAdapter() {
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				
			}
		});

		speedBar = (ControlBar) this.contentItem.getContentSystem()
				.createContentItem(ControlBar.class);
		speedBar.addControlBarMoverListener(new ControlBarMoverListener() {
			public void controlBarChanged(float oldPosition, float newPosition) {
				
			}
			
			public void cursorPressed() {
			}
			
			public void cursorReleased() {
			}
		});
	}

	/**
	 * Builds the stop button.
	 */
	private void buildStopButton() {
		stopButton = (RoundImageLabel) this.contentItem.getContentSystem()
				.createContentItem(RoundImageLabel.class);
		stopButton.setRadius(30);
		stopButton.setImageInfo(ControlbarResources.class
				.getResource("stopbutton.png"));
		stopButton.setBorderSize(4);
		stopButton.addItemListener(new ItemEventAdapter() {
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				JMEMediaPlayer.this.setPlaying(false);
				for (PlayListener l : playListeners) {
					l.videoStopped();
				}
			}
		});
	}

	/**
	 * Builds the time label.
	 */
	private void buildTimeLabel() {
		timeLabel = (TextLabel) this.contentItem.getContentSystem()
				.createContentItem(TextLabel.class);
		timeLabel.setWidth(50);
		timeLabel.setHeight(30);
		timeLabel.setBackgroundColour(Color.black);
		timeLabel.setTextColour(Color.white);
		timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		timeLabel.setBorderSize(1);
	}

	/**
	 * Builds the video.
	 */
	private void buildVideo() {
		videoPlayer = (VideoPlayer) this.contentItem.getContentSystem()
				.createContentItem(VideoPlayer.class);
		
		videoPlayer.addItemListener(new ItemEventAdapter() {
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				Rectangle rightBottomCorner = new Rectangle(videoPlayer
						.getWidth() - 40, videoPlayer.getHeight() - 40,
						videoPlayer.getWidth(), videoPlayer.getHeight());
				if (rightBottomCorner.contains(x, videoPlayer.getHeight() - y)) {
					return;
				}
				if (isControlPanelOn) {
					hideControlPanel();
				} else {
					showControlPanel();
				}
			}
		});
		
	}

	/**
	 * Gets the left time string.
	 *
	 * @return the left time string
	 */
	private String getLeftTimeString() {
		int leftTime = (int) (jMEVideoImage.getDuration().getSeconds() - jMEVideoImage
				.getMediaTime().getSeconds());
		int leftMin = leftTime / 60;
		int leftSec = leftTime % 60;
		String leftMinString = "";
		String leftSecString = "";
		if (leftMin < 10) {
			leftMinString = "0" + leftMin;
		} else {
			leftMinString = String.valueOf(leftMin);
		}
		if (leftSec < 10) {
			leftSecString = "0" + leftSec;
		} else {
			leftSecString = String.valueOf(leftSec);
		}
		return leftMinString + ":" + leftSecString;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMediaPlayerImplementation#getPlayerFrame()
	 */
	public OrthoContentItem getPlayerFrame() {
		return videoPlayer;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.JMEContentItemImplementation
	 * #getSpatial()
	 */
	@Override
	public Spatial getSpatial() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMediaPlayerImplementation#getVideoPlayer()
	 */
	@Override
	public VideoPlayer getVideoPlayer() {
		return videoPlayer;
	}

	/**
	 * Hide control panel.
	 */
	public void hideControlPanel() {
		isControlPanelOn = false;
		progressBar.setVisible(false);
		// speedBar.setVisible(false);
		// speedLabel.setVisible(false);
		soundVolumeBar.setVisible(false);
		soundLabel.setVisible(false);
		soundOffLabel.setVisible(false);
		playButton.setVisible(false);
		stopButton.setVisible(false);
		timeLabel.setVisible(false);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEContentItem#
	 * init()
	 */
	public void init() {
		super.init();

		buildVideo();
		buildProgressBar();
		buildTimeLabel();
		// buildSpeedBar();
		buildSoundVolumeBar();
		buildPlayButton();
		buildStopButton();
		buildFrame();
		
		mediaPlayer.addSubItem(progressBar);
		// mediaPlayer.addSubItem(speedBar);
		// mediaPlayer.addSubItem(speedLabel);
		mediaPlayer.addSubItem(soundVolumeBar);
		mediaPlayer.addSubItem(soundLabel);
		mediaPlayer.addSubItem(soundOffLabel);
		mediaPlayer.addSubItem(playButton);
		mediaPlayer.addSubItem(stopButton);
		mediaPlayer.addSubItem(timeLabel);
		mediaPlayer.addSubItem(videoPlayer);
		((Spatial) mediaPlayer.getImplementationObject()).updateRenderState();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem
	 * #makeFlickable(float)
	 */
	@Override
	public void makeFlickable(float deceleration) {
		videoPlayer.makeFlickable(deceleration);
	}

	/**
	 * Place frame.
	 *
	 * @param mediaNodeWidth
	 *            the media node width
	 * @param mediaNodeHeight
	 *            the media node height
	 */
	private void placeFrame(float mediaNodeWidth, float mediaNodeHeight) {
		frame.updateGeometry(
				mediaNodeWidth + (mediaPlayer.getBorderSize() * 2),
				mediaNodeHeight + (mediaPlayer.getBorderSize() * 2));
		frame.updateModelBound();
		
	}
	
	/**
	 * Place progress bar.
	 *
	 * @param mediaNodeWidth
	 *            the media node width
	 * @param mediaNodeHeight
	 *            the media node height
	 */
	private void placeProgressBar(float mediaNodeWidth, float mediaNodeHeight) {
		timeLabel.setLocalLocation(mediaNodeWidth - 20
				- (timeLabel.getWidth() / 2) - (mediaNodeWidth / 2),
				(float) (((0.1 - 1) * mediaNodeHeight) / 2));
		progressBar.setControlBarLength(mediaNodeWidth - 38
				- (timeLabel.getWidth() * 2));
		progressBar.setLocalLocation(
				((progressBar.getControlBarLength() / 2) + 8)
						- (mediaNodeWidth / 2),
				(float) (((0.1 - 1) * mediaNodeHeight) / 2));

	}
	
	/**
	 * Place sound volume bar.
	 *
	 * @param mediaNodeWidth
	 *            the media node width
	 * @param mediaNodeHeight
	 *            the media node height
	 */
	private void placeSoundVolumeBar(float mediaNodeWidth, float mediaNodeHeight) {
		soundVolumeBar.setControlBarLength((float) 0.25 * mediaNodeWidth);
		soundVolumeBar
				.setLocalLocation(
						(float) (((soundVolumeBar.getControlBarLength() / 2) + (0.65 * mediaNodeWidth)) - (mediaNodeWidth / 2)),
						(float) (((0.8) * mediaNodeHeight) / 2));

		soundLabel.getImageInfo().setHeight((int) (mediaNodeWidth * 0.02));
		soundLabel
				.setLocalLocation(
						(float) ((soundLabel.getRadius() + (0.54 * mediaNodeWidth)) - (mediaNodeWidth / 2)),
						(float) (((0.8) * mediaNodeHeight) / 2));

		soundOffLabel.getImageInfo().setHeight((int) (mediaNodeWidth * 0.02));
		soundOffLabel
				.setLocalLocation(
						(float) ((soundLabel.getRadius() + (0.54 * mediaNodeWidth)) - (mediaNodeWidth / 2)),
						(float) (((0.8) * mediaNodeHeight) / 2));
	}
	
	/**
	 * Place speed bar.
	 *
	 * @param mediaNodeWidth
	 *            the media node width
	 * @param mediaNodeHeight
	 *            the media node height
	 */
	@SuppressWarnings("unused")
	private void placeSpeedBar(float mediaNodeWidth, float mediaNodeHeight) {
		speedBar.setControlBarLength((float) 0.35 * mediaNodeWidth);
		speedBar.setLocalLocation(
				(float) (((speedBar.getControlBarLength() / 2) + (0.11 * mediaNodeWidth)) - (mediaNodeWidth / 2)),
				(float) (((0.9) * mediaNodeHeight) / 2));
		
		speedLabel.getImageInfo().setHeight((int) (mediaNodeWidth * 0.02));
		speedLabel
				.setLocalLocation(
						(float) ((speedLabel.getRadius() + (0.04 * mediaNodeWidth)) - (mediaNodeWidth / 2)),
						(float) (((0.9) * mediaNodeHeight) / 2));
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IMediaPlayerImplementation#removePlayListener(synergynetframework.appsystem
	 * .contentsystem.jme.items.JMEMediaPlayer.PlayListener)
	 */
	@Override
	public void removePlayListener(PlayListener l) {
		playListeners.remove(l);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMediaPlayerImplementation#removePlayListeners()
	 */
	@Override
	public void removePlayListeners() {
		playListeners.clear();
	}
	
	/**
	 * Render.
	 */
	public void render() {
		jMEVideoImage = (JMFVideoImage) videoPlayer
				.getMediaImplementationObject();
		
		float mediaNodeWidth = 150;
		float mediaNodeHeight = 100;
		if (jMEVideoImage != null) {
			mediaNodeWidth = jMEVideoImage.getVideoWidth()
					/ videoPlayer.getPixelsPerUnit();
			mediaNodeHeight = jMEVideoImage.getVideoHeight()
					/ videoPlayer.getPixelsPerUnit();
		}

		placeProgressBar(mediaNodeWidth, mediaNodeHeight);
		// placeSpeedBar(mediaNodeWidth, mediaNodeHeight);
		placeSoundVolumeBar(mediaNodeWidth, mediaNodeHeight);
		placeFrame(mediaNodeWidth, mediaNodeHeight);

		timeLabel.setText(this.getLeftTimeString());

		soundVolumeBar.updateControlBar();

	}
	
	// UNUSED
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IQuadContentItemImplementation#setAutoFitSize(boolean)
	 */
	@Override
	public void setAutoFitSize(boolean isEnabled) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IQuadContentItemImplementation#setHeight(int)
	 */
	@Override
	public void setHeight(int height) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMediaPlayerImplementation#setMediaURL(java.net.URL)
	 */
	@Override
	public void setMediaURL(URL url) {
		videoPlayer.setVideoURL(url);
		render();
		
		if (jMEVideoImage != null) {
			this.soundVolumeBar.setCurrentPosition(jMEVideoImage
					.getSoundVolumeLevel());
		}

		videoPlayer.stop();

	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMediaPlayerImplementation#setPixelsPerUnit(float)
	 */
	@Override
	public void setPixelsPerUnit(float ppu) {
		videoPlayer.setPixelsPerUnit(ppu);
		render();
		videoPlayer.stop();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMediaPlayerImplementation#setPlaying(boolean)
	 */
	@Override
	public void setPlaying(boolean b) {
		if (b) {
			videoPlayer.play();
			playButton.setVisible(false);
			stopButton.setVisible(true);

			hideControlPanel();
			isStarted = true;
		} else {
			videoPlayer.stop();
			stopButton.setVisible(false);
			playButton.setVisible(true);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IQuadContentItemImplementation#setWidth(int)
	 */
	@Override
	public void setWidth(int width) {
		
	}
	
	/**
	 * Show control panel.
	 */
	public void showControlPanel() {

		isControlPanelOn = true;
		progressBar.setVisible(true);
		// speedBar.setVisible(true);
		// speedLabel.setVisible(true);
		soundVolumeBar.setVisible(true);
		if (!jMEVideoImage.isMute()) {
			soundLabel.setVisible(true);
			soundOffLabel.setVisible(false);
		} else {
			soundLabel.setVisible(false);
			soundOffLabel.setVisible(true);
		}
		timeLabel.setVisible(true);
		if (videoPlayer.isPlaying()) {
			stopButton.setVisible(true);
		} else {
			playButton.setVisible(true);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem
	 * #update(float)
	 */
	public void update(float interpolation) {
		
		super.update(interpolation);

		if ((jMEVideoImage == null) || !isControlPanelOn || !isStarted) {
			return;
		}

		if (controlPanelShowTime < 10) {
			controlPanelShowTime = controlPanelShowTime + interpolation;
		} else {
			this.hideControlPanel();
			controlPanelShowTime = 0;
		}
		
		if (isControlPanelOn) {
			progressBar.setCurrentPosition((float) (jMEVideoImage
					.getMediaTime().getSeconds() / jMEVideoImage.getDuration()
					.getSeconds()));
			timeLabel.setText(getLeftTimeString());
		}
	}
}
