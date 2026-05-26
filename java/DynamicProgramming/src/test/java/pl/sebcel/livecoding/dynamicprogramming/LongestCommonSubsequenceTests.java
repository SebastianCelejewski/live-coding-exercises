package pl.sebcel.livecoding.dynamicprogramming;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LongestCommonSubsequenceTests {

	private LongestCommonSubsequence cut = new LongestCommonSubsequence();
	
	@Test
	public void should_return_0_if_any_of_strings_is_empty() {
		assertEquals(0,  cut.calculateLongestCommonSubsequenceLength("abc", ""));
		assertEquals(0,  cut.calculateLongestCommonSubsequenceLength("", "abc"));
		assertEquals(0,  cut.calculateLongestCommonSubsequenceLength("", ""));
	}
	
	@Test
	public void should_return_0_if_strings_are_built_from_different_characters() {
		assertEquals(0,  cut.calculateLongestCommonSubsequenceLength("abc", "def"));
		assertEquals(0,  cut.calculateLongestCommonSubsequenceLength("def", "abc"));
	}
	
	@Test
	public void should_return_length_of_identical_strings() {
		assertEquals(3, cut.calculateLongestCommonSubsequenceLength("abc", "abc"));
	}
	
	@Test
	public void should_return_length_of_a_string_that_is_completely_embedded_in_a_longer_string() {
		assertEquals(3,  cut.calculateLongestCommonSubsequenceLength("abc", "axbxc"));
		assertEquals(3,  cut.calculateLongestCommonSubsequenceLength("axbxc", "abc"));
	}
	
	@Test
	public void should_return_1_if_strings_are_built_from_the_same_characters_but_in_different_order() {
		assertEquals(1, cut.calculateLongestCommonSubsequenceLength("abc", "cba"));
		assertEquals(1, cut.calculateLongestCommonSubsequenceLength("cba", "abc"));
	}
	
	@Test
	public void should_handle_repeated_characters() {
		assertEquals(3,  cut.calculateLongestCommonSubsequenceLength("aab", "azab"));
		assertEquals(3,  cut.calculateLongestCommonSubsequenceLength("azab", "aab"));
	}
	
	@Test
	public void should_handle_multiple_equal_solutions() {
		assertEquals(2, cut.calculateLongestCommonSubsequenceLength("abc", "bac"));
		assertEquals(2, cut.calculateLongestCommonSubsequenceLength("bac", "abc"));
	}
	
	@Test
	public void should_handle_very_short_strings() {
		assertEquals(1, cut.calculateLongestCommonSubsequenceLength("a", "a"));
		assertEquals(0, cut.calculateLongestCommonSubsequenceLength("a", "b"));
		assertEquals(0, cut.calculateLongestCommonSubsequenceLength("b", "a"));
	}
	
	@Test
	public void should_handle_single_character_appearing_multiple_times() {
		assertEquals(2,  cut.calculateLongestCommonSubsequenceLength("aa", "aaaa"));
		assertEquals(2,  cut.calculateLongestCommonSubsequenceLength("aaaa", "aa"));
	}
	
	@Test
	public void should_handle_branching() {
		assertEquals(3, cut.calculateLongestCommonSubsequenceLength("abcde", "ace"));
		assertEquals(3, cut.calculateLongestCommonSubsequenceLength("ace", "abcde"));
	}
	
	@Test
	public void special_case_1() {
		assertEquals(3, cut.calculateLongestCommonSubsequenceLength("abcd", "...a...c....e...d...b"));
		assertEquals(3, cut.calculateLongestCommonSubsequenceLength("...a...c....e...d...b", "abcd"));
	}

}
