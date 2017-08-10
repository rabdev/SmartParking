package hu.bitnet.smartparking.RequestInterfaces;

import hu.bitnet.smartparking.ServerResponses.ServerResponseError;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Attila on 2017.08.10..
 */

public interface RequestInterfaceLoginError {

    @POST("login")
    @FormUrlEncoded
    Call<ServerResponseError> post(@Field(encoded = true, value = "email") String email, @Field("password") String password);

}
