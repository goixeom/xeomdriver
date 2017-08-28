# Goi Xe Om - Driver
Kiến trúc : Java core - Android

Android studio >2.3 

Gradle 2.3.3

Package Name : com.goixeomdriver

## Thư viện hỗ trợ :
Tất cả thư viện đã liệt kê trong app/build.gradle .

+ Image : Picasso , Glide , Crop, CircleImage...
+ Kết nối RestApi : Retrofit2 & Gson
+ Socket : Socket IO client
+ Log/Utils/Toast... : utilcode
+ View : ButterKnife Binding view, Recyclerview pull to refresh



## Kiến trúc project : 
Các file class được chia theo từng loại trong từng folder. 
+ Activity : chưa các file activity class
+ fragment : chứa các file fragment
+ api : chưa các class liên quan đến việc kết nối webserver.
+ Views: các class custom view
+ Utils : Các class dùng lại ở nhiều project....
+ Socket : các class liên quan xử lý socket đặt chuyến, bắt chuyến, thông báo....
+ Models : các class models của cả project, 
## Chức Năng API
Công nghệ sử dụng Retrofit 

Đường dẫn chính : app/main/java/apis
+ ApiConstants : Chứa toàn bộ URL, CODE Response, STATUS CODE để kết nối RestApi
+ ApiUtils : File tạo kết nối đến Api của Restrofit
+ CallBackCustom/ CallBackCustomNoDelay : File Callback khi gọi Retrofit kết nối Api . Chuẩn hóa tất cả callback về 1 dạng inteface.
Tất cả callback sẽ chỉ trả về khi thành công và nếu lỗi xảy ra được sử lý trong file này.
+ PlaceDetailJson : Các file hỗ trợ parse Json từ google/place. 
+ ApiResponse : File class trả về chuẩn của mỗi api, Tất cả api trả về đều có dạng
```Json 
    {
     "data": {Json},
     "errorId": 200,
     "message": "OK"
     }
```
Tất cả data response sẽ tự động được cast về theo dạng ApiResponse.class bằng Gson. 
Chỉ cần định nghĩa ApiResponse<T> khi gọi callBack api.

#### Example
Gọi API Login : 
Url : ApiConstants.API_LOGIN
Method : GoiXeOmApi
```Java 
    @GET(ApiConstants.API_LOGIN)
            Call<ApiResponse<User>> login(@Query("key") String key, @Query("phone") String phone, @Query("password") String password, @Query("imei") String imei);
``` 
Tại đây đối tượng trả về là 1 User và được cast vào ApiResponse để tự động khi callbackCustom được gọi.

Gọi API :
```Java 
              Call<ApiResponse<User>> login = getmApi().login(ApiConstants.API_KEY, edtPhone.getText().toString(), password, PhoneUtils.getIMEI());
                    login.enqueue(new CallBackCustom<ApiResponse<User>>(LoginActivity.this, getDialogProgress(), new OnResponse<ApiResponse<User>>() {
                        @Override
                        public void onResponse(ApiResponse<User> response) {
                                // Xu ly khi response.getData() 
                         }
                           }));
                                }
                     });
``` 

## Socket IO Client 

Công nghệ sử dụng SocketIO java

Đường dẫn chính : app/main/java/socket

+ SocketClient : Là 1 service chạy ngầm, tự động kết nối tới socket và thực hiện.
+ AutoRestartService : Receiver tự động khởi động lại service SocketClient để socket luôn chạy cùng hệ thống kể cả thi tắt app
+ InternetReceiver :  Receiver tự động bắt sự kiện khi mất kết nối hoặc có kết nối internet
+ SocketConstants : class chứa toàn bộ url, trạng thái chuyến đi, chuỗi config với socket
+ SocketResponse : tương tự như class ApiResponse

#### Example :
Khởi tạo socket : SocketClient.initSocket()

Hàm này thực hiện việc khởi tạo socket và các trạng thái socket, Được gọi ngay khi service chạy

+ Để đẩy 1 gói tin lên server : getSocket().emit()..

+ Để lắng nghe 1 luồng sự kiện server bắn về : getSocket().on("Ten_Luong_Su_Kien)..

Cụ thể tham khảo API DOC.

## Trình tự hoạt động App Tài Xế 
+ Đăng ký/ Đăng Nhập:  
    + Khi đăng ký cần verify số điện thoại tài xế. Verify thành công thì mới tiếp tục đăng ký
    + Sau khi đăng nhập cần kiểm tra xem tài khoản đó đã đầy đủ thông tin chưa, nếu có đủ rồi thì vào map , ngược lại thì báo update.
    + Sau khi đăng ký đăng nhập cần gọi socket update Imei máy điện thoại
    
+ Update thông tin tài xế :
    + Upload các file ảnh thông tin về tài xế,
    + Cần gọi socket để có thể nhận được thông báo realtime khi Admin verify thông tin.
    + Upload thành công thì hiện nút bầm vào map
 + Map Driver : 
    + Vô hiệu hóa nút back.
    + Cần gọi api lấy toàn bộ thông tin của tài xế -> binding lên view.
    + Cần gọi socket lắng nghe luồng để nhận thông báo về chuyến mới
    + Bật notification luôn hiện trên status bar báo app đang chạy ngầm
    + Cần gọi api get config server để trả về nhưng config trên server.
    + Lắng nghe location thay đổi -> gọi socket emit vị trí mới
 + Khi có chuyến mới : 
    + Bắt chuyến : Gọi api join vào chuyến , đồng thời gọi socket join vào chuyến để thông báo tới customer đã có người nhận chuyến.
    + Từ chối :  Gọi api từ chối, đồng thời gọi socket từ chối.
 + Khi bắt chuyến thành công : 
    + Thực hiện lắng nghe location thay đổi, mỗi khi thay đổi gọi socket emit location mới của driver
    + Thực hiện thay đổi các trạng thái chuyến đi KHÁCH ĐÃ LÊN XE -> ĐANG DI CHUYỂN -> KẾT THÚC CHUYẾN -> BACK VỀ MAP DRIVER
    + Mỗi khi thay đổi trạng thái chuyến đi cần emit lên server để thông báo tới customer.
 + Reconnection
    + Khi đang không trong chuyến đi
        + Mỗi khi disconnect với server thì trạng thái Driver sẽ về 2 . Sau đó khi có connect lại thì phải thay đổi trạng thái driver về trạng thái mà switch tại màn hình map driver đang bật hay tắt (0/1)
    + Khi đang trong chuyến đi 
        + Khi đang trong chuyến đi trạng thái driver luôn giữ là 3 . Khi connect lại không được thay đổi trạng thái driver. 
        + Gọi Api lấy về toàn bộ thông tin chuyến đang đi và binding view lại. 
        + Thực hiện kết nối join socket, lắng nghe location thay đổi và emit các trạng thái chuyến đi như trường hợp bắt chuyến thành công.
 + Notification 
    + Notification được đăng ký trong socketClient
    + Mỗi khi có noitification -> Gọi createNotification trong file CommomUtils và set các options cho notification để khi user mở notification sẽ tự động chuyển vào màn hình tương ứng
 + Các menu chức năng
 
    Gọi API tương ứng trong GoiXeOmApi -> BindingView
    
    

  


