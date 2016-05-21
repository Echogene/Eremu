package eremu.grid;

import eremu.math.MathUtil;

import java.util.stream.IntStream;

import static java.lang.Math.sin;

public class EscapeTimePainter extends AbstractPixelGridPainter {

	private final int xRange;
	private final int yRange;

	private final int maxIterations;
	private final double boundary;

	public EscapeTimePainter(PixelGrid grid) {
		super(grid);

		xRange = grid.getWidthResolution();
		yRange = grid.getHeightResolution();

		maxIterations = 4096;
		boundary = 40;
	}

	@Override
	public int getCompleteProgress() {
		return xRange * yRange;
	}

	@Override
	public void paint() {
		IntStream.range(0, getCompleteProgress())
				.parallel()
				.forEach(i -> {
					double initialX = MathUtil.progress(i % xRange, xRange, minX, maxX);
					double initialY = MathUtil.progress((int) Math.floor((double) i / (double) yRange), yRange, minY, maxY);
					double x = initialX;
					double y = initialY;
					int j = 0;
					while (x * x + y * y < boundary * boundary && j++ < maxIterations) {
						double xTemp = x * x - y * y + initialX;
						y = 2*sin(x * y) + initialY;
						x = xTemp;
					}
					grid.set(i % xRange, (int) Math.floor((double) i / (double) yRange), j);
					progress.incrementAndGet();
				});
	}
}
