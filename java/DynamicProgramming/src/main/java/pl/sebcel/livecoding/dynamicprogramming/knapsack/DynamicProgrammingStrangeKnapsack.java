package pl.sebcel.livecoding.dynamicprogramming.knapsack;

import java.util.Objects;

public class DynamicProgrammingStrangeKnapsack extends AbstractStrangeKnapsack {

	public int getMaxItemWeight(int limit, int[] weights) {

		Objects.requireNonNull(weights);

		if (weights.length == 0) {
			return 0;
		}

		if (limit == 0) {
			return 0;
		}

		if (limit < 0) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < weights.length; i++) {
			Objects.requireNonNull(weights[i]);
			if (weights[i] <= 0) {
				throw new IllegalArgumentException();
			}
		}

		int[][] dp = new int[weights.length][];
		for (int i = 0; i < weights.length; i++) {
			dp[i] = new int[limit + 1];
		}
		
		int numerOfOperations = 0;

		for (int i = weights.length - 1; i >= 0; i--) {
			for (int j = 0; j <= limit; j++) {
				numerOfOperations++;
				if (i == weights.length - 1) {
					if (weights[i] > j) {
						dp[i][j] = j;
					} else {
						dp[i][j] = j - weights[i];
					}
				} else {
					if (j < weights[i]) {
						dp[i][j] = dp[i+1][j];
					} else {
						dp[i][j] = Math.min(dp[i + 1][j], dp[i + 1][j - weights[i]]);
					}
				}
			}
		}

		System.out.println(numerOfOperations);
		return limit - dp[0][limit];
	}
}
