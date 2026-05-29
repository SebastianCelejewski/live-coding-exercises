package pl.sebcel.livecoding.dynamicprogramming.lcs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DPLCSv1 extends AbstractLCS {
	
	public int calculateLongestCommonSubsequenceLength(String firstString, String secondString) {
		Objects.requireNonNull(firstString);
		Objects.requireNonNull(secondString);

		if (firstString.length() == 0 || secondString.length() == 0) {
			return 0;
		}
		
		List<List<Integer>> positionsInSecondString = new ArrayList<List<Integer>>();
		for (int i = 0; i < firstString.length(); i++) {
			char letter = firstString.charAt(i);
			positionsInSecondString.add(findPositionsOfLetterInString(letter, secondString));
		}
		
		int[][] lengths = new int[firstString.length()][];
		int totalLongest = 0;
		for (int i = 0; i < firstString.length(); i++) {
			log("Checking character '" + firstString.charAt(i) +"' (position " + i + ")");
			lengths[i] = new int[positionsInSecondString.get(i).size()];
			List<Integer> myPositionsInSecondString = positionsInSecondString.get(i);
			if (myPositionsInSecondString.size() == 0) {
				lengths[i] = new int[0];
				log("  It does not appear in the second string. Skipping");
			} else {
				log("  It appears in the second string on " + myPositionsInSecondString.size() + " position(s)");
				for (int p = 0; p < myPositionsInSecondString.size(); p++) {
					log("    Checking position " + myPositionsInSecondString.get(p));
					int myPositionInSecondString = myPositionsInSecondString.get(p);
					int bestLengthForThisPosition = findBestLength(i, myPositionInSecondString, lengths, positionsInSecondString) + 1;
					lengths[i][p] = Math.max(lengths[i][p], bestLengthForThisPosition);
					log("      Best length for this position is " + lengths[i][p]);
					totalLongest = Math.max(totalLongest, lengths[i][p]);
					log("      Total longest is " + lengths[i][p]);
				}
			}
		}

		return totalLongest;
	}
	
	private int findBestLength(int myIndexInFirstString, int myPositionInSecondString, int[][] lengths, List<List<Integer>> positionsInSecondString) {
		if (myIndexInFirstString == 0) {
			return 0;
		}
		int bestSoFar = 0;
		for (int j = 0; j < myIndexInFirstString; j++) {
			List<Integer> candidatesPositionInSecondString = positionsInSecondString.get(j);
			if (candidatesPositionInSecondString.size() > 0) {
				for (int q = 0; q < candidatesPositionInSecondString.size(); q++) {
					int candidatePositionInSecondString = candidatesPositionInSecondString.get(q); 
					if (candidatePositionInSecondString < myPositionInSecondString) {
						bestSoFar = Math.max(bestSoFar, lengths[j][q]);
					}
				}
			}
		}
		return bestSoFar;
	}
	
	private List<Integer> findPositionsOfLetterInString(char letter, String string) {
		List<Integer> result = new ArrayList<>();
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == letter) {
				result.add(i);
			}
		}
		return result;
	}
}