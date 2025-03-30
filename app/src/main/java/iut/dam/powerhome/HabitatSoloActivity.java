package iut.dam.powerhome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

public class HabitatSoloActivity extends Activity {
    private static final String TAG = "HabitatSoloActivity";

    private TextView nomResident, nbEquipements, etage;
    private ImageView icone1, icone2, icone3, icone4;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_habitat);

        // Initialisation des vues
        nomResident = findViewById(R.id.nomResident);
        nbEquipements = findViewById(R.id.nbEquipements);
        etage = findViewById(R.id.etage);
        icone1 = findViewById(R.id.icone1);
        icone2 = findViewById(R.id.icone2);
        icone3 = findViewById(R.id.icone3);
        icone4 = findViewById(R.id.icone4);

        // ProgressDialog pour afficher le chargement
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Chargement des données...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        // Récupérer le token de l'utilisateur
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String token = preferences.getString("token", "");

        Log.d(TAG, "Token récupéré : " + token);

        if (token.isEmpty()) {
            Log.e(TAG, "Erreur : Aucun token trouvé !");
            Toast.makeText(this, "Erreur : Aucun token trouvé", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Charger les données du compte connecté
        getUserHabitat(token);
    }

    private void getUserHabitat(String token) {
        pDialog.show();
        String url = Config.SERVER_IP + "/powerhome/gethabitat2.php?token=" + token;

        Log.d(TAG, "URL de la requête : " + url);

        Ion.with(this)
                .load(url)
                .asString()
                .setCallback((e, result) -> {
                    pDialog.dismiss();

                    if (e != null || result == null) {
                        Log.e(TAG, "Erreur réseau ou réponse invalide", e);
                        Toast.makeText(this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Log.d(TAG, "Réponse JSON : " + result);
                    parseJsonResponse(result);
                });
    }

    private void parseJsonResponse(String json) {
        try {
            JSONArray habitatsArray = new JSONArray(json);
            if (habitatsArray.length() == 0) {
                Log.e(TAG, "Aucun habitat trouvé !");
                Toast.makeText(this, "Aucun habitat trouvé", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject habitatObj = habitatsArray.getJSONObject(0);

            // Extraction des données JSON
            String residentName = habitatObj.optString("resident_name", "Inconnu");
            int nbEquipementsVal = habitatObj.optInt("nb_equipements", 0);
            int etageVal = habitatObj.optInt("etage", 0);
            JSONArray appliancesArray = habitatObj.optJSONArray("appliances");

            Log.d(TAG, "Habitat trouvé : " + residentName + ", Étage : " + etageVal);

            // Mise à jour de l'interface utilisateur
            updateUI(residentName, nbEquipementsVal, etageVal, appliancesArray);

        } catch (Exception e) {
            Log.e(TAG, "Erreur de parsing JSON", e);
            Toast.makeText(this, "Erreur interne, veuillez réessayer", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(String residentName, int nbEquipementsVal, int etageVal, JSONArray appliancesArray) {
        nomResident.setText(residentName);
        nbEquipements.setText(nbEquipementsVal + " appareils");
        etage.setText("Étage : " + etageVal);

        // Initialiser toutes les icônes comme invisibles
        ImageView[] icons = {icone1, icone2, icone3, icone4};
        for (ImageView icon : icons) {
            icon.setVisibility(ImageView.GONE);
        }

        // Vérification des équipements
        if (appliancesArray != null) {
            int count = Math.min(appliancesArray.length(), icons.length);
            for (int i = 0; i < count; i++) {
                String applianceName = appliancesArray.optString(i, "");
                icons[i].setVisibility(ImageView.VISIBLE);

                switch (applianceName) {
                    case "Machine a laver":
                        icons[i].setImageResource(R.drawable.ic_laundry);
                        break;
                    case "Aspirateur":
                        icons[i].setImageResource(R.drawable.ic_cleaner);
                        break;
                    case "Climatiseur":
                        icons[i].setImageResource(R.drawable.ic_clim);
                        break;
                    case "Fer a repasser":
                        icons[i].setImageResource(R.drawable.ic_iron);
                        break;
                    default:
                        icons[i].setVisibility(ImageView.GONE);
                        break;
                }
            }
        }
    }
}
