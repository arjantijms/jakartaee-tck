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

package com.sun.ts.tests.jms.ee.mdb.xa;

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
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import jakarta.transaction.Status;
import jakarta.transaction.UserTransaction;

public class MsgBeanxa implements MessageDrivenBean, MessageListener {

	// properties object needed for logging, get this from the message object
	// passed into
	// the onMessage method.
	private java.util.Properties p = null;

	private TSNamingContext context = null;

	protected MessageDrivenContext mdc = null;

	private static final Logger logger = (Logger) System.getLogger(MsgBeanxa.class.getName());

	// JMS
	private QueueConnectionFactory qFactory = null;

	private QueueConnection qConnection = null;

	private Queue queueR = null;

	private Queue queue = null;

	private QueueSender mSender = null;

	public MsgBeanxa() {
		logger.log(Logger.Level.TRACE, "@MsgBeanMsgTestPropsQ()!");
	};

	public void ejbCreate() {
		logger.log(Logger.Level.TRACE, "mdb -ejbCreate() !!");
		try {
			context = new TSNamingContext();
			qFactory = (QueueConnectionFactory) context.lookup("java:comp/env/jms/MyQueueConnectionFactory");
			queueR = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
			// queue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE");
			p = new Properties();

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("MDB ejbCreate Error!", e);
		}
	}

	public void onMessage(Message msg) {
		QueueSession qSession = null;

		JmsUtil.initHarnessProps(msg, p);

		logger.log(Logger.Level.TRACE, " @onMessage!" + msg);
		TextMessage messageSent = null;
		try {
			logger.log(Logger.Level.TRACE,
					"TestCase:=====================" + msg.getStringProperty("COM_SUN_JMS_TESTNAME"));
			//
			qConnection = qFactory.createQueueConnection();
			if (qConnection == null) {
				logger.log(Logger.Level.TRACE, "connection error");
			} else {
				// qConnection.start();
				qSession = qConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			}

			logger.log(Logger.Level.TRACE, "will run TestCase: " + msg.getStringProperty("COM_SUN_JMS_TESTNAME"));
			runTests(msg, qSession);

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

	private void runTests(Message msg, QueueSession qSession) {
		try {
			// test to see if this is the first message
			if (msg.getIntProperty("TestCaseNum") > 0) {
				switch (msg.getIntProperty("TestCaseNum")) {
				case 1:
					runTest1(msg, qSession);
					break;
				case 2:
					runTest2(msg, qSession);
					break;
				case 3:
					runTest3(msg, qSession);
					break;
				case 4:
					runTest4(msg, qSession);
					break;
				case 5:
					runTest5(msg, qSession);
					break;
				case 6:
					runTest6(msg, qSession);
					break;
				case 7:
					runTest7(msg, qSession);
					break;
				case 8:
					runTest8(msg, qSession);
					break;
				case 9:
					runTest9(msg, qSession);
					break;
				case 10:
					runTest10(msg, qSession);
					break;
				default:
					logger.log(Logger.Level.TRACE,
							"Error in mdb - No test match for TestCaseNum: " + msg.getIntProperty("TestCaseNum"));
					break;
				}
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}// runTests

	/*
	 *
	 */
	private void runTest1(Message msg, QueueSession qSession) {
		try {
			logger.log(Logger.Level.TRACE, "Second time? = " + isJmsRedelivered(msg));
			// check redelivered flag
			if (!isJmsRedelivered(msg)) {
				logger.log(Logger.Level.TRACE, "Test1 message: first time thru");
				logger.log(Logger.Level.TRACE, "Message Number: " + msg.getIntProperty("TestCaseNum"));
				mdc.setRollbackOnly();
			} else {
				logger.log(Logger.Level.TRACE, "Test1 message: again - Pass!");
				// the rollback requeued the message - so this test passed!
				JmsUtil.sendTestResults("xaTest1", true, qSession, queueR);
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 *
	 */
	private void runTest2(Message msg, QueueSession qSession) {
		try {
			if (!isJmsRedelivered(msg)) {
				logger.log(Logger.Level.TRACE, "Test2 message: first time thru");
				logger.log(Logger.Level.TRACE, "Message Number: " + msg.getIntProperty("TestCaseNum"));
				JmsUtil.sendTestResults("xaTest2", false, qSession, queueR);
				mdc.setRollbackOnly();
			} else {
				logger.log(Logger.Level.TRACE, "Test2 message: again - Pass!");
				// the rollback requeued the message - so this test passed!
				JmsUtil.sendTestResults("xaTest2", true, qSession, queueR);
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	private void runTest3(Message msg, QueueSession qSession) {
		if (mdc.getRollbackOnly()) {
			logger.log(Logger.Level.TRACE, "runTest3! =====    FAIL!");
			JmsUtil.sendTestResults("xaTest3", false, qSession, queueR);
		} else {
			logger.log(Logger.Level.TRACE, "runTest3 =====    PASS!");
			JmsUtil.sendTestResults("xaTest3", true, qSession, queueR);
		}
	}// runTest3

	private void runTest4(Message msg, QueueSession qSession) {
		int status;
		try {
			// Obtain the transaction demarcation interface.
			UserTransaction ut = mdc.getUserTransaction();
			status = ut.getStatus();
			// There is no transaction associated with this.
			if (status != Status.STATUS_NO_TRANSACTION) {
				JmsUtil.sendTestResults("xaTest4", false, qSession, queueR);
				logger.log(Logger.Level.TRACE, "runTest4 =====    FAIL!");
			} else {
				logger.log(Logger.Level.TRACE, "runTest4 =====    PASS!");
				JmsUtil.sendTestResults("xaTest4", true, qSession, queueR);
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	private void runTest5(Message msg, QueueSession qSession) {
		TextMessage newMsg;
		try {
			// Obtain the transaction demarcation interface.
			UserTransaction ut = mdc.getUserTransaction();
			// start a transaction
			ut.begin();
			// send a message to MDB_QUEUE_REPLY.
			JmsUtil.sendTestResults("xaTest5", true, qSession, queueR);
			// commit the transaction
			ut.commit();

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	private void runTest6(Message msg, QueueSession session) {
		try {
			// Obtain the transaction demarcation interface.
			UserTransaction ut = mdc.getUserTransaction();
			// start a transaction
			ut.begin();
			// send a message to MDB_QUEUE_REPLY.
			JmsUtil.sendTestResults("xaTest6", true, session, queueR);
			// commit the transaction
			ut.commit();

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 *
	 */
	private void runTest7(Message msg, QueueSession qSession) {
		try {
			logger.log(Logger.Level.TRACE, "JmsRedelivered flag = " + isJmsRedelivered(msg));
			if (!isJmsRedelivered(msg)) {
				logger.log(Logger.Level.TRACE, "Test7 message: first time thru");
				logger.log(Logger.Level.TRACE, "Message Number: " + msg.getIntProperty("TestCaseNum"));
				mdc.setRollbackOnly();
			} else {
				logger.log(Logger.Level.TRACE, "Test7 message: again - Pass!");
				// the rollback requeued the message - so this test passed!
				JmsUtil.sendTestResults("xaTest7", true, qSession, queueR);
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	private void runTest8(Message msg, QueueSession qSession) {
		try {
			// Obtain the transaction demarcation interface.
			UserTransaction ut = mdc.getUserTransaction();
			// start a transaction
			ut.begin();
			// send a message to MDB_QUEUE_REPLY.
			JmsUtil.sendTestResults("xaTest8", false, qSession, queueR);
			// rollback the message
			ut.rollback();
			ut.begin();
			// send a message to MDB_QUEUE_REPLY.
			JmsUtil.sendTestResults("xaTest8", true, qSession, queueR);
			// rollback the message
			ut.commit();
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	private void runTest9(Message msg, QueueSession qSession) {
		try {
			// Obtain the transaction demarcation interface.
			UserTransaction ut = mdc.getUserTransaction();
			// start a transaction
			ut.begin();
			// send a message to MDB_QUEUE_REPLY.
			JmsUtil.sendTestResults("xaTest9", false, qSession, queueR);
			// rollback the message
			ut.rollback();
			// start a transaction
			ut.begin();
			// send a message to MDB_QUEUE_REPLY.
			JmsUtil.sendTestResults("xaTest9", true, qSession, queueR);
			// commit the message
			ut.commit();
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 *
	 */
	private void runTest10(Message msg, QueueSession qSession) {
		try {
			logger.log(Logger.Level.TRACE, "Second time? = " + isJmsRedelivered(msg));
			// check redelivered flag
			if (!isJmsRedelivered(msg)) {
				logger.log(Logger.Level.TRACE, "Test10 message: first time thru");
				logger.log(Logger.Level.TRACE, "Message Number: " + msg.getIntProperty("TestCaseNum"));
				JmsUtil.sendTestResults("xaTest10", false, qSession, queueR);
				mdc.setRollbackOnly();
			} else {
				logger.log(Logger.Level.TRACE, "Test10 message: again - Pass!");
				// the rollback requeued the message - so this test passed!
				JmsUtil.sendTestResults("xaTest10", true, qSession, queueR);
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	private boolean isJmsRedelivered(Message msg) {
		boolean redelivered = false;
		try {
			redelivered = msg.getJMSRedelivered();
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
		return redelivered;
	}

	public void setMessageDrivenContext(MessageDrivenContext mdc) {
		logger.log(Logger.Level.TRACE, "setMessageDrivenContext()!!");
		this.mdc = mdc;
	}

	public void ejbRemove() {
		logger.log(Logger.Level.TRACE, "remove()!!");
	}
}
