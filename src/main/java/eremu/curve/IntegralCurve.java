package eremu.curve;

import java.util.function.ToDoubleBiFunction;

public class IntegralCurve {

	private double x;
	private double y;
	private final ToDoubleBiFunction<Double, Double> headingFunction;
	private final double distanceToMove;

	public IntegralCurve(
			double initialX,
			double initialY,
			ToDoubleBiFunction<Double, Double> headingFunction,
			double distanceToMove
	) {
		x = initialX;
		y = initialY;
		this.headingFunction = headingFunction;
		this.distanceToMove = distanceToMove;
	}

	public void iterate() {
		double θ = headingFunction.applyAsDouble(x, y);
		double δx = distanceToMove * Math.cos(θ);
		x += δx;

		double δy = distanceToMove * Math.sin(θ);
		y += δy;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
