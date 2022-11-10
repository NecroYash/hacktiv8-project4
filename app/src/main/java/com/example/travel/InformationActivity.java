package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        buttonBack = findViewById(R.id.buttonBackInformation);
        buttonBack.setOnClickListener(this);
    }

    public void whatsapp(View view) {
        int viewID = view.getId();
        String number = null;
        switch (viewID){
            case R.id.wa_Danu:
                number = "62895325393060";
                break;
            case R.id.wa_lucky:
                number = "6287719857757";
                break;
            case R.id.wa_khalivio:
                number = "6281338442260";
                break;
            default:
                break;
        }
        String url = "https://api.whatsapp.com/send?phone="+number;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void github(View view) {
        int viewID = view.getId();
        String name = null;
        switch (viewID){
            case R.id.gitDanu:
                name = "muhxdan";
                break;
            case R.id.gitLucky:
                name = "Rigel9802";
                break;
            case R.id.gitVio:
                name = "NecroYash";
                break;
            default:
                break;
        }
        String url = "https://github.com/"+name;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        onBackPressed();
    }
}