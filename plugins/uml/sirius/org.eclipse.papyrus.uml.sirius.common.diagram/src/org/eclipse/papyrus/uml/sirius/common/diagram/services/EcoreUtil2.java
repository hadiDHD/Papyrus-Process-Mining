/******************************************************************************
 * Copyright (c) 2021 CEA LIST, Artal Technologies
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.common.diagram.services;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * This class contains convenient static methods for working with EMF objects.
 */
public class EcoreUtil2 {
	public static String defaultPattern = "{1} {0}"; //$NON-NLS-1$



	/**
	 * Gets the first container with the specified class type of the specified
	 * element.
	 * 
	 * @param elt
	 *            The element to check container.
	 * @param cls
	 *            The expected container class.
	 * @return The corresponding container elsewhere <code>null</code>.
	 */
	public static EObject getFirstContainer(EObject elt, EClass cls) {
		EObject container = null;

		if (elt != null) {
			container = elt.eContainer();
		}

		if (container == null) {
			return null;
		}

		if (cls.isSuperTypeOf(container.eClass())) {
			return container;
		}

		return getFirstContainer(container, cls);
	}

	/**
	 * Gets the first container with the specified class type of the specified
	 * elements.
	 * 
	 * @param elt
	 *            The element to check container.
	 * @param cls
	 *            The expected container classes list.
	 * @return The corresponding container elsewhere <code>null</code>.
	 */
	public static EObject getFirstContainer(EObject elt, List<EClass> cls) {
		EObject container = null;

		if (elt != null) {
			container = elt.eContainer();
		}

		if (container == null) {
			return null;
		}

		for (EClass c : cls) {
			if (c.isSuperTypeOf(container.eClass())) {
				return container;
			}
		}

		return getFirstContainer(container, cls);
	}
}
