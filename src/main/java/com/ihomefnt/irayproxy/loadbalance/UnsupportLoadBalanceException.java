package com.ihomefnt.irayproxy.loadbalance;

public class UnsupportLoadBalanceException extends Exception {

	public UnsupportLoadBalanceException() {
		super();
	}

	public UnsupportLoadBalanceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnsupportLoadBalanceException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportLoadBalanceException(String message) {
		super(message);
	}

	public UnsupportLoadBalanceException(Throwable cause) {
		super(cause);
	}

}
