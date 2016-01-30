package cs.nizam.daregame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class Game extends AppCompatActivity {

    InterstitialAd mInterstitialAd;
    HashMap<Integer, String> act= new HashMap<>();
    HashMap<Integer, Integer> animats= new HashMap<>();
    HashMap<Integer, Integer> pics= new HashMap<>();
    Set<Integer> whoset= new TreeSet<>();
    Set<Integer> doset= new TreeSet<>();
    //	Iterator<Integer>whoit;
    int num,start=0;
    Button whos,acts;
    TextView twho,tact;
    ImageView img;
    RelativeLayout rl;
    Random ran=new Random();
    private DatabaseHandler db;
    private int actionSize;
    private boolean reload;

    @Override
    protected void onResume() {
        List<String> actions = db.getAllActions();
        actionSize = actions.size();
        for (int i = 0; i < actions.size(); i++) {
            String action = actions.get(i);
            act.put(i+1,action);
        }
        animats.put(1, R.anim.anim);
        animats.put(2, R.anim.xmas1);
        animats.put(3, R.anim.xmas2);
        animats.put(4, R.anim.xmas3);
        animats.put(5, R.anim.xmas4);
        animats.put(6, R.anim.xmas5);
        animats.put(7, R.anim.xmas6);
        animats.put(8, R.anim.xmas7);
        animats.put(9, R.anim.xmas3);

        pics.put(1, R.drawable.imag5654es);
        pics.put(2, R.drawable.duet);
        pics.put(3, R.drawable.cry);
        pics.put(4, R.drawable.idi);
        pics.put(5, R.drawable.next);
        pics.put(6, R.drawable.half);
        pics.put(7, R.drawable.kooi);
        pics.put(8, R.drawable.todo);
        pics.put(9, R.drawable.idi);
        start=1;

        super.onResume();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        db = new DatabaseHandler(this);

        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-6021641109787388/1105750152");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                beginPlayingGame();
            }
        });

        requestNewInterstitial();

        whos=(Button) findViewById(R.id.btnwho);
        acts=(Button) findViewById(R.id.btnact);
        twho=(TextView) findViewById(R.id.who);
        tact=(TextView) findViewById(R.id.act);
        rl=(RelativeLayout) findViewById(R.id.back);
        img=(ImageView) findViewById(R.id.imageView1);
        acts.setEnabled(false);
        num=Integer.parseInt(getIntent().getExtras().getString("number"));




        whos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (whoset.size() < num) {

                    int i = ran.nextInt(num);
                    i++;
                    if (!whoset.contains(i)) {
                        twho.setText("No." + i);
                        whoset.add(i);
                        acts.setEnabled(true);
                        whos.setEnabled(false);
                    } else
                        whos.performClick();
                } else {
                    AlertDialog dialog = getAlertDialog("End of the game. \n Start over?");
                    dialog.show();
                }
            }
        });

        acts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (act != null && act.size() > 0) {
                    int i = ran.nextInt(actionSize);
                    i++;
                    if (doset.size() < actionSize) {
                        if (!doset.contains(i)) {
                            tact.setText(act.get(i));
                            doset.add(i);
                            animate();
                        } else {
                            acts.performClick();
                        }
                    } else {
                        doset.clear();
                        tact.setText(act.get(i));
                        doset.add(i);
                        animate();
                    }
                } else {
                    AlertDialog dialog = getAlertDialog("Actions Empty. Please add some in settings!");
                    dialog.show();
                }
            }

            private void animate() {
                int i = ran.nextInt(animats.size());
                i++;
                Animation anm = AnimationUtils.loadAnimation(getBaseContext(), animats.get(i));
                img.setImageResource(pics.get(i));
                img.setAnimation(anm);
                acts.setEnabled(false);
                whos.setEnabled(true);
            }
        });
    }

    private void beginPlayingGame() {
        if (reload) {
            Intent intent = new Intent(Game.this, MainActivity.class);
            startActivity(intent);
        }
        finish();
    }

    private AlertDialog getAlertDialog(String message) {
        return new AlertDialog.Builder(Game.this)
                .setMessage(message)
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reload = false;
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            beginPlayingGame();
                        }
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reload = true;
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }else {
                            beginPlayingGame();
                        }
                    }
                })
                .create();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
}
