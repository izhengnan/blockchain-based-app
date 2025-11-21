package org.example.shipment.service.backend;

import org.example.shipment.dto.Shipment;

import java.math.BigInteger;
import java.util.List;

public interface ShipmentBackendService {
    boolean addShipment(BigInteger userId, BigInteger carrier, String from, String to);

    boolean deleteShipmentById(BigInteger id);

    boolean updateShipment(BigInteger id, BigInteger carrierId, String status);

    List<Shipment> getAllShipment();

    Shipment getShipmentById(BigInteger id);

    List<Shipment> getShipmentByCarrierId(BigInteger carrierId);

    String getShipmentStatusById(BigInteger id);

    List<Shipment> getShipmentByUserId(BigInteger userId);
}
