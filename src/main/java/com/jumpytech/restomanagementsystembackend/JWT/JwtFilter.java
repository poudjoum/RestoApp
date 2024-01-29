package com.jumpytech.restomanagementsystembackend.JWT;

import io.jsonwebtoken.Claims;
import javax.servlet.FilterChain;
import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component

public class JwtFilter extends OncePerRequestFilter {
   private final JwtUtil jwtUtil;
    private final CustomerUsersDetailsService service;
    Claims claims=null;
   private String userName=null;

    public JwtFilter(JwtUtil jwtUtil, CustomerUsersDetailsService service) {
        this.jwtUtil = jwtUtil;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getServletPath().matches("/user/login|/user/forgotPassword|/user/signup|/user/update")){
            filterChain.doFilter(request,response);
        }else {
            String authorizationHeader=request.getHeader("Authorization");
            String token=null;
            if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
                token=authorizationHeader.substring(7);
                userName= jwtUtil.extractUsername(token);
                claims= jwtUtil.extractAllClaims(token);

            }
            if (userName!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails=service.loadUserByUsername(token);
                if (jwtUtil.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(request,response);
        }
    }
    public Boolean isAdmin(){
        return "Admin".equalsIgnoreCase((String) claims.get("role"));
    }
    public Boolean isUser(){
        return "user".equalsIgnoreCase((String) claims.get("role"));
    }
    public String getCurrentUser(){
        return userName;
    }

}
