package pl.sebcel.livecoding.dynamicprogramming.knapsack;

public abstract class AbstractStrangeKnapsack implements StrangeKnapsack {
	
	private final static boolean VERBOSE = false;

	protected void log(int level, String message) {
		if (VERBOSE) {
			for (int i = 0; i < level; i++) {
				System.out.print("  ");
			}
			System.out.println(message);
		}
	}
	
	protected String print(int[] weights, int idx) {
		StringBuilder result = new StringBuilder();
		result.append("[");
		for (int i = idx; i < weights.length; i++) {
			result.append(weights[i]);
			if (i < weights.length - 1) {
				result.append(", ");
			}
		}
		result.append("]");
		return result.toString();
	}

}
