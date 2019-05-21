package com.windawn;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

@Controller
public class WebuploaderAction {

	@RequestMapping("/upload")
	public String index() {
		return "upload";
	}
	
	
	@RequestMapping(method = {RequestMethod.POST}, value = {"/webUploader"})
	public void webUploader(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
	            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	            if (isMultipart) {
	                FileItemFactory factory = new DiskFileItemFactory();
	                ServletFileUpload upload = new ServletFileUpload(factory);
	 
	                // 得到所有的表单域，它们目前都被当作FileItem
	                List<FileItem> fileItems = upload.parseRequest(request);
	 
	                String id = "";
	                String fileName = "";
	                // 如果大于1说明是分片处理
	                int chunks = 1;
	                int chunk = 0;
	                FileItem tempFileItem = null;
	 
	                for (FileItem fileItem : fileItems) {
	                    if (fileItem.getFieldName().equals("id")) {
	                        id = fileItem.getString();
	                    } else if (fileItem.getFieldName().equals("name")) {
	                        fileName = new String(fileItem.getString().getBytes("ISO-8859-1"), "UTF-8");
	                    } else if (fileItem.getFieldName().equals("chunks")) {
	                        chunks = NumberUtils.toInt(fileItem.getString());
	                    } else if (fileItem.getFieldName().equals("chunk")) {
	                        chunk = NumberUtils.toInt(fileItem.getString());
	                    } else if (fileItem.getFieldName().equals("multiFile")) {
	                        tempFileItem = fileItem;
	                    }
	                }
	                //session中的参数设置获取是我自己的原因,文件名你们可以直接使用fileName,这个是原来的文件名
	                String realname = fileName;//转化后的文件名
	                request.getSession().setAttribute("realname",realname);
	                String filePath = "d:/file/upload/";//文件上传路径
	                
	                // 临时目录用来存放所有分片文件
	                String tempFileDir = filePath + id;
	                File parentFileDir = new File(tempFileDir);
	                if (!parentFileDir.exists()) {
	                    parentFileDir.mkdirs();
	                }
	                // 分片处理时，前台会多次调用上传接口，每次都会上传文件的一部分到后台
	                File tempPartFile = new File(parentFileDir, realname + "_" + chunk + ".part");
	                FileUtils.copyInputStreamToFile(tempFileItem.getInputStream(), tempPartFile);
	 
	                // 是否全部上传完成
	                // 所有分片都存在才说明整个文件上传完成
	                boolean uploadDone = true;
	                for (int i = 0; i < chunks; i++) {
	                    File partFile = new File(parentFileDir, realname + "_" + i + ".part");
	                    if (!partFile.exists()) {
	                        uploadDone = false;
	                    }
	                }
	                // 所有分片文件都上传完成
	                // 将所有分片文件合并到一个文件中
	                if (uploadDone) {
	                	// 得到 destTempFile 就是最终的文件
	                    File destTempFile = new File(filePath, realname);
	                    for (int i = 0; i < chunks; i++) {
	                        File partFile = new File(parentFileDir, realname + "_" + i + ".part");
	                        FileOutputStream destTempfos = new FileOutputStream(destTempFile, true);
	                        //遍历"所有分片文件"到"最终文件"中
	                        FileUtils.copyFile(partFile, destTempfos);
	                        destTempfos.close();
	                    }
	                    // 删除临时目录中的分片文件
	                    FileUtils.deleteDirectory(parentFileDir);
	                } else {
	                    // 临时文件创建失败
	                    if (chunk == chunks -1) {
	                        FileUtils.deleteDirectory(parentFileDir);
	                    }
	                }
	                
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}


}
