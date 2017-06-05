package org.yl.ims.model;

import java.io.Serializable;

public class BingModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String imgUrl;

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public BingModel(String imgUrl) {
		super();
		this.imgUrl = imgUrl;
	}

}
