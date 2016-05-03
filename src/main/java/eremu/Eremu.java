package eremu;

import eremu.curve.IntegralCurve;
import eremu.grid.PixelGrid;
import eremu.math.MathUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.lang.Math.*;

public class Eremu {

	private static final int WIDTH = 1100;
	private static final int HEIGHT = 1100;

	private static final BufferedImage IMAGE = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

	private static final int MIN_X = -6;
	private static final int MIN_Y = -6;
	private static final int MAX_X =  6;
	private static final int MAX_Y =  6;

	private static final double SPARSITY = 2;
	private static final int X_RANGE = (int) ((double) WIDTH / SPARSITY);
	private static final int Y_RANGE = (int) ((double) HEIGHT / SPARSITY);

	private static AtomicInteger progress = new AtomicInteger();

	private static PixelGrid grid;
	private static TimerTask task;

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Image");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT + 40));

		JProgressBar progressBar = new JProgressBar(0, X_RANGE * Y_RANGE);
		progressBar.setPreferredSize(new Dimension(WIDTH - 200, 30));
		progressBar.setStringPainted(true);
		panel.add(progressBar, BorderLayout.LINE_START);

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(MessageFormat.format(
				"{0}{1}Pictures",
				System.getProperty("user.home"),
				System.getProperty("file.separator")
		)));
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".png");
			}

			@Override
			public String getDescription() {
				return "PNG images (*.png)";
			}
		});

		JButton saveButton = new JButton("Save Image");
		saveButton.setPreferredSize(new Dimension(180, 30));
		saveButton.addActionListener(e -> {
			int returnVal = fileChooser.showSaveDialog(panel);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = new File(fileChooser.getSelectedFile().toString() + ".png");
				try {
					ImageIO.write(IMAGE, "png", file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel.add(saveButton, BorderLayout.LINE_END);

		ImageIcon icon = new ImageIcon(IMAGE);
		JLabel label = new JLabel(icon);
		panel.add(label, BorderLayout.PAGE_END);

		frame.add(panel);
		frame.setLocationByPlatform(true);
		frame.pack();
		frame.setVisible(true);

		task = new TimerTask() {
			@Override
			public void run() {
				refreshImage(grid);
				refreshProgress(progressBar);
				label.repaint();
			}
		};
		new Timer().scheduleAtFixedRate(task, 0, 200);
	}

	private static void refreshProgress(JProgressBar progressBar) {
		progressBar.setValue(progress.get());
	}

	private static int getColorValue(int value) {
		value = (int) (sqrt(40*value));
		return new Color(
				MathUtil.limitColour(value),
				MathUtil.limitColour(value - 256),
				MathUtil.limitColour(value - 512)
		).getRGB();
	}

	public static void main(String[] args) {

		grid = new PixelGrid(
				MIN_X,
				MIN_Y,
				MAX_X,
				MAX_Y,
				WIDTH,
				HEIGHT
		);
		double distanceToMove = 0.005;

		EventQueue.invokeLater(Eremu::createAndShowGUI);

		IntStream.range(0, X_RANGE * Y_RANGE)
				.parallel()
				.mapToObj(i -> {
					double initialX = MathUtil.progress(i % X_RANGE, X_RANGE, MIN_X, MAX_X);
					double initialY = MathUtil.progress((int) Math.floor((double) i / (double) Y_RANGE), Y_RANGE, MIN_Y, MAX_Y);
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
		if (task != null) {
			// Everything is done now.
			task.run();
			task.cancel();
		}
	}

	private static void refreshImage(PixelGrid grid) {
		List<List<Integer>> pixelValues = grid.getPixelValues();
		IntStream.range(0, WIDTH)
				.forEach(x -> IntStream.range(0, HEIGHT)
						.forEach(y -> IMAGE.setRGB(x, y, getColorValue(pixelValues.get(x).get(y))))
				);
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
