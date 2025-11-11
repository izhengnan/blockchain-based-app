package org.example.demo.service.backend.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.demo.raw.Shipment;
import org.example.demo.service.backend.CarrierBackendService;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Slf4j
@Service
public class ICarrierBackendService implements CarrierBackendService {
    @Value("${system.contract.shipmentAddress}")
    private String contractAddress;

    @Autowired
    private Client client;

    @Override
    public boolean addCarrier(String name, String info,String passwd) {
        Shipment shipment = Shipment.load(contractAddress,client,client.getCryptoSuite().getCryptoKeyPair());
        TransactionReceipt transactionReceipt = shipment.addCarrier(name, info, passwd);
        return transactionReceipt.isStatusOK();
    }

    @Override
    public boolean checkCarrier(int id, String passwd) throws ContractException {
        Shipment shipment = Shipment.load(contractAddress,client,client.getCryptoSuite().getCryptoKeyPair());
        return shipment.checkCarrier(new BigInteger(String.valueOf(id)),passwd);
    }

    @Override
    public BigInteger[] getShipmentsByCarrier(int id) throws ContractException {
        //1、获取合约实例
        Shipment shipment = Shipment.load(contractAddress,client,client.getCryptoSuite().getCryptoKeyPair());
        //2、与合约交互
        List<BigInteger> transactionReceipt = shipment.getShipmentsByCarrier(new BigInteger(String.valueOf(id)));
        return transactionReceipt.toArray(new BigInteger[0]);
    }
}
