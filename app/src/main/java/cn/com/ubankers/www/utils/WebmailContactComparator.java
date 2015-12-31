package cn.com.ubankers.www.utils;

import java.util.Comparator;

import cn.com.ubankers.www.user.model.CustomerBean;





public class WebmailContactComparator implements Comparator<CustomerBean>{

	public int compare(CustomerBean item0, CustomerBean item1) {
			int flag = BaseUtil.getPingYin(item0.getNickName()).compareTo(BaseUtil.getPingYin(item1.getNickName()));
			if (flag == 0) {
				return item0.getNickName().compareTo(item1.getNickName());
			} else {
				return flag;
			}
		
	}

}
