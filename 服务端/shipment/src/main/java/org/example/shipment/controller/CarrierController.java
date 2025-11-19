package org.example.shipment.controller;

import org.example.shipment.dto.Carrier;
import org.example.shipment.model.CommonResponse;
import org.example.shipment.service.backend.CarrierBackendService;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/carrier")
public class CarrierController {
    @Autowired
    private CarrierBackendService carrierBackendService;

    //注册
    @PostMapping("/add")
    public CommonResponse AddCarrier(@RequestBody Carrier carrier){
        boolean res = carrierBackendService.addCarrier(carrier.getName(), carrier.getInfo(), carrier.getPasswd());
        if(res) {
            return CommonResponse.ok(res);
        }
        return CommonResponse.fail("401", new Exception("添加用户失败"));
    }
    //登录
    @PostMapping("/check")
    public CommonResponse Check(@RequestBody Carrier carrier) throws ContractException {
        boolean res = carrierBackendService.checkCarrier(carrier.getName(), carrier.getPasswd());
        if(res) {
            return CommonResponse.ok(res);
        }
        return CommonResponse.fail("401", new Exception("用户名或密码错误"));
    }
    //查询承运商列表
    @CrossOrigin
    @GetMapping("/list")
    public CommonResponse GetAllCarrier() {
        List<Carrier> listCarrier = carrierBackendService.getAllCarrier();
        if (listCarrier != null) {
            return CommonResponse.ok(listCarrier);
        }
        return CommonResponse.fail("401", new Exception("查询用户列表失败"));
    }

    //根据id查询承运商
    @CrossOrigin
    @GetMapping("/getById")
    public CommonResponse GetCarrierById(@RequestParam BigInteger id) {
        Carrier carrier = carrierBackendService.getCarrierById(id);
        return carrier == null ? CommonResponse.fail("404", new Exception("承运商不存在")) : CommonResponse.ok(carrier);
    }

    //根据id删除承运商
    @CrossOrigin
    @DeleteMapping("/delete")
    public CommonResponse DeleteCarrier(@RequestParam BigInteger id) {
        boolean res = carrierBackendService.deleteCarrier(id);
        return res ? CommonResponse.ok(res) : CommonResponse.fail("404", new Exception("承运商不存在"));
    }
    //修改承运商密码
    @CrossOrigin
    @PostMapping("/updatePassword")
    public CommonResponse UpdatePassword(@RequestBody Carrier carrier) {
        boolean res = carrierBackendService.updatePassword(carrier.getId(), carrier.getPasswd());
        return res ? CommonResponse.ok(res) : CommonResponse.fail("404", new Exception("承运商不存在"));
    }
}
