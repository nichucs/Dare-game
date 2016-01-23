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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class Game extends AppCompatActivity {
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
    @Override
    protected void onResume() {
        act.put(1, "Song");
        act.put(2, "Duet");
        act.put(3, "Dialogue");
        act.put(4, "Classical song");
        act.put(5, "Ramp walk");
        act.put(6, "Stay 1 min without laughing");
        act.put(7, "kooooi...");
        act.put(8, "Do whatever you like");
        act.put(9, "Love proposal");

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

        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);

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
                    AlertDialog dialog = new AlertDialog.Builder(Game.this)
                            .setMessage("End of the game. \n Start over?")
                            .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Game.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .create();
                    dialog.show();
                }
            }
        });

        acts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int i = ran.nextInt(9);
                i++;
                if (doset.size() < 9) {
                    if (!doset.contains(i)) {
                        tact.setText(act.get(i).toString());
                        doset.add(i);
                        animate(i);
                    } else {
                        acts.performClick();
                    }
                } else {
                    doset.clear();
                    tact.setText(act.get(i).toString());
                    doset.add(i);
                    animate(i);
                }
            }

            private void animate(int i) {
                Animation anm = AnimationUtils.loadAnimation(getBaseContext(), animats.get(i));
                img.setImageResource(pics.get(i));
                img.setAnimation(anm);
                acts.setEnabled(false);
                whos.setEnabled(true);
            }
        });
    }
}
