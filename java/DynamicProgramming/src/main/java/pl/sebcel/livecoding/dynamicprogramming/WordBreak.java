package pl.sebcel.livecoding.dynamicprogramming;

import java.util.Objects;

public class WordBreak {

	public boolean checkIfWordCanBeBroken(String word, String... dictionary) {
		Objects.requireNonNull(word);
		Objects.requireNonNull(dictionary);
		for (String s : dictionary) {
			Objects.requireNonNull(s);
		}
		if (word.length() == 0) {
			return false;
		}
		
		boolean[] dp = new boolean[word.length()];
		
		for (int i = word.length() - 1; i >=0; i--) {
			String substring = word.substring(i);
			for (String dictionaryWord : dictionary) {
				if (substring.startsWith(dictionaryWord)) {
					if (i + dictionaryWord.length() == word.length()) {
						// reached the end of the word
						dp[i] = true;
					} else if (dp[i + dictionaryWord.length()]) {
						// we are in the middle of the word and there is a continuation
						dp[i] = true;
					}
				}
			}
		}
		
		return dp[0];
	}
	
}
