package apps.userdefinedgestures.taskreader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTaskReader implements TaskReader {

	@Override
	public List<String> getTaskList() {
		
		List<String> sequenceTaskList = getSequenceTaskList();
		List<String> taskList = new ArrayList<String>();
		
		Random random = new Random();
		int listSize = sequenceTaskList.size();
		for (int i=0; i<listSize;i++){
			int index = random.nextInt(sequenceTaskList.size());
			taskList.add(sequenceTaskList.get(index));
			sequenceTaskList.remove(index);
		}

		return taskList;
	}
	
	private List<String> getSequenceTaskList(){
		String[] objects={"cube", "tube", "ball", "sheet"};
		String[] viewports={"front", "top"};
		String[] transformer={"moveright", "moveup", "moveinside", "moverightinside", "moverightoutside", "rotatex", "rotatey", "rotatez", "scaleup", "scaledown"};
		
		List<String> sequenceTaskList = new ArrayList<String>();
		
		for (int i=0; i<objects.length;i++){
			for (int j=0; j<viewports.length; j++){
				for (int z=0; z<transformer.length; z++){
					sequenceTaskList.add(objects[i]+","+viewports[j]+","+transformer[z]);
				}
			}
			
		}
		
		return sequenceTaskList;
		
	}
	
	public static void main(String[] args) {
		RandomTaskReader randomTaskReader = new RandomTaskReader();
		List<String> list = randomTaskReader.getTaskList();
		for (String l: list){
			System.out.println(l);
		}
	}

}
