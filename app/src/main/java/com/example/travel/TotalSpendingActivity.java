package com.example.travel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class TotalSpendingActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    FirebaseFirestore db;

    ProgressBar progressBar;
    TextView totalSpending,customerName;
    ImageView buttonBack;

    ArrayList<Double> getTotalSpending = new ArrayList<>();
    double getTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_spending);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.progressBarTotalSpending);
        totalSpending = (TextView) findViewById(R.id.totalSpending);
        customerName = (TextView) findViewById(R.id.customerName);
        buttonBack = findViewById(R.id.buttonBackSpend);
        buttonBack.setOnClickListener(this);
    }


    private void getData(){
        progressBar.setVisibility(View.VISIBLE);

        db.collection("booking").whereEqualTo("uid", auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                double price = document.getLong("price");
                                getTotalSpending.add(price);
                            }
                            for(int x = 0; x<getTotalSpending.size(); x++){
                                getTotal += getTotalSpending.get(x);
                            }
                            progressBar.setVisibility(View.GONE);
                            if(getTotal == 0){
                                totalSpending.setText("Rp0");
                                return;
                            }
                            totalSpending.setText(getPrice(getTotal)+"");
                        }else{
                            Toast.makeText(getApplicationContext(), "Error, cannot get data. Please check your internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showData() {
        customerName.setText("Hi, " + auth.getCurrentUser().getDisplayName());
    }
    @Override
    protected void onStart() {
        super.onStart();
        getData();
        showData();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        onBackPressed();
    }


    private String getPrice(double price){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(price);
    }
}