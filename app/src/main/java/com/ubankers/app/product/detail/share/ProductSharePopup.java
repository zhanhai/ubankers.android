package com.ubankers.app.product.detail.share;

import android.view.Gravity;
import android.view.View;

import com.ubankers.app.product.detail.ProductDetailActivity;
import com.ubankers.app.product.detail.ProductDetailView;

import cn.com.ubankers.www.utils.MessageDialog;
import cn.com.ubankers.www.widget.ActionItem;
import cn.com.ubankers.www.widget.TitlePopup;

/**
 *
 */
public class ProductSharePopup extends TitlePopup {

    private ProductDetailView view;
    private View parent;

    public ProductSharePopup(final ProductDetailView view, final View parent, int width, int height) {
        super(view.getContext(), width, height);
        this.view = view;
        this.parent = parent;

        setItemOnClickListener(new OnItemOnClickListener() {
            public void onItemClick(ActionItem item, int position) {
                if (position != 0) {
                    return;
                }

                if (!view.isQualifiedCfmp()) {
                    new MessageDialog(view.getContext()).businessCardDialog();
                } else {
                    new ProductShareDialog(view)
                            .showAtLocation(parent, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
                }
            }
        }); //推荐，分享

        addAction(new ActionItem(view.getContext(), "分享"));
    }

}
