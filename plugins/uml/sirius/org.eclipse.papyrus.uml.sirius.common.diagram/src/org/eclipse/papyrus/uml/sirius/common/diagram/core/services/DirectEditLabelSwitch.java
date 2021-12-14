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

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.TemplateableElement;

/**
 * A switch that handle the label computation for each UML types for direct edit operation.
 *
 */
public class DirectEditLabelSwitch extends DisplayLabelSwitch {

	/**
	 * Qualifier separator used for direct edit.
	 */
	public static final String QUALIFIER_SEPARATOR = ","; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseClass(Class object) {
		final String templateParameters = LabelServices.INSTANCE.getTemplatedParameters(object);
		if (templateParameters != null) {
			return object.getName() + templateParameters;
		}

		return object.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String caseNamedElement(NamedElement object) {
		if (object instanceof TemplateableElement){
			final String templateParameters = LabelServices.INSTANCE
					.getTemplatedParameters((TemplateableElement)object);
			if (templateParameters != null) {
				return object.getName() + templateParameters;
			}
		}
		return object.getName();
	}

	@Override
	public String caseProperty(Property property) {
		if (!property.getQualifiers().isEmpty()) {
			String label = ""; //$NON-NLS-1$
			boolean first = true;
			final DisplayLabelSwitch displayLabelSwitch = new DisplayLabelSwitch();
			for (final Property qualifier : property.getQualifiers()) {
				if (first) {
					label += displayLabelSwitch.doSwitch(qualifier);
					first = false;
				} else {
					label += QUALIFIER_SEPARATOR;
					label += displayLabelSwitch.doSwitch(qualifier);
				}
			}
			return label;
		}
		return super.caseProperty(property);
	}
}
