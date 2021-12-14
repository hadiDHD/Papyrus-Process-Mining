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
 *    Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.common.diagram.core.services;

import org.eclipse.uml2.uml.Type;

/**
 * Utility class used to handle information about a name and a type.
 *
 */
public class NameAndType {
	/**
	 * The name.
	 */
	private final String name;

	/**
	 * The type.
	 */
	private final Type type;

	/**
	 * Constructor.
	 *
	 * @param name
	 *            the name.
	 * @param type
	 *            the type.
	 */
	public NameAndType(String name, Type type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Get name.
	 * 
	 * @return Name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get type.
	 * 
	 * @return Type
	 */
	public Type getType() {
		return type;
	}
}
