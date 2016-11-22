package org.solq.test.fm.convert.service;

import org.solq.fm.common.plug.IPlug;
import org.springframework.stereotype.Component;

@Component
public class TestPlug implements IPlug {

    //restart org.solq.fm.test.convert.service.TestPlug
    @Override
    public void reStart() {
	System.out.println("TestPlug 重启中");	
    }
}
