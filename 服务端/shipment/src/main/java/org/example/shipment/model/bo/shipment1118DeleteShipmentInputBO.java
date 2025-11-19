package org.example.shipment.model.bo;

import java.lang.Object;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class shipment1118DeleteShipmentInputBO {
  private BigInteger _shipment_id;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_shipment_id);
    return args;
  }
}
