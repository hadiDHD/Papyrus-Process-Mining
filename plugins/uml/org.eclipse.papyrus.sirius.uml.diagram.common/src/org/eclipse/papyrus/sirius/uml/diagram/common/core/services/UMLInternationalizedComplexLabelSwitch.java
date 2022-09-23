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
package org.eclipse.papyrus.sirius.uml.diagram.common.core.services;

import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.ValueSpecification;

/**
 * This switch builds complex label for some element (Constraint, Property, ...)
 */
public class UMLInternationalizedComplexLabelSwitch extends UMLInternationalizedNameSwitch {

	/**
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseConstraint(org.eclipse.uml2.uml.Constraint)
	 *
	 * @param constraint
	 * @return
	 */
	@Override
	public String caseConstraint(final Constraint constraint) {
		final String name = super.caseNamedElement(constraint);
		if (name != null && !name.isEmpty()) {
			final StringBuilder builder = new StringBuilder(name);
			final ValueSpecification specification = constraint.getSpecification();
			if (specification == null) {
				builder.append("missing specification"); //$NON-NLS-1$
			} else {
				final String specificationLabel = doSwitch(specification);
				if (specificationLabel != null) {
					builder.append(ILabelConstants.NL);
					builder.append(specificationLabel);
				}
			}
			return builder.toString();
		}

		return null;
	}


	/**
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseOpaqueBehavior(org.eclipse.uml2.uml.OpaqueBehavior)
	 *      ?
	 * @param object
	 * @return
	 */
	@Override
	public String caseOpaqueExpression(final OpaqueExpression opaqueExpression) {
		if (opaqueExpression.getLanguages().size() > 0 && opaqueExpression.getBodies().size() > 0) {
			final String language = opaqueExpression.getLanguages().get(0);
			final String body = opaqueExpression.getBodies().get(0);
			final StringBuilder builder = new StringBuilder();
			builder.append(ILabelConstants.OPENING_BRACE);
			builder.append(ILabelConstants.OPENING_BRACE);
			builder.append(language);
			builder.append(ILabelConstants.CLOSING_BRACE);
			builder.append(ILabelConstants.SPACE);
			builder.append(body);
			builder.append(ILabelConstants.CLOSING_BRACE);
			return builder.toString();
		}
		return null;
	}

	/**
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseProperty(org.eclipse.uml2.uml.Property)
	 *
	 * @param object
	 * @return
	 */

	@Override
	public String caseProperty(final Property property) {
		final String visibility = getVisibility(property);
		final String name = super.caseNamedElement(property);
		final Type type = property.getType();
		String typeAsString = ILabelConstants.UNDEFINED;
		if (type != null) {
			typeAsString = doSwitch(type);
		}
		final String multiplicity = super.caseMultiplicityElement(property);
		final StringBuilder builder = new StringBuilder();

		builder.append(visibility);
		if (property.isDerived()) {
			builder.append(ILabelConstants.DERIVED);
		}
		builder.append(ILabelConstants.SPACE);
		builder.append(name);
		builder.append(ILabelConstants.COLUMN);
		builder.append(ILabelConstants.SPACE);
		builder.append(typeAsString);
		builder.append(ILabelConstants.SPACE);
		builder.append(multiplicity);
		return builder.toString();
	}

	/**
	 * 
	 * @param namedElement
	 * @return
	 *         the string to use to represent the visibility of the {@link NamedElement}
	 * 
	 */
	private final String getVisibility(final NamedElement namedElement) {
		switch (namedElement.getVisibility()) {
		case PUBLIC_LITERAL:
			return ILabelConstants.PUBLIC;
		case PROTECTED_LITERAL:
			return ILabelConstants.PROTECTED;
		case PRIVATE_LITERAL:
			return ILabelConstants.PRIVATE;
		case PACKAGE_LITERAL:
			return ILabelConstants.PACKAGE;
		default:// nothing to do
		}
		return ILabelConstants.PUBLIC;// not possible
	}
}
