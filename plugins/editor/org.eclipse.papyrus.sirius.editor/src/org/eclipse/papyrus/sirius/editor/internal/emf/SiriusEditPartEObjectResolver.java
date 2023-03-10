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
package org.eclipse.papyrus.sirius.editor.internal.emf;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.papyrus.infra.emf.utils.EMFHelper;
import org.eclipse.sirius.diagram.ui.edit.api.part.IDiagramElementEditPart;
import org.eclipse.sirius.diagram.ui.edit.api.part.ISiriusEditPart;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;

/**
 * This class is used by {@link EMFHelper} to get the EObject (an UML Element in the main case) represented by a SiriusEditPart.
 * Thanks to this class, the ModelExplorer View shows the element selected in a Papyrus Sirius Diagram.
 * Thanks to this class, the Property View shows the property of the element selected in a Papyrus Sirius Diagram
 */
public final class SiriusEditPartEObjectResolver {

	private SiriusEditPartEObjectResolver() {
		// to prevent instantiation
	}

	/**
	 * 
	 * @param element
	 *            an object that we try to adapt into {@link EObject}
	 * @return the wrapped object, or the original {@code object} if it isn't our kind of EditPart
	 */
	public static final Object resolve(final Object element) {
		Object resolved = element;
		if (element instanceof IDiagramElementEditPart) {
			resolved = ((IDiagramElementEditPart) element).resolveTargetSemanticElement();
		} else if (element instanceof ISiriusEditPart && element instanceof GraphicalEditPart) { //we want GraphicalEditPart, but only the Sirius ones. We don't want make stuff for Papyrus GMF Diagrams
			//editparts provided by Sirius but they don't implements the method resolveTargetSemanticElement
			final GraphicalEditPart ep = (GraphicalEditPart) element;
			final EObject semanticElement = ((GraphicalEditPart) ep).resolveSemanticElement();
			// when we are in Sirius Diagram, we should get a DSemanticDecorator
			if (semanticElement instanceof DSemanticDecorator) {
				resolved = ((DSemanticDecorator) semanticElement).getTarget();
			}
		}
		return resolved;
	}
}
