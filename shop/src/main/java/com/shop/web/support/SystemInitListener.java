package com.shop.web.support;

import com.facetime.mgr.common.BusnDataDir;

import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.velocity.app.Velocity;
import org.springframework.core.io.ClassPathResource;

public class SystemInitListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		initVelocity();
		//		initSystemPrivilege();
		BusnDataDir.init();
	}

	private void initVelocity() throws AssertionError {
		try {
			System.out.println("[Start]-[SystemInitListener]");
			// init velocity config
			Properties prop = new Properties();
			String path = new ClassPathResource("/WEB-INF/log/velocity.log").getPath();
			prop.put("runtime.log", path);
			prop.put("file.resource.loader.path", new ClassPathResource("/WEB-INF/vm").getPath());
			prop.put("input.encoding", "UTF-8");
			prop.put("output.encoding", "UTF-8");
			Velocity.init(prop);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AssertionError("fail to init velocity config.");
		}
	}
}
