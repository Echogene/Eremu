package eremu.grid;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractPixelGridPainter implements PixelGridPainter {
	protected final double minX;
	protected final double minY;
	protected final double maxX;
	protected final double maxY;
	/**
	 * The number of integral curves to start on a row.
	 */
	protected final int xRange;
	/**
	 * The number of integral curves to start on a column
	 */
	protected final int yRange;
	protected final AtomicInteger progress = new AtomicInteger();
	protected final PixelGrid grid;

	public AbstractPixelGridPainter(double sparsity, PixelGrid grid, double sparsity) {
		minX = grid.getBottomLeftX();
		xRange = (int) ((double) grid.getWidthResolution() / sparsity);
		this.grid = grid;
		minY = grid.getBottomLeftY();
		maxY = grid.getTopRightY();
		maxX = grid.getTopRightX();
		yRange = (int) ((double) grid.getHeightResolution() / sparsity);
	}

	@Override
	public AtomicInteger getProgress() {
		return progress;
	}

	@Override
	public int getCompleteProgress() {
		return xRange * yRange;
	}
}
