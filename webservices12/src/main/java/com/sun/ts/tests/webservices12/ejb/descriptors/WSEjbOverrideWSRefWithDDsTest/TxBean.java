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

package com.sun.ts.tests.webservices12.ejb.descriptors.WSEjbOverrideWSRefWithDDsTest;

import jakarta.ejb.Stateless;
import jakarta.jws.WebService;

import javax.naming.InitialContext;

@WebService(portName = "JunkJunkJunkPortName", serviceName = "JunkJunkJunkServiceName", targetNamespace = "http://Tx.org", wsdlLocation = "META-INF/wsdl/TxService.wsdl", endpointInterface = "com.sun.ts.tests.webservices12.ejb.descriptors.WSEjbOverrideWSRefWithDDsTest.Tx")
@Stateless(name = "WsFrontEjb")
public class TxBean {

  private ChokeRemote choke = null;

  private Object o;

  private void lookupChoke() {
    try {
      if (choke == null) {
        InitialContext ctx = new InitialContext();
        choke = (ChokeRemote) ctx.lookup("java:comp/env/ejb/wschokebean");
      }
    } catch (Exception e) {
      System.out.println("*** TxBean.lookupChoke: failed to find choke");
      e.printStackTrace();
    }
  }

  public String txRequired(String s) {
    try {
      lookupChoke();
      choke.chokeMandatory();
      return s;
    } catch (Exception e) {
      throw new RuntimeException(
          "TxBean.txRequired choked on " + e.getMessage());
    }
  }

  public String txRequiresNew(String s) {
    try {
      lookupChoke();
      choke.chokeMandatory();
      return s;
    } catch (Exception e) {
      throw new RuntimeException(
          "TxBean.txRequiresNew choked on " + e.getMessage());
    }
  }

  public String txSupports(String s) {
    try {
      lookupChoke();
      choke.chokeNever();
      return s;
    } catch (Exception e) {
      throw new RuntimeException(
          "TxBean.txSupports choked on " + e.getMessage());
    }
  }

  public String txNotSupported(String s) {
    try {
      lookupChoke();
      choke.chokeNever();
      return s;
    } catch (Exception e) {
      throw new RuntimeException(
          "TxBean.txNotSupported choked on " + e.getMessage());
    }
  }

  public String txNever(String s) {
    try {
      lookupChoke();
      choke.chokeNever();
      return s;
    } catch (Exception e) {
      throw new RuntimeException("TxBean.txNever choked on " + e.getMessage());
    }
  }
}
