package hu.bitnet.smartparking.RequestInterfaces;

import hu.bitnet.smartparking.ServerResponses.ServerResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Attila on 2017.08.08..
 */

public interface RequestInterfaceParkingSelect {

    @POST("parking_select")
    @FormUrlEncoded
    Call<ServerResponse> post(@Field("sessionId") String sessionId, @Field("id") String id);

}
