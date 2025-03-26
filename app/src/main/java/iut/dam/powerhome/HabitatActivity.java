package iut.dam.powerhome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HabitatActivity extends Activity {
    private ListView listView;
    private HabitatAdapter adapter;
    private List<Habitat> habitants;

    private static final String TAG = "HabitatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitat);

        listView = findViewById(R.id.listViewHabitats);
        habitants = new ArrayList<>();

        int ic_cleaner = R.drawable.ic_cleaner;
        int ic_clim = R.drawable.ic_clim;
        int ic_laundry = R.drawable.ic_laundry;
        int ic_iron = R.drawable.ic_iron;

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView ivBack = findViewById(R.id.ivBack);
        if (ivBack == null) {
            Log.e(TAG, "Erreur : ivBack est null, vérifie son ID dans activity_register.xml");
        } else {
            ivBack.setOnClickListener(v -> {
                Log.d(TAG, "Retour à LoginActivity");
                Intent intent = new Intent(HabitatActivity.this, AccueilActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // Ajouter des habitants avec équipements
        habitants.add(new Habitat("Gaëtan Leclair", 4, 1, Arrays.asList(ic_laundry, ic_cleaner, ic_clim, ic_iron)));
        habitants.add(new Habitat("Cédric Boudet", 1, 1, Arrays.asList(ic_laundry)));
        habitants.add(new Habitat("Gaylord Thibodeaux", 2, 2, Arrays.asList(ic_iron, ic_cleaner)));
        habitants.add(new Habitat("Adam Jacquinot", 3, 3, Arrays.asList(ic_laundry, ic_cleaner, ic_iron)));


        adapter = new HabitatAdapter(this, habitants);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Habitat habitat = habitants.get(position);
                Toast.makeText(HabitatActivity.this, "Résident: " + habitat.getNom(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
