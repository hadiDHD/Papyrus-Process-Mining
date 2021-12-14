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

import org.eclipse.emf.ecore.EObject;

/**
 * Manage the diagram elements' tooltips.
 */
public class TooltipServices {
	/**
	 * A singleton instance to be accessed by other java services.
	 */
	public static final TooltipServices INSTANCE = new TooltipServices();

	/**
	 * Hidden constructor.
	 */
	private TooltipServices() {

	}

	/**
	 * Compute the tooltip of the given element.
	 *
	 * @param element
	 *            the {@link EObject} for which to retrieve a tooltip.
	 * @return the computed tooltip.
	 */
	public String computeTooltip(EObject element) {
		final TooltipSwitch tooltip = new TooltipSwitch();
		if (element != null) {
			return tooltip.doSwitch(element);
		}
		return ""; //$NON-NLS-1$
	}
}
