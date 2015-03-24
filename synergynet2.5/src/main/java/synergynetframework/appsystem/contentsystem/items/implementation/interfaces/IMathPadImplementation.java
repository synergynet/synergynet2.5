package synergynetframework.appsystem.contentsystem.items.implementation.interfaces;

import java.util.List;

import mit.ai.nl.core.Expression;
import synergynetframework.appsystem.contentsystem.items.MathPad.MathHandwritingListener;

/**
 * The Interface IMathPadImplementation.
 */
public interface IMathPadImplementation extends ISketchPadImplementation {

	/**
	 * Adds the math handwriting listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addMathHandwritingListener(MathHandwritingListener listener);

	/**
	 * Clear math expressions.
	 */
	public void clearMathExpressions();

	/**
	 * Gets the current expression.
	 *
	 * @return the current expression
	 */
	public Expression getCurrentExpression();

	/**
	 * Gets the math expressions.
	 *
	 * @return the math expressions
	 */
	public List<Expression> getMathExpressions();

	/**
	 * Removes the handwriting listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void removeHandwritingListener(MathHandwritingListener listener);

	/**
	 * Removes the handwriting listeners.
	 */
	public void removeHandwritingListeners();

	/**
	 * Sets the math engine enabled.
	 *
	 * @param isEnabled
	 *            the new math engine enabled
	 */
	public void setMathEngineEnabled(boolean isEnabled);

	/**
	 * Start new expression.
	 */
	public void startNewExpression();
}
