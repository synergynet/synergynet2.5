package synergynetframework.jme.sysutils;

import org.lwjgl.opengl.GL11;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;

/**
 * The Class StereoRenderPass.
 */
public class StereoRenderPass extends RenderPass {
	
	/**
	 * The Enum CameraSide.
	 */
	protected enum CameraSide {
		
		/** The left. */
		LEFT,
		
		/** The right. */
		RIGHT
	}

	/**
	 * The type of projection to use to create a stereo rendering.
	 */
	public enum ProjectionMode {
		
		/**
		 * Assymetric frustum is a more advanced stereo projection stereoMode
		 * that takes focus into account. It is a more accurate representation
		 * of how two eyes would percieve the environment with a focus plane.
		 * Unfortunately it can also cause more strain to the eye since it must
		 * adjust to the effects of simulating focus.
		 */
		ASYMMETRIC_FRUSTUM,
		/**
		 * Simple offset moves the camera a bit to the left and a bit to the
		 * right to create the stereo effect. It's equivelant to putting two
		 * cameras parallel to each over side by side.
		 */
		SIMPLE_OFFSET
	}
	
	/**
	 * The Enum StereoMode.
	 */
	public enum StereoMode {
		
		/**
		 * Stereo information is trasferred using seperate color channels Left
		 * eye is sent with the red color channel, while the right eye is sent
		 * as teal (green & blue) color channel.
		 */
		ANAGLYPH,
		/**
		 * Stereo information is rendered seperately to each part of the screen.
		 * The left half of the display contains the left eye, while the right
		 * half contains the right eye.
		 */
		SIDE_BY_SIDE,
		/**
		 * Stereo information is transferred using OpenGL's dedicated stereo
		 * buffers. Requires special hardware that supports stereo stereoMode
		 * and driver, such as nVidia consumer stereo driver. In addition, the
		 * OpenGL context must be initialized in stereo stereoMode, set the
		 * "GameStereo" parameter on the GameSettings passed to JmeContext to
		 * "true".
		 */
		STEREO_BUFFER
	}
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2172286953053652452L;
	
	/** The eye distance. */
	protected float eyeDistance = 1f;
	
	/** The focus distance. */
	protected float focusDistance = 25f;
	
	/** The projection mode. */
	protected ProjectionMode projectionMode = ProjectionMode.ASYMMETRIC_FRUSTUM;
	
	/** intraocular distance. */
	
	protected Node rootNode;
	
	/** The saved node loc. */
	protected Vector3f savedCameraLocation = new Vector3f(),
			savedNodeLoc = new Vector3f();
	
	/** The stereo mode. */
	protected StereoMode stereoMode = StereoMode.ANAGLYPH;
	
	/** The temp2. */
	protected Vector3f temp = new Vector3f(), temp2 = new Vector3f();
	
	/**
	 * Instantiates a new stereo render pass.
	 *
	 * @param rootNode
	 *            the root node
	 */
	public StereoRenderPass(Node rootNode) {
		this.rootNode = rootNode;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.jme.renderer.pass.RenderPass#doRender(com.jme.renderer.Renderer)
	 */
	@Override
	public void doRender(Renderer renderer) {
		
		renderer.setPolygonOffset(zFactor, zOffset);
		
		Camera cam = renderer.getCamera();
		
		// save the original camera location
		savedCameraLocation.set(cam.getLocation());
		
		renderer.clearZBuffer();
		// LEFT EYE
		setFrustum(cam, CameraSide.LEFT);
		if (stereoMode == StereoMode.SIDE_BY_SIDE) {
			cam.setViewPort(0f, 0.5f, 0f, 1f);
		} else if (stereoMode == StereoMode.ANAGLYPH) {
			GL11.glColorMask(true, false, false, true);
		} else if (stereoMode == StereoMode.STEREO_BUFFER) {
			GL11.glDrawBuffer(GL11.GL_BACK_LEFT);
		}
		cam.update();
		renderer.draw(rootNode);
		renderer.clearZBuffer();
		
		// RIGHT EYE
		setFrustum(cam, CameraSide.RIGHT);
		if (stereoMode == StereoMode.SIDE_BY_SIDE) {
			cam.setViewPort(0.5f, 1.0f, 0f, 1f);
		} else if (stereoMode == StereoMode.ANAGLYPH) {
			GL11.glColorMask(false, true, true, true);
		} else if (stereoMode == StereoMode.STEREO_BUFFER) {
			GL11.glDrawBuffer(GL11.GL_BACK_RIGHT);
		}
		renderer.clearBuffers();
		cam.update();
		renderer.draw(rootNode);
		
		cam.getLocation().set(savedCameraLocation);
		if (stereoMode == StereoMode.SIDE_BY_SIDE) {
			cam.setViewPort(0f, 1f, 0f, 1f);
		} else if (stereoMode == StereoMode.ANAGLYPH) {
			GL11.glColorMask(true, true, true, true);
		} else if (stereoMode == StereoMode.STEREO_BUFFER) {
			GL11.glDrawBuffer(GL11.GL_BACK_LEFT);
		}
		
	}
	
	/**
	 * Gets the eye distance.
	 *
	 * @return the eye distance
	 */
	public float getEyeDistance() {
		return eyeDistance;
	}
	
	/**
	 * Gets the focus distance.
	 *
	 * @return the focus distance
	 */
	public float getFocusDistance() {
		return focusDistance;
	}
	
	/**
	 * Set the distance between the stereo cameras.
	 *
	 * @param eyeDistance
	 *            the new eye distance
	 */
	public void setEyeDistance(float eyeDistance) {
		this.eyeDistance = eyeDistance;
	}
	
	/**
	 * Set the distance to where the two eyes focus.
	 *
	 * @param focusDistance
	 *            the new focus distance
	 */
	public void setFocusDistance(float focusDistance) {
		this.focusDistance = focusDistance;
	}
	
	/**
	 * Sets up the camera frustum for a left or right eye.
	 *
	 * @param cam
	 *            the cam
	 * @param side
	 *            the side
	 */
	protected void setFrustum(Camera cam, CameraSide side) {
		
		float aspectratio = 0;
		float aperture;
		
		float near = 0;
		float ndfl = 0;
		
		float widthdiv2 = 0;
		
		cam.getDirection().cross(cam.getUp(), temp);
		
		if (projectionMode == ProjectionMode.ASYMMETRIC_FRUSTUM) {
			// Divide by 2 for side-by-side stereo
			aspectratio = DisplaySystem.getDisplaySystem().getRenderer()
					.getWidth()
					/ DisplaySystem.getDisplaySystem().getRenderer()
							.getHeight();
			aperture = 45.0f;
			
			near = cam.getFrustumNear();
			ndfl = near / focusDistance;
			
			// aperture in radians
			widthdiv2 = near
					* FastMath.tan((FastMath.DEG_TO_RAD * aperture) / 2.0f);
			
			temp.multLocal(eyeDistance / 2.0f);
		} else {
			temp.multLocal(eyeDistance * 4.0f);
		}
		
		if (side == CameraSide.RIGHT) {
			if (projectionMode == ProjectionMode.ASYMMETRIC_FRUSTUM) {
				float top = widthdiv2;
				float bottom = -widthdiv2;
				float left = (-aspectratio * widthdiv2)
						- (0.5f * eyeDistance * ndfl);
				float right = (aspectratio * widthdiv2)
						- (0.5f * eyeDistance * ndfl);
				
				cam.setFrustum(near, cam.getFrustumFar(), left, right, top,
						bottom);
			}
			
			cam.getLocation().addLocal(temp);
		} else {
			if (projectionMode == ProjectionMode.ASYMMETRIC_FRUSTUM) {
				float top = widthdiv2;
				float bottom = -widthdiv2;
				float left = (-aspectratio * widthdiv2)
						+ (0.5f * eyeDistance * ndfl);
				float right = (aspectratio * widthdiv2)
						+ (0.5f * eyeDistance * ndfl);
				
				cam.setFrustum(near, cam.getFrustumFar(), left, right, top,
						bottom);
			}
			
			cam.getLocation().subtractLocal(temp);
		}
	}
	
	/**
	 * Sets the method that the render pass uses to simulate stereo with two
	 * cameras.
	 *
	 * @param projectionMode
	 *            the new projection
	 */
	public void setProjection(ProjectionMode projectionMode) {
		this.projectionMode = projectionMode;
	}
	
	/**
	 * Sets how the render pass transffers stereo information to the
	 * framebuffer.
	 *
	 * @param stereoMode
	 *            the new stereo mode
	 */
	public void setStereoMode(StereoMode stereoMode) {
		this.stereoMode = stereoMode;
	}
}