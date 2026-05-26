package pl.sebcel.livecoding.dynamicprogramming;

import java.util.Objects;

public class LongestCommonSubsequence {

	private final static boolean VERBOSE = false;

	public int calculateLongestCommonSubsequenceLength(String firstString, String secondString) {

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
	
/*

	x
	
	abx
      i
*/	

	private void log(String message) {
		if (VERBOSE) {
			System.out.println(message);
		}
	}

}

/*
 * 
 * abcd
 * 
 * a -> b 3 20
 * 
 * a -> c -> d 3 7 16
 * 
 * abcd ...a...c....e...d...b
 * 
 * 
 * - a - ab - abc - abcd abcd 0 - abc_ abc 0 - ab_ - ab_d abd 0 - ab__ ab 1 - a_
 * - a_c - a_cd acd 3 - a_c_ ac 2 - a__ - a__d ad 2 - a___ a 1 - _ - _b - _bc -
 * _bcd bcd 0 - _bc_ bc 0 - _b_ - _b_d bd 0 - _b__ b 1 - __ - __c - __cd cd 1 -
 * __c_ c 1 - ___ - ___d d 1 - ____
 * 
 */