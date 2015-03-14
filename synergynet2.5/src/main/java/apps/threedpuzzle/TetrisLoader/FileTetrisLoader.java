package apps.threedpuzzle.TetrisLoader;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public class FileTetrisLoader implements TetrisLoader {

	@Override
	public void loadTetris(List<Spatial> tetrisList, Node worldNode) {
		
		String fileName = "apps/threedpuzzle/TetrisSetting.txt";
		
		try {
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine = br.readLine();
			while (strLine!=null){
				String[] attrs = strLine.trim().split(",");
				
				Spatial tetris = worldNode.getChild(attrs[0]);
				tetris.setLocalTranslation(new Vector3f(Float.parseFloat(attrs[1]), Float.parseFloat(attrs[2]), 70));
				
				Quaternion tq = new Quaternion();
		    	tq.fromAngleAxis(Float.parseFloat(attrs[3]), new Vector3f(Float.parseFloat(attrs[4]), Float.parseFloat(attrs[5]), Float.parseFloat(attrs[6])));
		    	tetris.setLocalRotation(tq);		
		    	tetris.updateGeometricState(0f, false);	 
		    	
		    	strLine=br.readLine();
			}
			
			br.close();
			
				  	
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}
	
}
