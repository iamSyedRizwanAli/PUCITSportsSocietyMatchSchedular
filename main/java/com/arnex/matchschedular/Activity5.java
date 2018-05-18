package com.arnex.matchschedular;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Activity5 extends AppCompatActivity {

    String finalFile, sport, oc, coord;
    ArrayList<String> matches;
    boolean failureFlag = false;

    TextView fileText;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5);

        sport = getIntent().getStringExtra("Sport");
        oc = getIntent().getStringExtra("Occasion");
        coord = getIntent().getStringExtra("Coordinator");
        matches = getIntent().getStringArrayListExtra("Matches");

        button = (Button) findViewById(R.id.finalActButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fileText = (TextView) findViewById(R.id.fileName);
        createPDFFile();
        if(failureFlag){
            fileText.setText("<<ERROR>>");
            new AlertDialog.Builder(this).setTitle("There's a problem here!").setMessage("Our application faced difficulty in creating both PDF and Word file.\nKindly contact ARNEX about this issue").setNeutralButton("Okay", null).show();
        }else {
            fileText.setText(finalFile);
            readPdfFile();
        }

    }

    private void readPdfFile() {

        String pdfFile = Environment.getExternalStorageDirectory().getPath().toString() + "/Documents/" + finalFile;

        File file = new File(pdfFile);
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, "There has been a technical problem in opening the PDF file. Please contact ARNEX.", Toast.LENGTH_SHORT).show();
        }

    }

    private void createPDFFile(){

        failureFlag = false;
        String path = Environment.getExternalStorageDirectory().getPath().toString() + "/Documents";

        String morningMatches = "\n", afternoonMatches = "\n";

        for(int i = 0 ; i < matches.size() ; i++) {

            String temp = matches.get(i);

            if(temp.contains("Morning")) {
                morningMatches += "Match " + (i + 1) + ") " + temp.substring(0, temp.indexOf(';')) + "\n";
            }else{
                afternoonMatches += "Match " + (i + 1) + ") " + temp.substring(0, temp.indexOf(';')) + "\n";
            }
        }

        String body = "\nCoordinator: " + coord + "\nSport: " + sport + "\n\n";

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        String credits = "\n\n*This match schedule is created using PUCIT Sport Society Match Scheduler on " + dateFormat.format(date) + " at " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();


        File directory = new File(path);

        if(!directory.exists()){
            directory.mkdir();
        }

        Document doc = new Document();

        finalFile = sport + " - " + oc + ".pdf";
        File file = new File(directory, finalFile);

        int j = 1;

        while(file.exists()){
            finalFile = sport + " - " + oc + " (" + j +").pdf";
            file = new File(directory, finalFile);
            j++;
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            PdfWriter.getInstance(doc, fos);

            doc.open();

            InputStream inputStream = getAssets().open("pucit_sports_society_logo_no_background.png");
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleToFit(40,40);
            image.setAlignment(Element.ALIGN_CENTER);
            doc.add(image);

            Paragraph paragraph1 = new Paragraph("PUCIT Sports Society", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD | Font.UNDERLINE));
            paragraph1.setAlignment(Paragraph.ALIGN_CENTER);

            doc.add(paragraph1);

            Paragraph paragraph2 = new Paragraph(oc, new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL));
            paragraph2.setAlignment(Paragraph.ALIGN_CENTER);

            doc.add(paragraph2);

            Paragraph paragraph3 = new Paragraph(body,  new Font(Font.FontFamily.HELVETICA, 12));
            doc.add(paragraph3);

            Paragraph morningMatchesTitle = new Paragraph("Matches in Morning", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD | Font.UNDERLINE));
            morningMatchesTitle.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(morningMatchesTitle);

            doc.add(
                    new Paragraph(morningMatches, new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL))
            );

            Paragraph afternoonMatchesTitle = new Paragraph("Matches in Afternoon", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD | Font.UNDERLINE));
            afternoonMatchesTitle.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(afternoonMatchesTitle);

            doc.add(
                    new Paragraph(afternoonMatches, new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL))
            );

            Paragraph paragraph4 = new Paragraph(credits, new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.UNDERLINE | Font.ITALIC));
            doc.add(paragraph4);

            if(doc.isOpen()) {
                doc.close();
            }

            fos.close();

            fileText.setText(finalFile);

        }catch (IOException | DocumentException e) {
            failureFlag = true;
        }

        if(!failureFlag){
            return;
        }

        failureFlag = false;

        finalFile = sport + " - " + oc + ".doc";
        file = new File(directory, finalFile);

        j = 1;

        while(file.exists()){
            finalFile = sport + " - " + oc + " (" + j +").doc";
            file = new File(directory, finalFile);
            j++;
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos);

            dos.writeBytes("PUCIT Sports Society\n"+oc+"\n\n"+body+"\n\nMatches in Morning\n"+morningMatches+"\n\nMatches in Afternoon\n"+afternoonMatches+"\n\n"+credits);

            dos.close();

        }catch (IOException e){
            failureFlag = true;
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, Activity2.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

}
