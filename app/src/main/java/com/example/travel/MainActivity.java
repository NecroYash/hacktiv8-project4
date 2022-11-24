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
    LinearLayout buttonSelectDate, buttonSelectedTime;
    TextView selectedDated, selectedTime;
    Calendar calendar;
    String date, time, totalTime, totalDate, nameBus, linkBus = "";
    String[] from = { "Bandung", "Yogyakarta", "Solo", "Jakarta", "Surabaya", "Semarang"};
    String[] to = from;
    double price = 0;
    int longTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        topImageUser = (ImageView) findViewById(R.id.topImage);
        buttonContinueSeats = (Button) findViewById(R.id.buttonContinue);
        buttonSelectDate = (LinearLayout) findViewById(R.id.linearButtonDate);
        buttonSelectedTime = (LinearLayout) findViewById(R.id.linearButtonTime);
        selectedDated = (TextView) findViewById(R.id.selectDate);
        selectedTime = (TextView) findViewById(R.id.selectTime);

        dropdownFrom = findViewById(R.id.dropdownFrom);
        dropdownTo = findViewById(R.id.dropdownTo);

        buttonContinueSeats.setOnClickListener(this);

        setAdapter();
        selectDate();
        selectTime();
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

    private void selectTime(){
        final Calendar c = Calendar.getInstance();

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        buttonSelectedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                String hrstr = hourOfDay < 10 ? '0' + String.valueOf(hourOfDay) : String.valueOf(hourOfDay);
                                String minstr = minute < 10 ? '0' + String.valueOf(minute) : String.valueOf(minute);
                                time = hrstr + ":" + minstr ;
                                selectedTime.setText(time);
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

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
        }else if(date == null){
            Toast.makeText(MainActivity.this, "Please select your date", Toast.LENGTH_SHORT).show();
            return;
        }else if(time == null){
            Toast.makeText(MainActivity.this, "Please select your time", Toast.LENGTH_SHORT).show();
            return;
        }
        checkPriceAndTime();
        getTimeDate(date, time, longTime);
        Log.i("totalTime", String.valueOf(longTime));
        Intent intent = new Intent(MainActivity.this, SeatsActivity.class);
        intent.putExtra("uid", auth.getCurrentUser().getUid());
        intent.putExtra("name", auth.getCurrentUser().getDisplayName());
        intent.putExtra("email", auth.getCurrentUser().getEmail());
        intent.putExtra("from", dropdownFrom.getSelectedItem().toString());
        intent.putExtra("to", dropdownTo.getSelectedItem().toString());
        intent.putExtra("date", date);
        intent.putExtra("time", time);
        intent.putExtra("price", price);
        intent.putExtra("longTime", longTime);
        intent.putExtra("totalTime", totalTime);
        intent.putExtra("totalDate", totalDate);
        intent.putExtra("nameBus", nameBus);
        intent.putExtra("linkBus", linkBus);

        startActivity(intent);
    }

    private void checkPriceAndTime(){
        String from = dropdownFrom.getSelectedItem().toString();
        String to = dropdownTo.getSelectedItem().toString();

        if(from.equals("Solo") && to.equals("Yogyakarta") || from.equals("Yogyakarta") && to.equals("Solo")){
            price = 25000;
            longTime = 2;
            nameBus = "Sugeng Rahayu";
            linkBus = "https://drive.google.com/uc?id=1vHiuvPY6mtVtFQbkrd_9beiVTcVKIwlr";
        }else if(from.equals("Solo") && to.equals("Surabaya") || from.equals("Surabaya") && to.equals("Solo")){
            price = 90000;
            longTime = 4;
            nameBus = "Sugeng Rahayu";
            linkBus = "https://drive.google.com/uc?id=1vHiuvPY6mtVtFQbkrd_9beiVTcVKIwlr";
        }else if(from.equals("Surabaya") && to.equals("Yogyakarta") || from.equals("Yogyakarta") && to.equals("Surabaya")){
            price = 150000;
            longTime = 5;
            nameBus = "Agra Mas";
            linkBus = "https://drive.google.com/uc?id=1PEpN7Zbmvbc6OVBrATW1Fbj_gwFH4t6p";
        }else if(from.equals("Jakarta") && to.equals("Solo") || from.equals("Solo") && to.equals("Jakarta")){
            price = 165000;
            longTime = 7;
            nameBus = "Agra Mas";
            linkBus = "https://drive.google.com/uc?id=1PEpN7Zbmvbc6OVBrATW1Fbj_gwFH4t6p";
        }else if(from.equals("Jakarta") && to.equals("Yogyakarta") || from.equals("Yogyakarta") && to.equals("Jakarta")){
            price = 180000;
            longTime = 8;
            nameBus = "Eka";
            linkBus = "https://drive.google.com/uc?id=15iv8VOtgrfoFWgWmvXIiHgkKdv3Yh4oL";
        }else if(from.equals("Jakarta") && to.equals("Surabaya") || from.equals("Surabaya") && to.equals("Jakarta")){
            price = 250000;
            longTime = 10;
            nameBus = "Eka";
            linkBus = "https://drive.google.com/uc?id=15iv8VOtgrfoFWgWmvXIiHgkKdv3Yh4oL";
        }else if(from.equals("Bandung") && to.equals("Yogyakarta") || from.equals("Yogyakarta") && to.equals("Bandung")){
            price = 180000;
            longTime = 10;
            nameBus = "Harapan Jaya";
            linkBus = "https://drive.google.com/uc?id=1ouQF4iSvKgR7lYKmMO7XApicwi3pLTMS";
        }else if(from.equals("Bandung") && to.equals("Jakarta") || from.equals("Jakarta") && to.equals("Bandung")){
            price = 80000;
            longTime = 3;
            nameBus = "Harapan Jaya";
            linkBus = "https://drive.google.com/uc?id=1ouQF4iSvKgR7lYKmMO7XApicwi3pLTMS";
        }else if(from.equals("Bandung") && to.equals("Solo") || from.equals("Solo") && to.equals("Bandung")){
            price = 165000;
            longTime = 9;
            nameBus = "Po Haryanto";
            linkBus = "https://drive.google.com/uc?id=1tP4doVWiiRR9faI9UO2t3NRq-TBrLl0n";
        }else if(from.equals("Bandung") && to.equals("Surabaya") || from.equals("Surabaya") && to.equals("Bandung")){
            price = 275000;
            longTime = 9;
            nameBus = "Po Haryanto";
            linkBus = "https://drive.google.com/uc?id=1tP4doVWiiRR9faI9UO2t3NRq-TBrLl0n";
        }else if(from.equals("Bandung") && to.equals("Semarang") || from.equals("Semarang") && to.equals("Bandung")){
            price = 220000;
            longTime = 7;
            nameBus = "Sinar Jaya";
            linkBus = "https://drive.google.com/uc?id=1hEyLwnK9zmJ_vW6q5FuxQQ2KX9LqGy5b";
        }else if(from.equals("Jakarta") && to.equals("Semarang") || from.equals("Semarang") && to.equals("Jakarta")){
            price = 250000;
            longTime = 9;
            nameBus = "Sinar Jaya";
            linkBus = "https://drive.google.com/uc?id=1hEyLwnK9zmJ_vW6q5FuxQQ2KX9LqGy5b";
        }else if(from.equals("Semarang") && to.equals("Yogyakarta") || from.equals("Yogyakarta") && to.equals("Semarang")){
            price = 65000;
            longTime = 3;
            nameBus = "Sinar Jaya";
            linkBus = "https://drive.google.com/uc?id=1hEyLwnK9zmJ_vW6q5FuxQQ2KX9LqGy5b";
        }else if(from.equals("Semarang") && to.equals("Surabaya") || from.equals("Surabaya") && to.equals("Semarang")){
            price = 130000;
            longTime = 9;
            nameBus = "Widji Lestari";
            linkBus = "https://drive.google.com/uc?id=1HeQUyQurfqDv65aRu-9pllJioOcX6Zoh";
        }else if(from.equals("Solo") && to.equals("Semarang") || from.equals("Semarang") && to.equals("Solo")){
            price = 70000;
            longTime = 4;
            nameBus = "Sinar Jaya";
            linkBus = "https://drive.google.com/uc?id=1hEyLwnK9zmJ_vW6q5FuxQQ2KX9LqGy5b";
        }
    }


    @SuppressLint("SimpleDateFormat")
    private void getTimeDate(String date, String time, int longTime){
        String pola = "MMM d, yyyy HH:mm";
        Date dateTime = null;
        String showTimeDate;
        SimpleDateFormat formatter= new SimpleDateFormat(pola);
        String check = date + " " + time;
        try {
            dateTime = formatter.parse(check);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        calendar.setTime(dateTime);
        calendar.add(Calendar.HOUR, 10);

        showTimeDate = formatter.format(calendar.getTime());

        if(showTimeDate.length() == longTime){
            totalDate = showTimeDate.substring(0, 11);
            totalTime = showTimeDate.substring(12, 17);
        }else{
            totalDate = showTimeDate.substring(0, 12);
            totalTime = showTimeDate.substring(13, 18);
        }
    }
}