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

package com.sun.ts.tests.jms.ee.mdb.mdb_exceptQ;

import java.lang.System.Logger;
import java.security.Principal;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsUtil;
import com.sun.ts.tests.jms.commonee.ParentMsgBeanNoTx;

import jakarta.ejb.EJBHome;
import jakarta.jms.Message;
import jakarta.jms.QueueSession;
import jakarta.transaction.UserTransaction;

public class MsgBean extends ParentMsgBeanNoTx {

	private static final Logger logger = (Logger) System.getLogger(MsgBean.class.getName());

	protected void runTests(Message msg, QueueSession qSession, String testName, java.util.Properties p) {
		try {
			// test to see if this is the first message
			if (msg.getIntProperty("TestCaseNum") > 0) {

				switch (msg.getIntProperty("TestCaseNum")) {

				case 1: // MDB Queue w/ BMT demarcation
					runGetRollbackOnlyBMT(msg, qSession, testName);
					break;

				case 2: // MDB Queue w/ BMT demarcation
					runSetRollbackOnlyBMT(msg, qSession, testName);
					break;

				case 3: // MDB Queue w/CMT - TX_NOT_SUPPORTED
					runSetRollbackOnlyCMT(msg, qSession, testName);
					break;

				case 4: // MDB Queue w/CMT - TX_NOT_SUPPORTED
					runGetRollbackOnlyCMT(msg, qSession, testName);
					break;

				case 5: // MDB Queue w/CMT - TX_NOT_SUPPORTED
				case 6: // MDB Queue w/CMT
					runGetUserTransaction(msg, qSession, testName);
					break;

				case 7: // MDB Queue w/CMT
				case 8: // MDB Queue w/CMT - TX_NOT_SUPPORTED
					runGetCallerPrincipal(msg, qSession, testName);
					break;

				case 11: // MDB Queue w/CMT
				case 12: // MDB Queue w/CMT - TX_NOT_SUPPORTED
					runGetEJBHome(msg, qSession, testName);
					break;

				case 15: // MDB Queue w/ BMT demarcation
					runBeginAgain(msg, qSession, testName);
					break;

				default:
					logger.log(Logger.Level.ERROR, "Error in mdb - ");
					logger.log(Logger.Level.ERROR,
							"No test match for TestCaseNum: " + msg.getIntProperty("TestCaseNum"));
					break;
				}
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}// runTests

	// MDB_QUEUE_BMT getRollbackOnly
	public void runGetRollbackOnlyBMT(Message msg, QueueSession qSession, String testName) {

		result = false;

		try {

			// get beanManagedTx
			UserTransaction ut = mdc.getUserTransaction();
			ut.begin();

			// this should throw an exception
			try {
				mdc.getRollbackOnly();

				logger.log(Logger.Level.ERROR, "BMT MDB getRollbackOnly() Test Failed!");
			} catch (java.lang.IllegalStateException e) {
				result = true;
				logger.log(Logger.Level.TRACE, "BMT MDB getRollbackOnly() Test Succeeded!");
				logger.log(Logger.Level.TRACE, "Got expected IllegalStateException");
			}

			ut.rollback();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception: ", e);
		}
		// send a message to MDB_QUEUE_REPLY.
		JmsUtil.sendTestResults(testName, result, qSession, queueR);
	}

	// MDB_QUEUE_BMT setRollbackOnly
	public void runSetRollbackOnlyBMT(Message msg, QueueSession qSession, String testName) {
		result = false;
		try {

			// get beanManagedTx
			UserTransaction ut = mdc.getUserTransaction();

			ut.begin();

			// this should throw an exception
			try {
				mdc.setRollbackOnly();

				logger.log(Logger.Level.ERROR, "BMT MDB setRollbackOnly() Test Failed!");
				// send a message to MDB_QUEUE_REPLY.
			} catch (java.lang.IllegalStateException e) {
				logger.log(Logger.Level.TRACE, "BMT MDB setRollbackOnly() Test Succeeded!");
				logger.log(Logger.Level.TRACE, "Got expected IllegalStateException");
				result = true;
			}
			ut.rollback();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception: ", e);
		}
		JmsUtil.sendTestResults(testName, result, qSession, queueR);
	}

	// MDB_NSTX_CMT setRollbackOnly
	public void runSetRollbackOnlyCMT(Message msg, QueueSession qSession, String testName) {

		result = false;

		// this should throw an exception
		try {
			mdc.setRollbackOnly();

			logger.log(Logger.Level.ERROR, "CMT MDB setRollbackOnly() Test Failed!");
		} catch (java.lang.IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "CMT MDB setRollbackOnly() Test Succeeded!");
			logger.log(Logger.Level.TRACE, "Got expected IllegalStateException");
			result = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception: ", e);
		}
		JmsUtil.sendTestResults(testName, result, qSession, queueR);
	}

	public void runGetRollbackOnlyCMT(Message msg, QueueSession qSession, String testName) {
		result = false;

		// this should throw an exception
		try {
			mdc.getRollbackOnly();

			logger.log(Logger.Level.ERROR, "CMT MDB getRollbackOnly() Test Failed!");
		} catch (java.lang.IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "CMT MDB getRollbackOnly() Test Succeeded!");
			logger.log(Logger.Level.TRACE, "Got expected IllegalStateException");
			result = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception: ", e);
		}
		JmsUtil.sendTestResults(testName, result, qSession, queueR);
	}

	// MDB_NSTX_CMT getUserTransaction
	public void runGetUserTransaction(Message msg, QueueSession qSession, String testName) {
		result = false;

		// this should throw an exception
		try {
			UserTransaction ut = mdc.getUserTransaction();

			logger.log(Logger.Level.ERROR, "CMT MDB getUserTransaction() Test Failed!");
		} catch (java.lang.IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "CMT MDB getUserTransaction() Test Succeeded!");
			logger.log(Logger.Level.TRACE, "Got expected IllegalStateException");
			result = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception: ", e);
		}
		// send a message to MDB_QUEUE_REPLY.
		JmsUtil.sendTestResults(testName, result, qSession, queueR);
	}

	public void runGetCallerPrincipal(Message msg, QueueSession qSession, String testName) {
		result = false;

		try {
			Principal cp = mdc.getCallerPrincipal();
			logger.log(Logger.Level.TRACE, "CMT MDB getCallerPrincipal() Test Succeeded!");
			result = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "CMT MDB getCallerPrincipal() Test Failed!", e);
		}
		// send a message to MDB_QUEUE_REPLY.
		JmsUtil.sendTestResults(testName, result, qSession, queueR);
	}

	public void runGetEJBHome(Message msg, QueueSession qSession, String testName) {
		result = false;

		// this should throw an exception
		try {
			EJBHome home = mdc.getEJBHome();

			logger.log(Logger.Level.ERROR, "CMT MDB getEJBHome() Test Failed!");
		} catch (java.lang.IllegalStateException e) {
			logger.log(Logger.Level.TRACE, "CMT MDB getEJBHome() Test Succeeded!");
			logger.log(Logger.Level.TRACE, "Got expected IllegalStateException");
			result = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception: ", e);
		}
		// send a message to MDB_QUEUE_REPLY.
		JmsUtil.sendTestResults(testName, result, qSession, queueR);
	}

	public void runBeginAgain(Message msg, QueueSession qSession, String testName) {
		try {

			// get beanManagedTx
			UserTransaction ut = mdc.getUserTransaction();

			ut.begin();

			// this should throw an exception
			try {
				ut.begin();
				logger.log(Logger.Level.ERROR, "BMT MDB getBeginAgain() Test Failed!");
			} catch (jakarta.transaction.NotSupportedException e) {
				logger.log(Logger.Level.TRACE, "BMT MDB getBeginAgain() Test Succeeded!");
				logger.log(Logger.Level.TRACE, "Got expected NotSupportedException");
				result = true;
			}
			ut.rollback();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected exception: ", e);
		}
		// send a message to MDB_QUEUE_REPLY.
		JmsUtil.sendTestResults(testName, result, qSession, queueR);
	}

}
