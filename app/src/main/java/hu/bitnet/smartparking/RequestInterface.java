package hu.bitnet.smartparking;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Attila on 2017.08.04..
 */

public interface RequestInterface {

    @POST("login")
    Call<ServerResponse> operation(@Body ServerRequest request);

}
