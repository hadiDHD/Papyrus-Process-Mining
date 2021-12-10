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

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.util.UMLSwitch;

/**
 * A switch that handle the tooltip computation for each UML types.
 *
 */
public class TooltipSwitch extends UMLSwitch<String> implements ILabelConstants {

	@Override
	public String caseModel(Model object) {
		return object.getName();
	}

	@Override
	public String caseNamedElement(NamedElement object) {
		Element pkg = object;
		String tooltip = object.getName();
		while (pkg != null) {
			pkg = pkg.getOwner();
			if (pkg instanceof NamedElement) {
				tooltip = ((NamedElement)pkg).getName() + "::" + tooltip; //$NON-NLS-1$
			}
		}
		return tooltip;
	}
}
