/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $Id$
 */

package com.sun.ts.tests.jms.ee.mdb.mdb_rec;

import java.lang.System.Logger;
import java.util.Enumeration;
import java.util.Properties;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsUtil;

import jakarta.annotation.Resource;
import jakarta.ejb.EJBException;
import jakarta.ejb.PostActivate;
import jakarta.ejb.PrePassivate;
import jakarta.ejb.Remote;
import jakarta.ejb.Remove;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateful;
import jakarta.jms.BytesMessage;
import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.QueueConnection;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.QueueReceiver;
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.StreamMessage;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.TopicConnection;
import jakarta.jms.TopicConnectionFactory;
import jakarta.jms.TopicPublisher;
import jakarta.jms.TopicSession;

@Stateful
@Remote(MDB_AR_Test.class)
public class MDB_AR_TestEJB {

	@Resource
	private SessionContext sessionContext;

	@Resource(name = "jms/MDB_QUEUE_REPLY")
	private transient Queue rcvrQueue;

	@Resource(name = "jms/MDB_QUEUE")
	private transient Queue queue;

	@Resource(name = "jms/MyQueueConnectionFactory")
	private transient QueueConnectionFactory qcFactory;

	@Resource(name = "jms/MDB_TOPIC")
	private transient Topic topic;

	@Resource(name = "jms/MyTopicConnectionFactory")
	private transient TopicConnectionFactory tcFactory;

	private transient QueueConnection connection = null;

	private transient TopicConnection tConn = null;

	private Properties p = null;

	private String jmsUser;

	private String jmsPassword;

	private long timeout;

	private static final Logger logger = (Logger) System.getLogger(MDB_AR_TestEJB.class.getName());

	// Use this stateful session bean to test asynchronous receives of message
	// driven beans.
	// Has send methods for each type of jms message.

	public MDB_AR_TestEJB() {
	}

	public void setup(Properties props) {
		p = props;
		try {
			TestUtil.init(props);
			// get props
			timeout = Long.parseLong(System.getProperty("jms_timeout"));
			jmsUser = System.getProperty("user");
			jmsPassword = System.getProperty("password");
			// check props for errors
			if (timeout < 1) {
				throw new Exception("'jms_timeout' (milliseconds) in must be > 0");
			}
			if (jmsUser == null) {
				throw new Exception("'user' is null");
			}
			if (jmsPassword == null) {
				throw new Exception("'password' is null");
			}
			if (qcFactory == null || tcFactory == null || queue == null || rcvrQueue == null || topic == null
					|| sessionContext == null) {
				throw new Exception("@Resource injection failed");
			}
			tConn = tcFactory.createTopicConnection(jmsUser, jmsPassword);
			connection = qcFactory.createQueueConnection(jmsUser, jmsPassword);

			connection.start();
			tConn.start();
		} catch (Exception e) {
			throw new EJBException("@setup failed: ", e);
		}
	}

	public boolean isThereSomethingInTheQueue() {
		logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.isThereSomethingInTheQueue()");
		QueueBrowser qBrowser = null;
		Enumeration msgs = null;
		boolean ret = false;

		try {
			// Hopefully nothing is left in the queue
			QueueSession session = connection.createQueueSession(true, 0);
			qBrowser = session.createBrowser(rcvrQueue);
			msgs = qBrowser.getEnumeration();
			if (msgs.hasMoreElements()) {
				ret = true;
			}
			qBrowser.close();
			session.close();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error in isThereSomethingInTheQueue", e);
		}
		return ret;
	}

	public void cleanTheQueue() {
		logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.cleanTheQueue()");
		QueueBrowser qBrowser = null;
		Enumeration msgs = null;
		int numMsgs = 0;

		try {
			QueueSession session = connection.createQueueSession(true, 0);
			TextMessage msgRec = null;

			// delete anything left in the queue
			qBrowser = session.createBrowser(rcvrQueue);
			// count the number of messages
			msgs = qBrowser.getEnumeration();
			while (msgs.hasMoreElements()) {
				msgs.nextElement();
				numMsgs++;
			}
			qBrowser.close();

			// Read messages until Q is cleaned
			QueueReceiver rcvr = session.createReceiver(rcvrQueue);
			logger.log(Logger.Level.TRACE, "Cleaning " + numMsgs + " messages from the Q: " + rcvrQueue.getQueueName());
			for (int n = 0; n < numMsgs; n++) {

				logger.log(Logger.Level.TRACE, "dequeuing msg: " + n + " from the Q: " + rcvrQueue.getQueueName());
				for (int i = 0; i < 10; ++i) {
					msgRec = (TextMessage) rcvr.receive(timeout);
					if (msgRec != null) {
						logger.log(Logger.Level.TRACE, "dequeued message: " + n);
						break;
					}
					logger.log(Logger.Level.TRACE, "Attempt no: " + i + " Trying to dequeue message: " + n);
				} // end of internal for loop
			}
			session.close();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error in cleanTheQueue", e);
		}
	}

	public boolean checkOnResponse(String prop) {
		logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.checkOnResponse()");
		boolean status = false;

		try {
			QueueSession session = connection.createQueueSession(true, 0);
			status = recvMessageInternal(session, prop);
			logger.log(Logger.Level.TRACE, "Close the session");
			session.close();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception in checkOnResponse", e);
		}
		return status;
	}

	/**
	 * Send a message. In bmt case, create session BEFORE starting tx.
	 */
	public void sendTextMessageToQ(String prop) {
		try {
			String msg = "Test with a text message";
			logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.sendTextMessageToQ()");
			QueueSession session = connection.createQueueSession(true, 0);
			//

			logger.log(Logger.Level.TRACE, "calling send method with: " + msg + " + " + prop);
			QueueSender sender = session.createSender(queue);
			//
			logger.log(Logger.Level.TRACE, "got a q sender for: " + sender.getQueue().getQueueName());
			// Send a message.
			TextMessage message = session.createTextMessage();
			JmsUtil.addPropsToMessage(message, p);
			message.setText(msg);
			message.setStringProperty("Verify", prop);
			logger.log(Logger.Level.TRACE,
					"gonna send msg with this property to the mdb: " + message.getStringProperty("Verify"));

			sender.send(message);
			logger.log(Logger.Level.TRACE, "Close the session");
			session.close();
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}

	}

	/**
	 * Send a bytes message.
	 */
	public void sendBytesMessageToQ(String prop) {

		byte bValue = 9;
		try {
			logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.sendBytesMessageToQ()");
			QueueSession session = connection.createQueueSession(true, 0);
			//

			logger.log(Logger.Level.TRACE, "calling send method with: " + prop);
			QueueSender sender = session.createSender(queue);
			//
			logger.log(Logger.Level.TRACE, "got a q sender for: " + sender.getQueue().getQueueName());
			// Create a bytes message
			BytesMessage message = session.createBytesMessage();
			JmsUtil.addPropsToMessage(message, p);
			message.writeByte(bValue);
			message.setStringProperty("Verify", prop);
			logger.log(Logger.Level.TRACE,
					"gonna send msg with this property to the mdb: " + message.getStringProperty("Verify"));

			sender.send(message);
			logger.log(Logger.Level.TRACE, "Close the session");
			session.close();
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/**
	 * Send a stream message.
	 */
	public void sendStreamMessageToQ(String prop) {
		try {
			logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.sendStreamMessageToQ()");
			QueueSession session = connection.createQueueSession(true, 0);
			//

			logger.log(Logger.Level.TRACE, "calling send method with: " + prop);
			QueueSender sender = session.createSender(queue);
			//
			logger.log(Logger.Level.TRACE, "got a q sender for: " + sender.getQueue().getQueueName());
			// Create a stream message
			StreamMessage message = session.createStreamMessage();
			JmsUtil.addPropsToMessage(message, p);
			message.writeString("Testing with a Stream message...");
			message.setStringProperty("Verify", prop);
			logger.log(Logger.Level.TRACE,
					"gonna send msg with this property to the mdb: " + message.getStringProperty("Verify"));

			sender.send(message);
			logger.log(Logger.Level.TRACE, "Close the session");
			session.close();
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}

	}

	/**
	 * Send a map message.
	 */
	public void sendMapMessageToQ(String prop) {
		try {
			logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.sendMapMessageToQ()");
			QueueSession session = connection.createQueueSession(true, 0);
			//

			logger.log(Logger.Level.TRACE, "calling send method with: " + prop);
			QueueSender sender = session.createSender(queue);
			//
			logger.log(Logger.Level.TRACE, "got a q sender for: " + sender.getQueue().getQueueName());
			// Create a map message
			MapMessage message = session.createMapMessage();
			JmsUtil.addPropsToMessage(message, p);
			message.setString("aString", "Testing async receives");
			message.setStringProperty("Verify", prop);
			logger.log(Logger.Level.TRACE,
					"gonna send msg with this property to the mdb: " + message.getStringProperty("Verify"));

			sender.send(message);
			logger.log(Logger.Level.TRACE, "Close the session");
			session.close();
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}

	}

	/**
	 * Send an object message.
	 */
	public void sendObjectMessageToQ(String prop) {
		try {
			logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.sendObjectMessageToQ()");
			QueueSession session = connection.createQueueSession(true, 0);
			//

			logger.log(Logger.Level.TRACE, "calling send method with: " + prop);
			QueueSender sender = session.createSender(queue);
			//
			logger.log(Logger.Level.TRACE, "got a q sender for: " + sender.getQueue().getQueueName());
			// Create a object message
			ObjectMessage message = session.createObjectMessage();
			JmsUtil.addPropsToMessage(message, p);
			message.setObject("Object Message !!!!");
			message.setStringProperty("Verify", prop);
			logger.log(Logger.Level.TRACE,
					"gonna send msg with this property to the mdb: " + message.getStringProperty("Verify"));

			sender.send(message);
			logger.log(Logger.Level.TRACE, "Close the session");
			session.close();
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/**
	 * Send a text message to a topic.
	 */
	public void sendTextMessageToTopic(String prop) {
		try {
			String msg = "Testing  a Text message";
			logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.sendTextMessageToTopic()");
			TopicSession session = tConn.createTopicSession(true, 0);

			logger.log(Logger.Level.TRACE, "calling send method with: " + prop);
			TopicPublisher publisher = session.createPublisher(topic);
			logger.log(Logger.Level.TRACE, "got a topic publisher for: " + publisher.getTopic().getTopicName());
			// Send a message.
			TextMessage message = session.createTextMessage();
			JmsUtil.addPropsToMessage(message, p);
			message.setText(msg);
			message.setStringProperty("Verify", prop);
			logger.log(Logger.Level.TRACE,
					"gonna send msg with this property to the mdb: " + message.getStringProperty("Verify"));

			publisher.publish(message);
			logger.log(Logger.Level.TRACE, "Close the session");
			session.close();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception in sendTextMessageToTopic", e);
		}

	}

	/**
	 * Send a map message to a topic.
	 */
	public void sendMapMessageToTopic(String prop) {
		try {
			String msg = "Testing  a Map message";
			logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.sendMapMessageToTopic()");
			TopicSession session = tConn.createTopicSession(true, 0);

			logger.log(Logger.Level.TRACE, "calling send method with: " + prop);
			TopicPublisher publisher = session.createPublisher(topic);
			logger.log(Logger.Level.TRACE, "got a topic publisher for: " + publisher.getTopic().getTopicName());

			// Send a message.
			MapMessage message = session.createMapMessage();
			JmsUtil.addPropsToMessage(message, p);
			message.setString("aString", "Testing async receives");
			message.setStringProperty("Verify", prop);
			logger.log(Logger.Level.TRACE,
					"gonna send msg with this property to the mdb: " + message.getStringProperty("Verify"));

			publisher.publish(message);
			logger.log(Logger.Level.TRACE, "Close the session");
			session.close();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception in sendMapMessageToTopic", e);
		}
	}

	/**
	 * Send a object message to a topic.
	 */
	public void sendObjectMessageToTopic(String prop) {
		try {
			String msg = "Testing  a Object message";
			logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.sendObjectMessageToTopic()");
			TopicSession session = tConn.createTopicSession(true, 0);

			logger.log(Logger.Level.TRACE, "calling send method with: " + prop);
			TopicPublisher publisher = session.createPublisher(topic);
			logger.log(Logger.Level.TRACE, "got a topic publisher for: " + publisher.getTopic().getTopicName());

			// Send a message.
			ObjectMessage message = session.createObjectMessage();
			JmsUtil.addPropsToMessage(message, p);
			message.setObject("Object Message !!!!");
			message.setStringProperty("Verify", prop);
			logger.log(Logger.Level.TRACE,
					"gonna send msg with this property to the mdb: " + message.getStringProperty("Verify"));

			publisher.publish(message);
			logger.log(Logger.Level.TRACE, "Close the session");
			session.close();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception in sendObjectMessageToTopic", e);
		}
	}

	/**
	 * Send a Bytes message to a topic.
	 */
	public void sendBytesMessageToTopic(String prop) {
		byte bValue = 11;
		try {
			String msg = "Testing  a Bytes message";
			logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.sendBytesMessageToTopic()");
			TopicSession session = tConn.createTopicSession(true, 0);

			logger.log(Logger.Level.TRACE, "calling send method with: " + prop);
			TopicPublisher publisher = session.createPublisher(topic);
			logger.log(Logger.Level.TRACE, "got a topic publisher for: " + publisher.getTopic().getTopicName());

			// Send a message.
			BytesMessage message = session.createBytesMessage();
			JmsUtil.addPropsToMessage(message, p);
			message.writeByte(bValue);
			message.setStringProperty("Verify", prop);
			logger.log(Logger.Level.TRACE,
					"gonna send msg with this property to the mdb: " + message.getStringProperty("Verify"));

			publisher.publish(message);
			logger.log(Logger.Level.TRACE, "Close the session");
			session.close();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception in sendBytesMessageToTopic", e);
		}
	}

	/**
	 * Send a Stream message to a topic.
	 */
	public void sendStreamMessageToTopic(String prop) {
		byte bValue = 11;
		try {
			String msg = "Testing  a Stream message";
			logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.sendStreamMessageToTopic()");
			TopicSession session = tConn.createTopicSession(true, 0);

			logger.log(Logger.Level.TRACE, "calling send method with: " + prop);
			TopicPublisher publisher = session.createPublisher(topic);
			logger.log(Logger.Level.TRACE, "got a topic publisher for: " + publisher.getTopic().getTopicName());

			// Send a message.
			StreamMessage message = session.createStreamMessage();
			JmsUtil.addPropsToMessage(message, p);
			message.writeString("Testing with a Stream message...");
			message.setStringProperty("Verify", prop);
			logger.log(Logger.Level.TRACE,
					"gonna send msg with this property to the mdb: " + message.getStringProperty("Verify"));

			publisher.publish(message);
			logger.log(Logger.Level.TRACE, "Close the session");
			session.close();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception in sendStreamMessageToTopic", e);
		}
	}

	private boolean recvMessageInternal(QueueSession session, String prop) throws JMSException {
		boolean retcode = false;
		logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.recvMessageInternal()");
		// Create a message producer.
		QueueReceiver rcvr = session.createReceiver(rcvrQueue);
		// dequeue the response from the mdb
		Message msgRec = null;

		for (int i = 0; i < 10; ++i) {
			logger.log(Logger.Level.TRACE, "@recvMessageInternal trying to receive the message: " + i);
			msgRec = rcvr.receive(timeout);
			if (msgRec != null) {
				break;
			}
		} // end for loop

		if (msgRec != null) {
			logger.log(Logger.Level.TRACE, "Success: received Msg from Q!  " + msgRec.getStringProperty("Verify"));
			if (msgRec.getStringProperty("Verify").equals(prop)) {
				logger.log(Logger.Level.TRACE, "Pass: we got the expected msg back! ");
				retcode = true;
			} else {
				logger.log(Logger.Level.TRACE, "Fail: we didnt get the expected msg back! ");
			}
		} else {
			logger.log(Logger.Level.TRACE, "Fail: we didnt get any msg back! ");
		}
		return retcode;
	}

	public void cleanup() {
		logger.log(Logger.Level.TRACE, "In EJB cleanup");
		try {
			if (connection != null) {
				connection.close();
			}

			if (tConn != null) {
				tConn.close();
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception in cleanup", e);
		}
	}

	@PostActivate
	public void activate() {
		logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.activate()");
		try {
			TSNamingContext context = new TSNamingContext();
			logger.log(Logger.Level.TRACE, "Got TSNamingContext");

			qcFactory = (QueueConnectionFactory) context.lookup("java:comp/env/jms/MyQueueConnectionFactory");
			rcvrQueue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
			queue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE");

			tcFactory = (TopicConnectionFactory) context.lookup("java:comp/env/jms/MyTopicConnectionFactory");
			topic = (Topic) context.lookup("java:comp/env/jms/MDB_TOPIC");

			tConn = tcFactory.createTopicConnection(jmsUser, jmsPassword);
			connection = qcFactory.createQueueConnection(jmsUser, jmsPassword);

			connection.start();
			tConn.start();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR,
					"Error looking up Queue, Topic, ConnectionFactory objects, Starting connections", e);
			throw new EJBException("@activate: Error!", e);
		}
	}

	@PrePassivate
	public void passivate() {
		logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.passivate()");

		queue = null;
		rcvrQueue = null;
		qcFactory = null;
		topic = null;
		tcFactory = null;

		cleanup();
	}

	@Remove
	public void remove() {
		logger.log(Logger.Level.TRACE, "MDB_AR_TestEJB.remove()");
	}
}
