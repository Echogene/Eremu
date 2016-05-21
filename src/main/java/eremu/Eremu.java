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
}
