package com.kyle.zkStudy;

import org.apache.zookeeper.KeeperException;
import org.junit.Before;
import org.junit.Test;

public class zkClintTest {

	SimpleZkClient client = new SimpleZkClient();
	@Before
	public void init() throws Exception{
		
		client.init();
	}
	
	@Test
	public void testCreate() throws Exception{
		client.createZnode();
	}
	@Test
	public void testCheck() throws KeeperException, InterruptedException{
		client.checkZnode();
	}
	@Test
	public void testGetChildren() throws Exception{
		client.getChildren();
	}
	@Test
	public void testGetData() throws Exception{
		client.getData();
	}
	@Test
	public void testSetData() throws Exception{
		client.setData();
	}
	@Test
	public void testDelete() throws Exception{
		client.deleteZnode();
	}
}
