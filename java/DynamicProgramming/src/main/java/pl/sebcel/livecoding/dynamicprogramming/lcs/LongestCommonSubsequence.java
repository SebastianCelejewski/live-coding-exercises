package pl.sebcel.livecoding.dynamicprogramming.lcs;

public class LongestCommonSubsequence {

	private BruteForceLCS bruteForceLCS = new BruteForceLCS();
	private DPLCSv1 dpLcsV1 = new DPLCSv1();
	private DPLCSv2 dpLcsV2 = new DPLCSv2();

	public int calculateLongestCommonSubsequenceLength(String firstString, String secondString) {
		return dpLcsV2.calculateLongestCommonSubsequenceLength(firstString, secondString);
	}
}
