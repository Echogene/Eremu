package eremu;

import eremu.curve.IntegralCurve;
import eremu.grid.PixelGrid;
import eremu.math.MathUtil;
import eremu.ui.ImageWindow;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Eremu {

	private static final int WIDTH = 1100;
	private static final int HEIGHT = 1100;

	private static final int MIN_X = -6;
	private static final int MIN_Y = -6;
	private static final int MAX_X =  6;
	private static final int MAX_Y =  6;

	private static final double SPARSITY = 2;
	private static final int X_RANGE = (int) ((double) WIDTH / SPARSITY);
	private static final int Y_RANGE = (int) ((double) HEIGHT / SPARSITY);

	private static AtomicInteger progress = new AtomicInteger();

	private static PixelGrid grid;

	public static void main(String[] args) {

		grid = new PixelGrid(
				MIN_X,
				MIN_Y,
				MAX_X,
				MAX_Y,
				WIDTH,
				HEIGHT
		);
		double distanceToMove = 0.005;

		ImageWindow imageWindow = new ImageWindow(grid, progress, X_RANGE * Y_RANGE);
		EventQueue.invokeLater(imageWindow::show);

		IntStream.range(0, X_RANGE * Y_RANGE)
				.parallel()
				.mapToObj(i -> {
					double initialX = MathUtil.progress(i % X_RANGE, X_RANGE, MIN_X, MAX_X);
					double initialY = MathUtil.progress((int) Math.floor((double) i / (double) Y_RANGE), Y_RANGE, MIN_Y, MAX_Y);
					return new IntegralCurve(
							initialX,
							initialY,
							(x, y) -> MathUtil.towards(
									2*cos(y)-sin(x),
									2*sin(y)+cos(x),
									x,
									y
							),
							distanceToMove
					);
				})
				.forEach(curve -> {
					IntStream.range(0, 500)
							.forEach(i -> {
								curve.iterate();
								grid.increment(curve.getX(), curve.getY());
							});
					progress.incrementAndGet();
				});
		imageWindow.finish();
	}

	/**
	 * x * cos(y) + y * cos(x)
	 *
	 * MathUtil.towards(
	 *         y * y * sin(4 * y),
	 *         x * x * sin(4 * x),
	 *         x,
	 *         y
	 * )
	 *
	 * 4*sin(sqrt(abs(x*y))) + 2*sqrt(abs(cos(x)*cos(y))) + 0.5*(cos(8*x)+cos(8*y))
	 *
	 * MathUtil.towards(2*y*sin(x), 2*x*cos(y), x, y)*4
	 *         + 2*(sin(x)+sin(y))
	 *         + 0.5*(cos(4*x)+cos(4*y))
	 *         + 0.2*(sin(8*x)+sin(8*y)),
	 *
	 * 6*sqrt(x*x + y*y)
	 *         + 6*sin(x)*cos(y)
	 *         + 3*cos(2*x)*sin(2*y)
	 *         + 2*cos(6*x)*cos(6*y)
	 *         + sin(16*x)*sin(16*y)
	 *
	 * MathUtil.towards(
	 *         x*cos(PI/20) - y*sin(PI/20),
	 *         y*cos(PI/20) + x*sin(PI/20),
	 *         x,
	 *         y
	 * ),
	 */
}
