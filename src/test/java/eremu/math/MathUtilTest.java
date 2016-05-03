package eremu.math;

import org.junit.Test;

import static eremu.math.MathUtil.progress;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class MathUtilTest {

	@Test
	public void progress_should_work() throws Exception {
		assertThat(progress(0, 1, 0.0, 1.0), is(0.0));
		assertThat(progress(1, 1, 0.0, 1.0), is(1.0));

		assertThat(progress(0, 1, -1.0, 2.0), is(-1.0));
		assertThat(progress(1, 1, -1.0, 2.0), is(2.0));

		assertThat(progress(0,  10, -5.0, 5.0), is(-5.0));
		assertThat(progress(1,  10, -5.0, 5.0), is(-4.0));
		assertThat(progress(2,  10, -5.0, 5.0), is(-3.0));
		assertThat(progress(3,  10, -5.0, 5.0), is(-2.0));
		assertThat(progress(4,  10, -5.0, 5.0), is(-1.0));
		assertThat(progress(5,  10, -5.0, 5.0), is( 0.0));
		assertThat(progress(6,  10, -5.0, 5.0), is( 1.0));
		assertThat(progress(7,  10, -5.0, 5.0), is( 2.0));
		assertThat(progress(8,  10, -5.0, 5.0), is( 3.0));
		assertThat(progress(9,  10, -5.0, 5.0), is( 4.0));
		assertThat(progress(10, 10, -5.0, 5.0), is( 5.0));
	}
}