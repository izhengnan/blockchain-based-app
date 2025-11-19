package org.example.demo.controller;

import org.example.demo.model.CommonResponse;
import org.example.demo.service.backend.CarrierBackendService;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carrier")
public class CarrierController {
    @Autowired
    private CarrierBackendService carrierBackendService;

    //登录
    @PostMapping("/add")
    public CommonResponse AddCarrier(@RequestBody String name, @RequestBody String info, @RequestBody String passwd){
        boolean res = carrierBackendService.addCarrier(name, info, passwd);
        if(res) {
            return CommonResponse.ok(res);
        }
        return CommonResponse.fail("401", new Exception("添加用户失败"));
    }
    @PostMapping("/check")
    public CommonResponse Check(@RequestBody int id, @RequestBody String passwd) throws ContractException {
        boolean res = carrierBackendService.checkCarrier(id, passwd);
        if(res) {
            return CommonResponse.ok(res);
        }
        return CommonResponse.fail("401", new Exception("用户名或密码错误"));
    }
}
