package com.dempseywood.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VersionCheckService {

    @Value("${config.supported.version.min}")
    private int minVersion;

    public boolean isUpdateNeeded(int clientVersion){

        if(minVersion > clientVersion){
            return true;
        }else{
            return false;
        }
    }
}
