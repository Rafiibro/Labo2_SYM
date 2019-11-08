package com.example.labo2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import java.util.zip.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.BasePermissionListener;

public class CompresseActivity extends AppCompatActivity{
    public static TextView text_asynchrone = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show the welcome screen / login authentication dialog
        setContentView(R.layout.compresse_layout);
        this.text_asynchrone = (TextView) findViewById(R.id.textViewAsync);

        // Check permission pour l'IMEI
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.INTERNET ) != PackageManager.PERMISSION_GRANTED ) {
            // Demande l'autorisation
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.INTERNET)
                    .withListener(new BasePermissionListener())
                    .check();
        } else {
            // Show the welcome screen / login authentication dialog
            SymComManagerCompresse mcm = new SymComManagerCompresse() ;
            mcm.setCommunicationEventListener(
                    new CommunicationEventListener(){
                        public boolean handleServerResponse(final String response) {
                            CompresseActivity.this.runOnUiThread(new Runnable()  {
                                @Override
                                public void run() {
                                    // Update UI widget
                                    CompresseActivity.text_asynchrone.append(response);
                                }
                            });
                            /** Message has been handled...*/
                            return true;
                        }
                    }
            );
            mcm.sendRequest("http://sym.iict.ch/rest/json", "zgeg", "json");
        }
    }
}
