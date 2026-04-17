package pl.sebcel.livecoding.dynamicprogramming;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CoinAddingTests {
	
	private CoinsAdding cut = new CoinsAdding();
	
	@Test
	public void should_return_0_if_sum_is_0() {
		assertEquals(0, cut.calculateMinNumberOfRequiredCoins(null, 0));
	}
	
	@Test
	public void should_return_Integer_MAX_VALUE_for_no_coins() {
		assertEquals(Integer.MAX_VALUE, cut.calculateMinNumberOfRequiredCoins(null, 5));
		assertEquals(Integer.MAX_VALUE, cut.calculateMinNumberOfRequiredCoins(new int[0], 5));
	}

	@Test
	public void should_return_Integer_MAX_VALUE_for_negative_sum() {
		assertEquals(Integer.MAX_VALUE, cut.calculateMinNumberOfRequiredCoins(new int[]{2, 3, 7}, -1));
	}
	
	@Test
	public void should_return_1_if_coin_matches_the_sum() {
		assertEquals(1,  cut.calculateMinNumberOfRequiredCoins(new int[] {5}, 5));
	}

	@Test
	public void should_return_Integer_MAX_VALUE_if_not_possible_to_build_a_sum_for_particular_coins() {
		assertEquals(Integer.MAX_VALUE, cut.calculateMinNumberOfRequiredCoins(new int[] {2, 4}, 5));
		assertEquals(Integer.MAX_VALUE, cut.calculateMinNumberOfRequiredCoins(new int[] {3, 4}, 2));
	}
	
	@Test
	public void should_return_min_number_of_coins_that_build_a_sum() {
		assertEquals(3, cut.calculateMinNumberOfRequiredCoins(new int[] {2, 3, 5}, 12));
	}

}
