package eremu;

import eremu.grid.IntegralCurvePainter;
import eremu.grid.PixelGrid;
import eremu.ui.ImageWindow;

import java.awt.*;

public class Eremu {

	private static final int WIDTH = 1100;
	private static final int HEIGHT = 1100;

	private static final int MIN_X = -6;
	private static final int MIN_Y = -6;
	private static final int MAX_X =  6;
	private static final int MAX_Y =  6;

	private static final double SPARSITY = 2;

	public static void main(String[] args) {

		PixelGrid grid = new PixelGrid(
				MIN_X,
				MIN_Y,
				MAX_X,
				MAX_Y,
				WIDTH,
				HEIGHT
		);

		IntegralCurvePainter painter = new IntegralCurvePainter(grid, SPARSITY);

		ImageWindow imageWindow = new ImageWindow(grid, painter.getProgress(), painter.getCompleteProgress());
		EventQueue.invokeLater(imageWindow::show);

		painter.paint();

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
