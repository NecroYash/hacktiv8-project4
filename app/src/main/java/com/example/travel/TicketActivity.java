package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class TicketActivity extends AppCompatActivity {


    TextView fromTicket, toTicket, busTicket, timeTicket, dateTicket, longTicket, priceTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        fromTicket = (TextView) findViewById(R.id.fromTicket);
        toTicket = (TextView) findViewById(R.id.toTicket);
        busTicket = (TextView) findViewById(R.id.busTicket);
        timeTicket = (TextView) findViewById(R.id.timeTicket);
        dateTicket = (TextView) findViewById(R.id.dateTicket);
        longTicket = (TextView) findViewById(R.id.longTicket);
//        priceTicket = (TextView) findViewById(R.id.priceTicket);
    }


    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData(){
        fromTicket.setText(getIntent().getStringExtra("fromBooking"));
        toTicket.setText(getIntent().getStringExtra("toBooking"));
        busTicket.setText(getIntent().getStringExtra("nameBus"));
        timeTicket.setText(getIntent().getStringExtra("timeBooking"));
        dateTicket.setText(getIntent().getStringExtra("dateBooking"));
        longTicket.setText(getIntent().getStringExtra("longTime"));
    }
}