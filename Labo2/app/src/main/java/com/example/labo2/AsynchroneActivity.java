package com.example.labo2;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.BasePermissionListener;

public class AsynchroneActivity extends AppCompatActivity{
    public static TextView text_asynchrone = null;
    private Activity act = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asynchrone_layout);

        this.text_asynchrone = (TextView) findViewById(R.id.textViewAsync);
        this.text_asynchrone.setMovementMethod(new ScrollingMovementMethod());

        // Check permission pour l'IMEI
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.INTERNET ) != PackageManager.PERMISSION_GRANTED ) {
            // Demande l'autorisation
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.INTERNET)
                    .withListener(new BasePermissionListener())
                    .check();
        } else {
            // Show the welcome screen / login authentication dialog
            SymComManager mcm = new SymComManager() ;
            mcm.setCommunicationEventListener(
                    new CommunicationEventListener(){
                        public boolean handleServerResponse(final String response) {
                            AsynchroneActivity.this.runOnUiThread(new Runnable()  {
                                @Override
                                public void run() {
                                    // Update UI widget
                                    AsynchroneActivity.text_asynchrone.setText(response);
                                }
                            });
                            /** Message has been handled...*/
                            return true;
                        }
                    }
            );
            mcm.sendRequest("http://sym.iict.ch/rest/txt", "zgeg", "json");

        }


    }
}
