package com.dempseywood.navixy.reading;

import com.dempseywood.model.locationbased.Reading;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

    private boolean success;
    @JsonProperty("limit_exceeded")
    private boolean limitExceeded;
    private List<Reading> list;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isLimitExceeded() {
        return limitExceeded;
    }

    public void setLimitExceeded(boolean limitExceeded) {
        this.limitExceeded = limitExceeded;
    }

    public List<Reading> getList() {
        return list;
    }

    public void setList(List<Reading> list) {
        this.list = list;
    }
}
