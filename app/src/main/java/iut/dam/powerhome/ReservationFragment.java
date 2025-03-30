package iut.dam.powerhome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;

import java.util.Arrays;

public class ReservationFragment extends Fragment {

    private Spinner spinnerEquipement, spinnerHeure;
    private Button btnReserver;
    private TextView txtDate;
    private String selectedDate = null;
    private String userEmail;

    public ReservationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reservation, container, false);

        spinnerEquipement = root.findViewById(R.id.spinnerEquipement);
        spinnerHeure = root.findViewById(R.id.spinnerHeure);
        btnReserver = root.findViewById(R.id.btnReserver);
        txtDate = root.findViewById(R.id.txtDate);

        userEmail = getActivity().getIntent().getStringExtra("user_email");
        selectedDate = getActivity().getIntent().getStringExtra("selected_date");

        if (userEmail == null || selectedDate == null) {
            Toast.makeText(getContext(), "Erreur : données manquantes", Toast.LENGTH_SHORT).show();
            return root;
        }

        txtDate.setText("Date sélectionnée : " + selectedDate);

        loadEquipementsPerso(userEmail);

        String[] heures = {"08:00", "09:00", "10:00", "11:00", "14:00", "16:00"};
        ArrayAdapter<String> heureAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Arrays.asList(heures));
        spinnerHeure.setAdapter(heureAdapter);

        btnReserver.setOnClickListener(v -> {
            String equipement = spinnerEquipement.getSelectedItem().toString();
            String heure = spinnerHeure.getSelectedItem().toString();
            String creneau = selectedDate + " " + heure + ":00";

            String url = Config.SERVER_IP + "/powerhome/reserve.php";

            Ion.with(this)
                    .load(url)
                    .setBodyParameter("email", userEmail)
                    .setBodyParameter("equipement", equipement)
                    .setBodyParameter("creneau", creneau)
                    .asString()
                    .setCallback((e, result) -> {
                        if (e != null) {
                            Toast.makeText(getContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (result.contains("success")) {
                            Toast.makeText(getContext(), "Réservation réussie ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Échec  : " + result, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        Button btnRetour = root.findViewById(R.id.btnRetour);
        btnRetour.setOnClickListener(v -> {
            requireActivity().finish();
        });

        return root;
    }


    private void loadEquipementsPerso(String email) {
        String url = Config.SERVER_IP + "/powerhome/getUserAppliances.php";

        Ion.with(this)
                .load(url)
                .setBodyParameter("email", email)
                .asString()
                .setCallback((e, result) -> {
                    if (e != null) {
                        Toast.makeText(getContext(), "Erreur chargement équipements ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        JSONArray array = new JSONArray(result);
                        String[] equipements = new String[array.length()];
                        for (int i = 0; i < array.length(); i++) {
                            equipements[i] = array.getString(i);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, equipements);
                        spinnerEquipement.setAdapter(adapter);

                    } catch (Exception ex) {
                        Toast.makeText(getContext(), "Erreur JSON", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
