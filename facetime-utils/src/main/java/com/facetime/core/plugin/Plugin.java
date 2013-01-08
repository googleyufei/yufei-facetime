package com.facetime.core.plugin;

import java.util.Map;

import com.facetime.core.utils.ResultPack;

/**
 * 扩展点接口
 * @author dzb2k9
 *
 */
public interface Plugin {
    /**
	 * 检查扩展是否能够正常工作<br></>
     * 如果不能通过检查，那么plugin就不会被挂载<br></>
     * 扩展是否能正常工作由扩展自己决定,这个方法只返回ResultPack，可以包含一些通知信息，不要抛出任何的异常（包括runtime）
     *
     * @param ctx 传入的用于进行辅助判断的上下文对象
	 * @return
	 */
    ResultPack validate(Map<String, Object> ctx);
	/**
	 * 
	 */
	PluginDef getDef();

    /**
	 * 附加到扩展到管理器中的动作 <p>
	 * 插件可以在这里完成'初始化'动作，当然，前提已经是有效的扩展了。
	 * 
	 * @return
	 */
	void start(Map<String, Object> ctx) throws Exception;
	
	/**
	 * 扩展被卸载时候的动作 <p>
	 *  插件可以在这里完成'终止化'动作，当然，前提已经是有效的扩展了。
	 * @return
	 */
	void stop(Map<String, Object> ctx) throws Exception;
}
