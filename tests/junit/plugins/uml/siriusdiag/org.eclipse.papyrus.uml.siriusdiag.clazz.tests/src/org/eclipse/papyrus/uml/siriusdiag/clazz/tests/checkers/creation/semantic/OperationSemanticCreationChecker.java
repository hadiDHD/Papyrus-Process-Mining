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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.creation.semantic;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.AbstractSemanticNodeCreationChecker;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.UMLPackage;
import org.junit.Assert;

/**
 * Creation checker for UML Operation
 */
public class OperationSemanticCreationChecker extends AbstractSemanticNodeCreationChecker {

	/**
	 * 
	 * Constructor.
	 *
	 * @param expectedOwner
	 *            the expected owner of the created element
	 */
	public OperationSemanticCreationChecker(final EObject expectedOwner) {
		super(expectedOwner);
	}

	/**
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.AbstractSemanticNodeCreationChecker#validateSemanticElementInstance(org.eclipse.emf.ecore.EObject)
	 *
	 * @param semanticElement
	 */

	@Override
	protected void validateSemanticElementInstance(final EObject semanticElement) {
		Assert.assertTrue(NLS.bind("The created element must be a UML Operation instead of a {0}.", semanticElement.eClass().getName()), semanticElement instanceof org.eclipse.uml2.uml.Operation); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api.AbstractSemanticNodeCreationChecker#getContainmentFeature()
	 *
	 * @return
	 */

	@Override
	protected EReference getContainmentFeature() {
		if (this.semanticOwner instanceof org.eclipse.uml2.uml.Class) {
			return UMLPackage.eINSTANCE.getClass_OwnedOperation();
		} else if (this.semanticOwner instanceof DataType) {
			return UMLPackage.eINSTANCE.getDataType_OwnedOperation();
		}if (this.semanticOwner instanceof Interface) {
			return UMLPackage.eINSTANCE.getInterface_OwnedOperation();
		}
		return null;
	}

}
