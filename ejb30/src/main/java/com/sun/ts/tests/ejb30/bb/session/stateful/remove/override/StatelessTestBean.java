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

package com.sun.ts.tests.ejb30.bb.session.stateful.remove.override;

import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.Remove2IF;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveIF;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveLocal2IF;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveLocalIF;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.TestBeanBase;
import com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.TestIF;

import jakarta.ejb.EJB;
import jakarta.ejb.SessionContext;

//@Remote({TestIF.class})
//@Stateless(name="StatelessTestBean")
public class StatelessTestBean extends TestBeanBase implements TestIF {
  // @Resource(name="sessionContext")
  private SessionContext sessionContext;

  // @EJB(name="removeBean")
  private RemoveLocalIF removeBean;

  // @EJB(name="removeBean2")
  private RemoveLocal2IF removeBean2;

  @EJB(name = "removeBeanRemote")
  private RemoveIF removeBeanRemote;

  @EJB(name = "removeBean2Remote")
  private Remove2IF removeBean2Remote;

  public void remove() {
  }

  protected RemoveLocalIF getRemoveLocalBean() {
    // return removeBean;
    return (RemoveLocalIF) (sessionContext.lookup("removeBean"));
  }

  protected RemoveLocal2IF getRemoveLocalBean2() {
    // return removeBean2;
    return (RemoveLocal2IF) (sessionContext.lookup("removeBean2"));
  }

  protected void setRemoveLocalBean(RemoveLocalIF b) {
    this.removeBean = b;
  }

  protected void setRemoveLocalBean2(RemoveLocal2IF b) {
    this.removeBean2 = b;
  }

  protected void setRemoveRemoteBean2(Remove2IF b) {
    this.removeBean2Remote = b;
  }

  protected void setRemoveRemoteBean(RemoveIF b) {
    this.removeBeanRemote = b;
  }

  protected Remove2IF getRemoveRemoteBean2() {
    return (Remove2IF) (sessionContext.lookup("removeBean2Remote"));
  }

  protected RemoveIF getRemoveRemoteBean() {
    return (RemoveIF) (sessionContext.lookup("removeBeanRemote"));
  }
}
