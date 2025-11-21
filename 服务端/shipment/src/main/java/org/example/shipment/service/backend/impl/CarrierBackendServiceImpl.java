package org.example.shipment.service.backend.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.shipment.dto.Carrier;
import org.example.shipment.raw.Shipment1118;
import org.example.shipment.service.backend.CarrierBackendService;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class CarrierBackendServiceImpl implements CarrierBackendService {
    @Value("${system.contract.shipment1118Address}")
    private String contractAddress;

    @Autowired
    private Client client;

    @Override
    public boolean addCarrier(String name, String info,String passwd) {
        Shipment1118 shipment = Shipment1118.load(contractAddress,client,client.getCryptoSuite().getCryptoKeyPair());
        TransactionReceipt transactionReceipt = shipment.addCarrier(name, info, passwd);
        return transactionReceipt.isStatusOK();
    }

    @Override
    public boolean checkCarrier(String id, String passwd) throws ContractException {
        Shipment1118 shipment = Shipment1118.load(contractAddress,client,client.getCryptoSuite().getCryptoKeyPair());
        return shipment.checkCarrier(new BigInteger(String.valueOf(id)),passwd);
    }

    @Override
    public BigInteger[] getShipmentsByCarrier(String id) throws ContractException {
        //1、获取合约实例
        Shipment1118 shipment = Shipment1118.load(contractAddress,client,client.getCryptoSuite().getCryptoKeyPair());
        //2、与合约交互
        List<BigInteger> transactionReceipt = shipment.getShipmentsByCarrier(new BigInteger(String.valueOf(id)));
        return transactionReceipt.toArray(new BigInteger[0]);
    }

    @Override
    public List<Carrier> getAllCarrier() {
        
        try {
            // 创建Shipment1118实例
            Shipment1118 shipment = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
            
            // 调用区块链的getAllCarriers方法
            org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple4<java.util.List<java.math.BigInteger>, java.util.List<java.lang.String>, java.util.List<java.lang.String>, java.util.List<java.lang.String>> result = 
                shipment.getAllCarriers();
            
            // 提取结果
            List<BigInteger> carrierIds = result.getValue1();
            List<String> carrierNames = result.getValue2();
            List<String> carrierInfos = result.getValue3();
            List<String> carrierPasswords = result.getValue4();
            
            // 构建Carrier对象列表
            List<Carrier> carrierList = new ArrayList<>();
            for (int i = 0; i < carrierIds.size(); i++) {
                Carrier carrier = new Carrier(carrierIds.get(i), carrierNames.get(i), carrierInfos.get(i), carrierPasswords.get(i));
                carrierList.add(carrier);
            }
            
            log.info("成功获取承运商列表，共{}个承运商", carrierList.size());
            return carrierList;
            
        } catch (Exception e) {
            log.error("获取承运商列表失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
        
    }

    @Override
    public Carrier getCarrierById(BigInteger id) {
        
        try {
            // 创建Shipment1118实例
            Shipment1118 shipment = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
            
            // 调用区块链的getCarrierById方法
            org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple4<java.math.BigInteger, java.lang.String, java.lang.String, java.lang.String> result = 
                shipment.getCarrierById(id);
            
            // 提取结果
            BigInteger carrierId = result.getValue1();
            String carrierName = result.getValue2();
            String carrierInfo = result.getValue3();
            String carrierPassword = result.getValue4();
            
            // 构建Carrier对象
            Carrier carrier = new Carrier(carrierId, carrierName, carrierInfo, carrierPassword);
            
            log.info("成功获取承运商信息，ID: {}, 名称: {}", carrierId, carrierName);
            return carrier;
            
        } catch (Exception e) {
            log.error("获取承运商信息失败，ID: {}, 错误: {}", id, e.getMessage(), e);
            return null;
        }
        
    }

    @Override
    public boolean deleteCarrier(BigInteger id) {
        try {
            // 创建Shipment1118实例
            Shipment1118 shipment = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
            
            // 调用区块链的deleteCarrier方法
            TransactionReceipt transactionReceipt = shipment.deleteCarrier(id);
            
            // 检查交易状态
            if (transactionReceipt.isStatusOK()) {
                log.info("成功删除承运商，ID: {}", id);
                return true;
            } else {
                log.error("删除承运商失败，ID: {}, 状态码: {}", id, transactionReceipt.getStatus());
                return false;
            }
            
        } catch (Exception e) {
            log.error("删除承运商失败，ID: {}, 错误: {}", id, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean updatePassword(BigInteger id, String passwd) {//updateCarrierPassword
        try {
            // 创建Shipment1118实例
            Shipment1118 shipment = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
            
            // 调用区块链的updateCarrierPassword方法
            TransactionReceipt transactionReceipt = shipment.updateCarrierPassword(id, passwd);
            
            // 检查交易状态
            if (transactionReceipt.isStatusOK()) {
                log.info("成功更新id为{}的承运商密码，", id);
                return true;
            } else {
                log.error("更新承运商密码失败，id: {}, 状态码: {}", id, transactionReceipt.getStatus());
                return false;
            }
            
        } catch (Exception e) {
            log.error("更新承运商密码失败，id: {}, 错误: {}", id, e.getMessage(), e);
            return false;
        }
    }
}
