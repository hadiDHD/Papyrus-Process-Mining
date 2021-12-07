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
import org.eclipse.papyrus.infra.ui.menu.NameNormalizationCommand;
import org.eclipse.sirius.business.api.query.DRepresentationQuery;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;

/**
 * This class provides Sirius Diagram name normalization command.
 *
 */
public class SiriusDiagramNameNormalizationCommand extends NameNormalizationCommand {

	/**
	 * Constructor.
	 *
	 * @param domain
	 *            the editing domain
	 * @param siriusDiagram
	 *            the edited Sirius Diagram template
	 * @param normalization
	 *            the parameter defining the kind of normalization
	 */
	public SiriusDiagramNameNormalizationCommand(final TransactionalEditingDomain domain, final EObject siriusDiagram, final String normalization) {
		super(domain, siriusDiagram, normalization);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doExecute() {
		if (this.source instanceof DSemanticDiagram) {
			final DSemanticDiagram doc = (DSemanticDiagram) this.source;
			final String newName = normalizeName(doc.getName(), parameter);
			DRepresentationDescriptor representationDescriptor = new DRepresentationQuery(doc).getRepresentationDescriptor();
			if (representationDescriptor != null) {
				representationDescriptor.setName(newName);
			}
		}
	}

}
