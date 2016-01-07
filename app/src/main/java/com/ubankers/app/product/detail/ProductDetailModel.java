package com.ubankers.app.product.detail;

import android.content.Intent;

import com.ubankers.app.product.model.Product;

import cn.com.ubankers.www.product.model.ProductDetail;

/**
 *
 */
public class ProductDetailModel {
    private String productId;
    private String reserverName;
    private Product product;
    private boolean isQualifiedCfmp;

    public void init(final ProductDetailPresenter presenter, final Intent intent){
        productId = intent.getStringExtra(ProductDetailActivity.EXTRA_PRODUCT_ID);
        reserverName = intent.getStringExtra(ProductDetailActivity.EXTRA_RESERVER_NAME);
        product = Product.from((ProductDetail) intent.getSerializableExtra(ProductDetailActivity.EXTRA_PRODUCT_DETAIL));

        if(product != null && product.getProductId()!= null){
            productId = product.getProductId();
        }

        presenter.loadProductDetail(productId);
        presenter.verifyCfmpQualificationStatus();
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isQualifiedCfmp() {
        return isQualifiedCfmp;
    }

    public void isQualifiedCfmp(boolean isCertifiedCfmp) {
        this.isQualifiedCfmp = isCertifiedCfmp;
    }
}
