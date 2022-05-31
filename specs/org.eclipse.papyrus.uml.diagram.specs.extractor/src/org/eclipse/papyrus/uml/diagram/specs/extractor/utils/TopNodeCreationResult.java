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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * This class is used to save the result of the creation of an element as top node of the diagram
 */
public class TopNodeCreationResult {

	/**
	 * the name of the metaclass (label of the element in the palette)
	 */
	private final String metaclassName;

	/**
	 * map of elementType associated to the metaclassName VS capabilities (true when it can be created on the diagram as top node, false otherwise)
	 */
	private Map<IElementType, Boolean> capabilities = new HashMap<>();

	/**
	 * 
	 * Constructor.
	 *
	 * @param metaclassName
	 *            the name of the metaclass (label of the element in the palette)
	 */
	public TopNodeCreationResult(final String metaclassName) {
		this.metaclassName = metaclassName;
	}

	/**
	 * 
	 * @param elementType
	 *            an element
	 * @param isValidTopNode
	 *            <code>true</code> if this element type can be created on the diagram background
	 */
	public void registerResult(final IElementType elementType, final boolean isValidTopNode) {
		capabilities.put(elementType, Boolean.valueOf(isValidTopNode));
	}

	/**
	 * @return
	 *         the name of the represented metaclass
	 */
	public String getMetaclassName() {
		return this.metaclassName;
	}

	/**
	 * 
	 * @return
	 *         <code>true</code> if the represented metaclass can be used as topnode
	 */
	public boolean isValidTopNode() {
		return capabilities.values().contains(Boolean.TRUE);
	}

}
