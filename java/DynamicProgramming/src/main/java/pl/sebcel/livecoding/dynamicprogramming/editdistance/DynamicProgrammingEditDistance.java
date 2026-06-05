package pl.sebcel.livecoding.dynamicprogramming.editdistance;

import java.util.Objects;

public class DynamicProgrammingEditDistance extends AbstractEditDistance {

	@Override
	public int calculateEditDistance(String initial, String target) {
		Objects.requireNonNull(initial);
		Objects.requireNonNull(target);

		if (initial.equals(target)) {
			return 0;
		}
		
		int[][] dp = new int[initial.length() + 1][];
		for (int i = 0; i < initial.length() + 1; i++) {
			dp[i] = new int[target.length() + 1];
		}
		
		for (int i = initial.length(); i >=0; i--) {
			for (int j = target.length(); j >=0; j--) {

				// we reached the end of both strings - no further operations are necessary
				if (i == initial.length() && j == target.length()) {
					dp[i][j] = 0;
					continue;
				}
				
				// we reached the end of the target string - we need to remove characters from the initial
				if (i < initial.length() && j == target.length()) {
					dp[i][j] = 1 + dp[i+1][j];
					continue;
				}

				// we reached the end of the initial string - we need to add characters to the initial string 
				if (i == initial.length() && j < target.length()) {
					dp[i][j] = 1 + dp[i][j+1];
					continue;
				}

				// we are somewhere in the middle of both strings
				
				if (initial.charAt(i) == target.charAt(j)) {
					// characters are the same - no additional cost, so taking cost of next letters in both strings
					dp[i][j] = 0 + dp[i+1][j+1];
				} else {
					// characters are different and we have three options: replace, add or remove 
					int replaceCost = 1 + dp[i+1][j+1];
					int addCost = 1 + dp[i][j+1];
					int removeCost = 1 + dp[i+1][j];
					
					// we take the minimal cost of all of them
					dp[i][j] = min(replaceCost, addCost, removeCost);
				}
			}
		}
		
		return dp[0][0];
	}
}
