package iut.dam.powerhome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;
import java.util.List;
public class HabitatActivity extends Activity {
    private ListView listView;
    private HabitatAdapter adapter;
    private List<Habitat> habitants = new ArrayList<>();
    private ProgressDialog pDialog;
    private boolean isAdapterInitialized = false;
    private static final String TAG = "HabitatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitat);

        listView = findViewById(R.id.listViewHabitats);

        // Initialisation de la progressDialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Getting list of habitats...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        // Appel pour récupérer les habitats depuis le serveur
        getRemoteHabitats();

        // Action lors d'un clic sur un élément de la liste
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Habitat habitat = habitants.get(position);
            Toast.makeText(HabitatActivity.this, "Résident: " + habitat.getResidentName(), Toast.LENGTH_SHORT).show();
        });
    }

    // Méthode pour récupérer les habitats depuis le serveur
    public void getRemoteHabitats() {
        pDialog.show();

        String urlString = "http://192.168.1.250/powerhome/getHabitats_v2.php?token=6a81470661821d7ff232962aded97c41";
        Ion.with(this)
                .load(urlString)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        pDialog.dismiss();
                        if (result == null || result.getResult() == null) {
                            Log.d(TAG, "No response from the server!!!");
                            return;
                        }

                        String json = result.getResult();
                        Log.d(TAG, "Réponse JSON reçue : " + json);

                        // Parse les données JSON en une liste d'habitats
                        habitants.clear();
                        habitants.addAll(Habitat.getListFromJson(json));

                        // Affichage dans le log de chaque habitat récupéré
                        for (Habitat h : habitants) {
                            Log.d(TAG, "Habitat récupéré : residentName: " + h.getResidentName() + ", Étage: " + h.getEtage() + ", Equipements: " + h.getNbEquipements());
                        }

                        // Initialiser l'adaptateur avec la liste des habitants
                        initializeAdapter();
                    }
                });
    }

    // Méthode pour initialiser l'adaptateur
    private void initializeAdapter() {
        if (!isAdapterInitialized && !habitants.isEmpty()) {
            adapter = new HabitatAdapter(this, habitants);  // Utilisation de l'adaptateur mis à jour
            listView.setAdapter(adapter);
            isAdapterInitialized = true;
        } else if (isAdapterInitialized) {
            adapter.notifyDataSetChanged();
        }
    }
}
