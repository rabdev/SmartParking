package hu.bitnet.smartparking.ServerResponses;

import hu.bitnet.smartparking.Objects.BLE;
import hu.bitnet.smartparking.Objects.Error;
import hu.bitnet.smartparking.Objects.History;
import hu.bitnet.smartparking.Objects.MQTT;
import hu.bitnet.smartparking.Objects.Parking_places;
import hu.bitnet.smartparking.Objects.Profile;
import hu.bitnet.smartparking.Objects.Sum;

/**
 * Created by Attila on 2017.08.04..
 */

public class ServerResponse {

    private Profile profile;
    private Error error;
    private String alert;
    private Parking_places parking_places;
    private Parking_places[] addresses;
    private MQTT mqtt;
    private BLE ble;
    private Sum sum;
    private History[] history;

    public Profile getProfile() { return profile; }
    public Error getError() { return error; }
    public String getAlert() { return alert; }
    //public Parking_places getParking_places() { return parking_places; }
    public Parking_places[] getAddress() { return addresses; }
    public MQTT getMQTT() { return mqtt; }
    public BLE getBLE() { return ble; }
    public Sum getSum() { return sum; }
    public History[] getHistory() { return history; }

}
