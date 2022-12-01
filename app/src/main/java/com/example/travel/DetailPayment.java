package com.example.travel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailPayment extends AppCompatActivity implements View.OnClickListener{
    private TextView tbca,tmandiri,tindo;
    private String payMethod,virtualNum;
    private ImageView buttonBack;

    TextView nameUser, busName, seatsU, fromU, toU, timeU, dateU, totalDateU, totalTimeU, priceU, longTimeU;

    String name, email, from, to, date, time, seats, totalTime, totalDate, uid, nameBus, linkBus;
    int longTime;
    Double price;
    String pos;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_payment);

        db = FirebaseFirestore.getInstance();

        tbca = (TextView) findViewById(R.id.bca_txt);
        tmandiri = (TextView) findViewById(R.id.mandiri_txt);
        tindo = (TextView) findViewById(R.id.indomaret_txt);
        buttonBack = (ImageView) findViewById(R.id.buttonBackDetailPayment);
        buttonBack.setOnClickListener(this);

        nameUser = (TextView) findViewById(R.id.detailPaymentName);
        busName = (TextView) findViewById(R.id.detailPaymentBusName);
        seatsU = (TextView) findViewById(R.id.detailPaymentBusSeats);
        timeU = (TextView) findViewById(R.id.detailPaymentTime);
        fromU = (TextView) findViewById(R.id.detailPaymentFrom);
        toU = (TextView) findViewById(R.id.detailPaymentTo);
        dateU = (TextView) findViewById(R.id.detailPaymentDate);
        totalTimeU = (TextView) findViewById(R.id.detailPaymentTotalTime);
        totalDateU = (TextView) findViewById(R.id.detailPaymentTotalDate);
        priceU = (TextView) findViewById(R.id.detailPaymentPrice);
        longTimeU = (TextView) findViewById(R.id.detailPaymentLong);
    }

    public void pay(View view) {
        int viewID = view.getId();
        switch (viewID){
            case R.id.bca:
                payMethod = tbca.getText().toString();
                virtualNum = "80777081338442260";
                Intent intent = new Intent(DetailPayment.this, Transaction.class);
                addBooking();
                intent.putExtra("payMethod",payMethod);
                intent.putExtra("virtualNum",virtualNum);
                intent.putExtra("methodImg",R.drawable.bca);
                intent.putExtra("price", price);
                startActivity(intent);
                break;
            case R.id.mandiri:
                payMethod = tmandiri.getText().toString();
                virtualNum = "81222081338442260";

                Intent man = new Intent(DetailPayment.this, Transaction.class);
                addBooking();
                man.putExtra("payMethod",payMethod);
                man.putExtra("virtualNum",virtualNum);
                man.putExtra("methodImg",R.drawable.mandiri);
                man.putExtra("price", price);
                startActivity(man);
                break;
            case R.id.indomaret:
                payMethod = tindo.getText().toString();
                virtualNum = "83679081338442260";
                Intent indo = new Intent(DetailPayment.this, Transaction.class);
                addBooking();
                indo.putExtra("payMethod",payMethod);
                indo.putExtra("virtualNum",virtualNum);
                indo.putExtra("methodImg",R.drawable.inmaret);
                indo.putExtra("price", price);
                startActivity(indo);
                break;
            default:
                break;
        }
    }


    public void addBooking(){
        Map<String, Object> item = new HashMap<>();
        item.put("uid", uid);
        item.put("name", name);
        item.put("email", email);
        item.put("from", from);
        item.put("to", to);
        item.put("date", date);
        item.put("time", time);
        item.put("price", price);
        item.put("longTime", longTime);
        item.put("seats", seats);
        item.put("position", pos);
        item.put("payment", payMethod);
        item.put("totalTime", totalTime);
        item.put("totalDate", totalDate);
        item.put("nameBus", nameBus);
        item.put("linkBus", linkBus);

        db.collection("booking")
            .add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                }
            }). addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
    }

    private void getData(){
        uid = getIntent().getStringExtra("uid");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        date = getIntent().getStringExtra("date");
        seats = getIntent().getStringExtra("seats");
        time = getIntent().getStringExtra("time");
        price = getIntent().getDoubleExtra("price", 0);
        longTime = getIntent().getIntExtra("longTime", 0);
        pos = getIntent().getStringExtra("position");
        totalTime = getIntent().getStringExtra("totalTime");
        totalDate = getIntent().getStringExtra("totalDate");
        nameBus = getIntent().getStringExtra("nameBus");
        linkBus = getIntent().getStringExtra("linkBus");


        nameUser.setText(name);
        busName.setText(nameBus);
        seatsU.setText(seats);
        fromU.setText(from);
        toU.setText(to);
        timeU.setText(time);
        dateU.setText(date);
        totalDateU.setText(totalDate);
        totalTimeU.setText(totalTime);
        priceU.setText(String.valueOf(getPrice(price)));
        longTimeU.setText(longTime+"H");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DetailPayment.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private String getPrice(double price){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(price);
    }
}