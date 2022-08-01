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
package org.eclipse.papyrus.sirius.editor.modelexplorer.internal.query;

import java.util.Collections;
import java.util.List;

import org.eclipse.papyrus.emf.facet.efacet.core.IFacetManager;
import org.eclipse.papyrus.emf.facet.efacet.core.exception.DerivedTypedElementException;
import org.eclipse.papyrus.emf.facet.query.java.core.IJavaQuery2;
import org.eclipse.papyrus.emf.facet.query.java.core.IParameterValueList2;
import org.eclipse.sirius.diagram.DSemanticDiagram;

/**
 * Query to retrieve the visible references of corresponding Sirius Diagram.
 *
 */
public class GetVisibleReferencesQuery implements IJavaQuery2<DSemanticDiagram, List<Object>> {
	/**
	 * @see org.eclipse.papyrus.emf.facet.query.java.core.IJavaQuery2#evaluate(org.eclipse.emf.ecore.EObject, org.eclipse.papyrus.emf.facet.query.java.core.IParameterValueList2, org.eclipse.papyrus.emf.facet.efacet.core.IFacetManager)
	 *
	 * @param context
	 * @param parameterValues
	 * @param facetManager
	 * @return
	 *         return empty list of visible references.
	 * @throws DerivedTypedElementException
	 */
	@Override
	public List<Object> evaluate(final DSemanticDiagram context, final IParameterValueList2 parameterValues, final IFacetManager facetManager) throws DerivedTypedElementException {
		return Collections.emptyList();
	}
}
