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

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * This class is used to save elements than can be created inside another one
 * 
 */
public class CreateableElementInsideMe {

	/**
	 * the metaclass of the parent element (palette label)
	 */
	private final String metaclassName;

	/**
	 * key : a metaclass (palette label)
	 * value : if <code>true</code> the element can be created
	 */
	private Map<String, Boolean> resultByMetaclass = new TreeMap<>();

	/**
	 * 
	 * Constructor.
	 *
	 * @param metaclassName
	 *            the metaclassName
	 * @param allPossibleNodesMetaclasses
	 */
	public CreateableElementInsideMe(String metaclassName, Set<String> allPossibleNodesMetaclasses) {
		this.metaclassName = metaclassName;
		for (final String current : allPossibleNodesMetaclasses) {
			resultByMetaclass.put(current, Boolean.FALSE);
		}
	}

	protected String getMetaclassName() {
		return this.metaclassName;
	}

	/**
	 * 
	 * @param metaclassName
	 *            the name of the metaclass (palette label name)
	 * @param succeed
	 *            <code>true</code> if we can create an instance of the metaclass parameter inside the represented metaclass {@link CreateableElementInsideMe#metaclassName}
	 */
	public void registerCreatableElement(final String metaclassName, final boolean succeed) {
		if (!resultByMetaclass.get(metaclassName).booleanValue()) {
			// we only change false to true
			resultByMetaclass.put(metaclassName, Boolean.valueOf(succeed));
		}
	}

	/**
	 * 
	 * @return
	 *         a map of metaclass (key) with their capability (value true or false) to be created inside the represented metaclass {@link CreateableElementInsideMe#metaclassName}
	 */
	public Map<String, Boolean> getCreatableElementsList() {
		return resultByMetaclass;
	}
}
