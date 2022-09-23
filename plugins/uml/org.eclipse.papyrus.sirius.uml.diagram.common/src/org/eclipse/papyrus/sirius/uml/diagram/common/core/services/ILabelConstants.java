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
package org.eclipse.papyrus.sirius.uml.diagram.common.core.services;

/**
 * Describes the common label constants.
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

	/**
	 * The opening brace constant
	 */
	public static final String OPENING_BRACE = "{";//$NON-NLS-1$

	/**
	 * The closing brace constant
	 */
	public static final String CLOSING_BRACE = "}";//$NON-NLS-1$

	/**
	 * The opening square brace constant
	 */
	public static final String OPENING_SQUARE_BRACKET = "[";//$NON-NLS-1$

	/**
	 * The closing square brace constant
	 */
	public static final String CLOSING_SQUARE_BRACKET = "]";//$NON-NLS-1$

	/**
	 * The space char
	 */
	public static final String SPACE = " ";//$NON-NLS-1$

	/**
	 * the comma char
	 */
	public static final String COMMA = ",";//$NON-NLS-1$

	/**
	 * the column char
	 */
	public static final String COLUMN = ":";//$NON-NLS-1$

	/**
	 * the dot char
	 */
	public static final String DOT = ".";//$NON-NLS-1$

	/**
	 * the star char
	 */
	public static final String STAR = "*";//$NON-NLS-1$

	/**
	 * The label used for some undefined value
	 */
	public static final String UNDEFINED = "<Undefined>"; //$NON-NLS-1$

	/**
	 * the string used to represent public visibility
	 */
	public static final String PUBLIC = "+"; //$NON-NLS-1$

	/**
	 * the string used to represent protected visibility
	 */
	public static final String PROTECTED = "#"; //$NON-NLS-1$

	/**
	 * the string used to represent private visibility
	 */
	public static final String PRIVATE = "-"; //$NON-NLS-1$

	/**
	 * the string used to represent package visibility
	 */
	public static final String PACKAGE = "~"; //$NON-NLS-1$

	/**
	 * the string used to represent an element where the derived property is true
	 */
	public static final String DERIVED = "/"; //$NON-NLS-1$

}
