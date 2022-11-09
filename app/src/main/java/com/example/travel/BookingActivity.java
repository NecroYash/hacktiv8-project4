package com.example.travel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
    ArrayList<String> time = new ArrayList<>();;
    ArrayList<String> seat = new ArrayList<>();;
    ArrayList<String>  price = new ArrayList<>();;
    ArrayList<String>  totalTime = new ArrayList<>();;
    ArrayList<String>  totalDate = new ArrayList<>();;
    ArrayList<String>  longTime = new ArrayList<>();;

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
        adapter = new AdapterListBooking(getApplicationContext(), date, seat, price, time, totalTime, totalDate, longTime);
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

                            for (QueryDocumentSnapshot document : task.getResult()){
                                String dataDate = document.getString("date");
                                String dataTime = document.getString("time");
                                String dataSeat = document.getString("seats");
                                String dataPrice = document.getString("price");
                                String dataTotalTime = document.getString("totalTime");
                                String dataTotalDate = document.getString("totalDate");
                                String dataLongTime = document.getString("longTime");
                                date.add(dataDate);
                                seat.add(dataSeat);
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

    @Override
    public void onClick(View view) {
        onBackPressed();
    }
}