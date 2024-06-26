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
package com.sun.ts.tests.jms.ee.mdb.mdb_sndQ;

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
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

@Stateful
@Remote(MDB_SNDQ_Test.class)
public class MDB_SNDQ_TestEJB {

	@Resource
	private SessionContext sessionContext;

	@Resource(name = "jms/MDB_QUEUE")
	private transient Queue Dest;

	@Resource(name = "jms/MDB_QUEUE_REPLY")
	private transient Queue receiveDest;

	@Resource(name = "jms/MyQueueConnectionFactory")
	private transient ConnectionFactory cf;

	private transient Connection Conn;

	private Properties p = null;

	private long timeout;

	private String jmsUser;

	private String jmsPassword;

	private static final Logger logger = (Logger) System.getLogger(MDB_SNDQ_TestEJB.class.getName());

	public MDB_SNDQ_TestEJB() {
	}

	public void setup(Properties props) {
		logger.log(Logger.Level.TRACE, "MDB_SNDQ_TestEJB.askMDBToSendAMessage()");
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
			if (cf == null || Dest == null || receiveDest == null || sessionContext == null) {
				throw new Exception("@Resource injection failed");
			}
		} catch (Exception e) {
			throw new EJBException("@setup failed: ", e);
		}
	}

	public boolean askMDBToSendAMessage(String typeOfMessage) {
		logger.log(Logger.Level.TRACE, "MDB_SNDQ_TestEJB.askMDBToSendAMessage()");
		boolean ok = true;
		String myMessage = "I want you to send a message";
		try {
			Conn = cf.createConnection(jmsUser, jmsPassword);
			Session session = Conn.createSession(true, 0);
			Conn.start();

			MessageProducer mSender = session.createProducer(Dest);

			// create a text message
			TextMessage msg = session.createTextMessage();
			JmsUtil.addPropsToMessage(msg, p);
			msg.setText(myMessage);
			msg.setStringProperty("MessageType", typeOfMessage);

			// send the message
			mSender.send(msg);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("@askMDBToSendAMessage: Error!", e);
		} finally {
			try {
				if (Conn != null) {
					Conn.close();
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error closing connection in askMDBToSendAMessage", e);
			}
		}
		return ok;
	}

	// Validate that a given message was sent by mdb
	// prop = validate string
	public boolean checkOnResponse(String prop) {
		boolean status = false;
		try {
			logger.log(Logger.Level.TRACE, "MDB_SNDQ_TestEJB.checkOnResponse()");
			Conn = cf.createConnection(jmsUser, jmsPassword);
			Conn.start();

			Session session = Conn.createSession(true, 0);
			status = recvMessageInternal(session, prop);
			logger.log(Logger.Level.TRACE, "Close the session");
			session.close();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error in checkOnResponse", e);
		} finally {
			try {
				if (Conn != null) {
					Conn.close();
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error closing connection in checkOnResponse", e);
			}
		}
		return status;
	}

	private boolean recvMessageInternal(Session session, String prop) throws JMSException {
		boolean retcode = false;
		logger.log(Logger.Level.TRACE, "MDB_SNDQ_TestEJB.recvMessageInternal()");

		// Create a message producer.
		MessageConsumer rcvr = session.createConsumer(receiveDest);

		// remove the response from the mdb
		Message msgRec = null;
		for (int i = 0; i < 10; ++i) {
			logger.log(Logger.Level.TRACE, "@recvMessageInternal trying to receive the message: " + i);
			msgRec = rcvr.receive(timeout);
			if (msgRec != null) {
				break;
			}
		} // end for loop

		if (msgRec != null) {
			if (msgRec.getStringProperty("MessageType").equals(prop)) {
				logger.log(Logger.Level.TRACE,
						"Success: received Msg from Q!  " + msgRec.getStringProperty("MessageType"));
				logger.log(Logger.Level.TRACE, "Pass: we got the expected msg back! ");
				retcode = true;
			} else {
				logger.log(Logger.Level.ERROR, "Fail: we didnt get the expected msg back! ");
				logger.log(Logger.Level.ERROR, "Msg from Q:  " + msgRec.getStringProperty("MessageType"));
			}
		} else {
			logger.log(Logger.Level.ERROR, "Fail: we didnt get the expected msg back! ");
		}
		return retcode;
	}

	public boolean isThereSomethingInTheQueue() {
		logger.log(Logger.Level.TRACE, "MDB_SNDQ_TestEJB.isThereSomethingInTheQueue()");
		QueueBrowser qBrowser = null;
		Enumeration msgs = null;
		boolean ret = false;
		try {
			// Hopefully nothing is left in the Destination
			Conn = cf.createConnection(jmsUser, jmsPassword);
			Conn.start();

			Session session = Conn.createSession(true, 0);
			qBrowser = session.createBrowser((jakarta.jms.Queue) receiveDest);
			msgs = qBrowser.getEnumeration();
			if (msgs.hasMoreElements()) {
				ret = true;
			}
			qBrowser.close();
			session.close();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error in isThereSomethingInTheQueue", e);
		} finally {
			try {
				if (Conn != null) {
					Conn.close();
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error closing connection in isThereSomethingInTheQueue", e);
			}
		}
		return ret;
	}

	public void cleanTheQueue() {
		logger.log(Logger.Level.TRACE, "MDB_SNDQ_TestEJB.cleanTheQueue()");

		QueueBrowser qBrowser = null;
		Enumeration msgs = null;
		int numMsgs = 0;
		TextMessage msgRec = null;
		MessageConsumer rcvr = null;

		try {
			Conn = cf.createConnection(jmsUser, jmsPassword);
			Session session = Conn.createSession(true, 0);
			Conn.start();

			// delete anything left in the Destination
			qBrowser = session.createBrowser((Queue) receiveDest);
			// count the number of messages
			msgs = qBrowser.getEnumeration();
			while (msgs.hasMoreElements()) {
				msgs.nextElement();
				numMsgs++;
			}
			qBrowser.close();

			// Read messages until Destination is cleaned
			rcvr = session.createConsumer(receiveDest);
			for (int n = 0; n < numMsgs; n++) {
				for (int i = 0; i < 10; ++i) {
					msgRec = (TextMessage) rcvr.receive(timeout);
					if (msgRec != null) {
						logger.log(Logger.Level.TRACE, "Removed message from Destination: " + n);
						break;
					}
					logger.log(Logger.Level.TRACE, "Attempt no: " + i + " Trying to delete message: " + n);
				} // end of internal for loop
			}
			session.close();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error in cleanTheQueue", e);
		} finally {
			try {
				if (Conn != null) {
					Conn.close();
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error closing connection in cleanTheQueue", e);
			}
		}
	}

	@Remove
	public void remove() {
		logger.log(Logger.Level.TRACE, "MDB_SNDQ_TestEJB.remove()");
	}

	@PostActivate
	public void activate() {
		logger.log(Logger.Level.TRACE, "MDB_SNDQ_TestEJB.activate()");
		try {
			TSNamingContext context = new TSNamingContext();
			cf = (ConnectionFactory) context.lookup("java:comp/env/jms/MyQueueConnectionFactory");
			logger.log(Logger.Level.TRACE, "looked up connection factory object");

			Dest = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE");
			logger.log(Logger.Level.TRACE, "looked up the Destination");
			receiveDest = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error looking up Queue, Reply Queue, and ConnectionFactory objects", e);
			throw new EJBException("@activate: Error!", e);
		}
	}

	@PrePassivate
	public void passivate() {
		logger.log(Logger.Level.TRACE, "MDB_SNDQ_TestEJB.passivate()");
		receiveDest = null;
		Conn = null;
		Dest = null;
		cf = null;
	}
}
