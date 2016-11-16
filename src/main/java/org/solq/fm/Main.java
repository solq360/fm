package org.solq.fm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solq.fm.common.console.Console;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 应用程序入口口
 * 
 * @author solq
 */

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /** 默认的上下文配置名 */
    private static final String DEFAULT_APPLICATION_CONTEXT = "applicationContext.xml";

    public static void main(String[] arguments) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
	final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(DEFAULT_APPLICATION_CONTEXT);
	try {
	    logger.warn("容器已启动完成，开启服务器控制台");
	    Console console = new Console(applicationContext);
	    console.start();
	} catch (Exception exception) {
	    exception.printStackTrace();
	    if (applicationContext.isRunning()) {
		applicationContext.destroy();
	    }
	}
    }

}
