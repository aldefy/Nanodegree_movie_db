package techgravy.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushCallback;
import co.uk.rushorm.core.RushCore;

/**
 * Created by aditlal on 26/09/15.
 */
public class MovieVideoResultModel implements Rush , Parcelable {

    private String site;

    @SerializedName("id")
    private String mId;

    private String iso_639_1;

    private String name;

    private String type;

    private String key;

    private String size;

    public MovieVideoResultModel() {
    }

    protected MovieVideoResultModel(Parcel in) {
        site = in.readString();
        mId = in.readString();
        iso_639_1 = in.readString();
        name = in.readString();
        type = in.readString();
        key = in.readString();
        size = in.readString();
    }

    public static final Creator<MovieVideoResultModel> CREATOR = new Creator<MovieVideoResultModel>() {
        @Override
        public MovieVideoResultModel createFromParcel(Parcel in) {
            return new MovieVideoResultModel(in);
        }

        @Override
        public MovieVideoResultModel[] newArray(int size) {
            return new MovieVideoResultModel[size];
        }
    };

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getmId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ClassPojo [site = " + site + ", mId = " + mId + ", iso_639_1 = " + iso_639_1 + ", name = " + name + ", type = " + type + ", key = " + key + ", size = " + size + "]";
    }

    @Override
    public void save() {
        RushCore.getInstance().save(this);
    }

    @Override
    public void save(RushCallback callback) {
        RushCore.getInstance().save(this, callback);
    }

    @Override
    public void delete() {
        RushCore.getInstance().delete(this);
    }

    @Override
    public void delete(RushCallback callback) {
        RushCore.getInstance().delete(this, callback);
    }

    @Override
    public String getId() {
        return RushCore.getInstance().getId(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(site);
        dest.writeString(mId);
        dest.writeString(key);
    }
}
