package eremu.math;

public class MathUtil {

	public static double towardsOrigin(double x, double y) {
		return Math.PI + Math.atan2(y, x);
	}

	public static double towards(double towardsX, double towardsY, double x, double y) {
		return Math.atan2(towardsY - y, towardsX - x);
	}

	public static int limitColour(int colorValue) {
		return Math.max(0, Math.min(255, colorValue));
	}

	public static double progress(int progress, int completion, double min, double max) {
		return min + (double) progress * (max - min) / (double) completion;
	}
}
