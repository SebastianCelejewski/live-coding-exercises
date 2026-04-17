package pl.sebcel.livecoding.apidesign.batchprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ProcessingConfiguration<T, R>  {
	
	public enum OnErrorPolicy { CONTINUE, STOP }
	
	private final static int DEFAULT_BATCH_SIZE = 100;
	private final static boolean DEFAULT_PARALLELISM = false;
	private final static int DEFAULT_NUMBER_OF_RETIRES = 0;
	private final static OnErrorPolicy DEFAULT_ON_ERROR_POLICY = OnErrorPolicy.STOP;
	
	private List<T> data;
	
	private int batchSize = DEFAULT_BATCH_SIZE;
	private boolean parallel = DEFAULT_PARALLELISM;
	private int numberOfRetries = DEFAULT_NUMBER_OF_RETIRES;
	private OnErrorPolicy onErrorPolicy = DEFAULT_ON_ERROR_POLICY;
	
	public List<T> getData() {
		return data;
	}
	
	public ProcessingConfiguration<T, R> withBatchSize(int batchSize) {
		this.batchSize = batchSize;
		return this;
	}
	
	public ProcessingConfiguration<T, R> parallel() {
		this.parallel = true;
		return this;
	}

	public ProcessingConfiguration<T, R> withRetries(int numberOfRetries) {
		this.numberOfRetries = numberOfRetries;
		return this;
	}

	public ProcessingConfiguration<T, R> onErrorContinue() {
		this.onErrorPolicy = OnErrorPolicy.CONTINUE;
		return this;
	}

	public ProcessingConfiguration<T, R> onErrorStop() {
		this.onErrorPolicy = OnErrorPolicy.STOP;
		return this;
	}

	public ProcessingConfiguration(List<T> data) {
		this.data = List.copyOf(data);
	}
	
	public ProcessingResult<R> process(Function<List<T>, List<R>> processor, Consumer<Double> reportProgress, BooleanSupplier cancellation) throws ProcessingCancelledException {
		
		List<R> result = new ArrayList<R>();
		List<Exception> errors = new ArrayList<Exception>();

		int totalSize = data.size();
		for (int i = 0; i < totalSize; i += batchSize) {
			if (cancellation.getAsBoolean()) {
				throw new ProcessingCancelledException();
			}
			int thisBatchSize = Math.min(batchSize, totalSize - i);
			List<T> inputBatch = new ArrayList<T>(data.subList(i, i + thisBatchSize));
			try {
				result.addAll(processBatch(processor, inputBatch));
				reportProgress.accept((double) (i + thisBatchSize) / totalSize);
			} catch (Exception ex) {
				reportProgress.accept((double) (i + thisBatchSize) / totalSize);
				if (onErrorPolicy == OnErrorPolicy.CONTINUE) {
					errors.add(ex);
				} else {
					return ProcessingResult.failure(List.of(ex));
				}
			}
		}
		
		if (errors.isEmpty()) {
			return ProcessingResult.success(result);
		} else {
			return ProcessingResult.processedWithErrors(result, errors);			
		}
	}
	
	public List<R> processBatch(Function<List<T>, List<R>> processor, List<T> inputBatch) throws Exception {
		int numberOfAttempts = 0;
		Exception lastException = null;
		while (numberOfAttempts <= numberOfRetries) {
			try {
				List<R> processedBatch = processor.apply(inputBatch);
				return processedBatch;
			} catch (Exception ex) {
				lastException = ex;
				numberOfAttempts++;
			}
		}
		
		throw lastException;
	}
}
