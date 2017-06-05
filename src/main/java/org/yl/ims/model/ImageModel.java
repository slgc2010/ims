package org.yl.ims.model;

import java.io.Serializable;
import java.util.Date;

public class ImageModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private Date uploadDate;
	private String fileName;
	private long chunkSize;
	private String contentType;
	private String userId;

	public ImageModel(String id, Date uploadDate, String fileName,
			long chunkSize, String contentType, String userId) {
		super();
		this.id = id;
		this.uploadDate = uploadDate;
		this.fileName = fileName;
		this.chunkSize = chunkSize;
		this.contentType = contentType;
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(long chunkSize) {
		this.chunkSize = chunkSize;
	}

}
