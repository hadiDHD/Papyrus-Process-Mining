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

import org.eclipse.papyrus.emf.facet.efacet.core.IFacetManager;
import org.eclipse.papyrus.emf.facet.efacet.core.exception.DerivedTypedElementException;
import org.eclipse.papyrus.emf.facet.query.java.core.IJavaQuery2;
import org.eclipse.papyrus.emf.facet.query.java.core.IParameterValueList2;
import org.eclipse.papyrus.infra.internationalization.utils.utils.LabelInternationalizationPreferencesUtils;
import org.eclipse.papyrus.infra.internationalization.utils.utils.LabelInternationalizationUtils;
import org.eclipse.sirius.diagram.DSemanticDiagram;

/**
 * Query to retrieve the label of the corresponding Sirius Diagram.
 *
 */
public class GetSiriusDiagramLabelQuery implements IJavaQuery2<DSemanticDiagram, String> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String evaluate(final DSemanticDiagram context, final IParameterValueList2 parameterValues, final IFacetManager facetManager) throws DerivedTypedElementException {
		String label = context.getName();

		if (null != context.eResource() && LabelInternationalizationPreferencesUtils.getInternationalizationPreference(context)) {
			label = LabelInternationalizationUtils.getLabelWithoutSubstract(context, true);
			label = null != label ? label : context.getName();
		}

		return context.getName();
	}
}
