package com.fmss.userservice.configuration;

import com.fmss.userservice.model.LdapUser;
import com.fmss.userservice.security.EcommerceUserDetailService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

    @NotNull
    public static Optional<LdapUser> getUser() {
        return getUserDetails()
                .map(EcommerceUserDetailService::getDelegate);
    }

    @NotNull
    public static Optional<Authentication> getAuthentication() {
        return ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication);
    }

    @NotNull
    public static Optional<EcommerceUserDetailService> getUserDetails() {
        return getAuthentication()
                .map(Authentication::getPrincipal)
                .filter(EcommerceUserDetailService.class::isInstance)
                .map(EcommerceUserDetailService.class::cast);
    }

    public static void setAuthenticated(UserDetails userDetails) {
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
