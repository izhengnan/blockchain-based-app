package org.example.shipment.service.backend;

import org.example.shipment.dto.Carrier;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

import java.math.BigInteger;
import java.util.List;

public interface CarrierBackendService {
    boolean addCarrier(String name,String info,String passwd);
    boolean checkCarrier(String id,String passwd) throws ContractException;
    BigInteger[] getShipmentsByCarrier(String id) throws ContractException;

    List<Carrier> getAllCarrier();

    Carrier getCarrierById(BigInteger id);

    boolean deleteCarrier(BigInteger id);

    boolean updatePassword(BigInteger id, String passwd);
}
