package org.example.shipment.model.bo;

import java.lang.Object;
import java.lang.String;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class shipment1118AddShipmentInputBO {
  private BigInteger _id;

  private BigInteger _carrier;

  private String _from;

  private String _to;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_id);
    args.add(_carrier);
    args.add(_from);
    args.add(_to);
    return args;
  }
}
