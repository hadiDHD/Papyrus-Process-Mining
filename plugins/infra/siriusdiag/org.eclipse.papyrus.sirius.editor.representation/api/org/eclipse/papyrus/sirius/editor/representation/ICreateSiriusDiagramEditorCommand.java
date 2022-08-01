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
 *  Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *****************************************************************************/

package org.eclipse.papyrus.sirius.editor.representation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype;
import org.eclipse.sirius.diagram.DSemanticDiagram;


/**
 *
 * This interface must be implemented by the creation command registered in the architecture framework, to be able to create new Sirius Diagram
 *
 */
public interface ICreateSiriusDiagramEditorCommand {

	/**
	 *
	 * @param prototype
	 *            a view prototype (should be a PapyrusDSemanticDiagramViewPrototype)
	 * @param name
	 *            the name of the new Sirius Diagram to create
	 * @param semanticContext
	 *            the semantic context
	 * @param open
	 *            open after creation
	 * @return
	 *         the created Sirius Diagram
	 */
	public DSemanticDiagram execute(final ViewPrototype prototype, final String name, final EObject semanticContext, boolean open);

	/**
	 *
	 * @param prototype
	 *            a view prototype (should be a PapyrusDSemanticDiagramViewPrototype)
	 * @param name
	 *            the name of the new Sirius Diagram to create
	 * @param semanticContext
	 *            the semantic context
	 * @param graphicalContext
	 *            the graphical context
	 * @param open
	 *            open after creation
	 * @return
	 *         the created Sirius Diagram
	 */
	public DSemanticDiagram execute(final ViewPrototype prototype, final String name, final EObject semanticContext, final EObject graphicalContext, final boolean open);

}
