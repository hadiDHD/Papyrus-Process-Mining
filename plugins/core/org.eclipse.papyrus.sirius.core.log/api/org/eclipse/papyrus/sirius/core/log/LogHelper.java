/*******************************************************************************
 * Copyright (c) 2009, 2016, 2021 Atos Origin, CEA, Christian W. Damus, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Atos Origin - initial API and implementation
 *   Christian W. Damus (CEA) - bug 422257
 *   Christian W. Damus - bugs 465416, 485220
 *   Vincent LORENZO (CEA) - duplicated from Papyrus
 *******************************************************************************/
package org.eclipse.papyrus.sirius.core.log;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;

/**
 * A Log Helper.
 *
 * @author tszadel
 *
 */
public class LogHelper {

	/** The plugin Id. */
	private String pluginId;

	/** The plugin related to that helper. */
	private Bundle bundle;

	private boolean tracing;
	private Map<String, Boolean> traceOptions;

	/**
	 * Default Constructor.
	 * The associated plugin can be set later.
	 * If no plugin is set, use java log.
	 */
	public LogHelper() {
	}

	/**
	 * Constructor.
	 *
	 * @param activator
	 *            The activator.
	 */
	public LogHelper(Plugin activator) {
		setPlugin(activator);
	}

	/**
	 * Constructor.
	 *
	 * @param bundle
	 *            The bundle.
	 * @since 1.2
	 */
	public LogHelper(Bundle bundle) {
		setBundle(bundle);
	}

	/**
	 * Set the associated plugin.
	 * This plugin log will be used as log.
	 *
	 * @param activator
	 */
	public void setPlugin(Plugin activator) {
		setBundle(activator.getBundle());
	}

	/**
	 * Set the associated {@code bundle}.
	 * This {@code bundle}'s log will be used as log.
	 *
	 * @param bundle
	 *            the bundle
	 * @since 1.2
	 */
	public void setBundle(Bundle bundle) {
		this.pluginId = bundle.getSymbolicName();
		this.bundle = bundle;

		this.tracing = Boolean.valueOf(Platform.getDebugOption(String.format("%s/debug", pluginId))); //$NON-NLS-1$
		if (tracing) {
			this.traceOptions = new ConcurrentHashMap<>(32, 0.75f, 4);
		}
	}

	/**
	 * Log an informative message into the Eclipse log file
	 *
	 * @param message
	 *            the message to log
	 */
	public void info(String message) {
		log(message, IStatus.INFO);
	}

	/**
	 * Log a debug message into the Eclipse log file
	 *
	 * @param message
	 *            the message to log
	 */
	public void debug(String message) {
		if (isDebugEnabled()) {
			log("[DEBUG] " + message, IStatus.INFO); //$NON-NLS-1$
		}
	}

	/**
	 * Test if the platform is in debug mode.
	 *
	 * @return True if the platform is in debug mode.
	 */
	public boolean isDebugEnabled() {
		if (bundle != null) {
			return Platform.inDebugMode();
		}

		return false;
	}

	/**
	 * Queries whether the specified tracing {@code option} is enabled by the user.
	 *
	 * @param option
	 *            a tracing option, without the <tt>{@literal <bundle-id>/debug/}</tt> path prefix
	 *
	 * @return whether the tracing {@code option} is enabled
	 *
	 * @see #trace(String, String)
	 */
	public boolean isTraceEnabled(String option) {
		if (tracing) {
			final String key = String.format("%s/debug/%s", pluginId, option); //$NON-NLS-1$
			Boolean result;

			synchronized (traceOptions) {
				result = traceOptions.get(key);
				if (result == null) {
					result = Boolean.valueOf(Platform.getDebugOption(key));
					traceOptions.put(key, result);
				}
			}

			return result;
		}

		return false;
	}

	/**
	 * Prints the specified trace {@code message}, if the {@code option} is enabled by the user.
	 *
	 * @param option
	 *            the tracing option, without the <tt>{@literal <bundle-id>/debug/}</tt> path prefix
	 * @param message
	 *            the message to print
	 *
	 * @see #isTraceEnabled(String)
	 */
	public void trace(String option, String message) {
		if (isTraceEnabled(option)) {
			System.out.printf("[TRACE:%s] %s%n", option, message); //$NON-NLS-1$
		}
	}

	/**
	 * Log a message with given level into the Eclipse log file
	 *
	 * @param message
	 *            the message to log
	 * @param level
	 *            the message priority
	 */
	private void log(String message, int level) {
		log(new Status(level, pluginId, message));
	}

	/**
	 *
	 * @param status
	 */
	public void log(IStatus status) {

		if (bundle == null) {
			// TODO Do log with java ?
		} else {
			Platform.getLog(bundle).log(status);
		}
	}

	/**
	 * Log a warning message.
	 *
	 * @param e
	 *            the exception to log
	 */
	public void warn(String message) {
		log(message, IStatus.WARNING);
	}


	/**
	 * Log an exception into the Eclipse log file
	 *
	 * @param e
	 *            the exception to log
	 */
	public void error(Throwable e) {
		error("Unexpected Error", e); //$NON-NLS-1$
	}

	/**
	 * Log an exception into the Eclipse log file
	 *
	 * @param message
	 *            the message
	 * @param e
	 *            the exception to log
	 */
	public void error(String message, Throwable e) {

		Throwable t = e;
		if (e instanceof InvocationTargetException) {
			t = ((InvocationTargetException) e).getTargetException();
		}

		IStatus status;
		if (t instanceof CoreException) {
			status = ((CoreException) t).getStatus();
		} else {
			status = new Status(IStatus.ERROR, pluginId, message, e);
		}

		log(status);
	}

	/**
	 * Obtains the stack-trace description of the caller of the calling method (that is, the method that
	 * called the method using this helper method). Useful for logging warning messages etc.
	 *
	 * @return the caller of my caller, or a placeholder in case the JVM cannot provide the necessary
	 *         stack information (which is a documented possibility)
	 */
	public String getCallerMethod() {
		StackTraceElement[] stack = new Exception().fillInStackTrace().getStackTrace();
		return ((stack == null) || (stack.length < 3)) ? "<unknown caller>" : stack[2].toString(); //$NON-NLS-1$
	}
}
