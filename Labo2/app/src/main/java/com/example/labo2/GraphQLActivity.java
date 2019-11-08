/**
 * Auteurs: Bacso, Vuagniaux, Da Cunha
 */
package com.example.labo2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.BasePermissionListener;

import java.util.List;

public class GraphQLActivity extends AppCompatActivity{

    private Spinner spin = null;
    private Button btn = null;
    private TextView text = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_ql_layout);
        this.spin = findViewById(R.id.spinner);
        this.btn =  findViewById(R.id.showPost);
        this.text = findViewById(R.id.text);
        this.text.setMovementMethod(new ScrollingMovementMethod());

        // Check permission pour l'envois
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.INTERNET ) != PackageManager.PERMISSION_GRANTED ) {
            // Demande l'autorisation
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.INTERNET)
                    .withListener(new BasePermissionListener())
                    .check();
        } else {
            // Recuperation des auteurs et update de la dropdown
            List<Autors> authors = GraphQLApi.allAutors();
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                    android.R.layout.simple_spinner_dropdown_item,
                    authors);
            spin.setAdapter(spinnerArrayAdapter);

            // Affiche tous les posts de l'auteurs selectionne dans la dropdown
            // lors du click sur le bouton
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    text.setText("");
                    Autors author = (Autors)spin.getSelectedItem();
                    List<Post> posts = GraphQLApi.allPostByAutor(author.getId());
                    for(Post post : posts){
                        text.append("Title: \n");
                        text.append(post.getTitle() + "\n");
                        text.append("Description: \n");
                        text.append(post.getDescription() + "\n");
                        text.append("Date : \n");
                        text.append(post.getDate() + "\n");
                        text.append("Content: \n");
                        text.append(post.getContent() + "\n\n");
                    }
                }
            });


        }
    }
}
