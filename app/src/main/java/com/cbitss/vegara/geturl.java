package com.cbitss.vegara;

import java.io.Serializable;

public class geturl implements Serializable {
   private String phone,email;
   private String apiurl = "https://yegara.com/app/api/v1?";
    private String url = "";
    private String id;
    private String domain="";
    private String id_token,pakage_id,pakage_name;

    geturl(String phone,String email,String id)
    {
        this.phone=phone;
        this.email=email;
        this.id = id;
        this.id_token = "&phone=09"+phone+"&key="+id+"&token=6IzFIqywVx7T6HHR4KjLadD5rqVatcG5";
    }
    public geturl(String phone, String id){
        this.phone = phone;
        this.id = id;
        this.id_token ="&phone=09"+phone+"&key="+id+"&token=6IzFIqywVx7T6HHR4KjLadD5rqVatcG5";
    }

    public geturl() {
    }

    private void urlr(){
        url=apiurl;
    }
    String verifyRequestUrl(){
        urlr();
        url+= "command=otp&email="+email+id_token;
        return url;
    }



    public String verifyOtpUrl(String Otp){
        urlr();
        url += "command=verify&code="+Otp+id_token;
        return url;
    }

    public String searchurl(String search){
        urlr();
        url+="command=search&domain="+search+id_token;
        this.domain = search;
        return url;
    }
    public String placeorderurl(String bank){
        urlr();
        url += "command=order&domain="+domain+"&package_name="+pakage_name+"&package_id="+pakage_id+"&bank_name="+bank+id_token;
        return url;
    }
    public String confirmorderurl(String order_id,String name,String reference_number,String Date){
        urlr();
        url += "command=confirm&domain="+domain+"&order_id="+order_id+"&paymentby="+name+"&ref="+reference_number+"&paymentdate="+Date+id_token;
        return url;
    }
    public String carturl() {
        urlr();
        url += "command=cart"+id_token;
        return url;
    }
    public String deleteorderurl(String cart_doamin,String cart_orderid){
        urlr();
        url += "command=deleteorder&domain="+cart_doamin+"&order_id="+cart_orderid+id_token;
        return url;
    }

    public String serviceurl(){
        urlr();
        url += "command=service"+id_token;
        return url;
    }

    public String logouturl(){
        urlr();
        url += "command=logout"+id_token;
        return url;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getDomain() {
        return domain;
    }

    public void setPakage_id(String pakage_id) {
        this.pakage_id = pakage_id;
    }

    public void setPakage_name(String pakage_name) {
        this.pakage_name = pakage_name;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
