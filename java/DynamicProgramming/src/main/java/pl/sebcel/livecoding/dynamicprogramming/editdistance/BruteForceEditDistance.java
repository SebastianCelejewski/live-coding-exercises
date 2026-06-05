package pl.sebcel.livecoding.dynamicprogramming.editdistance;

import java.util.Objects;

public class BruteForceEditDistance extends AbstractEditDistance {

	public int calculateEditDistance(String initial, String target) {
		Objects.requireNonNull(initial);
		Objects.requireNonNull(target);

		if (initial.equals(target)) {
			return 0;
		}

		return _calculateEditDistance(0, initial, target);

	}

	private int _calculateEditDistance(int level, String initial, String target) {
		if (initial.length() == 0 && target.length() == 0) {
			return 0;
		}

		if (initial.length() == 0 && target.length() > 0) {
			return 1 + _calculateEditDistance(level + 1, initial, target.substring(1));
		}

		if (initial.length() > 0 && target.length() == 0) {
			return 1 + _calculateEditDistance(level + 1, initial.substring(1), target);
		}

		if (initial.charAt(0) == target.charAt(0)) {
			return 0 + _calculateEditDistance(level + 1, initial.substring(1), target.substring(1));
		}

		int addCost = _calculateEditDistance(level + 1, initial, target.substring(1));
		int replaceCost = _calculateEditDistance(level + 1, initial.substring(1), target.substring(1));
		int removeCost = _calculateEditDistance(level + 1, initial.substring(1), target);
		int result = 1 + min(addCost, replaceCost, removeCost);
		
		return result;
	}
}
