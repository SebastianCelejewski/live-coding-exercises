package pl.sebcel.livecoding.dynamicprogramming;

public class HouseRobbing {
	
	public int planRobbery(int[] houses) {
		
		if (houses == null || houses.length == 0) {
		    return 0;
		}
	
		int dp_minus_1 = 0;
		int dp_minus_2 = 0;
		
		for (int i = 0; i < houses.length; i++) {
			int current = Math.max(dp_minus_1, dp_minus_2 + houses[i]);
			dp_minus_2 = dp_minus_1;
			dp_minus_1 = current;
		}
		
		return dp_minus_1;
	}

}
