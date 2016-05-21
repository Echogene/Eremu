package eremu.ui;

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

import static java.awt.BorderLayout.LINE_END;
import static java.awt.BorderLayout.LINE_START;
import static java.awt.BorderLayout.PAGE_END;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.sqrt;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ImageWindow {

	private final int width;
	private final int height;

	private final BufferedImage image;

	private final TimerTask task;

	private final AtomicInteger progress;

	private final JFrame frame;
	private final JPanel panel;
	private final JProgressBar progressBar;
	private final JFileChooser fileChooser;
	private final JButton saveButton;
	private final JLabel label;

	public ImageWindow(final PixelGrid grid, AtomicInteger progress, int progressComplete) {

		width = grid.getWidthResolution();
		height = grid.getHeightResolution();

		this.progress = progress;

		image = new BufferedImage(width, height, TYPE_INT_RGB);

		frame = new JFrame("Image");
		setUpFrame();

		panel = new JPanel();
		setUpPanel();

		progressBar = new JProgressBar(0, progressComplete);
		setUpProgressBar();

		fileChooser = new JFileChooser();
		setUpFileChooser();

		saveButton = new JButton("Save Image");
		setUpSaveButton();

		ImageIcon icon = new ImageIcon(image);
		label = new JLabel(icon);
		setUpLabel();

		task = new TimerTask() {
			@Override
			public void run() {
				refreshImage(grid);
				refreshProgress(progressBar);
				label.repaint();
			}
		};
	}

	private void setUpFrame() {
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void setUpPanel() {
		panel.setPreferredSize(new Dimension(width, height + 40));
		frame.add(panel);
	}

	private void setUpProgressBar() {
		progressBar.setPreferredSize(new Dimension(width - 200, 30));
		progressBar.setStringPainted(true);
		panel.add(progressBar, LINE_START);
	}

	private void setUpFileChooser() {
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
	}

	private void setUpSaveButton() {
		saveButton.setPreferredSize(new Dimension(180, 30));
		saveButton.addActionListener(e -> {
			int returnVal = fileChooser.showSaveDialog(panel);
			if (returnVal == APPROVE_OPTION) {
				File file = new File(fileChooser.getSelectedFile().toString() + ".png");
				try {
					ImageIO.write(image, "png", file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel.add(saveButton, LINE_END);
	}

	private void setUpLabel() {
		panel.add(label, PAGE_END);
	}

	private void refreshImage(PixelGrid grid) {
		List<List<Integer>> pixelValues = grid.getPixelValues();
		IntStream.range(0, width)
				.forEach(x -> IntStream.range(0, height)
						.forEach(y -> image.setRGB(x, y, getColorValue(pixelValues.get(x).get(y))))
				);
	}

	private int getColorValue(int value) {
		value = (int) (sqrt(40 * value));
		return new Color(
				MathUtil.limitColour(value),
				MathUtil.limitColour(value - 256),
				MathUtil.limitColour(value - 512)
		).getRGB();
	}

	private void refreshProgress(JProgressBar progressBar) {
		progressBar.setValue(progress.get());
	}

	public void show() {
		frame.setLocationByPlatform(true);
		frame.pack();
		frame.setVisible(true);

		new Timer().scheduleAtFixedRate(task, 0, 200);
	}

	public void finish() {
		// Everything is done now, so refresh one last time and cancel.
		task.run();
		task.cancel();
	}
}
