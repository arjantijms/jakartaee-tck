/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * @(#)Client.java	1.8 03/05/16
 */
package com.sun.ts.tests.jms.ee.ejb.sessionQtests;

import java.lang.System.Logger;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;
import com.sun.ts.tests.jms.commonee.Tests;

import jakarta.ejb.EJB;


public class ClientIT {

	private static final String testName = "com.sun.ts.tests.jms.ee.ejb.sessionQtests.ClientIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final Logger logger = (Logger) System.getLogger(ClientIT.class.getName());

	// Harness req's
	private Properties props = null;

	// properties read
	long timeout;

	String user;

	String password;

	String mode;

	@EJB(name = "ejb/SessionTestsQ")
	private static Tests beanRef;

	/* Test setup: */

	/*
	 * setup() is called before each test
	 * 
	 * Creates Administrator object and deletes all previous Destinations.
	 * Individual tests create the JmsTool object with one default Queue and/or
	 * Topic Connection, as well as a default Queue and Topic. Tests that require
	 * multiple Destinations create the extras within the test
	 * 
	 * @class.setup_props: jms_timeout; user; password; platform.mode;
	 * 
	 */
	@BeforeEach
	public void setup() throws Exception {
		try {

			if (beanRef == null) {
				throw new Exception("@EJB injection failed");
			}

			// get props
			timeout = Long.parseLong(System.getProperty("jms_timeout"));
			user = System.getProperty("user");
			password = System.getProperty("password");
			mode = System.getProperty("platform.mode");

			// check props for errors
			if (timeout < 1) {
				throw new Exception("'jms_timeout' (milliseconds) in must be > 0");
			}
			if (user == null) {
				throw new Exception("'user' is null ");
			}
			if (password == null) {
				throw new Exception("'password' is null ");
			}
			if (mode == null) {
				throw new Exception("'platform.mode' is null");
			}

			beanRef.initLogging(props);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("Setup failed!", e);
		}
	}

	/*
	 * cleanup() is called after each test
	 */
	@AfterEach
	public void cleanup() throws Exception {
	}

	private void flushTheQueue() throws Exception {
		JmsTool tool = null;
		try {
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);

			logger.log(Logger.Level.TRACE, "Closing default Connection");
			tool.getDefaultConnection().close();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error creating JmsTool and closing Connection", e);
		} finally {
			try {
				tool.flushDestination();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error flush Destination: ", e);
			}
		}
	}

	/*
	 * @testName: simpleSendReceiveQueueTest
	 * 
	 * @assertion_ids: JMS:JAVADOC:334; JMS:JAVADOC:122; JMS:JAVADOC:504;
	 * JMS:JAVADOC:510; JMS:JAVADOC:242; JMS:JAVADOC:244; JMS:JAVADOC:221;
	 * JMS:JAVADOC:317;
	 * 
	 * @test_Strategy: Create a Text Message, send use a MessageProducer and receive
	 * it use a MessageConsumer via a Queue
	 */
	@Test
	public void simpleSendReceiveQueueTest() throws Exception {
		String testMessage = "Just a test";
		String messageReceived = null;

		try {

			beanRef.sendTextMessage_CQ(testName, testMessage);
			messageReceived = beanRef.receiveTextMessage_CQ();

			// Check to see if correct message received
			if (messageReceived != null) {
				if (messageReceived.equals(testMessage)) {
					logger.log(Logger.Level.INFO, "Message text: \"" + messageReceived + "\"");
					logger.log(Logger.Level.INFO, "Received correct message");
				} else {
					throw new Exception("didn't get the right message");
				}
			} else {
				throw new Exception("didn't get any message");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "simpleSendReceiveQueueTest failed: ", e);
			throw new Exception("simpleSendReceiveQueueTest failed !", e);
		} finally {
			try {
				flushTheQueue();
				if (null != beanRef)
					beanRef.remove();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "[Client] Ignoring Exception on " + "bean remove", e);
			}
		}
	}

	/*
	 * @testName: selectorAndBrowserTests
	 *
	 * @assertion_ids: JMS:JAVADOC:502; JMS:JAVADOC:504; JMS:JAVADOC:510;
	 * JMS:JAVADOC:242; JMS:JAVADOC:244; JMS:JAVADOC:246; JMS:JAVADOC:317;
	 * JMS:JAVADOC:334; JMS:JAVADOC:258; JMS:JAVADOC:260; JMS:JAVADOC:338;
	 * JMS:JAVADOC:221; JMS:SPEC:148; JMS:SPEC:149; JMS:JAVADOC:278;
	 * JMS:JAVADOC:280; JMS:JAVADOC:282; JMS:JAVADOC:284; JMS:JAVADOC:288;
	 *
	 * @test_Strategy: 1. Create two TextMessages, send use a MessageProducer 2.
	 * Create a QueueBrowser to browse the Queue so that all two messages can be
	 * seen. 3. Use the QueueBrowser to test getQueue 4. Create a QueueBrowser with
	 * selector to browse the Queue so that only one message can be seen; 5. Use the
	 * above QueueBrowser to test getMessageSelector 6. Create a MessageConsumer
	 * with a message selector so that only last message received. 7. Then create
	 * another MessageConsumer to verify all messages except the last message can be
	 * received from the Queue.
	 */
	@Test
	public void selectorAndBrowserTests() throws Exception {
		String testMessage = "Just a test: selectorAndBrowserTests";
		boolean pass = true;
		String messageReceived = null;

		try {

			beanRef.sendMessageP_CQ(testName, testMessage, false);
			beanRef.sendMessagePP_CQ(testName, testMessage, true, "TEST", "test");

			int msgNum = beanRef.browseTextMessage_CQ(2, testMessage);
			logger.log(Logger.Level.TRACE, "Default browser found " + msgNum + " messages");

			if (!beanRef.getQueue()) {
				logger.log(Logger.Level.ERROR, "Error: QueueBrowser.getQueue test failed");
				pass = false;
			}

			msgNum = beanRef.browseMessageS_CQ(1, testMessage, "TEST = 'test'");
			logger.log(Logger.Level.TRACE, "Selective browser find " + msgNum + " messages");

			if (!beanRef.getSelector("TEST = 'test'")) {
				logger.log(Logger.Level.ERROR, "Error: QueueBrowser.getMessageSelector test failed");
				pass = false;
			}

			if (pass != true)
				throw new Exception("selectorAndBrowserTests Failed!");
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("selectorAndBrowserTests");
		} finally {
			try {
				flushTheQueue();
				if (null != beanRef)
					beanRef.remove();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "[Client] Ignoring Exception on " + "bean remove", e);
			}
		}
	}
}
