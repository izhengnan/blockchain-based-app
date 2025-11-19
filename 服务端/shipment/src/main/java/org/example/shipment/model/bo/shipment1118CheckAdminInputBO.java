package org.example.shipment.model.bo;

import java.lang.Object;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class shipment1118CheckAdminInputBO {
  private String _account;

  private String _passwd;

  public List<Object> toArgs() {
    List args = new ArrayList();
    args.add(_account);
    args.add(_passwd);
    return args;
  }
}
