package com.arnex.matchschedular;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Activity2 extends AppCompatActivity {

    boolean backPressedTwiceToExit = false;
    Button continueButton;
    Spinner gameSpinner, categorySpinner;
    EditText name;
    String oc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        oc = getIntent().getStringExtra("Occasion");

        init();

    }

    private void init(){

        gameSpinner = (Spinner) findViewById(R.id.gameSpinner);
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);

        name = (EditText) findViewById(R.id.coordinatorName);

        initializeSpinners();

        continueButton = (Button) findViewById(R.id.continueButton2);
        continueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View view) {
                if(name.getText().toString().length() == 0){
                    Toast.makeText(view.getContext(), "Kindly fill the Coordinator name", Toast.LENGTH_SHORT).show();
                }else{
                    new AlertDialog.Builder(view.getContext()).setTitle("Confirmation").setMessage("Are you sure that Coordinator for " + categorySpinner.getSelectedItem().toString() + " " + gameSpinner.getSelectedItem().toString() + " is " + name.getText().toString() + "?").setNegativeButton("No", null).setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent(view.getContext(), Activity3.class);
                                    intent.putExtra("Coordinator", name.getText().toString());
                                    intent.putExtra("Sport", gameSpinner.getSelectedItem().toString() + " (" + categorySpinner.getSelectedItem().toString() + ")");
                                    intent.putExtra("Occasion", oc);
                                    startActivity(intent);
                                }
                            }).show();
                }
            }
        });

    }

    private void initializeSpinners(){

        String [] gameArray = {"Cricket","Football","Hockey","Volleyball","Basketball","Ludo","Tug of War","Foosball","Chess","Arm Wrestling","Carom (Single)", "Carom (Doubles)", "Table Tennis (Single)", "Table Tennis (Doubles)", "Darts", "Badminton (Single)", "Badminton (Doubles)", "Frisbee"};
        ArrayAdapter <String> games = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gameArray);
        gameSpinner.setAdapter(games);

        String [] gender = {"Boys", "Girls"};
        ArrayAdapter <String> category = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gender);
        categorySpinner.setAdapter(category);

    }

    @Override
    public void onBackPressed() {

        if(backPressedTwiceToExit){
            super.onBackPressed();
            return;
        }else {

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
