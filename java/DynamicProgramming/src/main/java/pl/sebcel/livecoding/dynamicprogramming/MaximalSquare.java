package pl.sebcel.livecoding.dynamicprogramming;

import java.util.Objects;

public class MaximalSquare {

	public int findLargestSquareSize(int[][] grid) {

		Objects.requireNonNull(grid);
		for (int i = 0; i < grid.length; i++) {
			Objects.requireNonNull(grid[i]);
		}

		int[][] dp = new int[grid.length][];
		for (int i = 0; i < grid.length; i++) {
			dp[i] = new int[grid[0].length];
		}

		int maxSquare = 0;
		
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == 0) {
					dp[i][j] = 0;
					continue;
				}

				int leftSize = 0;
				int topSize = 0;
				int diagonalSize = 0;

				if (i > 0) {
					leftSize = dp[i - 1][j];
				}
				if (j > 0) {
					topSize = dp[i][j - 1];
				}
				if (i > 0 && j > 0) {
					diagonalSize = dp[i - 1][j - 1];
				}

				dp[i][j] = 1 + Math.min(leftSize, Math.min(diagonalSize, topSize));
				maxSquare = Math.max(maxSquare, dp[i][j]);
			}
		}

		return maxSquare;
	}
}
