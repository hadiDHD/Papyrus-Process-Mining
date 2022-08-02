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
import org.eclipse.sirius.viewpoint.DRepresentationElement;
import org.junit.Assert;

/**
 * 
 */
public class SemanticDropChecker implements ISemanticNodeChecker {

	private final EObject droppedEObject;
	
	private final EObject semanticContainer;

	public SemanticDropChecker(final EObject droppedEObject) {
		this.droppedEObject = droppedEObject;
		this.semanticContainer = this.droppedEObject.eContainer();
	}

	/**
	 * @see org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.ISemanticNodeChecker#validateNode(org.eclipse.sirius.viewpoint.DRepresentationElement)
	 *
	 * @param createdElementRepresentation
	 */

	@Override
	public void validateNode(final DRepresentationElement createdElementRepresentation) {
		final List<EObject> semanticElements = createdElementRepresentation.getSemanticElements();
		Assert.assertEquals("The created element representation must have 1 associated semantic element", 1, semanticElements.size()); //$NON-NLS-1$

		final EObject element = semanticElements.get(0);
		Assert.assertTrue("The dropped element is not associated to the created view.", element==this.droppedEObject); //$NON-NLS-1$
		Assert.assertTrue("The semantic owner of the dropped element changed.", this.semanticContainer==element.eContainer()); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.ISemanticNodeChecker#validateAfterUndo()
	 *
	 */
	@Override
	public void validateAfterUndo() {
		// nothing to do
	}

	/**
	 * @see org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.ISemanticNodeChecker#validateAfterRedo()
	 *
	 */

	@Override
	public void validateAfterRedo() {
		// nothing to do
	}

}
