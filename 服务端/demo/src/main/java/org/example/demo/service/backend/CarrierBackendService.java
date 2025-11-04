package org.example.demo.service.backend;

import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

import java.math.BigInteger;

public interface CarrierBackendService {
    boolean addCarrier(String name,String info,String passwd);
    boolean checkCarrier(int id,String passwd) throws ContractException;
    BigInteger[] getShipmentsByCarrier(int id) throws ContractException;
}
