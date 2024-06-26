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
 * @(#)Bean1EJB.java	1.12 03/05/16
 */

package com.sun.ts.tests.integration.sec.propagation;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TSNamingContextInterface;
import com.sun.ts.lib.util.TestUtil;

import jakarta.annotation.Resource;
import jakarta.ejb.EJBException;
import jakarta.ejb.SessionContext;

public class Bean1EJB {

  @Resource
  private SessionContext sessionContext;

  /**
   * Returns the name of the caller principal
   */
  public String getCallerPrincipalName() {
    return sessionContext.getCallerPrincipal().getName();
  }

  /**
   * Looks up Bean2, creates an instance, and makes a call to one of its methods
   * to ensure the principal is propagated properly.
   */
  public String getPropagatedPrincipalName() {
    String result = null;

    try {
      TSNamingContextInterface nctx = null;
      String testLookup = "java:comp/env/ejb/Bean2";

      Bean2 bean2Ref = null;

      nctx = new TSNamingContext();
      bean2Ref = (Bean2) nctx.lookup(testLookup, Bean2.class);

      result = bean2Ref.getCallerPrincipalName();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Exception: " + e.getMessage());
    }

    return result;
  }

  public String Test() {
    return "true";
  }
}
