package pl.sebcel.livecoding.dynamicprogramming.editdistance;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BruteForceEditDistanceWithCacheAndIndicesInsteadSubstrings extends AbstractEditDistance {
	
	private Map<String, Integer> memoizationCache = new HashMap<>();

	public int calculateEditDistance(String initial, String target) {
		Objects.requireNonNull(initial);
		Objects.requireNonNull(target);

		if (initial.equals(target)) {
			return 0;
		}

		return _calculateEditDistance(0, initial, target, 0, 0);

	}

	private int _calculateEditDistance(int level, String initial, String target, int initialIdx, int targetIdx) {
		
		String cacheKey = getCacheKey(initialIdx, targetIdx);
		if (memoizationCache.containsKey(cacheKey)) {
			return memoizationCache.get(cacheKey);
		}
		
		if (initialIdx == initial.length() && targetIdx == target.length()) {
			return addToCacheAndReturn(initialIdx, targetIdx, 0);
		}

		if (initialIdx == initial.length() && targetIdx < target.length()) {
			return addToCacheAndReturn(initialIdx, targetIdx, 1 + _calculateEditDistance(level + 1, initial, target, initialIdx, targetIdx + 1));
		}

		if (initialIdx < initial.length() && targetIdx == target.length()) {
			return addToCacheAndReturn(initialIdx, targetIdx, 1 + _calculateEditDistance(level + 1, initial, target, initialIdx + 1, targetIdx));
		}

		if (initial.charAt(initialIdx) == target.charAt(targetIdx)) {
			return addToCacheAndReturn(initialIdx, targetIdx, 0 + _calculateEditDistance(level + 1, initial, target, initialIdx + 1, targetIdx + 1));
		}

		int addCost = _calculateEditDistance(level + 1, initial, target, initialIdx, targetIdx + 1);
		int replaceCost = _calculateEditDistance(level + 1, initial, target, initialIdx + 1, targetIdx + 1);
		int removeCost = _calculateEditDistance(level + 1, initial, target, initialIdx + 1, targetIdx);
		int result = 1 + min(addCost, replaceCost, removeCost);
		
		return addToCacheAndReturn(initialIdx, targetIdx, result);
	}
	
	private String getCacheKey(int initialIdx, int targetIdx) {
		return Integer.toString(initialIdx) + (char) 0 + Integer.toString(targetIdx);
	}
	
	private int addToCacheAndReturn(int initialIdx, int targetIdx, int result) {
		String cacheKey = getCacheKey(initialIdx, targetIdx);
		if (!memoizationCache.containsKey(cacheKey)) {
			memoizationCache.put(cacheKey, result);
		}
		return result;
	}
}
