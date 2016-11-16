package org.solq.fm.common.plug;

import java.util.HashMap;
import java.util.Map;

import org.solq.fm.common.console.anno.ConsoleBean;
import org.solq.fm.common.console.anno.ConsoleCommand;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * 插件服务
 * 
 * @author solq
 */
@Service
@ConsoleBean
public class PlugService implements ApplicationContextAware {

    private Map<Class<?>, IPlug> map = new HashMap<>();

    public synchronized void register(IPlug... plugs) {
	for (IPlug plug : plugs) {
	    map.put(plug.getClass(), plug);
	}
    }

    @ConsoleCommand("restart")
    public synchronized void reStart(String className) {
	try {
	    reStart(Class.forName(className));
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
    }

    public synchronized void reStart(Class<?> key) {
	IPlug plug = map.get(key);
	plug.reStart();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	Map<String, IPlug> beans = applicationContext.getBeansOfType(IPlug.class);
	if (!beans.isEmpty()) {
	    register(beans.values().toArray(new IPlug[0]));
	}
    }
}
