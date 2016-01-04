package com.ubankers.app.product.detail.share;

import android.view.Gravity;
import android.view.View;

import com.ubankers.app.product.detail.ProductDetailActivity;

import cn.com.ubankers.www.utils.MessageDialog;
import cn.com.ubankers.www.widget.ActionItem;
import cn.com.ubankers.www.widget.TitlePopup;

/**
 *
 */
public class ProductSharePopup extends TitlePopup {

    private ProductDetailActivity activity;
    private View parent;

    public ProductSharePopup(final ProductDetailActivity activity, final View parent, int width, int height) {
        super(activity, width, height);
        this.activity = activity;
        this.parent = parent;

        setItemOnClickListener(new OnItemOnClickListener() {
            public void onItemClick(ActionItem item, int position) {
                if (position != 0) {
                    return;
                }

                if (!activity.isQualifiedCFMP()) {
                    new MessageDialog(activity).businessCardDialog();
                } else {
                    new ProductShareDialog(activity)
                            .showAtLocation(parent, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                }
            }
        }); //推荐，分享

        addAction(new ActionItem(activity, "分享"));
    }

}
