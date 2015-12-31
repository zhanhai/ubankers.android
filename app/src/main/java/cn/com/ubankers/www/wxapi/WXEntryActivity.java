package cn.com.ubankers.www.wxapi;


import android.widget.Toast;
import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WechatHandlerActivity;

public class WXEntryActivity extends WechatHandlerActivity
{
  public void onGetMessageFromWXReq(WXMediaMessage paramWXMediaMessage)
  {
    startActivity(getPackageManager().getLaunchIntentForPackage(getPackageName()));
  }

  public void onShowMessageFromWXReq(WXMediaMessage paramWXMediaMessage)
  {
    if ((paramWXMediaMessage != null) && (paramWXMediaMessage.mediaObject != null) && ((paramWXMediaMessage.mediaObject instanceof WXAppExtendObject)))
      Toast.makeText(this, ((WXAppExtendObject)paramWXMediaMessage.mediaObject).extInfo, 0).show();
  }
}
