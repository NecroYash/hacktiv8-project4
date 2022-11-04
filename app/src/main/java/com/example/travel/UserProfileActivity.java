package com.example.travel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;

    ConstraintLayout buttonBooking, buttonSpending, buttonInformation, buttonLogout;
    ImageView imageProfile, buttonBack;
    TextView nameProfile, emailProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        auth = FirebaseAuth.getInstance();

        imageProfile = (ImageView) findViewById(R.id.imageProfile);
        nameProfile = (TextView) findViewById(R.id.nameProfile);
        emailProfile = (TextView) findViewById(R.id.emailProfile);

        buttonBack = (ImageView) findViewById(R.id.buttonBackProfile);
        buttonBooking = (ConstraintLayout) findViewById(R.id.buttonBooking);
        buttonSpending = (ConstraintLayout) findViewById(R.id.buttonSpending);
        buttonInformation = (ConstraintLayout) findViewById(R.id.buttonInformation);
        buttonLogout = (ConstraintLayout) findViewById(R.id.buttonLogout);

        buttonBack.setOnClickListener(this);
        buttonBooking.setOnClickListener(this);
        buttonSpending.setOnClickListener(this);
        buttonInformation.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);

        googleSignInClient = GoogleSignIn.getClient(UserProfileActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.buttonBackProfile) :
                onBackPressed();
                break;
            case (R.id.buttonBooking) :
                Toast.makeText(this, "Booking button", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.buttonSpending) :
                Toast.makeText(this, "Total Spending button", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.buttonInformation) :
                Toast.makeText(this, "Information button", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.buttonLogout) :
                dialog();
                break;
        }
    }

    private void showData(){
        nameProfile.setText(auth.getCurrentUser().getDisplayName());
        emailProfile.setText(auth.getCurrentUser().getEmail());

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
                .into(imageProfile);
    }

    private void logout(){
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    auth.signOut();
                    Intent intent = new Intent(UserProfileActivity.this, LoginSignupActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log out").setMessage("You will be returned to the login screen.").setCancelable(false).setPositiveButton("Log out", (DialogInterface.OnClickListener) (dialog, which) -> {
            logout();
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showData();
    }
}