package com.example.travel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travel.adapter.AdapterListBooking;
import com.example.travel.adapter.AdapterListBus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class BusActivity extends AppCompatActivity {

    TextView fromBus, toBus;
    ImageView buttonBack;
    Calendar calendar;
    FirebaseAuth auth;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    AdapterListBus adapter;
    ProgressBar loadingBus;

    ArrayList<String> listDate = new ArrayList<>();
    ArrayList<String> listFrom = new ArrayList<>();
    ArrayList<String> listTo = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> nameBus = new ArrayList<>();
    ArrayList<String> linkBus = new ArrayList<>();
    ArrayList<Double>  price = new ArrayList<>();
    ArrayList<String>  listTotalTime = new ArrayList<>();
    ArrayList<String>  listTotalDate = new ArrayList<>();
    ArrayList<Integer>  longTime = new ArrayList<>();

    String totalDate, totalTime, name, email, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadingBus = (ProgressBar) findViewById(R.id.loadingBusActivity);

        fromBus = (TextView) findViewById(R.id.fromBusList);
        toBus = (TextView) findViewById(R.id.toBusList);
        buttonBack = (ImageView) findViewById(R.id.buttonBackBusActivity);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void setAdapter(){
        recyclerView = findViewById(R.id.rvBus);
        adapter = new AdapterListBus(getApplicationContext(), listFrom, listTo, nameBus, price, time, longTime, listDate, listTotalDate, listTotalTime, linkBus);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(BusActivity.this));
    }


    @SuppressLint("SimpleDateFormat")
    private void getTimeDate(String date, String time, int longTime){
        String pola = "MMM d, yyyy HH:mm";
        Date dateTime = null;
        String showTimeDate;
        Log.i("infoData", "Awal = " + date + " " + time + " " + longTime);
        SimpleDateFormat formatter= new SimpleDateFormat(pola);
        String check = date + " " + time;
//        String check = date + " " + time;
        try {
            dateTime = formatter.parse(check);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        calendar.setTime(dateTime);
        calendar.add(Calendar.HOUR, longTime);

        showTimeDate = formatter.format(calendar.getTime());

        if(showTimeDate.length() == 17){
            totalDate = showTimeDate.substring(0, 11);
            totalTime = showTimeDate.substring(12, 17);
            Log.i("infoString", "Check = " + showTimeDate );
            Log.i("infoData", "Hasil = " + totalDate + " " + totalTime);
        }else{
            totalDate = showTimeDate.substring(0, 12);
            totalTime = showTimeDate.substring(13, 18);
            Log.i("infoString", "Check = " + showTimeDate );
            Log.i("infoData", "Hasil = " + totalDate + " " + totalTime);
        }
    }


    private void getData(){
        Intent intent = getIntent();
        String from = intent.getStringExtra("from");
        String to = intent.getStringExtra("to");
        String date = intent.getStringExtra("date");

        fromBus.setText(from);
        toBus.setText(to);

        loadingBus.setVisibility(View.VISIBLE);
        db.collection("busData")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            price.clear();
                            time.clear();
                            longTime.clear();
                            nameBus.clear();
                            listDate.clear();
                            listTotalDate.clear();
                            listTotalTime.clear();
                            linkBus.clear();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                if(Objects.equals(document.getString("from"), from) && Objects.equals(document.getString("to"), to)){
                                    String dataFrom = document.getString("from");
                                    String dataTo = document.getString("to");
                                    String dataNameBus = document.getString("nameBus");
                                    String dataTime = document.getString("time");
                                    String dataLinkBus = document.getString("linkBus");
                                    double dataPrice = Objects.requireNonNull(document.getLong("price"));
                                    int dataLongTime = Objects.requireNonNull(document.getLong("longTime")).intValue();

                                    getTimeDate(date, dataTime, dataLongTime);

                                    listDate.add(date);
                                    listFrom.add(dataFrom);
                                    listTo.add(dataTo);
                                    nameBus.add(dataNameBus);
                                    price.add(dataPrice);
                                    time.add(dataTime);
                                    longTime.add(dataLongTime);
                                    linkBus.add(dataLinkBus);

                                    listTotalTime.add(totalTime);
                                    listTotalDate.add(totalDate);
                                }
                            }
                            loadingBus.setVisibility(View.GONE);
                            setAdapter();

                        }else{
                            Toast.makeText(getApplicationContext(), "Error, cannot get data. Please check your internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        calendar = Calendar.getInstance();
        getData();
    }
}