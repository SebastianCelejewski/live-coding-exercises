package pl.sebcel.livecoding.dynamicprogramming;

import java.util.Objects;

public class MinimumPathSum {
	
	public int calculateMinimumPathSum(int[][] grid) {
		Objects.requireNonNull(grid);
		for (int i = 0; i < grid.length; i++) {
			Objects.requireNonNull(grid[i]);
		}
		
		int[][] dp = new int[grid.length][];
		for (int i = 0; i < grid.length; i++) {
			dp[i] = new int[grid[0].length];
		}
		
		dp[0][0] = 0; // dp[0][0] = grid[0][0] depending on problem definition
		
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (i == 0 && j == 0) {
					continue;
				}
				if (i > 0 && j == 0) {
					dp[i][j] = grid[i][j] + dp[i-1][j];
					continue;
				}
				
				if (i == 0 && j > 0) {
					dp[i][j] = grid[i][j] + dp[i][j-1];
					continue;
				}

				dp[i][j] = grid[i][j] + Math.min(dp[i-1][j], dp[i][j-1]);
			}
		}
		
		return dp[grid.length - 1][grid[0].length -1];
	}

}
