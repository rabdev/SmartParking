package hu.bitnet.smartparking.RequestInterfaces;

import hu.bitnet.smartparking.ServerRequests.ServerRequestLogin;
import hu.bitnet.smartparking.ServerResponses.ServerResponseLogin;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Attila on 2017.08.04..
 */

public interface RequestInterfaceLogin {

    @POST("login")
    Call<ServerResponseLogin> operation(@Body ServerRequestLogin request);

}
