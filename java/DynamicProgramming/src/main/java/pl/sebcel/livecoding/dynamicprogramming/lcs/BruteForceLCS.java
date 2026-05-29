package pl.sebcel.livecoding.dynamicprogramming.lcs;

import java.util.Objects;

public class BruteForceLCS extends AbstractLCS {
	
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
}
