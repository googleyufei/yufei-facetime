package com.facetime.core.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.facetime.core.collection.CollectionFactory;
import com.facetime.core.i18n.MessageBundle;
import com.facetime.core.i18n.MessageBundles;
import com.facetime.core.order.Orderer;
import com.facetime.core.resource.ResourceScanner;
import com.facetime.core.resource.impl.DefaultResourceScanner;
import com.facetime.core.utils.CE;
import com.facetime.core.utils.ClassUtils;
import com.facetime.core.utils.ResultPack;
import com.facetime.core.utils.StringUtils;

/**
 * 扩展管理器实现
 * 
 * @author dzb2k9 (dzb2k9@gmail.com)
 *
 */
public class PluginManagerImpl implements PluginManager {

	private static MessageBundle MSG = MessageBundles.of(PluginMessages.class);

	protected List<Plugin> _list = new ArrayList<Plugin>();

	protected Map<String, Object> _ctx = CollectionFactory.newCaseInsensitiveMap();

	public PluginManagerImpl() {
		// 把配置符号放入
		// TODO... 这里要把扩展用到的符号Sourcd都注入才可以
		//context.putAll(new SymbolContextImpl());
	}

	public PluginManagerImpl(Map<String, Object> ctx) {
		this._ctx = ctx;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> get(String id) {
		int size = (id == null || id.trim() == "") ? _list.size() : -1;
		List<T> alist = new ArrayList<T>(size > 0 ? size : 10);
		for (Plugin p : _list) {
			if (size > 0) {
				alist.add((T) p);
			} else {
				if (id.startsWith(p.getDef().getId())) {
					alist.add((T) p);
				}
			}
		}
		return alist;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> get(Class<T> clazz) {
		int size = (clazz == null) ? _list.size() : -1;
		List<T> alist = new ArrayList<T>(size > 0 ? size : 10);
		for (Plugin p : _list) {
			if (CE.of(p).isOf(clazz)) {
				alist.add((T) p);
			}
		}
		return alist;
	}

	public List<Plugin> loadPackage(String packageName) {

		ResourceScanner locator = new DefaultResourceScanner();

		Collection<String> pluginNames = locator.scanClassNames(packageName);
		Orderer<Plugin> orderer = new Orderer<Plugin>();

		//List<Plugin> list = new ArrayList<Plugin>();
		for (String pluginName : pluginNames) {
			try {
				Plugin plugin = load(pluginName);
				// 检测排序限制
				String constrants = StringUtils.isEmpty(plugin.getDef().getOrder()) ? "" : plugin.getDef().getOrder();
				// constrants格式是用"|"分割
				orderer.add(plugin.getDef().getId(), plugin, constrants);
				//list.fill(plugin);
			} catch (Exception e) {
				System.err.println("Class[" + pluginName + "] can not load as Extention-Point");
			}
		}

		return orderer.getOrdered();
	}

	/**
	 * 卸载扩展点<P>不管什么原因 反正不想要了
	 * 
	 * @param pluginClassName 要卸载的扩展点
	 * @return 返回卸载成功与否的状态和信息 
	 */
	public Plugin load(String pluginClassName) {
		Class<?> clzz = ClassUtils.loadClass(pluginClassName);
		Plugin ext;
		if (clzz == null) {
			throw new PluginException(MSG.format("plugin-class-load-error", pluginClassName));
		}
		try {

			ext = (Plugin) CE.of(clzz).create();
		} catch (Throwable e) {
			throw new PluginException(e, MSG.format("plugin-class-instance-error", pluginClassName));
		}
		return ext;
	}

	public void attach(Plugin plugin) {
		//TODO 检查动作考虑异步实现 如使用future
		// 检验
		ResultPack b = plugin.validate(_ctx);
		//是否检验成功 失败直接返回
		if (b.isFailed()) {
			return;
		}
		// 缺省equals 在abstract-plugin中已经实现（通过plugin-getId）
		if (_list.contains(plugin)) {
			return;
		}
		try {
			plugin.start(_ctx); //挂载
			_list.add(plugin);
		} catch (Throwable e) {
			throw new FailToAttachException(e, String.format("Attaching plugin %s failed", plugin));
		}
	}

	/**
	 * 卸载扩展点<P>不管什么原因 反正不想要了
	 * 
	 * @param plugin 要卸载的扩展点
	 * @param force       是否强制卸载
	 * return 返回卸载成功与否的状态和信息
	 */
	public void detach(Plugin plugin, boolean force) {
		if (_list.size() == 0 || (!_list.contains(plugin))) {
			return;
		}

		try {
			plugin.stop(_ctx);
			_list.remove(plugin);
		} catch (Throwable e) {
			if (force) {
				_list.remove(plugin);
			}
			throw new FailToDetachException(e, String.format("Attaching plugin %s failed", plugin));
		}
	}
}
