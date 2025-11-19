package org.example.demo.service.backend.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.demo.raw.Shipment;
import org.example.demo.service.backend.UserBackendService;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.request.Transaction;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Slf4j
@Service
public class IUserBackendService implements UserBackendService {

    @Value("${system.contract.shipmentAddress}")
    private String contractAddress;

    @Autowired
    private Client client;

    @Override
    public boolean addUser(String name, String passwd) {
        log.info("addUser: name:{},passwd:{}", name, passwd);
        Shipment shipment = Shipment.load(contractAddress,client,client.getCryptoSuite().getCryptoKeyPair());
        TransactionReceipt transactionReceipt = shipment.addUser(name, passwd);
        log.info(transactionReceipt.getStatus());
        return transactionReceipt.isStatusOK();
    }

    @Override
    public boolean checkUser(int id, String passwd) throws ContractException {
        log.info("checkUser: id:{},passwd:{}", id, passwd);
//        Shipment shipment = Shipment.load(contractAddress,client,client.getCryptoSuite().getCryptoKeyPair());
//        return shipment.checkUser(new BigInteger(String.valueOf(id)),passwd);
        Shipment shipment = Shipment.load(contractAddress,client,client.getCryptoSuite().getCryptoKeyPair());
        Boolean transactionReceipt = shipment.checkUser(new BigInteger(String.valueOf(id)),passwd);
        return transactionReceipt;

    }
}
