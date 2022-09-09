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
 * This class provides the semantic drop tools ids for Sirius StateMachine Diagram odesign
 */
public class SMD_SemanticDropToolsIds {

	private SMD_SemanticDropToolsIds() {
		// to prevent instantiation
	}
	
	public static final String DROP__COMMENT__TOOL = "SemanticCommentDrop"; //$NON-NLS-1$  

	public static final String DROP__COMMENTREPRESENTATION__TOOL = "SemanticCommentRepresentationDrop"; //$NON-NLS-1$
	
	public static final String DROP__CONSTRAINT__TOOL = "SemanticConstraintDrop"; //$NON-NLS-1$
	
	public static final String DROP__REGION__TOOL = "SemanticRegionDrop"; //$NON-NLS-1$
		
	public static final String DROP__PSEUDOSTATE__TOOL = "SemanticPseudostateDrop"; //$NON-NLS-1$

	public static final String DROP__STATE__TOOL = "SemanticStateDrop"; //$NON-NLS-1$

}
