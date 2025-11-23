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
public class shipment1118UpdateUserPasswordInputBO {
  private BigInteger _id;

  private String _newPasswd;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_id);
    args.add(_newPasswd);
    return args;
  }
}
