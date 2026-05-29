package pl.sebcel.livecoding.dynamicprogramming.lcs;

import java.util.Objects;

public class DPLCSv2 extends AbstractLCS {

	@Override
	public int calculateLongestCommonSubsequenceLength(String firstString, String secondString) {
		Objects.requireNonNull(firstString);
		Objects.requireNonNull(secondString);

		if (firstString.length() == 0 || secondString.length() == 0) {
			return 0;
		}

		int[][] states = new int[firstString.length()][];

		for (int i = 0; i < firstString.length(); i++) {
			states[i] = new int[secondString.length()];
			for (int j = 0; j < secondString.length(); j++) {

				if (firstString.charAt(i) == secondString.charAt(j)) {
					states[i][j] = findMaxInSubMatrix(i, j, states) + 1;
				} else {
					states[i][j] = getBestSoFar(i, j, states);
				}
			}
		}

		print(firstString, secondString, states);

		return findMaxInSubMatrix(firstString.length(), secondString.length(), states);
	}
	
	private int findMaxInSubMatrix(int i, int j, int[][] states) {
		if (i > 0 && j > 0) {
			return states[i-1][j-1];
		} else {
			return 0;
		}
	}
	
	private int getBestSoFar(int i, int j, int[][] states) {
		int best = 0;
		if (i > 0) {
			best = Math.max(best, states[i-1][j]);
		}
		if (j > 0) {
			best = Math.max(best,  states[i][j-1]);
		}
		return best;
	}

	private void print(String firstString, String secondString, int[][] states) {
		if (VERBOSE) {
			System.out.println("");
			System.out.print(" ");
			for (int i = 0; i < secondString.length(); i++) {
				System.out.print("  " + secondString.charAt(i));
			}
			System.out.println("");

			for (int i = 0; i < firstString.length(); i++) {
				System.out.print(firstString.charAt(i));
				for (int j = 0; j < secondString.length(); j++) {
					System.out.print("  " + states[i][j]);
				}
				System.out.println("");
			}
		}
	}
}
