package org.example.shipment.controller;

import org.example.shipment.dto.Admin;
import org.example.shipment.model.CommonResponse;
import org.example.shipment.service.backend.AdminBackendService;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminBackendService adminBackendService;

    @CrossOrigin
    @PostMapping("/add")
    public CommonResponse AddAdmin(@RequestBody Admin admin) {
        boolean res = adminBackendService.addAdmin(admin.getAccount(), admin.getPasswd());
        if (res) {
            return CommonResponse.ok(res);
        }
        return CommonResponse.fail("401", new Exception("添加管理员失败"));
    }

    // 管理员登录验证
    @CrossOrigin
    @PostMapping("/check")
    public CommonResponse CheckAdmin(@RequestBody Admin admin) throws ContractException {
        boolean res = adminBackendService.checkAdmin(admin.getAccount(), admin.getPasswd());
        if (res) {
            return CommonResponse.ok(res);
        }
        return CommonResponse.fail("401", new Exception("账户或密码错误"));
    }

    // 修改管理员密码
    @CrossOrigin
    @PostMapping("/updatePassword")
    public CommonResponse UpdateAdminPassword(@RequestBody Admin admin) {
        boolean res = adminBackendService.updateAdminPassword(admin.getAccount(), admin.getPasswd());
        if (res) {
            return CommonResponse.ok(res);
        }
        return CommonResponse.fail("401", new Exception("修改管理员密码失败"));
    }

    //查询管理员列表
    @CrossOrigin
    @GetMapping("/list")
    public CommonResponse ListAdmin() {
        List<Admin> listAdmin = adminBackendService.listAdmin();
        if (listAdmin != null) {
            return CommonResponse.ok(listAdmin);
        }
        return CommonResponse.fail("401", new Exception("查询管理员列表失败"));
    }

}