package com.dempseywood.webservice.geofence;

public class Count {
    private String material;
    private Integer count;

    public Count(){

    }

    public Count(String material, Integer count) {
        this.material = material;
        this.count = count;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
