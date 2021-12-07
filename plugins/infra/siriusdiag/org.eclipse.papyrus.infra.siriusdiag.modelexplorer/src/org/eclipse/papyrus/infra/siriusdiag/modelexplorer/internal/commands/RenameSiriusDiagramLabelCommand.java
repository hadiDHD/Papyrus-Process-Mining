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
package org.eclipse.papyrus.infra.siriusdiag.modelexplorer.internal.commands;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.papyrus.infra.internationalization.utils.utils.LabelInternationalizationUtils;
import org.eclipse.papyrus.views.modelexplorer.commands.RenameLabelCommand;
import org.eclipse.sirius.diagram.DSemanticDiagram;

public class RenameSiriusDiagramLabelCommand extends RenameLabelCommand {

	/**
	 * Default constructor.
	 *
	 * @param editingDomain
	 *            The editing domain
	 * @param commandLabel
	 *            The command label
	 * @param element
	 *            The element whose label is renamed
	 * @param elementLabel
	 *            The element label
	 * @param dialogTitle
	 *            The dialog title
	 */
	public RenameSiriusDiagramLabelCommand(TransactionalEditingDomain editingDomain, String commandLabel, EObject element, String currentElementLabel, String dialogTitle) {
		super(editingDomain, commandLabel, element, currentElementLabel, dialogTitle);
	}

	/**
	 * @see org.eclipse.papyrus.views.modelexplorer.commands.RenameLabelCommand#renameLabel(org.eclipse.emf.ecore.EObject, java.lang.String)
	 *
	 * @param element
	 * @param newLabel
	 */
	@Override
	protected void renameLabel(EObject element, String newLabel) {
		if (element instanceof DSemanticDiagram) {
			LabelInternationalizationUtils.setLabel(element, newLabel, null);
		}
	}

}
