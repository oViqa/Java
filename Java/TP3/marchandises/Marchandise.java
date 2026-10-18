package marchandises;

import java.util.Date;

public class Marchandise {
    private String code;
    private String description;
    private final Date dateAchat;

    public Marchandise(String code, String description) {
        this.code = code;
        this.description = description;
        this.dateAchat = new Date();
    }

    public Marchandise(String code) {
        this(code, "");
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Le produit \"" + code + "\" - \"" + description
                + "\" a ete achete le \"" + dateAchat + "\".";
    }
}