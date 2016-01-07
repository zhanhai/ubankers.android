package com.ubankers.app.member;

import com.ubankers.app.member.model.MemberAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Retrofit;

/**
 *
 */
@Module
public class MemberModule {
    @Provides
    @Singleton
    MemberAPI provideMemberAPI(Retrofit retrofit){
        return retrofit.create(MemberAPI.class);
    }
}
