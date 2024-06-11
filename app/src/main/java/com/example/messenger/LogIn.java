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

public class LogIn extends AppCompatActivity {
    Button login;
    EditText emailtext;
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
        mAuth = FirebaseAuth.getInstance();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        login = findViewById(R.id.loginbutton);
        emailtext = findViewById(R.id.editTextUserName2);
        Password = findViewById(R.id.editTextPassword2);

        login.setOnClickListener(v -> {
            String email, password;
            email = String.valueOf(emailtext.getText());
            password = String.valueOf(Password.getText());

            if (TextUtils.isEmpty(email)){
                Toast.makeText(LogIn.this, "Введите электронную почту", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)){
                Toast.makeText(LogIn.this, "Введите пароль", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LogIn.this, "Вы успешно вошли.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogIn.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LogIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}