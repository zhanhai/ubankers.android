package com.ubankers.app.base;

import android.app.Application;

import com.ubankers.app.base.session.Session;
import com.ubankers.app.base.session.SessionManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module
public class AppModule {

    private final Application app;
    private final SessionManager sessionManager;

    public AppModule(Application app, SessionManager sessionManager){
        this.app = app;
        this.sessionManager = sessionManager;
    }

    @Provides
    @Singleton
    public Application provideApp(){
        return app;
    }

    @Provides
    public Session provideSession(){
        return sessionManager.getSession();
    }


}
