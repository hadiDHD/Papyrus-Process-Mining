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
package org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sirius.diagram.DNodeListElement;
import org.eclipse.sirius.viewpoint.DRepresentationElement;
import org.junit.Assert;

/**
 * Abstract checker for element represented with a {@link DNodeListElement}
 */
public abstract class AbstractDNodeListElementCreationChecker extends AbstractGraphicalNodeCreationChecker {

	/**
	 * Constructor.
	 *
	 * @param diagram
	 * @param graphicalParent
	 */
	public AbstractDNodeListElementCreationChecker(final Diagram diagram, final EObject graphicalParent) {
		super(diagram, graphicalParent);
	}

	/**
	 * @see org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.AbstractDNodeCreationChecker#checkCreatedElementInstanceOf(org.eclipse.sirius.viewpoint.DRepresentationElement)
	 *
	 * @param createdElementRepresentation
	 */

	@Override
	protected void checkCreatedElementInstanceOf(final DRepresentationElement createdElementRepresentation) {
		Assert.assertTrue(NLS.bind("The created element must be a DNodeListElement instead of a {0}.", createdElementRepresentation.eClass().getName()), createdElementRepresentation instanceof DNodeListElement); //$NON-NLS-1$
	}

}
