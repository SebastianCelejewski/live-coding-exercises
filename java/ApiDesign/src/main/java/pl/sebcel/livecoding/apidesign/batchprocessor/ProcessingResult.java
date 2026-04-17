package pl.sebcel.livecoding.apidesign.batchprocessor;

import java.util.Collections;
import java.util.List;

public class ProcessingResult<T> {
	
	public static enum ProcessingStatus { SUCCESS, PROCESSED_WITH_ERRORS, FAILURE }

	private ProcessingStatus status;
	private List<T> data;
	private List<Exception> errors;
	
	public ProcessingStatus getStatus() {
		return status;
	}
	
	public List<T> getData() {
		return data;
	};
	
	public List<Exception> getErrors() {
		return errors;
	}
	
	private ProcessingResult(ProcessingStatus status, List<T> data, List<Exception> errors) {
		this.status = status;
		this.data = List.copyOf(data);
		this.errors = List.copyOf(errors);
	}
	
	public static <T> ProcessingResult<T> success(List<T> data) {
		return new ProcessingResult<>(ProcessingStatus.SUCCESS, data, Collections.emptyList());
	}
	
	public static <T> ProcessingResult<T> processedWithErrors(List<T> data, List<Exception> errors) {
		return new ProcessingResult<>(ProcessingStatus.PROCESSED_WITH_ERRORS, data, errors);
	}
	
	public static <T> ProcessingResult<T> failure(List<Exception> errors) {
		return new ProcessingResult<>(ProcessingStatus.FAILURE, Collections.emptyList(), errors);
	}
}
