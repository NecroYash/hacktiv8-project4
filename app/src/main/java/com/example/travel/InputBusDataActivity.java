package com.example.travel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class InputBusDataActivity extends AppCompatActivity {

    String from, to;
    int longTime, price;
    FirebaseFirestore db;

    Button button;
    EditText nameBus, linkBus, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_bus_data);

        db = FirebaseFirestore.getInstance();

        button = findViewById(R.id.pushBusData);
        nameBus = findViewById(R.id.inputNameBus);
        linkBus = findViewById(R.id.inputLinkBus);
        time = findViewById(R.id.inputTimeBus);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBooking();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        price = getIntent().getIntExtra("price", 0);
        longTime = getIntent().getIntExtra("longTime", 0);
    }

        public void addBooking(){
            Map<String, Object> item = new HashMap<>();
            item.put("from", from);
            item.put("to", to);
            item.put("price", price);
            item.put("longTime", longTime);

            item.put("nameBus", nameBus.getText().toString());
            item.put("linkBus", linkBus.getText().toString());
            item.put("time", time.getText().toString());

            db.collection("busData")
                    .add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(InputBusDataActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                    }). addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(InputBusDataActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
    }
}