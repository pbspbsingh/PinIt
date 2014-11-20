package com.pbs.apppinsdk;

import android.content.Context;

public interface AmazonAppPin {

	void pinIt() throws NotInstalledException;

	public static class Builder {
		private Builder() {
			// No objects for you
		}

		public static AmazonAppPin getInstance(Context context) {
			return new AmazonAppPinImpl(context);
		}
	}
}
