package com.facetime.core.logging.impl;

import java.util.Map;

import com.facetime.core.logging.Log;
import com.facetime.core.logging.LogAdapter;
import com.facetime.core.plugin.AbstractPlugin;
import com.facetime.core.plugin.Plugin;
import com.facetime.core.plugin.PluginDef;
import com.facetime.core.utils.ResultPack;
import com.facetime.core.utils.Version;

/**
 * Created by IntelliJ IDEA.
 * User: dzb
 * Date: 2010-11-19
 * Time: 20:02:46
 */
public class Slf4jLogAdapter extends AbstractPlugin implements LogAdapter, Plugin, PluginDef {
	
	public Slf4jLogAdapter() {
		this.setDef(this);
	}

    public String getId() {
        return "slf4j";
    }

    public String getName() {
        return "logger-slf4j";
    }

	public String getOrder() {
		return "after:console";
	}

	public Version getVersion() {
        return new Version("1.3");
    }
	
	public ResultPack validate(Map<String, Object> ctx) {
		try {
			Class.forName("org.slf4j.Logger", false, Thread.currentThread().getContextClassLoader());
			return ResultPack.succeed();
		} catch (Throwable e) {
			return ResultPack.failed().comment("Can not load \"org.slf4j.Logger\" class.");
		}		
	}

	public Log getLog(String label) {
		return new Log4Slf(label);
	}

	public String toString() {
		return "STREET-FRAMEWORK is using org.slf4j.Logger";
	}

    @SuppressWarnings("serial")
	private static class Log4Slf implements Log {

	    //------------------ 
	    // 方案2 Slf4f logger
	    private org.slf4j.Logger _log = null;	    
	    public Log4Slf(String className) { this._log = org.slf4j.LoggerFactory.getLogger(className);}    
	    //------------------

        public int getLevel() {
            return 0;
        }

        public void setLevel(int level) {
        }

        public void debug(Object message, Object... args){
	        String msg = String.valueOf(message);
	        if (_log.isDebugEnabled())  _log.debug((args == null || args.length == 0) ? msg : String.format(msg, args));
	    }
	    
	    public void debug(Throwable e, Object message, Object... args){
	        String msg = String.valueOf(message);
	        if (_log.isDebugEnabled())  _log.debug((args == null || args.length == 0) ? msg : String.format(msg, args), e);
	    }
	    
	    public void trace(Object message, Object... args) {
            String msg = String.valueOf(message);
	        if (_log.isTraceEnabled()) _log.trace((args == null || args.length == 0) ? msg : String.format(msg, args));
	    }
	    
	    public void trace(Throwable e, Object message, Object... args) {
            String msg = String.valueOf(message);
	        if (_log.isTraceEnabled()) _log.trace((args == null || args.length == 0) ? msg : String.format(msg, args), e);
	    }


	    public void info(Object message, Object... args) {
            String msg = String.valueOf(message);
	        if (_log.isInfoEnabled())   _log.info((args == null || args.length == 0) ? msg : String.format(msg, args));
	    }
	    
	    public void info(Throwable e, Object message, Object... args) {
            String msg = String.valueOf(message);
	        if (_log.isInfoEnabled())   _log.info((args == null || args.length == 0) ? msg : String.format(msg, args), e);
	    }

	    public void warn(Object message, Object... args){
            String msg = String.valueOf(message);
	        if (_log.isWarnEnabled())   _log.warn((args == null || args.length == 0) ? msg : String.format(msg, args));
	    }
	    
	    public void warn(Throwable e, Object message, Object... args){
            String msg = String.valueOf(message);
	        if (_log.isWarnEnabled())   _log.warn((args == null || args.length == 0) ? msg : String.format(msg, args), e);
	    }

	    public void error(Object message, Object... args){
            String msg = String.valueOf(message);
	        if (_log.isErrorEnabled())  _log.error((args == null || args.length == 0) ? msg : String.format(msg, args));
	    }

	    public void error(Throwable e, Object message, Object... args){
            String msg = String.valueOf(message);
	        if (_log.isErrorEnabled())  _log.error((args == null || args.length == 0) ? msg : String.format(msg, args), e);
	    }
	    
	    public boolean isDebugEnabled() { return _log.isDebugEnabled();}
	    public boolean isTraceEnabled() {return _log.isTraceEnabled();}
	    public boolean isInfoEnabled() {return _log.isInfoEnabled();}
	    public boolean isWarnEnabled() { return _log.isWarnEnabled();}
		public boolean isErrorEnabled() {return _log.isErrorEnabled();}
	}
}
