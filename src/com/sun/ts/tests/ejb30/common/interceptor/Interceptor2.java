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

package com.sun.ts.tests.ejb30.common.interceptor;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

/**
 * A business method interceptor for session beans. Only one AroundInvoke method
 * may be present on the bean class or on any given interceptor class.
 */
public class Interceptor2 {

  public Interceptor2() {
    super();
  }

  @AroundInvoke
  public Object intercept2(InvocationContext ctx) throws Exception {
    Object result = null;
    int orderInChain = 2;
    result = AroundInvokeTestImpl.intercept2(ctx, orderInChain);
    return result;
  }
}
