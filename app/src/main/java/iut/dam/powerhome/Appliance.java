package iut.dam.powerhome;

import com.google.gson.annotations.SerializedName;

public class Appliance {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("reference")
    private String reference;

    @SerializedName("wattage")
    private String wattage;

    public Appliance(String id, String name, String reference, String wattage) {
        this.id = id;
        this.name = name;
        this.reference = reference;
        this.wattage = wattage;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReference() {
        return reference;
    }

    public String getWattage() {
        return wattage;
    }

    @Override
    public String toString() {
        return "Appliance{" +
                "id='" + id + '\'' +
                ", residentName='" + name + '\'' +
                ", reference='" + reference + '\'' +
                ", wattage='" + wattage + '\'' +
                '}';
    }
}
