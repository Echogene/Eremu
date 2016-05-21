package eremu.grid;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PixelGrid {

	private final double bottomLeftX;
	private final double bottomLeftY;
	private final double topRightX;
	private final double topRightY;
	private final int widthResolution;
	private final int heightResolution;
	private final List<List<AtomicInteger>> pixelValues;

	public PixelGrid(double bottomLeftX, double bottomLeftY, double topRightX, double topRightY, int widthResolution, int heightResolution) {
		this.bottomLeftX = bottomLeftX;
		this.bottomLeftY = bottomLeftY;
		this.topRightX = topRightX;
		this.topRightY = topRightY;
		this.widthResolution = widthResolution;
		this.heightResolution = heightResolution;
		pixelValues = IntStream.range(0, widthResolution)
				.mapToObj(i -> IntStream.range(0, heightResolution))
				.map(col -> col.mapToObj(i -> new AtomicInteger()))
				.map(col -> col.collect(Collectors.toList()))
				.collect(Collectors.toList());
	}

	public void increment(double x, double y) {
		int xIndex = (int) Math.floor(widthResolution * (x - bottomLeftX) / (topRightX - bottomLeftX));
		int yIndex = (int) Math.floor(heightResolution * (y - bottomLeftY) / (topRightY - bottomLeftY));

		if (xIndex >= 0 && xIndex < widthResolution && yIndex >= 0 && yIndex < heightResolution) {
			pixelValues.get(xIndex).get(yIndex).incrementAndGet();
		}
	}

	public List<List<Integer>> getPixelValues() {
		return pixelValues.stream()
				.map(Collection::stream)
				.map(col -> col.map(AtomicInteger::get))
				.map(col -> col.collect(Collectors.toList()))
				.collect(Collectors.toList());
	}

	public int getWidthResolution() {
		return widthResolution;
	}

	public int getHeightResolution() {
		return heightResolution;
	}
}
