package techgravy.popularmovies.models;

/**
 * Created by aditlal on 26/09/15.
 */
public class MovieProductionCountries {

    private String name;
    private String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", name = " + name + "]";
    }

}
