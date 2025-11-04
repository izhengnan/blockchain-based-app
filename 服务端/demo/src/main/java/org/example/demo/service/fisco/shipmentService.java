package org.example.demo.service.fisco;

import java.lang.Exception;
import java.lang.String;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.demo.model.bo.shipmentAddCarrierInputBO;
import org.example.demo.model.bo.shipmentAddShipmentInputBO;
import org.example.demo.model.bo.shipmentAddUserInputBO;
import org.example.demo.model.bo.shipmentCheckCarrierInputBO;
import org.example.demo.model.bo.shipmentCheckUserInputBO;
import org.example.demo.model.bo.shipmentGetShipmentInputBO;
import org.example.demo.model.bo.shipmentGetShipmentsByCarrierInputBO;
import org.example.demo.model.bo.shipmentGetStatusInputBO;
import org.example.demo.model.bo.shipmentUpdateShipmentInputBO;
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
public class shipmentService {
  public static final String ABI = org.example.demo.utils.IOUtil.readResourceAsString("abi/shipment.abi");

  public static final String BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/ecc/shipment.bin");

  public static final String SM_BINARY = org.example.demo.utils.IOUtil.readResourceAsString("bin/sm/shipment.bin");

  @Value("${system.contract.shipmentAddress}")
  private String address;

  @Autowired
  private Client client;

  AssembleTransactionProcessor txProcessor;

  @PostConstruct
  public void init() throws Exception {
    this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
  }

  public CallResponse getStatus(shipmentGetStatusInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getStatus", input.toArgs());
  }

  public CallResponse getShipmentsByCarrier(shipmentGetShipmentsByCarrierInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getShipmentsByCarrier", input.toArgs());
  }

  public TransactionResponse addCarrier(shipmentAddCarrierInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addCarrier", input.toArgs());
  }

  public TransactionResponse addShipment(shipmentAddShipmentInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addShipment", input.toArgs());
  }

  public CallResponse getShipment(shipmentGetShipmentInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "getShipment", input.toArgs());
  }

  public TransactionResponse addUser(shipmentAddUserInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "addUser", input.toArgs());
  }

  public TransactionResponse updateShipment(shipmentUpdateShipmentInputBO input) throws Exception {
    return this.txProcessor.sendTransactionAndGetResponse(this.address, ABI, "updateShipment", input.toArgs());
  }

  public CallResponse checkCarrier(shipmentCheckCarrierInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "checkCarrier", input.toArgs());
  }

  public CallResponse checkUser(shipmentCheckUserInputBO input) throws Exception {
    return this.txProcessor.sendCall(this.client.getCryptoSuite().getCryptoKeyPair().getAddress(), this.address, ABI, "checkUser", input.toArgs());
  }
}
