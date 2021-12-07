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

package org.eclipse.papyrus.infra.siriusdiag.properties.internal.emf;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

/**
 * The {@link PropertySource} used to provide a better edition way for some properties of the Sirius DiagramPackage metamodel
 */
public class SiriusDiagramStructurePropertySource extends PropertySource {

	/**
	 * Constructor.
	 *
	 * @param object
	 * @param itemPropertySource
	 */
	public SiriusDiagramStructurePropertySource(final Object object, final IItemPropertySource itemPropertySource) {
		super(object, itemPropertySource);
	}

	/**
	 * @see org.eclipse.emf.edit.ui.provider.PropertySource#createPropertyDescriptor(org.eclipse.emf.edit.provider.IItemPropertyDescriptor)
	 *
	 * @param itemPropertyDescriptor
	 * @return
	 */
	@Override
	protected IPropertyDescriptor createPropertyDescriptor(final IItemPropertyDescriptor itemPropertyDescriptor) {
		// we don't provide specific customization for the property view
		return super.createPropertyDescriptor(itemPropertyDescriptor);
	}
}
