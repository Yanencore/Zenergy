package iut.dam.powerhome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Spinner spinner;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        loadLocale();

        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("Settings", MODE_PRIVATE);

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

        Button btnDeconnexion = findViewById(R.id.btnConnexion);
        if (btnDeconnexion != null) {
            btnDeconnexion.setOnClickListener(v -> {
                Log.d(TAG, "Déconnexion de l'utilisateur");
                Intent intent = new Intent(LoginActivity.this, AccueilActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        } else {
            Log.e(TAG, "Erreur : btnConnexion introuvable dans activity_login.xml");
        }
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

        // Redémarrer l'activité uniquement si la langue a changé
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

    public void openFacebook(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com"));
        startActivity(intent);
    }
}
