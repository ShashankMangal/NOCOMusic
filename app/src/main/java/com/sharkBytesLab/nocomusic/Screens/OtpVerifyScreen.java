package com.sharkBytesLab.nocomusic.Screens;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkBytesLab.nocomusic.MainActivity;
import com.sharkBytesLab.nocomusic.R;
import com.sharkBytesLab.nocomusic.databinding.ActivityOtpVerifyScreenBinding;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class OtpVerifyScreen extends AppCompatActivity {

    private ActivityOtpVerifyScreenBinding binding;

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private static final String TAG = "MAIN_TAG";
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private String value;
    private int newUser;
    private String lastPhoneNumber, paytm;
    private Query query;
    private int newCoins = 20;
    private  Thread thread;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityOtpVerifyScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("phoneNumber");

            newUser = extras.getInt("newUser");

        }

        if(newUser == 1)
        {
            newCoins = 50;
        }

        lastPhoneNumber = value.substring(value.length()-5);
        paytm = value.substring(value.length()-10);



        checkOtpAutomatically();

        binding.phoneVerifyText.setText("We have sent an OTP on \n" + value.toString());


        Runnable runnable = new Runnable() {
            @Override
            public void run() {

        try{



        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
            {

                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getApplicationContext(), "Error : "+ e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(s, forceResendingToken);

                Log.d("OnCodeSent : ", s);

                mVerificationId = s;
                forceResendingToken = token;

                Toast.makeText(getApplicationContext(), "Verification code sent.", Toast.LENGTH_SHORT).show();


            }
        };


        try {
            startPhoneNumberVerification(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resendVerificationCode(value, forceResendingToken);
                Toast.makeText(getApplicationContext(), "Resending verification code.", Toast.LENGTH_SHORT).show();
            }
        });


        moveOtpPosition();




        binding.VerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






                if (TextUtils.isEmpty(binding.inputOtp1.getText().toString().trim())) {

                    binding.inputOtp1.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);


                }
                else if(TextUtils.isEmpty(binding.inputOtp2.getText().toString().trim())) {


                    binding.inputOtp2.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
                else if(TextUtils.isEmpty(binding.inputOtp3.getText().toString().trim())) {


                    binding.inputOtp3.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
                else if(TextUtils.isEmpty(binding.inputOtp4.getText().toString().trim())) {


                    binding.inputOtp4.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
                else if(TextUtils.isEmpty(binding.inputOtp5.getText().toString().trim())) {


                    binding.inputOtp5.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
                else if(TextUtils.isEmpty(binding.inputOtp6.getText().toString().trim())) {


                    binding.inputOtp6.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }
                else
                {


                    String completeOtp = binding.inputOtp1.getText().toString() + binding.inputOtp2.getText().toString() + binding.inputOtp3.getText().toString() +
                            binding.inputOtp4.getText().toString() + binding.inputOtp5.getText().toString() + binding.inputOtp6.getText().toString();

                    try {
                        verifyPhoneNumberWithCode(mVerificationId, completeOtp);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(OtpVerifyScreen.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });

        }catch(Exception e){

            Toast.makeText(OtpVerifyScreen.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }



            }
        };
        thread = new Thread(runnable);
        thread.start();


    }

    private void moveOtpPosition()
    {

        binding.inputOtp1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!charSequence.toString().trim().isEmpty())
                {
                    binding.inputOtp2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.length()>0)
                {
                    binding.inputOtp2.requestFocus();
                }

            }
        });
        binding.inputOtp2.addTextChangedListener(new TextWatcher() {
            int previous_length = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previous_length = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(previous_length>charSequence.length())
                {
                    binding.inputOtp1.requestFocus();
                }

                else if(!charSequence.toString().trim().isEmpty())
                {
                    binding.inputOtp3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.length()>0)
                {
                    binding.inputOtp3.requestFocus();
                }

            }
        });
        binding.inputOtp3.addTextChangedListener(new TextWatcher() {
            int previous_length = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previous_length = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(previous_length>charSequence.length())
                {
                    binding.inputOtp2.requestFocus();
                }
                else if(!charSequence.toString().trim().isEmpty())
                {
                    binding.inputOtp4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.length()>0)
                {
                    binding.inputOtp4.requestFocus();
                }
            }
        });
        binding.inputOtp4.addTextChangedListener(new TextWatcher() {
            int previous_length = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previous_length = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(previous_length>charSequence.length())
                {
                    binding.inputOtp3.requestFocus();
                }
                else if(!charSequence.toString().trim().isEmpty())
                {
                    binding.inputOtp5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.length()>0)
                {
                    binding.inputOtp5.requestFocus();
                }
            }
        });
        binding.inputOtp5.addTextChangedListener(new TextWatcher() {
            int previous_length = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previous_length = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(previous_length>charSequence.length())
                {
                    binding.inputOtp4.requestFocus();
                }
                else if(!charSequence.toString().trim().isEmpty())
                {
                    binding.inputOtp6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>0)
                {
                    binding.inputOtp6.requestFocus();
                }
                else if(editable.length()==0)
                {
                    binding.inputOtp4.requestFocus();
                }
            }
        });

        binding.inputOtp6.addTextChangedListener(new TextWatcher() {
            int previous_length = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previous_length = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(previous_length>charSequence.length())
                {
                    binding.inputOtp5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
               if(editable.length()==0)
                {
                    binding.inputOtp5.requestFocus();
                }
            }
        });

    }

    private void checkOtpAutomatically()
    {

        Dialog dialog;
        dialog = new Dialog(OtpVerifyScreen.this);
        dialog.setContentView(R.layout.otp_auto);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.otp_dialog_bg));


        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.show();

        Button type_manually = dialog.findViewById(R.id.type_manually);

        type_manually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             dialog.dismiss();


            }
        });

    }


    private void startPhoneNumberVerification(String phone)
    {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth).setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this).setCallbacks(mCallbacks).build();

        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken token)
    {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth).setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this).setCallbacks(mCallbacks).
                        setForceResendingToken(token).build();

        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private void verifyPhoneNumberWithCode(String verificationId, String code)
    {


        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);



    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {

        firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                updateUi(user);



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Toast Error : ",e.getMessage());
                Log.i("Toast Error : ",e.getMessage());
            }
        });




    }

    private void  updateUi(FirebaseUser user)
    {
        String refer1 = value.substring(value.length()-3);
        String uid = user.getUid();
        String refer2 = uid.substring(uid.length()-3);
        String referCode = refer1+refer2;

        HashMap<String, Object> map = new HashMap<>();
        map.put("name","user"+lastPhoneNumber);
        map.put("coins",newCoins);
        map.put("paytm",paytm);
        map.put("uid",user.getUid());
        map.put("referCode",referCode);


        query = firestore.collection("Users").whereEqualTo("paytm", paytm);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
            {

                if(!value.isEmpty())
                {

                    Intent intent = new Intent(OtpVerifyScreen.this, MainActivity.class);
                    intent.putExtra("frag",3);
                    Log.d("Fragment value Otp : " , String.valueOf(3));
                    startActivity(intent);
                    finish();

                }
                else
                {
                    firestore.collection("Users").document(user.getUid()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(getApplicationContext(), "Welcome : user" +lastPhoneNumber , Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(OtpVerifyScreen.this, MainActivity.class);
                            intent.putExtra("frag",3);
                            startActivity(intent);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }

            }
        });







    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(OtpVerifyScreen.this, RegisterScreen.class));
        thread.interrupt();
        finish();
    }
}