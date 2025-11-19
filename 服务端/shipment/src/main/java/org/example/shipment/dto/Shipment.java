package org.example.shipment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipment {
    BigInteger id;
    BigInteger user_id;
    BigInteger carrier;
    String from;
    String to;
    String status;
    LocalDateTime createTime;
    LocalDateTime updateTime;
}
