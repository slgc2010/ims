package org.yl.ims.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.yl.ims.model.ImageModel;
import org.yl.ims.service.FileService;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.Part;

@Service
public class FileServiceImpl implements FileService {
	@Autowired
	private MongoTemplate mongoTemplate;
	final int USERID = 19871102;

	@Value("#{config['mongo.username']}")
	protected String mongoUserName;

	@Override
	public GridFSFile saveFile(Part part) throws IOException {
		GridFSFile file = null;
		if (part.isFile()) {// it's a file part
			FilePart filePart = (FilePart) part;
			filePart.setRenamePolicy(new DefaultFileRenamePolicy());
			String fileName = filePart.getFileName();
			InputStream in = filePart.getInputStream();
			file = getGridFS().createFile(in);// 创建gridfs文件
			file.put("fileName", fileName);
			file.put("userId", USERID);
			file.put("uploadDate", new Date());
			file.put("contentType", fileName.substring(fileName.lastIndexOf(".")));
			System.out.println("获取文件信息成功fileName:"+fileName);
			file.save();
			System.out.println("保持文件信息成功");
		}
		return file;
	}

	@Override
	public GridFSDBFile getFile(String fileName) {
		GridFS myFS = getGridFS();
		return myFS.findOne(fileName);
	}

	/**
	 * 文件删除
	 * 
	 * @param request
	 * @param response
	 */
	@Override
	public void delete(String fileId) {
		GridFS gridFS = this.getGridFS();
		ObjectId objId = new ObjectId(fileId);
		gridFS.remove(objId);
	}

	/**
	 * 查看文件列表
	 * 
	 * @param request
	 * @param response
	 */
	@Override
	public List<ImageModel> getFileList(int userId) {
		GridFS gridFS = this.getGridFS();
		DBObject query = new BasicDBObject("userId", USERID);
		List<GridFSDBFile> find = gridFS.find(query);
		List<ImageModel> result = new ArrayList<ImageModel>();
		for (GridFSDBFile g : find) {
			ImageModel img = new ImageModel(g.getId() + "", g.getUploadDate(), g.get("fileName") + "",
					g.getChunkSize(), g.getContentType(), g.get("userId") + "");
			result.add(img);
		}
		return result;
	}

	/**
	 * 查看单个文件、下载文件
	 * 
	 * @param request
	 * @param response
	 */
	@Override
	public GridFSDBFile downloadFile(String fileId) {
		GridFS gridFS = this.getGridFS();
		ObjectId objId = new ObjectId(fileId);
		return (GridFSDBFile) gridFS.findOne(objId);
	}

	private GridFS getGridFS() {
		DB db = mongoTemplate.getDb();
		GridFS myFS = new GridFS(db, mongoUserName);
		return myFS;
	}

}
