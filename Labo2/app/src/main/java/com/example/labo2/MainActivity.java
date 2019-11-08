package com.example.labo2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn_asynchrone = null;
    private Button btn_differe = null;
    private Button btn_jsonXml = null;
    private Button btn_compresse = null;
    private Button btn_graphQL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Active le layout de main activity */
        setContentView(R.layout.activity_main);

        /* Indique la policy des threads */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /* Initialisation des boutons */
        this.btn_asynchrone = findViewById(R.id.button1);
        this.btn_differe = findViewById(R.id.button2);
        this.btn_jsonXml = findViewById(R.id.button4);
        this.btn_compresse = findViewById(R.id.button5);
        this.btn_graphQL = findViewById(R.id.button6);

        /* Lorsque l'on clique sur un des boutons, lance l'activité correspondante */

        btn_asynchrone.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, AsynchroneActivity.class);
            startActivity(intent);
        });

        btn_compresse.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, CompresseActivity.class);
            startActivity(intent);
        });

        btn_jsonXml.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, JsonXMLActivity.class);
            startActivity(intent);
        });

        btn_graphQL.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, GraphQLActivity.class);
            startActivity(intent);
        });

        btn_differe.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, DiffereActivity.class);
            startActivity(intent);
        });

    }
}
