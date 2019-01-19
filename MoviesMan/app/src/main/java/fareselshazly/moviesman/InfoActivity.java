package fareselshazly.moviesman;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

public class InfoActivity extends AppCompatActivity {

    private String story = "";
    private String director = "";
    private String actors = "";
    private String date = "";
    private String rating = "";
    private String image = "";

    private GifDrawable loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        prepareGIF();
        extractIntent();
        setPoster();
        setTextViews();
    }

    private void prepareGIF(){
        loading = null;
        try {
            loading = new GifDrawable( getResources(), R.drawable.loading );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void extractIntent() {
        Intent intent = getIntent();

        image = intent.getStringExtra("image");
        story = intent.getStringExtra("story");
        director = intent.getStringExtra("director");
        actors = intent.getStringExtra("actors");
        date = intent.getStringExtra("date");
        rating = intent.getStringExtra("rating");

    }

    public void setPoster() {
        ImageView img = (ImageView)findViewById(R.id.infoImg);
        Glide.with(this).load(image).apply(new RequestOptions().override(600, 895).placeholder(loading).error(loading)).into(img);
    }

    public void setTextViews(){

        TextView storyV = (TextView)findViewById(R.id.storyView);
        TextView directorV = (TextView)findViewById(R.id.directorView);
        TextView actorsV = (TextView)findViewById(R.id.actorsView);
        TextView dateV = (TextView)findViewById(R.id.dateView);
        TextView ratingV = (TextView)findViewById(R.id.ratingView);

        String storyFinal = "<b>" + "Story : " + "</b>" + story;
        String directorFinal = "<b>" + "Director : " + "</b>" + director;
        String actorsFinal = "<b>" + "Actors : " + "</b>" + actors;
        String dateFinal = "<b>" + "Release Date : " + "</b>" + date;
        String ratingFinal = "<b>" + "IMDB Rating : " + "</b>" + rating + " ";

        storyV.setText(Html.fromHtml(storyFinal));
        directorV.setText(Html.fromHtml(directorFinal));
        actorsV.setText(Html.fromHtml(actorsFinal));
        dateV.setText(Html.fromHtml(dateFinal));
        ratingV.setText(Html.fromHtml(ratingFinal));

    }

}
