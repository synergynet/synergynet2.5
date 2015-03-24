package apps.threedinteraction.tv;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.llama.jmf.JMFVideoImage;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.VideoPlayer;
import apps.threedinteraction.button.KeyListener;
import apps.threedinteraction.button.RoundButton;
import apps.threedmanipulation.ThreeDManipulation;

import com.jme.image.Texture;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.WrapMode;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.shape.RoundedBox;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * The Class TV.
 */
public class TV extends Node {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6173587080007220150L;

	/** The content system. */
	protected ContentSystem contentSystem;

	/** The frame width. */
	protected float frameWidth;

	/** The height. */
	protected float height = 120;

	/** The j me video image. */
	protected JMFVideoImage jMEVideoImage;

	/** The listeners. */
	protected List<KeyListener> listeners = new ArrayList<KeyListener>();

	/** The tv frame texture. */
	protected URL tvFrameTexture;

	/** The video player. */
	protected VideoPlayer videoPlayer;

	/** The video quad. */
	protected Quad videoQuad;

	/** The width. */
	protected float width = 160;
	
	/**
	 * Instantiates a new tv.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param videoURL
	 *            the video url
	 * @param frameWidth
	 *            the frame width
	 * @param tvFrameTexture
	 *            the tv frame texture
	 */
	public TV(ContentSystem contentSystem, URL videoURL, float frameWidth,
			URL tvFrameTexture) {
		this.frameWidth = frameWidth;
		this.tvFrameTexture = tvFrameTexture;
		this.contentSystem = contentSystem;

		buildVideo(contentSystem, videoURL);

		this.setLocalScale(0.015f);

		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(FastMath.PI / 2f, new Vector3f(1, 0, 0));
		this.setLocalRotation(tq);
		this.updateGeometricState(0f, false);
	}

	/**
	 * Adds the key listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addKeyListener(KeyListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Builds the close button.
	 */
	public void buildCloseButton() {
		RoundButton close = new RoundButton("Close TV Button", 15f, 8f,
				ThreeDManipulation.class.getResource("taskbar.png"),
				ThreeDManipulation.class.getResource("onbutton.jpg"));
		close.setLocalTranslation(new Vector3f(0f, 10, height - 10));
		
		this.attachChild(close);
		close.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(String key) {
				for (KeyListener l : listeners) {
					l.keyPressed("tvclose");
				}

			}
		});
	}

	/**
	 * Builds the frame.
	 */
	public void buildFrame() {
		width = videoPlayer.getVideoWidth() / 1.5f;
		height = videoPlayer.getVideoHeight() / 1.5f;

		// width = 160;
		// height = 120;

		Vector3f min = new Vector3f(-(width + (frameWidth * 2)) / 2,
				-(frameWidth * 2) / 2,
				(-(height + (frameWidth * 2) + 2) / 2) - 15);
		Vector3f max = new Vector3f((width + (frameWidth * 2)) / 2,
				(frameWidth * 2) / 2,
				((height + (frameWidth * 2) + 2) / 2) + 15);
		Vector3f slopeV = new Vector3f(0.1f, 0.1f, 0.1f);
		
		RoundedBox rb = new RoundedBox("RoundedBox" + name, min, max, slopeV);

		rb.setLocalTranslation(0, 0, 0);

		this.attachChild(rb);

		TextureState ts;
		Texture texture;
		ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);
		texture = TextureManager.loadTexture(tvFrameTexture,
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);
		texture.setApply(ApplyMode.Replace);
		texture.setScale(new Vector3f(1, 1, 1));
		ts.setTexture(texture);
		ts.apply();
		rb.setRenderState(ts);
		rb.updateRenderState();
		
		// buildCloseButton();
		
	}

	/**
	 * Builds the video.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param videoURL
	 *            the video url
	 */
	public void buildVideo(ContentSystem contentSystem, URL videoURL) {

		videoPlayer = (VideoPlayer) contentSystem
				.createContentItem(VideoPlayer.class);
		videoPlayer.setVideoURL(videoURL);
		videoPlayer.setLocation(-100, -100);

		videoQuad = (Quad) (videoPlayer.getImplementationObject());
		videoQuad.setLocalTranslation(0, 5, -20);
		videoPlayer.setPlaying(false);
		Quaternion tq = new Quaternion();
		tq.fromAngleAxis((3 * FastMath.PI) / 2f, new Vector3f(1, 0, 0));
		videoQuad.setLocalRotation(tq);
		videoQuad.updateGeometricState(0f, false);
		this.attachChild(videoQuad);
		
		buildFrame();
		
	}

	/**
	 * Tv show.
	 *
	 * @param b
	 *            the b
	 */
	public void tvShow(boolean b) {
		videoPlayer.setPlaying(b);
	}

	/**
	 * Update.
	 *
	 * @param tpf
	 *            the tpf
	 */
	public void update(float tpf) {
		if (contentSystem != null) {
			contentSystem.update(tpf);
		}
	}

}
