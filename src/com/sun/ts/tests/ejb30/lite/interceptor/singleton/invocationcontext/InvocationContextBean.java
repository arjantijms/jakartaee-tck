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
package com.sun.ts.tests.ejb30.lite.interceptor.singleton.invocationcontext;

import com.sun.ts.tests.ejb30.common.invocationcontext.InvocationContextBase;
import com.sun.ts.tests.ejb30.common.invocationcontext.InvocationContextIF;
import com.sun.ts.tests.ejb30.common.invocationcontext.InvocationContextTestImpl;

import jakarta.ejb.Singleton;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

@Singleton
public class InvocationContextBean extends InvocationContextBase
    implements InvocationContextIF {
  @AroundInvoke
  private Object intercep(InvocationContext inv) throws Exception {
    InvocationContextTestImpl.interceptAll(inv);
    return inv.proceed();
  }
}
