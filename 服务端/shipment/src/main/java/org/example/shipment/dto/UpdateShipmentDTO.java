package org.example.shipment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShipmentDTO {
    private BigInteger id;
    private BigInteger carrierId;
    private String status;
}
