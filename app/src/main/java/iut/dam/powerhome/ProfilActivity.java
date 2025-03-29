package iut.dam.powerhome;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfilActivity extends AppCompatActivity {

    private static final String TAG = "ProfilActivity";
    private SharedPreferences preferences;
    private ProgressDialog pDialog;
    private TextView etName, etEmail, etPrenom, etConsoTotale;  // Ajout de TextView pour la consommation totale
    private List<Appliance> appliances;  // Liste pour stocker les équipements
    private ListView appliancesListView;
    private ApplianceAdapter applianceAdapter;  // L'adaptateur pour la liste des équipements

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        preferences = getSharedPreferences("Settings", MODE_PRIVATE);

        etName = findViewById(R.id.tvNom);
        etPrenom = findViewById(R.id.tvPrenom);
        etEmail = findViewById(R.id.tvEmail);
        etConsoTotale = findViewById(R.id.tvConsoTotale);  // Initialisation du TextView pour la consommation totale

        // Initialisation du ProgressDialog
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Chargement des informations...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        // Initialiser la liste des équipements
        appliances = new ArrayList<>();

        // Initialiser la ListView
        appliancesListView = findViewById(R.id.lvAppliances);
        applianceAdapter = new ApplianceAdapter(this, appliances);
        appliancesListView.setAdapter(applianceAdapter);

        // Afficher les informations actuelles du profil
        loadProfile();
    }

    // Méthode pour charger le profil depuis la base de données ou le serveur
    private void loadProfile() {
        pDialog.show();

        String emailConnection = getIntent().getStringExtra("user_email");
        Log.d(TAG, "Email reçu dans ProfilActivity : " + emailConnection);

        if (emailConnection == null || emailConnection.isEmpty()) {
            Toast.makeText(this, "Erreur : email non disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        String urlString = preferences.getString("serverAddress", "http://192.168.1.250") + "/powerhome/getHabitatFromEmail.php?email=" + emailConnection;
        Log.d(TAG, "URL de la requête : " + urlString);

        Ion.with(this)
                .load(urlString)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        pDialog.dismiss();

                        if (e != null) {
                            Log.d(TAG, "Erreur de connexion : " + e.getMessage());
                            Toast.makeText(ProfilActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (result == null || result.getResult() == null) {
                            Log.d(TAG, "Aucune réponse du serveur");
                            Toast.makeText(ProfilActivity.this, "Pas de réponse du serveur", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String response = result.getResult();
                        Log.d(TAG, "Réponse du serveur : " + response);

                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            if (jsonResponse.has("error")) {
                                String error = jsonResponse.getString("error");
                                Log.d(TAG, "Erreur serveur : " + error);
                                Toast.makeText(ProfilActivity.this, "Erreur : " + error, Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Extraction des données de l'utilisateur
                            JSONObject user = jsonResponse.getJSONObject("user");
                            String firstName = user.getString("firstName");
                            String lastName = user.getString("lastName");
                            String email = user.getString("email");

                            // Mise à jour des TextView avec les données extraites
                            etName.setText(lastName);
                            etPrenom.setText(firstName);
                            etEmail.setText(email);

                            // Vérification et récupération des équipements
                            if (jsonResponse.has("appliances")) {
                                JSONArray appliancesJsonArray = jsonResponse.getJSONArray("appliances");

                                // Nettoyer la liste avant de la remplir
                                appliances.clear();
                                int totalWattage = 0;  // Variable pour accumuler la consommation totale

                                // Ajouter les équipements à la liste et calculer la consommation totale
                                for (int i = 0; i < appliancesJsonArray.length(); i++) {
                                    JSONObject applianceJson = appliancesJsonArray.getJSONObject(i);
                                    String id = applianceJson.getString("id");
                                    String name = applianceJson.getString("name");
                                    String reference = applianceJson.getString("reference");
                                    String wattage = applianceJson.getString("wattage");

                                    // Créer un objet Appliance et l'ajouter à la liste
                                    Appliance appliance = new Appliance(id, name, reference, wattage);
                                    appliances.add(appliance);

                                    // Ajouter la puissance à la consommation totale
                                    totalWattage += Integer.parseInt(wattage);
                                }

                                // Mettre à jour l'adaptateur avec les nouveaux équipements
                                applianceAdapter.notifyDataSetChanged();

                                // Afficher la consommation totale
                                etConsoTotale.setText("Consommation Totale : " + totalWattage + " W");

                            } else {
                                Log.d(TAG, "Pas d'équipements trouvés.");
                            }

                        } catch (JSONException ex) {
                            Log.d(TAG, "Erreur de parsing JSON : " + ex.getMessage());
                            Toast.makeText(ProfilActivity.this, "Erreur de lecture des données", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
