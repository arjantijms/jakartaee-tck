/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.samples.ejb.ee.simpleHello;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import jakarta.ejb.EJBException;

import java.util.Properties;

public class HelloEJB {
  private String str;

  private Properties p = null;

  public HelloEJB() {
  }

  public void initialize(String str, Properties props) {
    p = props;
    try {
      TestUtil.logTrace("initialize");
      TestUtil.init(props);
      this.str = str;
    } catch (Exception e) {
      TestUtil.logErr("init failed", e);
    }
  }

  public String sayHello() {
    System.out.println("Hello EJB - creating another HelloEJB !!");

    // create EJB
    Hello hr = null;
    try {
      TSNamingContext ic = new TSNamingContext();
      hr = (Hello) ic.lookup("java:comp/env/ejb/Hello", Hello.class);
      hr.initialize("Hello Again!!", p);
      TestUtil.logMsg("Got the EJB!!");
    } catch (Exception ex) {
      TestUtil.logErr("got exception", ex);
      throw new EJBException("unknown exception", ex);
    }

    // invoke method on the EJB
    TestUtil.logMsg("HelloEJB: calling HelloEJB.sayHelloAgain() on thread "
        + Thread.currentThread());
    String result = null;
    try {
      result = hr.sayHelloAgain();
    } catch (Exception ex) {
      TestUtil.logErr("Exception encountered ", ex);
      throw new EJBException("Exception encountered", ex);
    }

    return result;
  }

  public String sayHelloAgain() {
    TestUtil.logMsg(
        "Hello EJB - in sayHelloAgain(), thread is " + Thread.currentThread());
    return str;
  }

}
