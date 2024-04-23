package com.jwttut.config;

import com.jwttut.helper.JwtUtil;
import com.jwttut.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class JwtAuthnFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // get jwt
        String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;

        if(StringUtils.hasLength(requestTokenHeader) && requestTokenHeader.startsWith("Bearer ")){
            // see if authz header starts with Bearer
            // token starts from index number 7
            jwtToken = requestTokenHeader.substring(7);

            System.out.println("Intercepted JWT : " + jwtToken);

            try{
                username = jwtUtil.extractUsername(jwtToken);
                System.out.println("Intercepted username : " + username);
            }
            catch (Exception exception){
                exception.printStackTrace();
            }

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // validate
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            else{
                System.out.println("Token is not validated!");
            }

        }
        filterChain.doFilter(request,response);

    }
}
