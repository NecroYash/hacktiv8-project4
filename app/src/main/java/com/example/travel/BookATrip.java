package com.example.travel;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class BookATrip extends AppCompatActivity {

    private TextView comBus, rating, curDate, depDate,
    depCity,arrCity, depClock, arrClock, platBus, classBus,
    seatAvailable, ratePrice, totPrice;
    private Button btnBook;
    private CardView btnDetailPict;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_atrip);

        // text view
        comBus = findViewById(R.id.namaBus);
        rating = findViewById(R.id.txtRatings);
        curDate = findViewById(R.id.dateNow);
        depDate = findViewById(R.id.Date);
        depCity = findViewById(R.id.kota_pertama);
        arrCity = findViewById(R.id.kota_kedua);
        depClock = findViewById(R.id.jam_berangkat);
        arrClock = findViewById(R.id.jam_sampai);
        platBus = findViewById(R.id.platBus);
        seatAvailable = findViewById(R.id.seatAvailable);
//        classBus = findViewById(R.id.classBus);
//        ratePrice = findViewById(R.id.RatePrice);
//        totPrice = findViewById(R.id.TotPrice);
//
//        // button
//        btnBook = findViewById(R.id.btnBooking);
        btnDetailPict = findViewById(R.id.btnLihatFoto);

        // image
        img = findViewById(R.id.imgBus);
    }
}