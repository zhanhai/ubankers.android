package com.ubankers.app.product.model;


public class ProductReserveOptions {
    private String payType;//支付类型
    private String endTime;//预定结束时间： 毫秒
    private int maxMoney;//最大金额(以万来计算)
    private int minMoney;//最小金额(以万来计算)
    private int incrementalMoney;//递增金额
    private boolean canReserve;//能否预约
    private String minMoneyYuan;

    public ProductReserveOptions(){
        this.canReserve = false;
        this.minMoneyYuan = "";
        this.payType = "";
        this.endTime = "";
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(int maxMoney) {
        this.maxMoney = maxMoney;
    }

    public int getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(int minMoney) {
        this.minMoney = minMoney;
    }

    public int getIncrementalMoney() {
        return incrementalMoney;
    }

    public void setIncrementalMoney(int incrementalMoney) {
        this.incrementalMoney = incrementalMoney;
    }

    public boolean isCanReserve() {
        return canReserve;
    }

    public void setCanReserve(boolean canReserve) {
        this.canReserve = canReserve;
    }

    public String getMinMoneyYuan() {
        return minMoneyYuan;
    }

    public void setMinMoneyYuan(String minMoneyYuan) {
        this.minMoneyYuan = minMoneyYuan;
    }
}
