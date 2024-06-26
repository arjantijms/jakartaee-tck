/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jms.ee20.cditests.ejbweb;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import com.sun.ts.lib.util.TestUtil;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.IllegalStateRuntimeException;
import jakarta.jms.JMSConnectionFactory;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSPasswordCredential;
import jakarta.jms.JMSSessionMode;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.TopicConnectionFactory;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletClient extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Properties harnessProps = null;

	long timeout;

	String user;

	String password;

	String mode;

	ArrayList queues = null;

	ArrayList connections = null;

	private static final int numMessages = 3;

	private static final int iterations = 5;

	private static int testsExecuted = 0;

	private static final Logger logger = (Logger) System.getLogger(ServletClient.class.getName());

	// JMSContext CDI injection specifying QueueConnectionFactory
	@Inject
	@JMSConnectionFactory("jms/QueueConnectionFactory")
	transient JMSContext context1;

	// JMSContext CDI injection specifying TopicConnectionFactory
	@Inject
	@JMSConnectionFactory("jms/TopicConnectionFactory")
	transient JMSContext context2;

	// JMSContext CDI injection specifying ConnectionFactory,
	// Password Credentials, and Session Mode
	@Inject
	@JMSConnectionFactory("jms/ConnectionFactory")
	@JMSPasswordCredential(userName = "j2ee", password = "j2ee")
	@JMSSessionMode(JMSContext.DUPS_OK_ACKNOWLEDGE)
	transient JMSContext context3;

	// JMSContext CDI injection for default Connection Factory
	@Inject
	transient JMSContext context4;

	@Resource(name = "jms/MyConnectionFactory")
	private transient ConnectionFactory cfactory;

	@Resource(name = "jms/MyQueueConnectionFactory")
	private transient QueueConnectionFactory qcfactory;

	@Resource(name = "jms/MyTopicConnectionFactory")
	private transient TopicConnectionFactory tcfactory;

	@Resource(name = "jms/MY_QUEUE")
	private transient Queue queue;

	@Resource(name = "jms/MY_TOPIC")
	private transient Topic topic;

	@Resource(name = "mybean")
	private MyManagedBean mybean;

	/*
	 * Utility method to return the session mode as a String
	 */
	private String printSessionMode(int sessionMode) {
		switch (sessionMode) {
		case JMSContext.SESSION_TRANSACTED:
			return "SESSION_TRANSACTED";
		case JMSContext.AUTO_ACKNOWLEDGE:
			return "AUTO_ACKNOWLEDGE";
		case JMSContext.CLIENT_ACKNOWLEDGE:
			return "CLIENT_ACKNOWLEDGE";
		case JMSContext.DUPS_OK_ACKNOWLEDGE:
			return "DUPS_OK_ACKNOWLEDGE";
		default:
			return "UNEXPECTED_SESSIONMODE";
		}
	}

	private void cleanup() {
		logger.log(Logger.Level.INFO, "cleanup");
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("ServletClient:init() Entering");
		System.out.println("queue=" + queue);
		System.out.println("topic=" + topic);
		System.out.println("cfactory=" + cfactory);
		System.out.println("qcfactory=" + qcfactory);
		System.out.println("tcfactory=" + tcfactory);
		System.out.println("mybean=" + mybean);
		if (queue == null || topic == null || context1 == null || context2 == null || context3 == null
				|| context4 == null || cfactory == null || qcfactory == null || tcfactory == null || mybean == null) {
			throw new ServletException("init() failed: port injection failed");
		}
		System.out.println("ServletClient:init() Leaving");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		boolean pass = true;
		Properties p = new Properties();
		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			String test = System.getProperty("TEST");
			System.out.println("doGet: test to execute is: " + test);
			if (test.equals("sendRecvQueueTestUsingCDIFromServlet")) {
				if (sendRecvQueueTestUsingCDIFromServlet())
					p.setProperty("TESTRESULT", "pass");
				else
					p.setProperty("TESTRESULT", "fail");
			} else if (test.equals("sendRecvTopicTestUsingCDIFromServlet")) {
				if (sendRecvTopicTestUsingCDIFromServlet())
					p.setProperty("TESTRESULT", "pass");
				else
					p.setProperty("TESTRESULT", "fail");
			} else if (test.equals("sendRecvUsingCDIDefaultFactoryFromServlet")) {
				if (sendRecvUsingCDIDefaultFactoryFromServlet())
					p.setProperty("TESTRESULT", "pass");
				else
					p.setProperty("TESTRESULT", "fail");
			} else if (test.equals("verifySessionModeOnCDIJMSContextFromServlet")) {
				if (verifySessionModeOnCDIJMSContextFromServlet())
					p.setProperty("TESTRESULT", "pass");
				else
					p.setProperty("TESTRESULT", "fail");
			} else if (test.equals("testRestrictionsOnCDIJMSContextFromServlet")) {
				if (testRestrictionsOnCDIJMSContextFromServlet())
					p.setProperty("TESTRESULT", "pass");
				else
					p.setProperty("TESTRESULT", "fail");
			} else if (test.equals("sendRecvQueueTestUsingCDIFromManagedBean")) {
				if (sendRecvQueueTestUsingCDIFromManagedBean())
					p.setProperty("TESTRESULT", "pass");
				else
					p.setProperty("TESTRESULT", "fail");
			} else if (test.equals("sendRecvTopicTestUsingCDIFromManagedBean")) {
				if (sendRecvTopicTestUsingCDIFromManagedBean())
					p.setProperty("TESTRESULT", "pass");
				else
					p.setProperty("TESTRESULT", "fail");
			} else {
				p.setProperty("TESTRESULT", "fail");
			}
			cleanup();
			p.list(out);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "doGet: Exception: " + e);
			System.out.println("doGet: Exception: " + e);
			p.setProperty("TESTRESULT", "fail");
			p.list(out);
		}
		out.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		harnessProps = new Properties();
		Enumeration enumlist = req.getParameterNames();
		while (enumlist.hasMoreElements()) {
			String name = (String) enumlist.nextElement();
			String value = req.getParameter(name);
			harnessProps.setProperty(name, value);
		}

		try {
			TestUtil.init(harnessProps);
			mybean.init(harnessProps);
			timeout = Long.parseLong(System.getProperty("jms_timeout"));
			user = System.getProperty("user");
			password = System.getProperty("password");
		} catch (Exception e) {
			System.out.println("doPost: Exception: " + e);
			e.printStackTrace();
			throw new ServletException("unable to initialize remote logging");
		}
		doGet(req, res);
		harnessProps = null;
	}

	public boolean sendRecvQueueTestUsingCDIFromServlet() {
		boolean pass = true;
		JMSConsumer consumer = null;
		System.out.println("sendRecvQueueTestUsingCDIFromServlet");
		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;

			logger.log(Logger.Level.INFO, "Using CDI injected context1 specifying QueueConnectionFactory");

			// Create JMSConsumer from JMSContext
			consumer = context1.createConsumer(queue);

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			messageSent = context1.createTextMessage();
			messageSent.setText("just a test");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "sendRecvQueueTestUsingCDIFromServlet");
			logger.log(Logger.Level.INFO, "Sending TextMessage");
			context1.createProducer().send(queue, messageSent);
			logger.log(Logger.Level.INFO, "Receiving TextMessage");
			messageReceived = (TextMessage) consumer.receive(timeout);

			// Check to see if correct message received
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "No message was received");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Message Sent: \"" + messageSent.getText() + "\"");
				logger.log(Logger.Level.INFO, "Message Received: \"" + messageReceived.getText() + "\"");
				if (messageReceived.getText().equals(messageSent.getText())) {
					logger.log(Logger.Level.INFO, "Received correct message");
				} else {
					logger.log(Logger.Level.ERROR, "Received incorrect message");
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			pass = false;
		} finally {
			try {
				consumer.receive(timeout);
				while (consumer.receiveNoWait() != null)
					;
				consumer.close();
			} catch (Exception e) {
			}
		}
		return pass;
	}

	public boolean sendRecvTopicTestUsingCDIFromServlet() {
		boolean pass = true;
		JMSConsumer consumer = null;
		System.out.println("sendRecvTopicTestUsingCDIFromServlet");
		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;

			logger.log(Logger.Level.INFO, "Using CDI injected context2 specifying TopicConnectionFactory");

			// Create JMSConsumer from JMSContext
			consumer = context2.createConsumer(topic);

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			messageSent = context2.createTextMessage();
			messageSent.setText("just a test");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "sendRecvTopicTestUsingCDIFromServlet");
			logger.log(Logger.Level.INFO, "Sending TextMessage");
			context2.createProducer().send(topic, messageSent);
			logger.log(Logger.Level.INFO, "Receiving TextMessage");
			messageReceived = (TextMessage) consumer.receive(timeout);

			// Check to see if correct message received
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "No message was received");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Message Sent: \"" + messageSent.getText() + "\"");
				logger.log(Logger.Level.INFO, "Message Received: \"" + messageReceived.getText() + "\"");
				if (messageReceived.getText().equals(messageSent.getText())) {
					logger.log(Logger.Level.INFO, "Received correct message");
				} else {
					logger.log(Logger.Level.ERROR, "Received incorrect message");
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			pass = false;
		} finally {
			try {
				consumer.close();
			} catch (Exception e) {
			}
		}
		return pass;
	}

	public boolean sendRecvUsingCDIDefaultFactoryFromServlet() {
		boolean pass = true;
		JMSConsumer consumer = null;
		JMSConsumer consumer2 = null;
		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			TextMessage messageReceived2 = null;

			logger.log(Logger.Level.INFO, "Using CDI injected context4 using default system connection factory");

			// Create JMSConsumer from JMSContext for Queue
			logger.log(Logger.Level.INFO, "Creating Consumer for Queue");
			consumer = context4.createConsumer(queue);

			// Create JMSConsumer from JMSContext for Topic
			logger.log(Logger.Level.INFO, "Creating Consumer for Topic");
			consumer2 = context4.createConsumer(topic);

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			messageSent = context4.createTextMessage();
			messageSent.setText("just a test");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "sendRecvUsingCDIDefaultFactoryFromServlet");
			logger.log(Logger.Level.INFO, "Sending TextMessage to Queue");
			context4.createProducer().send(queue, messageSent);
			logger.log(Logger.Level.INFO, "Sending TextMessage to Topic");
			context4.createProducer().send(topic, messageSent);
			logger.log(Logger.Level.INFO, "Receiving TextMessage from Queue consumer");
			messageReceived = (TextMessage) consumer.receive(timeout);
			logger.log(Logger.Level.INFO, "Receiving TextMessage from Topic consumer");
			messageReceived2 = (TextMessage) consumer2.receive(timeout);

			// Check to see if correct message received from Queue consumer
			logger.log(Logger.Level.INFO, "Check received message from Queue consumer");
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "No message was received");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Message Sent: \"" + messageSent.getText() + "\"");
				logger.log(Logger.Level.INFO, "Message Received: \"" + messageReceived.getText() + "\"");
				if (messageReceived.getText().equals(messageSent.getText())) {
					logger.log(Logger.Level.INFO, "Received correct message");
				} else {
					logger.log(Logger.Level.ERROR, "Received incorrect message");
					pass = false;
				}
			}

			// Check to see if correct message received from Queue consumer
			logger.log(Logger.Level.INFO, "Check received message from Topic consumer");
			if (messageReceived2 == null) {
				logger.log(Logger.Level.ERROR, "No message was received");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Message Sent: \"" + messageSent.getText() + "\"");
				logger.log(Logger.Level.INFO, "Message Received: \"" + messageReceived2.getText() + "\"");
				if (messageReceived2.getText().equals(messageSent.getText())) {
					logger.log(Logger.Level.INFO, "Received correct message");
				} else {
					logger.log(Logger.Level.ERROR, "Received incorrect message");
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			pass = false;
		} finally {
			try {
				consumer.receive(timeout);
				while (consumer.receiveNoWait() != null)
					;
				consumer.close();
				consumer2.close();
			} catch (Exception e) {
			}
		}
		return pass;
	}

	public boolean verifySessionModeOnCDIJMSContextFromServlet() {
		boolean pass = true;
		System.out.println("verifySessionModeOnCDIJMSContextFromServlet");
		try {
			logger.log(Logger.Level.INFO,
					"Checking session mode of context3 should be " + printSessionMode(JMSContext.DUPS_OK_ACKNOWLEDGE));
			if (context3.getSessionMode() != JMSContext.DUPS_OK_ACKNOWLEDGE) {
				logger.log(Logger.Level.ERROR,
						"Incorrect session mode returned: " + printSessionMode(context3.getSessionMode())
								+ "  expected: " + printSessionMode(JMSContext.DUPS_OK_ACKNOWLEDGE));
				pass = false;
			} else {
				logger.log(Logger.Level.INFO,
						"Returned correct session mode: " + printSessionMode(JMSContext.DUPS_OK_ACKNOWLEDGE));
			}

			logger.log(Logger.Level.INFO,
					"Checking session mode of context2 should be " + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
			if (context2.getSessionMode() != JMSContext.AUTO_ACKNOWLEDGE) {
				logger.log(Logger.Level.ERROR,
						"Incorrect session mode returned: " + printSessionMode(context2.getSessionMode())
								+ "  expected: " + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
				pass = false;
			} else {
				logger.log(Logger.Level.INFO,
						"Returned correct session mode: " + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
			}

			logger.log(Logger.Level.INFO,
					"Checking session mode of context1 should be " + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
			if (context1.getSessionMode() != JMSContext.AUTO_ACKNOWLEDGE) {
				logger.log(Logger.Level.ERROR,
						"Incorrect session mode returned: " + printSessionMode(context1.getSessionMode())
								+ "  expected: " + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
				pass = false;
			} else {
				logger.log(Logger.Level.INFO,
						"Returned correct session mode: " + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			pass = false;
		}
		return pass;
	}

	public boolean testRestrictionsOnCDIJMSContextFromServlet() {
		boolean pass = true;
		System.out.println("testRestrictionsOnCDIJMSContextFromServlet");
		try {
			logger.log(Logger.Level.INFO, "Calling JMSContext.acknowledge() MUST throw IllegalStateRuntimeException");
			try {
				context1.acknowledge();
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO,
					"Calling JMSContext.setClientID(String) MUST throw IllegalStateRuntimeException");
			try {
				context1.setClientID("test");
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO,
					"Calling JMSContext.setExceptionListener(ExceptionListener) MUST throw IllegalStateRuntimeException");
			try {
				context1.setExceptionListener(null);
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Calling JMSContext.start() MUST throw IllegalStateRuntimeException");
			try {
				context1.start();
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Calling JMSContext.stop() MUST throw IllegalStateRuntimeException");
			try {
				context1.stop();
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Calling JMSContext.commit() MUST throw IllegalStateRuntimeException");
			try {
				context1.commit();
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Calling JMSContext.rollback() MUST throw IllegalStateRuntimeException");
			try {
				context1.rollback();
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Calling JMSContext.recover() MUST throw IllegalStateRuntimeException");
			try {
				context1.recover();
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO,
					"Calling JMSContext.setAutoStart(boolean) MUST throw IllegalStateRuntimeException");
			try {
				context1.setAutoStart(true);
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Calling JMSContext.close() MUST throw IllegalStateRuntimeException");
			try {
				context1.close();
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			pass = false;
		}
		return pass;
	}

	public boolean sendRecvQueueTestUsingCDIFromManagedBean() {
		logger.log(Logger.Level.INFO, "DEBUG: sendRecvQueueTestUsingCDIFromManagedBean");
		return mybean.sendRecvQueueTestUsingCDIFromManagedBean();
	}

	public boolean sendRecvTopicTestUsingCDIFromManagedBean() {
		logger.log(Logger.Level.INFO, "DEBUG: sendRecvTopicTestUsingCDIFromManagedBean");
		return mybean.sendRecvTopicTestUsingCDIFromManagedBean();
	}
}
