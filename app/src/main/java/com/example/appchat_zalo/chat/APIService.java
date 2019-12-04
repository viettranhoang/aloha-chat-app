package com.example.appchat_zalo.chat;

import com.example.appchat_zalo.push_notification.MyRespone;
import com.example.appchat_zalo.push_notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAHx8knDw:APA91bFZek7v5cqnMGBoiG4HhHb5R9EpZucQxFmvHyGwyy3H8hxe7e-XFbuhPW6Kbn4EIHuNYSgz6KLrX7K3wQwDISWNPZsWvztz8FXfgTvgOsUV8Mff3uOx1OjwE9uV_TiywOcGDqdp"
            }
    )

    @POST("fcm/send")
    Call<MyRespone> sendNotification(@Body Sender body);
}
