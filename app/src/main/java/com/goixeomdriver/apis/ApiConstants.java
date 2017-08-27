package com.goixeomdriver.apis;

/**
 * Created by MyPC on 9/12/2016.
 */
public class ApiConstants {

    public static final String API_ROOT="https://goixeom.com/webservice1/";

    public static final String API_LOGIN="login-driver.php";
    public static final String API_LOGOUT="logout-driver.php";
    public static final String API_REGISTER="sign-up-driver.php";
    public static final String API_GET_INFOR="get-driver-info.php";
    public static final String API_FORGOT_PASSWORD="forgot-password-driver.php";
    public static final String API_SMS="verify-code-driver.php";
    public static final String API_CHECK_INFO="check-info-driver.php";
    public static final String API_UPLOAD="upload-profile.php";
    public static final String API_GET_INFO_TRIP="get-info-trip-driver.php";
    public static final String API_TRIP_RECONNECT="trip-reconnect.php";
    public static final String API_RECEIVE_TRIP="receive-trip.php";
    public static final String API_CLICKED="click.php";
    public static final String API_HISTORY="trip-history-driver.php";
    public static final String API_NOTIFICATION="notification-driver.php";
    public static final String API_FAQ="feedback-driver.php";
    public static final String API_WALLET="wallet.php";
    public static final String API_POCKET="report.php";
    public static final String API_GET_DRIVER="get-driver.php";
    public static final String API_DIRECTION="directions/json";
    public static final String API_KEY="9a887d75fed2ffb7b641dbf85b3bffd9";
    //CODE
    public static final int CODE_SUCESS = 200;
    public static final int CODE_ERROR_PARAM = 404;
    public static final int CODE_ERROR_SERVER = 500;
    public static final int CODE_ERROR_ACTIVED = 400;
    public static final int CODE_ERROR_LOGIN_PARAM = 300;
    public static final int CODE_ERROR_NO_EXIST_ACC = 404;
    public static final int CODE_ERROR_ALREADY_ACTIVE = 100;
    public static final int CODE_ERROR_LOCKED_ACC = 101;
    public static final int CODE_DEVICE_ANDROID = 1;
    public static final int CODE_CUSTOMER = 2;
    public static final int CODE_FINDING = 0;
    public static final int CODE_BIDED = 1;
    public static final int CODE_FINISH = 2;
    public static final int CODE_DECLINE = -1;
    public static final int CODE_TIME_OUT = -2;


    //LOGIN
    public static final String KEY_PHONE_NUMBER="phone_number";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_DEVICE_TOKEN="device_token";
    public static final String KEY_TYPE_DEVICE="type_device";
    public static final String KEY_TYPE_USER="type_user";


    //ACCESSTOKEN

    public static final String KEY_USERNAME="UserName";
    public static final String KEY_PASSWORD_ACCESSTOKEN="Password";
    public static final String KEY_LANGUAGE="Language";
    public static final String KEY_SITE="Site";

    //STORE
    public static final String KEY_ACCESSTOKEN="AccessToken";
    public static final String KEY_PAGESIZE ="PageSize";
    public static final String KEY_PAGEINDEX="PageIndex";
    public static final String KEY_QUERY="Query";
    //PRODUCT
    public static final String KEY_CODE="Code";


    public static final String KEY_COLLECTION = "Collection";
    public static final String KEY_COLOR = "Color";
    public static final String KEY_STYLE = "Style";
    public static final String KEY_SIZE = "Size";

    public static final String KEY_IMAGE_SIZE = "ImageWidth";

    public static final String KEY_PRODUCT_CODE="ProductCode";


    public static final String KEY_MODE = "Mode";
    public static final String KEY_AUTHTOKEN="AuthenticatedAccessToken";

    public static final int TYPE_BANG_LAI = 1;
    public static final int TYPE_DANGKY = 2;
    public static final int TYPE_BAO_HIEM = 3;
    public static final int TYPE_CMT = 4;
    public static final int TYPE_HOCHIEU =5;
    public static final String API_GET_DETAIL_BOOKING="info-trip-driver.php";
    public static final String API_NEXT_BOOKING="driver-booking.php";


}
