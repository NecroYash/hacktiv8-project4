package com.example.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginSignupActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout linearButton;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth auth;
    ProgressBar loading;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        auth = FirebaseAuth.getInstance();
        linearButton = (LinearLayout) findViewById(R.id.linear_button_login);
        linearButton.setOnClickListener(this);
        loading = (ProgressBar) findViewById(R.id.loadingLogin);
        createRequest();
    }

    @Override
    public void onClick(View view) {
        loading.setVisibility(View.VISIBLE);
        signin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
//            Toast.makeText(this, "MASUK DI BAGIAN IF", Toast.LENGTH_SHORT).show();
            // masuk sini toast tidak muncul
            if (signInAccountTask.isSuccessful()) {
                Toast.makeText(this, "MASUK BAGIAN TRY", Toast.LENGTH_SHORT).show();
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                    auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loading.setVisibility(View.GONE);
                                startActivity(new Intent(LoginSignupActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            } else {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(LoginSignupActivity.this, "Authentication Failed : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (ApiException e) {
                    Toast.makeText(this, "MASUK BAGIAN CATCH", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this, "MASUK DI ELSE TOAST", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createRequest() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void signin() {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, 100);
    }
}