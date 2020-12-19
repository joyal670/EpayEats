package com.epayeats.epayeatsuser.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsuser.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Login_Activity extends AppCompatActivity
{

    public ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;

    Button btnGenerateOtp, btnSignIn;
    EditText phoneNumber, Otp;
    TextView timer;

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    ImageButton google_sign_buton;
    private FirebaseAuth mAuth;

    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    private String verificationCodeSent;
    String getPhoneNumber, getOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loging...");



        mAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        btnGenerateOtp = findViewById(R.id.btn_generate_otp);
        btnSignIn = findViewById(R.id.btn_sign_in);
        google_sign_buton = findViewById(R.id.google_sign_buton);

        phoneNumber = findViewById(R.id.phoneEditText);
        Otp = findViewById(R.id.otpEditText);
        timer = findViewById(R.id.timer);

        firebaseLogin();

        btnGenerateOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    String phone = phoneNumber.getText().toString();

                    if (phone == null || phone.trim().isEmpty()) {
                        phoneNumber.setError("Provide Phone Number");
                        return;
                    }

                    getPhoneNumber = "+91" + phone;
                    btnSignIn.setVisibility(View.VISIBLE);
                    Otp.setVisibility(View.VISIBLE);

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            getPhoneNumber,
                            60,
                            TimeUnit.SECONDS,
                            Login_Activity.this,
                            callbacks

                    );

                    startTimer(60 * 1000, 1000);
                    btnGenerateOtp.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    getOtp = Otp.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeSent, getOtp);

                    SignInWithPhoneNumber(credential);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        google_sign_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                signIn();
            }
        });

        createRequest();

    }

    private void loginFn(String email, String password)
    {
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if (task.isSuccessful())
            {
                String userId = task.getResult().getUser().getUid();
                String type1 = "local_admin";
                String type2 = "restaurant";
                String type3 = "delivery_boy";

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user_data").child(userId);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if((type1.equals(snapshot.child("type").getValue().toString()) ) || (type2.equals(snapshot.child("type").getValue().toString())) || (type3.equals(snapshot.child("type").getValue().toString())) )
                        {
                            progressDialog.dismiss();
                            Toast.makeText(Login_Activity.this, "This email is registered as other main account, please use different a E-mail address", Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            progressDialog.dismiss();

                            sharedPreferences = getSharedPreferences("data", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userid", task.getResult().getUser().getUid());
                            editor.putBoolean("login_status", true);
                            editor.apply();

                            Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(Login_Activity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
            else {
                progressDialog.dismiss();
                Toast.makeText(Login_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Login_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Toast.makeText(this, ""+account.getId(), Toast.LENGTH_SHORT).show();
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken)
    {
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            final String key = task.getResult().getUser().getUid();
                            DatabaseReference reference1;
                            reference1 = FirebaseDatabase.getInstance().getReference("user_data").child(key);

                            FirebaseUser user = mAuth.getCurrentUser();

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("userId", key);
                            hashMap.put("userName", user.getDisplayName());
                            hashMap.put("userEmail", user.getEmail());
                            hashMap.put("type", "user");

                            SharedPreferences sharedPreferences;
                            sharedPreferences = getSharedPreferences("data", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userid", key);
                            editor.putString("useremail", task.getResult().getUser().getEmail());
                            editor.putBoolean("login_status", true);
                            editor.putString("username", task.getResult().getUser().getDisplayName());
                            editor.putString("displayimg", String.valueOf(task.getResult().getUser().getPhotoUrl()));
                            editor.apply();

                            reference1.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(Login_Activity.this, "Logined as "+user.getEmail(), Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login_Activity.this, WelcomeActivity.class));
                                        finish();
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(Login_Activity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(Login_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Login_Activity.this, "Failed"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void SignInWithPhoneNumber(PhoneAuthCredential credential) {
        try {
            progressDialog.show();
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                final String key = task.getResult().getUser().getUid();

                                DatabaseReference reference1;
                                reference1 = FirebaseDatabase.getInstance().getReference("user_data").child(key);

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("userId", key);
                                hashMap.put("userName", phoneNumber.getText().toString());
                                hashMap.put("userEmail", phoneNumber.getText().toString());
                                hashMap.put("type", "user");

                                SharedPreferences sharedPreferences;
                                sharedPreferences = getSharedPreferences("data", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userid", key);
                                editor.putString("useremail", phoneNumber.getText().toString());
                                editor.putBoolean("login_status", true);
                                editor.putString("displayimg", "");
                                editor.putString("username", task.getResult().getUser().getDisplayName());
                                editor.apply();

                                reference1.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            progressDialog.dismiss();
                                            startActivity(new Intent(Login_Activity.this, WelcomeActivity.class));
                                            finish();
                                        }
                                        else
                                        {
                                            progressDialog.dismiss();
                                            Toast.makeText(Login_Activity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(Login_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Login_Activity.this, "Incorrect OTP", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimer(final long finish, final long tick) {
        try {

            timer.setVisibility(View.VISIBLE);
            CountDownTimer countDownTimer;

            countDownTimer = new CountDownTimer(finish, tick) {
                @Override
                public void onTick(long l) {
                    long remindSec = l / 1000;
                    timer.setText("Retry after " + (remindSec / 60) + ":" + (remindSec % 60));
                }

                @Override
                public void onFinish() {
                    btnGenerateOtp.setText("Re-generate OTP");
                    btnGenerateOtp.setVisibility(View.VISIBLE);
                    timer.setVisibility(View.INVISIBLE);
                    cancel();
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void firebaseLogin() {
        try {
            auth = FirebaseAuth.getInstance();
            callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(Login_Activity.this, "Verification Failed" + e, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationCodeSent = s;
                    Toast.makeText(Login_Activity.this, "OTP Sent Successfully", Toast.LENGTH_LONG).show();

                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createRequest()
    {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

}