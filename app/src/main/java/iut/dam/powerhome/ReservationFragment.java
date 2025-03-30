package iut.dam.powerhome;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.koushikdutta.ion.Ion;

import java.util.Arrays;
import java.util.List;

public class ReservationFragment extends Fragment {
    private Spinner spinnerEquipement, spinnerHeure;
    private Button btnReserver;
    private String userEmail;

    public ReservationFragment() {}

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reservation, container, false);

        spinnerEquipement = root.findViewById(R.id.spinnerEquipement);
        spinnerHeure = root.findViewById(R.id.spinnerHeure);
        btnReserver = root.findViewById(R.id.btnReserver);

        // Valeurs factices
        List<String> heures = Arrays.asList("08:00", "10:00", "14:00", "19:00", "22:00");
        List<String> equipements = Arrays.asList("Machine a laver", "Aspirateur", "Climatiseur", "Fer à repasser");

        spinnerEquipement.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, equipements));
        spinnerHeure.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, heures));

        userEmail = requireActivity().getIntent().getStringExtra("user_email");

        btnReserver.setOnClickListener(v -> {
            String equipement = spinnerEquipement.getSelectedItem().toString();
            String heure = spinnerHeure.getSelectedItem().toString();
            reserverCreneau(equipement, heure);
        });

        return root;
    }

    private void reserverCreneau(String equipement, String heure) {
        String url =  Config.SERVER_IP + "/powerhome/reserve.php";
        String dateHeure = "2025-03-30 " + heure;

        Ion.with(this)
                .load("POST", url)
                .setBodyParameter("email", userEmail)
                .setBodyParameter("equipement", equipement)
                .setBodyParameter("creneau", dateHeure)
                .asString()
                .setCallback((e, result) -> {
                    if (e != null || result == null) {
                        Toast.makeText(getContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Réservation : " + result, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
