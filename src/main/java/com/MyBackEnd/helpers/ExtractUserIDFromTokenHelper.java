package com.MyBackEnd.helpers;

import com.MyBackEnd.services.JwtTokenServices;
import com.MyBackEnd.services.auth.MyCustomUserDetailsService;
import com.MyBackEnd.services.auth.MyCustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExtractUserIDFromTokenHelper {

    @Autowired
    private JwtTokenServices jwtTokenService;

    @Autowired
    private MyCustomUserDetailsService myCustomUserDetailService;

    private MyCustomUserDetails myCustomUserDetails;

    public int getUserIdFromToken(HttpServletRequest request){
        // SET USER ID INTEGER OBJECT:
        Integer user_id = null;
        // GET AUTHENTICATION HEADER:
        String authHeader = request.getHeader("Authorization");
        // SET JWT PROPERTY:
        String jwtToken = null;
        // SET USERNAME PROPERTY:
        String userEmail = null;

        // CHECK / VALIDATE AUTHORIZATION HEADER:
        if(authHeader != null || authHeader.startsWith("Bearer ")){
            // SET JWT TOKEN VALUE RETRIEVED FROM AUTHORIZATION HEADER:
            jwtToken = authHeader.substring(7);

            // EXTRACT THE USER EMAIL FROM JWT TOKEN:
            userEmail = jwtTokenService.extractUsername(jwtToken);
        }
        // END OF CHECK / VALIDATE AUTHORIZATION HEADER.


        // CHECK IF USERNAME / EMAIL IS NOT NULL AND SET ID IF BLOCK:
        if(userEmail != null){
            // GET USER DETAILS BY USER EMAIL:
            myCustomUserDetails = (MyCustomUserDetails) myCustomUserDetailService.loadUserByUsername(userEmail);

            // SET USER ID:
            user_id = myCustomUserDetails.getUserId();
        }
        // END OF CHECK IF USERNAME / EMAIL IS NOT NULL AND SET ID IF BLOCK:

        return user_id;
    }
    // END OF GET USER ID FROM TOKEN;
}
// END OF EXTRACT USER ID FROM TOKEN HELPER CLASS.