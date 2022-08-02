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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DNodeContainer;
import org.eclipse.sirius.diagram.DNodeList;

/**
 * Utility class providing utilities methods to manipulate Sirius graphical elements
 */
public class GraphicalOwnerUtils {

	/**
	 * 
	 * Constructor.
	 *
	 */
	private GraphicalOwnerUtils() {
		// to prevent instantiation
	}

	/**
	 * 
	 * @return
	 *         the number of owned children in the graphical parent
	 */
	public static final int getGraphicalOwnerChildrenSize(final EObject graphicalParent) {
		return getChildren(graphicalParent).size();
	}

	/**
	 * 
	 * @param graphicalParent
	 *            the graphical parent. It should be an element from Sirius Diagram metamodel
	 * @return
	 *         the list of owned element
	 */
	public static final List<? extends EObject> getChildren(final EObject graphicalParent) {
		if (graphicalParent instanceof DDiagram) {
			return ((DDiagram) graphicalParent).getOwnedDiagramElements();
		} else if (graphicalParent instanceof DNodeContainer) {
			return ((DNodeContainer) graphicalParent).getOwnedDiagramElements();
		} else if (graphicalParent instanceof DNodeList) {
			return ((DNodeList) graphicalParent).getOwnedElements();
		}
		throw new UnsupportedOperationException(NLS.bind("The type {0} is not yet supported", graphicalParent.eClass().getName())); //$NON-NLS-1$

	}
}
