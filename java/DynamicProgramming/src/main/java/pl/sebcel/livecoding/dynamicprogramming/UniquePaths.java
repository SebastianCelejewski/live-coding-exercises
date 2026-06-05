package pl.sebcel.livecoding.dynamicprogramming;

public class UniquePaths {

	public int findUniquePaths(int gridWidth, int gridHeight) {

		if (gridWidth <= 0 || gridHeight <= 0) {
			return 0;
		}

		int[][] dp = new int[gridWidth][];
		for (int i = 0; i < gridWidth; i++) {
			dp[i] = new int[gridHeight];
		}

		for (int i = 0; i < gridWidth; i++) {
			for (int j = 0; j < gridHeight; j++) {
				if (i == 0 && j == 0) {
					dp[i][j] = 1;
					continue;
				}
				int numberOfPaths = 0;
				if (i > 0) {
					numberOfPaths += dp[i - 1][j];
				}
				if (j > 0) {
					numberOfPaths += dp[i][j - 1];
				}
				dp[i][j] = numberOfPaths;
			}
		}

		return dp[gridWidth - 1][gridHeight - 1];
	}

}
