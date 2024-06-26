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

package com.sun.ts.tests.ejb30.bb.session.stateless.sessioncontext.annotated;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.common.sessioncontext.SessionContextBeanBase;
import com.sun.ts.tests.ejb30.common.sessioncontext.Three1IF;
import com.sun.ts.tests.ejb30.common.sessioncontext.Three2IF;
import com.sun.ts.tests.ejb30.common.sessioncontext.ThreeLocal1IF;
import com.sun.ts.tests.ejb30.common.sessioncontext.ThreeLocal2IF;

import jakarta.annotation.Resource;
import jakarta.ejb.Local;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;

@Stateless(name = "SessionContextBean")
@Remote({ Three1IF.class, Three2IF.class })
@Local({ ThreeLocal1IF.class, ThreeLocal2IF.class })
public class SessionContextBean extends SessionContextBeanBase
// implements Three1IF, Three2IF
{

  @Resource(name = "sessionContext")
  private SessionContext sessionContext;

  @Override
  protected SessionContext getSessionContext() {
    return sessionContext;
  }

  public SessionContextBean() {
  }

  public void remove() {
  }
}
