package pl.sebcel.livecoding.dynamicprogramming;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class HouseRobbingTests {
	
	private HouseRobbing cut = new HouseRobbing();
	
	@Test
	public void should_maximize_amount_of_collected_money() {
		int[] houses = {2, 7, 9, 3, 1};
		int maxValue = cut.planRobbery(houses);
		assertEquals(12, maxValue);
	}
	
	@Test
	public void should_return_0_for_no_houses() {
		assertEquals(0, cut.planRobbery(new int[0]));
	}
	
	@Test
	public void should_return_house_value_for_single_house() {
		assertEquals(5, cut.planRobbery(new int[] {5}));
	}

}
