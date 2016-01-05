package com.ubankers.app.base.session;

import cn.com.ubankers.www.user.model.UserBean;

/**
 *
 */
public interface SessionManager {
    void onLogin(UserBean user);
    void onLogout();

    Session getSession();
}
