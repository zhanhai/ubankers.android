package com.ubankers.app.member.model;

/**
 *
 */
public class CfmpQualificationStatus {
    private int qualified;

    public boolean isQualified() {
        return qualified == 0;
    }


    public void setQualified(int qualified) {
        this.qualified = qualified ;
    }
}
