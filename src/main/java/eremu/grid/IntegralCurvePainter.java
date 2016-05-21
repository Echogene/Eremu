package eremu.grid;

import eremu.curve.IntegralCurve;
import eremu.math.MathUtil;

import java.util.stream.IntStream;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Paint a pixel grid using integral curves.
 */
public class IntegralCurvePainter extends AbstractPixelGridPainter {

	public IntegralCurvePainter(PixelGrid grid, double sparsity) {
		super(sparsity, grid, sparsity);

	}

	@Override
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
