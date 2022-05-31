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

/**
 * A comparator of {@link TopNodeCreationResult}, to sort them by the metaclass field
 */
public class TopNodeCreationResultComparator implements Comparator<TopNodeCreationResult> {

	@Override
	public int compare(final TopNodeCreationResult o1, final TopNodeCreationResult o2) {
		final String metaclass1 = o1.getMetaclassName();
		final String metaclass2 = o2.getMetaclassName();
		return metaclass1.compareTo(metaclass2);
	}

}
