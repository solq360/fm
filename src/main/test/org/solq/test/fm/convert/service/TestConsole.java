package org.solq.test.fm.convert.service;

import org.solq.fm.common.console.anno.ConsoleBean;
import org.solq.fm.common.console.anno.ConsoleCommand;
import org.springframework.stereotype.Component;

@Component
@ConsoleBean
public class TestConsole {

    @ConsoleCommand("test1")
    public void test1(long a,String b){
	System.out.println(a + " : " + b);
    }
}
