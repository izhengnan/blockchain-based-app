package org.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarrierDTO {
    private BigInteger id;
    private String name;
    private String info;
    private String passwd;
}
