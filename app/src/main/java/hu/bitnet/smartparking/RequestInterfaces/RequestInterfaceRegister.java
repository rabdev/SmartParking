package hu.bitnet.smartparking.RequestInterfaces;

import hu.bitnet.smartparking.ServerResponses.ServerResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Attila on 2017.08.04..
 */

public interface RequestInterfaceRegister {

    @POST("register")
    @FormUrlEncoded
    Call<ServerResponse> post(@Field(encoded = true, value = "email") String email, @Field("firstName") String first_name, @Field("lastName") String last_name, @Field("password") String password, @Field("phone") String phone);

}
