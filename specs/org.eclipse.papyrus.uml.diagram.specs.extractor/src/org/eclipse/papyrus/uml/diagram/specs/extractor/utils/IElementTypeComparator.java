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
package org.eclipse.papyrus.uml.diagram.specs.extractor.utils;

import java.util.Comparator;

import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * a comparator used to sort {@link IElementType} by their id
 */
public class IElementTypeComparator implements Comparator<IElementType> {

	@Override
	public int compare(final IElementType o1, final IElementType o2) {
		String id1 = ""; //$NON-NLS-1$
		if (o1 != null) {
			id1 = o1.getId();
		}
		String id2 = ""; //$NON-NLS-1$
		if (o2 != null) {
			id2 = o2.getId();
		}
		return id1.compareToIgnoreCase(id2);
	}


}
