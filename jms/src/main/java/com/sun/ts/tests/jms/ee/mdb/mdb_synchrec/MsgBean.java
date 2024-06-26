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

package com.sun.ts.tests.jms.ee.mdb.mdb_synchrec;

import java.lang.System.Logger;
import java.util.Properties;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsUtil;

import jakarta.ejb.EJBException;
import jakarta.ejb.MessageDrivenBean;
import jakarta.ejb.MessageDrivenContext;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnection;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.QueueReceiver;
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.TextMessage;

public class MsgBean implements MessageDrivenBean, MessageListener {

	// properties object needed for logging,
	// get this from the message object passed into
	// the onMessage method.
	private java.util.Properties p = null;

	// Contexts
	private TSNamingContext context = null;

	protected MessageDrivenContext mdc = null;

	// JMS PTP
	private QueueConnectionFactory qFactory;

	private QueueConnection qConnection = null;

	private Queue queueR = null;

	private Queue queueS = null;

	private QueueSender mSender = null;

	private boolean result = false;

	private static final Logger logger = (Logger) System.getLogger(MsgBean.class.getName());

	public MsgBean() {
		logger.log(Logger.Level.TRACE, "@MsgBean()!");
	};

	public void ejbCreate() {
		logger.log(Logger.Level.TRACE, "In Message Bean ======================================EJBCreate");
		try {

			context = new TSNamingContext();
			qFactory = (QueueConnectionFactory) context.lookup("java:comp/env/jms/MyQueueConnectionFactory");
			queueR = (Queue) context.lookup("java:comp/env/jms/MY_QUEUE");
			queueS = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
			p = new Properties();

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("MDB ejbCreate Error", e);
		}
	}

	public void onMessage(Message msg) {
		long timeout = 10000;
		QueueSession qSession = null;
		TextMessage messageSent = null;
		TextMessage msgRec = null;
		String mdbMessage = "my mdb message";
		String testName = null;
		QueueSender qSender = null;
		QueueReceiver rcvr = null;
		boolean result = false;

		JmsUtil.initHarnessProps(msg, p);
		logger.log(Logger.Level.TRACE, "In Message Bean ======================================onMessage");
		try {
			testName = "mdbResponse";
			qConnection = qFactory.createQueueConnection();
			if (qConnection == null)
				throw new EJBException("MDB connection Error!");

			qConnection.start();

			qSession = qConnection.createQueueSession(true, 0);
			logger.log(Logger.Level.TRACE, "will run TestCase: " + testName);

			rcvr = qSession.createReceiver(queueR);

			logger.log(Logger.Level.TRACE, "Verify the synchronous receive");
			logger.log(Logger.Level.TRACE, "HHHHHHHHHHHHH+++++++++  Trying to receive message from the Queue: ");
			msgRec = (TextMessage) rcvr.receive(timeout);

			if (msgRec != null) {
				//
				logger.log(Logger.Level.TRACE, "mdb received a msg from MY_QUEUE");
				if (msgRec.getStringProperty("TestCase").equals(mdbMessage)) {
					logger.log(Logger.Level.TRACE, "Success: Correct msg recvd from MY_QUEUE");
					result = true;
				}
			}
			// send results to QUEUE_REPLY

			JmsUtil.sendTestResults(testName, result, qSession, queueS);
			logger.log(Logger.Level.TRACE, "Mdb test results send to queue reply");

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		} finally {
			if (qConnection != null) {
				try {
					qConnection.close();
				} catch (Exception e) {
					TestUtil.printStackTrace(e);
				}
			}
		}
	}

	public void setMessageDrivenContext(MessageDrivenContext mdc) {
		logger.log(Logger.Level.TRACE, "@MsgBean:setMessageDrivenContext()!");
		this.mdc = mdc;
	}

	public void ejbRemove() {
		logger.log(Logger.Level.TRACE, "@ejbRemove()");
	}
}
