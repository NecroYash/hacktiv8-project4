package com.example.travel;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.travel.adapter.MyRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class BookATrip extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth auth;
    ImageView buttonBack, busImage;
    TextView getUsername, getUserEmail, getFrom, getTo, getTime, getDate, getTotalTime, getTotalDate, getTotalSeats, getLongTime, getPrice, getBusName;
    ProgressBar loadingImageBus;
    String context, linkBus;
    Button buttonToTicket;
    private static final int CREATE_FILE = 1;

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
        buttonToTicket = (Button) findViewById(R.id.toTicketActivity);
        buttonBack.setOnClickListener(this);
        buttonToTicket.setOnClickListener(this);
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
       switch (view.getId()){
           case R.id.buttonBackBookingList:
               onBackPressed();
               break;
           case R.id.toTicketActivity:
               if(checkPermission()){
                   Intent intent = new Intent(BookATrip.this, TicketActivity.class);
                   intent.putExtra("fromBooking", getFrom.getText());
                   intent.putExtra("nameBus", getBusName.getText());
                   intent.putExtra("toBooking", getTo.getText());
                   intent.putExtra("timeBooking", getTime.getText());
                   intent.putExtra("dateBooking", getDate.getText());
                   intent.putExtra("longTime", getLongTime.getText());
                   intent.putExtra("priceBooking", getPrice.getText());
                   startActivity(intent);
               }else{
                   requestPermission();
               }
       }
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 10);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean checkPermission(){
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "Ticket.pdf");
        startActivityForResult(intent, CREATE_FILE);
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
    }
}