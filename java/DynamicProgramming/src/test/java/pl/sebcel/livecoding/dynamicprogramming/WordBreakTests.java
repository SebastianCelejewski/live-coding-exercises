package pl.sebcel.livecoding.dynamicprogramming;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class WordBreakTests {

	private WordBreak cut = new WordBreak();

	@Test
	public void should_validate_arguments_against_null_values() {
		assertThrows(NullPointerException.class, () -> cut.checkIfWordCanBeBroken("abc", (String[]) null));
		assertThrows(NullPointerException.class, () -> cut.checkIfWordCanBeBroken("abc", "a", null));
		assertThrows(NullPointerException.class, () -> cut.checkIfWordCanBeBroken(null, "a", "b", "c"));
	}

	@Test
	public void should_return_false_for_empty_string() {
		assertFalse(cut.checkIfWordCanBeBroken("", "a", "b", "c"));
	}

	@Test
	public void should_return_false_for_empty_dictionary() {
		assertFalse(cut.checkIfWordCanBeBroken("abc", new String[0]));
	}

	@Test
	public void should_return_true_for_string_built_from_dictionary_elements() {
		assertTrue(cut.checkIfWordCanBeBroken("dogcathouse", "dog", "cat", "house"));
	}

	@Test
	public void should_handle_greedy_trap() {
		assertTrue(cut.checkIfWordCanBeBroken("dogsnake", "dog", "dogs", "snake"));
	}

	@Test
	public void should_return_false_when_string_cannot_be_fully_broken() {
		assertFalse(cut.checkIfWordCanBeBroken("dogcathouses", "dog", "cat", "house"));
	}

	@Test
	public void should_handle_single_word_dictionary() {
		assertTrue(cut.checkIfWordCanBeBroken("dog", "dog"));
	}

	@Test
	public void should_return_false_when_no_dictionary_word_matches_prefix() {
		assertFalse(cut.checkIfWordCanBeBroken("xyz", "dog", "cat", "house"));
	}

	@Test
	public void should_handle_multiple_possible_breaks() {
		assertTrue(cut.checkIfWordCanBeBroken("catsanddog", "cat", "cats", "and", "sand", "dog"));
	}

	@Test
	public void should_handle_classic_false_case() {
		assertFalse(cut.checkIfWordCanBeBroken("catsandog", "cats", "dog", "sand", "and", "cat"));
	}

	@Test
	public void should_handle_repeated_dictionary_elements() {
		assertTrue(cut.checkIfWordCanBeBroken("aaaaaa", "a", "aa", "aaa"));
	}

	@Test
	public void should_handle_long_repeated_sequences() {
		assertTrue(cut.checkIfWordCanBeBroken("aaaaaaaaaaaa", "a", "aa", "aaa", "aaaa"));
	}

	@Test
	public void should_return_false_when_last_character_cannot_be_matched() {
		assertFalse(cut.checkIfWordCanBeBroken("aaaaab", "a", "aa", "aaa", "aaaa"));
	}

	@Test
	public void should_handle_dictionary_elements_that_are_prefixes_of_other_elements() {
		assertTrue(cut.checkIfWordCanBeBroken("applepenapple", "apple", "pen", "applepen"));
	}

	@Test
	public void should_handle_word_break_with_many_branching_paths() {
		assertTrue(cut.checkIfWordCanBeBroken("pineapplepenapple", "apple", "pen", "applepen", "pine", "pineapple"));
	}

	@Test
	public void should_return_false_when_greedy_choice_leads_to_dead_end() {
		assertFalse(cut.checkIfWordCanBeBroken("aaaaaaaab", "a", "aa", "aaa", "aaaa"));
	}

	@Test
	public void should_handle_dictionary_containing_unused_words() {
		assertTrue(cut.checkIfWordCanBeBroken("dogcat", "dog", "cat", "house", "snake", "car"));
	}

	@Test
	public void should_handle_single_character_words() {
		assertTrue(cut.checkIfWordCanBeBroken("abcabc", "a", "b", "c"));
	}

	@Test
	public void should_return_false_when_only_partial_break_is_possible() {
		assertFalse(cut.checkIfWordCanBeBroken("dogcatx", "dog", "cat"));
	}

	@Test
	public void should_handle_entire_string_as_single_dictionary_word() {
		assertTrue(cut.checkIfWordCanBeBroken("encyclopedia", "encyclopedia", "ency", "clopedia"));
	}
}
