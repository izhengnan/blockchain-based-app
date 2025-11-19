package org.example.demo.controller;

import org.example.demo.dto.UserAddRequest;
import org.example.demo.dto.UserCheckRequest;
import org.example.demo.model.CommonResponse;
import org.example.demo.service.backend.UserBackendService;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserBackendService userBackendService;


    @CrossOrigin
    @PostMapping("/add")
    public CommonResponse AddUser(@RequestBody UserAddRequest request) {
        boolean res = userBackendService.addUser(request.getUsername(), request.getPasswd());
        if (res) {
            return CommonResponse.ok(res);
        }
        return CommonResponse.fail("401", new Exception("添加用户失败"));
    }

    // 登录
    @CrossOrigin
    @PostMapping("/check")
    public CommonResponse Check(@RequestBody UserCheckRequest request) throws ContractException {
        boolean res = userBackendService.checkUser(request.getId(), request.getPasswd());
        if (res) {
            return CommonResponse.ok(res);
        }
        return CommonResponse.fail("401", new Exception("用户名或密码错误"));
    }
}
    