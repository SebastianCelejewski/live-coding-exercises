package pl.sebcel.livecoding.dynamicprogramming;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class UniquePathsTests {
	
	private UniquePaths cut = new UniquePaths();
	
	@Test
	public void should_return_0_if_grid_width_or_height_is_zero() {
		assertEquals(0, cut.findUniquePaths(0, 0));
		assertEquals(0, cut.findUniquePaths(0, 1));
		assertEquals(0, cut.findUniquePaths(1, 0));
	}
	
	@Test
	public void should_return_1_if_grid_has_only_one_column() {
		assertEquals(1, cut.findUniquePaths(1,  4));
	}


	@Test
	public void should_return_1_if_grid_has_only_one_row() {
		assertEquals(1, cut.findUniquePaths(4, 1));
	}
	
	@ParameterizedTest
	@CsvSource({"1, 1, 1", "2, 2, 2", "6, 3, 3"})
	public void should_calculate_number_of_unique_paths(int expected, int gridWidth, int gridHeight) {
		assertEquals(expected, cut.findUniquePaths(gridWidth, gridHeight));
	}

}
