/*
 * Copyright (C) 2010 SUNRico Inc.
 *  ------------------------------------------------------------------------------
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 *       http://www.streets.cn
 *
 *  ----------------------------------------------------------------------------------
 */

package com.facetime.core.utils;

import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**
 * Find Object in JNDI indicated paths
 * 
 * @author dzb2k9
 */
public class JNDIUtils {

	public static final String J2EE_ENV_JNDI_NAME = "java:comp/env/";
	public static final String JDBC_JNDI_NAME = J2EE_ENV_JNDI_NAME + "jdbc/";
	public static final String JMS_JNDI_NAME = J2EE_ENV_JNDI_NAME + "jms/";
	public static final String MAIL_JNDI_NAME = J2EE_ENV_JNDI_NAME + "mail/";
	public static final String URL_JNDI_NAME = J2EE_ENV_JNDI_NAME + "url/";

	private static InitialContext rootContext;

	/**
	 * Closes naming context.
	 */
	public static void close(Context ctx) {
		if (ctx != null) {
			try {
				ctx.close();
			} catch (NamingException nex) {
				// ignore
			}
			ctx = null;
		}
	}

	public static void closeRootContext() {
		close(rootContext);
	}

	/**
	 * Get root naming context (InitialContext).
	 * 
	 * @see javax.naming.InitialContext
	 */
	public static final Context getRootContext() throws NamingException {

		if (rootContext == null)
			rootContext = new InitialContext(System.getProperties());
		return rootContext;
	}

	public static final Object lookup(Context ctx, String location) throws NamingException {

		Object obj = null;
		try {
			obj = ctx.lookup(location);
		} catch (NamingException n1) {
			// java:comp/env/ObjectName to ObjectName
			if (location.indexOf("java:comp/env/") != -1)
				try {
					obj = ctx.lookup(StringUtils.replace(location, "java:comp/env/", ""));
				} catch (NamingException n2) {
					// java:comp/env/ObjectName to java:ObjectName
					obj = ctx.lookup(StringUtils.replace(location, "comp/env/", ""));
				}
			else if (location.indexOf("java:") != -1)
				try {
					obj = ctx.lookup(StringUtils.replace(location, "java:", ""));
				} catch (NamingException n2) {
					// java:ObjectName to java:comp/env/ObjectName
					obj = ctx.lookup(StringUtils.replace(location, "java:", "java:comp/env/"));
				}
			else if (location.indexOf("java:") == -1)
				try {
					obj = ctx.lookup("java:" + location);
				} catch (NamingException n2) {

					// ObjectName to java:comp/env/ObjectName

					obj = ctx.lookup("java:comp/env/" + location);
				}
			else
				throw new NamingException();
		}
		return obj;
	}

	/**
	 * Utility method for looking up an Object via JNDI. Prefixes
	 * <code>java:comp/env/</code> to <code>location</code>. If that is not
	 * found, it retries without the prefix.
	 * 
	 * @param location
	 *            JNDI location
	 */
	public static final Object lookup(String location) throws NamingException {

		return lookup(getRootContext(), location);
	}

	/**
	 * Utility method for looking up and narrowing an Object via JNDI. Used when
	 * using RMI-IIOP.
	 * 
	 * @see #lookup(String)
	 * @see #narrow(Object, Class)
	 */
	public static final Object lookup(String location, Class<?> classType) throws NamingException, RemoteException {
		return narrow(lookup(location), classType);
	}

	/**
	 * Utility method for narrowing portable object to a class
	 */
	public static final Object narrow(Object o, Class<?> classType) {
		return PortableRemoteObject.narrow(o, classType);
	}
}
