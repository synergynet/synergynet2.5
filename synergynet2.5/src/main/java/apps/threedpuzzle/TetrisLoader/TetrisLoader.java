package apps.threedpuzzle.TetrisLoader;

import java.util.List;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

public interface TetrisLoader {

	public void loadTetris(List<Spatial> tetrisList, Node worldNode);
}
