package com.madhwendra.datecompare;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
   private TextView date;
   private DatabaseReference databaseReference;
   private String sampleDate;
   private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        date = findViewById(R.id.date);
        progressBar = findViewById(R.id.progressbar);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Dates");

        getData();



    }

    private void getData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    sampleDate = snapshot.child("date").getValue(String.class);
                    progressBar.setVisibility(View.GONE);


                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "dd-MM-yyyy");
                    try {

                        Date savedDate = dateFormat.parse(sampleDate);
                        System.out.println(savedDate);

                        Date currentDate = new Date();

                        long diff = currentDate.getTime() - savedDate.getTime();
                        long seconds = diff / 1000;
                        long minutes = seconds / 60;
                        long hours = minutes / 60;
                        long days = hours / 24;

                        if (savedDate.before(currentDate)) {


                            Log.e("oldDate", "is previous date");
                            Log.e("Difference: ", " seconds: " + seconds + " minutes: " + minutes
                                    + " hours: " + hours + " days: " + days);

                            date.setText("" + days + " days");

                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}