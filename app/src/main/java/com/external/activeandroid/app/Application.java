package com.external.activeandroid.app;

import com.dmy.farming.api.data.CITY;
import com.dmy.farming.api.data.RECENTCITY;
import com.dmy.farming.protocol.ADDRESS;
import com.external.activeandroid.ActiveAndroid;
import com.external.activeandroid.Configuration;
import com.dmy.farming.api.data.USER;

public class Application extends android.app.Application {
	@Override
	public void onCreate() {
		super.onCreate();
		//initActive();
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		ActiveAndroid.dispose();
	}

	public void initActive() {
		Configuration.Builder configurationBuilder = new Configuration.Builder(this);

		configurationBuilder.addModelClass(CITY.class);
		configurationBuilder.addModelClass(RECENTCITY.class);

		configurationBuilder.addModelClass(ADDRESS.class);
		configurationBuilder.addModelClass(USER.class);

		ActiveAndroid.initialize(configurationBuilder.create());
	}

	public void resetDatabase() {
		ActiveAndroid.dispose();
		initActive();
	}
}