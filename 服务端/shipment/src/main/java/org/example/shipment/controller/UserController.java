package org.example.shipment.controller;

import org.example.shipment.dto.User;
import org.example.shipment.model.CommonResponse;
import org.example.shipment.service.backend.UserBackendService;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserBackendService userBackendService;


    @CrossOrigin
    @PostMapping("/add")
    public CommonResponse AddUser(@RequestBody User user) {
        boolean res = userBackendService.addUser(user.getName(), user.getPasswd());
        if (res) {
            return CommonResponse.ok(res);
        }
        return CommonResponse.fail("401", new Exception("添加用户失败"));
    }

    // 登录
    @CrossOrigin
    @PostMapping("/check")
    public CommonResponse Check(@RequestBody User user) throws ContractException {
        boolean res = userBackendService.checkUser(user.getId(), user.getPasswd());
        if (res) {
            return CommonResponse.ok(res);
        }
        return CommonResponse.fail("401", new Exception("用户名或密码错误"));
    }
    //查询用户列表
    @CrossOrigin
    @GetMapping("/list")
    public CommonResponse GetAllUser() {
        List<User> listUser = userBackendService.getAllUser();
        if (listUser != null) {
            return CommonResponse.ok(listUser);
        }
        return CommonResponse.fail("401", new Exception("查询用户列表失败"));
    }

    //根据id查询用户列表
    @CrossOrigin
    @GetMapping("/getById")
    public CommonResponse GetUserById(@RequestParam BigInteger id) {
        User user = userBackendService.getUserById(id);
        if (user != null) {
            return CommonResponse.ok(user);
        }
        return CommonResponse.fail("401", new Exception("查询用户列表失败"));
    }

    //根据id删除用户
    @CrossOrigin
    @DeleteMapping("/delete")
    public CommonResponse DeleteUser(@RequestParam BigInteger id) {
        boolean res = userBackendService.deleteUser(id);
        if (res) {
            return CommonResponse.ok(res);
        }
        return CommonResponse.fail("401", new Exception("删除用户失败"));
    }

    //修改用户密码
    @CrossOrigin
    @PostMapping("/updatePassword")
    public CommonResponse UpdatePassword(@RequestBody User user) {
        boolean res = userBackendService.updatePassword(user.getId(), user.getPasswd());
        if (res) {
            return CommonResponse.ok(res);
        }
        return CommonResponse.fail("401", new Exception("修改用户密码失败"));
    }

}
    