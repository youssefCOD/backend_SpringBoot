package com.MyBackEnd.config.filters;

import com.MyBackEnd.services.JwtTokenServices;
import com.MyBackEnd.services.auth.MyCustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenServices jwtTokenServices;

    @Autowired
    private MyCustomUserDetailsService myCustomUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //GET authentication header
        String authHeader = request.getHeader("Authorization");

        //GET JWT Property
        String jwtToken = null;

        //SET username Property
        String userEmail = null;

        //check /validate authorization header if block
        if (authHeader ==null || !authHeader.startsWith("bearer ")){
            filterChain.doFilter(request, response);
            //kill code execution here
            return;
        }
        //end of check / validate authorization header if block.

        //set jwt value
        jwtToken = authHeader.substring(7);

        //extract username from token
        userEmail = jwtTokenServices.extractUsername(jwtToken);

        //check if user is null and is authenticated
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = myCustomUserDetailsService.loadUserByUsername(userEmail);
            if (jwtTokenServices.validateToken(jwtToken, userDetails)){
                UsernamePasswordAuthenticationToken userAuthToken
                        = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                userAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(userAuthToken);
            }

        }
        filterChain.doFilter(request, response);
    }
}
//END OF JWT Auth Filter