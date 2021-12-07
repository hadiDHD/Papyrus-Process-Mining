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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.papyrus.emf.facet.efacet.core.IFacetManager;
import org.eclipse.papyrus.emf.facet.efacet.core.exception.DerivedTypedElementException;
import org.eclipse.papyrus.emf.facet.query.java.core.IJavaQuery2;
import org.eclipse.papyrus.emf.facet.query.java.core.IParameterValueList2;
import org.eclipse.papyrus.infra.emf.utils.EMFHelper;
import org.eclipse.sirius.diagram.DSemanticDiagram;

/**
 * Query to retrieve the collection of all contained Sirius Diagram.
 *
 */
public class GetContainedSiriusDiagram implements IJavaQuery2<EObject, Collection<DSemanticDiagram>> {

	/**
	 * @see org.eclipse.papyrus.emf.facet.query.java.core.IJavaQuery2#evaluate(org.eclipse.emf.ecore.EObject, org.eclipse.papyrus.emf.facet.query.java.core.IParameterValueList2, org.eclipse.papyrus.emf.facet.efacet.core.IFacetManager)
	 *
	 * @param source
	 * @param parameterValues
	 * @param facetManager
	 * @return
	 *         the collection of all contained Sirius Diagram.
	 * @throws DerivedTypedElementException
	 */
	@Override
	public Collection<DSemanticDiagram> evaluate(EObject source, IParameterValueList2 parameterValues, IFacetManager facetManager) throws DerivedTypedElementException {
		List<DSemanticDiagram> siriusDiagrams = new ArrayList<>();

		Collection<Setting> settings = EMFHelper.getUsages(source);
		if (settings != null) {
			for (Setting setting : settings) {
				EObject usingElement = setting.getEObject();
				if (usingElement instanceof DSemanticDiagram) {
					DSemanticDiagram siriusDiagram = (DSemanticDiagram) usingElement;
					if (null != siriusDiagram.getTarget() && siriusDiagram.getTarget() == source && !siriusDiagrams.contains(siriusDiagram)) {
						siriusDiagrams.add(siriusDiagram);
					}
				}
			}
		}
		return siriusDiagrams;
	}

}
