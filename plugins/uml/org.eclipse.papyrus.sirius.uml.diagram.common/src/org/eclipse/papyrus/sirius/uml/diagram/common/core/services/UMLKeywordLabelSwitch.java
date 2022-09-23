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

import org.eclipse.uml2.uml.Abstraction;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ElementImport;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.InformationItem;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Substitution;
import org.eclipse.uml2.uml.util.UMLSwitch;

/**
 * This switch is used to get the Keyword of UML element
 */
public class UMLKeywordLabelSwitch extends UMLSwitch<String> {

	/**
	 * Open quote mark.
	 */
	private static final String ST_LEFT = ILabelConstants.ST_LEFT;

	/**
	 * Close quote mark.
	 */
	private static final String ST_RIGHT = ILabelConstants.ST_RIGHT;

	private static final String INFORMATION_ITEM_KEYWORD = "Information"; //$NON-NLS-1$

	private static final String METACLASS_KEYWORD = "Metaclass"; //$NON-NLS-1$

	private static final String PRIMITIVE_TYPE_KEYWORD = "Primitive"; //$NON-NLS-1$

	private static final String SUBSTITUTION_KEYWORD = "substitute"; //$NON-NLS-1$ , lower case for Substitution link

	/**
	 * 
	 * @param element
	 *            a UML element
	 * @return
	 *         its metaclass name or <code>null</code> when the parameter is <code>null</code>
	 */
	private String getMetaclassName(final Element element) {
		if (element != null) {
			return element.eClass().getName();
		}
		return null;
	}

	/**
	 * 
	 * @param keyword
	 *            the keyword for the element
	 * @return
	 *         the keyword between quote or <code>null</code>
	 */
	private String buildKeywordString(final String keyword) {
		if (keyword == null) {
			return null;
		}
		final StringBuilder builder = new StringBuilder();
		builder.append(ST_LEFT);
		builder.append(keyword);
		builder.append(ST_RIGHT);
		return builder.toString();
	}

	/**
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseAbstraction(org.eclipse.uml2.uml.Abstraction)
	 *
	 * @param abstraction
	 * @return
	 */
	@Override
	public String caseAbstraction(final Abstraction abstraction) {
		return buildKeywordString(getMetaclassName(abstraction).toLowerCase());// to lower case for Abstraction link
	}

	/**
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseClass(org.eclipse.uml2.uml.Class)
	 *
	 * @param clazz
	 * @return
	 */
	@Override
	public String caseClass(final Class clazz) {
		if (clazz.isMetaclass()) {
			return buildKeywordString(METACLASS_KEYWORD);
		}
		return null;
	}

	/**
	 * 
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseComponent(org.eclipse.uml2.uml.Component)
	 *
	 * @param component
	 * @return
	 */
	@Override
	public String caseComponent(final Component component) {
		return buildKeywordString(getMetaclassName(component));
	}


	/**
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseDataType(org.eclipse.uml2.uml.DataType)
	 *
	 * @param datatype
	 * @return
	 */
	@Override
	public String caseDataType(final DataType datatype) {
		return buildKeywordString(getMetaclassName(datatype));
	}

	/**
	 * 
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseElementImport(org.eclipse.uml2.uml.ElementImport)
	 *
	 * @param elementImport
	 * @return
	 */
	@Override
	public String caseElementImport(final ElementImport elementImport) {
		return buildKeywordString(getMetaclassName(elementImport).toLowerCase());// to lower case for ElementImport link
	}

	/**
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseEnumeration(org.eclipse.uml2.uml.Enumeration)
	 *
	 * @param enumeration
	 * @return
	 */
	@Override
	public String caseEnumeration(final Enumeration enumeration) {
		return buildKeywordString(getMetaclassName(enumeration));
	}

	/**
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseInformationItem(org.eclipse.uml2.uml.InformationItem)
	 *
	 * @param informationItem
	 * @return
	 */

	@Override
	public String caseInformationItem(final InformationItem informationItem) {
		return buildKeywordString(INFORMATION_ITEM_KEYWORD);
	}

	/**
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseInterface(org.eclipse.uml2.uml.Interface)
	 *
	 * @param interface_
	 * @return
	 */
	@Override
	public String caseInterface(final Interface interface_) {
		return buildKeywordString(getMetaclassName(interface_));
	}


	/**
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#casePrimitiveType(org.eclipse.uml2.uml.PrimitiveType)
	 *
	 * @param primitiveType
	 * @return
	 */
	@Override
	public String casePrimitiveType(final PrimitiveType primitiveType) {
		return buildKeywordString(PRIMITIVE_TYPE_KEYWORD);
	}

	/**
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseSignal(org.eclipse.uml2.uml.Signal)
	 *
	 * @param signal
	 * @return
	 */
	@Override
	public String caseSignal(final Signal signal) {
		return buildKeywordString(getMetaclassName(signal));
	}

	/**
	 * @see org.eclipse.uml2.uml.util.UMLSwitch#caseSubstitution(org.eclipse.uml2.uml.Substitution)
	 *
	 * @param substitution
	 * @return
	 */
	@Override
	public String caseSubstitution(final Substitution substitution) {
		return buildKeywordString(SUBSTITUTION_KEYWORD);
	}
	
}
