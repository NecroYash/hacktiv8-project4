package com.example.travel;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TicketActivity extends AppCompatActivity{

    ImageView generateTicket;
    TextView fromTicket, toTicket, busTicket, timeTicket, dateTicket, longTicket, priceTicket;

    private static final int PERMISSION_REQUEST_CODE = 10;
    private static final String TAG = "Tripy.com";
    int pdfHeight = 1000;
    int pdfWidth = 720;
    private PdfDocument document;
    private static final int CREATE_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        fromTicket = (TextView) findViewById(R.id.fromTicket);
        toTicket = (TextView) findViewById(R.id.toTicket);
        busTicket = (TextView) findViewById(R.id.busTicket);
        timeTicket = (TextView) findViewById(R.id.timeTicket);
        dateTicket = (TextView) findViewById(R.id.dateTicket);
        longTicket = (TextView) findViewById(R.id.longTicket);
    }
//
    public void createPdfFromView(View view){
        final Dialog ticketDialog = new Dialog(this);
        ticketDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ticketDialog.setContentView(R.layout.generate);
        ticketDialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(ticketDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ticketDialog.getWindow().setAttributes(lp);
        ImageView downloadTicket = ticketDialog.findViewById(R.id.downloadTicket);
        TextView fromT = ticketDialog.findViewById(R.id.fromT);
        TextView toT = ticketDialog.findViewById(R.id.toT);
        TextView busT = ticketDialog.findViewById(R.id.bussT);
        TextView timeT = ticketDialog.findViewById(R.id.timeT);
        TextView dateT = ticketDialog.findViewById(R.id.dateT);
        TextView longT = ticketDialog.findViewById(R.id.longT);

        fromT.setText(fromTicket.getText());
        toT.setText(toTicket.getText());
        busT.setText(busTicket.getText());
        timeT.setText(timeTicket.getText());
        dateT.setText(dateTicket.getText());
        longT.setText(longTicket.getText());

        ticketDialog.show();
        downloadTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePdfFromView(ticketDialog.findViewById(R.id.layoutTicketUser));
            }
        });
    }

    private void generatePdfFromView(View view) {
        Bitmap bitmap = getBitmapFromView(view);
        document = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page myPage = document.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(myPage);
        createFile();
    }


    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "ticket.pdf");
        startActivityForResult(intent, CREATE_FILE);
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if(bgDrawable != null){
            bgDrawable.draw(canvas);
        }else{
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK){
            Uri uri = null;
            if(data != null){
                uri = data.getData();

                if(document != null){
                    ParcelFileDescriptor pfd = null;
                    try{
                        pfd = getContentResolver().openFileDescriptor(uri, "w");
                        FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
                        document.writeTo(fileOutputStream);
                        document.close();
                    } catch (IOException e) {
                        try{
                            DocumentsContract.deleteDocument(getContentResolver(), uri);
                        }catch (FileNotFoundException ex){
                            ex.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                }
            }
        }
    }





    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }
    private void getData(){
        fromTicket.setText(getIntent().getStringExtra("fromBooking"));
        toTicket.setText(getIntent().getStringExtra("toBooking"));
        busTicket.setText(getIntent().getStringExtra("nameBus"));
        timeTicket.setText(getIntent().getStringExtra("timeBooking"));
        dateTicket.setText(getIntent().getStringExtra("dateBooking"));
        longTicket.setText(getIntent().getStringExtra("longTime"));
    }


}