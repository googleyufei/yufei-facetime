package com.shop.util;

import com.facetime.mgr.utils.ImageSizer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.springframework.web.multipart.MultipartFile;

public class UploadUtil {

	public static String getFileExt(MultipartFile file) {
		if (file == null) {
			return "";
		}
		String fileName = file.getOriginalFilename();
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	public static String getFileName(MultipartFile file) {
		String ext = getFileExt(file);
		return UUID.randomUUID().toString() + "." + ext;// 构建文件名称
	}

	public static String getProductImg140Path(Integer productTypeId,
			Integer productId) {
		return "/images/product/" + productTypeId + "/" + productId + "/140x";
	}

	public static String getProductImgPrototypePath(Integer productTypeId,
			Integer productId) {
		return "/images/product/" + productTypeId + "/" + productId
				+ "/prototype";
	}

	public static File saveFile(File savedir, String fileName, byte[] data)
			throws Exception {
		if (!savedir.exists()) {
			boolean saved = savedir.mkdirs();
			if (!saved) {
				throw new AssertionError("fail to mk dirs.");
			}
		}
		File file = new File(savedir, fileName);
		FileOutputStream fileoutstream = new FileOutputStream(file);
		fileoutstream.write(data);
		fileoutstream.close();
		return file;
	}

	public static void saveProductImageFile(ServletContext application,
			MultipartFile imagefile, String filename, Integer productTypeId,
			Integer productId) throws Exception {
		String pathdir = getProductImgPrototypePath(productTypeId, productId);
		String pathdir140 = getProductImg140Path(productTypeId, productId);
		String realpathdir = application.getRealPath(pathdir);
		String realpathdir140 = application.getRealPath(pathdir140);

		File savedir = new File(realpathdir);
		File savedir140 = new File(realpathdir140);
		File file = UploadUtil
				.saveFile(savedir, filename, imagefile.getBytes());
		if (!savedir140.exists()) {
			boolean saved = savedir140.mkdirs();
			if (!saved) {
				throw new AssertionError("fail to mk dirs.");
			}
		}
		File file140 = new File(realpathdir140, filename);
		ImageSizer.resize(file, file140, 140, UploadUtil.getFileExt(imagefile));
	}

	/**
	 * 将文件写入服务器的硬盘
	 * 
	 * @param multipartFile
	 * @param context
	 * @param suffixPath
	 */
	public static String writeFileToDisk(MultipartFile file,
			ServletContext context, String suffixPath) throws Exception {
		String fileName = getFileName(file);
		String realPath = WebUtil.getRealPath(context, suffixPath);
		File savedir = new File(realPath);
		saveFile(savedir, fileName, file.getBytes());
		return fileName;
	}
}
