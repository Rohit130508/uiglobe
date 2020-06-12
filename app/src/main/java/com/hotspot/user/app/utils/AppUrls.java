package com.hotspot.user.app.utils;

public class AppUrls {

    public static String BaseUrl = "http://uniglobe.flyaway.co.in/api/";


    public static String checkPhone = BaseUrl+"UserIdValidation?UserId=";

    public static String Registration = BaseUrl+"Register?UserId=";
    public static String Login = BaseUrl+"Login?UserId=";


    public static String PinCodeValidation = BaseUrl+"PinCodeValidation?PinCodeNo=";

//   &Name={Name}&Dob={Dob}&Gender={Gender}&ImgUrl={ImgUrl}&City={City}&Pan={Pan}&Aadhar={Aadhar}&PanUrl={PanUrl}&AadharUrl={AadharUrl}
    public static String ProfileKYC = BaseUrl+"Profile?UserId=";
}
