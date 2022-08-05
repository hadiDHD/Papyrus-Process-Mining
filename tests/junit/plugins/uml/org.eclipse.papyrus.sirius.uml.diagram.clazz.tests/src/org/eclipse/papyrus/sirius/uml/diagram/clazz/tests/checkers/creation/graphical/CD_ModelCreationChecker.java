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

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.internal.constants.MappingTypes;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.AbstractDNodeContainerCreationChecker;

/**
 * Creation checker for the graphical node CD_Model
 */
public class CD_ModelCreationChecker extends AbstractDNodeContainerCreationChecker {

	/**
	 * 
	 * Constructor.
	 *
	 * @param diagram
	 *            the GMF diagram
	 * @param graphicalParent
	 *            the graphical parent of the element to create
	 */
	public CD_ModelCreationChecker(Diagram diagram, final EObject graphicalParent) {
		super(diagram, graphicalParent);
	}

	/**
	 * @see org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.AbstractDNodeContainerCreationChecker#getNodeMappingType()
	 *
	 * @return
	 */
	@Override
	protected String getNodeMappingType() {
		return MappingTypes.MODEL_NODE_TYPE;
	}

	/**
	 * @see org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.AbstractDNodeContainerCreationChecker#getNodeCompartmentTypes()
	 *
	 * @return
	 */
	@Override
	protected List<String> getNodeCompartmentTypes() {
		return Collections.singletonList(MappingTypes.MODEL_NODE_PACKAGEDELEMENTS_COMPARTMENTS_TYPE);
	}

}