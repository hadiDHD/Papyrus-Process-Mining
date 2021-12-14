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
 *    Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.common.diagram;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.sirius.diagram.ui.edit.api.part.AbstractDDiagramEditPart;
import org.eclipse.sirius.diagram.ui.edit.api.part.IAbstractDiagramNodeEditPart;
import org.eclipse.sirius.diagram.ui.tools.api.editor.tabbar.AbstractTabbarContributor;

/**
 * The Class PapyrusSiriusTabbarContributor.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
public class PapyrusSiriusTabbarContributor extends AbstractTabbarContributor {

	/** The diagram contribution items. */
	private ArrayList<IContributionItem> diagramContributionItems;

	/** The diagram element contribution items. */
	private ArrayList<IContributionItem> diagramElementContributionItems;

	/**
	 * Gets the contribution items.
	 *
	 * @param selection the selection
	 * @param part the part
	 * @param manager the manager
	 * @return the contribution items
	 * @see org.eclipse.sirius.diagram.ui.tools.api.editor.tabbar.ITabbarContributor#getContributionItems(org.eclipse.jface.viewers.ISelection, org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart, org.eclipse.jface.action.ToolBarManager)
	 */

	@Override
	public List<IContributionItem> getContributionItems(ISelection selection, IDiagramWorkbenchPart part, ToolBarManager manager) {
		List<IContributionItem> contributionItems = new ArrayList<IContributionItem>();
		if (selection instanceof IStructuredSelection) {
			Object firstElement = ((IStructuredSelection) selection).getFirstElement();
			if (firstElement instanceof AbstractDDiagramEditPart) {
				contributionItems = getDiagramContributionItem(part, manager);
			} else if (firstElement instanceof IAbstractDiagramNodeEditPart) {
				contributionItems = getDiagramElementContributionItem(part, manager);
			}
		}
		return contributionItems;
	}

	/**
	 * Accept.
	 *
	 * @param selection the selection
	 * @return true, if successful
	 * @see org.eclipse.sirius.diagram.ui.tools.api.editor.tabbar.ITabbarContributor#accept(org.eclipse.jface.viewers.ISelection)
	 */

	@Override
	public boolean accept(ISelection selection) {
		boolean accept = false;
		if (selection == null) {
			accept = true;
		} else if (selection instanceof IStructuredSelection) {
			Object firstElement = ((IStructuredSelection) selection).getFirstElement();
			if (firstElement instanceof AbstractDDiagramEditPart || firstElement instanceof IAbstractDiagramNodeEditPart) {
				accept = true;
			}
		}
		return accept;
	}

	/**
	 * Gets the contribution items.
	 *
	 * @param part the part
	 * @param manager the manager
	 * @return the contribution items
	 * @see org.eclipse.sirius.diagram.ui.tools.api.editor.tabbar.ITabbarContributor#getContributionItems(org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart, org.eclipse.jface.action.ToolBarManager)
	 */

	@Override
	public List<IContributionItem> getContributionItems(IDiagramWorkbenchPart part, ToolBarManager manager) {
		return getDiagramContributionItem(part, manager);
	}

	/**
	 * Gets the diagram contribution item.
	 *
	 * @param part
	 *            the part
	 * @param manager
	 *            the manager
	 * @return the diagram contribution item
	 */
	private List<IContributionItem> getDiagramContributionItem(IDiagramWorkbenchPart part, ToolBarManager manager) {
		if (diagramContributionItems == null) {
			diagramContributionItems = new ArrayList<IContributionItem>();
//			diagramContributionItems.add(createArrangeMenuManager(part));
//			diagramContributionItems.add(createSelectMenuManager());
			diagramContributionItems.add(createLayerContribution(part, manager));
//			diagramContributionItems.add(createZoomInContribution(part));
//			diagramContributionItems.add(createZoomOutContribution(part));
//			diagramContributionItems.add(createZoomContribution(part));
			diagramContributionItems.add(createSelectPinnedElementsContribution(part));
			diagramContributionItems.add(createSelectHiddenElementsContribution(part));
			diagramContributionItems.add(createFilterContribution(part, manager));
			diagramContributionItems.add(createPasteFormatContribution(part));
			diagramContributionItems.add(createRefreshContribution());
			diagramContributionItems.add(createSaveAsImageContributionItem());
			diagramContributionItems.add(createModeMenuManagerContributionItem(part));
			diagramContributionItems.add(createCopyAppearancePropertiesContribution(part));
			diagramContributionItems.add(createCopyFormatContribution(part));
		}
		diagramElementContributionItems=null;
		return diagramContributionItems;
	}

	/**
	 * Gets the diagram element contribution item.
	 *
	 * @param part
	 *            the part
	 * @param manager
	 *            the manager
	 * @return the diagram element contribution item
	 */
	private List<IContributionItem> getDiagramElementContributionItem(IDiagramWorkbenchPart part, ToolBarManager manager) {
		if (diagramElementContributionItems == null) {
			diagramElementContributionItems = new ArrayList<IContributionItem>();
			diagramElementContributionItems.add(createArrangeMenuManager(part));
			diagramElementContributionItems.add(createAlignMenuManager());
			diagramElementContributionItems.add(createHideElementLabelContribution(part));
			diagramElementContributionItems.add(createHideElementContribution(part));
			diagramElementContributionItems.add(createDeleteFromDiagramContribution(part));
			diagramElementContributionItems.add(createDeleteFromModelContribution(part));
			IContributionItem pinElementContributionItem = createPinElementContribution(part);
			diagramElementContributionItems.add(pinElementContributionItem);
			diagramElementContributionItems.add(createUnPinElementContribution(part, pinElementContributionItem));
			diagramElementContributionItems.add(createFontColorContribution(part));
			diagramElementContributionItems.add(createPasteFormatContribution(part));
			diagramElementContributionItems.add(createBoldFontStyleContribution(part));
			diagramElementContributionItems.add(createItalicFontStyleContribution(part));
			diagramElementContributionItems.add(createFontDialogContribution(part));
			diagramElementContributionItems.add(createFillColorContribution(part));
			diagramElementContributionItems.add(createLineColorPropertyContribution(part));
			diagramElementContributionItems.add(createResetStylePropertyContribution(part));
			diagramElementContributionItems.add(createSetStyleToWorkspaceImageContribution(part));
			diagramElementContributionItems.add(createSaveAsImageContributionItem());
			diagramElementContributionItems.add(createDistributeContribution());
			diagramElementContributionItems.add(createModeMenuManagerContributionItem(part));
			diagramElementContributionItems.add(createRouterContribution());
			diagramElementContributionItems.add(createCopyAppearancePropertiesContribution(part));
			diagramElementContributionItems.add(createCopyFormatContribution(part));
			diagramElementContributionItems.add(createSizeBothContribution(part));
			diagramElementContributionItems.add(createAutoSizeContribution(part));
		}
		diagramContributionItems=null;
		return diagramElementContributionItems;
	}
}
