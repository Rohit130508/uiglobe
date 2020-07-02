package com.hotspot.user.app.utils;

public class AppUrls {

    public static String BaseUrl = "http://uniglobe.flyaway.co.in/api/";


    public static String checkPhone = BaseUrl+"UserIdValidation/Get?UserId=";

    public static String Registration = BaseUrl+"Register/Post?UserId=";
    public static String Login = BaseUrl+"Login/Post?UserId=";


    public static String PinCodeValidation = BaseUrl+"PinCodeValidation/Get?PinCodeNo=";

//   &Name={Name}&Dob={Dob}&Gender={Gender}&ImgUrl={ImgUrl}&City={City}&Pan={Pan}&Aadhar={Aadhar}&PanUrl={PanUrl}
//   {AadharUrl}
    public static String ProfileKYC = BaseUrl+"Profile/Post";


    public static String ABOUTUS = "http://uniglobenidhi.in/about.html";
    public static String CONTACTUS = "http://uniglobenidhi.in/contact.html";
    public static String BasicInfo = BaseUrl +"BasicInfo/Post";

//    public static String ProfileKYC = BaseUrl+"Profile?UserId=";
}
