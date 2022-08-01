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
 *  Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *****************************************************************************/

package org.eclipse.papyrus.sirius.editor.properties.internal.emf;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.sirius.diagram.DiagramPackage;

/**
 * Property section dedicated to element coming from Sirius DiagramPackage
 */
public class SiriusDiagramPackageSectionFilter implements IFilter {

	/**
	 *
	 * Constructor.
	 *
	 */
	public SiriusDiagramPackageSectionFilter() {
		// nothing to do
	}

	/**
	 *
	 * @see org.eclipse.jface.viewers.IFilter#select(java.lang.Object)
	 *
	 * @param toTest
	 * @return
	 */
	@Override
	public boolean select(final Object toTest) {
		final EObject eobject = org.eclipse.papyrus.infra.emf.utils.EMFHelper.getEObject(toTest);
		if (false == eobject instanceof EObject) {
			return false;
		}
		final EPackage epackage = eobject.eClass().getEPackage();
		return epackage == DiagramPackage.eINSTANCE;
	}

}
