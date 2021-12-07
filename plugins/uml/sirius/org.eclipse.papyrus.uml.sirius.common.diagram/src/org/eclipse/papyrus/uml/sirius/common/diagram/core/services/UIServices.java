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
 *    Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and others
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.common.diagram.core.services;

import java.util.Collection;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.business.api.query.DDiagramQuery;

import com.google.common.collect.Sets;

/**
 * Services to handle UI concerns.
 *
 */
public class UIServices {
	/**
	 * A singleton instance to be accessed by other java services.
	 */
	public static final UIServices INSTANCE = new UIServices();

	/**
	 * Hidden constructor.
	 */
	private UIServices() {

	}

	/**
	 * Default height.
	 *
	 * @return The default height.
	 */
	public int defaultHeight() {
		return 10;
	}

	/**
	 * Default width.
	 *
	 * @return The default width.
	 */
	public int defaultWidth() {
		return 12;
	}

	/**
	 * Get displayed node in a diagram
	 *
	 * @param diagram
	 *            Diagram
	 * @return List of displayed semantic objects.
	 */
	public Collection<EObject> getDisplayedNodes(DDiagram diagram) {
		final Set<EObject> result = Sets.newLinkedHashSet();
		final DDiagramQuery query = new DDiagramQuery(diagram);
		for (final DDiagramElement diagramElement : query.getAllDiagramElements()) {
			result.add(diagramElement.getTarget());
		}
		return result;
	}

	/**
	 * Mark for auto size.
	 *
	 * @param any
	 *            Any
	 * @return the given auto sized object
	 */
	public EObject markForAutosize(EObject any) {
		if (any != null) {
			//any.eAdapters().add(AutosizeTrigger.AUTO_SIZE_MARKER);
		}
		return any;
	}
}
