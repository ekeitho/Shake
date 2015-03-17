package com.ekeitho.shake.login;

import android.app.Application;

import com.ekeitho.shake.R;
import android.util.Log;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class ShakeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /* Enable Local Datastore */
        Parse.enableLocalDatastore(this);

        // Required - Initialize the Parse SDK
        Parse.initialize(this, getString(R.string.parse_app_id),
                getString(R.string.parse_client_key));

        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
    }

}
