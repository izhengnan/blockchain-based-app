package org.example.shipment.service.backend.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.shipment.dto.Admin;
import org.example.shipment.dto.LoginInfo;
import org.example.shipment.raw.Shipment1118;
import org.example.shipment.service.backend.AdminBackendService;
import org.example.shipment.utils.JwtUtils;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AdminBackendServiceImpl implements AdminBackendService {

    @Value("${system.contract.shipment1118Address}")
    private String contractAddress;

    @Autowired
    private Client client;

    @Override
    public boolean addAdmin(String account, String passwd) {
        log.info("addAdmin: account:{}, passwd:{}", account, passwd);
        Shipment1118 shipment = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
        TransactionReceipt transactionReceipt = shipment.addAdmin(account, passwd);
        log.info("addAdmin transaction status: {}", transactionReceipt.getStatus());
        return transactionReceipt.isStatusOK();
    }

    @Override
    public LoginInfo checkAdmin(String account, String passwd) throws ContractException {
        log.info("checkAdmin: account:{}, passwd:{}", account, passwd);
        Shipment1118 shipment = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
        Boolean result = shipment.checkAdmin(account, passwd);
        
        if (result) {
            // 构建JWT声明
            Map<String, Object> claims = new HashMap<>();
            claims.put("account", account);
            claims.put("userType", "admin");
            
            // 生成令牌
            String token = JwtUtils.generateJwt(claims);
            
            // 构建并返回LoginInfo对象，只包含token、account和userType
            return new LoginInfo(token, null, account, null, "admin");
        } else {
            // 管理员验证失败，返回null
            log.warn("管理员验证失败: account:{}", account);
            return null;
        }
    }

    @Override
    public boolean updateAdminPassword(String account, String newPasswd) {
        log.info("updateAdminPassword: account:{}, newPasswd:{}", account, newPasswd);
        Shipment1118 shipment = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
        TransactionReceipt transactionReceipt = shipment.updateAdminPassword(account, newPasswd);
        log.info("updateAdminPassword transaction status: {}", transactionReceipt.getStatus());
        return transactionReceipt.isStatusOK();
    }


    @Override
    public List<Admin> listAdmin() {
        try {
            Shipment1118 shipment = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
            org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3<List<BigInteger>, List<String>, List<String>> tuple3 = 
                    shipment.getAllAdmins();
            
            List<BigInteger> ids = tuple3.getValue1();
            List<String> accounts = tuple3.getValue2();
            List<String> passwds = tuple3.getValue3();
            
            List<Admin> adminList = new java.util.ArrayList<>();
            for (int i = 0; i < accounts.size(); i++) {
                Admin admin = new Admin();
                admin.setAccount(accounts.get(i));
                admin.setPasswd(passwds.get(i));
                adminList.add(admin);
            }
            
            log.info("listAdmin: found {} admins", adminList.size());
            return adminList;
            
        } catch (Exception e) {
            log.error("listAdmin error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to list admins", e);
        }
    }
}