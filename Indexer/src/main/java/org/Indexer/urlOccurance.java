package org.Indexer;

class urlOccurrence {
    private String urlString;
    private int occurrences;

    public urlOccurrence(String urlString, int occurrences) {
        this.urlString = urlString;
        this.occurrences = occurrences;
    }

    public String getUrlString() {
        return urlString;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }
}