package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Transaction extends AppCompatActivity implements View.OnClickListener{

    private TextView payMeth,vNum;
    private ImageView methImg,buttonBack;
    private String nPayMeth,nVNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        payMeth = (TextView) findViewById(R.id.method_txt);
        vNum = (TextView) findViewById(R.id.vnum);
        methImg = (ImageView) findViewById(R.id.method_img);
        buttonBack = (ImageView) findViewById(R.id.buttonBackTransaction);
        buttonBack.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        nPayMeth = bundle.getString("payMethod");
        nVNum = bundle.getString("virtualNum");

        int nMethImg = bundle.getInt("methodImg");
        payMeth.setText(nPayMeth);
        vNum.setText(nVNum);
        methImg.setImageResource(nMethImg);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        onBackPressed();
    }

    public void mainmenu(View view) {
        Intent intent = new Intent(Transaction.this, MainActivity.class);
        startActivity(intent);
    }
}