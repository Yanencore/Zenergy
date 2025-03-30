package iut.dam.powerhome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AccueilActivity extends AppCompatActivity {

    private static final String TAG = "AccueilActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);

        Log.d(TAG, "Lancement de AccueilActivity");

        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        String password = intent.getStringExtra("PASSWORD");

        // Récup l'email du Bundle
        String email = intent.getStringExtra("user_email");

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        if (username != null) {
            tvWelcome.setText("Bienvenue, " + username + " !\nVotre mot de passe est : " + password);
        } else {
            tvWelcome.setText("Bienvenue sur votre profil !");
        }

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Déconnexion de l'utilisateur");

                Intent intent = new Intent(AccueilActivity.this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button imglistes = findViewById(R.id.btnliste);
        imglistes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Ouverture de HabitatActivity");
                Intent intent = new Intent(AccueilActivity.this, HabitatActivity.class);
                startActivity(intent);
            }
        });

        Button profil = findViewById(R.id.btnProfile);
        profil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Ouverture de ProfilActivity");

                // Passer l'email à ProfilActivity via Intent
                Intent intent = new Intent(AccueilActivity.this, ProfilActivity.class);
                intent.putExtra("user_email", email);
                startActivity(intent);
            }
        });

        Button btnListe = findViewById(R.id.btnliste);
        btnListe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Ouverture de HabitatActivity");

                // Passer l'email à ProfilActivity via Intent
                Intent intent = new Intent(AccueilActivity.this, HabitatActivity.class);
                intent.putExtra("user_email", email);
                startActivity(intent);
            }
        });

        // Bouton vers le calendrier éco
        Button btnEcoCalendar = findViewById(R.id.btnEcoCalendar);
        btnEcoCalendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Ouverture de EcoCalanderActivity");

                // Passer l'email à ProfilActivity via Intent
                Intent intent = new Intent(AccueilActivity.this, EcoCalendarActivity.class);
                intent.putExtra("user_email", email);
                startActivity(intent);
            }
        });

    }
}
