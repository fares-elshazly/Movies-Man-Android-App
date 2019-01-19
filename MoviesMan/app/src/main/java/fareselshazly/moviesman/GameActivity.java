package fareselshazly.moviesman;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import pl.droidsonroids.gif.GifDrawable;


public class GameActivity extends AppCompatActivity {

    private ArrayList<String> list = new ArrayList<String>();
    private int i = 0;
    private String movie = "NULL";
    private String holder = "NULL";
    private int wrongs = 0;
    private int equal = 0;

    private String movieName = "";
    private String url = "http://www.omdbapi.com/?t=";
    private String imgUrl = "http://img.omdbapi.com/?apikey=882f9923&i=";
    private String story = "";
    private String id = "";
    private String director = "";
    private String actors = "";
    private String date = "";
    private String rating = "";

    private GifDrawable loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        i = intent.getIntExtra("level", 0);

        prepareGIF();
        readMovies();
        pickMovie();
    }

    private void prepareGIF(){
        loading = null;
        try {
            loading = new GifDrawable( getResources(), R.drawable.loading );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Reading & Picking The Movie !

    public void readMovies() {
        Scanner scan = new Scanner(getResources().openRawResource(R.raw.movies));

        while(scan.hasNextLine()){
            String line = scan.nextLine();
            list.add(line);
        }

        scan.close();
    }

    public void pickMovie(){
        holder = "";
        movie = list.get(i);
        movieName = movie;
        movie = movie.toLowerCase();
        char [] arr = movie.toCharArray();

        for(int j = 0; j < movie.length(); j++){
            if(j > 0) {
                if (Character.isDigit(arr[j]) || arr[j] == '-' || arr[j] == ':' || arr[j] == '\'' || arr[j] == ',' || arr[j] == '.' || arr[j] == '&') {
                    holder += (" " + arr[j]);
                    equal++;
                } else if (arr[j] != ' ') {
                    holder += " _";
                } else {
                    holder += "  ";
                    equal++;
                }
            }
            else {
                if (Character.isDigit(arr[j]) || arr[j] == '-' || arr[j] == ':' || arr[j] == '\'' || arr[j] == ',' || arr[j] == '.' || arr[j] == '&') {
                    holder += arr[j];
                    equal++;
                } else {
                    holder += "_";
                }
            }
        }

        TextView hold = (TextView) findViewById(R.id.tv);
        hold.setText(holder);

        getData();
    }




    // Getting JSON Movies !

    public void getData(){

        movieName = movieName.replaceAll("\\s","%20");
        url += movieName;
        url += "&plot=full&apikey=882f9923";

        Ion.with(this)
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        processData(result);
                    }
                });
    }

    private void processData(String data){
        try{

            if(data != null) {

                JSONObject json = new JSONObject(data);

                story = json.getString("Plot");
                id = json.getString("imdbID");
                director = json.getString("Director");
                actors = json.getString("Actors");
                date = json.getString("Released");
                rating = json.getString("imdbRating");

            }

            setPoster();

        }catch (JSONException e){
            Log.d("JSON", String.valueOf(e));
        }
    }

    public void setPoster() {
        imgUrl += id;
        ImageView img = (ImageView) findViewById(R.id.posterImg);
        try {
            if(id.length() > 2) {
                Glide.with(this).load(imgUrl).apply(new RequestOptions().override(500, 795).placeholder(loading).error(loading)).into(img);
            } else {
                Glide.with(this).load(loading).apply(new RequestOptions().placeholder(R.drawable.placeholder)).into(img);
            }
        } catch (Exception e){
            Log.d("Glide", String.valueOf(e));
        }
     }




    // Updating Activity Controls !

    public void updateHolder(View v){
        boolean check = false;

        Button btn = (Button) findViewById(v.getId());
        btn.setEnabled(false);
        String letter = btn.getText().toString();
        letter = letter.toLowerCase();
        char[] Char = letter.toCharArray();

        char[] movieArray = movie.toCharArray();
        char[] holderArray = holder.toCharArray();

        for(int j = 0; j < holder.length(); j+=2){
            if(movieArray[j/2] == Char[0]){
                holderArray[j] = Char[0];
                check = true;
                equal++;
            }
        }

        if(check) {
            holder = String.valueOf(holderArray);
            holder = holder.toUpperCase();

            TextView hold = (TextView) findViewById(R.id.tv);
            hold.setText(holder);

            if(equal == movie.length()){
                i++;
                win();
            }
        }else {
            wrongs++;
            updateImage();
        }
    }

    public void updateImage(){
        ImageView Image = (ImageView) findViewById(R.id.imageView8);
        switch(wrongs){
            case 0 : Image.setImageResource(R.drawable.s1);
                break;
            case 1 : Image.setImageResource(R.drawable.s2);
                break;
            case 2 : Image.setImageResource(R.drawable.s3);
                break;
            case 3 : Image.setImageResource(R.drawable.s4);
                break;
            case 4 : Image.setImageResource(R.drawable.s5);
                break;
            case 5 : Image.setImageResource(R.drawable.s6);
                break;
            case 6 : Image.setImageResource(R.drawable.s7);
                lose();
                break;
        }
    }

    public void updateLevel(){
        PrintStream output = null;
        try{
            output = new PrintStream (openFileOutput("lvl.txt", MODE_PRIVATE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        output.println(i);
        output.close();
    }




    // Action Methods !

    public void win() {

        updateLevel();

        LinearLayout kb = (LinearLayout)findViewById(R.id.keyboard);
        kb.setVisibility(View.GONE);

        LinearLayout posterLayout = (LinearLayout)findViewById(R.id.poster);
        posterLayout.setVisibility(View.VISIBLE);

        winDialog();

    }

    private void winDialog(){
        Dialog winDialog = new Dialog(this);
        winDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        winDialog.setContentView(R.layout.win_dialog);
        winDialog.setCancelable(false);
        winDialog.setCanceledOnTouchOutside(false);

        ImageButton info = (ImageButton) winDialog.findViewById(R.id.information);
        ImageButton next = (ImageButton) winDialog.findViewById(R.id.next);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoClick();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextOrRestartClick();
                finish();
            }
        });

        winDialog.show();
    }

    private void infoClick(){
        Intent intent = new Intent(this, InfoActivity.class);

        intent.putExtra("image", imgUrl);
        intent.putExtra("story", story);
        intent.putExtra("director", director);
        intent.putExtra("actors", actors);
        intent.putExtra("date", date);
        intent.putExtra("rating", rating);

        startActivity(intent);
    }

    private void nextOrRestartClick(){
        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra("level", i);

        startActivity(intent);
    }

    public void lose(){
        loseDialog();
    }

    private void loseDialog(){
        Dialog loseDialog = new Dialog(this);
        loseDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loseDialog.setContentView(R.layout.lose_dialog);
        loseDialog.setCancelable(false);
        loseDialog.setCanceledOnTouchOutside(false);

        ImageButton back = (ImageButton) loseDialog.findViewById(R.id.back);
        ImageButton restart = (ImageButton) loseDialog.findViewById(R.id.restart);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextOrRestartClick();
                finish();
            }
        });

        loseDialog.show();
    }

    private void backClick(){
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

}
