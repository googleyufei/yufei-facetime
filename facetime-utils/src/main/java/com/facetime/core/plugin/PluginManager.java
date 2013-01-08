package com.facetime.core.plugin;

import java.net.URL;
import java.util.List;


/**
 * 扩展点管理器
 * @author dzb2k9 (dzb2k9@gmail.com)
 *
 */
public interface PluginManager {
	/**
	 * 获取符合label的全部可用的插件
	 * 
	 * @return 符合label全部可用的插件, 如果label为null或blank, 那么返回全部。
	 */
	<T> List<T> get(String pluginId);
	<T> List<T> get(Class<T> pluginClazz);	
	/**
	 * 返回一个包内所有的Plugin
	 */
	List<Plugin> loadPackage(String packageName);
	/**
	 * 载入一个Plugin
	 */
	Plugin load(String pluginClassName) throws PluginException;
	/**
	 * 载入扩展点实现类
     * @param plugin 实现了ExtentionPoint接口的类名。<p>来吧，让本管理器实现来载入你。
     */
	void attach(Plugin plugin);
	/**
	 * 从管理器中卸除扩展点 <P> 除非你确定知道在做什么，否则不要随意detach
	 * 
	 * @param plugin 要卸除的扩展点
	 * @param force 强迫卸除，即使包含依赖（依赖被卸载的扩展点会变得不可用，loaded为failed）

	 */
	void detach(Plugin plugin, boolean force);
	

    /**
     * Plug-ins life-cycle events callback interface.
     * 
     * @version $Id: PluginManager.java,v 1.5 2007/04/07 12:42:14 ddimon Exp $
     */
    public interface EventListener {
        /**
         * This method will be called by the manager just after plug-in has been
         * successfully activated.
         * 
         * @param plugin
         *            just activated plug-in
         */
        void pluginActivated(Plugin plugin);

        /**
         * This method will be called by the manager just before plug-in
         * deactivation.
         * 
         * @param plugin
         *            plug-in to be deactivated
         */
        void pluginDeactivated(Plugin plugin);

        /**
         * This method will be called by the manager just before plug-in
         * disabling.
         * 
         * @param descriptor
         *            descriptor of plug-in to be disabled
         */
        void pluginDisabled(PluginDef def);

        /**
         * This method will be called by the manager just after plug-in
         * enabling.
         * 
         * @param descriptor
         *            descriptor of enabled plug-in
         */
        void pluginEnabled(PluginDef def);
    }

    /**
     * An abstract adapter class for receiving plug-ins life-cycle events. The
     * methods in this class are empty. This class exists as convenience for
     * creating listener objects.
     * 
     * @version $Id: PluginManager.java,v 1.5 2007/04/07 12:42:14 ddimon Exp $
     */
    public abstract static class EventListenerAdapter implements EventListener {
        /**
         * @see org.java.plugin.PluginManager.EventListener#pluginActivated(
         *      org.java.plugin.Plugin)
         */
        public void pluginActivated(final Plugin plugin) {
            // no-op
        }

        /**
         * @see org.java.plugin.PluginManager.EventListener#pluginDeactivated(
         *      org.java.plugin.Plugin)
         */
        public void pluginDeactivated(final Plugin plugin) {
            // no-op
        }

        /**
         * @see org.java.plugin.PluginManager.EventListener#pluginDisabled(
         *      org.java.plugin.registry.PluginDescriptor)
         */
        public void pluginDisabled(final PluginDef def) {
            // no-op
        }

        /**
         * @see org.java.plugin.PluginManager.EventListener#pluginEnabled(
         *      org.java.plugin.registry.PluginDescriptor)
         */
        public void pluginEnabled(final PluginDef def) {
            // no-op
        }
    }

    /**
     * Simple callback interface to hold info about plug-in manifest and plug-in
     * data locations.
     * 
     * @version $Id: PluginManager.java,v 1.5 2007/04/07 12:42:14 ddimon Exp $
     */
    public static interface PluginLocation {
        /**
         * @return location of plug-in manifest
         */
        URL getManifestLocation();

        /**
         * @return location of plug-in context ("home")
         */
        URL getContextLocation();
    }
}