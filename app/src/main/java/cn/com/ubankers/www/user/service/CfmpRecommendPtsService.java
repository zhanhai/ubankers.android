package cn.com.ubankers.www.user.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import cn.com.ubankers.www.application.MyApplication;
import cn.com.ubankers.www.http.HttpConfig;
import cn.com.ubankers.www.user.controller.activity.CfmpRecommerActivity;
import cn.com.ubankers.www.user.model.RecommendPtsBean;
import cn.com.ubankers.www.user.view.CfmpProductAdapter;
import cn.com.ubankers.www.utils.Tools;
import cn.com.ubankers.www.widget.MyDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
/**
 * 
 * @author 廖准华
 *财富是推荐产品的service
 */
public class CfmpRecommendPtsService {
	private Context context;
    private AsyncHttpClient client;
	private String userId;
	private ListView lv;
	private List<RecommendPtsBean> list;
	private static  MyDialog myDialog;
	public CfmpRecommendPtsService(Context context,String userId,ListView lv,List<RecommendPtsBean> list){
		this.context = context;
		this.userId = userId;
		this.lv = lv;
		this.list = list;
		client = MyApplication.app.getClient(context);
		if(myDialog==null){
			myDialog=MyDialog.createDialog(context,"正在加载中...");
		}else{
			myDialog=MyDialog.createDialog(context,"正在加载中...");
		}
	}
	public void initData() {
		myDialog.show();
		StringEntity entity=null;
		try {	
			JSONObject json = new JSONObject();
			json.put("investorId",userId);
			entity =new StringEntity(json.toString(),"utf-8");
		} catch (Exception e1) {
				e1.printStackTrace();
		}
		client.post(context,HttpConfig.URL_RECOMMENT_PRODUCT,entity,"application/json",new JsonHttpResponseHandler(){
			 @Override
			public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			
				try {
					boolean flag = response.getBoolean("success");
					if(flag){
						JSONObject jsonObject = response.getJSONObject("result");
						String errorCode = jsonObject.getString("errorCode");
						//totalCount
						int totalCount = jsonObject.getInt("totalCount");
						if(errorCode.equals("success")){//总记录的条数
							JSONArray jsonArray = jsonObject.getJSONArray("list");
							if(jsonArray.length()==0){
								Toast.makeText(context, "您所绑定的财富师暂未给您推荐产品", Toast.LENGTH_SHORT).show();
							}else{
							for(int i=0;i<jsonArray.length();i++){
								/**
								 * "addTime": 1435297676604,
					                "begin": 0,
					                "cfmpId": 33565,
					                "cfmpMobile": "15721529173",
					                "cfmpNickName": "财富师1",
					                "dir": "",
					                "end": 1000,
					                "id": 101137,
					                "investorId": 33564,
					                "investorIds": [],
					                "investorMobile": "15121148971",
					                "investorNickName": "user33564",
					                "isOrdered": false,
					                "limit": 0,
					                "minSureBuyPrice": 1,
					                "productId": 100033,
					                "productName": "qqq",
					                "productRate": "11",
					                "reason": "",
					                "sortName": "",
					                "start": 0
							    */
								
									
								Log.e("112121212","@@@@@@@###");
								
								RecommendPtsBean cfmpProductBean = new RecommendPtsBean();
								JSONObject object = jsonArray.getJSONObject(i);
								String addTime = object.getString("addTime");
								String productName =object.getString("productName");
								String productRate = object.getString("productRate");
								String minSureBuyPrice = object.getString("minSureBuyPrice");
								String productId=object.getString("productId");
								BigDecimal db = new BigDecimal(minSureBuyPrice);//科学计数法转换成数字
								minSureBuyPrice = db.toPlainString();
								minSureBuyPrice=Tools.Intercept(minSureBuyPrice);
								cfmpProductBean.setAddTime(addTime);
								cfmpProductBean.setMinSureBuyPrice(minSureBuyPrice);
								cfmpProductBean.setProductName(productName);
								cfmpProductBean.setProductRate(productRate);
								cfmpProductBean.setProductId(productId);
								list.add(cfmpProductBean);
							}
							}
						}
						CfmpProductAdapter adapter = new CfmpProductAdapter(context, list);
						lv.setAdapter(adapter);
						myDialog.dismiss();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Log.e("jkdkjjkdkjd","失败啦！！！！！");
				super.onFailure(statusCode, headers, responseString, throwable);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Toast.makeText(context, "请求超时，请重试", Toast.LENGTH_SHORT).show();
				myDialog.dismiss();
			}
			
		});
		
	}
}
