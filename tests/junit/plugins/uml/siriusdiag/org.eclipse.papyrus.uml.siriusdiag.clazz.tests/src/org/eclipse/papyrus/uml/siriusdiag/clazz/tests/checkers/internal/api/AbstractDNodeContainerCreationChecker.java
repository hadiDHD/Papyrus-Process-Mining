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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.viewpoint.DRepresentationElement;
import org.junit.Assert;

/**
 * Abstract checker for element represented with a {@link DNodeContainer}
 */
public abstract class AbstractDNodeContainerCreationChecker extends AbstractGraphicalNodeCreationChecker {

	/**
	 * 
	 * Constructor.
	 *
	 * @param diagram
	 *            the gmf diagram
	 * @param graphicalParent
	 *            the graphical parent
	 */
	public AbstractDNodeContainerCreationChecker(final Diagram diagram, final EObject graphicalParent) {
		super(diagram, graphicalParent);
	}

	/**
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.AbstractDNodeCreationChecker#checkCreatedElementInstanceOf(org.eclipse.sirius.viewpoint.DRepresentationElement)
	 *
	 * @param createdElementRepresentation
	 */

	@Override
	protected void checkCreatedElementInstanceOf(final DRepresentationElement createdElementRepresentation) {
		Assert.assertTrue(NLS.bind("The created element must be a DNodeContainer instead of a {0}.", createdElementRepresentation.eClass().getName()), createdElementRepresentation instanceof DNodeContainer); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.AbstractDNodeCreationChecker#checkCreatedElementMapping(org.eclipse.sirius.viewpoint.DRepresentationElement)
	 *
	 * @param createdElementRepresentation
	 */

	@Override
	protected void checkCreatedElementMapping(final DRepresentationElement createdElementRepresentation) {
		// 1. check the mapping type of the created element
		super.checkCreatedElementMapping(createdElementRepresentation);
		final DNodeContainer nodeContainer = (DNodeContainer) createdElementRepresentation;
		final List<String> compartments = getNodeCompartmentTypes();

		// 2.check the number of children
		Assert.assertEquals("The created DNodeContainer doesn't have the expected number of children", compartments.size(), nodeContainer.getOwnedDiagramElements().size()); //$NON-NLS-1$

		// 3. check the mapping type of compartments
		for (int i = 0; i < compartments.size(); i++) {
			final String compartmentMappingType = compartments.get(i);
			final DDiagramElement subElement = nodeContainer.getOwnedDiagramElements().get(i);
			Assert.assertEquals("The mapping type of compartment is not the expected one", compartmentMappingType, subElement.getMapping().getName()); //$NON-NLS-1$
		}
	}

	/**
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.AbstractDNodeCreationChecker#getNumberOfExpectedCreatedElement()
	 *
	 * @return
	 */

	@Override
	protected int getNumberOfExpectedCreatedElement() {
		// 1 node with its compartments
		return 1 + getNodeCompartmentTypes().size();
	}

	/**
	 * 
	 * @return
	 *         the list of the compartment of the node, represented by their IDs
	 */
	protected abstract List<String> getNodeCompartmentTypes();

}
