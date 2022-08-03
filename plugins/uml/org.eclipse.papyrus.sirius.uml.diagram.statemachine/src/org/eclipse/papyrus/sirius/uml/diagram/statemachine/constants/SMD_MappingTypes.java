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
 * Ibtihel Khemir (CEA LIST) <ibtihel.khemir@cea.fr> - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.sirius.uml.diagram.statemachine.constants;

/**
 * This class provides mapping types for Sirius statemachine Diagram odesign
 */
public class SMD_MappingTypes {

	private SMD_MappingTypes() {
		// to prevent instantiation
	}

	public static final String COMMENT_NODE_TYPE = "SMD_Comment"; //$NON-NLS-1$
	public static final String CONSTRAINT_NODE_TYPE = "SMD_Constraint"; //$NON-NLS-1$
	public static final String CONTEXTLINK_EDGE_TYPE = "SMD_ContextLink"; //$NON-NLS-1$
	public static final String LINK_EDGE_TYPE = "SMD_Link"; //$NON-NLS-1$
	public static final String STATEMACHINE_NODE_TYPE = "SMD_StateMachine"; //$NON-NLS-1$
	public static final String STATEROOT_NODE_TYPE = "SMD_StateRoot"; //$NON-NLS-1$
	public static final String TRANSITION_EDGE_TYPE = "SMD_Transition"; //$NON-NLS-1$



}
