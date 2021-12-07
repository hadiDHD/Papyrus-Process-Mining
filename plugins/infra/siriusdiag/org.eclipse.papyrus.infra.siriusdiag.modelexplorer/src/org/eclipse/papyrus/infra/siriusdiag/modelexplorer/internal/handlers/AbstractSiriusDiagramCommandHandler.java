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
package org.eclipse.papyrus.infra.siriusdiag.modelexplorer.internal.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.infra.emf.utils.EMFHelper;
import org.eclipse.papyrus.infra.ui.command.AbstractCommandHandler;
import org.eclipse.sirius.diagram.DSemanticDiagram;

/**
 * This abstract handler provides the method to select Sirius Diagram.
 *
 */
public abstract class AbstractSiriusDiagramCommandHandler extends AbstractCommandHandler {

	/**
	 * Get the list of selected Sirius Diagram.
	 *
	 * @return the list of Sirius Diagram
	 */
	public List<DSemanticDiagram> getSelectedDSemanticDiagrams() {
		final List<DSemanticDiagram> siriusDiagrams = new ArrayList<>();

		// Get first element if the selection is an IStructuredSelection
		final Iterator<?> iterator = getSelectedElements().iterator();

		while (iterator.hasNext()) {
			Object current = iterator.next();
			// Get the sirius diagram template object (Facet Element) by IAdaptable mechanism
			EObject diagram = EMFHelper.getEObject(current);
			if (diagram instanceof DSemanticDiagram) {
				siriusDiagrams.add((DSemanticDiagram) diagram);
			}
		}

		return siriusDiagrams;
	}

}
