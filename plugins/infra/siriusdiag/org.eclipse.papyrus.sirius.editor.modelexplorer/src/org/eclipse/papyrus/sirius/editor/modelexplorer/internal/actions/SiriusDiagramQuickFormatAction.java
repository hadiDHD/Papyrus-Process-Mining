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
package org.eclipse.papyrus.sirius.editor.modelexplorer.internal.actions;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.papyrus.sirius.editor.modelexplorer.internal.commands.SiriusDiagramNameNormalizationCommand;
import org.eclipse.papyrus.infra.ui.menu.AbstractEMFParametricOnSelectedElementsAction;
import org.eclipse.papyrus.infra.ui.menu.NameNormalizationCommand;

/**
 * This class provides Sirius Diagram name quick format action.
 *
 */
public class SiriusDiagramQuickFormatAction extends AbstractEMFParametricOnSelectedElementsAction {

	/**
	 * Constructor.
	 *
	 * @param parameter
	 * @param selectedElement
	 */
	public SiriusDiagramQuickFormatAction(String parameter, List<EObject> selectedElement) {
		super(parameter, selectedElement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Command getBuildedCommand() {
		CompoundCommand cc = new CompoundCommand(NameNormalizationCommand.NAME_ACTION);
		for (EObject element : getSelection()) {
			final TransactionalEditingDomain domain = getEditingDomain();
			SiriusDiagramNameNormalizationCommand myCmd = new SiriusDiagramNameNormalizationCommand(domain, element, parameter);
			cc.append(myCmd);
		}

		if (!cc.isEmpty() && cc.canExecute()) {
			return cc;
		}
		return UnexecutableCommand.INSTANCE;
	}
}
