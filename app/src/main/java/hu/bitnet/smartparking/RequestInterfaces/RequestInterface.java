package hu.bitnet.smartparking.RequestInterfaces;

import hu.bitnet.smartparking.ServerRequests.ServerRequest;
import hu.bitnet.smartparking.ServerResponses.ServerResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Attila on 2017.08.04..
 */

public interface RequestInterface {

    @POST("register")
    Call<ServerResponse> operation(@Body ServerRequest request);

}
