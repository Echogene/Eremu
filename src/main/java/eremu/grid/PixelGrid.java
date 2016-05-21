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
		int xIndex = getXIndex(x);
		int yIndex = getYIndex(y);

		if (isInArea(xIndex, yIndex)) {
			pixelValues.get(xIndex).get(yIndex).incrementAndGet();
		}
	}

	public void set(double x, double y, int value) {
		int xIndex = getXIndex(x);
		int yIndex = getYIndex(y);

		if (isInArea(xIndex, yIndex)) {
			pixelValues.get(xIndex).get(yIndex).set(value);
		}
	}

	public void set(int pixelX, int pixelY, int value) {

		if (isInArea(pixelX, pixelY)) {
			pixelValues.get(pixelX).get(pixelY).set(value);
		}
	}

	private boolean isInArea(int xIndex, int yIndex) {
		return xIndex >= 0 && xIndex < widthResolution && yIndex >= 0 && yIndex < heightResolution;
	}

	private int getYIndex(double y) {
		return (int) Math.floor(heightResolution * (y - bottomLeftY) / (topRightY - bottomLeftY));
	}

	private int getXIndex(double x) {
		return (int) Math.floor(widthResolution * (x - bottomLeftX) / (topRightX - bottomLeftX));
	}

	public List<List<Integer>> getPixelValues() {
		return pixelValues.stream()
				.map(Collection::stream)
				.map(col -> col.map(AtomicInteger::get))
				.map(col -> col.collect(Collectors.toList()))
				.collect(Collectors.toList());
	}

	public double getBottomLeftX() {
		return bottomLeftX;
	}

	public double getBottomLeftY() {
		return bottomLeftY;
	}

	public double getTopRightX() {
		return topRightX;
	}

	public double getTopRightY() {
		return topRightY;
	}

	public int getWidthResolution() {
		return widthResolution;
	}

	public int getHeightResolution() {
		return heightResolution;
	}
}
