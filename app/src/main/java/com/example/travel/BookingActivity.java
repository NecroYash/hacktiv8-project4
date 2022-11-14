package com.example.travel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travel.adapter.AdapterListBooking;
import com.example.travel.adapter.MyRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Objects;

public class BookingActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth auth;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    AdapterListBooking adapter;
    ProgressBar loadingBookingList;
    ImageView buttonBack;

    ArrayList<String> date = new ArrayList<>();;
    ArrayList<String> from = new ArrayList<>();;
    ArrayList<String> to = new ArrayList<>();;
    ArrayList<String> time = new ArrayList<>();;
    ArrayList<String> seat = new ArrayList<>();;
    ArrayList<String> nameBus = new ArrayList<>();;
    ArrayList<String> linkBus = new ArrayList<>();;
    ArrayList<Double>  price = new ArrayList<>();;
    ArrayList<String>  totalTime = new ArrayList<>();;
    ArrayList<String>  totalDate = new ArrayList<>();;
    ArrayList<Integer>  longTime = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        

        buttonBack = findViewById(R.id.buttonBackBooking);
        
        buttonBack.setOnClickListener(this);

        loadingBookingList = findViewById(R.id.loadingBookingList);

    }

    private void setAdapter(){
        recyclerView = findViewById(R.id.rvBooking);
        adapter = new AdapterListBooking(getApplicationContext(), date, from, to, seat, nameBus, linkBus, price, time, totalTime, totalDate, longTime);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(BookingActivity.this));
    }

    private void getData(){
        loadingBookingList.setVisibility(View.VISIBLE);
        db.collection("booking").whereEqualTo("uid", Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            date.clear();
                            seat.clear();
                            price.clear();
                            time.clear();
                            totalTime.clear();
                            totalDate.clear();
                            longTime.clear();
                            nameBus.clear();
                            linkBus.clear();
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String dataDate = document.getString("date");
                                String dataFrom = document.getString("from");
                                String dataTo = document.getString("to");
                                String dataNameBus = document.getString("nameBus");
                                String dataLinkBus = document.getString("linkBus");
                                String dataTime = document.getString("time");
                                String dataSeat = document.getString("seats");
                                double dataPrice = Objects.requireNonNull(document.getLong("price"));
                                String dataTotalTime = document.getString("totalTime");
                                String dataTotalDate = document.getString("totalDate");
                                int dataLongTime = Objects.requireNonNull(document.getLong("longTime")).intValue();
                                date.add(dataDate);
                                from.add(dataFrom);
                                to.add(dataTo);
                                seat.add(dataSeat);
                                nameBus.add(dataNameBus);
                                linkBus.add(dataLinkBus);
                                price.add(dataPrice);
                                time.add(dataTime);
                                totalTime.add(dataTotalTime);
                                totalDate.add(dataTotalDate);
                                longTime.add(dataLongTime);
                            }
                            loadingBookingList.setVisibility(View.GONE);
                            setAdapter();

                        }else{
                            Toast.makeText(getApplicationContext(), "Error, cannot get data. Please check your internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonBackBooking:
                onBackPressed();
                break;
        }
    }


}