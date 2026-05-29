package pl.sebcel.livecoding.dynamicprogramming.lcs;

public abstract class AbstractLCS implements LCS {

	protected final static boolean VERBOSE = true;

	protected void log(String message) {
		if (VERBOSE) {
			System.out.println(message);
		}
	}
}