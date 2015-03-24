/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package apps.twentyfourpoint.algorithm;

import java.util.Enumeration;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import apps.twentyfourpoint.utils.IllegalExpressionException;

/**
 * The Class Expression.
 */
public class Expression {
	
	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		Expression exp = new Expression("(((45+66))*7)+5");
		try {
			System.out.println(exp.getValue());
		} catch (IllegalExpressionException e) {
			System.out.println("-1");
		}
	}

	/** The cparen. */
	private final int CPAREN = 3;

	/** The current value. */
	private double currentValue;

	/** The div. */
	private final int DIV = 5;

	/** The eol. */
	private final int EOL = 0;

	/** The input precedence. */
	private final int INPUT_PRECEDENCE[] = { 0, 0, 100, 0, 3, 3, 1, 1 };

	/** The input expression. */
	protected String inputExpression;

	/** The last token. */
	private int lastToken;

	/** The minus. */
	private final int MINUS = 7;

	/** The mult. */
	private final int MULT = 4;

	/** The oparen. */
	private final int OPAREN = 2;

	/** The operator stack. */
	private Stack<Integer> operatorStack;

	/** The plus. */
	private final int PLUS = 6;

	/** The post fix stack. */
	private Stack<Double> postFixStack;

	/** The stack precedence. */
	private final int STACK_PRECEDENCE[] = { -1, 0, 0, 99, 4, 4, 2, 2 };

	/** The result. */
	private double theResult;
	
	/** The value. */
	private final int VALUE = 1;
	
	/**
	 * Instantiates a new expression.
	 */
	public Expression() {
		inputExpression = null;
		operatorStack = new Stack<Integer>();
		postFixStack = new Stack<Double>();
		operatorStack.push(new Integer(0));
	}
	
	/**
	 * Instantiates a new expression.
	 *
	 * @param inString
	 *            the in string
	 */
	public Expression(String inString) {
		inputExpression = inString.trim();
		operatorStack = new Stack<Integer>();
		postFixStack = new Stack<Double>();
		operatorStack.push(new Integer(0));
	}
	
	/**
	 * Instantiates a new expression.
	 *
	 * @param inVector
	 *            the in vector
	 */
	@SuppressWarnings("rawtypes")
	public Expression(Vector inVector) {
		StringBuffer tmpString = new StringBuffer();
		for (Enumeration e = inVector.elements(); e.hasMoreElements(); tmpString
				.append(e.nextElement())) {
			;
		}
		inputExpression = tmpString.toString().trim();
		operatorStack = new Stack<Integer>();
		postFixStack = new Stack<Double>();
		operatorStack.push(new Integer(0));
	}
	
	/**
	 * Apply operation.
	 *
	 * @param topOperator
	 *            the top operator
	 * @throws IllegalExpressionException
	 *             the illegal expression exception
	 */
	private void applyOperation(int topOperator)
			throws IllegalExpressionException {
		if (topOperator == OPAREN) {
			throw new IllegalExpressionException("Unbalanced parenthesis");
		}
		double rightOperand = getPostStackTop();
		double leftOperand = getPostStackTop();
		if (topOperator == PLUS) {
			postFixStack.push(new Double(leftOperand + rightOperand));
		} else if (topOperator == MINUS) {
			postFixStack.push(new Double(leftOperand - rightOperand));
		} else if (topOperator == MULT) {
			postFixStack.push(new Double(leftOperand * rightOperand));
		} else if (topOperator == DIV) {
			if (rightOperand != 0) {
				postFixStack.push(new Double(leftOperand / rightOperand));
			} else {
				throw new IllegalExpressionException("Division by zero");
			}
		}
		operatorStack.pop();
	}
	
	/**
	 * Gets the expression.
	 *
	 * @return the expression
	 */
	public String getExpression() {
		return removeSpace(inputExpression);
	}
	
	/**
	 * Gets the post stack top.
	 *
	 * @return the post stack top
	 * @throws IllegalExpressionException
	 *             the illegal expression exception
	 */
	private double getPostStackTop() throws IllegalExpressionException {
		if (postFixStack.empty()) {
			throw new IllegalExpressionException("Missing operand");
		} else {
			return postFixStack.pop().doubleValue();
		}
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 * @throws IllegalExpressionException
	 *             the illegal expression exception
	 */
	public double getValue() throws IllegalExpressionException {
		StringTokenizer parser = new StringTokenizer(inputExpression,
				"+-*/() ", true);
		do {
			if (!parser.hasMoreTokens()) {
				lastToken = 0;
				processToken();
			} else {
				String token = parser.nextToken();
				char firstChar = token.charAt(0);
				if (!Character.isWhitespace(firstChar)) {
					if ((token.length() == 1) && isOperator(firstChar)) {
						switch (firstChar) {
							case '+':
								lastToken = PLUS;
								break;
							
							case '-':
								lastToken = MINUS;
								break;
							
							case '*':
								lastToken = MULT;
								break;
							
							case '/':
								lastToken = DIV;
								break;
							
							case '(':
								if (lastToken != VALUE) {
									lastToken = OPAREN;
								} else {
									throw new IllegalExpressionException(
											"Missing operator");
								}
								break;
							
							case ')':
								lastToken = CPAREN;
								break;
						}
					} else {
						try {
							currentValue = Double.valueOf(token).doubleValue();
						} catch (NumberFormatException e) {
							throw new IllegalExpressionException(
									"Unknown symbol");
						}
						if ((lastToken != 1) && (lastToken != 3)) {
							lastToken = 1;
						} else {
							throw new IllegalExpressionException(
									"missing operator");
						}
					}
					processToken();
				}
			}
		} while (lastToken != EOL);
		if (postFixStack.empty()) {
			throw new IllegalExpressionException("Missing operand");
		}
		theResult = postFixStack.pop().doubleValue();
		if (!postFixStack.empty()) {
			throw new IllegalExpressionException("Missing operator");
		} else {
			return theResult;
		}
	}
	
	/**
	 * Checks if is operator.
	 *
	 * @param c
	 *            the c
	 * @return true, if is operator
	 */
	private boolean isOperator(char c) {
		return (c == '+') || (c == '-') || (c == '*') || (c == '/')
				|| (c == '(') || (c == ')');
	}
	
	/**
	 * Process token.
	 *
	 * @throws IllegalExpressionException
	 *             the illegal expression exception
	 */
	private void processToken() throws IllegalExpressionException {
		switch (lastToken) {
			case VALUE:
				postFixStack.push(new Double(currentValue));
				return;
				
			case CPAREN:
				int topOperator;
				while (((topOperator = operatorStack.peek().intValue()) != OPAREN)
						&& (topOperator != EOL)) {
					applyOperation(topOperator);
				}
				if (topOperator == OPAREN) {
					operatorStack.pop();
				} else {
					throw new IllegalExpressionException(
							"Missing open parenthesis");
				}
				break;
			
			default:
				while (INPUT_PRECEDENCE[lastToken] <= STACK_PRECEDENCE[topOperator = operatorStack
						.peek().intValue()]) {
					applyOperation(topOperator);
				}
				if (lastToken != EOL) {
					operatorStack.push(new Integer(lastToken));
				}
				break;
		}
	}
	
	/**
	 * Removes the space.
	 *
	 * @param inputString
	 *            the input string
	 * @return the string
	 */
	private String removeSpace(String inputString) {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < inputString.length(); i++) {
			@SuppressWarnings("unused")
			char c;
			if ((c = inputString.charAt(i)) != ' ') {
				s.append(inputString.charAt(i));
			}
		}
		
		return s.toString();
	}

	/**
	 * Sets the expression.
	 *
	 * @param inString
	 *            the new expression
	 */
	public void setExpression(String inString) {
		inputExpression = inString.trim();
	}
}
