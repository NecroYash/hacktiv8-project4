package com.example.travel;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.travel.adapter.MyRecyclerViewAdapter;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth auth;
    FirebaseFirestore db;
    MaterialDatePicker materialDatePicker;
    ImageView topImageUser;
    AppCompatSpinner dropdownFrom, dropdownTo;
    Button buttonContinueSeats;
    LinearLayout buttonSelectDate;
    TextView selectedDated;
    Calendar calendar;
    String date = "";
    String[] from = { "Bandung", "Yogyakarta", "Solo", "Jakarta", "Surabaya", "Semarang"};
    String[] to = from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        topImageUser = (ImageView) findViewById(R.id.topImage);
        buttonContinueSeats = (Button) findViewById(R.id.buttonContinue);
        buttonSelectDate = (LinearLayout) findViewById(R.id.linearButtonDate);
        selectedDated = (TextView) findViewById(R.id.selectDate);

        dropdownFrom = findViewById(R.id.dropdownFrom);
        dropdownTo = findViewById(R.id.dropdownTo);

        buttonContinueSeats.setOnClickListener(this);

        setAdapter();
        selectDate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        calendar = Calendar.getInstance();
        getImageUser();
    }

    private void setAdapter(){
        ArrayAdapter adapterFrom = new ArrayAdapter(this, android.R.layout.simple_spinner_item, from);
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter adapterTo = new ArrayAdapter(this, android.R.layout.simple_spinner_item, to);
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdownFrom.setAdapter(adapterFrom);
        dropdownTo.setAdapter(adapterTo);
    }

    private void selectDate(){
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Select a Date");
        materialDatePicker = materialDateBuilder.build();

        buttonSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                utc.setTimeInMillis((Long) selection);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy");
                date = format.format(utc.getTime());
                selectedDated.setText(date);

            }
        });
    }

    private void getImageUser(){
        Glide.with(this.getApplicationContext())
                .load(auth.getCurrentUser().getPhotoUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .error(R.drawable.ic_launcher_background)
                .into(topImageUser);

        topImageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {

        if(dropdownFrom.getSelectedItem().toString().equals(dropdownTo.getSelectedItem().toString())){
            Toast.makeText(MainActivity.this, "Cannot same place", Toast.LENGTH_SHORT).show();
            return;
        }else if(date == null || selectedDated.getText().equals("Date")){
            Toast.makeText(MainActivity.this, "Please select your date", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, BusActivity.class);
        intent.putExtra("from", dropdownFrom.getSelectedItem().toString());
        intent.putExtra("to", dropdownTo.getSelectedItem().toString());
        intent.putExtra("date", date);
        startActivity(intent);
    }
}