package hu.bitnet.smartparking.ServerResponses;

import hu.bitnet.smartparking.Objects.Error;
import hu.bitnet.smartparking.Objects.Profile;

/**
 * Created by Attila on 2017.08.04..
 */

public class ServerResponseLogin {

    private Profile profile;
    private Error error;
    private String alert;

    public Profile getProfile() { return profile; }
    public Error getError() { return error; }
    public String getAlert() { return alert; }

}
