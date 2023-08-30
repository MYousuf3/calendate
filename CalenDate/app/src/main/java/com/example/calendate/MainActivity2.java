package com.example.calendate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    ListView listView;
    Button button;

    Calendar calendar;

    EditText name,pass,checkPass;

    List<String> calList;
    String calName;
    String calPass;

    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference listRef;
    DatabaseReference passRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().hide();

        listView = findViewById(R.id.list);
        calList = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity2.this, android.R.layout.simple_list_item_activated_1, calList);

        button = findViewById(R.id.button);



        database = FirebaseDatabase.getInstance();
        reference = database.getReference("calendars");
        SharedPreferences preferences = getSharedPreferences("PREFS",0);
        listView.setAdapter(adapter);
        listRef =database.getReference("calendars");
        listRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                calList.clear();
                Iterable<DataSnapshot> rooms = snapshot.getChildren();
                for(DataSnapshot snapshot1:rooms)
                {
                    calList.add(snapshot1.getKey());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity2.this, android.R.layout.simple_list_item_activated_1, calList);
                    listView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                showDialog();

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Dialog dialog = new Dialog(MainActivity2.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog2);

                checkPass = dialog.findViewById(R.id.dialogPass2);
                Button ok2 = dialog.findViewById(R.id.dialogOK2);
                ok2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        passRef = database.getReference("calendars/" +calList.get(i));
                        passRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()) {
                                    Log.d("tag", calList.get(i));
                                    String enteredPass = checkPass.getText().toString();
                                    String password = snapshot.child("pass").getValue(String.class);
                                    Log.d("tag","equaled: " + password + "," +enteredPass);

                                    if(password.equals(enteredPass))
                                    {
                                        Intent intent = new Intent(getBaseContext(),MainActivity3.class);
                                        intent.putExtra("calName",calList.get(i));
                                        startActivity(intent);
                                        dialog.dismiss();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                dialog.show();
            }
        });

    }

    public void showDialog(){
            Dialog dialog = new Dialog(MainActivity2.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog);

            name = dialog.findViewById(R.id.dialogName);
            pass = dialog.findViewById(R.id.dialogPass);
            Button ok = dialog.findViewById(R.id.dialogOK);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calName = String.valueOf(name.getText());
                    calPass = String.valueOf(pass.getText());
                    createCal(calName,calPass);

                    dialog.dismiss();

                }
            });

            dialog.show();

    }

    public void createCal(String name, String pass){


        calendar = new Calendar(name, pass);
        reference.child(name).setValue(calendar);

        button.setEnabled(true);

    }

}