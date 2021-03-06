/*
 * Copyright (c) 2015, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.javaee.resource.servlet;

import java.io.IOException;

import jakarta.annotation.Resource;
import jakarta.mail.MailSessionDefinition;
import jakarta.mail.Session;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@MailSessionDefinition(name = "java:global/env/ResourceGlobalTestServlet_MailSession", properties = {
    "test=ResourceGlobalTestServlet_MailSession" })
@WebServlet(urlPatterns = { "/resourceGlobalTest" })
public class ResourceGlobalTestServlet extends HttpServlet {

  // the value of the "test" property above
  private static final String EXPECTED = "ResourceGlobalTestServlet_MailSession";

  @Resource(lookup = "java:global/env/ResourceGlobalTestServlet_MailSession")
  Session session;

  public void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    ResourceUtil.test(response, session, EXPECTED);
  }
}
