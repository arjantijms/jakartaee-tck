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

package com.sun.ts.tests.ejb30.bb.localaccess.statelessclient;

import com.sun.ts.tests.ejb30.bb.localaccess.common.DefaultLocalIF;
import com.sun.ts.tests.ejb30.bb.localaccess.common.LocalIF;
import com.sun.ts.tests.ejb30.bb.localaccess.common.StatefulDefaultLocalIF;
import com.sun.ts.tests.ejb30.bb.localaccess.common.StatefulLocalIF;
import com.sun.ts.tests.ejb30.bb.localaccess.common.TestBeanBase;
import com.sun.ts.tests.ejb30.bb.localaccess.common.TestBeanIF;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBContext;
import jakarta.ejb.EJBs;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

@Stateless
@Remote({ TestBeanIF.class })
@EJBs({
    @EJB(name = "ejb/localStatelessRefName", beanName = "StatelessLocalBean", beanInterface = LocalIF.class),
    @EJB(name = "ejb/localStateless2RefName", beanName = "StatelessLocal2Bean", beanInterface = LocalIF.class),
    @EJB(name = "ejb/defaultLocalStatelessRefName", beanName = "StatelessDefaultLocalBean", beanInterface = DefaultLocalIF.class),
    @EJB(name = "ejb/localStatefulRefName", beanName = "StatefulLocalBean", beanInterface = StatefulLocalIF.class),
    @EJB(name = "ejb/defaultLocalStatefulRefName", beanName = "StatefulDefaultLocalBean", beanInterface = StatefulDefaultLocalIF.class) })

@TransactionManagement(TransactionManagementType.BEAN)
public class StatelessTestBean extends TestBeanBase implements TestBeanIF {
  @Resource
  private SessionContext sessionContext;

  public EJBContext getEJBContext() {
    return sessionContext;
  }

  public void remove() {
  }

}
