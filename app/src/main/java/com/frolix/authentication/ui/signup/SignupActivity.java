package com.frolix.authentication.ui.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.frolix.authentication.MainActivity;
import com.frolix.authentication.R;
import com.frolix.authentication.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText password;
    private EditText passwordVerify;
    FirebaseAuth firebaseAuth;
    DatabaseReference userRef;

    public static final String TAG = SignupActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        passwordVerify = findViewById(R.id.password_verify);
        Button signupButton = findViewById(R.id.signup);

        firebaseAuth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String userPhone = phone.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                String userPasswordVerify = passwordVerify.getText().toString();

                userRef = FirebaseDatabase.getInstance().getReference("users").child(userPhone); // Reference to User Collection -> phoneID in Database

                if(userName.isEmpty()) {
                    name.setError("Name required");
                    name.requestFocus();
                } else if(userEmail.isEmpty()) {
                    email.setError("Email required");
                    email.requestFocus();
                } else if(userPhone.isEmpty()) {
                    phone.setError("Phone number required");
                    phone.requestFocus();
                } else if(userPassword.isEmpty()) {
                    password.setError("Enter password");
                    password.requestFocus();
                } else if(!userEmail.contains("@")) {
                    email.setError("Invalid email");
                    email.requestFocus();
                } else if(userPassword.length() < 8) {
                    password.setError("Password is too short, minimum 8 character required");
                    password.requestFocus();
                } else if(!userPassword.equals(userPasswordVerify)) {
                    passwordVerify.setError("Password mismatch! Re-enter your password.");
                    passwordVerify.requestFocus();
                } else {
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()) { // Checking phone number in database collection for new entry
                                firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(!task.isSuccessful()) {
                                            Toast.makeText(SignupActivity.this.getApplicationContext(), "Signup failed! "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            userRef = FirebaseDatabase.getInstance().getReference("users");
                                            userRef.child(userPhone).setValue(new User(userName, userEmail));

                                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                            onDestroy();
                                        }
                                    }
                                });
                            } else {
                                phone.setError("This number is already taken, try another one.");
                                phone.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d(TAG, databaseError.getMessage());
                        }
                    };
                    userRef.addListenerForSingleValueEvent(eventListener);
                }
            }
        });

    }
}
