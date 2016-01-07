package com.ubankers.app.product.model;


public class ProductSaleOptions {
    private ProductReserveOptions reserveOptions = new ProductReserveOptions();

    public ProductReserveOptions getReserveOptions() {
        return reserveOptions;
    }

    public void setReserveOptions(ProductReserveOptions reserveOptions) {
        if(reserveOptions == null){
            return;
        }

        this.reserveOptions = reserveOptions;
    }
}
