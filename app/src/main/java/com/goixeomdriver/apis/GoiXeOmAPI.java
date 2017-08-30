package com.goixeomdriver.apis;


import com.goixeomdriver.models.AppConfig;
import com.goixeomdriver.models.DetailTripModel;
import com.goixeomdriver.models.History;
import com.goixeomdriver.models.NextBooking;
import com.goixeomdriver.models.NotificationData;
import com.goixeomdriver.models.PocketModel;
import com.goixeomdriver.models.Routes;
import com.goixeomdriver.models.TripInforModel;
import com.goixeomdriver.models.User;
import com.goixeomdriver.models.VerifyCode;
import com.goixeomdriver.models.WalletModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by MyPC on 9/12/2016.
 */
public interface GoiXeOmAPI {


    @GET(ApiConstants.API_GET_INFOR)
    Call<ApiResponse<User>> getInfor(@Query("key") String key, @Query("id") int id);

    @GET(ApiConstants.API_LOGIN)
    Call<ApiResponse<User>> login(@Query("key") String key, @Query("phone") String phone, @Query("password") String password, @Query("imei") String imei);

    @FormUrlEncoded
    @POST(ApiConstants.API_LOGOUT)
    Call<ApiResponse> logout(@Field("key") String key, @Field("id") int id);

    @FormUrlEncoded
    @POST(ApiConstants.API_REGISTER)
    Call<ApiResponse<String>> register(@Field("key") String key, @Field("phone") String phone, @Field("name") String nameField
            , @Field("email") String email, @Field("ref-code") String refCode, @Field("password") String password, @Field("imei") String imei, @Field("type") int type);

    @FormUrlEncoded
    @POST(ApiConstants.API_FORGOT_PASSWORD)
    Call<ApiResponse> forgotPassword(@Field("key") String key
            , @Field("email") String email);

    @GET(ApiConstants.API_SMS)
    Call<ApiResponse<VerifyCode>> sendSMS(@Query("key") String key, @Query("phone") String phone);

    @GET(ApiConstants.API_GET_INFO_TRIP)
    Call<ApiResponse<TripInforModel>> getDetailTrip(@Query("key") String key, @Query("t_id") int idTrip, @Query("u_id") int idUser);

    @GET(ApiConstants.API_TRIP_RECONNECT)
    Call<ApiResponse<TripInforModel>> getDetailReconnectTrip(@Query("key") String key, @Query("t_id") int idTrip, @Query("u_id") int idUser);

    @FormUrlEncoded
    @POST(ApiConstants.API_RECEIVE_TRIP)
    Call<ApiResponse<String>> receiveTrip(@Field("key") String key, @Field("t_id") int idTrip, @Field("d_id") int idDriver);

    @FormUrlEncoded
    @POST(ApiConstants.API_CLICKED)
    Call<ApiResponse> click(@Field("key") String key, @Field("id") int idTrip);

    @GET(ApiConstants.API_CHECK_INFO)
    Call<ApiResponse> checkInfor(@Query("key") String key, @Query("phone") String phone, @Query("name") String nameField
            , @Query("email") String email, @Query("ref-code") String refCode);

    @Multipart
    @POST(ApiConstants.API_UPLOAD)
    Call<ApiResponse> uploadProfile(@PartMap() Map<String, RequestBody> partMap,
                                    @Part MultipartBody.Part file);

    @GET(ApiConstants.API_HISTORY)
    Call<ApiResponse<List<History>>> getHistories(@Query("key") String key, @Query("id") int id);

    @GET(ApiConstants.API_NOTIFICATION)
    Call<ApiResponse<List<NotificationData>>> getNotification(@Query("key") String key, @Query("id") int id);

    @GET(ApiConstants.API_DIRECTION)
    Call<ApiResponse<List<Routes>>> getDirection(
            @Query("origin") String orgin
            , @Query("destination") String destination
            , @Query("key") String key);

    @GET(ApiConstants.API_WALLET)
    Call<ApiResponse<WalletModel>> getWallet(@Query("key") String key, @Query("id") int id);

    @GET(ApiConstants.API_POCKET)
    Call<ApiResponse<PocketModel>> getPocket(@Query("key") String key, @Query("id") int id);

    @GET(ApiConstants.API_GET_DRIVER)
    Call<ApiResponse<List>> getDriver(@Query("key") String key, @Query("lat") double id, @Query("lng") double lng);

    @FormUrlEncoded
    @POST(ApiConstants.API_FAQ)
    Call<ApiResponse> feedback(@Field("key") String key, @Field("id") int id, @Field("content") String content);

    @GET(ApiConstants.API_GET_DETAIL_BOOKING)
    Call<ApiResponse<DetailTripModel>> getDetailBooking(@Query("key") String key
            , @Query("t_id") int idTrip
    );

    @GET(ApiConstants.API_NEXT_BOOKING)
    Call<ApiResponse<NextBooking>> getNextBookingDriver(@Query("key") String key
            , @Query("d_id") int driverId
    );
    @GET(ApiConstants.API_CONFIG_APP)
    Call<ApiResponse<AppConfig>> getAppConfig(@Query("key") String key
    );
    @Streaming
    @GET
    Call<okhttp3.ResponseBody> downloadFileWithDynamicUrlAsync(@Url String fileUrl);
}
