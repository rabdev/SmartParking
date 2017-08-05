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

    @FormUrlEncoded
    @POST("register")
    Call<ServerResponse> post(@Field("firstName") String first_name, @Field("lastName") String last_name, @Field("email") String email, @Field("password") String password, @Field("phone") String phone);

}
