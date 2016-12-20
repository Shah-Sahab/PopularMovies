package com.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Psych on 11/25/16.
 *
 * Keeps a Movie Trailer
 *
 * id: "533ec652c3a3685448000106",
 * iso_639_1: "en",
 * iso_3166_1: "US",
 * key: "_jGXcSBcvQQ",
 * name: "Trailer 1",
 * site: "YouTube",
 * size: 720,
 * type: "Trailer"
 */
public class Trailer implements Parcelable {

    public static final String _ID = "id";
    public static final String KEY = "key";
    public static final String NAME = "name";
    public static final String SITE = "site";
    public static final String SIZE = "size";
    public static final String TYPE = "type";
    public static final String RESULTS = "results";
    public static final String LANGUAGE = "iso_639_1";
    public static final String COUNTRY_CODE = "iso_3166_1";

    private int size;
    private String key;
    private String site;
    private String language;
    private String youtubeId;
    private String type;
    private String countryCode;
    private String trailerName;

    public Trailer() {
    }

    public Trailer(int size, String key, String site, String language, String youtubeId, String countryCode, String trailerName, String type) {
        this.size = size;
        this.key = key;
        this.site = site;
        this.language = language;
        this.youtubeId = youtubeId;
        this.countryCode = countryCode;
        this.trailerName = trailerName;
        this.type = type;
    }

    protected Trailer(Parcel in) {
        size = in.readInt();
        key = in.readString();
        site = in.readString();
        language = in.readString();
        youtubeId = in.readString();
        type = in.readString();
        countryCode = in.readString();
        trailerName = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public String toString() {
        return "Trailer{" +
                        "size=" + size +
                        ", key='" + key + '\'' +
                        ", site='" + site + '\'' +
                        ", language='" + language + '\'' +
                        ", youtubeId='" + youtubeId + '\'' +
                        ", countryCode='" + countryCode + '\'' +
                        ", trailerName='" + trailerName + '\'' +
                        '}';
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(size);
        parcel.writeString(key);
        parcel.writeString(site);
        parcel.writeString(language);
        parcel.writeString(youtubeId);
        parcel.writeString(countryCode);
        parcel.writeString(trailerName);
    }
}