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

package com.sun.ts.tests.ejb.ee.deploy.session.stateless.enventry.scope;

import com.sun.ts.tests.ejb.ee.deploy.util.shared.enventry.scope.TestCode;
import com.sun.ts.tests.common.ejb.wrappers.StatelessWrapper;

public class BeanEJB extends StatelessWrapper {

  /**
   * Lookup a String env entry named 'name' and compare its runtime value with
   * 'ref'.
   *
   * @param name
   *          StringJNDI name of the nev entry to lookup.
   * @param ref
   *          String Reference value for this env entry (the one in DD).
   *
   * @return True if runtime value and reference matches. False otherwise.
   */
  public boolean checkEntry(String name, String ref) {
    return TestCode.checkEntry(nctx, name, ref);
  }
}
