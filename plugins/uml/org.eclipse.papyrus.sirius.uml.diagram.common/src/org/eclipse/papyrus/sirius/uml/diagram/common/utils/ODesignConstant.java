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
package org.eclipse.papyrus.sirius.uml.diagram.common.utils;

/**
 * This class contains contants used in several odesign
 */
public class ODesignConstant {

	private ODesignConstant() {
		// to prevent instantiation
	}

	/**
	 * The ID of the layer used to show/hide the applied Stereotype
	 */
	public static final String APPLIED_STEREOTYPE_LAYER_ID = "AppliedStereotypeLayer"; //$NON-NLS-1$	
	
	/**
	 * The ID of the layer used to show/hide qualified name
	 */
	public static final String QUALIFIED_NAMED_LAYER_ID = "QualifiedNameLayer"; //$NON-NLS-1$

}
