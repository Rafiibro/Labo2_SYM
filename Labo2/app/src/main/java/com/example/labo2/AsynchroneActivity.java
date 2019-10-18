package com.example.labo2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.BasePermissionListener;

public class AsynchroneActivity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("HTTP: ","ahahahahahahahahahahahahahahahahahahahahahahahaha");

        // Check permission pour l'IMEI
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.INTERNET ) != PackageManager.PERMISSION_GRANTED ) {
            // Demande l'autorisation
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.INTERNET)
                    .withListener(new BasePermissionListener())
                    .check();
        } else {
            // Show the welcome screen / login authentication dialog
            setContentView(R.layout.asynchrone_layout);
            SymComManager mcm = new SymComManager() ;
            mcm.setCommunicationEventListener(
                    new CommunicationEventListener(){
                        public boolean handleServerResponse(String response) {
                            Log.i("HTTP: ",response);
                            return true;
                        }
                    }
            );
            mcm.sendRequest("http://sym.iict.ch/rest/txt", "zgeg");
        }


    }
}
