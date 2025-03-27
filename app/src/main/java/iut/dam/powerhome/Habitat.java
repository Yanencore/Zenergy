package iut.dam.powerhome;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
public class Habitat {
    @SerializedName("id")
    private String id;

    @SerializedName("floor")
    private int etage;

    @SerializedName("area")
    private String area;

    @SerializedName("appliances")
    private List<Appliance> appliances;

    // Ajouter un champ name si nécessaire
    @SerializedName("residentName")
    private String residentName;

    public Habitat(String id, String name, int etage, String area, List<Appliance> appliances) {
        this.id = id;
        this.residentName = (name != null && !name.isEmpty()) ? name : "Nom non défini";  // Valeur par défaut
        this.etage = etage;
        this.area = area;
        this.appliances = appliances != null ? appliances : new ArrayList<>();
    }

    // Getter pour name
    public String getResidentName() {
        return residentName;
    }

    public String getId() {
        return id;
    }

    public int getEtage() {
        return etage;
    }

    public String getArea() {
        return area;
    }

    public List<Appliance> getAppliances() {
        return appliances;
    }

    public int getNbEquipements() {
        return appliances.size();
    }

    public static List<Habitat> getListFromJson(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Habitat>>() {}.getType();
        List<Habitat> list = gson.fromJson(json, type);

        if (list == null) {
            return new ArrayList<>();
        }

        for (Habitat habitat : list) {
            if (habitat.appliances == null) {
                habitat.appliances = new ArrayList<>();
            }
            Log.d("Habitat", "residentName: " + habitat.getResidentName() + ", Étage: " + habitat.getEtage() + ", Equipements: " + habitat.getAppliances());
        }

        return list;
    }

}
