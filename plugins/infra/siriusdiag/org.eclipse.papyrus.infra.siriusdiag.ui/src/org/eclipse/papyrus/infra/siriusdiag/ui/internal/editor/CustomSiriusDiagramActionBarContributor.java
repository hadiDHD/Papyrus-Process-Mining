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

package org.eclipse.papyrus.infra.siriusdiag.ui.internal.editor;

import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramActionBarContributor;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.sirius.diagram.ui.part.SiriusDiagramEditor;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;

/**
 * This action bar contributor allows to group expressions creation in a submenu
 */
public class CustomSiriusDiagramActionBarContributor extends DiagramActionBarContributor {

	@Override
	protected Class getEditorClass() {
		return SiriusDiagramEditor.class;
	}

	@Override
	protected String getEditorId() {
		return "org.eclipse.papyrus.SiriusDiagramEditorID";
	}

	@Override
	public void init(IActionBars bars, IWorkbenchPage page) {
		super.init(bars, page);
		IMenuManager menuManager = bars.getMenuManager();

		IContributionItem undoAction = bars.getMenuManager().findMenuUsingPath("undoGroup"); //$NON-NLS-1$
		if (undoAction != null) {
			menuManager.remove(undoAction);
		}
		// print preview
		IMenuManager fileMenu = bars.getMenuManager().findMenuUsingPath(IWorkbenchActionConstants.M_FILE);
		if (null != fileMenu) {
			fileMenu.remove("pageSetupAction"); //$NON-NLS-1$
		}
	}
}
