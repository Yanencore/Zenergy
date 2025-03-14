package iut.dam.powerhome;

import java.util.List;

public class Habitat {
    private String nom;
    private int nbEquipements;
    private int etage;
    private List<Integer> equipementsIcons;

    public Habitat(String nom, int nbEquipements, int etage, List<Integer> equipementsIcons) {
        this.nom = nom;
        this.nbEquipements = nbEquipements;
        this.etage = etage;
        this.equipementsIcons = equipementsIcons;
    }

    public String getNom() {
        return nom;
    }

    public int getNbEquipements() {
        return nbEquipements;
    }

    public int getEtage() {
        return etage;
    }

    public List<Integer> getEquipementsIcons() {
        return equipementsIcons;
    }
}
