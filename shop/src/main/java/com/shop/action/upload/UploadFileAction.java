package com.shop.action.upload;

import com.facetime.core.utils.IdGenerator;
import com.facetime.spring.action.Action;
import com.facetime.spring.support.LogicUtils;
import com.facetime.spring.support.PMLO;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletContextAware;

import com.shop.domain.PageView;
import com.shop.domain.upload.UploadFile;
import com.shop.logic.bean.BaseBean;
import com.shop.logic.bean.UploadfileBean;
import com.shop.util.SiteUrl;
import com.shop.util.UploadUtil;

@Controller
public class UploadFileAction extends Action implements ServletContextAware {

	private ServletContext servletContext;

	@RequestMapping("/control/uploadfile/add")
	public String add(UploadfileBean bean, ModelMap model) throws Exception {
		String message = null;
		if (!BaseBean.checkCommonFile(bean.getUploadfile(), message)) {
			System.out.println("message val:" + message);
			model.addAttribute("message", message);
			model.addAttribute("urladdress",
					SiteUrl.readUrl("control.uploadfile.addUI"));
			return "share/message";
		}
		String filename = UploadUtil.writeFileToDisk(bean.getUploadfile(),
				servletContext, getSuffixPath());
		UploadFile uploadfile = new UploadFile(getSuffixPath() + "/" + filename);
		uploadfile.setId(IdGenerator.strId());
		this.getDefaultLogic().save(uploadfile);
		model.addAttribute("message", "文件上传成功!");
		model.addAttribute("urladdress",
				SiteUrl.readUrl("control.uploadfile.list"));
		return "share/message";
	}

	@RequestMapping("/control/uploadfile/addUI")
	public String addUI() {
		return "uploadfile/upload";
	}

	@RequestMapping("/control/uploadfile/delete")
	public String delete(UploadfileBean bean, HttpServletRequest request)
			throws Exception {
		List<String> filePaths = this.getDefaultLogic().findPart(
				UploadFile.class, LogicUtils.filterby("id", PMLO.IN, bean.getFileids()),
				new String[] { "filepath" });
		if (filePaths != null) {
			defaultLogic.deleteByIds(UploadFile.class, bean.getFileids());
			for (String filePath : filePaths) {
				String realpath = request.getSession().getServletContext()
						.getRealPath(filePath);
				File deletefile = new File(realpath);
				if (deletefile.exists()) {
					boolean deleted = deletefile.delete();
					if (!deleted) {
						throw new AssertionError("fail to delete file.");
					}
				}
			}
		}
		request.setAttribute("message", "删除文件成功!");
		request.setAttribute("urladdress",
				SiteUrl.readUrl("control.uploadfile.list"));
		return "share/message";
	}

	@RequestMapping("/control/uploadfile/list")
	public String list(UploadfileBean bean, ModelMap model) throws Exception {
		PageView<UploadFile> pageView = new PageView<UploadFile>(12,
				bean.getPage());
		pageView.setQueryResult(getDefaultLogic().findPage(UploadFile.class,
				LogicUtils.pageby(pageView.getCurrentpage(), pageView.getMaxresult())));
		model.addAttribute("pageView", pageView);
		return "uploadfile/uploadfilelist";
	}

	@Override
	public void setServletContext(ServletContext paramServletContext) {
		servletContext = paramServletContext;
	}

	private String getSuffixPath() {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd/HH");
		String pathdir = "/images/uploadfile/" + dateformat.format(new Date());
		return pathdir;
	}
}
