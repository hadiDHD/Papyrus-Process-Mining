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
package org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.creation.semantic;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.AbstractSemanticNodeCreationChecker;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.Assert;

/**
 * Creation checker for UML Constraint
 */
public class ConstraintSemanticCreationChecker extends AbstractSemanticNodeCreationChecker {

	/**
	 * Constructor.
	 *
	 * @param expectedOwner
	 */
	public ConstraintSemanticCreationChecker(final EObject expectedOwner) {
		super(expectedOwner);
	}

	/**
	 * @see org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.AbstractSemanticNodeCreationChecker#validateSemanticElementInstance(org.eclipse.emf.ecore.EObject)
	 *
	 * @param createdElement
	 */
	@Override
	protected void validateSemanticElementInstance(final EObject semanticElement) {
		Assert.assertTrue(NLS.bind("The created element must be a UML Constraint instead of a {0}.", semanticElement.eClass().getName()), semanticElement instanceof Constraint); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api.AbstractSemanticNodeCreationChecker#getContainmentFeature()
	 *
	 * @return
	 */
	@Override
	protected EReference getContainmentFeature() {
		return UMLPackage.eINSTANCE.getNamespace_OwnedRule();
	}

}