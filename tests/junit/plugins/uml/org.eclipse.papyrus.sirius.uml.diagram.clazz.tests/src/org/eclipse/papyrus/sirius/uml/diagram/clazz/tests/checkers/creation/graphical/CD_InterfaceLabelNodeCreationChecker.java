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
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.AbstractDNodeListElementCreationChecker;

/**
 * Creation checker for the graphical node CD_InterfaceLabelNode
 */
public class CD_InterfaceLabelNodeCreationChecker extends AbstractDNodeListElementCreationChecker {

	/**
	 * 
	 * Constructor.
	 *
	 * @param diagram
	 *            the GMF diagram
	 * @param graphicalParent
	 *            the graphical parent of the element to create
	 */
	public CD_InterfaceLabelNodeCreationChecker(final Diagram diagram, final EObject graphicalParent) {
		super(diagram, graphicalParent);
	}

	/**
	 * @see org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.AbstractDNodeCreationChecker#getNodeMappingType()
	 *
	 * @return
	 */

	@Override
	protected String getNodeMappingType() {
		return MappingTypes.INTERFACE_LABEL_NODE_TYPE;
	}

}
