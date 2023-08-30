package com.example.calendate;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.calendate.MainActivity3;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentOne extends Fragment {
    EditText editFrag;
    Button buttonFrag;

    String x;
    String notes;

    TextView text;

    FirebaseDatabase database;
    DatabaseReference reference;
    Gson gson;

    ArrayList<JSONObject> list;
    SendInformation sendInformation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one,null);



        MainActivity3 activity = (MainActivity3)getActivity();
        x = activity.getNum();
        notes = activity.getText(x);

        String calName = activity.getCalName();
        database = FirebaseDatabase.getInstance();

        reference = database.getReference("calendars/"+calName);


        Log.d("tag",x +" " + notes);
        text= view.findViewById(R.id.textView);

        text.setText(notes);
        text.setMovementMethod(new ScrollingMovementMethod());
        buttonFrag = view.findViewById(R.id.buttonFragment);

        list = new ArrayList<>();

        buttonFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                x = activity.getNum();
                notes = activity.getText(x);
                text.setText(notes);

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        sendInformation =(SendInformation) context;
    }

    public interface SendInformation{
        public void sentFromFragments(String str);
    }


}