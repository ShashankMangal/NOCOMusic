package com.sharkBytesLab.nocomusic.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkBytesLab.nocomusic.databinding.ActivityRegisterScreenBinding;

public class RegisterScreen extends AppCompatActivity {


    private ActivityRegisterScreenBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private int newUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        try {
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            newUser =1;
        } catch (Exception e) {
            e.printStackTrace();
        }


        binding.countryCode.registerCarrierNumberEditText(binding.paytmNumber);

        binding.referRed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.paytmNumber.getEditableText().toString())) {
                    binding.paytmNumber.setError("Enter Phone Number!");
                    binding.paytmNumber.requestFocus();
                   binding.referRed.setEnabled(false);
                }
                else
                {
                    binding.referRed.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(binding.paytmNumber.getEditableText().toString())) {
                    binding.paytmNumber.setError("Enter Phone Number!");
                    binding.paytmNumber.requestFocus();
                    binding.referRed.setEnabled(false);
                }
                else
                {
                    binding.referRed.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(binding.paytmNumber.getEditableText().toString())) {
                    binding.paytmNumber.setError("Enter Phone Number!");
                    binding.paytmNumber.requestFocus();
                    binding.referRed.setEnabled(false);
                }
               else
                {
                    binding.referRed.setEnabled(true);
                }
            }
        });


        binding.paytmNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {





                firestore.collection("Users").whereEqualTo("paytm",charSequence.toString().replace(" ","")).get().
                        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        Log.d("Document Matched: ", document.getId() + " => " + document.get("uid").toString());

                                        newUser =0;

                                        binding.referRed.setVisibility(View.GONE);


                                    }
                                }else
                                {
                                    newUser = 1;

                                    binding.referRed.setVisibility(View.VISIBLE);
                                }

                            }
                        });




            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.referRed.setEnabled(true);
            }
        });

        binding.registerScreenPolicyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RegisterScreen.this, RegisterPrivacyPolicyScreen.class));
                finish();

            }
        });



        binding.sendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(binding.paytmNumber.getText().toString().trim())) {
                    binding.paytmNumber.setError("Enter Phone Number!");
                    binding.paytmNumber.requestFocus();
                }




                else {




                    String referCheck = binding.referRed.getText().toString();



                    if(referCheck.length()!=0){





                    firestore.collection("Users").whereEqualTo("referCode",referCheck).get().
                            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                            Log.d("Document details: ", document.getId() + " => " + document.get("uid").toString());


                                            try {
                                                String oppositeId = document.getId().toString();
                                                updateReferCoins(oppositeId);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    } else {
                                        Log.d("Error: ", "Wrong Referral Code or No Internet.", task.getException());
                                    }

                                }
                            });





                    }
                    else
                    {

                        String getNo = binding.countryCode.getFullNumberWithPlus().replace(" ","");

                        Intent in = new Intent(RegisterScreen.this, OtpVerifyScreen.class);
                        in.putExtra("phoneNumber", getNo);
                        in.putExtra("newUser",newUser);
                        startActivity(in);
                        Log.d("Verify Number : ",getNo);

                    }



                }
            }
        });



    }

    private void updateReferCoins(String oppositeUserId)
    {
        Log.d("Opposite User Id:",oppositeUserId);
        firestore.collection("Users").document(oppositeUserId).update("coins", FieldValue.increment(50)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Referral Success.", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(RegisterScreen.this, OtpVerifyScreen.class);
                in.putExtra("phoneNumber", "+91"+binding.paytmNumber.getText().toString().trim());
                in.putExtra("newUser",newUser);
                startActivity(in);
                Log.d("Verify Number : ","+91"+ binding.paytmNumber.getText().toString());
            }
        });

    }



}