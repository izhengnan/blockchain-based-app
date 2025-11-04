package org.example.demo.service.backend;

import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

public interface UserBackendService {
    //注册
    boolean addUser(String name,String passwd);
    //验证
    boolean checkUser(int id,String passwd) throws ContractException;

}
