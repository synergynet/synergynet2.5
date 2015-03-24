package apps.threedpuzzle.TetrisLoader;

import java.util.List;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 * The Interface TetrisLoader.
 */
public interface TetrisLoader {
	
	/**
	 * Load tetris.
	 *
	 * @param tetrisList
	 *            the tetris list
	 * @param worldNode
	 *            the world node
	 */
	public void loadTetris(List<Spatial> tetrisList, Node worldNode);
}
