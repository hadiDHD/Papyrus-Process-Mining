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

package org.eclipse.papyrus.sirius.editor.internal.editor;

import org.eclipse.papyrus.infra.ui.editor.IMultiDiagramEditor;
import org.eclipse.sirius.diagram.ui.part.SiriusDiagramActionBarContributor;
import org.eclipse.ui.IEditorPart;

/**
 * Customized actionbar contributor for Papyrus Sirius nested editor
 */
public class CustomSiriusDiagramActionBarContributor extends SiriusDiagramActionBarContributor {

	/**
	 * 
	 * @see org.eclipse.sirius.diagram.ui.part.SiriusDiagramActionBarContributor#getEditorClass()
	 *
	 * @return
	 */
	@Override
	protected Class<?> getEditorClass() {
		//it doesn't seem required to return NestedSiriusDiagramViewEditor.class
		return super.getEditorClass(); 
	}

	/**
	 * 
	 * @see org.eclipse.sirius.diagram.ui.part.SiriusDiagramActionBarContributor#getEditorId()
	 *
	 * @return
	 */
	@Override
	protected String getEditorId() {
		//defining our own ID for the editor will break actions, because this ID is used by Sirius action to get the instance of the editor
		return super.getEditorId(); 
	}

	/**
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#setActiveEditor(org.eclipse.ui.IEditorPart)
	 *
	 * @param editor
	 */
	@Override
	public void setActiveEditor(IEditorPart editor) {
		if (editor instanceof IMultiDiagramEditor && ((IMultiDiagramEditor) editor).getActiveEditor() != null) {
			final IEditorPart ep = ((IMultiDiagramEditor) editor).getActiveEditor();
			super.setActiveEditor(ep);
		} else {
			super.setActiveEditor(editor);
		}
	}
}
