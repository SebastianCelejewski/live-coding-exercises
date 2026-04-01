package pl.sebcel.livecoding.javastreams.advancedorderservice;

public class MissingExchangeRateException extends RuntimeException {
	
	private static final long serialVersionUID = -2442745432966008961L;

	public MissingExchangeRateException(String missingCurrency) {
		super(missingCurrency);
	}

}
