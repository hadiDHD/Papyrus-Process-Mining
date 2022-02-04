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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.graphical;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.uml.sirius.clazz.diagram.internal.constants.MappingTypes;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.AbstractDNodeContainerCreationChecker;

/**
 * Creation checker for the graphical node CD_PrimitiveType
 */
public class CD_PrimitiveTypeCreationChecker extends AbstractDNodeContainerCreationChecker {

	/**
	 * 
	 * Constructor.
	 *
	 * @param diagram
	 *            the GMF diagram
	 * @param graphicalParent
	 *            the graphical parent of the element to create
	 */
	public CD_PrimitiveTypeCreationChecker(final Diagram diagram, final EObject graphicalParent) {
		super(diagram, graphicalParent);
	}

	/**
	 * @Override
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.AbstractDNodeContainerCreationChecker#getNodeMappingType()
	 *
	 * @return
	 */
	public String getNodeMappingType() {
		return MappingTypes.PRIMITIVETYPE_NODE_TYPE;
	}

	/**
	 * 
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.AbstractDNodeContainerCreationChecker#getNodeCompartmentTypes()
	 *
	 * @return
	 */
	public List<String> getNodeCompartmentTypes() {
		final List<String> compartments = new ArrayList<>();
		compartments.add(MappingTypes.PRIMITIVETYPE_NODE_ATTRIBUTES_COMPARTMENT_TYPE);
		compartments.add(MappingTypes.PRIMITIVETYPE_NODE_OPERATIONS_COMPARTMENT_TYPE);
		return compartments;
	}
}