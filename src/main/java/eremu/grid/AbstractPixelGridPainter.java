package eremu.grid;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractPixelGridPainter implements PixelGridPainter {

	protected final double minX;
	protected final double minY;
	protected final double maxX;
	protected final double maxY;

	protected final AtomicInteger progress = new AtomicInteger();
	protected final PixelGrid grid;

	protected AbstractPixelGridPainter(PixelGrid grid) {
		this.grid = grid;

		minX = grid.getBottomLeftX();
		minY = grid.getBottomLeftY();
		maxY = grid.getTopRightY();
		maxX = grid.getTopRightX();
	}

	@Override
	public AtomicInteger getProgress() {
		return progress;
	}
}
