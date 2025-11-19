package org.example.shipment.service.fisco;

import java.lang.Exception;
import java.lang.String;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.shipment.model.bo.shipment1118AddAdminInputBO;
import org.example.shipment.model.bo.shipment1118AddCarrierInputBO;
import org.example.shipment.model.bo.shipment1118AddShipmentInputBO;
import org.example.shipment.model.bo.shipment1118AddUserInputBO;
import org.example.shipment.model.bo.shipment1118CheckAdminInputBO;
import org.example.shipment.model.bo.shipment1118CheckCarrierInputBO;
import org.example.shipment.model.bo.shipment1118CheckUserInputBO;
import org.example.shipment.model.bo.shipment1118DeleteCarrierInputBO;
import org.example.shipment.model.bo.shipment1118DeleteShipmentInputBO;
import org.example.shipment.model.bo.shipment1118DeleteUserInputBO;
import org.example.shipment.model.bo.shipment1118GetCarrierByIdInputBO;
import org.example.shipment.model.bo.shipment1118GetShipmentInputBO;
import org.example.shipment.model.bo.shipment1118GetShipmentsByCarrierInputBO;
import org.example.shipment.model.bo.shipment1118GetStatusInputBO;
import org.example.shipment.model.bo.shipment1118GetUserByIdInputBO;
import org.example.shipment.model.bo.shipment1118UpdateAdminPasswordInputBO;
import org.example.shipment.model.bo.shipment1118UpdateCarrierInfoInputBO;
import org.example.shipment.model.bo.shipment1118UpdateCarrierPasswordInputBO;
import org.example.shipment.model.bo.shipment1118UpdateShipmentInputBO;
import org.example.shipment.model.bo.shipment1118UpdateUserPasswordInputBO;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Data
public class shipment1118Service {
  public static final String ABI = org.example.shipment.utils.IOUtil.readResourceAsString("abi/shipment1118.abi");

  public static final String BINARY = org.example.shipment.utils.IOUtil.readResourceAsString("bin/ecc/shipment1118.bin");

  public static final String SM_BINARY = org.example.shipment.utils.IOUtil.readResourceAsString("bin/sm/shipment1118.bin");

  @Value("${system.contract.shipment1118Address}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public CallResponse getStatus(shipment1118GetStatusInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getStatus", input.toArgs());
  }

  public TransactionResponse deleteShipment(shipment1118DeleteShipmentInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "deleteShipment", input.toArgs());
  }

  public CallResponse getAllUsers() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getAllUsers", Arrays.asList());
  }

  public TransactionResponse addCarrier(shipment1118AddCarrierInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addCarrier", input.toArgs());
  }

  public CallResponse getAllCarriers() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getAllCarriers", Arrays.asList());
  }

  public TransactionResponse updateUserPassword(shipment1118UpdateUserPasswordInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "updateUserPassword", input.toArgs());
  }

  public CallResponse getAllShipments() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getAllShipments", Arrays.asList());
  }

  public TransactionResponse updateCarrierInfo(shipment1118UpdateCarrierInfoInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "updateCarrierInfo", input.toArgs());
  }

  public CallResponse getShipment(shipment1118GetShipmentInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getShipment", input.toArgs());
  }

  public CallResponse getUserById(shipment1118GetUserByIdInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getUserById", input.toArgs());
  }

  public TransactionResponse addUser(shipment1118AddUserInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addUser", input.toArgs());
  }

  public CallResponse checkAdmin(shipment1118CheckAdminInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "checkAdmin", input.toArgs());
  }

  public TransactionResponse addAdmin(shipment1118AddAdminInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addAdmin", input.toArgs());
  }

  public CallResponse checkUser(shipment1118CheckUserInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "checkUser", input.toArgs());
  }

  public CallResponse getCarrierById(shipment1118GetCarrierByIdInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getCarrierById", input.toArgs());
  }

  public TransactionResponse updateAdminPassword(shipment1118UpdateAdminPasswordInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "updateAdminPassword", input.toArgs());
  }

  public CallResponse getShipmentsByCarrier(shipment1118GetShipmentsByCarrierInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getShipmentsByCarrier", input.toArgs());
  }

  public TransactionResponse updateCarrierPassword(shipment1118UpdateCarrierPasswordInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "updateCarrierPassword", input.toArgs());
  }

  public CallResponse getAllAdmins() throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getAllAdmins", Arrays.asList());
  }

  public TransactionResponse deleteUser(shipment1118DeleteUserInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "deleteUser", input.toArgs());
  }

  public TransactionResponse addShipment(shipment1118AddShipmentInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addShipment", input.toArgs());
  }

  public TransactionResponse updateShipment(shipment1118UpdateShipmentInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "updateShipment", input.toArgs());
  }

  public TransactionResponse deleteCarrier(shipment1118DeleteCarrierInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "deleteCarrier", input.toArgs());
  }

  public CallResponse checkCarrier(shipment1118CheckCarrierInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "checkCarrier", input.toArgs());
  }
}
