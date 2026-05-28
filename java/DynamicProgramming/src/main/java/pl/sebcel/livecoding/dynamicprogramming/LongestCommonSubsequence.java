package pl.sebcel.livecoding.dynamicprogramming;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LongestCommonSubsequence {

	private final static boolean VERBOSE = true;
	
	public int calculateLongestCommonSubsequenceLength(String firstString, String secondString) {
		return calculateLongestCommonSubsequenceLengthUsingDynamicProgramming(firstString, secondString);
	}
	
	public int calculateLongestCommonSubsequenceLengthUsingDynamicProgramming(String firstString, String secondString) {
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

	public int calculateLongestCommonSubsequenceLengthUsingBruteForce(String firstString, String secondString) {

		Objects.requireNonNull(firstString);
		Objects.requireNonNull(secondString);

		if (firstString.length() == 0 || secondString.length() == 0) {
			return 0;
		}

		String[] sequencesFromFirstString = generateSequences(firstString);
		int longestSequence = 0;
		for (String sequenceFromFirstString : sequencesFromFirstString) {
			log("\"" + sequenceFromFirstString + "\" in \"" + secondString + "\" Start");
			boolean stringContainsSequence = stringContainsSequence(secondString, sequenceFromFirstString);
			log("\"" + sequenceFromFirstString + "\" in \"" + secondString + "\": " + stringContainsSequence);
			log("");
			if (stringContainsSequence) {
				longestSequence = Math.max(longestSequence, sequenceFromFirstString.length());
			}
		}

		return longestSequence;
	}

	private String[] generateSequences(String string) {
		String[] sequences = new String[(int) Math.pow(2, string.length() + 1)];

		for (int i = 0; i < Math.pow(2, string.length() + 1); i++) {
			StringBuffer sequence = new StringBuffer();
			for (int j = 0; j < string.length(); j++) {
				int includeLetter = i & (int) Math.pow(2, j);
				if (includeLetter > 0) {
					sequence.append(string.charAt(j));
				}
			}
			sequences[i] = sequence.toString();
		}

		return sequences;
	}

	private boolean stringContainsSequence(String string, String sequence) {
		if (sequence.length() == 0) {
			return false;
		}
		
		int pointerInString = 0;
		for (int i = 0; i < sequence.length(); i++) {
			char charToFind = sequence.charAt(i);
			log("  i: " + i + ", char to find: " + charToFind);
			if (pointerInString == string.length()) {
				log("  p:" + pointerInString + " Reached the end of the string. Cannot look for another character");
				return false;
			}
			while (string.charAt(pointerInString) != charToFind) {
				pointerInString++;
				log("  p:" + pointerInString);
				if (pointerInString == string.length()) {
					log("  p:" + pointerInString + " Reached the end of the string. Character not found");
					return false;	
				}
			}
			log("  Found character " + charToFind + " at position " + pointerInString);
			pointerInString++;
			log("  p:" + pointerInString);
		}

		return true;
	}

	private void log(String message) {
		if (VERBOSE) {
			System.out.println(message);
		}
	}

}
