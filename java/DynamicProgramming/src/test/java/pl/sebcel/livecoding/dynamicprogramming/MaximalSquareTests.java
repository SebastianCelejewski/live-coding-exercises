package pl.sebcel.livecoding.dynamicprogramming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class MaximalSquareTests {
	
	private MaximalSquare cut = new MaximalSquare();
	
	@Test
	public void should_throw_NullPointerException_if_grid_is_null() {
		assertThrows(NullPointerException.class, () -> cut.findLargestSquareSize(null));
	}
	
	@Test
	public void should_throw_NullPointerException_if_grid_contains_null_rows_or_null_columns() {
		assertThrows(NullPointerException.class, () -> cut.findLargestSquareSize(new int[5][]));
	}

	@Test
	public void should_return_1_for_1x1_grid_containing_1() {
		assertEquals(1, cut.findLargestSquareSize(new int[][]{new int[]{1}}));
	}
	
	@Test
	public void should_return_0_for_1x1_grid_containing_0() {
		assertEquals(0, cut.findLargestSquareSize(new int[][]{new int[]{0}}));
	}
	
	@Test
	public void should_return_2_for_2x2_grid_containing_1s_only() {
		assertEquals(2, cut.findLargestSquareSize(new int[][]{
			new int[]{1, 1},
			new int[]{1, 1}
		}));
	}
	
	@Test
	public void should_return_1_for_2x2_grid_containing_1s_and_0s() {
		assertEquals(1, cut.findLargestSquareSize(new int[][]{
			new int[]{1, 1},
			new int[]{1, 0}
		}));
	}

	@Test
	public void should_return_1_for_1xn_grid_containing_1s_and_0s() {
		assertEquals(1, cut.findLargestSquareSize(new int[][]{
			new int[]{1, 1, 0, 1, 1, 0, 1}
		}));
	}

	@Test
	public void should_return_2_for_2x2_square_in_3x3_grid() {
		assertEquals(2, cut.findLargestSquareSize(new int[][]{
			new int[]{0, 0, 0},
			new int[]{0, 1, 1,},
			new int[]{0, 1, 1,}
		}));
	}

}
