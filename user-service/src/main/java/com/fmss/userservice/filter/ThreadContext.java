package com.fmss.userservice.filter;

import com.fmss.commondata.configuration.UserContext;

public class ThreadContext {
    private static final ThreadLocal<UserContext> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(UserContext userContext) {
        currentUser.set(userContext);
    }

    public static UserContext getCurrentUser() {
        return currentUser.get();
    }

}
