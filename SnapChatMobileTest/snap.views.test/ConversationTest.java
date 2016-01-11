package snap.views.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import snap.models.Conversation;

public class ConversationTest {
	Conversation c = new Conversation("abc", new Date(), "abc", "abc");
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetUrl() {
		String expected = "abc";
		assertEquals(expected, c.getUrl());
	}
	
	@Test
	public void testGetUrl2() {
		String wrongExpected = "abcdef";
		assertNotEquals(wrongExpected, c.getUrl());
	}

	@Test
	public void testSetUrl() {
		String url = "def";
		c.setUrl(url);
		assertEquals(url,c.getUrl());
	}
	
	@Test
	public void testGetMsg() {
		String expected = "abc";
		assertEquals(expected, c.getMsg());
	}
	
	@Test
	public void testGetMsg2() {
		String wrongExpected = "defggg";
		assertNotEquals(wrongExpected, c.getMsg());
	}

	@Test
	public void testSetMsg() {
		String msg = "gdfgd";
		c.setUrl(msg);
		assertEquals(msg,c.getUrl());
	}

	@Test
	public void testGetDate() {
		Date expected = new Date();
		assertEquals(expected, c.getDate());
	}

	@Test
	public void testSetDate() {
		Date d = new Date();
		c.setDate(d);
		assertEquals(d,c.getDate());
	}

	@Test
	public void testGetSender() {
		String expected = "abc";
		assertEquals(expected, c.getSender());
	}
	
	@Test
	public void testGetSender2() {
		String wrongExpected = "foo";
		assertNotEquals(wrongExpected, c.getSender());
	}
	
	@Test
	public void testSetSender() {
		String sender = "sender";
		c.setSender(sender);
		assertEquals(sender, c.getSender());
	}

}
