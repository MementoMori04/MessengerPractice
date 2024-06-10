package com.example.messenger;



import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int MAX_MASSAGE = 150;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");


    EditText editTextmes;
    Button sendbutt;
    RecyclerView recyclerView;

    //   Button logout;
    ArrayList<String> messeges = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setContentView(R.layout.item_massage);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DataAdapter dataAdapter = new DataAdapter(this, messeges);
        recyclerView.setAdapter(dataAdapter);
//        name = findViewById(R.id.imya);
        editTextmes = findViewById(R.id.txt);
        sendbutt = findViewById(R.id.send);


//        logout.findViewById(R.id.logoutbutton);
        sendbutt.setOnClickListener(v -> {
            String mes = editTextmes.getText().toString();
            if (mes.isEmpty()) {
                return;
            }
            if (mes.length() > MAX_MASSAGE) {
                return;
            }

            String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(); // Получаем UID текущего пользователя

            // Создаем объект, содержащий сообщение и UID
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("message", mes);
            messageMap.put("userId", userId);

            // Добавляем сообщение в базу данных Firebase
            myRef.push().setValue(messageMap);

            editTextmes.setText("");
        });
        myRef.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String s) {
                String msg = snapshot.child("message").getValue(String.class);
                String userId = snapshot.child("userId").getValue(String.class);
                dataAdapter.notifyDataSetChanged();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users/" + userId);

                // Добавляем слушатель для получения данных пользователя
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String username;
                            username = Objects.requireNonNull(snapshot.child("nickname").getValue()).toString();
//                            SpannableString usernameSpannable = new SpannableString(username);
//                            if (username != null) {
//                                usernameSpannable.setSpan(new RelativeSizeSpan(0.5f), 0, usernameSpannable.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            }

                            messeges.add(username + "\n" + msg);
                            dataAdapter.notifyDataSetChanged();
                            recyclerView.smoothScrollToPosition(messeges.size());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Ошибка при получении имени пользователя", error.toException());
                    }
                });
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


