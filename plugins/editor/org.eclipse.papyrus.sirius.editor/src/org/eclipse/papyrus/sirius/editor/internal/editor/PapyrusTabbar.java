/*****************************************************************************
 * Copyright (c) 2022 CEA LIST
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Vincent Lorenzo (CEA LIST) <vincent.lorenzo@cea.fr> - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.sirius.editor.internal.editor;

import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.papyrus.infra.ui.editor.IMultiDiagramEditor;
import org.eclipse.sirius.diagram.ui.tools.internal.editor.tabbar.Tabbar;
import org.eclipse.sirius.diagram.ui.tools.internal.editor.tabbar.TabbarToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Papyrus Tabbar. This customization is required to access to the Sirius editor.
 */
@SuppressWarnings("restriction")
public class PapyrusTabbar extends Tabbar {

	/**
	 * Constructor.
	 *
	 * @param parent
	 * @param part
	 */
	public PapyrusTabbar(Composite parent, IDiagramWorkbenchPart part) {
		super(parent, part);
	}

	/**
	 * @see org.eclipse.sirius.diagram.ui.tools.internal.editor.tabbar.Tabbar#createTabbarToolBarManager(org.eclipse.swt.widgets.ToolBar, org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart)
	 *
	 * @param toolbar
	 * @param part
	 * @return
	 */

	@Override
	protected TabbarToolBarManager createTabbarToolBarManager(ToolBar toolbar, IDiagramWorkbenchPart part) {
		return new TabbarToolBarManager(toolbar, part) {

			/**
			 * @see org.eclipse.sirius.diagram.ui.tools.internal.editor.tabbar.TabbarToolBarManager#getActivePart(org.eclipse.ui.IWorkbenchPage)
			 *
			 * @param activePage
			 * @return
			 */
			
			@Override
			protected IWorkbenchPart getActivePart(IWorkbenchPage activePage) {
				if(part instanceof IMultiDiagramEditor) {
					return ((IMultiDiagramEditor) part).getActiveEditor();
				}
				return part;
			}

		};
	}

	/**
	 * @see org.eclipse.sirius.diagram.ui.tools.internal.editor.tabbar.Tabbar#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 *
	 * @param partSelected
	 * @param selection
	 */

	@Override
	public void selectionChanged(IWorkbenchPart partSelected, ISelection selection) {
		if (partSelected instanceof IMultiDiagramEditor) {
			final IEditorPart part = ((IMultiDiagramEditor) partSelected).getActiveEditor();
			super.selectionChanged(part, selection);
		} else {
			super.selectionChanged(partSelected, selection);
		}

	}
}
