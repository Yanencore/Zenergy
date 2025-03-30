package iut.dam.powerhome;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

public class EcoCalendarFragment extends Fragment {

    private CalendarView calendarView;
    private final HashMap<String, Integer> data = new HashMap<>();

    public EcoCalendarFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_eco_calendar, container, false);
        calendarView = root.findViewById(R.id.calendarView);

        // Charger les données depuis le PHP
        loadCalendarData();

        // Réaction au clic sur une date
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);

            if (data.containsKey(date)) {
                int pourcentage = data.get(date);
                String niveau = (pourcentage <= 30) ? "Vert"
                        : (pourcentage <= 70) ? "Orange"
                        : "Rouge";

                Toast.makeText(getContext(),
                        date + " → " + pourcentage + "% (" + niveau + ")",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), date + " → aucune donnée", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView btnBack = root.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            requireActivity().finish();
        });


        return root;
    }

    private void loadCalendarData() {
        String url = Config.SERVER_IP + "/powerhome/getCalendarData.php";

        Ion.with(this)
                .load(url)
                .asString()
                .setCallback((e, result) -> {
                    if (e != null || result == null) {
                        Toast.makeText(getContext(), "Erreur de chargement des données", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        data.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject day = jsonArray.getJSONObject(i);
                            String date = day.getString("date");
                            int pourcentage = day.getInt("pourcentage");
                            data.put(date, pourcentage);
                        }

                    } catch (Exception ex) {
                        Log.e("EcoCalendar", "Erreur parsing JSON", ex);
                    }
                });
    }
}
