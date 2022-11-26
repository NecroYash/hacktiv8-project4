package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailPayment extends AppCompatActivity implements View.OnClickListener{
    private TextView tbca,tmandiri,tindo;
    private String payMethod,virtualNum;
    private ImageView buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_payment);

        tbca = (TextView) findViewById(R.id.bca_txt);
        tmandiri = (TextView) findViewById(R.id.mandiri_txt);
        tindo = (TextView) findViewById(R.id.indomaret_txt);
        buttonBack = (ImageView) findViewById(R.id.buttonBackDetailPayment);
        buttonBack.setOnClickListener(this);
    }

    public void pay(View view) {
        int viewID = view.getId();
        switch (viewID){
            case R.id.bca:
                payMethod = tbca.getText().toString();
                virtualNum = "80777081338442260";

                Intent intent = new Intent(DetailPayment.this, Transaction.class);
                intent.putExtra("payMethod",payMethod);
                intent.putExtra("virtualNum",virtualNum);
                intent.putExtra("methodImg",R.drawable.bca);
                startActivity(intent);
                break;
            case R.id.mandiri:
                payMethod = tmandiri.getText().toString();
                virtualNum = "81222081338442260";

                Intent man = new Intent(DetailPayment.this, Transaction.class);
                man.putExtra("payMethod",payMethod);
                man.putExtra("virtualNum",virtualNum);
                man.putExtra("methodImg",R.drawable.mandiri);
                startActivity(man);
                break;
            case R.id.indomaret:
                payMethod = tindo.getText().toString();
                virtualNum = "83679081338442260";

                Intent indo = new Intent(DetailPayment.this, Transaction.class);
                indo.putExtra("payMethod",payMethod);
                indo.putExtra("virtualNum",virtualNum);
                indo.putExtra("methodImg",R.drawable.inmaret);
                startActivity(indo);
                break;
            default:
                break;
        }
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