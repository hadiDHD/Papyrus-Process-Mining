/******************************************************************************
 * Copyright (c) 2009, 2022 Obeo, CEA LIST, Artal Technologies
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Obeo - initial API and implementation
 *  Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - adaptation to integrate in Papyrus
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.common.diagram.core.services;

/**
 * Describes the common label constants.
 *
 */
public interface ILabelConstants {

	/**
	 * New line constant.
	 */
	public static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$

	/**
	 * Open quote mark.
	 */
	public static final String ST_LEFT = "\u00AB"; //$NON-NLS-1$

	/**
	 * Close quote mark.
	 */
	public static final String ST_RIGHT = "\u00BB"; //$NON-NLS-1$

	/**
	 * Receiver element name suffix.
	 */
	public static final String RECEIVER_SUFFIX = "_receiver"; //$NON-NLS-1$

	/**
	 * Sender element name suffix.
	 */
	public static final String SENDER_SUFFIX = "_sender"; //$NON-NLS-1$

	/**
	 * Finish element name suffix.
	 */
	public static final String FINISH_SUFFIX = "_finish"; //$NON-NLS-1$

	/**
	 * Start element name suffix.
	 */
	public static final String START_SUFFIX = "_start"; //$NON-NLS-1$

	/**
	 * The guard suffix constant.
	 */
	public static final String GUARD_SUFFIX = "_guard"; //$NON-NLS-1$
	
}
