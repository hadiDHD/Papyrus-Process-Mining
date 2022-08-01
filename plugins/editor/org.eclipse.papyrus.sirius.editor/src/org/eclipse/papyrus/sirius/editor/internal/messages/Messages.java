/******************************************************************************
 * Copyright (c) 2021 CEA LIST, Artal Technologies
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *****************************************************************************/

package org.eclipse.papyrus.sirius.editor.internal.messages;

import org.eclipse.osgi.util.NLS;

/**
 * Messages for the current pluginS
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.papyrus.sirius.editor.internal.messages.messages"; //$NON-NLS-1$
	public static String SiriusDiagramEditorInput_NoName;
	public static String SiriusDiagramEditorInput_NoDescription;
	public static String SiriusDiagramEditorInput_Tooltip;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
