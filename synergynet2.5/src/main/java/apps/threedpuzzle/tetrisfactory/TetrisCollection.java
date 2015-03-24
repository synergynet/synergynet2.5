package apps.threedpuzzle.tetrisfactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import apps.threedmanipulation.ThreeDManipulation;
import apps.threedpuzzle.TetrisLoader.TetrisLoader;
import apps.threedpuzzle.gestures.TwoFingersGesture;

import com.jme.bounding.BoundingBox;
import com.jme.math.Quaternion;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.converters.FormatConverter;
import com.jmex.model.converters.ObjToJme;

/**
 * The Class TetrisCollection.
 */
public class TetrisCollection {
	
	/**
	 * The listener interface for receiving objectRotateTranslateScale events.
	 * The class that is interested in processing a objectRotateTranslateScale
	 * event implements this interface, and the object created with that class
	 * is registered with a component using the component's
	 * <code>addObjectRotateTranslateScaleListener<code> method. When
	 * the objectRotateTranslateScale event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ObjectRotateTranslateScaleEvent
	 */
	public interface ObjectRotateTranslateScaleListener {

		/**
		 * Item moved.
		 *
		 * @param targetSpatial
		 *            the target spatial
		 * @param newLocationX
		 *            the new location x
		 * @param newLocationY
		 *            the new location y
		 * @param newLocationZ
		 *            the new location z
		 */
		public void itemMoved(Spatial targetSpatial, float newLocationX,
				float newLocationY, float newLocationZ);

		/**
		 * Item rotated.
		 *
		 * @param targetSpatial
		 *            the target spatial
		 * @param quaternion
		 *            the quaternion
		 */
		public void itemRotated(Spatial targetSpatial, Quaternion quaternion);
	}

	/** The listeners. */
	protected List<ObjectRotateTranslateScaleListener> listeners = new ArrayList<ObjectRotateTranslateScaleListener>();

	/** The tetris collection. */
	protected List<Spatial> tetrisCollection = new ArrayList<Spatial>();

	/** The tetris loader. */
	protected TetrisLoader tetrisLoader;

	/** The tetris types. */
	protected String[] tetrisTypes = { "LTetris", "TTetris", "ZTetris",
			"ITetris" };

	/** The world node. */
	protected Node worldNode;

	/**
	 * Instantiates a new tetris collection.
	 *
	 * @param worldNode
	 *            the world node
	 * @param tetrisLoader
	 *            the tetris loader
	 */
	public TetrisCollection(Node worldNode, TetrisLoader tetrisLoader) {
		this.worldNode = worldNode;
		this.tetrisLoader = tetrisLoader;
		this.buildTetris(5);

	}

	/**
	 * Adds the rotate translate scale listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addRotateTranslateScaleListener(
			ObjectRotateTranslateScaleListener l) {
		listeners.add(l);
	}
	
	/**
	 * Builds the tetris.
	 *
	 * @param number
	 *            the number
	 */
	private void buildTetris(int number) {

		Spatial tetris = null;
		URL model;
		
		for (int i = 0; i < number; i++) {
			for (String tetrisType : tetrisTypes) {
				model = ThreeDManipulation.class.getClassLoader().getResource(
						"" + tetrisType + ".obj");

				FormatConverter converter = new ObjToJme();
				String folder = ThreeDManipulation.class.getClassLoader()
						.getResource("ThreeDManipulationResources.class")
						.toString()
						.replace("ThreeDManipulationResources.class", "");
				converter.setProperty("mtllib", folder);
				ByteArrayOutputStream BO = new ByteArrayOutputStream();
				
				try {
					converter.convert(model.openStream(), BO);
					tetris = (Spatial) BinaryImporter.getInstance().load(
							new ByteArrayInputStream(BO.toByteArray()));
					tetris.setName(tetrisType + i);
					tetris.setLocalScale(10f);
					
					tetris.setModelBound(new BoundingBox());
					tetris.updateModelBound();
					worldNode.attachChild(tetris);
					
				} catch (Exception e) {
					System.out.println("Damn exceptions! O_o \n" + e);
					e.printStackTrace();
					System.exit(0);
				}
				
				// ControlPointRotateTranslateScale cprts1 = new
				// ControlPointRotateTranslateScale(tp1);
				TwoFingersGesture cprts = new TwoFingersGesture(tetris);
				cprts.setPickMeOnly(true);
				cprts.addRotateTranslateScaleListener(new TwoFingersGesture.ObjectRotateTranslateScaleListener() {
					
					@Override
					public void itemMoved(Spatial targetSpatial,
							float newLocationX, float newLocationY,
							float newLocationZ) {
						for (ObjectRotateTranslateScaleListener l : listeners) {
							l.itemMoved(targetSpatial, newLocationX,
									newLocationY, newLocationZ);
						}

					}
					
					@Override
					public void itemRotated(Spatial targetSpatial,
							Quaternion quaternion) {

						for (ObjectRotateTranslateScaleListener l : listeners) {
							l.itemRotated(targetSpatial, quaternion);
						}

					}
					
				});
				
				this.tetrisCollection.add(tetris);
			}
			
		}
		this.tetrisLoader.loadTetris(tetrisCollection, worldNode);
		
	}

	/**
	 * Removes the rotate translate scale listener.
	 *
	 * @param l
	 *            the l
	 */
	public void removeRotateTranslateScaleListener(
			ObjectRotateTranslateScaleListener l) {
		if (listeners.contains(l)) {
			listeners.remove(l);
		}
	}

}
