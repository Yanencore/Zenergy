package iut.dam.powerhome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Efface l'historique des activités
                startActivity(intent);
                finish(); // Ferme l'activité actuelle
            }
        });

        Button btnSettings = findViewById(R.id.btnProfile);
        btnSettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Ouverture de HabitatActivity");
                Intent intent = new Intent(AccueilActivity.this, HabitatActivity.class);
                startActivity(intent);
            }
        });

    }
}
