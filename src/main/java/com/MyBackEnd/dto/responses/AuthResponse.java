package com.MyBackEnd.dto.responses;

import com.MyBackEnd.services.auth.MyCustomUserDetails;
import com.MyBackEnd.services.auth.MyCustomUserDetailsService;

public class AuthResponse {
    private String token;
    private MyCustomUserDetails myCustomUserDetails;

    public AuthResponse(String token , MyCustomUserDetails myCustomUserDetails){
        this.token = token;
        this.myCustomUserDetails = myCustomUserDetails;
    }

    public String getToken(){
        return token;
    }
    public int getUserId(){
        return this.myCustomUserDetails.getUserId();
    }

    public String getUsername(){
        return this.myCustomUserDetails.getUsername();
    }

    public String getFirstName(){
        return this.myCustomUserDetails.getFirstName();
    }

    public String getLastName(){
        return this.myCustomUserDetails.getLastName();
    }
}
