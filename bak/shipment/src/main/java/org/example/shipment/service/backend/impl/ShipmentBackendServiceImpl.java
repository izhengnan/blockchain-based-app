package org.example.shipment.service.backend.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.shipment.dto.Shipment;
import org.example.shipment.raw.Shipment1118;
import org.example.shipment.service.backend.ShipmentBackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.model.TransactionReceipt;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ShipmentBackendServiceImpl implements ShipmentBackendService {
    @Value("${system.contract.shipment1118Address}")
    private String contractAddress;

    @Autowired
    private Client client;

    @Override
    public boolean addShipment(BigInteger userId, BigInteger carrier, String from, String to) {
        log.info("添加运输记录：userId={},carrier={},from={},to={}", userId, carrier, from, to);
        try {
            Shipment1118 shipment1118 = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
            TransactionReceipt receipt = shipment1118.addShipment(userId, carrier, from, to);
            log.info("添加运输记录交易成功，交易哈希：{}，status={}", receipt.getTransactionHash(), receipt.getStatus());
            return true;
        } catch (Exception e) {
            log.error("添加运输记录失败：{}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteShipmentById(BigInteger id) {//deleteShipment
        log.info("删除运输记录：id={}", id);
        try {
            Shipment1118 shipment1118 = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
            TransactionReceipt receipt = shipment1118.deleteShipment(id);
            log.info("删除运输记录交易成功，交易哈希：{}", receipt.getTransactionHash());
            return true;
        } catch (Exception e) {
            log.error("删除运输记录失败：{}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateShipment(BigInteger id, BigInteger carrierId, String status) {
        log.info("更新运输记录：id={}, carrierId={}, status={}", id, carrierId, status);
        try {
            Shipment1118 shipment1118 = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
            TransactionReceipt receipt = shipment1118.updateShipment(carrierId, id, status);
            log.info("更新运输记录交易成功，交易哈希：{}", receipt.getTransactionHash());
            return true;
        } catch (Exception e) {
            log.error("更新运输记录失败：{}", e.getMessage());
            return false;
        }
    }

    @Override
    public List<Shipment> getAllShipment() {
        log.info("获取所有运输记录");
        try {
            Shipment1118 shipment1118 = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
            
            // 获取所有物流记录的ID列表
            org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple6<List<BigInteger>, List<BigInteger>, List<BigInteger>, List<String>, List<String>, List<String>> result = 
                shipment1118.getAllShipments();
            
            List<BigInteger> ids = result.getValue1();
            List<Shipment> shipments = new ArrayList<>();
            
            // 对每个ID调用getShipment方法获取完整信息，包括链上的时间戳
            for (BigInteger id : ids) {
                try {
                    // 获取完整的物流信息，包括创建时间和修改时间
                    org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple8<BigInteger, BigInteger, BigInteger, String, String, String, BigInteger, BigInteger> shipmentResult = 
                        shipment1118.getShipment(id);
                    
                    // 创建Shipment对象，使用链上的时间戳
                    Shipment shipment = new Shipment(
                        shipmentResult.getValue1(),
                        shipmentResult.getValue2(),
                        shipmentResult.getValue3(),
                        shipmentResult.getValue4(),
                        shipmentResult.getValue5(),
                        shipmentResult.getValue6(),
                        // 转换链上时间戳为LocalDateTime
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(shipmentResult.getValue7().longValue()), java.time.ZoneOffset.UTC),
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(shipmentResult.getValue8().longValue()), java.time.ZoneOffset.UTC)
                    );
                    shipments.add(shipment);
                } catch (Exception e) {
                    log.error("获取ID为 {} 的运输记录失败：{}", id, e.getMessage());
                    // 继续处理下一个记录
                }
            }
            
            log.info("获取所有运输记录成功，共找到 {} 条记录", shipments.size());
            return shipments;
        } catch (Exception e) {
            log.error("获取所有运输记录失败：{}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Shipment getShipmentById(BigInteger id) {//getShipment
        log.info("根据ID获取运输记录：id={}", id);
        try {
            Shipment1118 shipment1118 = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
            org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple8<BigInteger, BigInteger, BigInteger, String, String, String, BigInteger, BigInteger> result = 
                shipment1118.getShipment(id);
            
            Shipment shipment = new Shipment(
                result.getValue1(),
                result.getValue2(),
                result.getValue3(),
                result.getValue4(),
                result.getValue5(),
                result.getValue6(),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(result.getValue7().longValue()), java.time.ZoneOffset.UTC),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(result.getValue8().longValue()), java.time.ZoneOffset.UTC)
            );
            log.info("根据ID获取运输记录成功，运输记录ID：{}, 用户ID：{}, 承运商ID：{}", 
                shipment.getId(), shipment.getUser_id(), shipment.getCarrier());
            return shipment;
        } catch (Exception e) {
            log.error("根据ID获取运输记录失败：{}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<Shipment> getShipmentByCarrierId(BigInteger carrierId) {//getShipmentsByCarrier
        log.info("根据承运商ID获取运输记录：carrierId={}", carrierId);
        try {
            Shipment1118 shipment1118 = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
            List<BigInteger> shipmentIds = shipment1118.getShipmentsByCarrier(carrierId);
            
            List<Shipment> shipments = new ArrayList<>();
            for (BigInteger shipmentId : shipmentIds) {
                Shipment shipment = getShipmentById(shipmentId);
                if (shipment != null) {
                    shipments.add(shipment);
                }
            }
            log.info("根据承运商ID获取运输记录成功，共找到 {} 条记录，承运商ID：{}", 
                shipments.size(), carrierId);
            return shipments;
        } catch (Exception e) {
            log.error("根据承运商ID获取运输记录失败：{}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public String getShipmentStatusById(BigInteger id) {//getStatus
        log.info("获取运输记录状态：id={}", id);
        try {
            Shipment1118 shipment1118 = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
            // 只调用getStatus获取状态
            String status = shipment1118.getStatus(id);
            
            if (status == null) { 
                log.error("根据ID获取运输记录状态失败，运输记录ID：{}", id);
                return null;
            }
            log.info("根据ID获取运输记录状态成功，运输记录ID：{}, 状态：{}", id, status);
            return status;
        } catch (Exception e) {
            log.error("获取运输记录状态失败：{}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<Shipment> getShipmentByUserId(BigInteger userId) {
        log.info("根据用户ID获取运输记录：userId={}", userId);
        //先查询所有的运输记录，然后根据用户ID过滤
        List<Shipment> shipments = getAllShipment();
        List<Shipment> userShipments = new ArrayList<>();
        for (Shipment shipment : shipments) {
            if (shipment.getUser_id().equals(userId)) {
                userShipments.add(shipment);
            }
        }
        log.info("根据用户ID获取运输记录成功，共找到 {} 条记录，用户ID：{},数据为：{}",userShipments.size(), userId, userShipments);
        return userShipments;
    }

    @Override
    public List<Shipment> getShipmentByUserIdAndCarrierId(BigInteger userId, BigInteger carrierId) {
        log.info("根据用户ID和承运商ID获取运输记录：userId={}, carrierId={}", userId, carrierId);
        //先查询所有的运输记录，然后根据用户ID与承运商ID过滤
        List<Shipment> shipments = getShipmentByUserId(userId);
        List<Shipment> userCarrierShipments = new ArrayList<>();
        for (Shipment shipment : shipments) {
            if (shipment.getCarrier().equals(carrierId)) {
                userCarrierShipments.add(shipment);
            }
        }
        log.info("根据用户ID和承运商ID获取运输记录成功，共找到 {} 条记录，用户ID：{},承运商ID：{},数据为：{}", userCarrierShipments.size(), userId, carrierId, userCarrierShipments);
        return userCarrierShipments;
    }
}
