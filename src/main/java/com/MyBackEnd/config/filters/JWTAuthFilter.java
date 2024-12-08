package com.MyBackEnd.config.filters;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.MyBackEnd.controllers.ProjectController;
import com.MyBackEnd.services.JwtTokenServices;
import com.MyBackEnd.services.auth.MyCustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private JwtTokenServices jwtTokenServices;

    @Autowired
    private MyCustomUserDetailsService myCustomUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {



        // GET authentication header
        String authHeader = request.getHeader("Authorization");

        // GET JWT Property
        String jwtToken;

        // SET username Property
        String userEmail;

        //check /validate authorization header if block
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            log.debug("problem here 1");
            //kill code execution here
            return;
        }
        //end of check / validate authorization header if block.

        //set jwt value
        jwtToken = authHeader.substring(7);

        //for printing the jwt token in the console
        log.info("________Authorization Header:_______  " + jwtToken);
        log.debug(jwtToken);
        //extract username from token
        userEmail = jwtTokenServices.extractUsername(jwtToken);

        System.out.println(userEmail);

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


        logger.debug("Auth header: " + authHeader);
        logger.debug("JWT: " + jwtToken);
        logger.debug("User email: " + userEmail);


        filterChain.doFilter(request, response);
    }
}
//END OF JWT Auth Filter