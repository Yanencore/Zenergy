package iut.dam.powerhome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.d(TAG, "Lancement de RegisterActivity");

        Spinner spinnerCountryCode = findViewById(R.id.spinnerCountryCode);
        EditText etPhone = findViewById(R.id.etPhone);
        EditText etPrenom = findViewById(R.id.etPrenom);
        EditText etNom = findViewById(R.id.etNom);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnRegister = findViewById(R.id.btnRegister);

        // Code PAYS
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.country,
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerCountryCode.setAdapter(adapter);

        // Choix PAYS
        spinnerCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCode = parent.getItemAtPosition(position).toString();
                etPhone.setHint(selectedCode + " Mobile");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ImageView ivBack = findViewById(R.id.ivBack);

        if (ivBack == null) {
            Log.e(TAG, "Erreur : ivBack est null, vérifie son ID dans activity_register.xml");
        } else {
            ivBack.setOnClickListener(v -> {
                Log.d(TAG, "Retour à LoginActivity");
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
        }

        // Validation
        btnRegister.setOnClickListener(v -> {
            String username = etPrenom.getText().toString().trim();
            String nom = etNom.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String countryCode = spinnerCountryCode.getSelectedItem().toString();

            // Fusionner le code pays avec le numéro
            String fullPhone = countryCode + " " + phone;

            // VERIFICATION
            if (username.isEmpty() || nom.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Avertissement : Un champ est vide !");
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                Toast.makeText(RegisterActivity.this, "Adresse email invalide", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Erreur : Email invalide " + email);
                return;
            }

            if (password.length() < 8) {
                Toast.makeText(RegisterActivity.this, "Le mot de passe doit avoir au moins 8 caractères", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Erreur : Mot de passe trop court !");
                return;
            }
            if (phone.length() < 10) {
                Toast.makeText(RegisterActivity.this, "Le numero doit etre composé de 10 chiffre", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Erreur : Numero trop court !");
                return;
            }


            Snackbar snackbar = Snackbar.make(v, "Inscription réussie", Snackbar.LENGTH_LONG);
            snackbar.setAction("ANNULER", view -> {
                Toast.makeText(RegisterActivity.this, "Annulation de l'inscription", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Annulation de l'inscription");
            });
            snackbar.show();

            Log.i(TAG, "Inscription réussie avec : " + username + " " + nom + ", Email: " + email + ", Téléphone: " + fullPhone);


            Intent intent = new Intent(RegisterActivity.this, AccueilActivity.class);
            intent.putExtra("USERNAME", username);
            intent.putExtra("PASSWORD", password);
            startActivity(intent);
            finish();
        });

    }

}
