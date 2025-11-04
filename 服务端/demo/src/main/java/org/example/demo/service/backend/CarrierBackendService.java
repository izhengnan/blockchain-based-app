package org.example.demo.service.backend;

import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

public interface CarrierBackendService {
    boolean addCarrier(String name,String info,String passwd);
    //验证
    boolean checkCarrier(int id,String passwd) throws ContractException;
}
