package com.example.travel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.travel.adapter.MyRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SeatsActivity extends AppCompatActivity implements View.OnClickListener {
    MyRecyclerViewAdapter adapter;
    FirebaseFirestore db;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    ProgressBar seatsLoading;
    TextView getSeat, getPrice;
    Button buttonContinueDetail;
    ImageView buttonBackSeats;
    Calendar calendar;
    String name, email, from, to, date, time, seats, price, longTime, totalTime, totalDate, uid;
    String pos;
    String[] data = {"A", "B", "C", "D", "A", "B", "C", "D", "A", "B", "C", "D", "A", "B", "C", "D", "A", "B", "C", "D", "A", "B", "C", "D", "A", "B", "C", "D", "A", "B", "C", "D", "A", "B", "C", "D", "A", "B", "C", "D", };
    private ArrayList<String> checkSeats = new ArrayList<>();;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seats);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        calendar = Calendar.getInstance();

        seatsLoading = (ProgressBar) findViewById(R.id.seatsLoading);
        getSeat = findViewById(R.id.getSeat);
        getPrice = findViewById(R.id.getPrice);
        buttonBackSeats = (ImageView) findViewById(R.id.buttonBackSeats);
        buttonContinueDetail = findViewById(R.id.buttonContinueDetail);

        buttonContinueDetail.setOnClickListener(this);
        buttonBackSeats.setOnClickListener(this);

        getSeats();
    }

    private void getSeats(){
        seats = getIntent().getStringExtra("data");

        if(seats == null){
            getSeat.setText("seat");
        }else{
            getDataOnrestart();
            Log.i("show", "show price"+ price );
            getSeat.setText(seats);
            getPrice.setText(price);
        }
    }

    public void addBooking(){
        Map<String, Object> item = new HashMap<>();
        item.put("uid", uid);
        item.put("name", name);
        item.put("email", email);
        item.put("from", from);
        item.put("to", to);
        item.put("date", date);
        item.put("time", time);
        item.put("price", price);
        item.put("longTime", longTime);
        item.put("seats", seats);
        item.put("position", pos);
        item.put("totalTime", totalTime);
        item.put("totalDate", totalDate);

        db.collection("booking")
                .add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(SeatsActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                }). addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SeatsActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveDataOnrestart(){
        SharedPreferences sharedPreferences = getSharedPreferences("save", 0);
        SharedPreferences.Editor data = sharedPreferences.edit();
        data.putString("uid", uid);
        data.putString("name", name);
        data.putString("email", email);
        data.putString("from", from);
        data.putString("to", to);
        data.putString("date", date);
        data.putString("time", time);
        data.putString("price", price);
        data.putString("longTime", longTime);
//        data.putString("totalTime", totalTime);
//        data.putString("totalDate", totalDate);

        data.apply();
    }

    @SuppressLint("SimpleDateFormat")
    private  void getDataOnrestart(){
        SharedPreferences sharedPreferences = getSharedPreferences("save", 0);
        uid = sharedPreferences.getString("uid", "");
        name = sharedPreferences.getString("name","");
        email = sharedPreferences.getString("email","");
        from = sharedPreferences.getString("from","");
        to = sharedPreferences.getString("to","");
        date = sharedPreferences.getString("date","");
        time = sharedPreferences.getString("time", "");
        price = sharedPreferences.getString("price", "");
        longTime = sharedPreferences.getString("longTime", "");
//        totalTime = sharedPreferences.getString("totalTime", "");
//        totalDate = sharedPreferences.getString("totalDate", "");
        getTimeDate(date, time, longTime);
        Log.i("timer", time);
        pos = getIntent().getStringExtra("pos");
    }

    private void setAdapter(){
        recyclerView = findViewById(R.id.rvSeats);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new MyRecyclerViewAdapter(this, SeatsActivity.this, data, checkSeats);
        recyclerView.setAdapter(adapter);
    }

    private void getData(){
        seatsLoading.setVisibility(View.VISIBLE);

        db.collection("booking")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String seats = document.getString("position");
                                checkSeats.add(seats);
                            }

                            seatsLoading.setVisibility(View.GONE);
                            setAdapter();
                        }else{
                            Toast.makeText(getApplicationContext(), "Error, cannot get data. Please check your internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonBackSeats:
                onBackPressed();
                break;
            case R.id.buttonContinueDetail:
                if(MyRecyclerViewAdapter.dataSeat == null){
                    Toast.makeText(SeatsActivity.this, "Please select your seats", Toast.LENGTH_SHORT).show();
                    return;
                }
                addBooking();
                Intent intent = new Intent(SeatsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                MyRecyclerViewAdapter.dataSeat = null;
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();

        if(MyRecyclerViewAdapter.dataSeat == null || name == null){
            MyRecyclerViewAdapter.lastIndex = 0;
            uid = getIntent().getStringExtra("uid");
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            from = getIntent().getStringExtra("from");
            to = getIntent().getStringExtra("to");
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");
            price = getIntent().getStringExtra("price");
            longTime = getIntent().getStringExtra("longTime");
//            totalTime = getIntent().getStringExtra("totalTime");
//            totalDate = getIntent().getStringExtra("totalDate");
            getPrice.setText(price);
            saveDataOnrestart();

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyRecyclerViewAdapter.dataSeat = null;
        Intent intent = new Intent(SeatsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    @SuppressLint("SimpleDateFormat")
    private void getTimeDate(String date, String time, String longTime){
        String pola = "MMM d, yyyy HH:mm";
        Date dateTime = null;
        String showTimeDate;
        SimpleDateFormat formatter= new SimpleDateFormat(pola);

        try {
            dateTime = formatter.parse(date + " " + time);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        assert dateTime != null;
        calendar.setTime(dateTime);
        calendar.add(Calendar.HOUR, Integer.parseInt(longTime));

        showTimeDate = formatter.format(calendar.getTime());

        if(showTimeDate.length() == 17){
            totalDate = showTimeDate.substring(0, 11);
            totalTime = showTimeDate.substring(12, 17);
        }else{
            totalDate = showTimeDate.substring(0, 12);
            totalTime = showTimeDate.substring(13, 18);
        }
    }



}