package com.MsgServer;

import javax.ejb.Remote;

@Remote
public interface ExampleServerRemote {
	public String getMsg();
	
}