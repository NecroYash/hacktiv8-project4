package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.TextView;

public class BookingActivity extends AppCompatActivity {

    private TextView comBus, capCity, depCity, arrCity, date, time, confTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        comBus = findViewById(R.id.comBus);
        capCity = findViewById(R.id.capCity);
        depCity = findViewById(R.id.depCity);
        arrCity = findViewById(R.id.arrCity);
        date = findViewById(R.id.txtDate);
        time = findViewById(R.id.txtTime);
        confTicket = findViewById(R.id.confirmTicket);


    }
}