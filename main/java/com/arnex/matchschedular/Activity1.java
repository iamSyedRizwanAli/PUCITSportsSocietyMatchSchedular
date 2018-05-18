package com.arnex.matchschedular;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Activity1 extends AppCompatActivity {

    Button continueButton;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        editText = (EditText) findViewById(R.id.occasion);

        String oc = readOccasion();

        if(!oc.equals("0")){
            editText.setText(oc);
        }

        continueButton = (Button) findViewById(R.id.startButton);
        continueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                EditText editText = (EditText) findViewById(R.id.occasion);
                String occasion = editText.getText().toString();

                if(occasion.length() == 0){
                    Toast.makeText(view.getContext(), "Kindly fill in the occasion", Toast.LENGTH_SHORT).show();
                }else{
                    writeOccasion(occasion);
                    Intent i = new Intent(view.getContext(), Activity2.class);
                    i.putExtra("Occasion", occasion);
                    startActivity(i);
                }

            }
        });
    }

    private String readOccasion(){

        String input = "0";

        try {
            File file = new File(getApplicationContext().getFilesDir(), "occasionFile");
            FileInputStream fis = new FileInputStream(file);
            DataInputStream dis = new DataInputStream(fis);

            input = dis.readLine();

            dis.close();
            fis.close();

        }catch (FileNotFoundException e){

        }catch (IOException e){

        }

        return input;
    }

    private void writeOccasion(String occasion){

        try{
            File file = new File(getApplicationContext().getFilesDir(), "occasionFile");

            FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos);

            dos.writeBytes(occasion);

            dos.close();
            fos.close();

        }catch (FileNotFoundException e){

        }catch (IOException e){

        }

    }

}
