package org.Indexer;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;

public class linkData {

    private String URL;
    private String TITLE;
    private String KEYWORDS;
    private String[] STEMMED_KEYWORDS;
    private double popularity;



//constructor
    public linkData(String URL, String TITLE, String KEYWORDS, String STEMMED_KEYWORDS){
        this.URL=URL;
        this.TITLE=TITLE;
        this.KEYWORDS=KEYWORDS;
        this.STEMMED_KEYWORDS=STEMMED_KEYWORDS.split(" ");


    }

//    Getters and Setters
    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getKEYWORDS() {
        return KEYWORDS;
    }

    public void setKEYWORDS(String KEYWORDS) {
        this.KEYWORDS = KEYWORDS;
    }

    public String[] getSTEMMED_KEYWORDS() {
        return STEMMED_KEYWORDS;
    }

    public void setSTEMMED_KEYWORDS(String[] STEMMED_KEYWORDS) {
        this.STEMMED_KEYWORDS = STEMMED_KEYWORDS;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }
}
