package pl.sebcel.livecoding.dynamicprogramming;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import pl.sebcel.livecoding.dynamicprogramming.editdistance.BruteForceEditDistanceWithCache;
import pl.sebcel.livecoding.dynamicprogramming.editdistance.EditDistance;

public class EditDistanceTests {

	private EditDistance cut = new BruteForceEditDistanceWithCache();
	
	@Test
	public void should_throw_NullPointerException_if_any_of_input_strings_are_null() {
		assertThrows(NullPointerException.class, () -> {
			editDistance("A", null);
		});

		assertThrows(NullPointerException.class, () -> {
			editDistance(null, "A");
		});

		assertThrows(NullPointerException.class, () -> {
			editDistance(null, null);
		});
	}

	@Test
	public void should_return_0_for_two_empty_strings() {
		assertEquals(0, editDistance("", ""));
	}

	@Test
	public void should_return_length_of_second_string_when_first_string_is_empty() {
		assertEquals(3, editDistance("", "abc"));
	}

	@Test
	public void should_return_length_of_first_string_when_second_string_is_empty() {
		assertEquals(3, editDistance("abc", ""));
	}

	@Test
	public void should_return_0_for_identical_strings() {
		assertEquals(0, editDistance("abc", "abc"));
	}

	@Test
	public void should_handle_single_character_replacement() {
		assertEquals(1, editDistance("abc", "axc"));
	}

	@Test
	public void should_handle_single_character_insertion() {
		assertEquals(1, editDistance("abc", "abxc"));
	}

	@Test
	public void should_handle_single_character_deletion() {
		assertEquals(1, editDistance("abxc", "abc"));
	}

	@Test
	public void should_handle_multiple_replacements() {
		assertEquals(3, editDistance("abc", "xyz"));
	}

	@Test
	public void should_handle_classic_kitten_sitting_case() {
		assertEquals(3, editDistance("kitten", "sitting"));
	}

	@Test
	public void should_handle_classic_intention_execution_case() {
		assertEquals(5, editDistance("intention", "execution"));
	}

	@Test
	public void should_handle_repeated_characters() {
		assertEquals(2, editDistance("aaaaa", "aaa"));
	}

	@Test
	public void should_handle_shifted_strings() {
		assertEquals(1, editDistance("abcdef", "zabcdef"));
	}

	@Test
	public void should_handle_non_trivial_mixed_operations() {
		assertEquals(3, editDistance("abcdef", "azced"));
	}

	@Test
	public void should_handle_strings_with_many_possible_paths() {
		assertEquals(2, editDistance("banana", "ananas"));
	}

	@Test
	public void should_handle_completely_different_strings() {
		assertEquals(6, editDistance("abcdef", "uvwxyz"));
	}

	@Test
	public void should_handle_single_character_strings() {
		assertEquals(1, editDistance("a", "b"));
	}

	@Test
	public void should_handle_insertion_at_the_beginning() {
		assertEquals(1, editDistance("abc", "zabc"));
	}

	@Test
	public void should_handle_insertion_at_the_end() {
		assertEquals(1, editDistance("abc", "abcz"));
	}

	@Test
	public void should_handle_deletion_at_the_beginning() {
		assertEquals(1, editDistance("zabc", "abc"));
	}

	@Test
	public void should_handle_deletion_at_the_end() {
		assertEquals(1, editDistance("abcz", "abc"));
	}

	@Test
	public void should_handle_interleaved_changes() {
		assertEquals(3, editDistance("axbxcxd", "abcd"));
	}

	@Test
	public void should_handle_long_common_subsequence_with_noise() {
		assertEquals(14, editDistance("xxAxxBxxCxxDxxExxFxx", "AyyByyCyyDyyEyyF"));
	}

	private int editDistance(String initial, String target) {
		return cut.calculateEditDistance(initial, target);
	}
}
