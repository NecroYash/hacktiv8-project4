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
    String date, time;
    String[] from = { "Bandung", "Yogyakarta", "Solo", "Jakarta", "Surabaya", "Semarang"};
    String[] to = from;
    String price, longTime, totalTime, totalDate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar = Calendar.getInstance();
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
            @SuppressLint("SetTextI18n")
            @Override
            public void onPositiveButtonClick(Object selection) {
                date = materialDatePicker.getHeaderText();
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
//        getTimeDate(date, time, longTime);

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
//        intent.putExtra("totalTime", totalTime);
//        intent.putExtra("totalDate", totalDate);
        startActivity(intent);
    }

    private void checkPriceAndTime(){
        String from = dropdownFrom.getSelectedItem().toString();
        String to = dropdownTo.getSelectedItem().toString();

        if(from.equals("Solo") && to.equals("Yogyakarta") || from.equals("Yogyakarta") && to.equals("Solo")){
            price = "25.000";
            longTime = "2";
        }else if(from.equals("Solo") && to.equals("Surabaya") || from.equals("Surabaya") && to.equals("Solo")){
            price = "90.000";
            longTime = "4";
        }else if(from.equals("Surabaya") && to.equals("Yogyakarta") || from.equals("Yogyakarta") && to.equals("Surabaya")){
            price = "150.000";
            longTime = "5";
        }else if(from.equals("Jakarta") && to.equals("Solo") || from.equals("Solo") && to.equals("Jakarta")){
            price = "165.000";
            longTime = "7";
        }else if(from.equals("Jakarta") && to.equals("Yogyakarta") || from.equals("Yogyakarta") && to.equals("Jakarta")){
            price = "180.000";
            longTime = "8";
        }else if(from.equals("Jakarta") && to.equals("Surabaya") || from.equals("Surabaya") && to.equals("Jakarta")){
            price = "250.000";
            longTime = "10";
        }else if(from.equals("Bandung") && to.equals("Yogyakarta") || from.equals("Yogyakarta") && to.equals("Bandung")){
            price = "180.000";
            longTime = "10";
        }else if(from.equals("Bandung") && to.equals("Jakarta") || from.equals("Jakarta") && to.equals("Bandung")){
            price = "80.000";
            longTime = "3";
        }else if(from.equals("Bandung") && to.equals("Solo") || from.equals("Solo") && to.equals("Bandung")){
            price = "165.000";
            longTime = "9";
        }else if(from.equals("Bandung") && to.equals("Surabaya") || from.equals("Surabaya") && to.equals("Bandung")){
            price = "275.000";
            longTime = "9";
        }else if(from.equals("Bandung") && to.equals("Semarang") || from.equals("Semarang") && to.equals("Bandung")){
            price = "220.000";
            longTime = "7";
        }else if(from.equals("Jakarta") && to.equals("Semarang") || from.equals("Semarang") && to.equals("Jakarta")){
            price = "250.000";
            longTime = "9";
        }else if(from.equals("Semarang") && to.equals("Yogyakarta") || from.equals("Yogyakarta") && to.equals("Semarang")){
            price = "65.000";
            longTime = "3";
        }else if(from.equals("Semarang") && to.equals("Surabaya") || from.equals("Surabaya") && to.equals("Semarang")){
            price = "130.000";
            longTime = "9";
        }else if(from.equals("Solo") && to.equals("Semarang") || from.equals("Semarang") && to.equals("Solo")){
            price = "70.000";
            longTime = "4";
        }
    }


//    @SuppressLint("SimpleDateFormat")
//    private void getTimeDate(String date, String time, String longTime){
//        String pola = "MMM d, yyyy HH:mm";
//        Date dateTime = null;
//        String showTimeDate;
//        SimpleDateFormat formatter= new SimpleDateFormat(pola);
//
//        try {
//            dateTime = formatter.parse(date + " " + time);
//        } catch (ParseException ex) {
//            ex.printStackTrace();
//        }
//
//        assert dateTime != null;
//        calendar.setTime(dateTime);
//        calendar.add(Calendar.HOUR, Integer.parseInt(longTime));
//
//        showTimeDate = formatter.format(calendar.getTime());
//
//        if(showTimeDate.length() == 17){
//            totalDate = showTimeDate.substring(0, 11);
//            totalTime = showTimeDate.substring(12, 17);
//        }else{
//            totalDate = showTimeDate.substring(0, 12);
//            totalTime = showTimeDate.substring(13, 18);
//        }
//    }
}