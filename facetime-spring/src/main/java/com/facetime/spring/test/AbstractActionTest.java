package com.facetime.spring.test;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpHeaders;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

import com.facetime.core.utils.StringUtils;
import com.facetime.spring.support.QueryFilter;

/**
 * Action父测试类的抽象类
 *
 * @author yufei
 * @Create_by 2012-12-7
 * @Design_by eclipse  
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:*beans.xml", "classpath:*springmvc-servlet.xml" })
public abstract class AbstractActionTest {

	protected MockServletContext msc;
	protected HandlerMapping handlerMapping;
	protected AnnotationMethodHandlerAdapter handlerAdapter;
	protected AnnotationMethodHandlerExceptionResolver exceptionResolver;
	protected HandlerExecutionChain chain;
	protected ModelAndView view;
	protected MockHttpServletResponse response;
	protected MockHttpServletRequest request;
	protected XmlWebApplicationContext context;

	public AbstractActionTest() {
		ContextConfiguration config = this.getClass().getAnnotation(ContextConfiguration.class);
		this.context = new XmlWebApplicationContext();
		context.setConfigLocations(config.locations());
		msc = new MockServletContext();

		context.setServletContext(msc);
		context.refresh();
		msc.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
		handlerMapping = context.getBean(DefaultAnnotationHandlerMapping.class);
		exceptionResolver = (AnnotationMethodHandlerExceptionResolver) context.getBean(context
				.getBeanNamesForType(AnnotationMethodHandlerExceptionResolver.class)[0]);
	}

	public void createRequest() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.clearAttributes();
		handlerAdapter = new AnnotationMethodHandlerAdapter();
	}

	/**
	 * 发送请求, 返回的是json类型的数据
	 * @param uri
	 */
	public String send(String uri) {
		Assert.notNull(request);
		request.setRequestURI(uri);
		try {
			chain = handlerMapping.getHandler(request);
			handlerAdapter.handle(request, response, chain.getHandler());
		} catch (Exception e) {
			e.printStackTrace();
			exceptionResolver.resolveException(request, response, chain.getHandler(), e);
		}
		try {
			return response.getContentAsString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ModelAndView send(String uri, QueryFilter... params) {
		Assert.notNull(request);
		request.setRequestURI(uri);

		if (params != null && params.length > 0) {
			for (QueryFilter param : params) {
				request.addParameter(param.getProperty(), param.getValue().toString());
			}
		}
		try {
			chain = handlerMapping.getHandler(request);
			view = handlerAdapter.handle(request, response, chain.getHandler());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	public void createJsonRequest() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.clearAttributes();

		handlerAdapter = new AnnotationMethodHandlerAdapter();
		// messageConverter是用于将response转换为pojo的,  一般是用户自己指定
		//		HttpMessageConverter<?>[] messageConverters = { new MappingJacksonHttpMessageConverter() };
		//		handlerAdapter.setMessageConverters(messageConverters);

		request.setContentType(MediaType.APPLICATION_JSON.toString());
		request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString() + ";charset=utf-8");
		request.setCharacterEncoding("utf-8");
	}

	public String postToUrl(String uri, String json) {
		Assert.notNull(request);
		request.setRequestURI(uri);
		request.setMethod(RequestMethod.POST.name());

		if (StringUtils.isValid(json)) {
			request.setContent(json.getBytes());
		}
		try {
			chain = handlerMapping.getHandler(request);
			handlerAdapter.handle(request, response, chain.getHandler());
		} catch (Exception e) {
			exceptionResolver.resolveException(request, response, chain.getHandler(), e);
		}
		try {
			return response.getContentAsString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getFromUrl(String uri, QueryFilter... params) {
		Assert.notNull(request);
		request.setRequestURI(uri);
		request.setMethod(RequestMethod.GET.name());

		if (params != null && params.length > 0) {
			for (QueryFilter param : params) {
				request.addParameter(param.getProperty(), param.getValue().toString());
			}
		}
		try {
			chain = handlerMapping.getHandler(request);
			handlerAdapter.handle(request, response, chain.getHandler());
		} catch (Exception e) {
			exceptionResolver.resolveException(request, response, chain.getHandler(), e);
		}
		try {
			return response.getContentAsString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
