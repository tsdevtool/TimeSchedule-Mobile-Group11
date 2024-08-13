package com.example.utils;

import android.content.Context;

public class MailUtils {
    public static final String EMAIL = "sieusml03@gmail.com";
    public static final String PASSWORD = "sieu08042003";

    private String receiver;
    private String subject;
    private String body;
    private JavaMailAPI javaMailAPI;

//    public MailUtils(){
//
//    }
//    public void sendRegisterAccount(Context context, String email, String password){
//        receiver = email.toLowerCase().trim();
//        subject = "TIMESCHEDULE - GỬI THÔNG TIN MẬT KHẨU";
//        body = subject + "\n\nĐây là mật khẩu tài khoản được gửi từ TimeSchedule. Tuyệt đối không được gửi cho bất cứ ai. \n"
//            + password.trim() +"\n\nThân ái";
//        javaMailAPI = new JavaMailAPI(context, receiver,subject, body);
//        javaMailAPI.execute();
//    }

}
