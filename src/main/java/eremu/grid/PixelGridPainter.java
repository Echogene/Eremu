package eremu.grid;

import java.util.concurrent.atomic.AtomicInteger;

public interface PixelGridPainter {

	AtomicInteger getProgress();

	int getCompleteProgress();

	void paint();
}
