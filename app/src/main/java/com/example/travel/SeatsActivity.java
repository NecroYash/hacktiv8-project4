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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SeatsActivity extends AppCompatActivity implements View.OnClickListener {
    MyRecyclerViewAdapter adapter;
    FirebaseFirestore db;
    FirebaseAuth auth;
    RecyclerView recyclerView;
    ProgressBar seatsLoading;
    TextView getSeat;
    Button buttonContinueDetail;
    ImageView buttonBackSeats;
    String name, email, from, to, date, seats, price, uid;
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

        seatsLoading = (ProgressBar) findViewById(R.id.seatsLoading);
        getSeat = findViewById(R.id.getSeat);
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
        item.put("price", price);
        item.put("seats", seats);
        item.put("position", pos);

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

        data.apply();
    }

    private  void getDataOnrestart(){
        SharedPreferences sharedPreferences = getSharedPreferences("save", 0);
        uid = sharedPreferences.getString("uid", "");
        name = sharedPreferences.getString("name","");
        email = sharedPreferences.getString("email","");
        from = sharedPreferences.getString("from","");
        to = sharedPreferences.getString("to","");
        date = sharedPreferences.getString("date","");
        pos = getIntent().getStringExtra("pos");
        price = "Rp.300.000";
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

                            Log.i("send", String.valueOf(checkSeats));

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
                if(MyRecyclerViewAdapter.dataSeat == null && price == null){
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

            saveDataOnrestart();

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyRecyclerViewAdapter.dataSeat = null;
    }
}