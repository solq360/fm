package org.solq.fm.common.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * 控制台线程的处理类
 * 
 * @author solq
 */
public class ConsoleRunner implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleRunner.class);

    private Console console;

    public ConsoleRunner(Console console) {
	this.console = console;
    }

    @Override
    public void run() {
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	try {
	    while (!console.isStop()) {
		String line = in.readLine();
		String name = getName(line);
		Command command = console.getCommand(name);
		if (command == null) {
		    FormattingTuple message = MessageFormatter.format("指令[{}]不存在", name);
		    System.err.println(message.getMessage());
		    continue;
		}
		String[] arguments = getArguments(line);
		command.execute(arguments);

	    }

	    // 修复在 JDK 6 环境下，出现 JDWP exit error AGENT_ERROR_NO_JNI_ENV(183) 的问题
	    System.exit(0);
	} catch (IOException e) {
	    logger.error("获取命令行输入时出现异常", e);
	}
    }

    private String[] getArguments(String line) {
	String[] arguments = line.split(" ");
	if (arguments.length < 2) {
	    return new String[0];
	}
	String[] result = new String[arguments.length - 1];
	System.arraycopy(arguments, 1, result, 0, arguments.length - 1);
	return result;
    }

    private String getName(String line) {
	return line.split(" ")[0];
    }

}
