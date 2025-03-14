package iut.dam.powerhome;

public class Country {
    private int flagResource;
    private String name;
    private String languageCode;

    public Country(int flagResource, String name, String languageCode) {
        this.flagResource = flagResource;
        this.name = name;
        this.languageCode = languageCode;
    }

    public int getFlagResource() {
        return flagResource;
    }

    public String getName() {
        return name;
    }

    public String getLanguageCode() {
        return languageCode;
    }
}
