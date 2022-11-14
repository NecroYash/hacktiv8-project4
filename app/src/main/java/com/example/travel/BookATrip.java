package com.example.travel;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.travel.adapter.MyRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class BookATrip extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth auth;
    ImageView buttonBack, busImage;
    TextView getUsername, getUserEmail, getFrom, getTo, getTime, getDate, getTotalTime, getTotalDate, getTotalSeats, getLongTime, getPrice, getBusName;
    ProgressBar loadingImageBus;
    String context, linkBus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_atrip);
        auth = FirebaseAuth.getInstance();

        busImage = (ImageView) findViewById(R.id.imgBus);
        buttonBack = (ImageView) findViewById(R.id.buttonBackBookingList);
        getUsername = (TextView) findViewById(R.id.getUsernameBooking);
        getUserEmail = (TextView) findViewById(R.id.getUserEmailBooking);
        getBusName = (TextView) findViewById(R.id.namaBus);
        getFrom = (TextView) findViewById(R.id.getFromBooking);
        getTo = (TextView) findViewById(R.id.getToBooking);
        getTime = (TextView) findViewById(R.id.getTimeBooking);
        getDate = (TextView) findViewById(R.id.getDateBooking);
        getTotalTime = (TextView) findViewById(R.id.getTotalTime);
        getTotalDate = (TextView) findViewById(R.id.getTotalDate);
        getTotalSeats = (TextView) findViewById(R.id.getBookingSeats);
        getLongTime = (TextView) findViewById(R.id.getLongTime);
        getPrice = (TextView) findViewById(R.id.getPriceBooking);
        loadingImageBus = (ProgressBar) findViewById(R.id.imageLoadingDetailBooking);

        buttonBack.setOnClickListener(this);
    }

    private void getDataBooking(){
        getUsername.setText(auth.getCurrentUser().getDisplayName());
        getUserEmail.setText(auth.getCurrentUser().getEmail());
        getFrom.setText(getIntent().getStringExtra("fromBooking"));
        getBusName.setText(getIntent().getStringExtra("nameBus"));
        getTo.setText(getIntent().getStringExtra("toBooking"));
        getTime.setText(getIntent().getStringExtra("timeBooking"));
        getDate.setText(getIntent().getStringExtra("dateBooking"));
        getTotalTime.setText(getIntent().getStringExtra("totalTimeBooking"));
        getTotalDate.setText(getIntent().getStringExtra("totalDateBooking"));
        getTotalSeats.setText(getIntent().getStringExtra("seatBooking"));
        getLongTime.setText(getIntent().getStringExtra("longTime"));
        getPrice.setText(getIntent().getStringExtra("priceBooking"));

        context = getIntent().getStringExtra("context");
        linkBus = getIntent().getStringExtra("linkBus");

        getImageUser();
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


    private void getImageUser(){
        loadingImageBus.setVisibility(View.VISIBLE);
        Glide.with(this.getApplicationContext())
                .load(linkBus)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        loadingImageBus.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        loadingImageBus.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(R.drawable.ic_launcher_background)
                .into(busImage);
//
    }
}