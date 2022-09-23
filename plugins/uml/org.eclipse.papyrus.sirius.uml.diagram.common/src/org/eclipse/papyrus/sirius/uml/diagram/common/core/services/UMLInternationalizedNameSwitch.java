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

import org.eclipse.papyrus.uml.internationalization.utils.utils.UMLLabelInternationalization;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.ElementImport;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.util.UMLSwitch;

/**
 * This switch provides the 'basic' label for each element:
 * <ul>
 * <li>internationalized name for {@link NamedElement}</li>
 * <li>the value of the field to display for element which are not {@link NamedElement}</li>
 * <ul>
 * 
 * This switch doesn't build specific label
 */
public class UMLInternationalizedNameSwitch extends UMLSwitch<String> {

	/**
	 * boolean value indicating if we need to use the internationalized label
	 */
	private boolean useQualifiedName = false;

	/**
	 * 
	 * @param useQualifiedName
	 *            if true, we will use the internationalized label
	 */
	public void useQualifiedName(final boolean useQualifiedName) {
		this.useQualifiedName = useQualifiedName;
	}

	/**
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseNamedElement(org.eclipse.uml2.uml.NamedElement)
	 *
	 * @param namedElement
	 * @return
	 */

	@Override
	public String caseNamedElement(final NamedElement namedElement) {
		if (this.useQualifiedName) {
			return UMLLabelInternationalization.getInstance().getQualifiedLabel(namedElement);
		}
		return UMLLabelInternationalization.getInstance().getLabel(namedElement);
	}

	/**
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseComment(org.eclipse.uml2.uml.Comment)
	 *
	 * @param object
	 * @return
	 */

	@Override
	public String caseComment(final Comment comment) {
		return comment.getBody();
	}

	/**
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseElementImport(org.eclipse.uml2.uml.ElementImport)
	 *
	 * @param object
	 * @return
	 */
	@Override
	public String caseElementImport(final ElementImport elementImport) {
		return elementImport.getAlias();
	}

	/**
	 * @see org.eclipse.papyrus.sirius.uml.diagram.common.core.services.UMLInternationalizedNameSwitch#caseMultiplicityElement(org.eclipse.uml2.uml.MultiplicityElement)
	 *
	 * @param multiplicityElement
	 * @return
	 */
	@Override
	public String caseMultiplicityElement(final MultiplicityElement multiplicityElement) {
		final int lower = multiplicityElement.getLower();
		final int upper = multiplicityElement.getUpper();
		final StringBuffer label = new StringBuffer();
		label.append(ILabelConstants.OPENING_SQUARE_BRACKET);
		if (lower == upper) {
			// [1..1]
			label.append(lower);
		} else if (lower == 0 && upper == -1) {
			// [0..*]
			label.append(ILabelConstants.STAR);
		} else {
			label.append(lower);
			label.append(ILabelConstants.DOT).append(ILabelConstants.DOT);
			if (upper == -1) {
				label.append(ILabelConstants.STAR);
			} else {
				label.append(upper);
			}
		}
		label.append(ILabelConstants.CLOSING_SQUARE_BRACKET);
		return label.toString();
	}

}
