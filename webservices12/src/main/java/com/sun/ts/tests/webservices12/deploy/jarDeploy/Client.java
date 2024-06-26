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

package com.sun.ts.tests.webservices12.deploy.jarDeploy;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;

import java.util.Iterator;
import jakarta.xml.ws.Service;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.util.Properties;

public class Client extends EETest {

  @jakarta.xml.ws.WebServiceRef(name = "service/WSjarDeploy")
  static HelloWsService svc;

  HelloWs port = null;

  private void getStub() throws Exception {
    TestUtil.logMsg("WebServiceRef for service/WSjarDeploy");
    TestUtil.logMsg("svc=" + svc);
    TestUtil.logMsg("Get port from Service");
    port = (HelloWs) svc.getPort(HelloWs.class);
    TestUtil.logMsg("Port obtained");
    TestUtil.logMsg("port=" + port);
    JAXWS_Util.dumpTargetEndpointAddress(port);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxws-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort;
   */

  public void setup(String[] args, Properties p) throws Exception {
    try {
      getStub();
    } catch (Exception e) {
      throw new Exception("setup failed:", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Exception {
    logMsg("cleanup ok");
  }

  private void printSeperationLine() {
    TestUtil.logMsg("---------------------------");
  }

  /*
   * @testName: call_hello
   *
   * @assertion_ids: WS4EE:SPEC:183; JavaEE:SPEC:247;
   *
   * 
   * @test_Strategy: call method on deployed web services implementation, which
   * is deployed from a .jar file
   */
  public void call_hello() throws Exception {
    TestUtil.logMsg("call_hello");
    try {
      HelloRequest req = new HelloRequest();
      req.setArgument("Hi there");
      TestUtil.logMsg("Invoke sayHello method ...");
      HelloResponse ret = port.sayHello(req);
      if (!ret.getArgument().equals("'Hi there' to you too!")) {
        TestUtil.logMsg("test call_hello failed: return value from server is: "
            + ret.getArgument());
        throw new Exception("call_hello failed");
      }
    } catch (Throwable t) {
      TestUtil.logMsg("test call_hello failed: got exception " + t.toString());
      throw new Exception("call_hello failed");
    }
    return;
  }

}
