package com.dempseywood.service;

import com.dempseywood.model.AppState;
import com.dempseywood.model.EquipmentStatus;
import com.dempseywood.repository.EquipmentStatusRepository;
import com.dempseywood.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppStateService {

    @Autowired
    private EquipmentStatusRepository equipmentStatusRepository;

    public AppState getAppState(String imei){
        AppState state = new AppState();
        Date time = new Date();
        Date startOfDay = DateTimeUtil.getInstance().getTimeOfBeginningOfToday(time);
        List<EquipmentStatus> statusList = equipmentStatusRepository.findByTimestampAfterAndImeiOrderByTimestampDesc(startOfDay, imei);
        state.setCount(statusList.size() /2);
        if(statusList.isEmpty()){
            EquipmentStatus status = equipmentStatusRepository.findTopByImeiOrderByTimestampDesc(imei);
            state.setCount(0);
            state.setEquipment(status.getEquipment());
            state.setTask(status.getTask());
            state.setOperator(status.getOperator());
            state.setStatus("Unloaded");
        }else {
            EquipmentStatus lastStatus = statusList.get(statusList.size() - 1);
            state.setEquipment(lastStatus.getEquipment());
            state.setTask(lastStatus.getTask());
            state.setOperator(lastStatus.getOperator());
            state.setStatus(lastStatus.getStatus());
        }
        return state;

    }
}
