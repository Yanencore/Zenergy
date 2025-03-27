package iut.dam.powerhome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Spinner spinner;
    private SharedPreferences preferences;
    private EditText etIdentifiant;
    private EditText etMotDePasse;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadLocale();

        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("Settings", MODE_PRIVATE);

        etIdentifiant = findViewById(R.id.etIdentifiant);
        etMotDePasse = findViewById(R.id.etMotDePasse);
        spinner = findViewById(R.id.spinnerCountry);

        List<Country> items = new ArrayList<>();
        items.add(new Country(R.drawable.drapeau_france, "France", "fr"));
        items.add(new Country(R.drawable.drapeau_royaume_uni, "United Kingdom", "en"));
        items.add(new Country(R.drawable.drapeau_espagne, "Spain", "es"));

        CountryAdapter adapter = new CountryAdapter(this, R.layout.item_country, items);
        spinner.setAdapter(adapter);

        String currentLang = preferences.getString("My_Lang", "fr");
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getLanguageCode().equals(currentLang)) {
                spinner.setSelection(i);
                break;
            }
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country selectedCountry = (Country) parent.getItemAtPosition(position);
                String selectedLanguage = selectedCountry.getLanguageCode();
                String savedLanguage = preferences.getString("My_Lang", "fr");

                if (!selectedLanguage.equals(savedLanguage)) {
                    changeLanguage(selectedLanguage);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        TextView tvCreerUn = findViewById(R.id.tvCreerCompte);
        tvCreerUn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        Button btnConnexion = findViewById(R.id.btnConnexion);
        btnConnexion.setOnClickListener(v -> loginUser());

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Connexion en cours...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
    }

    private void loginUser() {
        String email = etIdentifiant.getText().toString().trim();
        String password = etMotDePasse.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        pDialog.show();

        String urlString = "http://192.168.1.250/powerhome/login.php?email=" + email + "&password=" + password;
        http://localhost/powerhome/login.php?email=aelbaasi@gmail.com&password=1234azer!
        Ion.with(this)
                .load(urlString)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        pDialog.dismiss();

                        if (result == null || result.getResult() == null) {
                            Log.d(TAG, "Erreur : Aucune réponse du serveur");
                            Toast.makeText(LoginActivity.this, "Erreur de connexion au serveur", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String response = result.getResult();
                        Log.d(TAG, "Réponse du serveur : " + response);

                        if (!response.equals("\"incorrect email or password !\"")) {
                            Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, AccueilActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void changeLanguage(String languageCode) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("My_Lang", languageCode);
        editor.apply();

        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        finish();
        startActivity(getIntent());
    }

    private void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "fr");
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}
