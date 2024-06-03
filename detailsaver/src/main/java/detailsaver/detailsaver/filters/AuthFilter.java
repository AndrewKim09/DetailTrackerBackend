package detailsaver.detailsaver.filters;

import detailsaver.detailsaver.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


public class AuthFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("doFilter");
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String authHeader = httpRequest.getHeader("Authorization");
        System.out.println("authHeader: " + authHeader);
        if(authHeader != null){
            String[] authHeaderArr = authHeader.split("Bearer ");
            if(authHeaderArr.length == 2 && authHeaderArr[1] != null){
                String token = authHeaderArr[1];
                try{
                    Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY).build().parseSignedClaims(token).getBody();
                    httpRequest.setAttribute("userId", Integer.parseInt(claims.get("userId").toString()));
                    System.out.println("User ID: " + Integer.parseInt(claims.get("userId").toString()));
                } catch(Exception e){
                    httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "invalid/expired token");
                    return;
                }
            }
            else{
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be Bearer [token]");
            }
        }
        else{
            httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "authorization token must be provided");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
