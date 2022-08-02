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
package org.eclipse.papyrus.sirius.uml.diagram.clazz.tests.checkers.internal.api;

/**
 * This checker allows to group an {@link ISemanticNodeCreationChecker} and an {@link IGraphicalNodeCreationChecker}
 */
public class SemanticAndGraphicalCreationChecker extends SemanticAndGraphicChecker {

	/**
	 * Constructor.
	 *
	 * @param semanticChecker
	 * @param graphicalChecker
	 */
	public SemanticAndGraphicalCreationChecker(final ISemanticNodeCreationChecker semanticChecker, final IGraphicalNodeCreationChecker graphicalChecker) {
		super(semanticChecker, graphicalChecker);
	}

}
