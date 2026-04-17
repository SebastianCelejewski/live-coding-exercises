package pl.sebcel.livecoding.apidesign.batchprocessor;

import java.util.List;

public class BatchProcessor
{
	public static <T, R> ProcessingConfiguration<T, R> from(List<T> elements) {
		return new ProcessingConfiguration<T, R>(elements);
	}
}
