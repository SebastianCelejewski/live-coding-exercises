package pl.sebcel.livecoding.dynamicprogramming;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LongestIncreasingSequenceTests {
	
	private LongestIncreasingSequence cut = new LongestIncreasingSequence();

	@Test
	public void should_return_0_for_empty_array() {
		assertEquals(0,  cut.calculateLongestIncreasingSequenceLength(new int[0]));
	}
	
	@Test
	public void should_return_1_for_single_element() {
		int[] singleElement = {5};
		assertEquals(1,  cut.calculateLongestIncreasingSequenceLength(singleElement));
	}
	
	@Test
	public void should_return_1_for_descending_values() {
		int[] descendingValues = {5, 3, 2};
		assertEquals(1, cut.calculateLongestIncreasingSequenceLength(descendingValues));
	}

	@Test
	public void should_return_length_for_consecutive_ascending_values() {
		int[] ascendingValues = {1, 2, 3};
		assertEquals(3,  cut.calculateLongestIncreasingSequenceLength(ascendingValues));
	}

	@Test
	public void should_return_length_for_not_congiguous_ascending_values() {
		int[] input = {5, 1, 2, 3, 10, 4, 5, 6};
		assertEquals(6,  cut.calculateLongestIncreasingSequenceLength(input));
	}
}
