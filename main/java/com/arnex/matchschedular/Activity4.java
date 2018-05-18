package com.arnex.matchschedular;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Activity4 extends AppCompatActivity {

    boolean backPressedTwiceToExit = false;

    ArrayList <String> matches;
    TextView textView, fileText;

    Button button;

    String oc, coord, sport, finalFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        matches = getIntent().getStringArrayListExtra("Matches");
        setContentView(R.layout.activity_4);

        oc = getIntent().getStringExtra("Occasion");
        coord = getIntent().getStringExtra("Coordinator");
        sport = getIntent().getStringExtra("Sport");

        button = (Button) findViewById(R.id.continueButton4);

        init();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init(){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(view.getContext(), Activity5.class);
                i.putExtra("Coordinator", coord);
                i.putExtra("Occasion", oc);
                i.putExtra("Sport", sport);
                i.putStringArrayListExtra("Matches", matches);
                startActivity(i);

            }
        });

        fileText = (TextView) findViewById(R.id.fileName);

        TextView view = (TextView) findViewById(R.id.occasionField);
        view.setText(oc);

        view = (TextView) findViewById(R.id.coordinatorNameField);
        view.setText(coord);

        view = (TextView) findViewById(R.id.sportField);
        view.setText(sport);

        textView = (TextView) findViewById(R.id.matches);

        String xyz = "";

        for (int i = 0 ; i < matches.size() ; i++){
            String temp = matches.get(i);
            xyz += "Match " + (i + 1) + ")\n" + temp.substring(0, temp.indexOf(';')) + "\n\n";
        }

        textView.setText(xyz);

    }

    public void onBackPressed() {

        if (backPressedTwiceToExit) {
            super.onBackPressed();
            return;
        } else {

            backPressedTwiceToExit = true;

            Toast.makeText(this, "Press the back key again to return to main screen.", Toast.LENGTH_SHORT).show();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    backPressedTwiceToExit = false;
                }
            };

            Timer t = new Timer();
            t.schedule(task, 3000);

        }
    }

}
