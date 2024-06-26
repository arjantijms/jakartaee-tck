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

package com.sun.ts.tests.saaj.api.jakarta_xml_soap.AttachmentPart;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.System.Logger;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.transform.stream.StreamSource;

import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.saaj.common.BASE64DecoderStream;
import com.sun.ts.tests.saaj.common.BASE64EncoderStream;
import com.sun.ts.tests.saaj.common.SOAP_Util;

import jakarta.activation.DataHandler;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.soap.AttachmentPart;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeader;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;

public class AttachmentPartTestServlet extends HttpServlet {
	private static final Logger logger = (Logger) System.getLogger(AttachmentPartTestServlet.class.getName());

	private MessageFactory mf = null;

	private SOAPMessage msg = null;

	private AttachmentPart ap = null;

	private static final String XML_RESOURCE_FILE = "/attach.xml";

	private final static String XML_RESOURCE_FILE2 = "/attach2.xml";

	private static final String TXT_RESOURCE_FILE = "/attach.txt";

	private static final String GIF_RESOURCE_FILE = "/attach.gif";

	private static final String JPEG_RESOURCE_FILE = "/attach.jpeg";

	private static final String HTML_RESOURCE_FILE = "/attach.html";

	private static final String NOEXISTS_RESOURCE_FILE = "/attach.noexists";

	private final static String XML_FILE = "attach.xml";

	private final static String HTML_FILE = "attach.html";

	private final static String GIF_FILE = "attach.gif";

	private final static String NULL_FILE = "attach.null";

	private final static String JPEG_FILE = "attach.jpeg";

	private final static String NOEXISTS_FILE = "attach.noexist";

	private InputStream in1 = null;

	private InputStream in2 = null;

	private InputStream in3 = null;

	private InputStream in4 = null;

	private DataHandler dh = null;

	private String cntxroot = "/AttachmentPart_web";

	private TSURL tsurl = new TSURL();

	private URL url = null;

	private URL url2 = null;

	private String host = null;

	private int port = 0;

	private ServletContext servletContext = null;

	private MimeHeader mh = null;

	private void setup() throws Exception {
		logger.log(Logger.Level.TRACE, "setup");

		SOAP_Util.setup();

		// Get host and port info
		host = SOAP_Util.getHostname();
		port = SOAP_Util.getPortnum();

		// Create a message from the message factory.
		logger.log(Logger.Level.TRACE, "Create message from message factory");
		msg = SOAP_Util.getMessageFactory().createMessage();

		// Create attachment part
		ap = msg.createAttachmentPart();

		// Remove any MimeHeaders if any
		ap.removeAllMimeHeaders();

		logger.log(Logger.Level.TRACE, "Getting resource as stream " + XML_RESOURCE_FILE);
		in1 = (InputStream) servletContext.getResourceAsStream(XML_RESOURCE_FILE);

		TestUtil.logTrace("Getting second resource as stream " + XML_RESOURCE_FILE2);
		in2 = (InputStream) servletContext.getResourceAsStream(XML_RESOURCE_FILE2);

		// Want to set an attachment from the following url.
		url = tsurl.getURL("http", SOAP_Util.getHostname(), SOAP_Util.getPortnum(), cntxroot + XML_RESOURCE_FILE);

		logger.log(Logger.Level.TRACE, "URL=" + url);

		// Want to set an attachment from the following url.
		url2 = tsurl.getURL("http", SOAP_Util.getHostname(), SOAP_Util.getPortnum(), cntxroot + XML_RESOURCE_FILE2);
		logger.log(Logger.Level.TRACE, "URL2=" + url2);

	}

	private void dispatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "dispatch");
		String testname = SOAP_Util.getHarnessProps().getProperty("TESTNAME");
		if (testname.equals("addMimeHeader1Test")) {
			logger.log(Logger.Level.TRACE, "Starting addMimeHeader1Test");
			addMimeHeader1Test(req, res);
		} else if (testname.equals("addMimeHeader2Test")) {
			logger.log(Logger.Level.TRACE, "Starting addMimeHeader2Test");
			addMimeHeader2Test(req, res);
		} else if (testname.equals("addMimeHeader3Test")) {
			logger.log(Logger.Level.TRACE, "Starting addMimeHeader3Test");
			addMimeHeader3Test(req, res);
		} else if (testname.equals("addMimeHeader4Test")) {
			logger.log(Logger.Level.TRACE, "Starting addMimeHeader4Test");
			addMimeHeader4Test(req, res);
		} else if (testname.equals("addMimeHeader5Test")) {
			logger.log(Logger.Level.TRACE, "Starting addMimeHeader5Test");
			addMimeHeader5Test(req, res);
		} else if (testname.equals("addMimeHeader6Test")) {
			logger.log(Logger.Level.TRACE, "Starting addMimeHeader6Test");
			addMimeHeader6Test(req, res);
		} else if (testname.equals("addMimeHeader7Test")) {
			logger.log(Logger.Level.TRACE, "Starting addMimeHeader7Test");
			addMimeHeader7Test(req, res);
		} else if (testname.equals("clearContent1Test")) {
			logger.log(Logger.Level.TRACE, "Starting clearContent1Test");
			clearContent1Test(req, res);
		} else if (testname.equals("clearContent2Test")) {
			logger.log(Logger.Level.TRACE, "Starting clearContent2Test");
			clearContent2Test(req, res);
		} else if (testname.equals("clearContent3Test")) {
			logger.log(Logger.Level.TRACE, "Starting clearContent3Test");
			clearContent3Test(req, res);
		} else if (testname.equals("clearContent4Test")) {
			logger.log(Logger.Level.TRACE, "Starting clearContent4Test");
			clearContent4Test(req, res);
		} else if (testname.equals("getAllMimeHeaders1Test")) {
			logger.log(Logger.Level.TRACE, "Starting getAllMimeHeaders1Test");
			getAllMimeHeaders1Test(req, res);
		} else if (testname.equals("getAllMimeHeaders2Test")) {
			logger.log(Logger.Level.TRACE, "Starting getAllMimeHeaders2Test");
			getAllMimeHeaders2Test(req, res);
		} else if (testname.equals("getAllMimeHeaders3Test")) {
			logger.log(Logger.Level.TRACE, "Starting getAllMimeHeaders3Test");
			getAllMimeHeaders3Test(req, res);
		} else if (testname.equals("getAllMimeHeaders4Test")) {
			logger.log(Logger.Level.TRACE, "Starting getAllMimeHeaders4Test");
			getAllMimeHeaders4Test(req, res);
		} else if (testname.equals("getContentId1Test")) {
			logger.log(Logger.Level.TRACE, "Starting getContentId1Test");
			getContentId1Test(req, res);
		} else if (testname.equals("getContentId2Test")) {
			logger.log(Logger.Level.TRACE, "Starting getContentId2Test");
			getContentId2Test(req, res);
		} else if (testname.equals("getContentLocation1Test")) {
			logger.log(Logger.Level.TRACE, "Starting getContentLocation1Test");
			getContentLocation1Test(req, res);
		} else if (testname.equals("getContentLocation2Test")) {
			logger.log(Logger.Level.TRACE, "Starting getContentLocation2Test");
			getContentLocation2Test(req, res);
		} else if (testname.equals("getContentLocation3Test")) {
			logger.log(Logger.Level.TRACE, "Starting getContentLocation3Test");
			getContentLocation3Test(req, res);
		} else if (testname.equals("getContent1Test")) {
			logger.log(Logger.Level.TRACE, "Starting getContent1Test");
			getContent1Test(req, res);
		} else if (testname.equals("getContent2Test")) {
			logger.log(Logger.Level.TRACE, "Starting getContent2Test");
			getContent2Test(req, res);
		} else if (testname.equals("getContent3Test")) {
			logger.log(Logger.Level.TRACE, "Starting getContent3Test");
			getContent3Test(req, res);
		} else if (testname.equals("getContent4Test")) {
			logger.log(Logger.Level.TRACE, "Starting getContent4Test");
			getContent4Test(req, res);
		} else if (testname.equals("getContent5Test")) {
			logger.log(Logger.Level.TRACE, "Starting getContent5Test");
			getContent5Test(req, res);
		} else if (testname.equals("getDataHandler1Test")) {
			logger.log(Logger.Level.TRACE, "Starting getDataHandler1Test");
			getDataHandler1Test(req, res);
		} else if (testname.equals("getDataHandler2Test")) {
			logger.log(Logger.Level.TRACE, "Starting getDataHandler2Test");
			getDataHandler2Test(req, res);
		} else if (testname.equals("getDataHandler3Test")) {
			logger.log(Logger.Level.TRACE, "Starting getDataHandler3Test");
			getDataHandler3Test(req, res);
		} else if (testname.equals("getMatchingMimeHeaders1Test")) {
			logger.log(Logger.Level.TRACE, "Starting getMatchingMimeHeaders1Test");
			getMatchingMimeHeaders1Test(req, res);
		} else if (testname.equals("getMatchingMimeHeaders2Test")) {
			logger.log(Logger.Level.TRACE, "Starting getMatchingMimeHeaders2Test");
			getMatchingMimeHeaders2Test(req, res);
		} else if (testname.equals("getMatchingMimeHeaders3Test")) {
			logger.log(Logger.Level.TRACE, "Starting getMatchingMimeHeaders3Test");
			getMatchingMimeHeaders3Test(req, res);
		} else if (testname.equals("getMatchingMimeHeaders4Test")) {
			logger.log(Logger.Level.TRACE, "Starting getMatchingMimeHeaders4Test");
			getMatchingMimeHeaders4Test(req, res);
		} else if (testname.equals("getMatchingMimeHeaders5Test")) {
			logger.log(Logger.Level.TRACE, "Starting getMatchingMimeHeaders5Test");
			getMatchingMimeHeaders5Test(req, res);
		} else if (testname.equals("getMimeHeader1Test")) {
			logger.log(Logger.Level.TRACE, "Starting getMimeHeader1Test");
			getMimeHeader1Test(req, res);
		} else if (testname.equals("getMimeHeader2Test")) {
			logger.log(Logger.Level.TRACE, "Starting getMimeHeader2Test");
			getMimeHeader2Test(req, res);
		} else if (testname.equals("getMimeHeader3Test")) {
			logger.log(Logger.Level.TRACE, "Starting getMimeHeader3Test");
			getMimeHeader3Test(req, res);
		} else if (testname.equals("getMimeHeader4Test")) {
			logger.log(Logger.Level.TRACE, "Starting getMimeHeader4Test");
			getMimeHeader4Test(req, res);
		} else if (testname.equals("getNonMatchingMimeHeaders1Test")) {
			logger.log(Logger.Level.TRACE, "Starting getNonMatchingMimeHeaders1Test");
			getNonMatchingMimeHeaders1Test(req, res);
		} else if (testname.equals("getNonMatchingMimeHeaders2Test")) {
			logger.log(Logger.Level.TRACE, "Starting getNonMatchingMimeHeaders2Test");
			getNonMatchingMimeHeaders2Test(req, res);
		} else if (testname.equals("getNonMatchingMimeHeaders3Test")) {
			logger.log(Logger.Level.TRACE, "Starting getNonMatchingMimeHeaders3Test");
			getNonMatchingMimeHeaders3Test(req, res);
		} else if (testname.equals("getNonMatchingMimeHeaders4Test")) {
			logger.log(Logger.Level.TRACE, "Starting getNonMatchingMimeHeaders4Test");
			getNonMatchingMimeHeaders4Test(req, res);
		} else if (testname.equals("getNonMatchingMimeHeaders5Test")) {
			logger.log(Logger.Level.TRACE, "Starting getNonMatchingMimeHeaders5Test");
			getNonMatchingMimeHeaders5Test(req, res);
		} else if (testname.equals("getSize1Test")) {
			logger.log(Logger.Level.TRACE, "Starting getSize1Test");
			getSize1Test(req, res);
		} else if (testname.equals("getSize2Test")) {
			logger.log(Logger.Level.TRACE, "Starting getSize2Test");
			getSize2Test(req, res);
		} else if (testname.equals("getSize3Test")) {
			logger.log(Logger.Level.TRACE, "Starting getSize3Test");
			getSize3Test(req, res);
		} else if (testname.equals("getSize4Test")) {
			logger.log(Logger.Level.TRACE, "Starting getSize4Test");
			getSize4Test(req, res);
		} else if (testname.equals("removeAllMimeHeaders1Test")) {
			logger.log(Logger.Level.TRACE, "Starting removeAllMimeHeaders1Test");
			removeAllMimeHeaders1Test(req, res);
		} else if (testname.equals("removeAllMimeHeaders2Test")) {
			logger.log(Logger.Level.TRACE, "Starting removeAllMimeHeaders2Test");
			removeAllMimeHeaders2Test(req, res);
		} else if (testname.equals("removeAllMimeHeaders3Test")) {
			logger.log(Logger.Level.TRACE, "Starting removeAllMimeHeaders3Test");
			removeAllMimeHeaders3Test(req, res);
		} else if (testname.equals("removeAllMimeHeaders4Test")) {
			logger.log(Logger.Level.TRACE, "Starting removeAllMimeHeaders4Test");
			removeAllMimeHeaders4Test(req, res);
		} else if (testname.equals("removeMimeHeader1Test")) {
			logger.log(Logger.Level.TRACE, "Starting removeMimeHeader1Test");
			removeMimeHeader1Test(req, res);
		} else if (testname.equals("removeMimeHeader2Test")) {
			logger.log(Logger.Level.TRACE, "Starting removeMimeHeader2Test");
			removeMimeHeader2Test(req, res);
		} else if (testname.equals("removeMimeHeader3Test")) {
			logger.log(Logger.Level.TRACE, "Starting removeMimeHeader3Test");
			removeMimeHeader3Test(req, res);
		} else if (testname.equals("removeMimeHeader4Test")) {
			logger.log(Logger.Level.TRACE, "Starting removeMimeHeader4Test");
			removeMimeHeader4Test(req, res);
		} else if (testname.equals("setContentId1Test")) {
			logger.log(Logger.Level.TRACE, "Starting setContentId1Test");
			setContentId1Test(req, res);
		} else if (testname.equals("setContentId2Test")) {
			logger.log(Logger.Level.TRACE, "Starting setContentId2Test");
			setContentId2Test(req, res);
		} else if (testname.equals("setContentId3Test")) {
			logger.log(Logger.Level.TRACE, "Starting setContentId3Test");
			setContentId3Test(req, res);
		} else if (testname.equals("setContentId4Test")) {
			logger.log(Logger.Level.TRACE, "Starting setContentId4Test");
			setContentId4Test(req, res);
		} else if (testname.equals("setContentLocation1Test")) {
			logger.log(Logger.Level.TRACE, "Starting setContentLocation1Test");
			setContentLocation1Test(req, res);
		} else if (testname.equals("setContentLocation2Test")) {
			logger.log(Logger.Level.TRACE, "Starting setContentLocation2Test");
			setContentLocation2Test(req, res);
		} else if (testname.equals("setContentLocation3Test")) {
			logger.log(Logger.Level.TRACE, "Starting setContentLocation3Test");
			setContentLocation3Test(req, res);
		} else if (testname.equals("setContentLocation4Test")) {
			logger.log(Logger.Level.TRACE, "Starting setContentLocation4Test");
			setContentLocation4Test(req, res);
		} else if (testname.equals("setContentLocation5Test")) {
			logger.log(Logger.Level.TRACE, "Starting setContentLocation5Test");
			setContentLocation5Test(req, res);
		} else if (testname.equals("setContent1Test")) {
			logger.log(Logger.Level.TRACE, "Starting setContent1Test");
			setContent1Test(req, res);
		} else if (testname.equals("setContent2Test")) {
			logger.log(Logger.Level.TRACE, "Starting setContent2Test");
			setContent2Test(req, res);
		} else if (testname.equals("setContent3Test")) {
			logger.log(Logger.Level.TRACE, "Starting setContent3Test");
			setContent3Test(req, res);
		} else if (testname.equals("setDataHandler1Test")) {
			logger.log(Logger.Level.TRACE, "Starting setDataHandler1Test");
			setDataHandler1Test(req, res);
		} else if (testname.equals("setDataHandler2Test")) {
			logger.log(Logger.Level.TRACE, "Starting setDataHandler2Test");
			setDataHandler2Test(req, res);
		} else if (testname.equals("setDataHandler3Test")) {
			logger.log(Logger.Level.TRACE, "Starting setDataHandler3Test");
			setDataHandler3Test(req, res);
		} else if (testname.equals("setMimeHeader1Test")) {
			logger.log(Logger.Level.TRACE, "Starting setMimeHeader1Test");
			setMimeHeader1Test(req, res);
		} else if (testname.equals("setMimeHeader2Test")) {
			logger.log(Logger.Level.TRACE, "Starting setMimeHeader2Test");
			setMimeHeader2Test(req, res);
		} else if (testname.equals("setMimeHeader3Test")) {
			logger.log(Logger.Level.TRACE, "Starting setMimeHeader3Test");
			setMimeHeader3Test(req, res);
		} else if (testname.equals("setMimeHeader4Test")) {
			logger.log(Logger.Level.TRACE, "Starting setMimeHeader4Test");
			setMimeHeader4Test(req, res);
		} else if (testname.equals("setMimeHeader5Test")) {
			logger.log(Logger.Level.TRACE, "Starting setMimeHeader5Test");
			setMimeHeader5Test(req, res);
		} else if (testname.equals("SetGetBase64ContentTest1")) {
			logger.log(Logger.Level.TRACE, "Starting SetGetBase64ContentTest1");
			SetGetBase64ContentTest1(req, res);
		} else if (testname.equals("SetGetBase64ContentTest2")) {
			logger.log(Logger.Level.TRACE, "Starting SetGetBase64ContentTest2");
			SetGetBase64ContentTest2(req, res);
		} else if (testname.equals("SetGetBase64ContentTest3")) {
			logger.log(Logger.Level.TRACE, "Starting SetGetBase64ContentTest3");
			SetGetBase64ContentTest3(req, res);
		} else if (testname.equals("setBase64ContentIOExceptionTest1")) {
			logger.log(Logger.Level.TRACE, "Starting setBase64ContentIOExceptionTest1");
			setBase64ContentIOExceptionTest1(req, res);
		} else if (testname.equals("setBase64ContentNullPointerExceptionTest1")) {
			logger.log(Logger.Level.TRACE, "Starting setBase64ContentNullPointerExceptionTest1");
			setBase64ContentNullPointerExceptionTest1(req, res);
		} else if (testname.equals("setBase64ContentSOAPExceptionTest1")) {
			logger.log(Logger.Level.TRACE, "Starting setBase64ContentSOAPExceptionTest1");
			setBase64ContentSOAPExceptionTest1(req, res);
		} else if (testname.equals("getBase64ContentSOAPExceptionTest1")) {
			logger.log(Logger.Level.TRACE, "Starting getBase64ContentSOAPExceptionTest1");
			getBase64ContentSOAPExceptionTest1(req, res);
		} else if (testname.equals("SetGetRawContentTest1")) {
			logger.log(Logger.Level.TRACE, "Starting SetGetRawContentTest1");
			SetGetRawContentTest1(req, res);
		} else if (testname.equals("SetGetRawContentTest2")) {
			logger.log(Logger.Level.TRACE, "Starting SetGetRawContentTest2");
			SetGetRawContentTest2(req, res);
		} else if (testname.equals("SetGetRawContentTest3")) {
			logger.log(Logger.Level.TRACE, "Starting SetGetRawContentTest3");
			SetGetRawContentTest3(req, res);
		} else if (testname.equals("SetGetRawContentTest4")) {
			logger.log(Logger.Level.TRACE, "Starting SetGetRawContentTest4");
			SetGetRawContentTest4(req, res);
		} else if (testname.equals("SetGetRawContentBytesTest1")) {
			logger.log(Logger.Level.TRACE, "Starting SetGetRawContentBytesTest1");
			SetGetRawContentBytesTest1(req, res);
		} else if (testname.equals("SetRawContentBytesSOAPOrNullPointerExceptionTest1")) {
			logger.log(Logger.Level.TRACE, "Starting SetRawContentBytesSOAPOrNullPointerExceptionTest1");
			SetRawContentBytesSOAPOrNullPointerExceptionTest1(req, res);
		} else if (testname.equals("GetRawContentBytesSOAPOrNullPointerExceptionTest1")) {
			logger.log(Logger.Level.TRACE, "Starting GetRawContentBytesSOAPOrNullPointerExceptionTest1");
			GetRawContentBytesSOAPOrNullPointerExceptionTest1(req, res);
		} else if (testname.equals("setRawContentIOExceptionTest1")) {
			logger.log(Logger.Level.TRACE, "Starting setRawContentIOExceptionTest1");
			setRawContentIOExceptionTest1(req, res);
		} else if (testname.equals("setRawContentNullPointerExceptionTest1")) {
			logger.log(Logger.Level.TRACE, "Starting setRawContentNullPointerExceptionTest1");
			setRawContentNullPointerExceptionTest1(req, res);
		} else if (testname.equals("getRawContentSOAPExceptionTest1")) {
			logger.log(Logger.Level.TRACE, "Starting getRawContentSOAPExceptionTest1");
			getRawContentSOAPExceptionTest1(req, res);
		} else {
			throw new ServletException("The testname '" + testname + "' was not found in the test servlet");
		}
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		logger.log(Logger.Level.TRACE,"AddHeaderTestServlet:init (Entering)");
		SOAP_Util.doServletInit(servletConfig);
		servletContext = servletConfig.getServletContext();
		logger.log(Logger.Level.TRACE,"AddHeaderTestServlet:init (Leaving)");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "doGet");
		dispatch(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "doPost");
		SOAP_Util.doServletPost(req, res);
		doGet(req, res);
	}

	private void addMimeHeader1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addMimeHeader1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			ap.addMimeHeader("Content-Description", "some text");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void addMimeHeader2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addMimeHeader2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Id", "id@abc.com");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void addMimeHeader3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addMimeHeader3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Description", "some text2");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void addMimeHeader4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addMimeHeader4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader with invalid arguments");
			ap.addMimeHeader("", "");

			logger.log(Logger.Level.ERROR, "Error: expected java.lang.IllegalArgumentException to be thrown");
			pass = false;
		} catch (java.lang.IllegalArgumentException ia) {
			// test passed
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void addMimeHeader5Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addMimeHeader5Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader with invalid arguments");
			ap.addMimeHeader("", "some text");

			logger.log(Logger.Level.ERROR, "Error: expected java.lang.IllegalArgumentException to be thrown");
			pass = false;
		} catch (java.lang.IllegalArgumentException ia) {
			// test passed
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void addMimeHeader6Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addMimeHeader6Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader with null value");
			ap.addMimeHeader("Content-Description", null);

			logger.log(Logger.Level.INFO, "Adding MimeHeader with null string value");
			ap.addMimeHeader("Content-Description", "");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader with null value");
			ap.addMimeHeader("Content-Description", null);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void addMimeHeader7Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "addMimeHeader7Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader invalid arguments");
			ap.addMimeHeader(null, null);

			logger.log(Logger.Level.ERROR, "Error: expected java.lang.IllegalArgumentException to be thrown");
			pass = false;
		} catch (java.lang.IllegalArgumentException ia) {
			// test passed
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void clearContent1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "clearContent1Test");
		Properties resultProps = new Properties();
		boolean pass = false;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content ");
			ap.setContent(new StreamSource(in1), "text/xml");

			logger.log(Logger.Level.INFO, "Clearing Content ");
			ap.clearContent();

			try {

				logger.log(Logger.Level.INFO, "Getting Content ");
				InputStream is = (InputStream) ap.getContent();

				logger.log(Logger.Level.ERROR, "Error: SOAPException should have been thrown");
				pass = false;
			} catch (SOAPException e) {
				pass = true;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void clearContent2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "clearContent2Test");
		Properties resultProps = new Properties();
		boolean pass = false;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content ");
			ap.setContent(new StreamSource(in1), "text/xml");

			logger.log(Logger.Level.INFO, "Setting Content a second time");
			ap.setContent(new StreamSource(in2), "text/xml");

			logger.log(Logger.Level.INFO, "Clearing Content ");
			ap.clearContent();

			try {

				logger.log(Logger.Level.INFO, "Getting Content ");
				InputStream is = (InputStream) ap.getContent();

				logger.log(Logger.Level.ERROR, "Error: SOAPException should have been thrown");
				pass = false;
			} catch (SOAPException e) {
				pass = true;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void clearContent3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "clearContent3Test");
		Properties resultProps = new Properties();
		boolean pass = false;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Mime Header ");
			ap.setMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Setting Content Id Header ");
			ap.setContentId("id@abc.com");

			logger.log(Logger.Level.INFO, "Setting Content ");
			ap.setContent(new StreamSource(in1), "text/xml");

			logger.log(Logger.Level.INFO, "Clearing Content ");
			ap.clearContent();

			try {

				logger.log(Logger.Level.INFO, "Getting Content ");
				InputStream is = (InputStream) ap.getContent();

				logger.log(Logger.Level.ERROR, "Error: SOAPException should have been thrown");
				pass = false;
			} catch (SOAPException e) {
				pass = true;
			}

			Iterator iterator = ap.getAllMimeHeaders();
			int cnt = 0;
			boolean foundHeader1 = false;
			boolean foundHeader2 = false;
			boolean foundDefaultHeader = false;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equalsIgnoreCase("Content-Description") && value.equals("some text")) {
					if (!foundHeader1) {
						foundHeader1 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header1");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the same header1 header twice");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equalsIgnoreCase("Content-ID") && value.equals("id@abc.com")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the same header2 header twice");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equalsIgnoreCase("Content-Type") && value.equals("text/xml")) {
					if (!foundDefaultHeader) {
						foundDefaultHeader = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for default header");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						TestUtil.logErr("Error: Received the same default header header twice");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an invalid header");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive both headers");
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void clearContent4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "clearContent4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Clearing Content ");
			ap.clearContent();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getAllMimeHeaders1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getAllMimeHeaders1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			ap.addMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			Iterator iterator = ap.getAllMimeHeaders();
			boolean foundHeader1 = false;
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if ((name.equalsIgnoreCase("Content-Description") && value.equals("some text"))) {
					if (!foundHeader1) {
						foundHeader1 = true;
						logger.log(Logger.Level.INFO, "MimeHeader did match");
						logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the header1 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "MimeHeader did not match");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!foundHeader1) {
				logger.log(Logger.Level.ERROR, "Error: did not receive expected MimeHeader");
				pass = false;
			}
			if (cnt != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected one item to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getAllMimeHeaders2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getAllMimeHeaders2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Id", "id@abc.com");

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			Iterator iterator = ap.getAllMimeHeaders();
			int cnt = 0;
			boolean foundHeader1 = false;
			boolean foundHeader2 = false;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equalsIgnoreCase("Content-Description") && value.equals("some text")) {
					if (!foundHeader1) {
						foundHeader1 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header1");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the header1 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equalsIgnoreCase("Content-Id") && value.equals("id@abc.com")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the header2 MimeHeader again (unxpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive all MimeHeaders");
				pass = false;
			}

			if (cnt != 2) {
				logger.log(Logger.Level.ERROR, "Error: expected 2 items to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getAllMimeHeaders3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getAllMimeHeaders3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			Iterator iterator = ap.getAllMimeHeaders();
			int cnt = 0;
			boolean foundHeader1 = false;
			boolean foundHeader2 = false;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equalsIgnoreCase("Content-Description") && value.equals("some text")) {
					if (!foundHeader1) {
						foundHeader1 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header1");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the header1 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equalsIgnoreCase("Content-Description") && value.equals("some text2")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header2 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive all MimeHeaders");
				pass = false;
			}
			if (cnt != 2) {
				logger.log(Logger.Level.ERROR, "Error: expected 2 items to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getAllMimeHeaders4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getAllMimeHeaders4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.TRACE, "Getting all MimeHeaders");
			Iterator iterator = ap.getAllMimeHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received unexpected MimeHeader");
				logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContentId1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContentId1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Id");
			ap.setContentId("id@abc.com");

			logger.log(Logger.Level.INFO, "Getting Content Id");
			String result = ap.getContentId();

			if (!result.equals("id@abc.com")) {
				logger.log(Logger.Level.ERROR, "Error: received invalid value from getContentId()");
				logger.log(Logger.Level.ERROR, "expected result: id@abc.com");
				logger.log(Logger.Level.ERROR, "actual result:" + result);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContentId2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContentId2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Id");
			ap.setContentId("id@abc.com");

			logger.log(Logger.Level.INFO, "Setting Content Id again");
			ap.setContentId("id2@abc2.com");

			logger.log(Logger.Level.INFO, "Getting Content Id");
			String result = ap.getContentId();

			if (!result.equals("id2@abc2.com")) {
				logger.log(Logger.Level.ERROR, "Error: received invalid value from getContentId()");
				logger.log(Logger.Level.ERROR, "expected result: id2@abc2.com");
				logger.log(Logger.Level.ERROR, "actual result:" + result);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContentLocation1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContentLocation1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Location");
			ap.setContentLocation("http://localhost/someapp");

			logger.log(Logger.Level.INFO, "Getting Content Location");
			String result = ap.getContentLocation();

			if (!result.equals("http://localhost/someapp")) {
				logger.log(Logger.Level.ERROR, "Error: received invalid Location value from getContentLocation()");
				logger.log(Logger.Level.ERROR, "expected result: http://localhost/someapp");
				logger.log(Logger.Level.ERROR, "actual result:" + result);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContentLocation2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContentLocation2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Location");
			ap.setContentLocation("/WEB-INF/somefile");

			logger.log(Logger.Level.INFO, "Getting Content Location");
			String result = ap.getContentLocation();

			if (!result.equals("/WEB-INF/somefile")) {
				logger.log(Logger.Level.ERROR, "Error: received invalid Location value from getContentLocation()");
				logger.log(Logger.Level.ERROR, "expected result: /WEB-INF/somefile");
				logger.log(Logger.Level.ERROR, "actual result:" + result);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContentLocation3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContentLocation3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Location");
			ap.setContentLocation("http://localhost/someapp");

			logger.log(Logger.Level.INFO, "Setting Content Location again");
			ap.setContentLocation("/WEB-INF/somefile");

			logger.log(Logger.Level.INFO, "Getting Content Location");
			String result = ap.getContentLocation();

			if (!result.equals("/WEB-INF/somefile")) {
				logger.log(Logger.Level.ERROR, "Error: received invalid Location value from getContentLocation()");
				logger.log(Logger.Level.ERROR, "expected result: /WEB-INF/somefile");
				logger.log(Logger.Level.ERROR, "actual result:" + result);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContent1Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContent1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			url = tsurl.getURL("http", host, port, cntxroot + "/" + XML_FILE);
			logger.log(Logger.Level.INFO, "Get DataHandler to xml attachment: " + XML_FILE);
			logger.log(Logger.Level.INFO, "URL = " + url);
			dh = new DataHandler(url);

			logger.log(Logger.Level.INFO, "Setting Content for text/xml mime type using StreamSource");
			ap.setContent(new StreamSource(dh.getInputStream()), "text/xml");

			logger.log(Logger.Level.INFO, "Getting Content should return StreamSource object");
			Object o = ap.getContent();
			logger.log(Logger.Level.INFO, "object returned=" + o);
			if (o != null) {
				if (o instanceof StreamSource)
					logger.log(Logger.Level.INFO, "StreamSource object was returned (ok)");
				else {
					logger.log(Logger.Level.ERROR, "Unexpected object was returned (not ok)");
					logger.log(Logger.Level.ERROR, "Unexpected object=" + o);
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR, "null was returned");
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContent2Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContent2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			url = tsurl.getURL("http", host, port, cntxroot + "/" + HTML_FILE);
			logger.log(Logger.Level.INFO, "Get DataHandler to html attachment: " + HTML_FILE);
			logger.log(Logger.Level.INFO, "URL = " + url);
			DataHandler dh = new DataHandler(url);

			logger.log(Logger.Level.INFO, "Setting Content for text/html mime type using InputStream");
			ap.setContent(dh.getInputStream(), "text/html");

			logger.log(Logger.Level.INFO, "Getting Content should return String or InputStream object");
			Object o = ap.getContent();
			logger.log(Logger.Level.INFO, "object returned=" + o);
			if (o != null) {
				if (o instanceof String)
					logger.log(Logger.Level.INFO, "String object was returned (ok)");
				else if (o instanceof InputStream)
					logger.log(Logger.Level.INFO, "InputStream object was returned (ok)");
				else {
					logger.log(Logger.Level.ERROR, "Unexpected object was returned (not ok)");
					logger.log(Logger.Level.ERROR, "Unexpected object=" + o);
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR, "null was returned");
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContent3Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContent3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			url = tsurl.getURL("http", host, port, cntxroot + "/" + GIF_FILE);
			/******
			 * Comment out (using DataHandler for gif requires DISPLAY environment variable
			 * to be set because GifDataContentHandler appserver code calls
			 * Toolkit.getDefaultToolkit() logger.log(Logger.Level.INFO,"Get DataHandler to
			 * gif attachment: " + GIF_FILE); logger.log(Logger.Level.INFO,"URL = " + url);
			 * dh = new DataHandler(url); logger.log(Logger.Level.INFO,"DataHandler = " +
			 * dh);
			 * 
			 * logger.log(Logger.Level.INFO,"Create image/gif mime attachment using
			 * DataHandler"); ap = msg.createAttachmentPart(dh);
			 * logger.log(Logger.Level.INFO,"Set the content type to image/gif");
			 * ap.setContentType("image/gif");
			 *****/
			logger.log(Logger.Level.INFO, "URL =" + url);
						
			logger.log(Logger.Level.INFO, "Create image/gif mime attachment using ImageIO");
			Image image = javax.imageio.ImageIO.read(url);
			
			ap = msg.createAttachmentPart(image, "image/gif");

			logger.log(Logger.Level.INFO, "Getting Content should return an Image object");
			Object o = ap.getContent();
			logger.log(Logger.Level.INFO, "object returned=" + o);
			if (o != null) {
				if (o instanceof Image)
					logger.log(Logger.Level.INFO, "Image object was returned (ok)");
				else {
					logger.log(Logger.Level.ERROR, "Unexpected object was returned (not ok)");
					logger.log(Logger.Level.ERROR, "Unexpected object=" + o);
				}
			} else {
				logger.log(Logger.Level.ERROR, "null was returned");
				pass = false;
			}
			/*****
			 * Comment out (appserver does not allow use of Toolkit.getDefaultToolkit())
			 * ap.clearContent(); logger.log(Logger.Level.INFO, "Setting Content via Image
			 * for image/gif mime type"); logger.log(Logger.Level.INFO,"URL to GIF file=" +
			 * url); Image image = Toolkit.getDefaultToolkit().createImage(url);
			 * ap.setContent(image,"image/gif"); logger.log(Logger.Level.INFO,"Getting
			 * Content should return Image object"); o = ap.getContent();
			 * logger.log(Logger.Level.INFO,"object returned="+o); if(o != null) { if(o
			 * instanceof Image) logger.log(Logger.Level.INFO,"Image object was returned
			 * (ok)"); else { logger.log(Logger.Level.ERROR,"Unexpected object was returned
			 * (not ok)"); logger.log(Logger.Level.ERROR,"Unexpected object="+o); } } else {
			 * logger.log(Logger.Level.ERROR,"null was returned"); pass = false; }
			 *****/
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		
		logger.log(Logger.Level.ERROR, "Test result: " + pass);

		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}
	

	private void getContent4Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContent4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			url = tsurl.getURL("http", host, port, cntxroot + "/" + JPEG_FILE);
			logger.log(Logger.Level.INFO, "Get DataHandler to jpeg attachment: " + JPEG_FILE);
			logger.log(Logger.Level.INFO, "URL = " + url);
			dh = new DataHandler(url);
			logger.log(Logger.Level.INFO, "DataHandler = " + dh);

			logger.log(Logger.Level.INFO, "Create image/jpeg mime attachment using DataHandler");
			ap = msg.createAttachmentPart(dh);
			logger.log(Logger.Level.INFO, "Set the content type to image/jpeg");
			ap.setContentType("image/jpeg");
			
			

			logger.log(Logger.Level.INFO, "Getting Content should return an Image object");
			Object o = ap.getContent();
			logger.log(Logger.Level.INFO, "object returned=" + o);
			if (o != null) {
				if (o instanceof Image)
					logger.log(Logger.Level.INFO, "Image object was returned (ok)");
				else {
					logger.log(Logger.Level.ERROR, "Unexpected object was returned (not ok)");
					logger.log(Logger.Level.ERROR, "Unexpected object=" + o);
				}
			} else {
				logger.log(Logger.Level.ERROR, "null was returned");
				pass = false;
			}
			/*****
			 * Comment out (appserver does not allow use of Toolkit.getDefaultToolkit())
			 * ap.clearContent(); logger.log(Logger.Level.INFO, "Setting Content via Image
			 * for image/jpeg mime type"); logger.log(Logger.Level.INFO,"URL to JPEG file="
			 * + url); Image image = Toolkit.getDefaultToolkit().createImage(url);
			 * ap.setContent(image,"image/jpeg"); logger.log(Logger.Level.INFO,"Getting
			 * Content should return Image object"); o = ap.getContent();
			 * logger.log(Logger.Level.INFO,"object returned="+o); if(o != null) { if(o
			 * instanceof Image) logger.log(Logger.Level.INFO,"Image object was returned
			 * (ok)"); else { logger.log(Logger.Level.ERROR,"Unexpected object was returned
			 * (not ok)"); logger.log(Logger.Level.ERROR,"Unexpected object="+o); } } else {
			 * logger.log(Logger.Level.ERROR,"null was returned"); pass = false; }
			 *****/
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getContent5Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getContent2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content for text/plain mime type using String");
			String exp = "some text";
			ap.setContent(exp, "text/plain");

			logger.log(Logger.Level.INFO, "Getting Content should return String object");
			Object o = ap.getContent();
			logger.log(Logger.Level.INFO, "object returned=" + o);
			String s = null;
			if (o != null) {
				if (o instanceof String) {
					logger.log(Logger.Level.INFO, "String object was returned (ok)");
					s = (String) o;
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected object was returned (not ok)");
					logger.log(Logger.Level.ERROR, "Unexpected object=" + o);
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR, "null was returned");
				pass = false;
			}

			if (s != null) {
				logger.log(Logger.Level.INFO, "Verifying contents");
				if (s.equals(exp)) {
					logger.log(Logger.Level.INFO, "contents are equal - expected");
				} else {
					logger.log(Logger.Level.ERROR, "contents not equal - unexpected");
					logger.log(Logger.Level.ERROR, "expected (" + exp + ")");
					logger.log(Logger.Level.ERROR, "received (" + s + ")");
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR, "s is null");
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getDataHandler1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getDataHandler1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating DataHandler");
			DataHandler dh1 = new DataHandler(url);

			logger.log(Logger.Level.INFO, "Setting DataHandler");
			ap.setDataHandler(dh1);

			logger.log(Logger.Level.INFO, "Getting DataHandler");
			DataHandler dh = ap.getDataHandler();

			logger.log(Logger.Level.INFO, "compare attachment received is same as one added");
			if (dh1.equals(dh)) {
				logger.log(Logger.Level.INFO, "Got AttachmentPart object");
			} else {
				logger.log(Logger.Level.ERROR, "AttachmentPart object mismatch");
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getDataHandler2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getDataHandler2Test");
		Properties resultProps = new Properties();
		boolean pass = false;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			try {
				logger.log(Logger.Level.INFO, "Getting DataHandler");
				ap.getDataHandler();
				logger.log(Logger.Level.ERROR, "Error: Expected a SOAPException to be returned");
				pass = false;
			} catch (SOAPException se) {
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getDataHandler3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getDataHandler3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Creating DataHandler");
			DataHandler dh1 = new DataHandler(url);

			logger.log(Logger.Level.INFO, "Creating second DataHandler");
			DataHandler dh2 = new DataHandler(url2);

			logger.log(Logger.Level.INFO, "Setting DataHandler");
			ap.setDataHandler(dh2);

			logger.log(Logger.Level.INFO, "Setting DataHandler a second time");
			ap.setDataHandler(dh1);

			logger.log(Logger.Level.INFO, "Getting DataHandler");
			DataHandler dh = ap.getDataHandler();

			logger.log(Logger.Level.INFO, "compare attachment received is same as one added");
			if (dh1.equals(dh)) {
				logger.log(Logger.Level.INFO, "Got AttachmentPart object");
			} else {
				logger.log(Logger.Level.ERROR, "AttachmentPart object mismatch");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void displayArray(String[] array) {
		int len = array.length;
		for (int i = 0; i < len; i++) {
			logger.log(Logger.Level.TRACE, "array[" + i + "]=" + array[i]);
		}
	}

	private void getMatchingMimeHeaders1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMatchingMimeHeaders1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			ap.addMimeHeader("Content-Description", "some text");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			iterator = ap.getAllMimeHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Description" };
			logger.log(Logger.Level.INFO, "List of matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting matching MimeHeaders");
			iterator = ap.getMatchingMimeHeaders(sArray);
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equalsIgnoreCase("Content-Description") && value.equals("some text")) {
					logger.log(Logger.Level.INFO, "MimeHeaders do match ");
					logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (cnt != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected one item to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getMatchingMimeHeaders2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMatchingMimeHeaders2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Id", "id@abc.com");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			iterator = ap.getAllMimeHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Description" };
			logger.log(Logger.Level.INFO, "List of matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting matching MimeHeaders");
			iterator = ap.getMatchingMimeHeaders(sArray);
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equalsIgnoreCase("Content-Description") && value.equals("some text")) {
					logger.log(Logger.Level.INFO, "MimeHeaders do match ");
					logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (cnt != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected one items to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getMatchingMimeHeaders3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMatchingMimeHeaders3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Description", "some text2");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			iterator = ap.getAllMimeHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Description" };
			logger.log(Logger.Level.INFO, "List of matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting matching MimeHeaders");
			iterator = ap.getMatchingMimeHeaders(sArray);
			cnt = 0;
			boolean foundHeader1 = false;
			boolean foundHeader2 = false;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equalsIgnoreCase("Content-Description") && value.equals("some text")) {
					if (!foundHeader1) {
						foundHeader1 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header1");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the header1 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equalsIgnoreCase("Content-Description") && value.equals("some text2")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the header2 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive both MimeHeaders");
				pass = false;
			}
			if (cnt != 2) {
				logger.log(Logger.Level.ERROR, "Error: expected two items to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getMatchingMimeHeaders4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMatchingMimeHeaders4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			ap.addMimeHeader("Content-Description", "some text");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			iterator = ap.getAllMimeHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "doesnotexist" };
			logger.log(Logger.Level.INFO, "List of matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non-existent MimeHeader");
			iterator = ap.getMatchingMimeHeaders(sArray);
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
				logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");

		resultProps.list(out);
	}

	private void getMatchingMimeHeaders5Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMatchingMimeHeaders5Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 3 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Description", "some text2");
			ap.addMimeHeader("Content-Id", "id@abc.com");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			iterator = ap.getAllMimeHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Description", "Content-Id", "Content-Location" };
			logger.log(Logger.Level.INFO, "List of matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting matching MimeHeaders");
			iterator = ap.getMatchingMimeHeaders(sArray);
			cnt = 0;
			boolean foundHeader1 = false;
			boolean foundHeader2 = false;
			boolean foundHeader3 = false;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equalsIgnoreCase("Content-Description") && value.equals("some text")) {
					if (!foundHeader1) {
						foundHeader1 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header1");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the header1 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equalsIgnoreCase("Content-Description") && value.equals("some text2")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the header2 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equalsIgnoreCase("Content-Id") && value.equals("id@abc.com")) {
					if (!foundHeader3) {
						foundHeader3 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header3");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR, "Error: Received the header3 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2 && foundHeader3)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive all MimeHeaders");
				pass = false;
			}
			if (cnt != 3) {
				logger.log(Logger.Level.ERROR, "Error: expected three items to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getMimeHeader1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMimeHeader1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			ap.addMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Getting MimeHeader");
			String sArray[] = ap.getMimeHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected only one item to be returned, got a total of:" + len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text")) {
					logger.log(Logger.Level.ERROR,
							"Error: received invalid value from getMimeHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: some text");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getMimeHeader2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMimeHeader2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Id", "id@abc.com");

			logger.log(Logger.Level.INFO, "Getting MimeHeader");
			String sArray[] = ap.getMimeHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text")) {
					logger.log(Logger.Level.ERROR,
							"Error: received invalid value from getMimeHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: some text");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}

			sArray = ap.getMimeHeader("Content-Id");
			len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getMimeHeader(Content-Id), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("id@abc.com")) {
					logger.log(Logger.Level.ERROR, "Error: received invalid value from getMimeHeader(Content-Id)");
					logger.log(Logger.Level.ERROR, "expected result: id@abc.com");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getMimeHeader3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMimeHeader3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Getting MimeHeader");
			String sArray[] = ap.getMimeHeader("Content-Description");
			int len = sArray.length;
			if (len != 2) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text") && !temp.equals("some text2")) {
					logger.log(Logger.Level.ERROR,
							"Error: received invalid value from getMimeHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: some text or some text2");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getMimeHeader4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getMimeHeader4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			ap.addMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Getting non-existent MimeHeader");
			String sArray[] = ap.getMimeHeader("doesnotexist");
			if (sArray != null && sArray.length > 0) {
				logger.log(Logger.Level.ERROR, "Error: was able to get a non-existent MimeHeader");
				pass = false;
				int len = sArray.length;
				for (int i = 0; i < len; i++) {
					logger.log(Logger.Level.ERROR, "actual result:" + sArray[i]);
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");

		resultProps.list(out);
	}

	private void getNonMatchingMimeHeaders1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNonMatchingMimeHeaders1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			ap.addMimeHeader("Content-Description", "some text");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			iterator = ap.getAllMimeHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Id" };
			logger.log(Logger.Level.INFO, "List of non matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non matching MimeHeaders");
			iterator = ap.getNonMatchingMimeHeaders(sArray);
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equalsIgnoreCase("Content-Description") && value.equals("some text")) {
					logger.log(Logger.Level.INFO, "MimeHeaders do match ");
					logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (cnt != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected one items to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getNonMatchingMimeHeaders2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNonMatchingMimeHeaders2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Id", "id@abc.com");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			iterator = ap.getAllMimeHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Id" };
			logger.log(Logger.Level.INFO, "List of non matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non matching MimeHeaders");
			iterator = ap.getNonMatchingMimeHeaders(sArray);
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equalsIgnoreCase("Content-Description") && value.equals("some text")) {
					logger.log(Logger.Level.INFO, "MimeHeaders do match ");
					logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (cnt != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected one items to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getNonMatchingMimeHeaders3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNonMatchingMimeHeaders3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Description", "some text2");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			iterator = ap.getAllMimeHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Id" };
			logger.log(Logger.Level.INFO, "List of non matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non matching MimeHeaders");
			iterator = ap.getNonMatchingMimeHeaders(sArray);
			cnt = 0;
			boolean foundHeader1 = false;
			boolean foundHeader2 = false;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equalsIgnoreCase("Content-Description") && value.equals("some text")) {
					if (!foundHeader1) {
						foundHeader1 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header1");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header1 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equalsIgnoreCase("Content-Description") && value.equals("some text2")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header2 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an unexpected header");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive both MimeHeaders");
				pass = false;
			}
			if (cnt != 2) {
				logger.log(Logger.Level.ERROR, "Error: expected two items to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getNonMatchingMimeHeaders4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNonMatchingMimeHeaders4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			ap.addMimeHeader("Content-Description", "some text");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			iterator = ap.getAllMimeHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Description" };
			logger.log(Logger.Level.INFO, "List of non matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non matching MimeHeaders");
			iterator = ap.getNonMatchingMimeHeaders(sArray);
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
				logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getNonMatchingMimeHeaders5Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getNonMatchingMimeHeaders5Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 3 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Description", "some text2");
			ap.addMimeHeader("Content-Id", "id@abc.com");

			Iterator iterator = null;
			int cnt = 0;

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders");
			iterator = ap.getAllMimeHeaders();
			cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.INFO, "received: name=" + name + ", value=" + value);
			}

			String sArray[] = { "Content-Id", "Content-Location" };
			logger.log(Logger.Level.INFO, "List of non matching MimeHeaders contains:");
			displayArray(sArray);

			logger.log(Logger.Level.INFO, "Getting non matching MimeHeaders");
			iterator = ap.getNonMatchingMimeHeaders(sArray);
			cnt = 0;
			boolean foundHeader1 = false;
			boolean foundHeader2 = false;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (name.equalsIgnoreCase("Content-Description") && value.equals("some text")) {
					if (!foundHeader1) {
						foundHeader1 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header1");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header1 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else if (name.equalsIgnoreCase("Content-Description") && value.equals("some text2")) {
					if (!foundHeader2) {
						foundHeader2 = true;
						logger.log(Logger.Level.INFO, "MimeHeaders do match for header2");
						logger.log(Logger.Level.INFO, "receive: name=" + name + ", value=" + value);
					} else {
						logger.log(Logger.Level.ERROR,
								"Error: Received the same header2 MimeHeader again (unexpected)");
						logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: Received an unexpected MimeHeader");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				}
			}

			if (!(foundHeader1 && foundHeader2)) {
				logger.log(Logger.Level.ERROR, "Error: did not receive both MimeHeaders");
				pass = false;
			}
			if (cnt != 2) {
				logger.log(Logger.Level.ERROR, "Error: expected two items to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getSize1Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getSize1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Getting Size on attachment with no content set");
			int size = ap.getSize();
			logger.log(Logger.Level.INFO, "The returned size = " + size);
			if (size != -1 && size != 0) {
				logger.log(Logger.Level.ERROR, "Error: received invalid size from getSize()");
				logger.log(Logger.Level.ERROR, "received:" + size + ", expected:0");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getSize2Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getSize2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Getting Size on attachment with no content set");
			int size1 = ap.getSize();
			logger.log(Logger.Level.INFO, "The returned size1 = " + size1);
			if (size1 != -1 && size1 != 0) {
				logger.log(Logger.Level.ERROR, "Error: received invalid size from getSize()");
				logger.log(Logger.Level.ERROR, "received:" + size1 + ", expected:0");
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Setting Mime Header for attachment via addMimeHeader()");
			ap.addMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Getting Size");
			int size2 = ap.getSize();
			logger.log(Logger.Level.INFO, "The returned size2 = " + size2);
			if (size2 != -1 && size2 != 0) {
				logger.log(Logger.Level.ERROR, "Error: received invalid size from getSize()");
				logger.log(Logger.Level.ERROR, "received:" + size2);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getSize3Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getSize3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Getting Size on attachment with no content set");
			int size1 = ap.getSize();
			logger.log(Logger.Level.INFO, "The returned size1 = " + size1);
			if (size1 != -1 && size1 != 0) {
				logger.log(Logger.Level.ERROR, "Error: received invalid size from getSize()");
				logger.log(Logger.Level.ERROR, "received:" + size1 + ", expected:0");
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Setting Content for attachment via setContent() and mimetype=text/xml");
			ap.setContent(new StreamSource(in1), "text/xml");
			logger.log(Logger.Level.INFO, "Getting Size");
			int size2 = ap.getSize();
			logger.log(Logger.Level.INFO, "The returned size2 = " + size2);
			if (size2 <= 0) {
				logger.log(Logger.Level.ERROR, "Error: received invalid size from getSize()");
				logger.log(Logger.Level.ERROR, "received:" + size2 + ", expected > 0");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getSize4Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getSize4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Getting Size on attachment with no content set");
			int size1 = ap.getSize();
			logger.log(Logger.Level.INFO, "The returned size1 = " + size1);
			if (size1 != -1 && size1 != 0) {
				logger.log(Logger.Level.ERROR, "Error: received invalid size from getSize()");
				logger.log(Logger.Level.ERROR, "received:" + size1 + ", expected:0");
				pass = false;
			}

			logger.log(Logger.Level.INFO, "Setting DataHandler for attachment using setDataHandler()");
			ap.setDataHandler(new DataHandler(url));
			logger.log(Logger.Level.INFO, "Getting Size");
			int size2 = ap.getSize();
			logger.log(Logger.Level.INFO, "The returned size2 = " + size2);
			if (size2 <= 0) {
				logger.log(Logger.Level.ERROR, "Error: received invalid size from getSize()");
				logger.log(Logger.Level.ERROR, "received:" + size2);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void removeAllMimeHeaders1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeAllMimeHeaders1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			ap.addMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders ...");
			ap.removeAllMimeHeaders();

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders ...");
			Iterator iterator = ap.getAllMimeHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received unexpected MimeHeader");
				logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void removeAllMimeHeaders2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeAllMimeHeaders2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Id", "id@abc.com");

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders ...");
			ap.removeAllMimeHeaders();

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders ...");
			Iterator iterator = ap.getAllMimeHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received unexpected MimeHeader");
				logger.log(Logger.Level.ERROR, "receive: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void removeAllMimeHeaders3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeAllMimeHeaders3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders ...");
			ap.removeAllMimeHeaders();

			logger.log(Logger.Level.INFO, "Getting all MimeHeaders ...");
			Iterator iterator = ap.getAllMimeHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received unexpected MimeHeader");
				logger.log(Logger.Level.ERROR, "receive: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void removeAllMimeHeaders4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeAllMimeHeaders4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Removing all MimeHeaders object ...");
			ap.removeAllMimeHeaders();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void removeMimeHeader1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeMimeHeader1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			ap.addMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Removing MimeHeader");
			ap.removeMimeHeader("Content-Description");

			logger.log(Logger.Level.INFO, "Getting MimeHeaders");
			Iterator iterator = ap.getAllMimeHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received unexpected MimeHeader");
				logger.log(Logger.Level.ERROR, "receive: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got atotal of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void removeMimeHeader2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeMimeHeader2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Id", "id@abc.com");

			logger.log(Logger.Level.INFO, "Removing MimeHeader");
			ap.removeMimeHeader("Content-Id");

			logger.log(Logger.Level.INFO, "Getting MimeHeaders");
			Iterator iterator = ap.getAllMimeHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				if (!(name.equalsIgnoreCase("Content-Description") && value.equals("some text"))) {
					logger.log(Logger.Level.ERROR, "MimeHeader did not match");
					logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "MimeHeader did match");
				}
			}

			if (cnt != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected only one item to be returned, got a total of:" + cnt);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void removeMimeHeader3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeMimeHeader3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Removing MimeHeader");
			ap.removeMimeHeader("Content-Description");

			logger.log(Logger.Level.INFO, "Getting MimeHeaders");
			Iterator iterator = ap.getAllMimeHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received unexpected MimeHeader");
				logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void removeMimeHeader4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "removeMimeHeader4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Removing MimeHeader");
			ap.removeMimeHeader("doesnotexist");

			logger.log(Logger.Level.INFO, "Getting MimeHeader");
			Iterator iterator = ap.getAllMimeHeaders();
			int cnt = 0;
			while (iterator.hasNext()) {
				cnt++;
				mh = (MimeHeader) iterator.next();
				String name = mh.getName();
				String value = mh.getValue();
				logger.log(Logger.Level.ERROR, "Received unexpected MimeHeader");
				logger.log(Logger.Level.ERROR, "received: name=" + name + ", value=" + value);
				pass = false;
			}

			if (cnt != 0) {
				logger.log(Logger.Level.ERROR, "Error: expected no items to be returned, got a total of:" + cnt);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");

		resultProps.list(out);
	}

	private void setContentId1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentId1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Id");
			ap.setContentId("name@abc.com");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContentId2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentId2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Setting Content Id to null");
			ap.setContentId(null);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		try {
			setup();
			logger.log(Logger.Level.INFO, "Setting Content Id to null string");
			ap.setContentId("");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContentId3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentId3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Setting Content Id");
			ap.setContentId("id@abc.com");

			logger.log(Logger.Level.INFO, "Setting Content Id again");
			ap.setContentId("id2@abc2.com");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContentId4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentId4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Id");
			ap.setContentId("id@abc.com");

			logger.log(Logger.Level.INFO, "Setting Content Id again");
			ap.setContentId("id2@ced.com");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContentLocation1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentLocation1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Location");
			ap.setContentLocation("http://localhost/someapp");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContentLocation2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentLocation2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Location to null string");
			ap.setContentLocation("");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Location to null");
			ap.setContentLocation(null);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContentLocation3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentLocation3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Location again");
			ap.setContentLocation("/WEB-INF/somefile");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContentLocation4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentLocation4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content Location again");
			ap.setContentLocation("/WEB-INF/somefile");

			logger.log(Logger.Level.INFO, "Setting Content Location again");
			ap.setContentLocation("http://localhost/someapp");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContentLocation5Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContentLocation5Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Setting Content Location to null");
			ap.setContentLocation(null);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		try {
			setup();
			logger.log(Logger.Level.INFO, "Setting Content Location to null string");
			ap.setContentLocation("");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContent1Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContent1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content using StreamSource for text/xml mime type");
			ap.setContent(new StreamSource(in1), "text/xml");
			logger.log(Logger.Level.INFO, "Now get the content");
			Object o = ap.getContent();
			if (o == null) {
				logger.log(Logger.Level.ERROR, "Getting the content returned null (unexpected)");
				pass = false;
			} else if (!(o instanceof StreamSource)) {
				logger.log(Logger.Level.ERROR,
						"Getting the content did not return a StreamSource for text/xml content");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContent2Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContent2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting Content a first time");
			ap.setContent(new StreamSource(in1), "text/xml");
			logger.log(Logger.Level.INFO, "Resetting Content a second time");
			ap.setContent(new StreamSource(in2), "text/xml");
			logger.log(Logger.Level.INFO, "Now get the content");
			Object o = ap.getContent();
			if (o == null) {
				logger.log(Logger.Level.ERROR, "Getting the content returned null (unexpected)");
				pass = false;
			} else if (!(o instanceof StreamSource)) {
				logger.log(Logger.Level.ERROR,
						"Getting the content did not return a StreamSource for text/xml content");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setContent3Test(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setContent3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			try {
				logger.log(Logger.Level.INFO,
						"Setting Content with invalid param (a null) may throw IllegalArgumentException");
				ap.setContent(in1, null);
				// test passed
			} catch (java.lang.IllegalArgumentException iae) {
				// test passed
				logger.log(Logger.Level.INFO, "IllegalArgumentException was thrown (ok)");
			}
			try {
				logger.log(Logger.Level.INFO,
						"Setting Content with invalid param (wrong object for text/xml) may throw IllegalArgumentException");
				logger.log(Logger.Level.INFO, "IllegalArgumentException may be thrown");
				ap.setContent(in1, "text/xml");
				// test passed
			} catch (java.lang.IllegalArgumentException iae) {
				// test passed
				logger.log(Logger.Level.INFO, "IllegalArgumentException was thrown (ok)");
			}
			try {
				logger.log(Logger.Level.INFO,
						"Setting Content with invalid param (wrong object for image/gif) may throw IllegalArgumentException");
				logger.log(Logger.Level.INFO, "IllegalArgumentException may be thrown");
				ap.setContent("The Content", "image/gif");
				// test passed
			} catch (java.lang.IllegalArgumentException iae) {
				// test passed
				logger.log(Logger.Level.INFO, "IllegalArgumentException was thrown (ok)");
			}
			try {
				logger.log(Logger.Level.INFO,
						"Setting Content with invalid param (wrong object for image/jpeg) may throw IllegalArgumentException");
				logger.log(Logger.Level.INFO, "IllegalArgumentException may be thrown");
				ap.setContent("The Content", "image/jpeg");
				// test passed
			} catch (java.lang.IllegalArgumentException iae) {
				// test passed
				logger.log(Logger.Level.INFO, "IllegalArgumentException was thrown (ok)");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setDataHandler1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setDataHandler1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting DataHandler");
			ap.setDataHandler(new DataHandler(url));

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setDataHandler2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setDataHandler2Test");
		Properties resultProps = new Properties();
		boolean pass = false;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			try {
				logger.log(Logger.Level.INFO, "Setting DataHandler");
				ap.setDataHandler(null);
			} catch (IllegalArgumentException iae) {
				pass = true;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setDataHandler3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setDataHandler3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting DataHandler");
			ap.setDataHandler(new DataHandler(url));

			logger.log(Logger.Level.INFO, "Setting DataHandler");
			ap.setDataHandler(new DataHandler(url2));

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setMimeHeader1Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setMimeHeader1Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			ap.addMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Setting MimeHeader");
			ap.setMimeHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Getting MimeHeader");
			String sArray[] = ap.getMimeHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR, "Error: expected only one item to be returned, got a total of:" + len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text2")) {
					logger.log(Logger.Level.ERROR,
							"Error: received invalid value from setMimeHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: some text2");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setMimeHeader2Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setMimeHeader2Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			ap.addMimeHeader("Content-Description", "some text");
			ap.addMimeHeader("Content-Id", "id@abc.com");

			logger.log(Logger.Level.INFO, "Setting MimeHeader");
			ap.setMimeHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Getting MimeHeader");
			String sArray[] = ap.getMimeHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text2")) {
					logger.log(Logger.Level.ERROR,
							"Error: received invalid value from setMimeHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: some text2");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}

			logger.log(Logger.Level.INFO, "Getting MimeHeader");
			sArray = ap.getMimeHeader("Content-Id");
			len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getMimeHeader(name2), got a total of:" + len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("id@abc.com")) {
					logger.log(Logger.Level.ERROR, "Error: received invalid value from getMimeHeader(Content-Id)");
					logger.log(Logger.Level.ERROR, "expected result: id@abc.com");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setMimeHeader3Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setMimeHeader3Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding 2 MimeHeaders");
			ap.setMimeHeader("Content-Description", "some text");
			ap.setMimeHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Setting MimeHeader");
			ap.setMimeHeader("Content-Description", "image/jpeg");

			logger.log(Logger.Level.INFO, "Getting MimeHeader");
			String sArray[] = ap.getMimeHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("image/jpeg")) {
					logger.log(Logger.Level.ERROR,
							"Error: received invalid value from getMimeHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: image/jpeg");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setMimeHeader4Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setMimeHeader4Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Setting MimeHeader");
			ap.setMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Getting MimeHeader");
			String sArray[] = ap.getMimeHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("some text")) {
					logger.log(Logger.Level.ERROR,
							"Error: received invalid value from getMimeHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: some text");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");

		resultProps.list(out);
	}

	private void setMimeHeader5Test(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setMimeHeader5Test");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Adding MimeHeader");
			ap.addMimeHeader("Content-Description", "some text");

			logger.log(Logger.Level.INFO, "Setting MimeHeader");
			ap.setMimeHeader("Content-Description", "some text2");

			logger.log(Logger.Level.INFO, "Setting MimeHeader again");
			ap.setMimeHeader("Content-Description", "impage/jpeg");

			logger.log(Logger.Level.INFO, "Getting MimeHeader");
			String sArray[] = ap.getMimeHeader("Content-Description");
			int len = sArray.length;
			if (len != 1) {
				logger.log(Logger.Level.ERROR,
						"Error: expected only one item to be returned for getMimeHeader(Content-Description), got a total of:"
								+ len);
				pass = false;
			}

			for (int i = 0; i < len; i++) {
				String temp = sArray[i];
				if (!temp.equals("impage/jpeg")) {
					logger.log(Logger.Level.ERROR,
							"Error: received invalid value from getMimeHeader(Content-Description)");
					logger.log(Logger.Level.ERROR, "expected result: impage/jpeg");
					logger.log(Logger.Level.ERROR, "actual result:" + temp);
					pass = false;
				}
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void SetGetBase64ContentTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetBase64ContentTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			url = tsurl.getURL("http", host, port, cntxroot + "/" + JPEG_FILE);
			logger.log(Logger.Level.INFO, "Get DataHandler to jpeg attachment: " + JPEG_FILE);
			logger.log(Logger.Level.INFO, "URL = " + url);
			dh = new DataHandler(url);
			logger.log(Logger.Level.INFO, "Create InputStream from DataHandler's InputStream");
			InputStream is = dh.getInputStream();

			logger.log(Logger.Level.INFO, "Setting Content via InputStream for image/jpeg mime type");
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			OutputStream ret = new BASE64EncoderStream(bos);
			int count;
			byte buf[] = new byte[8192];
			while ((count = is.read(buf, 0, 8192)) != -1) {
				ret.write(buf, 0, count);
			}
			ret.flush();
			buf = bos.toByteArray();
			InputStream stream = new ByteArrayInputStream(buf);
			ap.setBase64Content(stream, "image/jpeg");

			logger.log(Logger.Level.INFO, "Getting Content should return InputStream object");
			InputStream r = ap.getBase64Content();
			logger.log(Logger.Level.INFO, "object returned=" + r);
			if (r == null) {
				logger.log(Logger.Level.ERROR, "null was returned");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void SetGetBase64ContentTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetBase64ContentTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		byte buf1[] = new byte[30000];
		byte buf2[] = new byte[30000];
		byte cbuf2[] = new byte[30000];

		try {
			setup();

			url = tsurl.getURL("http", host, port, cntxroot + "/" + XML_FILE);
			logger.log(Logger.Level.INFO, "Get DataHandler to xml attachment: " + XML_FILE);
			logger.log(Logger.Level.INFO, "URL = " + url);
			dh = new DataHandler(url);
			logger.log(Logger.Level.INFO, "Get InputStream from DataHandler");
			InputStream is1 = dh.getInputStream();
			logger.log(Logger.Level.INFO, "Save attachment data in buf1");
			int length1 = 0, count1 = 0;
			while (count1 != -1) {
				count1 = is1.read(buf1, length1, (30000 - length1));
				if (count1 > 0)
					length1 += count1;
			}
			is1.close();
			logger.log(Logger.Level.INFO, "length1=" + length1);
			logger.log(Logger.Level.INFO, "Get DataHandler to xml attachment again: " + XML_FILE);
			dh = new DataHandler(url);
			logger.log(Logger.Level.INFO, "Create InputStream from DataHandler's InputStream");
			InputStream is = dh.getInputStream();

			logger.log(Logger.Level.INFO, "Setting Content via InputStream for image/xml mime type");
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			OutputStream ret = new BASE64EncoderStream(bos);
			int count;
			byte buf[] = new byte[8192];
			while ((count = is.read(buf, 0, 8192)) != -1) {
				ret.write(buf, 0, count);
			}
			ret.flush();
			buf = bos.toByteArray();
			InputStream stream = new ByteArrayInputStream(buf);
			ap.setBase64Content(stream, "text/xml");

			logger.log(Logger.Level.INFO, "Getting Content should return InputStream object");
			InputStream r = ap.getBase64Content();
			logger.log(Logger.Level.INFO, "object returned=" + r);
			if (r != null) {
				logger.log(Logger.Level.INFO, "Save attachment data in buf2");
				r = new BASE64DecoderStream(r);
				int length2 = 0, count2 = 0;
				while (count2 != -1) {
					count2 = r.read(cbuf2, length2, (30000 - length2));
					if (count2 > 0)
						length2 += count2;
				}
				r.close();
				logger.log(Logger.Level.INFO, "length2=" + length2);
				for (int i = 0; i < length2; i++)
					buf2[i] = cbuf2[i];
				logger.log(Logger.Level.INFO, "Compare data in buf1 and buf2 (Must be Equal)");
				if (length1 != length2) {
					logger.log(Logger.Level.ERROR,
							"Lengths are different: length1=" + length1 + ", length2=" + length2);
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Lengths are equal (ok)");
					for (int i = 0; i < length1; i++) {
						if (buf1[i] != buf2[i]) {
							logger.log(Logger.Level.ERROR,
									"Data is different: buf1[" + i + "]=" + buf1 + ", buf2[" + i + "]=" + buf2);
							pass = false;
							break;
						}
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "null was returned");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void SetGetBase64ContentTest3(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetBase64ContentTest3");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			url = tsurl.getURL("http", host, port, cntxroot + "/" + NULL_FILE);
			logger.log(Logger.Level.INFO, "Get DataHandler to null attachment: " + NULL_FILE);
			logger.log(Logger.Level.INFO, "URL = " + url);
			dh = new DataHandler(url);
			logger.log(Logger.Level.INFO, "Create InputStream from DataHandler's InputStream");
			InputStream is = dh.getInputStream();

			logger.log(Logger.Level.INFO, "Call setBase64Content() with null content");
			ap.setBase64Content(is, "image/jpeg");
			int size = ap.getSize();
			if (size != 0) {
				logger.log(Logger.Level.ERROR,
						"setBase64Content() with null content should return size=0, got size=" + size);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void SetGetRawContentTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetRawContentTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			url = tsurl.getURL("http", host, port, cntxroot + "/" + GIF_FILE);
			logger.log(Logger.Level.INFO, "Get DataHandler to gif attachment: " + GIF_FILE);
			logger.log(Logger.Level.INFO, "URL = " + url);
			dh = new DataHandler(url);
			logger.log(Logger.Level.INFO, "Get InputStream from DataHandler");
			InputStream is1 = dh.getInputStream();

			logger.log(Logger.Level.INFO, "Setting Content via InputStream for image/gif mime type");
			ap.setRawContent(is1, "image/gif");

			logger.log(Logger.Level.INFO, "Getting Content should return InputStream object");
			InputStream is2 = ap.getRawContent();
			logger.log(Logger.Level.INFO, "object returned=" + is2);
			if (is2 == null) {
				logger.log(Logger.Level.ERROR, "null was returned");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void SetGetRawContentTest2(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetRawContentTest2");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			url = tsurl.getURL("http", host, port, cntxroot + "/" + XML_RESOURCE_FILE);
			logger.log(Logger.Level.INFO, "Get DataHandler to gif attachment: " + XML_RESOURCE_FILE);
			logger.log(Logger.Level.INFO, "URL = " + url);
			dh = new DataHandler(url);
			logger.log(Logger.Level.INFO, "Get InputStream from DataHandler");
			InputStream is1 = dh.getInputStream();

			logger.log(Logger.Level.INFO, "Setting Content via InputStream for text/xml mime type");
			ap.setRawContent(is1, "text/xml");

			logger.log(Logger.Level.INFO, "Getting Content should return InputStream object");
			InputStream is2 = ap.getRawContent();
			logger.log(Logger.Level.INFO, "object returned=" + is2);
			if (is2 == null) {
				logger.log(Logger.Level.ERROR, "null was returned");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void SetGetRawContentTest3(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetRawContentTest3");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		byte buf1[] = new byte[30000];
		byte buf2[] = new byte[30000];

		try {
			setup();

			url = tsurl.getURL("http", host, port, cntxroot + "/" + JPEG_FILE);
			logger.log(Logger.Level.INFO, "Get DataHandler to gif attachment: " + JPEG_FILE);
			logger.log(Logger.Level.INFO, "URL = " + url);
			dh = new DataHandler(url);
			logger.log(Logger.Level.INFO, "Get InputStream from DataHandler");
			InputStream is1 = dh.getInputStream();
			logger.log(Logger.Level.INFO, "Save attachment data in buf1");
			int length1 = 0, count1 = 0;
			while (count1 != -1) {
				count1 = is1.read(buf1, length1, (30000 - length1));
				if (count1 > 0)
					length1 += count1;
			}
			is1.close();
			logger.log(Logger.Level.INFO, "length1=" + length1);
			dh = new DataHandler(url);
			is1 = dh.getInputStream();

			logger.log(Logger.Level.INFO, "Setting Content via InputStream for image/jpeg mime type");
			ap.setRawContent(is1, "image/jpeg");

			logger.log(Logger.Level.INFO, "Getting Content should return InputStream object");
			InputStream is2 = ap.getRawContent();
			logger.log(Logger.Level.INFO, "object returned=" + is2);
			logger.log(Logger.Level.INFO, "getSize()=" + ap.getSize());

			if (is2 != null) {
				logger.log(Logger.Level.INFO, "Save attachment data in buf2");
				int length2 = 0, count2 = 0;
				while (count2 != -1) {
					count2 = is2.read(buf2, length2, (30000 - length2));
					if (count2 > 0)
						length2 += count2;
				}
				is2.close();
				logger.log(Logger.Level.INFO, "length2=" + length2);
				logger.log(Logger.Level.INFO, "Compare data in buf1 and buf2 (Must be Equal)");
				if (length1 != length2) {
					logger.log(Logger.Level.ERROR,
							"Lengths are different: length1=" + length1 + ", length2=" + length2);
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Lengths are equal (ok)");
					for (int i = 0; i < length1; i++) {
						if (buf1[i] != buf2[i]) {
							logger.log(Logger.Level.ERROR,
									"Data is different: buf1[" + i + "]=" + buf1 + ", buf2[" + i + "]=" + buf2);
							pass = false;
							break;
						}
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "null was returned");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void SetGetRawContentTest4(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetRawContentTest4");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			url = tsurl.getURL("http", host, port, cntxroot + "/" + NULL_FILE);
			logger.log(Logger.Level.INFO, "Get DataHandler to null attachment: " + NULL_FILE);
			logger.log(Logger.Level.INFO, "URL = " + url);
			dh = new DataHandler(url);
			logger.log(Logger.Level.INFO, "Get InputStream from DataHandler");
			InputStream is = dh.getInputStream();

			logger.log(Logger.Level.INFO, "Call setRawContent() with null content");
			ap.setRawContent(is, "image/gif");
			int size = ap.getSize();
			if (size != 0) {
				logger.log(Logger.Level.ERROR,
						"setRawContent() with null content should return size=0, got size=" + size);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void SetGetRawContentBytesTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetGetRawContentBytesTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		byte buf1[] = new byte[30000];

		try {
			setup();

			url = tsurl.getURL("http", host, port, cntxroot + "/" + JPEG_FILE);
			logger.log(Logger.Level.INFO, "Get DataHandler to gif attachment: " + JPEG_FILE);
			logger.log(Logger.Level.INFO, "URL = " + url);
			dh = new DataHandler(url);
			logger.log(Logger.Level.INFO, "Get InputStream from DataHandler");
			InputStream is1 = dh.getInputStream();
			logger.log(Logger.Level.INFO, "Save attachment data in buf1");
			int length1 = 0, count1 = 0;
			while (count1 != -1) {
				count1 = is1.read(buf1, length1, (30000 - length1));
				if (count1 > 0)
					length1 += count1;
			}
			is1.close();
			logger.log(Logger.Level.INFO, "length1=" + length1);
			byte data1[] = new byte[length1];
			for (int i = 0; i < length1; i++)
				data1[i] = buf1[i];
			dh = new DataHandler(url);
			is1 = dh.getInputStream();

			logger.log(Logger.Level.INFO, "Setting Content via byte[] array for image/jpeg mime type");
			ap.setRawContentBytes(data1, 0, length1, "image/jpeg");

			logger.log(Logger.Level.INFO, "Getting Content should return byte[] array");
			byte data2[] = ap.getRawContentBytes();

			if (data2 != null) {
				int length2 = data2.length;
				logger.log(Logger.Level.INFO, "length2=" + length2);
				logger.log(Logger.Level.INFO, "Compare data in data1 and data2 (Must be Equal)");
				if (length1 != length2) {
					logger.log(Logger.Level.ERROR,
							"Lengths are different: length1=" + length1 + ", length2=" + length2);
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Lengths are equal (ok)");
					for (int i = 0; i < length1; i++) {
						if (data1[i] != data2[i]) {
							logger.log(Logger.Level.ERROR,
									"Data is different: data1[" + i + "]=" + data1 + ", data2[" + i + "]=" + data2);
							pass = false;
							break;
						}
					}
				}
			} else {
				logger.log(Logger.Level.ERROR, "null was returned");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void SetRawContentBytesSOAPOrNullPointerExceptionTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "SetRawContentBytesSOAPOrNullPointerExceptionTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Setting Content via byte[] array that is null");
			byte data1[] = null;
			ap.setRawContentBytes(data1, 0, 1, "image/jpeg");
			logger.log(Logger.Level.ERROR, "Did not throw expected SOAPException or NullPointerException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Caught expected SOAPException");
		} catch (NullPointerException e) {
			logger.log(Logger.Level.INFO, "Caught expected NullPointerException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void GetRawContentBytesSOAPOrNullPointerExceptionTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "GetRawContentBytesSOAPOrNullPointerExceptionTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Getting Content on an empty attachment");
			byte data1[] = null;
			data1 = ap.getRawContentBytes();
			logger.log(Logger.Level.ERROR, "Did not throw expected SOAPException or NullPointerException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Caught expected SOAPException");
		} catch (NullPointerException e) {
			logger.log(Logger.Level.INFO, "Caught expected NullPointerException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setBase64ContentIOExceptionTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setBase64ContentIOExceptionTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			url = tsurl.getURL("http", host, port, cntxroot + "/" + NOEXISTS_FILE);
			logger.log(Logger.Level.INFO, "Get DataHandler to gif attachment: " + NOEXISTS_FILE);
			logger.log(Logger.Level.INFO, "URL = " + url);
			dh = new DataHandler(url);

			logger.log(Logger.Level.INFO, "Call setBase64Content() on non existant InputStream");
			ap.setBase64Content(dh.getInputStream(), "image/gif");
			logger.log(Logger.Level.ERROR, "Did not throw expected IOException");
			pass = false;
		} catch (IOException e) {
			logger.log(Logger.Level.INFO, "Did throw expected IOException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setBase64ContentNullPointerExceptionTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setBase64ContentNullPointerExceptionTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Call setBase64Content() passing in an InputStream that is null");
			InputStream is = null;
			ap.setBase64Content(is, "image/gif");
			logger.log(Logger.Level.ERROR, "Did not throw expected NullPointerException");
			pass = false;
		} catch (NullPointerException e) {
			logger.log(Logger.Level.INFO, "Did throw expected NullPointerException");
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did throw expected SOAPException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setBase64ContentSOAPExceptionTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setBase64ContentSOAPExceptionTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			url = tsurl.getURL("http", host, port, cntxroot + "/" + JPEG_FILE);
			logger.log(Logger.Level.INFO, "Get DataHandler to jpeg attachment: " + JPEG_FILE);
			logger.log(Logger.Level.INFO, "URL = " + url);
			dh = new DataHandler(url);
			logger.log(Logger.Level.INFO, "Create InputStream from DataHandler's InputStream");
			InputStream is = dh.getInputStream();
			logger.log(Logger.Level.INFO, "Call setBase64Content() with an InputStream that is not Base64 encoded");
			ap.setBase64Content(is, "image/jpeg");
			logger.log(Logger.Level.ERROR, "Did not throw expected SOAPException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did throw expected SOAPException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setRawContentIOExceptionTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setRawContentIOExceptionTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			url = tsurl.getURL("http", host, port, cntxroot + "/" + NOEXISTS_FILE);
			logger.log(Logger.Level.INFO, "Get DataHandler to attachment that doesn't exist: " + NOEXISTS_FILE);
			logger.log(Logger.Level.INFO, "URL = " + url);
			dh = new DataHandler(url);

			logger.log(Logger.Level.INFO, "Call setRawContent() on a non existant InputStream");
			ap.setRawContent(dh.getInputStream(), "image/gif");
			logger.log(Logger.Level.ERROR, "Did not throw expected IOException");
			pass = false;
		} catch (IOException e) {
			logger.log(Logger.Level.INFO, "Did throw expected IOException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void setRawContentNullPointerExceptionTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "setRawContentNullPointerExceptionTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();

			logger.log(Logger.Level.INFO, "Call setRawContent() passing in an InputStream that is null");
			InputStream is = null;
			ap.setRawContent(is, "image/gif");
			logger.log(Logger.Level.ERROR, "Did not throw expected NullPointerException");
			pass = false;
		} catch (NullPointerException e) {
			logger.log(Logger.Level.INFO, "Did throw expected NullPointerException");
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did throw expected SOAPException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getBase64ContentSOAPExceptionTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getBase64SOAPExceptionTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Call getBase64Content() on empty Attachment object");
			InputStream is = ap.getBase64Content();
			logger.log(Logger.Level.ERROR, "Did not throw expected SOAPFaultException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did throw expected SOAPFaultException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}

	private void getRawContentSOAPExceptionTest1(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		logger.log(Logger.Level.TRACE, "getRawSOAPExceptionTest1");
		Properties resultProps = new Properties();
		boolean pass = true;

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		try {
			setup();
			logger.log(Logger.Level.INFO, "Call getRawContent() on empty Attachment object");
			InputStream is = ap.getRawContent();
			logger.log(Logger.Level.ERROR, "Did not throw expected SOAPFaultException");
			pass = false;
		} catch (SOAPException e) {
			logger.log(Logger.Level.INFO, "Did throw expected SOAPFaultException");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception: " + e);
			TestUtil.printStackTrace(e);
			pass = false;
		}
		// Send response object and test result back to client
		if (pass)
			resultProps.setProperty("TESTRESULT", "pass");
		else
			resultProps.setProperty("TESTRESULT", "fail");
		resultProps.list(out);
	}
}
