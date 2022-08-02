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
package org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.graphical;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.internal.constants.MappingTypes;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.AbstractDNodeCreationChecker;

/**
 * Creation checker for the graphical node CD_InformationItem
 */
public class CD_InformationItemCreationChecker extends AbstractDNodeCreationChecker {

	/**
	 * 
	 * Constructor.
	 *
	 * @param diagram
	 *            the GMF diagram
	 * @param graphicalParent
	 *            the graphical parent of the element to create
	 */
	public CD_InformationItemCreationChecker(final Diagram diagram, final EObject container) {
		super(diagram, container);
	}

	/**
	 * @see org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.AbstractDNodeContainerCreationChecker#getNodeMappingType()
	 *
	 * @return
	 */
	@Override
	protected String getNodeMappingType() {
		return MappingTypes.INFORMATION_ITEM_NODE_TYPE;
	}

}