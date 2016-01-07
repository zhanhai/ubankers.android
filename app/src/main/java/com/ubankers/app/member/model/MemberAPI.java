package com.ubankers.app.member.model;

import com.ubankers.app.base.api.Response;

import retrofit.http.GET;
import retrofit.http.Headers;
import rx.Observable;

/**
 *
 */
public interface MemberAPI {
    @GET("/user/ajax/member/getqualify")
    @Headers("Cache-Control: no-cache")
    Observable<Response<CfmpQualificationStatus>> isQualifiedCfmp();
}
