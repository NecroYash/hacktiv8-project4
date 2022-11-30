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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
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
    String name, email, from, to, date, time, seats, totalTime, totalDate, uid, nameBus, linkBus;
    int longTime;
    Double price;
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
            getSeat.setText(seats);
            getPrice.setText(getPrice(price));
        }
    }

//    public void addBooking(){
//        Map<String, Object> item = new HashMap<>();
//        item.put("uid", uid);
//        item.put("name", name);
//        item.put("email", email);
//        item.put("from", from);
//        item.put("to", to);
//        item.put("date", date);
//        item.put("time", time);
//        item.put("price", price);
//        item.put("longTime", longTime);
//        item.put("seats", seats);
//        item.put("position", pos);
//        item.put("totalTime", totalTime);
//        item.put("totalDate", totalDate);
//        item.put("nameBus", nameBus);
//        item.put("linkBus", linkBus);
//
//        db.collection("booking")
//                .add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Toast.makeText(SeatsActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                    }
//                }). addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(SeatsActivity.this, "Failure", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

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
        data.putString("price", String.valueOf(price));
        data.putInt("longTime", longTime);
        data.putString("totalTime", totalTime);
        data.putString("totalDate", totalDate);
        data.putString("nameBus", nameBus);
        data.putString("linkBus", linkBus);

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
        price = Double.valueOf(sharedPreferences.getString("price", ""));
        longTime = sharedPreferences.getInt("longTime", 0);
        totalTime = sharedPreferences.getString("totalTime", "");
        totalDate = sharedPreferences.getString("totalDate", "");
        nameBus = sharedPreferences.getString("nameBus", "");
        linkBus = sharedPreferences.getString("linkBus", "");
        pos = getIntent().getStringExtra("pos");
    }

    private void setAdapter(){
        recyclerView = findViewById(R.id.rvSeats);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new MyRecyclerViewAdapter(this, SeatsActivity.this, data, checkSeats);
        recyclerView.setAdapter(adapter);
    }

    private void getData(String from, String to){
        seatsLoading.setVisibility(View.VISIBLE);

        db.collection("booking")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
//                            checkSeats.clear();
                            Log.i("show", checkSeats.toString());
                            for (QueryDocumentSnapshot document : task.getResult()){
                                if(document.getString("from").equals(from) && document.getString("to").equals(to) || document.getString("from").equals(to) && document.getString("to").equals(from)){
                                    if(document.getString("nameBus").equals(nameBus) && document.getString("time").equals(time) ){
                                        String seats = document.getString("position");
                                        Log.i("iniSeats", seats);
                                        checkSeats.add(seats);
                                    }
                                }
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
//                addBooking();
//                Intent intent = new Intent(SeatsActivity.this, BookATrip.class);
//                intent.putExtra("dateBooking", date);
//                intent.putExtra("fromBooking", from);
//                intent.putExtra("toBooking", to);
//                intent.putExtra("seatBooking", seats);
//                intent.putExtra("timeBooking", time);
//                intent.putExtra("totalTimeBooking", totalTime);
//                intent.putExtra("totalDateBooking", totalDate);
//                intent.putExtra("longTime", String.valueOf(longTime)+"H");
//                intent.putExtra("priceBooking", getPrice(price));
//                intent.putExtra("nameBus", nameBus);
//                intent.putExtra("linkBus", linkBus);
//                intent.putExtra("context", "notList");
//                Log.i("totalTime", totalTime);
//                startActivity(intent);
                Intent intent = new Intent(SeatsActivity.this, DetailPayment.class);

                intent.putExtra("uid", uid);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("date", date);
                intent.putExtra("from", from);
                intent.putExtra("to", to);
                intent.putExtra("seats", seats);
                intent.putExtra("time", time);
                intent.putExtra("totalTime", totalTime);
                intent.putExtra("totalDate", totalDate);
                intent.putExtra("longTime", longTime);
                intent.putExtra("position", pos);
                intent.putExtra("price", price);
                intent.putExtra("nameBus", nameBus);
                intent.putExtra("linkBus", linkBus);
                Log.i("checkPrice", getPrice(price));
                startActivity(intent);
                MyRecyclerViewAdapter.dataSeat = null;
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(MyRecyclerViewAdapter.dataSeat == null || name == null){
            MyRecyclerViewAdapter.lastIndex = 0;
            uid = auth.getUid();
            name = auth.getCurrentUser().getDisplayName();
            email = auth.getCurrentUser().getEmail();
            from = getIntent().getStringExtra("from");
            to = getIntent().getStringExtra("to");
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");
            price = getIntent().getDoubleExtra("price", 0);
            longTime = getIntent().getIntExtra("longTime", 0);
            totalTime = getIntent().getStringExtra("totalTime");
            totalDate = getIntent().getStringExtra("totalDate");
            nameBus = getIntent().getStringExtra("nameBus");
            linkBus = getIntent().getStringExtra("linkBus");

            getPrice.setText(getPrice(price));
            saveDataOnrestart();

        }
        getData(from, to);
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


    private String getPrice(double price){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(price);
    }
}