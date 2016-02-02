package cs.nizam.daregame;

import android.app.Application;

/**
 * Created by nizamcs on 2/2/16.
 */
public class DareGame extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AnalyticsTrackers.initialize(this);
    }
}
