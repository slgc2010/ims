package org.yl.ims.service;

import java.io.IOException;
import java.util.List;

import org.yl.ims.model.ImageModel;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.oreilly.servlet.multipart.Part;

public interface FileService {

	GridFSDBFile getFile(String fileName);

	GridFSFile saveFile(Part part) throws IOException;

	void delete(String fileId);

	GridFSDBFile downloadFile(String fileId);

	List<ImageModel> getFileList(int userId);
}
