/**
 * Auteurs: Bacso, Vuagniaux, Da Cunha
 */
package com.example.labo2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        setContentView(R.layout.json_xml_layout);
        this.text_asynchrone = (TextView) findViewById(R.id.textViewAsync);
        this.sendJSON =  findViewById(R.id.sendJSON);
        this.sendXML = findViewById(R.id.sendXML);

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

            // Enois de la requete json lors de l'appuis sur le bouton send
            sendJSON.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String req = FormatRequest.jsonRequest("JSON test");
                    mcm.sendRequest("http://sym.iict.ch/rest/json", req, "json");
                }
            });

            // Enois de la requete xml lors de l'appuis sur le bouton send
            sendXML.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    List<Personne> personnes = new ArrayList<Personne>();
                    Personne pers1 = new Personne("Vuagniaux", "Remy", "Female", "1112223344", "Rageux");
                    Personne pers2 = new Personne("Bacso", "Gaetan", "Others", "1112223344", "Bard");
                    Personne pers3 = new Personne("Da Cunha", "Rafael", "Male", "1112223344", "Etranger");
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
