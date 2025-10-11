package com.firstproject.firstproject.filter;

import com.firstproject.firstproject.service.UserDetailsServiceImpl;
import com.firstproject.firstproject.utils.JwtUtil;
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
public class JwtFilter extends OncePerRequestFilter { /* one request only get filtered one time only */

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    /* overriding this function from super class which defines internal working of any requst*/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization"); /* in any header there is one field called authorization now we passed bearer token(JWT) from any client, after that we are extracting that from header and storing*/
        String username = null;
        String jwt = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }
        if(username != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(jwtUtil.validateToken(jwt))  {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); /*adding userDetails in the auth*/
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); /*adding details of the request in auth ex.,ip */
                SecurityContextHolder.getContext().setAuthentication(auth); /* adding the auth in security context holder */
            }
        }
        response.addHeader("admin", "divy");
        chain.doFilter(request, response); /* passing the request and response forward into the chain if any other filter is remaining*/
    }
}
