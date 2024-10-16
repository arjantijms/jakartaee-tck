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

package com.sun.ts.tests.ejb30.bb.localaccess.webclient;

import com.sun.ts.tests.ejb30.bb.localaccess.common.TestBeanBase;
import com.sun.ts.tests.ejb30.bb.localaccess.common.TestBeanIF;

import jakarta.annotation.Resource;
import jakarta.ejb.EJBContext;
import jakarta.ejb.Remote;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;

@Stateless
@Remote(TestBeanIF.class)
public class StatelessTestBean extends TestBeanBase implements TestBeanIF {
    @Resource
    private SessionContext sessionContext;

    public EJBContext getEJBContext() {
        return sessionContext;
    }

    public void remove() {
    }

}
