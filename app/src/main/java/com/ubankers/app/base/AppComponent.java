package com.ubankers.app.base;

import com.ubankers.app.base.session.Session;

import javax.inject.Singleton;

import dagger.Component;

/**
 *
 */
@Singleton
@Component(modules=AppModule.class)
public interface AppComponent {
    Session session();
}
