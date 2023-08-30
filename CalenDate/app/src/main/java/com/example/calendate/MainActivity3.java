package com.example.calendate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity3 extends AppCompatActivity implements com.example.calendate.FragmentOne.SendInformation{

    EditText name,notes;
    Button save;
    JSONObject json;

    String calName;
    Gson gson;
    FirebaseDatabase database;
    DatabaseReference reference;

    CalendarView calendarView;
    int date = 0;

    FragmentContainerView containerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        calendarView = findViewById(R.id.calendarView);

        containerView = findViewById(R.id.fragmentContainerView);

        database = FirebaseDatabase.getInstance();
        calName = getIntent().getStringExtra("calName");
        reference = database.getReference("calendars/"+calName);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String json1 = snapshot.child("json").getValue(String.class);
                    JSONObject jsonString = gson.fromJson(json1, JSONObject.class);
                    if(jsonString!=null)
                        json = jsonString;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gson = new Gson();

        name = findViewById(R.id.nameEdit);
        notes = findViewById(R.id.notesEdit);
        save = findViewById(R.id.saveButton);


        json = new JSONObject();

        Date currentTime = Calendar.getInstance().getTime();
        date = 0;
        date += (currentTime.getYear()+1900) * 10000;
        date += (currentTime.getMonth()+1)*100;
        date +=currentTime.getDate();


        calendarView.setMinDate(System.currentTimeMillis());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteAdded = name.getText() + ": " + notes.getText();
                Log.d("tag", noteAdded);
                try {
                    if(json.has(String.valueOf(date)))
                        noteAdded = json.get(String.valueOf(date)) + "\n" + noteAdded;
                    json.put(String.valueOf(date), noteAdded);

                    String jsonString = gson.toJson(json);

                    reference.child("json").setValue(jsonString);

                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = 0;
                date += year * 10000;
                date += (month+1)*100;
                date +=dayOfMonth;

                loadFragment(new com.example.calendate.FragmentOne());

            }
        });


    }
    @SuppressLint("WrongConstant")
    private void loadFragment(Fragment fragment) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction
                .replace(R.id.fragmentContainerView, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ;
        fragmentTransaction.commit(); // save the changes

    }

    public void sentFromFragments(String str)
    {
        try {
            json.put(String.valueOf(date), str);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("tag", String.valueOf(json));


    }

    public String getNum()
    {
        return String.valueOf(date);
    }

    public String getText(String x)
    {
        try {
            if(json.has(x))
                return (String)json.get(x);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Notes";
    }

    public String getCalName()
    {
        return calName;
    }

}