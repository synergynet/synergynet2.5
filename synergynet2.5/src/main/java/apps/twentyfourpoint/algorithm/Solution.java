/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package apps.twentyfourpoint.algorithm;

import java.util.Enumeration;
import java.util.Vector;

import apps.twentyfourpoint.utils.IllegalExpressionException;


public class Solution {

	@SuppressWarnings("rawtypes")
	static Vector numbers;
	private boolean hasSolution;
	@SuppressWarnings("rawtypes")
	private Vector theSolution;
	private double theResult;
	static final int TOTAL_POSITION = 13;
	static final int NUM_POSITION[] = { 1, 4, 8, 11 };
	static int numCombin[][] = new int[24][4];
	static boolean hasNumCombin = false;
	static final int OP_POSITION[] = { 2, 6, 10 };
	static int opCombin[][] = new int[64][3];
	static boolean hasOpCombin = false;
	static Character whiteSpace = new Character(' ');
	static Character openParen = new Character('(');
	static Character closeParen = new Character(')');
	static Character addition = new Character('+');
	static Character subtract = new Character('-');
	static Character multiply = new Character('*');
	static Character division = new Character('/');
	static final int PAREN_POSITION[] = { 0, 3, 5, 7, 9, 12 };
	static final int PAREN_COMBIN[][] = { { 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 2, 0, 0, 0 }, { 1, 0, 0, 0, 2, 0 }, { 0, 1, 0, 0, 2, 0 },
			{ 0, 1, 0, 0, 0, 2 }, { 0, 0, 0, 1, 0, 2 }, { 1, 0, 2, 1, 0, 2 } };

	@SuppressWarnings({"rawtypes", "unchecked"})
	public Solution(int num0, int num1, int num2, int num3) {
		theSolution = null;
		numbers = new Vector(4);
		numbers.addElement(new Integer(num0));
		numbers.addElement(new Integer(num1));
		numbers.addElement(new Integer(num2));
		numbers.addElement(new Integer(num3));
		searchSolution();
	}

	@SuppressWarnings("rawtypes")
	public Vector getSolution() {
		return theSolution;
	}

	public boolean hasSolution() {
		return hasSolution;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private static void numberCombination() {
		Vector numbers = new Vector(4);
		int count = 0;
		if (hasNumCombin)
			return;
		for (int i = 0; i < 4; i++)
			numbers.addElement(new Integer(i));

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 2; k++) {
					Vector tmpNumbers = (Vector) numbers.clone();
					numCombin[count][0] = ((Integer) tmpNumbers.elementAt(i))
							.intValue();
					tmpNumbers.removeElementAt(i);
					numCombin[count][1] = ((Integer) tmpNumbers.elementAt(j))
							.intValue();
					tmpNumbers.removeElementAt(j);
					numCombin[count][2] = ((Integer) tmpNumbers.elementAt(k))
							.intValue();
					tmpNumbers.removeElementAt(k);
					numCombin[count][3] = ((Integer) tmpNumbers.elementAt(0))
							.intValue();
					tmpNumbers.removeElementAt(0);
					count++;
				}

			}

		}

		hasNumCombin = true;
	}

	private static void operatorCombination() {
		int count = 0;
		if (hasOpCombin)
			return;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 4; k++) {
					opCombin[count][0] = i;
					opCombin[count][1] = j;
					opCombin[count][2] = k;
					count++;
				}

			}

		}

		hasOpCombin = true;
	}



	@SuppressWarnings({"rawtypes", "unchecked"})
	private void searchSolution() {
		Vector tmpExpression = new Vector(13);
		int count = 0;
		numberCombination();
		operatorCombination();
		for (int i = 0; i < 13; i++)
			tmpExpression.addElement(whiteSpace);
		for (int num = 0; num < 24; num++) {
			for (int paren = 0; paren < 7; paren++) {
				for (int op = 0; op < 64; op++) {
					if (count++ % 500 == 0)
					for (int i = 0; i < 4; i++)
						tmpExpression.setElementAt(numbers
								.elementAt(numCombin[num][i]), NUM_POSITION[i]);

					for (int i = 0; i < 6; i++)
						switch (PAREN_COMBIN[paren][i]) {
						case 1: 
							tmpExpression.setElementAt(openParen,
									PAREN_POSITION[i]);
							break;

						case 2: 
							tmpExpression.setElementAt(closeParen,
									PAREN_POSITION[i]);
							break;

						case 0: 
							tmpExpression.setElementAt(whiteSpace,
									PAREN_POSITION[i]);
							break;
						}

					for (int i = 0; i < 3; i++)
						switch (opCombin[op][i]) {
						case 0: 
							tmpExpression
									.setElementAt(addition, OP_POSITION[i]);
							break;

						case 1: 
							tmpExpression
									.setElementAt(subtract, OP_POSITION[i]);
							break;

						case 2: 
							tmpExpression
									.setElementAt(multiply, OP_POSITION[i]);
							break;

						case 3: 
							tmpExpression
									.setElementAt(division, OP_POSITION[i]);
							break;
						}

					try {
						theResult = (new Expression(tmpExpression)).getValue();
					} catch (IllegalExpressionException e) {
						continue;
					}
					if (theResult == 24D) {
						hasSolution = true;
						theSolution = tmpExpression;
						return;
					}
				}

			}

		}

		hasSolution = false;
		theSolution = null;
	}
	@SuppressWarnings({"rawtypes"})
	public static void printSolution(Vector theSolution){
		for (Enumeration e = theSolution.elements(); e.hasMoreElements(); )
		{
			System.out.print(e.nextElement());
		}
	}
	
	@SuppressWarnings({"rawtypes"})
	public static String getSolutionString(Vector theSolution){
		StringBuilder sb=new StringBuilder();
		for (Enumeration e = theSolution.elements(); e.hasMoreElements(); )
		{
			sb.append(e.nextElement());
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
	Solution solution=new Solution(8,13,3,2);
	if(solution.hasSolution){
		printSolution(solution.getSolution());
	}
}
}

