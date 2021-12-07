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
 *    Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and others
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.common.diagram.core.services;

import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.description.filter.FilterDescription;

/**
 * Service to manage filters.
 *
 */
public final class FilterService {
    /**
     * A singleton instance to be accessed by other java services.
     */
    public static final FilterService INSTANCE = new FilterService();

    /**
     * Hidden constructor.
     */
    private FilterService() {
    }

	public boolean isBenpointFilterActivated(DDiagram diagram) {
		for (FilterDescription filter : diagram.getActivatedFilters()) {
			if (filter.getName().equals("Show Bendpoint")) {
				return true;
			}
		}
		return false;
	}

	public boolean isStereotypeFilterActivated(DDiagram diagram) {
		for (FilterDescription filter : diagram.getActivatedFilters()) {
			if (filter.getName().equals("Show Stereotypes")) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isQualifiedNameFilterActivated(DDiagram diagram) {
		for (FilterDescription filter : diagram.getActivatedFilters()) {
			if (filter.getName().equals("Show Qualified Names")) {
				return true;
			}
		}
		return false;
	}
	
}
