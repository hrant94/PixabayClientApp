package com.hrant.pixabayclientapp.model;

import java.util.List;

public class PixabayResponseModel {
    private int totalHits;
    private List<PixabayImageModel> hits;
    private int total;

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public List<PixabayImageModel> getHits() {
        return hits;
    }

    public void setHits(List<PixabayImageModel> hits) {
        this.hits = hits;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
