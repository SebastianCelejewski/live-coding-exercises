package pl.sebcel.livecoding.dynamicprogramming;

public class CoinsAdding {

	public int calculateMinNumberOfRequiredCoins(int[] coins, int sum) {
		if (sum == 0) {
			return 0;
		}

		if (sum < 0 || coins == null || coins.length == 0) {
			return Integer.MAX_VALUE;
		}

		int[] dp = new int[sum + 1];
		dp[0] = 0;

		for (int i = 1; i <= sum; i++) {

			int min = Integer.MAX_VALUE;

			for (int coinValue : coins) {
				if (i >= coinValue && dp[i - coinValue] != Integer.MAX_VALUE) {
					min = Math.min(min, dp[i - coinValue] + 1);
				}
			}

			dp[i] = min;
		}

		return dp[sum];
	}
}