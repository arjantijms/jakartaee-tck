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

package com.sun.ts.tests.webservices12.wsdlImport.http.twin4.client;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;

import java.util.Properties;

import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebServiceRef;

public class Client extends EETest {
  private String SERVICE_NAME_WITH_WSDL_1 = "WSTwin4Http1";

  private String SERVICE_NAME_WITH_WSDL_2 = "WSTwin4Http2";

  private Tests1 port1;

  private Tests2 port2;

  @WebServiceRef(name = "service/WSTwin4Http1")
  static Twin4HttpSvc1 svc1;

  @WebServiceRef(name = "service/WSTwin4Http2")
  static Twin4HttpSvc2 svc2;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap webservices-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort;
   */

  public void setup(String[] args, Properties p) throws Exception {
    try {
      TestUtil.logMsg("Get port from Service");
      port1 = (Tests1) svc1.getPort(Tests1.class);
      TestUtil.logMsg("Port obtained");

      TestUtil.logMsg("Get port from Service");
      port2 = (Tests2) svc2.getPort(Tests2.class);
      TestUtil.logMsg("Port obtained");
    } catch (Exception e) {
      throw new Exception("setup failed:", e);
    }

    logMsg("setup ok");
  }

  public void cleanup() throws Exception {
    logMsg("cleanup ok");
  }

  /*
   * @testName: InvokeMethod
   *
   * @assertion_ids: WS4EE:SPEC:214; WS4EE:SPEC:66;
   *
   * @test_Strategy: Call a method in Tests.
   */
  public void InvokeMethod() throws Exception {
    TestUtil.logMsg("InvokeMethod");
    try {
      TestUtil.logMsg("Invoking method on Service1");
      port1.invokeTest1();
      TestUtil.logMsg("Service1 passed");
      TestUtil.logMsg("Invoking method on Service2");
      port2.invokeTest2();
      TestUtil.logMsg("Service2 passed");
    } catch (Throwable t) {
      TestUtil
          .logMsg("test InvokeMethod failed: got exception " + t.toString());
      throw new Exception("InvokeMethod failed");
    }

    return;
  }
}
