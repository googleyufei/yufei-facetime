package com.shop.action.product;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.shop.domain.product.ProductInfo;
import com.shop.util.UploadUtil;

public class BuildHtmlFile {

	public static void createProductHtml(ProductInfo product, File saveDir) {
		try {
			if (!saveDir.exists()) {
				boolean saved = saveDir.mkdirs();
				if (!saved) {
					throw new AssertionError("mk dir failed.");
				}
			}
			VelocityContext context = new VelocityContext();
			context.put("product", product);
			context.put("productPrototypeImgPath",
					UploadUtil.getProductImgPrototypePath(product.getId(), product.getType().getTypeid()));
			context.put("product140ImgPath",
					UploadUtil.getProductImg140Path(product.getId(), product.getType().getTypeid()));
			Template template = Velocity.getTemplate("product/productview.html");
			FileOutputStream outStream = new FileOutputStream(new File(saveDir, product.getId() + ".shtml"));
			OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
			BufferedWriter sw = new BufferedWriter(writer);
			template.merge(context, sw);
			sw.flush();
			sw.close();
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
