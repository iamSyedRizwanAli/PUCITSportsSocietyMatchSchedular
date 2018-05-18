package com.arnex.matchschedular;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class Activity3 extends AppCompatActivity {

    boolean backPressedTwiceToExit = false;
    String [] shift = {"Morning", "Afternoon"};
    RelativeLayout.LayoutParams rlp;
    int no_of_teams = 0;

    Button addTeam, subTeam, makeSchedule;

    String oc, coord, sport;

    LinearLayout parentLayout;
    ArrayList<Integer> teamIdList, etIdList, shiftIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);

        oc = getIntent().getStringExtra("Occasion");
        coord = getIntent().getStringExtra("Coordinator");
        sport = getIntent().getStringExtra("Sport");

        init();
    }

    private void init(){

        teamIdList = new ArrayList<>();
        shiftIdList = new ArrayList<>();
        etIdList = new ArrayList<>();
        parentLayout = (LinearLayout) findViewById(R.id.teams);

        rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        addTeam = (Button) findViewById(R.id.addTeam);
        addTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonFunction(view.getId());
            }
        });

        subTeam = (Button) findViewById(R.id.subTeam);
        subTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonFunction(view.getId());
            }
        });

        makeSchedule = (Button) findViewById(R.id.continueButton3);
        makeSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonFunction(R.id.continueButton3);
            }
        });

        addTeamInLayout(createTeamField());

    }

    private void buttonFunction(int id){

        View view = this.getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity3.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if(id == R.id.addTeam){
            addTeamInLayout(createTeamField());
        }else if(id == R.id.subTeam){
            removeTeamFromLayout();
        }else{
            schedule();
        }

    }

    private RelativeLayout createTeamField() {

        RelativeLayout teamBar = new RelativeLayout(this);
        int x = View.generateViewId();
        teamBar.setId(x);
        teamIdList.add(x);

        x = View.generateViewId();
        etIdList.add(x);

        EditText editText = new EditText(this);
        editText.setId(x);

        x = View.generateViewId();
        shiftIdList.add(x);

        Spinner spinner = new Spinner(this);
        spinner.setId(x);
        ArrayAdapter<String> spinnerArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, shift);

        editText.setHint("Team/Player " + (no_of_teams + 1) + " name");
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        spinner.setAdapter(spinnerArray);
        spinner.setLayoutParams(rlp);
        teamBar.addView(editText);
        teamBar.addView(spinner);

        return teamBar;

    }

    private void addTeamInLayout(RelativeLayout subject){
        parentLayout.addView(subject, no_of_teams);
        no_of_teams++;
    }

    private void removeTeamFromLayout(){
        if(no_of_teams > 1) {
            no_of_teams--;
            parentLayout.removeViewAt(no_of_teams);
            etIdList.remove(no_of_teams);
            shiftIdList.remove(no_of_teams);
            teamIdList.remove(no_of_teams);
        }else{
            Toast.makeText(this, "There is always at least one team on the screen", Toast.LENGTH_SHORT).show();
        }
    }

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

    private boolean areFieldsFilled(){

        boolean flag = false;
        TextView textView;
        for(int i = 0 ; i < etIdList.size() && !flag ; i++){

            textView = (TextView) findViewById(etIdList.get(i));

            if(textView.getText().toString().length() == 0){
                flag = true;
            }

        }

        return flag;
    }

    private void schedule(){

        if(!areFieldsFilled()) {
            ArrayList<Team> morningTeams, afternoonTeams;
            ArrayList<Match> morningMatches, afternoonMatches;

            Stack<Team> morningStack = new Stack<Team>();

            morningTeams = new ArrayList<>();
            afternoonTeams = new ArrayList<>();

            morningMatches = new ArrayList<>();
            afternoonMatches = new ArrayList<>();

            Spinner temp;
            EditText editText;

            for (int i = 0; i < no_of_teams; i++) {
                temp = (Spinner) findViewById(shiftIdList.get(i));
                editText = (EditText) findViewById(etIdList.get(i));

                if (temp.getSelectedItem().toString().equals("Morning")) {
                    morningTeams.add(new Team(editText.getText().toString(), "Morning"));
                } else {
                    afternoonTeams.add(new Team(editText.getText().toString(), "Afternoon"));
                }
            }

            boolean[] morningFlags = new boolean[morningTeams.size()];
            int selectedTeamA, selectedTeamB;
            Double dTemp = 0.00;

            int j = 0;

            for (int i = 0; i < morningTeams.size() / 2; i++) {

                do {
                    dTemp = Math.random() * morningTeams.size();
                    selectedTeamA = dTemp.intValue();
                }while (morningFlags[selectedTeamA]);

                morningFlags[selectedTeamA] = true;

                j++;

                do {
                dTemp = Math.random() * morningTeams.size();
                selectedTeamB = dTemp.intValue();
                }while (morningFlags[selectedTeamB]);

                morningFlags[selectedTeamB] = true;

                afternoonMatches.add(new Match(morningTeams.get(selectedTeamA).getTeamName(), morningTeams.get(selectedTeamB).getTeamName(), "Afternoon"));

                j++;
            }

            boolean[] afternoonFlags = new boolean[afternoonTeams.size()];
            int l = 0;
            for (int k = 0; k < afternoonTeams.size() / 2; k++) {

                do {
                    dTemp = Math.random() * afternoonTeams.size();
                    selectedTeamA = dTemp.intValue();
                } while (afternoonFlags[selectedTeamA]);

                afternoonFlags[selectedTeamA] = true;

                l++;

                do {
                    dTemp = Math.random() * afternoonTeams.size();
                    selectedTeamB = dTemp.intValue();
                } while (afternoonFlags[selectedTeamB]);

                afternoonFlags[selectedTeamB] = true;
                l++;

                morningMatches.add(new Match(afternoonTeams.get(selectedTeamA).getTeamName(), afternoonTeams.get(selectedTeamB).getTeamName(), "Morning"));

            }

            if (j < morningTeams.size() && l < afternoonTeams.size()) {

                int x;

                for (x = 0; x < morningFlags.length && morningFlags[x]; x++) {

                }

                int y;

                for (y = 0; y < afternoonTeams.size() && afternoonFlags[y]; y++) {

                }

                dTemp = Math.random() * 2;
                int toss = dTemp.intValue();

                if (toss == 0) {
                    morningMatches.add(new Match(morningTeams.get(x).getTeamName(), afternoonTeams.get(y).getTeamName(), "Morning"));
                } else {
                    afternoonMatches.add(new Match(morningTeams.get(x).getTeamName(), afternoonTeams.get(y).getTeamName(), "Afternoon"));
                }

            } else if (l < afternoonTeams.size()) {

                int x;

                for (x = 0; x < afternoonTeams.size() && afternoonFlags[x]; x++) {

                }

                if(morningMatches.size() == 0) {
                    morningMatches.add(new Match(afternoonTeams.get(x).getTeamName(), "<winner of Match " + (afternoonMatches.size() + 1) + ">", "Morning"));
                }else{
                    morningMatches.add(new Match(afternoonTeams.get(x).getTeamName(), "<winner of Match 1>", "Morning"));
                }
            } else if (j < morningTeams.size()) {

                int y;

                for (y = 0; y < morningTeams.size() && morningFlags[y]; y++) {

                }

                if(afternoonMatches.size() == 0) {
                    afternoonMatches.add(new Match(morningTeams.get(y).getTeamName(), "<winner of Match " + morningMatches.size() + ">", "Afternoon"));
                }else{
                    afternoonMatches.add(new Match(morningTeams.get(y).getTeamName(), "<winner of Match " + (morningMatches.size() + 1) + ">", "Afternoon"));
                }
            }

            ArrayList<String> matches = new ArrayList<>();

            for (int i = 0; i < morningMatches.size(); i++) {
                matches.add(morningMatches.get(i).toString());
            }

            for (int i = 0; i < afternoonMatches.size(); i++) {
                matches.add(afternoonMatches.get(i).toString());
            }

            Intent intent = new Intent(this, Activity4.class);
            intent.putStringArrayListExtra("Matches", matches);
            intent.putExtra("Occasion", oc);
            intent.putExtra("Coordinator", coord);
            intent.putExtra("Sport", sport);

            startActivity(intent);
        }else{
            Toast.makeText(this, "Kindly fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

}
