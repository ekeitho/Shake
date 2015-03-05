package com.ekeitho.shake;

import android.app.Application;

import com.parse.Parse;

public class ShakeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /* Enable Local Datastore */
        Parse.enableLocalDatastore(this);

        // Required - Initialize the Parse SDK
        Parse.initialize(this, getString(R.string.parse_app_id),
                getString(R.string.parse_client_key));

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

    }

}
