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

package com.sun.ts.tests.jms.ee.mdb.mdb_sndToTopic;

import java.lang.System.Logger;
import java.util.Properties;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsUtil;

import jakarta.ejb.EJBException;
import jakarta.ejb.MessageDrivenBean;
import jakarta.ejb.MessageDrivenContext;
import jakarta.jms.BytesMessage;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import jakarta.jms.StreamMessage;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.TopicConnection;
import jakarta.jms.TopicConnectionFactory;
import jakarta.jms.TopicPublisher;
import jakarta.jms.TopicSession;

public class MsgBeanToTopic implements MessageDrivenBean, MessageListener {

	// properties object needed for logging, get this from the message object
	// passed into
	// the onMessage method.
	private java.util.Properties p = null;

	private TSNamingContext context = null;

	private MessageDrivenContext mdc = null;

	private static final Logger logger = (Logger) System.getLogger(MsgBeanToTopic.class.getName());

	// JMS
	private TopicConnectionFactory tFactory = null;

	private TopicConnection tConnection = null;

	private Topic topic = null;

	private TopicPublisher mPublisher = null;

	private TopicSession tSession = null;

	public MsgBeanToTopic() {
		logger.log(Logger.Level.TRACE, "@MsgBeanToTopic()!");
	};

	public void ejbCreate() {
		logger.log(Logger.Level.TRACE, "@MsgBeanToTopic-ejbCreate() !!");
		try {
			context = new TSNamingContext();
			tFactory = (TopicConnectionFactory) context.lookup("java:comp/env/jms/MyTopicConnectionFactory");
			topic = (Topic) context.lookup("java:comp/env/jms/MDB_TOPIC_REPLY");
			p = new Properties();
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("MDB ejbCreate Error!", e);
		}
	}

	public void onMessage(Message msg) {
		JmsUtil.initHarnessProps(msg, p);
		logger.log(Logger.Level.TRACE, "@MsgBeanToTopic - onMessage! " + msg);

		try {
			tConnection = tFactory.createTopicConnection();
			tConnection.start();
			logger.log(Logger.Level.TRACE, "started the connection !!");

			// Send a message back to acknowledge that the mdb received the message.
			tSession = tConnection.createTopicSession(true, 0);

			if (msg.getStringProperty("MessageType").equals("TextMessage")) {
				sendATextMessage();
			} else if (msg.getStringProperty("MessageType").equals("BytesMessage")) {
				sendABytesMessage();
			} else if (msg.getStringProperty("MessageType").equals("MapMessage")) {
				sendAMapMessage();
			} else if (msg.getStringProperty("MessageType").equals("StreamMessage")) {
				sendAStreamMessage();
			} else if (msg.getStringProperty("MessageType").equals("ObjectMessage")) {
				sendAnObjectMessage();
			} else {
				logger.log(Logger.Level.TRACE, "@onMessage - invalid message type found in StringProperty");
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		} finally {
			try {
				if (tConnection != null) {
					tConnection.close();
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
			}
		}

	}

	// message bean helper methods follow.
	// Each method will send a simple message of the type requested.
	// this will send a text message to a Topic

	// must call init for logging to be properly performed
	public void initLogging(java.util.Properties p) {
		try {
			TestUtil.init(p);
			logger.log(Logger.Level.TRACE, "MsgBean initLogging OK.");
		} catch (RemoteLoggingInitException e) {
			TestUtil.printStackTrace(e);
			logger.log(Logger.Level.INFO, "MsgBean initLogging failed.");
			throw new EJBException(e.getMessage());
		}
	}

	private void sendATextMessage() {
		logger.log(Logger.Level.TRACE, "@MsgBean - sendATextMessage");
		try {
			String myMsg = "I am sending a text message as requested";
			// send a text message as requested to MDB_TOPIC_REPLY
			mPublisher = tSession.createPublisher(topic);
			TextMessage msg = tSession.createTextMessage();
			JmsUtil.addPropsToMessage(msg, p);
			msg.setText(myMsg);
			msg.setStringProperty("MessageType", "TextMessageFromMsgBean");
			mPublisher.publish(msg);

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	private void sendABytesMessage() {
		logger.log(Logger.Level.TRACE, "@MsgBean - sendABytesMessage");
		try {
			byte aByte = 10;
			// send a bytes message as requested to MDB_TOPIC_REPLY
			mPublisher = tSession.createPublisher(topic);
			BytesMessage msg = tSession.createBytesMessage();
			JmsUtil.addPropsToMessage(msg, p);
			msg.writeByte(aByte);
			msg.setStringProperty("MessageType", "BytesMessageFromMsgBean");
			mPublisher.publish(msg);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	private void sendAMapMessage() {
		logger.log(Logger.Level.TRACE, "@MsgBean - sendAMapMessage");
		try {
			String myMsg = "I am sending a map message as requested";
			// send a map message as requested to MDB_TOPIC_REPLY
			mPublisher = tSession.createPublisher(topic);
			MapMessage msg = tSession.createMapMessage();
			JmsUtil.addPropsToMessage(msg, p);
			msg.setString("MapMessage", myMsg);
			msg.setStringProperty("MessageType", "MapMessageFromMsgBean");
			mPublisher.publish(msg);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	private void sendAStreamMessage() {
		logger.log(Logger.Level.TRACE, "@MsgBean - sendAStreamMessage");
		try {
			String myMsg = "I am sending a stream message as requested";
			// send a stream message as requested to MDB_TOPIC_REPLY
			mPublisher = tSession.createPublisher(topic);
			StreamMessage msg = tSession.createStreamMessage();
			JmsUtil.addPropsToMessage(msg, p);
			msg.writeString(myMsg);
			msg.setStringProperty("MessageType", "StreamMessageFromMsgBean");
			mPublisher.publish(msg);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	private void sendAnObjectMessage() {
		logger.log(Logger.Level.TRACE, "@MsgBean - sendAnObjectMessage");
		try {
			String myMsg = "I am sending a text message as requested";
			// send an object message as requested to MDB_TOPIC_REPLY
			mPublisher = tSession.createPublisher(topic);

			ObjectMessage msg = tSession.createObjectMessage();
			JmsUtil.addPropsToMessage(msg, p);
			msg.setObject(myMsg);
			msg.setStringProperty("MessageType", "ObjectMessageFromMsgBean");
			mPublisher.publish(msg);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	public void setMessageDrivenContext(MessageDrivenContext mdc) {
		logger.log(Logger.Level.TRACE, "In MsgBean::setMessageDrivenContext()!!");
		this.mdc = mdc;
	}

	public void ejbRemove() {
		logger.log(Logger.Level.TRACE, "In MsgBean::remove()!!");
	}
}
