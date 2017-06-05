package org.yl.ims.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yl.ims.model.ImageModel;
import org.yl.ims.service.FileService;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;

@Controller
@RequestMapping("fileapi")
public class FileCtrl {

	@Autowired
	private FileService fileService;

	@RequestMapping("upload")
	@ResponseBody
	public String uploadFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		MultipartParser mp = new MultipartParser(request, 1024 * 1024 * 1024,
				true, true, "UTF-8");// “GB18030”必须和jsp编码格式相同，不然会产生中文乱码
		Part part = null;
		List<String> gl = new ArrayList<String>();
		while ((part = mp.readNextPart()) != null) {
			GridFSFile saveFile = fileService.saveFile(part);
			gl.add(saveFile.getId().toString());
		}
		String jsonString = JSONArray.toJSONString(gl);
		return jsonString;
	}

	@RequestMapping("download")
	@ResponseBody
	public void downloadFile(String fileId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		GridFSDBFile gridFSDBFile = fileService.downloadFile(fileId);
		if (gridFSDBFile != null) {
			OutputStream sos = response.getOutputStream();
			response.setContentType("application/octet-stream");
			String name = (String) gridFSDBFile.get("fileName");
			String fileName = new String(name.getBytes("UTF-8"), "ISO8859-1");
			response.addHeader("Content-Disposition", "attachment; filename=\""
					+ fileName + "\"");
			gridFSDBFile.writeTo(sos);
			sos.flush();
			sos.close();
		}
	}

	@RequestMapping("showlist")
	@ResponseBody
	public List<ImageModel> getFile() {
		return fileService.getFileList(1111);
	}
}
