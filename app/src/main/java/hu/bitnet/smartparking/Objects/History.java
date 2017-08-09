package hu.bitnet.smartparking.Objects;

/**
 * Created by Attila on 2017.08.04..
 */

public class History {

    private Profile profile;
    private BLE ble;
    private MQTT mqtt;
    private Parking_places parking_places;
    private Sum sum;
    private Place place;

    public Profile getProfile() { return profile; }
    public BLE getBle() { return ble; }
    public MQTT getMQTT() { return mqtt; }
    public Parking_places getParking_places() { return parking_places; }
    public Sum getSum() { return sum; }
    public Place getPlace() { return place; }

}
