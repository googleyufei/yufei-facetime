package com.facetime.core.plugin;

import java.util.Map;

import com.facetime.core.utils.CE;
import com.facetime.core.utils.LE;

/**
 * 用以减少用不到的方法的实现的抽象Plugin实现类
 */
public abstract class AbstractPlugin implements Plugin {

	private PluginDef def;
	
    public PluginDef getDef() {
    	return def;
    }
    
    public void setDef(PluginDef def) {
    	this.def = def;
    }
    
	public void start(Map<String, Object> ctx) throws Exception {
		// Implemented for override;
	}

	public void stop(Map<String, Object> ctx) throws Exception {		
		// Implemented for override;
	}
    
    void doStart(Map<String, Object> ctx) {
    	try {
			start(ctx);
		} catch (Exception e) {
			throw new PluginException(e, String.format("Plugin[%s] start error!", getDef().getId()));			
		}
    }
    
    void doStop(Map<String, Object> ctx) {
    	try {
			stop(ctx);
		} catch (Exception e) {
			throw new PluginException(e, String.format("Plugin[%s] stop error!", getDef().getId()));
		}
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Plugin[getId=").append(this.getDef().getId())
                .append("; getName=").append(this.getDef().getName())
                .append("; getVersion=").append(this.getDef().getVersion()).append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!CE.of(o).isOf(PluginDef.class)) return false;
        return LE.equals(this.getDef().getId(), ((PluginDef)o).getId());
    }
}
