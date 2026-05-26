package pl.sebcel.livecoding.dynamicprogramming;

public class LongestIncreasingSequence {

	public int calculateLongestIncreasingSequenceLength(int[] numbers) {
		if (numbers == null || numbers.length == 0) {
			return 0;
		}
		
		if (numbers.length == 1) {
			return 1;
		}
		
		int longestSequenceLength = 0;
		int[] lengths = new int[numbers.length];
		lengths[numbers.length - 1] = 1;
		
		for (int i = numbers.length - 2; i >= 0; i--) {
			int currentSequenceLength = findBestSequence(i, numbers, lengths) + 1;
			lengths[i] = currentSequenceLength;
			longestSequenceLength = Math.max(longestSequenceLength, currentSequenceLength);
		}
		
		return longestSequenceLength;	

	}
	
	private int findBestSequence(int i, int[] numbers, int[] lengths) {
		int longestSequenceLength = 0;
		for (int j = i+1; j < numbers.length; j++) {
			if (numbers[j] > numbers[i]) {
				longestSequenceLength = Math.max(longestSequenceLength, lengths[j]);
			}
		}
		return longestSequenceLength;
	}
}
