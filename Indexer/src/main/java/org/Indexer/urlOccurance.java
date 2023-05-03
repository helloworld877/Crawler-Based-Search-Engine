package org.Indexer;

class urlOccurrence {
    private String urlString;
    private int occurrences;
    private int from;
    private int to;

    public urlOccurrence(String urlString, int occurrences,Integer from,Integer to) {
        this.urlString = urlString;
        this.occurrences = occurrences;
        if(from == null) {
            this.from=0;
        }
        else{
            this.from=from;
        }

        if(to==null)
        {
            this.to=0;
        }
        else{
            this.to=to;

        }
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

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }
}