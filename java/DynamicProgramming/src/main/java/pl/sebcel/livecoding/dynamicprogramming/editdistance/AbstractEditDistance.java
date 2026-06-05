package pl.sebcel.livecoding.dynamicprogramming.editdistance;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractEditDistance implements EditDistance {
	
	protected boolean DEBUG = false;
	
	protected Map<Integer, String> indents = new HashMap<>();
	
	protected void log(int level, String message) {
		if (DEBUG) {
			if (!indents.containsKey(level)) {
				StringBuffer b = new StringBuffer();
				for (int i = 0; i < level; i++) {
					b.append("  ");
				}
				indents.put(level, b.toString());
			}
			System.out.println(indents.get(level) + message);
		}
	}
	
	protected int min(int a, int b, int c) {
		return Math.min(a, Math.min(b, c));
	}
}
