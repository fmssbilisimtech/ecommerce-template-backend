package com.fmss.apigateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmss.apigateway.dto.JwtTokenDto;
import com.fmss.commondata.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Base64;

import static org.apache.commons.lang.BooleanUtils.isFalse;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenValidateInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    private final WebClient webClient;
    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    @Value("${jwt.enabled:false}")
    private boolean isJwtEnabled;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (isFalse(isJwtEnabled)) {
            return true;
        }

        try {
            final var token = parseJwt(request);
            if (!StringUtils.hasText(token)) {
                return false;
            }
            final var chunks = token.split("\\.");
            final var decoder = Base64.getUrlDecoder();

            final var header = new String(decoder.decode(chunks[0]));
            final var payload = new String(decoder.decode(chunks[1]));
            final var userDetails = new ObjectMapper().readValue(payload, JwtTokenDto.class);
            final var userName = userDetails.getUserName();
            boolean isValidToken = jwtUtil.validateToken(token, userName);

            String userId = userDetails.getUserId();
            WebClient.builder()
                    .defaultHeader("userId", userId)
                    .build();

            log.info("TokenValidateInterceptor::token validating:{}::userName:{}", isValidToken, userName);
            return isValidToken;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private String parseJwt(HttpServletRequest request) {
        final var headerAuth = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
            return headerAuth.substring(7);
        }
        return "";
    }

}
