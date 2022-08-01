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

import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * This class provides the section for the elements coming from Sirius DiagramPackage metamodel
 */
public class SiriusDiagramPropertySection extends AbstractEObjectAdvancedPropertySection {

	/**
	 *
	 * Constructor.
	 *
	 */
	public SiriusDiagramPropertySection() {
		super();
	}


	/**
	 * @see org.eclipse.papyrus.sirius.editor.properties.internal.emf.AbstractEObjectAdvancedPropertySection#createPropertySource(java.lang.Object, org.eclipse.emf.edit.provider.IItemPropertySource)
	 *
	 * @param object
	 * @param itemPropertySource
	 * @return
	 */
	@Override
	public IPropertySource createPropertySource(Object object, IItemPropertySource itemPropertySource) {
		return new SiriusDiagramStructurePropertySource(object, itemPropertySource);
	}
}
