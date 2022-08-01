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
package org.eclipse.papyrus.sirius.editor.modelexplorer.internal.directeditor;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.papyrus.infra.gmfdiag.extensionpoints.editors.utils.IDirectEditorsIds;
import org.eclipse.sirius.diagram.DSemanticDiagram;

/**
 * This preference initializer initializes the preferences for the direct editor of Sirius Diagram.
 *
 */
public class SiriusDiagramDirectEditorPreferenceInitializer extends AbstractPreferenceInitializer {

	/**
	 * Prefix 'papyrus.editors' to store preferences.
	 */
	private static final String PREFIX_PAPYRUS_EDITOR = IDirectEditorsIds.EDITOR_FOR_ELEMENT;

	/**
	 * The Value for the objects 'Papyrus Sirius Diagram'.
	 */
	private static final String EDITOR_NAME = "Papyrus Sirius Diagram (DSemanticDiagram) Direct Editor"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */
	public SiriusDiagramDirectEditorPreferenceInitializer() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initializeDefaultPreferences() {
		// required to get the good preference store
		IPreferenceStore store = org.eclipse.papyrus.infra.gmfdiag.extensionpoints.editors.Activator.getDefault().getPreferenceStore();
		store.setDefault(PREFIX_PAPYRUS_EDITOR + DSemanticDiagram.class.getName(), EDITOR_NAME);
	}

}
