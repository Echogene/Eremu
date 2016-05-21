package eremu.grid;

import eremu.curve.IntegralCurve;
import eremu.math.MathUtil;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Paint a pixel grid using integral curves.
 */
public class IntegralCurvePainter {

	private final double minX;
	private final double minY;
	private final double maxX;
	private final double maxY;

	/**
	 * The number of integral curves to start on a row.
	 */
	private final int xRange;

	/**
	 * The number of integral curves to start on a column
	 */
	private final int yRange;

	private final AtomicInteger progress = new AtomicInteger();

	private final PixelGrid grid;

	public IntegralCurvePainter(PixelGrid grid, double sparsity) {
		this.grid = grid;

		minX = grid.getBottomLeftX();
		minY = grid.getBottomLeftY();
		maxX = grid.getTopRightX();
		maxY = grid.getTopRightY();

		xRange = (int) ((double) grid.getWidthResolution() / sparsity);
		yRange = (int) ((double) grid.getHeightResolution() / sparsity);
	}

	public AtomicInteger getProgress() {
		return progress;
	}

	public int getCompleteProgress() {
		return xRange * yRange;
	}

	public void paint() {
		double distanceToMove = 0.005;

		IntStream.range(0, getCompleteProgress())
				.parallel()
				.mapToObj(i -> {
					double initialX = MathUtil.progress(i % xRange, xRange, minX, maxX);
					double initialY = MathUtil.progress((int) Math.floor((double) i / (double) yRange), yRange, minY, maxY);
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
	}
}
