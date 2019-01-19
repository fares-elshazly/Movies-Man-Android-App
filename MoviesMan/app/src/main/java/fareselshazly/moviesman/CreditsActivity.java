package fareselshazly.moviesman;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CreditsActivity extends AppCompatActivity {

    private Intent Intent = new Intent();

    private static final String KEY_NAME = "Name";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_MESSAGE = "Message";

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference ref = db.getReference("Messages");

    private EditText Name;
    private EditText Email;
    private EditText Message;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        MobileAds.initialize(this, "ca-app-pub-1721739157362964/2142006208");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1721739157362964/2142006208");

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        Name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        Message = findViewById(R.id.message);
    }

    public void SendClick(View v){
        if(CheckForm()) {
            ToDatabase();
        }
    }

    public void FbClick(View v){
        Intent = new Intent (Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Fares.ElShazlyy"));
        startActivity(Intent);
    }

    public void TwitterClick(View v){
        Intent = new Intent (Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Fares_ElShazly"));
        startActivity(Intent);
    }

    public void WhatsappClick(View v){
        String url = "https://api.whatsapp.com/send?phone=201027744970";
        Intent = new Intent(Intent.ACTION_VIEW);
        Intent.setData(Uri.parse(url));
        startActivity(Intent);
    }

    private boolean isEmail(EditText text){
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean isEmpty(EditText text){
        CharSequence txt = text.getText().toString();
        return TextUtils.isEmpty(txt);
    }

    private boolean CheckForm(){
        if(isEmpty(Name)){
            Name.setError("Your Name is Required!");
            return false;
        }

        if(!isEmail(Email)){
            Email.setError("Type a Valid E-mail Address!");
            return false;
        }

        if(isEmpty(Message)){
            Message.setError("Your Message is Required!");
            return false;
        }

        return true;
    }

    private void ToDatabase(){
        String NameStr = Name.getText().toString();
        String EmailStr = Email.getText().toString();
        String MessageStr = Message.getText().toString();

        Map<String, Object> MSG = new HashMap<>();
        MSG.put(KEY_NAME, NameStr);
        MSG.put(KEY_EMAIL, EmailStr);
        MSG.put(KEY_MESSAGE, MessageStr);

        String Key = ref.push().getKey();

        ref.child(Key).setValue(MSG).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ShowDialog();
            }
        });
    }

    private void ShowDialog(){
        Dialog doneDialog = new Dialog(this);
        doneDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        doneDialog.setContentView(R.layout.sent_dialog);
        doneDialog.setCancelable(false);
        doneDialog.setCanceledOnTouchOutside(false);

        ImageButton done = (ImageButton) doneDialog.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAds();
               // doneClick();
                finish();
            }
        });

        doneDialog.show();
    }

    private void doneClick(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
