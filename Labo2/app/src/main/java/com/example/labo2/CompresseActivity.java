/**
 * Auteurs: Bacso, Vuagniaux, Da Cunha
 */
package com.example.labo2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.BasePermissionListener;

public class CompresseActivity extends AppCompatActivity{
    public static TextView text_asynchrone = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compresse_layout);
        this.text_asynchrone = findViewById(R.id.textViewAsync);

        // Check permission pour l'envois
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.INTERNET ) != PackageManager.PERMISSION_GRANTED ) {
            // Demande l'autorisation
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.INTERNET)
                    .withListener(new BasePermissionListener())
                    .check();
        } else {
            // Creation du handler pour la reponse
            SymComManagerCompresse mcm = new SymComManagerCompresse() ;
            mcm.setCommunicationEventListener(
                    new CommunicationEventListener(){
                        public boolean handleServerResponse(final String response) {
                            CompresseActivity.this.runOnUiThread(new Runnable()  {
                                @Override
                                public void run() {
                                    // Update du textview
                                    CompresseActivity.text_asynchrone.append(response);
                                }
                            });
                            /** Message has been handled...*/
                            return true;
                        }
                    }
            );
            // Envois de la requete
            mcm.sendRequest("http://sym.iict.ch/rest/txt", "zgeg", "json");
        }
    }
}
