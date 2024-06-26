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
package com.sun.ts.tests.webservices12.sec.descriptors.servlet.certificate;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;
import java.util.Iterator;
import jakarta.xml.ws.*;
import javax.xml.namespace.QName;
import java.util.Properties;
import java.util.Map;
import javax.net.ssl.*;

public class Client extends EETest {
  HelloCertificate certificatePort;

  private TSURL ctsurl = new TSURL();

  private String hostname = "localhost";

  private String PROTOCOL = "https";

  private String urlString = null;

  private int portnum = 8000;

  private static final String ENDPOINTURL = "/WSSecWarCertificate_web/ws4ee/SecWarCertificate";

  @WebServiceRef(name = "service/secWar/wscertificate")
  static HelloCertificateService service = null;

  private void getPort() throws Exception {
    TestUtil.logMsg("service=" + service);
    TestUtil.logMsg("Get port from certificate Service");
    certificatePort = (HelloCertificate) service.getHelloCertificatePort();
    TestUtil.logMsg("port=" + certificatePort);
    TestUtil.logMsg("certificate port obtained");
    BindingProvider bindingProvider = (BindingProvider) certificatePort;
    Map<String, Object> map = bindingProvider.getRequestContext();
    TestUtil
        .logMsg("Setting the target endpoint address on WS port: " + urlString);
    map.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, urlString);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap webservices-url-props.dat
   * 
   * @class.setup_props: webServerHost; securedWebServicePort;
   */

  public void setup(String[] args, Properties p) throws Exception {
    try {
      hostname = p.getProperty("webServerHost");
      portnum = Integer.parseInt(p.getProperty("securedWebServicePort"));
      urlString = ctsurl.getURLString(PROTOCOL, hostname, portnum, ENDPOINTURL);
      HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      });
      getPort();
    } catch (Exception e) {
      throw new Exception("setup failed:", e);
    }
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Exception {
    TestUtil.logMsg("cleanup ok");
  }

  private void printSeperationLine() {
    TestUtil.logMsg("---------------------------");
  }

  /*
   * @testName: secWarCertificate
   *
   * @assertion_ids: WS4EE:SPEC:193; WS4EE:SPEC:196; WS4EE:SPEC:194;
   * WS4EE:SPEC:195; WS4EE:SPEC:9002;
   * 
   * @test_Strategy: Call EJB with client certificate authentication
   */
  public void secWarCertificate() throws Exception {
    TestUtil.logMsg("secWarCertificate");
    try {
      String ret1 = certificatePort.sayHelloCertificate("secWarCertificate");
      if (!ret1.equals("'secWarCertificate' from HelloCertificateImpl!")) {
        TestUtil.logMsg("secWarCertificate failed: return value: " + ret1);
        throw new Exception("SecWarCertificate failed");
      }
      TestUtil.logMsg("secWarCertificate passed");
    } catch (Throwable t) {
      TestUtil
          .logMsg("secWarCertificate failed: got exception " + t.toString());
      throw new Exception("secWarCertificate failed");
    }
    return;
  }
}
