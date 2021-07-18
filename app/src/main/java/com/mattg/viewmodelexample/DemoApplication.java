package com.mattg.viewmodelexample;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import timber.log.Timber;

/**
 * Class to instantiate application level variables/functions
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }
    }
    //method to get application context app wide
    public static Context getStaticContext(){
        return getStaticContext().getApplicationContext();
    }
}

class ReleaseTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return;
        }

        // log your crash to your favourite
        // Sending crash report to Firebase CrashAnalytics

        // FirebaseCrash.report(message);
        // FirebaseCrash.report(new Exception(message));
    }
}
