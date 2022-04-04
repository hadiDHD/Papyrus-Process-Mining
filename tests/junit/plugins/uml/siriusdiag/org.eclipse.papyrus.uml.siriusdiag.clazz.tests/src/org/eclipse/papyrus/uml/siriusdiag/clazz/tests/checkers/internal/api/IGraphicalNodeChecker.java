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
 * Common interface for Checker in charge to validate the graphical aspect of an action on a node
 */
public interface IGraphicalNodeChecker {

	/**
	 * @param createdElementRepresentation
	 */
	void validateNode(final DRepresentationElement createdElementRepresentation);

	/**
	 * validate the graphical aspect after the Undo of the action
	 */
	void validateAfterUndo();

	/**
	 * validate the graphical aspect after the Redo of the action
	 */
	void validateAfterRedo();

}
