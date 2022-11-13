package com.example.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.travel.adapter.MyRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class BookATrip extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth auth;
    ImageView buttonBack;
    TextView getUsername, getUserEmail, getFrom, getTo, getTime, getDate, getTotalTime, getTotalDate, getTotalSeats, getLongTime, getPrice;

    String context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_atrip);
        auth = FirebaseAuth.getInstance();

        buttonBack = (ImageView) findViewById(R.id.buttonBackBookingList);
        getUsername = (TextView) findViewById(R.id.getUsernameBooking);
        getUserEmail = (TextView) findViewById(R.id.getUserEmailBooking);
        getFrom = (TextView) findViewById(R.id.getFromBooking);
        getTo = (TextView) findViewById(R.id.getToBooking);
        getTime = (TextView) findViewById(R.id.getTimeBooking);
        getDate = (TextView) findViewById(R.id.getDateBooking);
        getTotalTime = (TextView) findViewById(R.id.getTotalTime);
        getTotalDate = (TextView) findViewById(R.id.getTotalDate);
        getTotalSeats = (TextView) findViewById(R.id.getBookingSeats);
        getLongTime = (TextView) findViewById(R.id.getLongTime);
        getPrice = (TextView) findViewById(R.id.getPriceBooking);

        buttonBack.setOnClickListener(this);
    }

    private void getDataBooking(){
        getUsername.setText(auth.getCurrentUser().getDisplayName());
        getUserEmail.setText(auth.getCurrentUser().getEmail());
        getFrom.setText(getIntent().getStringExtra("fromBooking"));
        getTo.setText(getIntent().getStringExtra("toBooking"));
        getTime.setText(getIntent().getStringExtra("timeBooking"));
        getDate.setText(getIntent().getStringExtra("dateBooking"));
        getTotalTime.setText(getIntent().getStringExtra("totalTimeBooking"));
        getTotalDate.setText(getIntent().getStringExtra("totalDateBooking"));
        getTotalSeats.setText(getIntent().getStringExtra("seatBooking"));
        getLongTime.setText(getIntent().getStringExtra("longTime"));
        getPrice.setText(getIntent().getStringExtra("priceBooking"));

        context = getIntent().getStringExtra("context");


    }

    @Override
    protected void onStart() {
        super.onStart();
        getDataBooking();
    }

    @Override
    public void onClick(View view) {
       onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(!context.equals("list")){
            MyRecyclerViewAdapter.dataSeat = null;
            Intent intent = new Intent(BookATrip.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}