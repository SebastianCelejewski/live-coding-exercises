package pl.sebcel.livecoding.dynamicprogramming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class MinimumPathSumTests {
	
	private MinimumPathSum cut = new MinimumPathSum();
	
	@Test
	public void should_throw_NullPointerException_if_grid_is_null() {
		assertThrows(NullPointerException.class, () -> cut.calculateMinimumPathSum(null));
	}
	
	@Test
	public void should_throw_NullPointerException_if_grid_contains_null_rows_or_null_columns() {
		assertThrows(NullPointerException.class, () -> cut.calculateMinimumPathSum(new int[5][]));
	}

	@Test
	public void should_return_0_for_1x1_grid() {
		assertEquals(0, cut.calculateMinimumPathSum(new int[][]{new int[]{7}}));
	}
	
	@Test
	public void should_return_path_with_lower_cost_for_2x2_grid() {
		assertEquals(6, cut.calculateMinimumPathSum(new int[][]{
			new int[]{1, 2},
			new int[]{3, 4}
		}));
	}
	
}
