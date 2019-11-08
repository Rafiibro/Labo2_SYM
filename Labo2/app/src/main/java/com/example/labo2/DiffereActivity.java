package com.example.labo2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.BasePermissionListener;

public class DiffereActivity extends AppCompatActivity{
    public static TextView text_asynchrone = null;
    private EditText req = null;
    private Button send = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.differe_layout);

        this.text_asynchrone = (TextView) findViewById(R.id.textViewAsync);
        this.req =  findViewById(R.id.request);
        this.send = findViewById(R.id.button);
        this.text_asynchrone.setMovementMethod(new ScrollingMovementMethod());

        // Check permission pour l'envois
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.INTERNET ) != PackageManager.PERMISSION_GRANTED ) {
            // Demande l'autorisation
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.INTERNET)
                    .withListener(new BasePermissionListener())
                    .check();
        } else {
            // Creation du handler pour la reponse
            SymComManager mcm = new SymComManager() ;
            mcm.setCommunicationEventListener(
                    new CommunicationEventListener(){
                        public boolean handleServerResponse(final String response) {
                            DiffereActivity.this.runOnUiThread(new Runnable()  {
                                @Override
                                public void run() {
                                    // Update UI widget
                                    DiffereActivity.text_asynchrone.append("Response: \n");
                                    DiffereActivity.text_asynchrone.append(response + "\n\n");
                                }
                            });
                            /** Message has been handled...*/
                            return true;
                        }
                    }
            );

            // Enois de la requete lors de l'appuis sur le bouton send
            send.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mcm.sendRequest("http://sym.iict.ch/rest/txt", req.getText().toString(), "json");
                }
            });


        }

    }
}
