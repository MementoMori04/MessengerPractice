package com.example.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    Button signup;
    Button login;
    EditText Email;
    EditText Username;
    EditText Password;
    private FirebaseAuth mAuth;


        @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        signup = findViewById(R.id.signupbutton);
        login = findViewById(R.id.loginbutton);
        Email = findViewById(R.id.editTextEmail);
        Password = findViewById(R.id.editPassword);
        Username = findViewById(R.id.UserName);



        login.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, LogIn.class);
            startActivity(intent);
        });
        signup.setOnClickListener(v -> {
            String email, password, nickname;
            email = String.valueOf(Email.getText());
            password = String.valueOf(Password.getText());
            nickname = String.valueOf(Username.getText());

            if(TextUtils.isEmpty(nickname)){
                Toast.makeText(SignUp.this, "Введите имя пользователя", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(SignUp.this, "Введите электронную почту", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(SignUp.this, "Введите пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Вы успешно зарегисртировались.",
                                    Toast.LENGTH_SHORT).show();

                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users/" + userId);
                            Map<String, Object> userValues = new HashMap<>();
                            userValues.put("nickname", nickname);
                            userRef.setValue(userValues);

                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    });

        });

    }
}