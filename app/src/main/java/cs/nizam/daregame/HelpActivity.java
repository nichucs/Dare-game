package cs.nizam.daregame;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class HelpActivity extends Activity {

    WebView webView;
    TextView version;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        mTracker = AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);

        webView = (WebView) findViewById(R.id.webView);
        version = (TextView) findViewById(R.id.version);

        webView.loadUrl("file:///android_asset/Help.html");

        try {
            version.append(" " + getPackageManager().getPackageInfo(getPackageName(),0).versionName);
            version.append("(" + getPackageManager().getPackageInfo(getPackageName(),0).versionCode + ")");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName(AnalyticsTrackers.HELP);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
