package pl.sebcel.livecoding.dynamicprogramming.knapsack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BruteForceStrangeKnapsack extends AbstractStrangeKnapsack {

	private final static boolean USE_CACHE = false;

	private static int operationsCounter = 0;

	public int getMaxItemWeight(int limit, int[] weights) {

		operationsCounter = 0;

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

		int result = limit - find(0, limit, weights, 0);
		System.out.println("" + operationsCounter + " operations");
		return result;
	}

	private String getKey(int limit, int idx) {
		return Integer.toString(limit) + (char) 0 + Integer.toString(idx);
	}

	private Map<String, Integer> cache = new HashMap<>();

	private int putToCacheAndReturn(int limit, int idx, int value) {
		System.out.println("Storing (" + limit + ", " + idx + "): " + value);
		if (USE_CACHE) {
			String cacheKey = getKey(limit, idx);
			if (!cache.containsKey(cacheKey)) {
				cache.put(cacheKey, value);
			}
		}

		return value;
	}

	private int find(int level, int limit, int[] weights, int idx) {
		operationsCounter++;
		log(level, "Operation " + operationsCounter);

		if (USE_CACHE) {
			String cacheKey = getKey(limit, idx);
			if (cache.containsKey(cacheKey)) {
				System.out.println("Fetching (" + limit + ", " + idx + "): " + cache.get(cacheKey));
				log(level, "CACHE HIT for (" + limit + ", " + idx + ")");
				return cache.get(cacheKey);
			}
		}

		log(level, print(weights, idx) + ", limit: " + limit);

		// checking if this is the last element
		if (idx == weights.length - 1) {
			System.out.println("  This is the last element");
			if (weights[idx] > limit) {
				log(level, "" + weights[idx] + " przekracza limit, więc jej nie bieżemy");
				System.out.println("  Element is bigger than current limit - we cannot take it, so storing the current limit");
				return putToCacheAndReturn(limit, idx, limit);
			} else {
				System.out.println("  Element is not bigger than current limit - we are storing the difference between current limit and the element");
				log(level, weights[idx] + " nie przekracza limitu, więc bieżemy i do maksymalnej pojemności brakuje nam " + (limit - weights[idx]));
				return putToCacheAndReturn(limit, idx, limit - weights[idx]);
			}
		}

		log(level, "Sprawdzam czy " + weights[idx] + " czasem nie przekracza limitu");
		if (weights[idx] > limit) {
			log(level, "Przekracza, więc wiadomo, że nie możemy jej wziąć. Sprawdzam, co będzie, kiedy nie wezmę");
			System.out.println("(" + limit + ", " + idx + ") wants (" + limit + ", " + (idx + 1) + ")");
			return putToCacheAndReturn(limit, idx, find(level + 1, limit, weights, idx + 1));
		}

		// take
		log(level, "Sprawdzam, co będzie, kiedy wezmę " + weights[idx]);
		System.out.println("(" + limit + ", " + idx + ") wants (" + (limit - weights[idx]) + ", " + (idx + 1) + ")");
		int valueIfTake = find(level + 1, limit - weights[idx], weights, idx + 1);

		// skip
		log(level, "Sprawdzam, co będzie, kiedy nie wezmę " + weights[idx]);
		System.out.println("(" + limit + ", " + idx + ") wants (" + limit  + ", " + (idx + 1) + ")");
		int valueIfSkip = find(level + 1, limit, weights, idx + 1);

		log(level, "Kiedy wezmę: " + valueIfTake + ", kiedy nie wezmę: " + valueIfSkip);

		int best = Math.min(valueIfTake, valueIfSkip);

		log(level, "Najlepsza wartość: " + best);

		return putToCacheAndReturn(limit, idx, best);
	}

}
