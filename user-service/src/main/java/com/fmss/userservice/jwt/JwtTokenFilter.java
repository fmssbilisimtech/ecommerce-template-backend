package com.fmss.userservice.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmss.commondata.configuration.UserContext;
import com.fmss.commondata.dtos.response.JwtTokenResponseDto;
import com.fmss.commondata.util.JwtUtil;
import com.fmss.userservice.configuration.UserDetailsConfiguration;
import com.fmss.userservice.filter.ThreadContext;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

import static com.fmss.userservice.constants.UserConstants.*;
import static java.util.Objects.nonNull;

@Slf4j
public class JwtTokenFilter implements Filter {

    private final JwtUtil jwtUtil = new JwtUtil();

    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final var token = parseJwt((HttpServletRequest) request);
        if (Strings.isEmpty(token)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            UserContext userContext = new UserContext();
            userContext.setUserId(jwtUtil.getUserDetailsFromToken(token).userId());
            final var chunks = token.split("\\.");
            final var decoder = Base64.getUrlDecoder();

            final var header = new String(decoder.decode(chunks[0]));
            final var payload = new String(decoder.decode(chunks[1]));
            final var userDetails = new ObjectMapper().readValue(payload, JwtTokenResponseDto.class);
            final var userName = userDetails.email();
            boolean isValidToken = jwtUtil.validateToken(token, userName);

            userContext.setUserName(userName);
            userContext.setUserId(userDetails.userId());
            ThreadContext.setCurrentUser(userContext);
            log.info("TokenValidateInterceptor::token validating:{}::userName:{}", isValidToken, userName);

        } catch (Exception e) {
            log.debug("logContextModel can not be init : {}", e.getMessage());
        }
        chain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        final var headerAuth = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
            return headerAuth.substring(7);
        }
        return "";
    }



    @Override
    public void destroy() {
        Filter.super.destroy();
    }

}
