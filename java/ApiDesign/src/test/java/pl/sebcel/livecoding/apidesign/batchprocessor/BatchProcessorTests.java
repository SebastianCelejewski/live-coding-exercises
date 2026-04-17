package pl.sebcel.livecoding.apidesign.batchprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import pl.sebcel.livecoding.apidesign.batchprocessor.ProcessingResult.ProcessingStatus;

public class BatchProcessorTests {

	@Test
	public void Should_process_all_elements_when_there_are_no_processing_errors() throws ProcessingCancelledException {
		List<Integer> input = List.of(1, 5, 3, 6, 2, -2, -3, -1, -5, -7);
		ProcessingResult<Integer> result = BatchProcessor
								.<Integer, Integer>from(input)
								.withBatchSize(5)
								.parallel()
								.onErrorContinue()
								.withRetries(5)
								.process(list -> list.stream().map(x -> 2 * x).toList(), this::reportProgress, () -> false);
		
		assertEquals(ProcessingStatus.SUCCESS, result.getStatus());
		assertEquals(10, result.getData().size());
	}
	
	@Test
	public void Should_process_as_many_elements_as_possible_when_there_is_a_processing_error_but_configured_to_continue_upon_error() throws ProcessingCancelledException {
		List<Integer> input = List.of(1, 5, 3, 6, 2, -2, -3, -1, -5, -7);
		ProcessingResult<Integer> result = BatchProcessor
								.<Integer, Integer>from(input)
								.withBatchSize(5)
								.parallel()
								.onErrorContinue()
								.withRetries(5)
								.process(data -> {
									if (data.get(0) > 0) {
										return data;
									} else {
										throw new RuntimeException("Processing exception");
									}
								}, this::reportProgress, () -> false);
		
		assertEquals(ProcessingStatus.PROCESSED_WITH_ERRORS, result.getStatus());
		assertEquals(5, result.getData().size());
	}

	@Test
	public void Should_process_as_many_elements_as_possible_when_there_is_a_processing_error_but_configured_to_stop_upon_error() throws ProcessingCancelledException {
		List<Integer> input = List.of(1, 5, 3, 6, 2, -2, -3, -1, -5, -7);
		ProcessingResult<Integer> result = BatchProcessor
								.<Integer, Integer>from(input)
								.withBatchSize(5)
								.parallel()
								.onErrorStop()
								.withRetries(5)
								.process(_ -> {
									throw new RuntimeException("Processing exception");
								}, this::reportProgress, () -> false);
		
		assertEquals(ProcessingStatus.FAILURE, result.getStatus());
		assertEquals(0, result.getData().size());
	}
	
	private void reportProgress(double percentCompleted) {
		System.out.println((int) (100 * percentCompleted) + "%");
	}
}
