package cn.com.ubankers.www.product.model;

import java.io.Serializable;

public class PagerBean implements Serializable {
		 /**
			 * 
			 */
		private static final long serialVersionUID = -795146593408878267L;
		private String imageUrl;//图片的链接
		private String description;//描述
		private String picUrl;//跳转的链接
		private boolean isProduct;//是产品
		private boolean isArticle;//是文章
		private boolean otherDefined;//其他
		private String objId;//要获取内容的id
		public String getImageUrl() {
			return imageUrl;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getPicUrl() {
			return picUrl;
		}
		public void setPicUrl(String picUrl) {
			this.picUrl = picUrl;
		}
		public boolean isProduct() {
			return isProduct;
		}
		public void setProduct(boolean isProduct) {
			this.isProduct = isProduct;
		}
		public boolean isArticle() {
			return isArticle;
		}
		public void setArticle(boolean isArticle) {
			this.isArticle = isArticle;
		}
		public boolean isOtherDefined() {
			return otherDefined;
		}
		public void setOtherDefined(boolean otherDefined) {
			this.otherDefined = otherDefined;
		}
		public String getObjId() {
			return objId;
		}
		public void setObjId(String objId) {
			this.objId = objId;
		}
		
			
}
