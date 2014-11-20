package com.pbs.apppinsdk;

public class NotInstalledException extends IllegalArgumentException {

	private static final long serialVersionUID = -5260750127478785369L;

	public NotInstalledException() {
		this(null);
	}

	public NotInstalledException(Throwable t) {
		super("Amazon App Pin is not installed on this device. Please install it and try again.", t);
	}
}
