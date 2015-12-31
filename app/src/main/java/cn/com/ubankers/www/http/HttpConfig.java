package cn.com.ubankers.www.http;

/**
 * @author 张鹏飞
 * 网络参数，请求地址封装类
 */
public class HttpConfig {
	//uat域名
	public static final String UDOMAIN = "uat.ubankers.com";
	//生产环境域名
	public static final String WDOMAIN = "www.ubankers.com";

	//使用的协议
	public static final String HTTP = "http://";
	public static final String HTTPS = "https://";

	//生产环境公共路径
    public static final String HTTP_QUERY_URL = HTTP + WDOMAIN;
    public static final String HTTP_IMAGE_QUERY_URL = HTTP_QUERY_URL + "/s3/ajax/rd/upload/linkFile/";
	
//	uat路径
/*	public static final String HTTP_QUERY_URL ="https://uat.ubankers.com";
    public static final String HTTP_IMAGE_QUERY_URL ="https://uat.ubankers.com/s3/ajax/rd/upload/linkFile/";*/
    

	//dev环境
//    public static final String HTTP_QUERY_URL ="http://192.168.0.248:7070";
//    public static final String HTTP_IMAGE_QUERY_URL ="http://192.168.0.248:7070/s3/ajax/rd/upload/linkFile/";

    //注册接口
	public static final String URL_REGIST=HTTP_QUERY_URL+"/user/ajax/user/register/";
	//注册获取语音验证码接口
	public static final String VOICE_VERIFICATION_CODE = HTTP_QUERY_URL+"/user/ajax/user/sendVoiceValidCode";
	//忘记密码时，发送语音短信验证码
	public static final String FORGET_PASSWORD_VOICE_VERIFICATION_CODE = HTTP_QUERY_URL+"/user/ajax/user/bindingmobile/retrievePwd/sendVoiceCode";
	//保持登录接口
	public static final String URL_MAIN_LOGIN=HTTP_QUERY_URL+"/user/ajax/user/maintainLogin/";
	
	
	//获取验证码接口
	public static final String URL_REQUEST_VERIFYCODE=HTTP_QUERY_URL+"/user/ajax/user/sendSmsValidCode/";	
	//获取用户扩展信息
	public static final String URL_USEREXTEBD = HTTP_QUERY_URL+ "/user/ajax/user/setExtAttr/";
   
	//财富师发送邮件	
	public static final String  URL_SENDEMAIURL = HTTP_QUERY_URL+"/user/ajax/user/sendEmailForCmfById";
	
	//registerIsExsit
	public static final String URL_REGISTER_IS_EXSIT = HTTP_QUERY_URL+"/user/ajax/user/bindingmobile/surePhoneIsUsed/";
    
	//登录接口
	public static final  String URL_LOGIN =HTTP_QUERY_URL+"/user/ajax/user/login/";
	
	//检测第三方登录账号的接口
	public static final String URL_ChECKTHRIDVERIFY= HTTP_QUERY_URL+"/user/ajax/thirdparty/ckuser";
    
	//第三方登录绑定用户接口
	public static final String URL_BIND= HTTP_QUERY_URL+"/user/ajax/thirdparty/bindlogin";
    
    //产品列表的接口
	public static final String URL_PRODUCTLIST = HTTP_QUERY_URL+"/product/ajax/rd/product/common/list";
	
	//产品头部滚动的图片
	public static final String URL_PRODUCT_TITLE_IMAGE=HTTP_QUERY_URL+"/cms/ajax/focusImage/config/get/new_banner";
	
	//产品详情的接口
	//productParticularsUrl
	public static final String URL_PRODUCT_PARTICULARS =HTTP_QUERY_URL+"/product/ajax/rd/product/common/get/";
	//财富师获取B类投资者列表
	public static final String URL_B_INVESTOR= HTTP_QUERY_URL+"/product/ajax/rd/unregisteduserreserve/getuser";
	
	//查看某产品绑定财富师可预约的投资者
	public static  final String URL_INVESTOR = HTTP_QUERY_URL+"/product/ajax/rd/productreserve/cfmpres";
	//投资者预约产品的接口
	public static final String  URL_INVESTOR_ORDER_PRODUCT = HTTP_QUERY_URL+"/product/ajax/rw/productreserve/investoradd";
	
	//财富师给投资者预约产品的接口
	public static final String URL_CFMP_BOUND_INVERTOR = HTTP_QUERY_URL+"/product/ajax/rw/productreserve/cfmpadd";
	
	//投资者是否绑定财富师的接口
	public static final String URL_INVESTOR_BOUND =HTTP_QUERY_URL+"/product/ajax/rd/investorBindCfmp/listCfmpByInvestorId";
	
	//h5产品详情的接口
	public static final String URL_INFROMATION = HTTP_QUERY_URL+ "/product/_escape_app/productinfo/";
	
	//财富师个人中心预约管理未确定预约的接口
	public static final  String URL_CFMPUNORDER = HTTP_QUERY_URL+"/product/ajax/rd/productreserve/cfmplist";
	
	//预约详情接口
	public static final String URL_RESERVE_DETAILS = HTTP_QUERY_URL+"/product/ajax/rd/productreserve/cfmpexlist";
	
	//
	public static final String URL_IDS_PIC = HTTP_QUERY_URL+"/product/ajax/rw/productreserve/update";
	
	//
	public static final String URL_PAY_PIC = HTTP_QUERY_URL+"/product/ajax/rw/productreserve/updatevoucher";
	
	//财富师未确认的确认预约的接口
	public static final  String URL_CFMP_COUNTERMAND = HTTP_QUERY_URL+"/product/ajax/rw/productreserve/examine";	
	
	//财富师个人中心预约管理已确定预约的接口
	public static final  String URL_INVESTOR_ORDER =HTTP_QUERY_URL+"/product/ajax/rd/productreserve/cfmppro";
	
    //财富师未注册投资者客户接口
	public static final  String  URL_REGISTER_INVESTOR= HTTP_QUERY_URL+"/product/ajax/rd/investorBindCfmp/myClientele";
	
	//投资者已预约产品接口
	public static final  String  URL_INVESTORORDER=HTTP_QUERY_URL+"/product/ajax/rd/productreserve/investorlist";
	
	//投资者取消预约产品接口
	public static final  String URL_INVESTORORDER_COUNTERMAND = HTTP_QUERY_URL+"/product/ajax/rw/productreserve/delete";
	
	//	财富师推荐产品接口
	public static final  String URL_RECOMMENT_PRODUCT=HTTP_QUERY_URL+"/product/ajax/rd/cfmpRecommendProduct/list";
		
	//获取绑定的财富师
	public static final  String URL_CFMPPICTURE =HTTP_QUERY_URL+"/product/ajax/rd/investorBindCfmp/getCfmpByInvestorId";
	
	//投资者查询财富师
	public static final  String  URL_SELECTCFMP = HTTP_QUERY_URL+"/product/ajax/rd/productUser/getCfmpByMobile/";
	
    //投资者未绑定财富师去查询财富师
	public static final  String  URL_GETUSER = HTTP_QUERY_URL+"/product/ajax/rd/investorBindCfmp/mineCfmp";
	
	//投资者未绑定财富师去绑定财富师
	public static final  String URL_GETCFMPIS  = HTTP_QUERY_URL+"/product/ajax/rw/investorBindCfmp/add";
	
	//关于我们html5页面接口
	public static final String URL_ABOUT=HTTP_QUERY_URL+"/view/app_aboutusnew.html";
	
	//上传图片到服务器
	public static final String  URL_UPLOADPHOTO= HTTP_QUERY_URL+ "/s3/ajax/rw/upload/uploadThumbnail";

	//身份认证(批量修改信息接口)
	public static final String  URL_AUTHENTICATION= HTTP_QUERY_URL+ "/user/ajax/user/setExtAttrs";
	
	//银行卡认证接口(单独修改信息接口)
	public static final String  URL_BANKCARD= HTTP_QUERY_URL + "/user/ajax/user/setExtAttrs";
	
	//邮箱绑定接口
	public static final String  URL_BINDINGEMA=HTTP_QUERY_URL+"/user/ajax/user/bindemail/email/binding/";
	
    //修改密码接口	
	public static final String  URL_STORAGECHGPASSWORD=HTTP_QUERY_URL+"/user/ajax/user/modifyUserPwd";
	
	//注册协议html5接口
	public static final String URL_REGISTRATIONPROTOCOL =HTTP_QUERY_URL+"/view/registerAgreement.html";
	
	//获得顶上图片和置顶五篇文章接口
	public static final  String URL_TITLE_IMAGE = HTTP_QUERY_URL+ "/sns/api/main/articles";
	
	//融圈儿文章详情H5接口
	public static final String URL_ARTICL_H5=HTTP_QUERY_URL+"/sns/_escape_app/articleDetail/";
	
	//融圈儿文章详情接口
	public static final String URL_ARTICLE_DETAIL = HTTP_QUERY_URL+"/sns/api/articles/";
	//获取融圈儿list接口
	public static final  String  URL_ARTICLE= HTTP_QUERY_URL+ "/sns/api/articles/recent?";
		
	//回复评论接口
	public static final String URL_COMMENT_REPLY = HTTP_QUERY_URL+"/sns/api/articles/";
	
	//删除评论接口
	public static final String URL_COMMENT_DELET = HTTP_QUERY_URL+"/sns/api/articles/";

	//分享文章接口
	public static final String URl_SHARE_ARTICLE=HTTP_QUERY_URL+"/sns/_escape_app/articleDetail/";
	
    //融圈儿喜欢接口
	public static final String URL_LOVE=HTTP_QUERY_URL+ "/sns/api/articles/";
	
	//融圈儿收藏接口
	public static final String  URL_COLLECTIONS =HTTP_QUERY_URL+ "/sns/api/articles/";
   
	//查看评论和发表评论接口	
	public static final String URL_COMMENT =HTTP_QUERY_URL+ "/sns/api/articles/";
	
	//回复评论的 和删除评论接口
	public static final String URL_REPLY=HTTP_QUERY_URL+"/sns/api/articles/";
	
	//版本更新
	public static final  String  URL_UPDATE_VERSION =HTTP_QUERY_URL+"/identity/ajax/ex/updateCheck/getNewVersionInfo/";
	
	//预约产品
	public static final String countermandUrl = HTTP_QUERY_URL+"/product/ajax/rw/productreserve/examine";
	
	//用户退出接口
	public static final String URL_ = HTTP_QUERY_URL+"/user/ajax/user/logout/";
	
	//用户退出接口
	public static final String URL_LOGOUT = HTTP_QUERY_URL+"/user/ajax/user/logout/";
	
	//登录重置密码接口
	public static final String URL_RESET_PASSWORD = HTTP_QUERY_URL+"/user/ajax/user/bindingmobile/retrievePwd/setPassword";
	
	//财富师推荐财富师
//	public static final String URL_RECOMMOND_CFMP = HTTP_QUERY_URL+"/register/cfmpStudio/";
	public static final String URL_RECOMMOND_CFMP = HTTP_QUERY_URL+"/register/recommend/";
	//获取用户信息接口
	public static final String URL_USER_INFO =HTTP_QUERY_URL+"/user/ajax/user/getUser";
	
	//获取财富工作室信息接口
	public static final String URL_CHECK_WEALTHS=HTTP_QUERY_URL+"/product/ajax/rd/cfmpstudio/getinfo";
	
	//创建财富工作室接口
	public static final String URL_SUBMIT_WEALTH =HTTP_QUERY_URL+"/product/ajax/rw/cfmpstudio/audit";
	
	//财富工作室财富师推荐接口
	public static final String URL_RECOMMENFD_CFMP =HTTP_QUERY_URL+"/register/cfmpStudio/";
//	public static final String URL_RECOMMENFD_CFMP =HTTP_QUERY_URL+"/register/recommend/";
	
	//财富工作室成员接口
	public static final String URL_CFMP_MEMBERS =HTTP_QUERY_URL+"/product/ajax/rd/cfmpstudiomember/getallmember";
	
	//创建财富工作室接口
	public static final String URL_CREAT_STUDIO =HTTP_QUERY_URL+"/product/ajax/rw/cfmpstudio/create";
	
	//关闭财富工作室接口
	public static final String URL_CLOSE_STUDIO =HTTP_QUERY_URL+"/product/ajax/rw/cfmpstudio/disband";
	
	//财富工作室当前成员接口(不包括退出成员)
	public static final String URL_ACTIVE_MEMBER =HTTP_QUERY_URL+"/product/ajax/rd/cfmpstudiomember/getactivemember";
	
	//修改财富工作室名称和签名
	public static final String URL_MODIFY_STUDIO_INFO =HTTP_QUERY_URL+"/product/ajax/rw/cfmpstudio/update";
	
	//修改个人中心用户名
	public static final String URL_PERSONAL_NAME = HTTP_QUERY_URL + "/user/ajax/user/setNickName/";
	
	//检查手机号码是不注册过
	public static final String URL_PHONE_IS_USED =HTTP_QUERY_URL+"/user/ajax/user/bindingmobile/surePhoneIsUsed/";
	
	//重置密码获取验证码接口
	public static final String URL_RESET_VERIFYCODE =HTTP_QUERY_URL+"/user/ajax/user/bindingmobile/retrievePwd/verifycode";
	
	//重置密码验证码确认接口
	public static final String URL_RESET_CONFIRM_VERIFYCODE =HTTP_QUERY_URL+"/user/ajax/user/bindingmobile/retrievePwd/lastverifycode";
	
	//用户资金信息接口
	public static final String URL_MONEY = HTTP_QUERY_URL+"/pay/api/payGateway/queryLastUserAmount/";
	
	//融圈儿个人首页
	public static final String URL_SNS_PERSONAL = HTTP_QUERY_URL+"/sns/api/users/";
	
	//绑定用户直接登录
	public static final String URL_INSERT= HTTP_QUERY_URL+ "/user/ajax/thirdparty/insert/";
	
	//完善用户信息接口
	public static final String URL_SUPPINFO= HTTP_QUERY_URL+ "/user/ajax/user/supplinfo";
	
	//财富师已购买记录
	public static final String URL_CFMP_BUY_PRODUCT= HTTP_QUERY_URL+ "/product/ajax/rd/orderinfo/cfmpProductBoughtHistory";
	//投资者已购买记录
	public static final String  URL_INVESTORS_BUY_PRODUCT= HTTP_QUERY_URL+"/product/ajax/rd/orderinfo/investorProductBoughtHistory";
	//判断是否是合格的财富师
	public static final String URL_QUALIFIED_WEALTH_DIVISION=HTTP_QUERY_URL+"/user/ajax/member/getqualify";
	//账户余额
	public static final String URL_ACCOUNT_BALANCE=HTTP_QUERY_URL+"/pay/api/payGateway/queryLastUserAmount/";
    //账户流水	
	public static final String RUNNING_WATER=HTTP_QUERY_URL+"/pay/api/userbusiness/tradelist";
	//提现前账户余额
	public static final String THE_BALANCE_OF =HTTP_QUERY_URL+"/pay/api/withdraw/limit";
	//提现请求
	public static final String CASH_WITHDRAWAL=HTTP_QUERY_URL+"/pay/api/withdraw";
	//提现费率计算
	public static final String	CASH_WITHDRAWA=HTTP_QUERY_URL+"/pay/api/withdraw/feeData";
	//成員退出財富師工作室
	public static final String URL_MEMBER_OUT_SPACE=HTTP_QUERY_URL+"/product/ajax/rw/cfmpstudiomember/leave";
	//核心财富师移除成员
	public static final String URL_LEADER_OUT_MEMBER=HTTP_QUERY_URL+"/product/ajax/rw/cfmpstudiomember/leaderRemoveMember";
	//核心财富师查询 工作室业绩（上月）、工作室业绩目标、工作室佣金总金额
	public static final String URL_LEADER_money=HTTP_QUERY_URL+"/product/ajax/rd/performance/getStudioPerformanceInfo";
	//核心财富师查看成员详情
	public static final String URL_LEADER_FIND_MEMBER = HTTP_QUERY_URL+"/product/ajax/rd/performance/getStudioMemberPerformanceInfo";
	//财富师工作室成员查看自身信息
	public static final String URL_MEMBER_FIND = HTTP_QUERY_URL+"/product/ajax/rd/performance/getMemberperformanceInfo";
	//更改财富师业绩目标
	public static final String URL_LEADER_UPDATE_SALE=HTTP_QUERY_URL+"/product/ajax/rw/cfmpstudio/setPerformance";
	//
	public static final String URL_URL=HTTP_QUERY_URL+"/product/ajax/rd/cfmpstudio/getlink";
	//银行卡核对
	public static final String URL_BANK_CARD_CHECK=HTTP_QUERY_URL+"/product/ajax/checkBankValid/";
}