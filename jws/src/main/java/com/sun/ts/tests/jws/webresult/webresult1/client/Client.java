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
 */
/*
 * $Id$
 */

package com.sun.ts.tests.jws.webresult.webresult1.client;

import java.net.URL;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jws.common.JWS_Util;
import com.sun.ts.tests.jws.common.WsdlUtils;
import com.sun.ts.tests.jws.common.XMLUtils;

/**
 * @test
 * @executeClass com.sun.ts.tests.jws.webresult.webresult1.client.Client
 * @sources Client.java
 * @keywords webservice
 */

public class Client extends ServiceEETest {

  static WebResultWebServiceService service = null;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "jwstest.webresultwebservice.endpoint.1";

  private static final String EXPECTED_DEFAULT_PART_NAME_1 = "helloStringResponse";

  private static final String EXPECTED_DEFAULT_PART_NAME_2 = "helloString2Response";

  private static final String EXPECTED_ELEMENT_TARGETNAMESPACE_4 = "hello4/employee";

  private static final String EXPECTED_ELEMENT_TARGETNAMESPACE_5 = "hello5/name";

  private static final String EXPECTED_OPERATION_NAME_3 = "helloString3";

  private static final String EXPECTED_OPERATION_NAME_5 = "helloString5";

  private static final String EXPECTED_PART_ELEMENT_NAME_1 = "helloStringResponse";

  private static final String EXPECTED_PART_ELEMENT_NAME_2 = "helloString2Response";

  private static final String EXPECTED_PART_ELEMENT_NAME_3 = "name3";

  private static final String EXPECTED_PART_ELEMENT_NAME_4 = "employee";

  private static final String EXPECTED_PART_ELEMENT_NAME_5 = "name5";

  private static final String EXPECTED_PART_NAME_3 = "name3";

  private static final String EXPECTED_PART_NAME_4 = "Employee";

  private static final String EXPECTED_PART_NAME_5 = "name5";

  private static final String EXPECTED_RESULT_1 = "hello1 : Hello jsr181 to Web Service";

  private static final String EXPECTED_RESULT_2 = "hello2 : Hello First Name:jsr181 Last Name:jaxws to Web Service";

  private static final String HOSTNAME = "localhost";

  private static final String MODEPROP = "platform.mode";

  // service and port information
  private static final String NAMESPACEURI = "http://server.webresult1.webresult.jws.tests.ts.sun.com/";

  private static final String PKG_NAME = "com.sun.ts.tests.jws.webresult.webresult1.client.";

  private static final String PORT_NAME = "webResultWebServicePort";

  private static final int PORTNUM = 8000;

  private static final String PORTTYPE_NAME = "webResultWebService";

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String SERVICE_NAME = "webResultWebServiceService";

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String WSDLLOC_URL = "jwstest.webresultwebservice.wsdlloc.1";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  private TSURL ctsurl = new TSURL();

  // Document used to check the WSDL
  private Document doc;

  private QName EXPECTED_DEFAULT_PART_QNAME_1 = new QName(NAMESPACEURI,
      EXPECTED_DEFAULT_PART_NAME_1);

  private QName EXPECTED_DEFAULT_PART_QNAME_2 = new QName(NAMESPACEURI,
      EXPECTED_DEFAULT_PART_NAME_2);

  private QName EXPECTED_OPERATION_QNAME_5 = new QName(NAMESPACEURI,
      EXPECTED_OPERATION_NAME_5);

  private QName EXPECTED_PART_ELEMENT_QNAME_1 = new QName(NAMESPACEURI,
      EXPECTED_PART_ELEMENT_NAME_1);

  private QName EXPECTED_PART_ELEMENT_QNAME_2 = new QName(NAMESPACEURI,
      EXPECTED_PART_ELEMENT_NAME_2);

  private QName EXPECTED_PART_ELEMENT_QNAME_3 = new QName(NAMESPACEURI,
      EXPECTED_PART_ELEMENT_NAME_3);

  private QName EXPECTED_PART_ELEMENT_QNAME_4 = new QName(
      EXPECTED_ELEMENT_TARGETNAMESPACE_4, EXPECTED_PART_ELEMENT_NAME_4);

  private QName EXPECTED_PART_ELEMENT_QNAME_5 = new QName(
      EXPECTED_ELEMENT_TARGETNAMESPACE_5, EXPECTED_PART_ELEMENT_NAME_5);

  private QName EXPECTED_PART_QNAME_3 = new QName(NAMESPACEURI,
      EXPECTED_PART_NAME_3);

  private QName EXPECTED_PART_QNAME_4 = new QName(NAMESPACEURI,
      EXPECTED_PART_NAME_4);

  private QName EXPECTED_PART_QNAME_5 = new QName(NAMESPACEURI,
      EXPECTED_PART_NAME_5);

  private String hostname = HOSTNAME;

  String modeProperty = null; // platform.mode -> (standalone|j2ee)

  private WebResultWebService port;

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private int portnum = PORTNUM;

  private QName PORTTYPE_QNAME = new QName(NAMESPACEURI, PORTTYPE_NAME);

  private Properties props = null;

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private String url = null;

  private URL wsdlurl = null;

  public void cleanup() throws Exception {
    TestUtil.logMsg("cleanup ok");
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (WebResultWebService) service.getPort(WebResultWebService.class);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
  }

  private void getPortJ2ee() throws Exception {
    /* Lookup service then obtain port and stub */
    InitialContext ctx = new InitialContext();
    TestUtil.logMsg("Obtained InitialContext");
    TestUtil.logMsg("Lookup java:comp/env/service/WebResultwebservice");
    jakarta.xml.ws.Service svc = (jakarta.xml.ws.Service) ctx
        .lookup("java:comp/env/service/WebResultwebservice");
    TestUtil.logMsg("Obtained service");
    TestUtil.logMsg("Get port");
    port = (WebResultWebService) svc.getPort(WebResultWebService.class);
    TestUtil.logMsg("Obtained port");
    JWS_Util.setSOAPLogging(port);
  }

  private void getPortStandalone() throws Exception {
    port = (WebResultWebService) JWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        WebResultWebServiceService.class, PORT_QNAME,
        WebResultWebService.class);
    JWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  /*
   * @class.testArgs: -ap jws-url-props.dat @class.setup_props: webServerHost;
   * webServerPort; platform.mode;
   */
  public void setup(String[] args, Properties p) throws Exception {
    props = p;
    boolean pass = true;

    try {
      hostname = p.getProperty(WEBSERVERHOSTPROP);
      if (hostname == null)
        pass = false;
      else if (hostname.equals(""))
        pass = false;
      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        pass = false;
      }
      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        getPortStandalone();
      } else {
        getTestURLs();
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (WebResultWebServiceService) getSharedObject();
        getPortJavaEE();
      }

      doc = WsdlUtils.getDocument(wsdlurl.toString());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("setup failed:", e);
    }

    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Exception("setup failed:");
    }
  }

  /*
   * @testName: testHelloString
   *
   * @assertion_ids: JWS:SPEC:3013
   *
   * @test_Strategy:
   */
  public void testHelloString() throws Exception {
    TestUtil.logMsg("testHelloString");
    boolean pass = true;
    String result = "";
    try {
      TestUtil.logMsg("Invoke the helloString() method of WebResultWebService");
      result = port.helloString("jsr181");
      if (!result.equals(EXPECTED_RESULT_1))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Exception("testHelloString failed", e);
    }

    if (!pass)
      throw new Exception("testHelloString failed returned value : " + result);
    TestUtil.logMsg("Invocation of helloString() passed");
  }

  /*
   * @testName: testHelloString2
   *
   * @assertion_ids: JWS:SPEC:3013
   *
   * @test_Strategy:
   */
  public void testHelloString2() throws Exception {
    TestUtil.logMsg("testHelloString2");
    boolean pass = true;
    String result = "";
    try {
      TestUtil
          .logMsg("Invoke the helloString2() method of WebMethodWebService");
      Name name = new Name();
      name.setFirstName("jsr181");
      name.setLastName("jaxws");
      result = port.helloString2(name);
      if (!result.equals(EXPECTED_RESULT_2))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Exception("testHelloString2 failed", e);
    }

    if (!pass)
      throw new Exception("testHelloString2 failed, returned value : " + result);
    TestUtil.logMsg("Invocation of helloString2() passed");
  }

  /*
   * @testName: testHelloString3
   *
   * @assertion_ids: JWS:SPEC:3013
   *
   * @test_Strategy:
   */
  public void testHelloString3() throws Exception {
    TestUtil.logMsg("testHelloString3");
    boolean pass = true;
    Name output = null;
    try {
      TestUtil
          .logMsg("Invoke the helloString3() method of WebMethodWebService");

      output = port.helloString3(true);

      if ((!output.getFirstName().equals("jsr181"))
          && (!output.getLastName().equals("jsr109")))
        pass = false;

      TestUtil.logMsg(" First Name : " + output.getFirstName()
          + "  Last Name : " + output.getLastName());

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Exception("testHelloString3 failed", e);
    }

    if (!pass)
      throw new Exception("testHelloString3 failed, returned value : " + output);
    TestUtil.logMsg("Invocation of helloString3() passed");
  }

  /*
   * @testName: testHelloString4
   *
   * @assertion_ids: JWS:SPEC:3013
   *
   * @test_Strategy:
   */
  public void testHelloString4() throws Exception {
    TestUtil.logMsg("testHelloString3");
    boolean pass = true;
    Name output = null;

    try {
      TestUtil
          .logMsg("Invoke the helloString4() method of WebMethodWebService");

      Employee employee = port.helloString4();

      output = employee.getName();

      if (output != null) {

        TestUtil.logMsg(" First Name : " + output.getFirstName()
            + "  Last Name : " + output.getLastName());

        if ((!output.getFirstName().equals("jsr181"))
            && (!output.getLastName().equals("jaxws")))
          pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Exception("testHelloString4 failed", e);
    }

    if (!pass)
      throw new Exception("testHelloString4 failed, returned value : " + output);
    TestUtil.logMsg("Invocation of helloString4() passed");
  }

  /*
   * @testName: testPingWSDL
   *
   * @assertion_ids: JWS:SPEC:3004
   *
   * @test_Strategy: Ping WSDL
   *
   */
  public void testPingWSDL() throws Exception {
    TestUtil.logMsg("testPingWSDL");
    boolean pass = true;
    try {
      TestUtil.logMsg("Get the WSDL of WebResultWebService");
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      TestUtil.logMsg("Parse WSDL from location " + wsdlurl.toString());
      Document document = builder.parse(wsdlurl.toString());
      XMLUtils.xmlDumpDocument(document);
      NodeList nodeList = document.getElementsByTagName("*");
      if (nodeList.getLength() <= 0) {
        TestUtil.logErr("WSDL did not parse successfully");
        pass = false;
      }
      TestUtil.logMsg("WSDL parsed successfully");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Exception("testPingWSDL failed", e);
    }

    if (!pass)
      throw new Exception("testPingWSDL failed");
  }

  /*
   * @testName: testWSDL1
   *
   * @assertion_ids: JWS:SPEC:5009; JWS:SPEC:6007
   *
   * @test_Strategy: Check for the default element QName of the part and the
   * default name of the part
   *
   */

  public void testWSDL1() throws Exception {
    boolean pass = false;
    String elementValue = null;
    Element messageElement = null;
    Element partElement = null;
    Attr ele = null;
    QName elementQName = null;
    QName operationQName = new QName(NAMESPACEURI, "helloString");

    try {

      messageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          operationQName, "output");
      if (messageElement != null) {
        partElement = com.sun.ts.tests.jws.common.WsdlUtils.findPartElement(doc,
            messageElement, EXPECTED_DEFAULT_PART_QNAME_1);
      }

      if (partElement != null) {
        // Check if we need to use the name space
        ele = partElement.getAttributeNode("element");
        if (ele != null) {

          elementValue = ele.getValue();
          elementQName = com.sun.ts.tests.jws.common.WsdlUtils
              .createQName(partElement, elementValue);
          if (elementQName.equals(EXPECTED_PART_ELEMENT_QNAME_1))
            pass = true;

        }
      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Exception("testWSDL1 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(
          " testWSDL1() passed :  Found the part element with element name : "
              + elementQName + " in the WSDL ");
    } else {
      throw new Exception(
          "testWSDL1 failed : could not find part element with element name : "
              + EXPECTED_PART_ELEMENT_QNAME_1 + " in Service : " + SERVICE_QNAME
              + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL2
   *
   * @assertion_ids: JWS:SPEC:6007
   *
   * @test_Strategy: Check for the default element QName of the part and the
   * default name of the part
   *
   */

  public void testWSDL2() throws Exception {
    boolean pass = false;
    String elementValue = null;
    Element messageElement = null;
    Element partElement = null;
    Attr ele = null;
    QName elementQName = null;
    QName operationQName = new QName(NAMESPACEURI, "helloString2");

    try {

      messageElement = com.sun.ts.tests.jws.common.WsdlUtils
          .findMessageElement(doc, PORTTYPE_QNAME, operationQName, "output");
      if (messageElement != null) {
        partElement = com.sun.ts.tests.jws.common.WsdlUtils.findPartElement(doc,
            messageElement, EXPECTED_DEFAULT_PART_QNAME_2);
      }

      if (partElement != null) {
        // Check if we need to use the name space
        ele = partElement.getAttributeNode("element");
        if (ele != null) {

          elementValue = ele.getValue();
          elementQName = com.sun.ts.tests.jws.common.WsdlUtils
              .createQName(partElement, elementValue);
          if (elementQName.equals(EXPECTED_DEFAULT_PART_QNAME_2))
            pass = true;

        }
      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Exception("testWSDL2 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(
          " testWSDL2() passed :  Found the part element with element name : "
              + elementQName + " in the WSDL ");
    } else {
      throw new Exception(
          "testWSDL2 failed : could not find part element with element name : "
              + EXPECTED_PART_ELEMENT_QNAME_2 + " in Service : " + SERVICE_QNAME
              + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL3
   *
   * @assertion_ids: JWS:SPEC:6011
   *
   * @test_Strategy: Check for the part element name and partName when "name"
   * attribute of WebResult set
   *
   */

  public void testWSDL3() throws Exception {
    boolean pass = false;
    String elementValue = null;
    Element messageElement = null;
    Element partElement = null;
    Attr ele = null;
    QName elementQName = null;
    QName operationQName = new QName(NAMESPACEURI, "helloString3");

    try {

      messageElement = com.sun.ts.tests.jws.common.WsdlUtils
          .findMessageElement(doc, PORTTYPE_QNAME, operationQName, "output");
      if (messageElement != null) {
        partElement = com.sun.ts.tests.jws.common.WsdlUtils.findPartElement(doc,
            messageElement, EXPECTED_PART_QNAME_3);
      }

      if (partElement != null) {
        // Check if we need to use the name space
        ele = partElement.getAttributeNode("element");
        if (ele != null) {

          elementValue = ele.getValue();
          elementQName = com.sun.ts.tests.jws.common.WsdlUtils
              .createQName(partElement, elementValue);
          if (elementQName.equals(EXPECTED_PART_ELEMENT_QNAME_3))
            pass = true;

        }
      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Exception("testWSDL3 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(
          " testWSDL3() passed :  Found the part element with element name : "
              + elementQName + " in the WSDL ");
    } else {
      throw new Exception(
          "testWSDL3 failed : could not find part element with element name : "
              + EXPECTED_PART_ELEMENT_QNAME_3 + " in Service : " + SERVICE_QNAME
              + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL4
   *
   * @assertion_ids: JWS:SPEC:6007
   *
   * @test_Strategy: Check for the part element name and partName when "name"
   * and "partName" attributes of WebResult set
   *
   */

  public void testWSDL4() throws Exception {
    boolean pass = false;
    String elementValue = null;
    Element messageElement = null;
    Element partElement = null;
    Attr ele = null;
    QName elementQName = null;
    QName operationQName = new QName(NAMESPACEURI, "helloString4");

    try {

      messageElement = com.sun.ts.tests.jws.common.WsdlUtils
          .findMessageElement(doc, PORTTYPE_QNAME, operationQName, "output");
      if (messageElement != null) {
        partElement = com.sun.ts.tests.jws.common.WsdlUtils.findPartElement(doc,
            messageElement, EXPECTED_PART_QNAME_4);
      }

      if (partElement != null) {
        // Check if we need to use the name space
        ele = partElement.getAttributeNode("element");
        if (ele != null) {

          elementValue = ele.getValue();
          elementQName = com.sun.ts.tests.jws.common.WsdlUtils
              .createQName(partElement, elementValue);
          if (elementQName.equals(EXPECTED_PART_ELEMENT_QNAME_4))
            pass = true;

        }
      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Exception("testWSDL4 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(
          " testWSDL4() passed :  Found the part element with element name : "
              + elementQName + " in the WSDL ");
    } else {
      throw new Exception(
          "testWSDL4 failed : could not find part element with element name : "
              + EXPECTED_PART_ELEMENT_QNAME_4 + " in Service : " + SERVICE_QNAME
              + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL5
   *
   * @assertion_ids: JWS:SPEC:6013
   *
   * @test_Strategy: Check for the header
   *
   */

  public void testWSDL5() throws Exception {
    boolean pass = false;
    Element operationElement = null;
    Element soapHeaderElement = null;
    Element messageElement = null;
    Element partElement = null;

    try {

      operationElement = com.sun.ts.tests.jws.common.WsdlUtils
          .findOperationElement(doc, EXPECTED_OPERATION_QNAME_5, SERVICE_QNAME,
              PORT_QNAME);

      if (operationElement != null) {

        soapHeaderElement = com.sun.ts.tests.jws.common.WsdlUtils
            .findSoapHeaderElement(doc, operationElement, "output",
                EXPECTED_PART_NAME_5, PORTTYPE_QNAME,
                EXPECTED_OPERATION_QNAME_5);

      }

      messageElement = com.sun.ts.tests.jws.common.WsdlUtils.findMessageElement(
          doc, PORTTYPE_QNAME, EXPECTED_OPERATION_QNAME_5, "output");

      if (messageElement != null) {

        partElement = com.sun.ts.tests.jws.common.WsdlUtils.findPartElement(doc,
            messageElement, EXPECTED_PART_ELEMENT_QNAME_5, true);

      }

      if (soapHeaderElement != null && partElement != null)
        pass = true;

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Exception("testWSDL5 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(" testWSDL5() passed :  Found the soap header element : "
          + soapHeaderElement + " : " + EXPECTED_PART_ELEMENT_QNAME_5
          + " in the WSDL");
    } else {
      throw new Exception("testWSDL5 failed : could not find soap header element : "
          + EXPECTED_PART_ELEMENT_QNAME_5 + " in the WSDL ");
    }

  }
}
