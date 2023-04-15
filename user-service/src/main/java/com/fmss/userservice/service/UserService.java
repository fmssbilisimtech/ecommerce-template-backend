package com.fmss.userservice.service;

import com.fmss.commondata.util.JwtUtil;
import com.fmss.userservice.exeption.RestException;
import com.fmss.userservice.exeption.UserAlreadyExistException;
import com.fmss.userservice.mail.MailingService;
import com.fmss.userservice.model.LdapUser;
import com.fmss.userservice.model.User;
import com.fmss.userservice.repository.LdapRepository;
import com.fmss.userservice.repository.UserRepository;
import com.fmss.userservice.request.UserRegisterRequestDto;
import com.fmss.userservice.security.EcommerceUserDetailService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.InvalidNameException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

import static com.fmss.userservice.constants.UserConstants.*;
import static com.fmss.userservice.util.AppSettingsKey.CREATE_PASSWORD_URL_FORMAT;
import static com.fmss.userservice.util.AppSettingsKey.RESET_PASSWORD_URL_FORMAT;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    @Lazy
    private final PasswordEncoder passwordEncoder;
    private final MailingService mailingService;
    private final JwtUtil jwtTokenUtil;

    private final LdapRepository ldapRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    //@PostConstruct
    public void init() throws InvalidNameException {
//        final var ldapUser = new LdapUser();
//        //ldapUser.setId(new LdapName("uid=sercan1,o=64346c9136393df65a68908f,dc=jumpcloud,dc=com"));
//        ldapUser.setGivenName("mahoni");
//        ldapUser.setUid("mahoni");
//        ldapUser.setSn("mahoni");
//        ldapUser.setMail("muhammed.alagoz@fmsstech.com");
//        ldapUser.setUserPassword(passwordEncoder.encode("1234"));
//
//        ldapRepository.create(ldapUser);
//        var user = ldapRepository.findUser("sercan@a.com");
//        System.out.println(user);
        sendForgotPasswordMail("muhammed.alagoz@fmsstech.com");
    }

    @Transactional
    public void registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        if (existByEmail(userRegisterRequestDto.email())) {
            log.info("registerUser() method generate user already exist :{}", userRegisterRequestDto.userName());
            throw new UserAlreadyExistException("User already exist");
        }
        final var user = userRegisterRequestDto.toUser();
        user.setUserPassword(passwordEncoder.encode(userRegisterRequestDto.password()));
        ldapRepository.create(user);
    }

    @Transactional
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void sendForgotPasswordMail(String username) {
        final var ldapUser = ldapRepository.findUser(username);
        log.info("sendForgetLink(userName) method entry :{}", username);
        final String link = createForgotPasswordLink(ldapUser);
        log.info("password forget link method entry :{}", link.isEmpty());
        mailingService.sendForgotPasswordEmail(ldapUser.getMail(), ldapUser.getGivenName(), ldapUser.getSn(), link);
    }

    @Transactional
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(CUSTOMER_ID_INVALID));
    }

    @Transactional
    public User findByEmail(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }


    @Transactional(readOnly = true)
    public EcommerceUserDetailService getUserByForgotPasswordToken(String uid, String token) {
        if (!getTokenEncoded(token)) {
            throw new RestException(INVALID_TOKEN);
        }
        final LdapUser ldapUser = getUserByEncodedUserId(uid);
        return new EcommerceUserDetailService(ldapUser);
    }

    @Transactional
    public void resetPassword(String uid, String password) {
        changePassword(uid, password);
    }


    @Transactional
    public void changePassword(String email, String currentPassword, String newPassword) {
        final var ldapUser = ldapRepository.findUser(email);
//        if (passwordEncoder.matches(currentPassword, ldapUser.getUserPassword())) {
        if (ldapAuth(email, currentPassword)) {
            changeUserPassword(ldapUser, newPassword);
        } else {
            throw new RestException(CURRENT_AND_BEFORE_PASSWORD_NOT_MATCH);
        }
    }

    private boolean ldapAuth(String username, String password) {
        try {
            return ldapRepository.checkPassword(username, password);
        } catch (Exception e) {
            log.error("Password not matched : " + username, e);
            return false;
        }
    }

    @Transactional
    public EcommerceUserDetailService getUserByCreatePasswordToken(String uid, String token) {
        final LdapUser user = getUserByEncodedUserId(uid);
        if (!StringUtils.equals(token, user.generateCreatePasswordToken())) {
            throw new RestException(INVALID_TOKEN);
        }
        return new EcommerceUserDetailService(user);
    }

    @Transactional
    public void createPassword(String uid, String password) {
        changePassword(uid, password);
    }

    private LdapUser getUserByEncodedUserId(String encodedUserId) {
        try {
            final byte[] bytes = Base64.decodeBase64(URLDecoder.decode(encodedUserId, StandardCharsets.UTF_8));
            final var userIdLong = new String(bytes, StandardCharsets.UTF_8);
            return ldapRepository.findUser(userIdLong);
        } catch (Exception e) {
            throw new RestException(INVALID_TOKEN, e);
        }
    }

    private boolean getTokenEncoded(String token) {
        try {
            final byte[] bytes = Base64.decodeBase64(URLDecoder.decode(token, StandardCharsets.UTF_8));
            final var timeString = new String(bytes, StandardCharsets.UTF_8);
            final var instant = Instant.ofEpochMilli(Long.parseLong(timeString));

            final var localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            return ChronoUnit.MINUTES.between(localDateTime, LocalDateTime.now()) < 5;
        } catch (Exception e) {
            throw new RestException(INVALID_TOKEN, e);
        }
    }

    private void changePassword(String encodedUserId, String password) {
        final LdapUser user = getUserByEncodedUserId(encodedUserId);
        log.info("user change password  entry:{}", user.getMail());
        changeUserPassword(user, password);
    }

    private void changeUserPassword(LdapUser user, String password) {
        log.info("user create forgot password link :{}", user.getMail());
        user.setUserPassword(passwordEncoder.encode(password));
        ldapRepository.updateUserPassword(user.getCn(), user.getUserPassword());
    }

    private String createNewUserPasswordLink(LdapUser ldapUser) {
        final String token = ldapUser.generateCreatePasswordToken();
        String url = null;
        if (StringUtils.isEmpty(url)) {
            url = "http://89.19.23.50:3000";
        }
        return String.format(CREATE_PASSWORD_URL_FORMAT, url, token, createBase64UserUid(ldapUser));
    }

    private String createBase64UserUid(LdapUser ldapUser) {
        return Base64.encodeBase64URLSafeString(ldapUser.getMail().getBytes());
    }

    private String createForgotPasswordLink(LdapUser user) {
        final String token = "test";
        log.info("user create forgot password link :{}", user.getUid());
        String url = null;
        if (StringUtils.isEmpty(url)) {
            url = "http://89.19.23.50:3000";
        }
        return String.format(RESET_PASSWORD_URL_FORMAT, url, token, createBase64UserUid(user));
    }

    private static String generateTime() {
        try {
            long epochMilli = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
            return Base64.encodeBase64URLSafeString(String.valueOf(epochMilli).getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
            return Base64.encodeBase64URLSafeString(String.valueOf(new Date().getTime()).getBytes());
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        log.info("validate otp token entry :{}", userDetails.getUsername());
        return jwtTokenUtil.validateToken(token, userDetails.getUsername());
    }

    private String generateOtpToken(LdapUser user) {
        log.info("generate otp token entry :{}", user.getUid());
        final var random = new Random();
        final var randomNumber = random.nextInt(900000) + 100000;
        redisTemplate.opsForValue().set(user.getUid() + OTP_REDIS_KEY, String.valueOf(randomNumber), Duration.ofMinutes(2));
        return String.valueOf(randomNumber);
    }

    public void sentOtp(String email) {
        log.info("user send otp mothod entry :{}", email);
        final var ldapUser = ldapRepository.findUser(email);
        final String otp = generateOtpToken(ldapUser);
        mailingService.sendOtpEmail(ldapUser.getMail(), ldapUser.getGivenName(), ldapUser.getSn(), otp);
    }

    public boolean verifyOtp(String email, String otp) {
        log.info("user verify otp mothod entry :{}", email);
        try {
            final var user = ldapRepository.findUser(email);
            String sessionOtp = (String) redisTemplate.opsForValue().get(user.getUid() + OTP_REDIS_KEY);
            if (!StringUtils.equals(otp, sessionOtp)) {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
