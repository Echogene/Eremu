package eremu;

import eremu.grid.PixelGrid;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;

public class PixelGridTest {

	@Test
	public void should_create_a_one_by_one_grid() throws Exception {
		PixelGrid grid = new PixelGrid(
				0,
				0,
				1,
				1,
				1,
				1
		);
		assertThat(grid.getPixelValues(), hasSize(1));
		assertThat(grid.getPixelValues().get(0), hasSize(1));
		assertThat(grid.getPixelValues().get(0).get(0), is(0));
	}

	@Test
	public void should_create_a_two_by_one_grid() throws Exception {
		PixelGrid grid = new PixelGrid(
				0,
				0,
				1,
				1,
				2,
				1
		);
		assertThat(grid.getPixelValues(), hasSize(2));
		assertThat(grid.getPixelValues().get(0), hasSize(1));
		assertThat(grid.getPixelValues().get(0).get(0), is(0));
		assertThat(grid.getPixelValues().get(1), hasSize(1));
		assertThat(grid.getPixelValues().get(1).get(0), is(0));
	}

	@Test
	public void should_create_a_one_by_two_grid() throws Exception {
		PixelGrid grid = new PixelGrid(
				0,
				0,
				1,
				1,
				1,
				2
		);
		assertThat(grid.getPixelValues(), hasSize(1));
		assertThat(grid.getPixelValues().get(0), hasSize(2));
		assertThat(grid.getPixelValues().get(0).get(0), is(0));
		assertThat(grid.getPixelValues().get(0).get(1), is(0));
	}

	@Test
	public void should_not_increment_when_out_of_bounds() throws Exception {
		PixelGrid grid = new PixelGrid(
				0,
				0,
				1,
				1,
				1,
				1
		);
		grid.increment(2, 2);

		assertThat(grid.getPixelValues().get(0).get(0), is(0));
	}

	@Test
	public void should_increment_when_in_pixel_centre() throws Exception {
		PixelGrid grid = new PixelGrid(
				0,
				0,
				1,
				1,
				1,
				1
		);
		grid.increment(0.5, 0.5);

		assertThat(grid.getPixelValues().get(0).get(0), is(1));
	}

	@Test
	public void should_increment_when_in_pixel_bottom_left() throws Exception {
		PixelGrid grid = new PixelGrid(
				0,
				0,
				1,
				1,
				1,
				1
		);
		grid.increment(0, 0);

		assertThat(grid.getPixelValues().get(0).get(0), is(1));
	}

	@Test
	public void should_increment_when_in_pixel_bottom_left_when_pixel_is_not_at_origin() throws Exception {
		PixelGrid grid = new PixelGrid(
				-1,
				-1,
				1,
				1,
				1,
				1
		);
		grid.increment(-1, -1);

		assertThat(grid.getPixelValues().get(0).get(0), is(1));
	}

	@Test
	public void should_increment_all_pixels_in_two_by_one_grid() throws Exception {
		PixelGrid grid = new PixelGrid(
				-1,
				-1,
				1,
				1,
				1,
				2
		);
		grid.increment(-0.5, -0.5);
		grid.increment( 0.5, -0.5);
		grid.increment(-0.5,  0.5);
		grid.increment( 0.5,  0.5);

		assertThat(grid.getPixelValues().get(0).get(0), is(2));
		assertThat(grid.getPixelValues().get(0).get(1), is(2));
	}

	@Test
	public void should_increment_all_pixels_in_one_by_two_grid() throws Exception {
		PixelGrid grid = new PixelGrid(
				-1,
				-1,
				1,
				1,
				2,
				1
		);
		grid.increment(-0.5, -0.5);
		grid.increment( 0.5, -0.5);
		grid.increment(-0.5,  0.5);
		grid.increment( 0.5,  0.5);

		assertThat(grid.getPixelValues().get(0).get(0), is(2));
		assertThat(grid.getPixelValues().get(1).get(0), is(2));
	}

	@Test
	public void should_increment_all_pixels_in_two_by_two_grid() throws Exception {
		PixelGrid grid = new PixelGrid(
				-1,
				-1,
				1,
				1,
				2,
				2
		);
		grid.increment(-0.5, -0.5);
		grid.increment( 0.5, -0.5);
		grid.increment(-0.5,  0.5);
		grid.increment( 0.5,  0.5);

		assertThat(grid.getPixelValues().get(0).get(0), is(1));
		assertThat(grid.getPixelValues().get(0).get(1), is(1));
		assertThat(grid.getPixelValues().get(1).get(0), is(1));
		assertThat(grid.getPixelValues().get(1).get(1), is(1));
	}
}