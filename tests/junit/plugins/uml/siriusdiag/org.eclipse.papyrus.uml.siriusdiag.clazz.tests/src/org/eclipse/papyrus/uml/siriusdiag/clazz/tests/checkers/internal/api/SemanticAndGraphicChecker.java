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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.checkers.internal.api;

import org.eclipse.sirius.viewpoint.DRepresentationElement;

/**
 * This checker allows to group an {@link ISemanticNodeChecker} and an {@link IGraphicalNodeChecker}
 */
public abstract class SemanticAndGraphicChecker {

	/**
	 * the semantic checker
	 */
	private final ISemanticNodeChecker semanticChecker;

	/**
	 * the graphical checker
	 */
	private final IGraphicalNodeChecker graphicalChecker;

	/**
	 * Constructor.
	 *
	 */
	public SemanticAndGraphicChecker(final ISemanticNodeChecker semanticChecker, final IGraphicalNodeChecker graphicalChecker) {
		this.semanticChecker = semanticChecker;
		this.graphicalChecker = graphicalChecker;
	}

	/**
	 * 
	 * @param createdElementRepresentation
	 */
	public void validateNode(final DRepresentationElement createdElementRepresentation) {
		this.semanticChecker.validateNode(createdElementRepresentation);
		this.graphicalChecker.validateNode(createdElementRepresentation);
	}

	/**
	 * validate the semantic aspect after the Undo of the action
	 */
	public void validateAfterUndo() {
		this.semanticChecker.validateAfterUndo();
		this.graphicalChecker.validateAfterUndo();
	}

	/**
	 * validate the semantic aspect after the Redo of the action
	 */
	public void validateAfterRedo() {
		this.semanticChecker.validateAfterRedo();
		this.graphicalChecker.validateAfterRedo();
	}
}
