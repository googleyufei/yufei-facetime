package com.facetime.communication.servlet;

import static com.facetime.core.conf.SysLogger.facetimeLogger;

import com.facetime.communication.activemq.AmqProducer;
import com.facetime.communication.bean.MessageDTO;
import com.facetime.core.file.FileConstants;
import com.facetime.core.http.ErrorType;
import com.facetime.core.http.HttpConstants;
import com.facetime.core.http.PojoMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SendMessageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		request.setCharacterEncoding(FileConstants.CHARSET_UTF_8);
		response.setCharacterEncoding(FileConstants.CHARSET_UTF_8);
		response.setContentType("text/plain; charset=UTF-8");

		String result = FileConstants.OK_MARK;
		try {
			// token
			String token = request.getHeader(HttpConstants.UserTokenkey);

			// post data
			InputStream is = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			String postData = sb.toString();

			MessageDTO messageDTO = PojoMapper.getObject(postData, MessageDTO.class);
			result = AmqProducer.getInstance().sendMessage(token, messageDTO);
		} catch (Exception ex) {
			result = ErrorType.ERR_500;
			facetimeLogger.error("send message", ex);
		}
		response.getWriter().write(result);
	}

}
