package techgravy.popularmovies.models;

/**
 * Created by aditlal on 26/09/15.
 */
public class MovieProductionCompanies {
    private String name;

    private String iso_3166_1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    @Override
    public String toString() {
        return "ClassPojo [name = " + name + ", iso_3166_1 = " + iso_3166_1 + "]";
    }
}
