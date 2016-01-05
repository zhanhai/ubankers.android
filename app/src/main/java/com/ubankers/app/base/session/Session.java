package com.ubankers.app.base.session;

import javax.inject.Inject;

import cn.com.ubankers.www.user.model.UserBean;

/**
 *
 */
public class Session {
    public static final String USER_ROLE_CFMP = "cfmp";
    public static final String USER_ROLE_INVESTOR = "investor";

    @Inject
    SessionManager sessionManager;

    private UserBean currentUser;

    public Session(UserBean currentUser){
        this.currentUser = currentUser;
    }

    public boolean isLogin(){
        return currentUser != null;
    }

    public void invalidate(){
        this.currentUser = null;
        sessionManager.onLogout();

        //TODO Should send out logout event here
    }

    public boolean isCfmp(){
        return isLogin() && USER_ROLE_CFMP.equals(currentUser.getUserRole());
    }

    public boolean isInvestor(){
        return isLogin() && USER_ROLE_INVESTOR.equals(currentUser.getUserRole());
    }

}
