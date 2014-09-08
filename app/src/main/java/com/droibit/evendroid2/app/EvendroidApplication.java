package com.droibit.evendroid2.app;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;


public class EvendroidApplication extends Application {

	/** {@inheritDoc} */
	@Override
	public void onCreate() {
		super.onCreate();
		ActiveAndroid.initialize(this);
	}
	
	/** {@inheritDoc} */
	@Override
	public void onTerminate() {
		super.onTerminate();
		ActiveAndroid.dispose();
	}
}
