package com.MsgServer;

import javax.ejb.Local;

@Local
public interface ExampleServerLocal {

	public String getMsg();
}
