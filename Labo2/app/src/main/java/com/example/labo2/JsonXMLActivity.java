package com.example.labo2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.BasePermissionListener;

import java.util.ArrayList;
import java.util.List;

public class JsonXMLActivity extends AppCompatActivity{
    public static TextView text_asynchrone = null;
    private Button sendJSON = null;
    private Button sendXML = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show the welcome screen / login authentication dialog
        setContentView(R.layout.json_xml_layout);
        this.text_asynchrone = (TextView) findViewById(R.id.textViewAsync);
        this.sendJSON =  findViewById(R.id.sendJSON);
        this.sendXML = findViewById(R.id.sendXML);

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
                            JsonXMLActivity.this.runOnUiThread(new Runnable()  {
                                @Override
                                public void run() {
                                    // Update UI widget
                                    JsonXMLActivity.text_asynchrone.setText(response);
                                }
                            });
                            /** Message has been handled...*/
                            return true;
                        }
                    }
            );

            sendJSON.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String req = FormatRequest.jsonRequest("JSON test");
                    mcm.sendRequest("http://sym.iict.ch/rest/json", req, "json");
                }
            });

            sendXML.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    List<Personne> personnes = new ArrayList<Personne>();
                    Personne pers1 = new Personne("Vuagniaux", "Remy", "Male", "1112223344", "home");
                    Personne pers2 = new Personne("Bacso", "Gaetan", "Male", "1112223344", "work");
                    Personne pers3 = new Personne("Da Cunha", "Rafael", "Male", "1112223344", "mobile");
                    personnes.add(pers1);
                    personnes.add(pers2);
                    personnes.add(pers3);
                    String req = FormatRequest.xmlRequest(personnes);
                    mcm.sendRequest("http://sym.iict.ch/rest/xml", req, "xml");
                }
            });


        }
    }
}
