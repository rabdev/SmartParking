package hu.bitnet.smartparking.Objects;

/**
 * Created by Attila on 2017.08.04..
 */

public class Parking_places {

    public Parking_places(String latitude, String longitude, String address){
        this.latitude=latitude;
        this.longitude=longitude;
        this.address=address;
    }

    private String address;
    private String price;
    private String distance;
    private String time;
    private String id;
    private String latitude, longitude;
    private BLE ble;
    private MQTT mqtt;


    public String getAddress() { return address; }
    public String getPrice() { return price; }
    public String getDistance() { return distance; }
    public String getTime() { return time; }
    public String getId() { return id; }
    public String getLatitude() {return latitude;}
    public String getLongitude() {return longitude;}
    public BLE getBLE() { return ble; }
    public MQTT getMQTT() { return mqtt; }

}
