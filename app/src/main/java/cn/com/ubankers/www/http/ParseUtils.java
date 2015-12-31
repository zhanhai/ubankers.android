package cn.com.ubankers.www.http;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import cn.com.ubankers.www.product.model.ProductDetail;
import cn.com.ubankers.www.user.model.InvestorOrderBean;
import cn.com.ubankers.www.product.model.PagerBean;
import cn.com.ubankers.www.product.model.ReserverBean;
import cn.com.ubankers.www.sns.model.ArticleBean;
import cn.com.ubankers.www.sns.model.AuthorBean;
import cn.com.ubankers.www.sns.model.ColArticleBean;
import cn.com.ubankers.www.sns.model.CommentBean;
import cn.com.ubankers.www.sns.model.ContentSourceBean;
import cn.com.ubankers.www.user.model.AddCfmpBean;
import cn.com.ubankers.www.user.model.AttributesBean;
import cn.com.ubankers.www.user.model.BaseBean;
import cn.com.ubankers.www.user.model.BindCfmpBean;
import cn.com.ubankers.www.user.model.CustomerBean;
import cn.com.ubankers.www.user.model.MembersBean;
import cn.com.ubankers.www.user.model.MoneyBean;
import cn.com.ubankers.www.user.model.RecommendPtsBean;
import cn.com.ubankers.www.user.model.UserBean;
import cn.com.ubankers.www.user.model.UserNewBean;
import cn.com.ubankers.www.user.model.WealthBean;

public class ParseUtils {
	/**
	 * 注册-解析获取用户信息
	 * @param param
	 * @return
	 */
	 public  static UserBean parseRegist(String param){
	     UserBean info=new Gson().fromJson(param, UserBean.class);
	 return info;
	}
	/**
	 * 解析获取产品信息
	 * @param param
	 * @return
	 */
	public  static ProductDetail parseProduct(JSONObject object){
		ProductDetail productDetail = new ProductDetail();
		try{
			if(!object.isNull("id")){
				productDetail.setProductId(object.getString("id"));
			}else{
				productDetail.setProductId("0");}
			if(!object.isNull("productName")){
				productDetail.setProductName(object.getString("productName"));
			}else{
				productDetail.setProductName("");}
			if(!object.isNull("moduleId")){
				productDetail.setModuleId(object.getString("moduleId"));
			}else{
				productDetail.setModuleId("0");}
			if(!object.isNull("state")){
				productDetail.setState(object.getInt("state"));
			}else{
				productDetail.setState(0);}
			if(!object.isNull("isHot")){
				productDetail.setIsHot(object.getInt("isHot"));
			}else{
				productDetail.setIsHot(0);}
			if(!object.isNull("productTerm")){
				productDetail.setProductTerm(object.getString("productTerm"));
			}else{
				productDetail.setProductTerm("");}
			if(!object.isNull("countProductRate")){
				productDetail.setCountProductRate(object.getString("countProductRate"));
			}else{
				productDetail.setCountProductRate("");}
			if(!object.isNull("minSureBuyPrice")){
				productDetail.setMinSureBuyPrice(object.getString("minSureBuyPrice"));
			}else{
				productDetail.setMinSureBuyPrice("0");}
			if(!object.isNull("raisedProcessShow")){
				productDetail.setRaisedProcessShow(object.getInt("raisedProcessShow"));
			}else{
				productDetail.setRaisedProcessShow(0);}
		}catch(JSONException e){
			e.printStackTrace();
		}
	 return productDetail;
	}
	/**
	 * 解析获取文章信息
	 * @param object
	 * @return
	 */
	public  static ArticleBean parseArticle(JSONObject object){
		ArticleBean  articleBean = new ArticleBean();
		try{
			if(!object.isNull("_id")){
				articleBean.set_id(object.getString("_id"));
			}
			if(!object.isNull("_contentType")){
				articleBean.set_contentType(object.getString("_contentType"));
			}
			if(!object.isNull("title")){
				articleBean.setTitle(object.getString("title"));
			}
			if(!object.isNull("content")){
				articleBean.setContent(object.getString("content"));
			}
			if(!object.isNull("abstract")){
				articleBean.setAbstracting(object.getString("abstract"));
			}
			if(!object.isNull("cover")){
				articleBean.setCover(object.getString("cover"));
			}
			if(!object.isNull("__v")){
				articleBean.set__v(object.getInt("__v"));
			}
			if(!object.isNull("sign")){
				articleBean.setSign(object.getString("sign"));
			}
			if(!object.isNull("resource_address")){
				articleBean.setResource_address(object.getString("resource_address"));
			}
			if(!object.isNull("favor_count")){
				articleBean.setFavor_count(object.getInt("favor_count"));
			}
			if(!object.isNull("refer_count")){
				articleBean.setRefer_count(object.getInt("refer_count"));
			}
			if(!object.isNull("vote_count")){
				articleBean.setVote_count(object.getInt("vote_count"));
			}
			if(!object.isNull("comment_count")){
				articleBean.setComment_count(object.getInt("comment_count"));
			}
			if(!object.isNull("read_count")){
				articleBean.setRead_count(object.getInt("read_count"));
			}
			if(!object.isNull("create_date")){
				articleBean.setCreate_date(object.getString("create_date"));
			}
			if(!object.isNull("is_voted")){
				articleBean.setIs_voted(object.getBoolean("is_voted"));
			}else{
				articleBean.setIs_voted(false);
			}
			if(!object.isNull("is_favored")){
				articleBean.setIs_favored(object.getBoolean("is_favored"));
			}else{
				articleBean.setIs_favored(false);
			}
			if(!object.isNull("tags")){
				articleBean.setTags(object.getString("tags"));
			}
			if(!object.isNull("author")){
				JSONObject author = object.getJSONObject("author");
				AuthorBean authorBean = new AuthorBean();
			if(!author.isNull("author_id")){
				authorBean.setAuthor_id(author.getString("author_id"));
			}
			if(!author.isNull("author_name")){
				authorBean.setAuthor_name(author.getString("author_name"));
			}
			if(!author.isNull("userface")){
				authorBean.setUserface(author.getString("userface"));
			}
			articleBean.setIdentifying(author.get("identifying") == null|| author.get("identifying").equals("null") ? 0: author.getInt("identifying"));
//			if(!author.isNull("identifying")){
//				authorBean.setIdentifying(author.getInt("identifying"));	
//			}
			if(!author.isNull("role")){
				authorBean.setRole(author.getInt("role"));
			}
			articleBean.setAuthor(authorBean);
			} 
			if(!object.isNull("contentSource")){
				JSONObject contentSource = object.getJSONObject("contentSource");
				ContentSourceBean sourceBean = new ContentSourceBean();
				if(!contentSource.isNull("source_type")){
					sourceBean.setSource_type(contentSource.getString("source_type"));
				}
				if(!contentSource.isNull("source_detail")){
					sourceBean.setSource_detail(contentSource.getString("source_detail"));
				}
				
				articleBean.setSourceBean(sourceBean);
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		 return articleBean;
	}
	/**
	 * 解析获取绑定财富师信息
	 * @param param
	 * @return
	 */
	public  static BindCfmpBean parseBindCfmp(String param){
		BindCfmpBean info=new Gson().fromJson(param, BindCfmpBean.class);
	 return info;
	}
	/**
	 * 解析获取财富工作室信息
	 * @param param
	 * @return
	 */
	public  static WealthBean parseWealth(String param){
		WealthBean info=new Gson().fromJson(param, WealthBean.class);
		return info;
	}
	/**
	 * 解析获取预约状态信息
	 * @param param
	 * @return
	 */
	public  static ReserverBean parseReserver(String param){
		ReserverBean info=new Gson().fromJson(param, ReserverBean.class);
		return info;
	}
	/**
	 * 解析财富师推荐产品信息
	 * @param param
	 * @return
	 */
	public  static RecommendPtsBean parseRecommend(String param){
		RecommendPtsBean info=new Gson().fromJson(param, RecommendPtsBean.class);
		return info;
	}
	/**
	 * 解析我的客户信息
	 * @param param
	 * @return
	 */
	public  static CustomerBean parseCustomer(String param){
		CustomerBean info=new Gson().fromJson(param, CustomerBean.class);
		return info;
	}
	/**
	 * 解析投资者预约订单信息
	 * @param param
	 * @return
	 */
	public  static InvestorOrderBean parseInvestorOrder(String param){
		InvestorOrderBean info=new Gson().fromJson(param, InvestorOrderBean.class);
		return info;
	}
	/**
	 * 解析财富师预约订单信息
	 * @param param
	 * @return
	 */
	public  static InvestorOrderBean parseCfmpOrder(String param){
		InvestorOrderBean info=new Gson().fromJson(param, InvestorOrderBean.class);
		return info;
	}
	/**
	 * 解析用户评论信息
	 * @param param
	 * @return
	 */
	public  static CommentBean parseComment(String param){
		CommentBean info=new Gson().fromJson(param, CommentBean.class);
		return info;
	}
	/**
	 * 解析财富师成员
	 * @param param
	 * @return
	 */
	public  static MembersBean parseMembers(String param){
		MembersBean info=new Gson().fromJson(param, MembersBean.class);
		return info;
	}	
	/**
	 * 产品广告
	 * @param param
	 * @return
	 */
	public  static PagerBean parsePctPager(String param){
		PagerBean info=new Gson().fromJson(param, PagerBean.class);
		return info;
	}	
	/**
	 * 收藏文章信息
	 */
	public  static ColArticleBean parseColArticle(String param){
		ColArticleBean info=new Gson().fromJson(param, ColArticleBean.class);
		return info;
	}	
	
	/**
	 * @param param
	 * 获取用户基本信息
	 * @return info 
	 */
	public static BaseBean parseBaseInfo(String param){
		BaseBean  info  = new Gson().fromJson(param,BaseBean.class);
		return info;
		
	}
	
	/**
	 * @param param
	 * 获取用户基本信息
	 * @return info 
	 */
	public static UserNewBean parseUserInfo(String param,Type type){
		UserNewBean  info  = new Gson().fromJson(param, type);
		return info;
		
	}
	
	/**
	 * @param param
	 * 获取用户扩展信息
	 * @return info 
	 */
	public static AttributesBean parsExtendInfo(String param ){
		AttributesBean  info  = new Gson().fromJson(param,AttributesBean.class);
		return info;
		
	}
	
	/**
	 * @param param
	 * 获取用户资金信息
	 * @return info 
	 */
	public static MoneyBean parseMoney(String param ){
		MoneyBean  info  = new Gson().fromJson(param, MoneyBean.class);
		return info;
		
	}
	
	/**
	 * @param param
	 * add cfmp 信息
	 * @return info 
	 */
	public static AddCfmpBean parseAddCfmp(String param ){
		AddCfmpBean  info  = new Gson().fromJson(param, AddCfmpBean.class);
		return info;
		
	}
	/**
	 * parse user information
	 */
	public static UserBean  parseUserBean(UserNewBean userNewBean, Context context){
		UserBean userBean = new UserBean();
		BaseBean baseBean =null;
		AttributesBean 	attributesBean=null;
		if(userNewBean!=null&&userNewBean.getBase()!=null){
			baseBean=userNewBean.getBase();
			userBean.setUserId(baseBean.getId());
			userBean.setUserName(baseBean.getNickName());
			userBean.setUserMobile(baseBean.getMobile());
			userBean.setEmail(baseBean.getEmail());
			userBean.setLoginToken(baseBean.getLoginToken());
			
	}
		if(userNewBean!=null&&userNewBean.getExtAttributes()!=null){
			for(int i=0;i<userNewBean.getExtAttributes().size();i++){
			 attributesBean = userNewBean.getExtAttributes().get(i);
			 if (attributesBean != null) {		
			    if(attributesBean.getCatalog().equals("product")&&attributesBean.getName().equals("role")){
					 userBean.setUserRole(getRole(attributesBean));
				 }
			    if(attributesBean.getCatalog().equals("userface")){
					 userBean.setUserFaceID(attributesBean.getValue());
				}
			  }
			}
		}
		return userBean;	
	}
	
	//登陆解析安全中心接口
	
	public static UserBean  parseUserBeanlg(UserNewBean userNewBean, Context context){
		UserBean userBean = new UserBean();
		BaseBean baseBean =null;
		AttributesBean 	attributesBean=null;
		if(userNewBean!=null&&userNewBean.getBase()!=null){
			baseBean=userNewBean.getBase();
			userBean.setUserId(baseBean.getId());
			userBean.setUserName(baseBean.getNickName());
			userBean.setUserMobile(baseBean.getMobile());
			userBean.setEmail(baseBean.getEmail());
			userBean.setLoginToken(baseBean.getLoginToken());
			
	}
		if(userNewBean!=null&&userNewBean.getExtAttributes()!=null){
			for(int i=0;i<userNewBean.getExtAttributes().size();i++){
			 attributesBean = userNewBean.getExtAttributes().get(i);
			 if (attributesBean != null) {		
				 if(attributesBean.getCatalog().equals("idcard")){
					 if(attributesBean.getName().equals("realname")){
						 userBean.setIdcard_name(attributesBean.getValue());
					 }else if(attributesBean.getName().equals("frontfid")){
						 userBean.setIdcard_frontfid(attributesBean.getValue());
					 }else if(attributesBean.getName().equals("backfid")){
					     userBean.setIdcard_backfid(attributesBean.getValue());
				     }else if(attributesBean.getName().equals("status")){//银行卡状态
						 userBean.setIdcard_status(attributesBean.getValue());//身份证状态
				     }else if(attributesBean.getName().equals("no")){
				    	 userBean.setIdcard_no(attributesBean.getValue());
				     }else if(attributesBean.getName().equals("reason")){
				    	 userBean.setIdcard_Failure_reason(attributesBean.getValue());
				     }
				 }else if(attributesBean.getCatalog().equals("product")){
					 userBean.setUserRole(getRole(attributesBean));
				 }else if(attributesBean.getCatalog().equals("businessCard")){
					 if(attributesBean.getName().equals("status")){
						 userBean.setBusinessCard(attributesBean.getValue());//名片认证状态
				     }else if(attributesBean.getName().equals("fileid")){
						 userBean.setFileid(attributesBean.getValue());//名片图片id
					 } else if(attributesBean.getName().equals("reason")){
						 userBean.setCatalog_Failure_reason(attributesBean.getValue());//名片失败原因
					 }
					 
				 }else if(attributesBean.getCatalog().equals("bankcard")){
					 if(attributesBean.getName().equals("status")){//银行卡状态
						 userBean.setBankcard_status(attributesBean.getValue());
					 }else if(attributesBean.getName().equals("no")){//银行卡号
						 userBean.setBankcard_no(attributesBean.getValue());
					 }else if(attributesBean.getName().equals("reason")){//失败原因
						 userBean.setBankcard_Failure_reason(attributesBean.getValue());
					 }
				 }else if(attributesBean.getCatalog().equals("userface")){
					 if(attributesBean.getName().equals("fileid")){
						userBean.setUserFaceID(attributesBean.getValue());
				 }
			  }
			}
		}
	}
		return userBean;	
	}
	/**
	 * 
	 * @param sharepreferences save information
	 * @return
	 */
	public static void saveSharePreferences(Context context,String object){
		SharedPreferences sharedPreferences = context.getSharedPreferences("token",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
		editor.putString("userInfo", object);
		editor.commit();//提交修改
	}
	/*public static void saveShare(Context context,BindBean bindBean){
		SharedPreferences sharedPreferences = context.getSharedPreferences("bind",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
		editor.putString("userName", bindBean.getPlatfromUserName());
		editor.putString("openId", bindBean.getBundleId());
		editor.putString("provider", bindBean.getPlatfromName());
		editor.putString("mobile","");
		editor.commit();//提交修改
	}*/
	/*public static UserBean getUserBind(Context context){
		SharedPreferences preferences = context.getSharedPreferences("bind", Context.MODE_PRIVATE);
		UserBean userBean  = new UserBean();
		userBean.setUserName(preferences.getString("userName", "")); 
		userBean.setOpenid(preferences.getString("openId", "")); 
		userBean.setProvider(preferences.getString("provider", "")); 
		userBean.setUserMobile(preferences.getString("mobile", "")); 
		return userBean;
	}*/
		 //角色
	public static  String getRole(AttributesBean attributesBean) {
	    String role = "tourist";
		if (attributesBean.getValue().equals("1")) {
			role = "fundraiser";
		} else if (attributesBean.getValue().equals("2")) {
					role = "cfmp";
		} else if (attributesBean.getValue().equals("3")) {
			role = "investor";
		}
		  return role;
		}
}

