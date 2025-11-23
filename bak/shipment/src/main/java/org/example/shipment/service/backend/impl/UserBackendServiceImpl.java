package org.example.shipment.service.backend.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.shipment.dto.User;
import org.example.shipment.raw.Shipment1118;
import org.example.shipment.service.backend.UserBackendService;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.request.Transaction;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.shipment.dto.LoginInfo;
import org.example.shipment.utils.JwtUtils;

@Slf4j
@Service
public class UserBackendServiceImpl implements UserBackendService {

    @Value("${system.contract.shipment1118Address}")
    private String contractAddress;

    @Autowired
    private Client client;

    @Override
    public boolean addUser(String name, String passwd) {
        log.info("addUser: name:{},passwd:{}", name, passwd);
        Shipment1118 shipment = Shipment1118.load(contractAddress,client,client.getCryptoSuite().getCryptoKeyPair());
        TransactionReceipt transactionReceipt = shipment.addUser(name, passwd);
        log.info(transactionReceipt.getStatus());
        return transactionReceipt.isStatusOK();
    }

    @Override
    public LoginInfo checkUser(BigInteger id, String passwd) throws ContractException {
        log.info("checkUser: id:{},passwd:{}", id, passwd);
        Shipment1118 shipment = Shipment1118.load(contractAddress,client,client.getCryptoSuite().getCryptoKeyPair());
        Boolean isValid = shipment.checkUser(new BigInteger(String.valueOf(id)),passwd);
        
        if (isValid) {
            // 获取用户详细信息
            User user = getUserById(id);
            
            // 构建JWT声明
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", id.toString());
            claims.put("name", user.getName());
            claims.put("userType", "user");
            
            // 生成令牌
            String token = JwtUtils.generateJwt(claims);
            
            // 构建并返回LoginInfo对象
            return new LoginInfo(token, user.getName(), null, id, "user");
        } else {
            // 用户验证失败，返回null
            log.warn("用户验证失败: id:{}", id);
            return null;
        }
    }

    @Override
    public List<User> getAllUser() {
        try {
            // 创建Shipment1118实例
            Shipment1118 shipment = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
            
            // 调用区块链的getAllUsers方法
            org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3<java.util.List<java.math.BigInteger>, java.util.List<java.lang.String>, java.util.List<java.lang.String>> result = 
                shipment.getAllUsers();
            
            // 提取结果
            List<BigInteger> userIds = result.getValue1();
            List<String> userNames = result.getValue2();
            List<String> userPasswords = result.getValue3();
            
            // 构建User对象列表
            List<User> userList = new ArrayList<>();
            for (int i = 0; i < userIds.size(); i++) {
                User user = new User(userIds.get(i), userNames.get(i), userPasswords.get(i));
                userList.add(user);
            }
            
            log.info("成功获取用户列表，共{}个用户", userList.size());
            return userList;
            
        } catch (Exception e) {
            log.error("获取用户列表失败: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
        
    }

    @Override
    public User getUserById(BigInteger id) {
        
        try {
            // 创建Shipment1118实例
            Shipment1118 shipment = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
            
            // 调用区块链的getUserById方法
            org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3<java.math.BigInteger, java.lang.String, java.lang.String> result = 
                shipment.getUserById(id);
            
            // 提取结果
            BigInteger userId = result.getValue1();
            String userName = result.getValue2();
            String userPassword = result.getValue3();
            
            // 构建User对象
            User user = new User(userId, userName, userPassword);
            
            log.info("成功获取用户信息，ID: {}, 名称: {}", userId, userName);
            return user;
            
        } catch (Exception e) {
            log.error("获取用户信息失败，ID: {}, 错误: {}", id, e.getMessage(), e);
            return null;
        }
        
    }

    @Override
    public boolean deleteUser(BigInteger id) {
        try {
            // 创建Shipment1118实例
            Shipment1118 shipment = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
            
            // 调用区块链的deleteUser方法
            TransactionReceipt transactionReceipt = shipment.deleteUser(id);
            
            // 检查交易状态
            if (transactionReceipt.isStatusOK()) {
                log.info("成功删除用户，ID: {}", id);
                return true;
            } else {
                log.error("删除用户失败，ID: {}, 状态码: {}", id, transactionReceipt.getStatus());
                return false;
            }
            
        } catch (Exception e) {
            log.error("删除用户失败，ID: {}, 错误: {}", id, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean updatePassword(BigInteger id, String passwd) {
        try {
            // 创建Shipment1118实例
            Shipment1118 shipment = Shipment1118.load(contractAddress, client, client.getCryptoSuite().getCryptoKeyPair());
            
            // 调用区块链的updatePassword方法
            TransactionReceipt transactionReceipt = shipment.updateUserPassword(id, passwd);
            
            // 检查交易状态
            if (transactionReceipt.isStatusOK()) {
                log.info("成功更新用户密码，ID: {}", id);
                return true;
            } else {
                log.error("更新用户密码失败，ID: {}, 状态码: {}", id, transactionReceipt.getStatus());
                return false;
            }
            
        } catch (Exception e) {
            log.error("更新用户密码失败，ID: {}, 错误: {}", id, e.getMessage(), e);
            return false;
        }
    }
}
