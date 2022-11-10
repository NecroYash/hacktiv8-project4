package com.example.travel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TotalSpendingActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore db;

    ProgressBar progressBar;
    TextView totalSpending,customerName;

    ArrayList<String> getTotalSpending = new ArrayList<>();
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
    }


    private void getData(){
        progressBar.setVisibility(View.VISIBLE);

        db.collection("booking")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String price = document.getString("price");
                                getTotalSpending.add(price);
                            }
                            for(int x = 0; x<getTotalSpending.size(); x++){
                                getTotal += Double.parseDouble(getTotalSpending.get(x));
                            }
                            progressBar.setVisibility(View.GONE);
                            totalSpending.setText("Rp"+getTotal+"00,00");
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
}