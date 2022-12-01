package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

public class Transaction extends AppCompatActivity implements View.OnClickListener{

    private TextView payMeth,vNum, price;
    private ImageView methImg,buttonBack;
    private String nPayMeth,nVNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        payMeth = (TextView) findViewById(R.id.method_txt);
        vNum = (TextView) findViewById(R.id.vnum);
        methImg = (ImageView) findViewById(R.id.method_img);
        price = (TextView) findViewById(R.id.totalpriceTransaction);
        buttonBack = (ImageView) findViewById(R.id.buttonBackTransaction);
        buttonBack.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        nPayMeth = bundle.getString("payMethod");
        nVNum = bundle.getString("virtualNum");

        int nMethImg = bundle.getInt("methodImg");
        payMeth.setText(nPayMeth);
        vNum.setText(nVNum);
        methImg.setImageResource(nMethImg);
        price.setText(String.valueOf(getPrice(bundle.getDouble("price"))));
    }

    private String getPrice(double price){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(price);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Transaction.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        onBackPressed();
    }

    public void mainmenu(View view) {
        Intent intent = new Intent(Transaction.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}