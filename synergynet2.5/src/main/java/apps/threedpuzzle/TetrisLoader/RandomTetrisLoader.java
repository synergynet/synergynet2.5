package apps.threedpuzzle.TetrisLoader;

import java.util.List;
import java.util.Random;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;


/**
 * The Class RandomTetrisLoader.
 */
public class RandomTetrisLoader implements TetrisLoader {

	/* (non-Javadoc)
	 * @see apps.threedpuzzle.TetrisLoader.TetrisLoader#loadTetris(java.util.List, com.jme.scene.Node)
	 */
	@Override
	public void loadTetris(List<Spatial> tetrisList, Node worldNode) {
		Random rand = new Random();
		int randomX=0;
		int randomY=0;
		
		for (Spatial tetris:tetrisList){
			randomX=0;
	    	randomY=0;

	    	while ((randomX<60 && randomX>-45)||(randomY<60 && randomY>-45)){
	    		randomX = rand.nextInt(190)-100;
	    		randomY = rand.nextInt(150) -80;
	    	}
        
	    	tetris.setLocalTranslation(new Vector3f(randomX, randomY, 70));
        
	    	randomX = rand.nextInt(8);
	    	randomY = rand.nextInt(10);
    	
	    	Quaternion tq = new Quaternion();
	    	tq.fromAngleAxis(2*FastMath.PI/2f*randomX/8, new Vector3f(randomX/10, randomY/10, randomX/4));
	    	tetris.setLocalRotation(tq);		
	    	tetris.updateGeometricState(0f, false);	 
		}
		
	}

	
}
