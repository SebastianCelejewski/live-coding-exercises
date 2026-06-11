package pl.sebcel.livecoding.dynamicprogramming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import pl.sebcel.livecoding.dynamicprogramming.knapsack.DynamicProgrammingKnapsack;
import pl.sebcel.livecoding.dynamicprogramming.knapsack.Knapsack;

public class KnapsackTests {

	private Knapsack cut = new DynamicProgrammingKnapsack();

	@Test
	public void should_validate_input() {
		assertThrows(IllegalArgumentException.class, () -> cut.getMaxItemWeight(-5, new int[] { 2, 3 }));
		assertThrows(IllegalArgumentException.class, () -> cut.getMaxItemWeight(5, new int[] { -2, 3 }));
		assertThrows(NullPointerException.class, () -> cut.getMaxItemWeight(5, null));
	}

	@Test
	public void should_return_zero_when_capacity_is_zero() {
		assertEquals(0, cut.getMaxItemWeight(0, new int[] { 3, 4, 5 }));
	}

	@Test
	public void should_return_zero_when_no_item_fits() {
		assertEquals(0, cut.getMaxItemWeight(2, new int[] { 3, 4, 5 }));
	}

	@Test
	public void should_select_single_item_when_only_one_fits() {
		assertEquals(3, cut.getMaxItemWeight(3, new int[] { 3, 4, 5 }));
	}

	@Test
	public void should_select_two_items_when_both_fit_exactly() {
		assertEquals(7, cut.getMaxItemWeight(7, new int[] { 3, 4, 5 }));
	}

	@Test
	public void should_skip_heavy_item_to_fit_more_items() {
		assertEquals(7, cut.getMaxItemWeight(7, new int[] { 3, 4, 6 }));
	}

	@Test
	public void should_handle_exact_fit_using_all_capacity() {
		assertEquals(8, cut.getMaxItemWeight(8, new int[] { 3, 4, 5 }));
	}

	@Test
	public void should_handle_case_where_only_last_item_fits() {
		assertEquals(5, cut.getMaxItemWeight(5, new int[] { 6, 7, 5 }));
	}

	@Test
	public void should_handle_multiple_equivalent_solutions() {
		assertEquals(6, cut.getMaxItemWeight(6, new int[] { 3, 3, 3 }));
	}

	@Test
	public void should_handle_many_small_items() {
		assertEquals(4, cut.getMaxItemWeight(4, new int[] { 1, 1, 1, 1, 5 }));
	}

	@Test
	public void should_handle_greedy_trap() {
		assertEquals(10, cut.getMaxItemWeight(10, new int[] { 8, 5, 5 }));
	}

	@Test
	public void should_handle_capacity_larger_than_total_weight() {
		assertEquals(12, cut.getMaxItemWeight(20, new int[] { 3, 4, 5 }));
	}

	@Test
	public void should_handle_single_item_input() {
		assertEquals(5, cut.getMaxItemWeight(5, new int[] { 5 }));
	}

	@Test
	public void should_return_empty_selection_for_empty_items() {
		assertEquals(0, cut.getMaxItemWeight(10, new int[] {}));
	}

	@Test
	public void should_handle_branching_decisions() {
		assertEquals(10, cut.getMaxItemWeight(10, new int[] { 2, 3, 5, 6 }));
	}

	@Test
	public void should_handle_many_equal_weights() {
		assertEquals(6, cut.getMaxItemWeight(6, new int[] { 2, 2, 2, 2, 2, 2 }));
	}

	@Test
	public void should_show_huge_advantage_of_dp_over_bruteforce() {
		assertEquals(20, cut.getMaxItemWeight(20, new int[] { 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4 }));
	}
}
