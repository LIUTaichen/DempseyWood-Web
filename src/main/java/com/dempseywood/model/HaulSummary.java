package com.dempseywood.model;

public class HaulSummary extends Haul{
    private Integer loadCount = 0;

    public Integer getLoadCount() {
        return loadCount;
    }

    public void setLoadCount(Integer loadCount) {
        this.loadCount = loadCount;
    }
}
