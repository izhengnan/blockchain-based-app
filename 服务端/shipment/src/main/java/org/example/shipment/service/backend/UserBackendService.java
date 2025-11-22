package org.example.shipment.service.backend;

import org.example.shipment.dto.LoginInfo;
import org.example.shipment.dto.User;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

import java.math.BigInteger;
import java.util.List;

public interface UserBackendService {
    //注册
    boolean addUser(String name,String passwd);
    //验证并返回令牌信息
    LoginInfo checkUser(BigInteger id, String passwd) throws ContractException;

    List<User> getAllUser();

    User getUserById(BigInteger id);

    boolean deleteUser(BigInteger id);

    boolean updatePassword(BigInteger id, String passwd);
}
