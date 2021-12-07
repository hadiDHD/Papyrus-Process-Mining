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
package org.eclipse.papyrus.infra.siriusdiag.modelexplorer.internal.query;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.papyrus.emf.facet.efacet.core.IFacetManager;
import org.eclipse.papyrus.emf.facet.efacet.core.exception.DerivedTypedElementException;
import org.eclipse.papyrus.emf.facet.query.java.core.IJavaQuery2;
import org.eclipse.papyrus.emf.facet.query.java.core.IParameterValueList2;
import org.eclipse.papyrus.infra.emf.utils.EMFHelper;
import org.eclipse.sirius.diagram.DSemanticDiagram;

/**
 * Query to test if the selected element is a Sirius Diagram container.
 *
 */
public class IsSiriusDiagramContainerQuery implements IJavaQuery2<EObject, Boolean> {
	/**
	 * @see org.eclipse.papyrus.emf.facet.query.java.core.IJavaQuery2#evaluate(org.eclipse.emf.ecore.EObject, org.eclipse.papyrus.emf.facet.query.java.core.IParameterValueList2, org.eclipse.papyrus.emf.facet.efacet.core.IFacetManager)
	 *
	 * @param context
	 * @param parameterValues
	 * @param facetManager
	 * @return
	 *         <code>true</code> if the selection is a Sirius Diagram container
	 * @throws DerivedTypedElementException
	 */
	@Override
	public Boolean evaluate(final EObject context, final IParameterValueList2 parameterValues, final IFacetManager facetManager) throws DerivedTypedElementException {
		Collection<Setting> settings = EMFHelper.getUsages(context);
		if (settings != null) {
			for (Setting setting : settings) {
				EObject usingElement = setting.getEObject();
				if (usingElement instanceof DSemanticDiagram) {
					final DSemanticDiagram siriusDiagram = (DSemanticDiagram) usingElement;
					final EObject container = siriusDiagram.getTarget();
					if (container == context) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
