package fareselshazly.moviesman;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private int i = 0;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-1721739157362964/2142006208");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1721739157362964/2142006208");

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        getLevel();
    }

    public void onResume() {
        super.onResume();
        getLevel();
    }

    public boolean fileExist(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    public void getLevel(){

        TextView tv = (TextView) findViewById(R.id.myImageViewText);
        Scanner scan = null;
        String line = "";

        if(fileExist("lvl.txt")) {

            try {
                scan = new Scanner(openFileInput("lvl.txt"));
                while (scan.hasNextLine()) {
                    line = String.valueOf(scan.nextLine());
                }
                i = Integer.parseInt(line);
                tv.setText("Lv. " + i);
            } catch (Exception e) {
                e.printStackTrace();
            }

            scan.close();

        }else {
            tv.setText("Lv. " + 0);
        }
    }

    public void PlayClick(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("level", i);
        startActivity(intent);
        ShowAds();
    }

    public void CreditsClick(View view) {
        Intent intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);
        ShowAds();
    }

    public void ExitClick(View view) {
        finish();
    }

    private void ShowAds(){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }
}

