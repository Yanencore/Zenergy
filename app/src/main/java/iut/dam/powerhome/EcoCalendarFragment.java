package iut.dam.powerhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class EcoCalendarFragment extends Fragment {

    private CalendarView calendarView;
    private Button btnReserver;
    private String selectedDate;
    private String userEmail;
    private final HashMap<String, Integer> data = new HashMap<>();

    public EcoCalendarFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_eco_calendar, container, false);

        calendarView = root.findViewById(R.id.calendarView);
        btnReserver = root.findViewById(R.id.btnReserver);

        userEmail = getActivity().getIntent().getStringExtra("user_email");

        // Init avec la date du jour
        selectedDate = formatDate(calendarView.getDate());

        // Clic sur une date du calendrier
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);

            if (data.containsKey(selectedDate)) {
                int pourcentage = data.get(selectedDate);
                String niveau = (pourcentage <= 30) ? "Vert" :
                        (pourcentage <= 70) ? "Orange" : "Rouge";

                Toast.makeText(getContext(),
                        selectedDate + " → " + pourcentage + "% (" + niveau + ")",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), selectedDate + " → aucune donnée", Toast.LENGTH_SHORT).show();
            }
        });

        // Clic bouton Réserver
        btnReserver.setOnClickListener(v -> {
            if (selectedDate == null || userEmail == null) {
                Toast.makeText(getContext(), "Données manquantes", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(getActivity(), ReservationActivity.class);
            intent.putExtra("user_email", userEmail);
            intent.putExtra("selected_date", selectedDate);
            startActivity(intent);
        });

        ImageView btnBack = root.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCalendarData(); // 🔁 Recharge les données du calendrier
    }

    private void loadCalendarData() {
        String url = Config.SERVER_IP + "/powerhome/getCalendarData.php";
        data.clear();
        Ion.with(this)
                .load(url)
                .asString()
                .setCallback((e, result) -> {
                    if (e != null) {
                        Toast.makeText(getContext(), "Erreur de chargement", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        JSONArray array = new JSONArray(result);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            String date = obj.getString("date");
                            int pourcentage = obj.getInt("pourcentage");
                            data.put(date, pourcentage);
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getContext(), "Erreur parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String formatDate(long millis) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(millis));
    }
}
