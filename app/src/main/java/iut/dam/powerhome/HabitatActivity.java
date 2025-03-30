package iut.dam.powerhome;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;
import java.util.List;

public class HabitatActivity extends Activity {
    private ListView listView;
    private HabitatAdapter adapter;
    private List<Habitat> habitants = new ArrayList<>();
    private ProgressDialog pDialog;
    private boolean isAdapterInitialized = false;
    private static final String TAG = "HabitatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitat);

        listView = findViewById(R.id.listViewHabitats);
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Getting list of habitats...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        getRemoteHabitats();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Habitat habitat = habitants.get(position);
            Toast.makeText(HabitatActivity.this, "Résident: " + habitat.getResidentName(), Toast.LENGTH_SHORT).show();
        });
    }

    public void getRemoteHabitats() {
        pDialog.show();

        String urlString = Config.SERVER_IP + "/powerhome/getHabitats_v2.php?token=6a81470661821d7ff232962aded97c41";
        Ion.with(this)
                .load(urlString)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        pDialog.dismiss();
                        if (result == null || result.getResult() == null) {
                            Log.d(TAG, "No response from the server!!!");
                            return;
                        }

                        String json = result.getResult();
                        Log.d(TAG, "Réponse JSON reçue : " + json);

                        habitants.clear();
                        habitants.addAll(Habitat.getListFromJson(json));

                        for (Habitat h : habitants) {
                            Log.d(TAG, "Habitat récupéré : residentName: " + h.getResidentName() + ", Étage: " + h.getEtage() + ", Equipements: " + h.getNbEquipements());
                        }

                        initializeAdapter();
                    }
                });
    }

    private void initializeAdapter() {
        if (!isAdapterInitialized && !habitants.isEmpty()) {
            adapter = new HabitatAdapter(this, habitants);
            listView.setAdapter(adapter);
            isAdapterInitialized = true;
        } else if (isAdapterInitialized) {
            adapter.notifyDataSetChanged();
        }
    }
}
