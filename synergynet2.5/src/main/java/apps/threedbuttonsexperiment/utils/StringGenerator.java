package apps.threedbuttonsexperiment.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StringGenerator {
	
	public static String generateNumber(int length){
		
		Random random = new Random();
		
		List<Integer> numberSet = new ArrayList<Integer>();
		for (int i=0; i<=9; i++){
			numberSet.add(i);
		}
		
		String generatedString="";
		
		for (int i=0; i<length; i++){
			int index = random.nextInt(numberSet.size());
			generatedString+=numberSet.get(index).toString();
			numberSet.remove(index);
		}
		
		return generatedString;
	}
	
	public static void main(String[] args) {
		
		for (int i=0; i<100; i++)
			System.out.println("number is: "+StringGenerator.generateNumber(6));
	}
}
