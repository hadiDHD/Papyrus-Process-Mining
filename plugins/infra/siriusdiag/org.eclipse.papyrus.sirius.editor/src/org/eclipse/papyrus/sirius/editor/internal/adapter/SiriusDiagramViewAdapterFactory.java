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

package org.eclipse.papyrus.sirius.editor.internal.adapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.papyrus.infra.core.sasheditor.di.contentprovider.IOpenable;
import org.eclipse.papyrus.infra.core.sasheditor.di.contentprovider.IOpenableWithContainer;
import org.eclipse.sirius.diagram.DSemanticDiagram;

/**
 * Adapter factory converting Document to IOpenable.
 *
 */
public class SiriusDiagramViewAdapterFactory implements IAdapterFactory {

	/**
	 *
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 *
	 * @param adaptableObject
	 * @param adapterType
	 * @return
	 */
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adapterType == IOpenable.class) {
			if (adaptableObject instanceof DSemanticDiagram) {
				DSemanticDiagram diagram = (DSemanticDiagram) adaptableObject;
				return new IOpenableWithContainer.Openable(adaptableObject, diagram.getTarget());
			}
		}

		return null;
	}

	/**
	 *
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 *
	 * @return
	 */
	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IOpenable.class };
	}

}
