package org.example.shipment.service.backend;

import org.example.shipment.dto.Admin;
import org.example.shipment.dto.LoginInfo;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

import java.util.List;

public interface AdminBackendService {
    //添加管理员
    boolean addAdmin(String account, String passwd);
    //验证管理员登录
    LoginInfo checkAdmin(String account, String passwd) throws ContractException;
    //修改管理员密码
    boolean updateAdminPassword(String account, String newPasswd);

    List<Admin> listAdmin();
}