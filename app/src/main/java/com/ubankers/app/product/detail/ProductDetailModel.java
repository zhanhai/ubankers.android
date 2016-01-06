package com.ubankers.app.product.detail;

import android.content.Intent;

import cn.com.ubankers.www.product.model.ProductDetail;

/**
 *
 */
public class ProductDetailModel {
    private String productId;
    private String reserverName;
    private ProductDetail productDetail;
    private boolean isQualifiedCfmp;

    public void init(Intent intent){
        productId = intent.getStringExtra(ProductDetailActivity.EXTRA_PRODUCT_ID);
        reserverName = intent.getStringExtra(ProductDetailActivity.EXTRA_RESERVER_NAME);
        productDetail = (ProductDetail) intent.getSerializableExtra(ProductDetailActivity.EXTRA_PRODUCT_DETAIL);

        if(productDetail != null && productDetail.getProductId()!= null){
            productId = productDetail.getProductId();
        }
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getReserverName() {
        return reserverName;
    }

    public void setReserverName(String reserverName) {
        this.reserverName = reserverName;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }

    public boolean isQualifiedCfmp() {
        return isQualifiedCfmp;
    }

    public void isQualifiedCfmp(boolean isCertifiedCfmp) {
        this.isQualifiedCfmp = isCertifiedCfmp;
    }
}
