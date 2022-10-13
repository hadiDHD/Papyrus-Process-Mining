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

import java.util.Iterator;

import org.eclipse.papyrus.sirius.uml.diagram.common.utils.ODesignConstant;
import org.eclipse.papyrus.uml.internationalization.utils.utils.UMLLabelInternationalization;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;

/**
 * This service is in charge to provides the label to use for each element.
 * The returned label is composed like this:
 * <ul>
 * <li>keyword + applied stereotype + label of the element itself</li>
 * <li>often the label of the element is its name (potentially internationalized), but sometimes it is more complex, (like for UML Property)</li>
 * </ul>
 */
public class UMLLabelService {

	/**
	 * This switch provides the keyword for each UML Element
	 */
	private final UMLKeywordLabelSwitch keywordProvider = new UMLKeywordLabelSwitch();

	/**
	 * This switch provides the label to use for each element.
	 * This label is internationalized (according to the Papyrus editor internationalization configuration).
	 * This provider is in charge to build complex label (like for UML Property for example)
	 */
	private final UMLInternationalizedComplexLabelSwitch nameProvider = new UMLInternationalizedComplexLabelSwitch();

	/**
	 * 
	 * @param diagram
	 *            the current diagram
	 * @return
	 *         <code>true</code> if we need to use the Qualified Name of elements in the diagram
	 */
	private boolean isShowingQualifiedName(final DDiagram diagram) {
		long count = diagram.getActivatedLayers().stream().filter(layer -> ODesignConstant.QUALIFIED_NAMED_LAYER_ID.equals(layer.getName())).count();
		return count == 1;
	}

	/**
	 * 
	 * @param diagram
	 *            the current diagram
	 * @return
	 *         <code>true</code> if we need to use the Qualified Name of elements in the diagram
	 */
	private boolean isShowingAppliedStereotype(final DDiagram diagram) {
		long count = diagram.getActivatedLayers().stream().filter(layer -> ODesignConstant.APPLIED_STEREOTYPE_LAYER_ID.equals(layer.getName())).count();
		return count == 1;
	}

	/**
	 * This method build a single line label. These labels are used for Nodes inside List Compartment. In this case, the qualified name is never used
	 * 
	 * @param element
	 *            the element for which we want a label
	 * @param diagram
	 *            the current diagram
	 * @return
	 *         a single line label
	 */
	public String buildSingleLineLabel(final Element element, final DDiagram diagram) {
		return buildLabel(element, diagram, false, false); // we don't show qualified name in single line label
	}

	/**
	 * This method build a multiline label. These labels are used for Nodes and Edges, excepted inside List Compartment.
	 * 
	 * @param element
	 *            the element for which we want a label
	 * @param diagram
	 *            the current diagram
	 * @return
	 *         a multi line label
	 */
	public String buildMultilineLabel(final Element element, final DDiagram diagram) {
		return buildLabel(element, diagram, true, isShowingQualifiedName(diagram));
	}


	/**
	 * 
	 * @param element
	 *            a UML Element
	 * @return
	 *         the String representing the applied stereotype, or <code>null</code>
	 */
	private final String buildStereotypeLabel(final Element element) {
		final StringBuilder builder = new StringBuilder();
		final Iterator<Stereotype> iter = element.getAppliedStereotypes().iterator();
		if (iter.hasNext()) {
			builder.append(ILabelConstants.ST_LEFT);
		}
		while (iter.hasNext()) {
			final Stereotype current = iter.next();
			builder.append(UMLLabelInternationalization.getInstance().getKeyword(current));
			if (iter.hasNext()) {
				builder.append(ILabelConstants.COMMA);
				builder.append(ILabelConstants.SPACE);
			}
		}
		if (builder.length() > 0) {
			builder.append(ILabelConstants.ST_RIGHT);
		}
		return builder.toString();
	}

	/**
	 * 
	 * @param element
	 *            the UML element for which we want a label
	 * @param diagram
	 *            the current diagram
	 * @param multiline
	 *            boolean indicating if we want a multiline label or not
	 * @param useQualifiedName
	 *            boolean indicating if we must use the qualified name to build the label
	 * @return
	 *         the label to display in the diagram
	 */
	private final String buildLabel(final Element element, final DDiagram diagram, final boolean multiline, final boolean useQualifiedName) {
		this.nameProvider.useQualifiedName(useQualifiedName);
		final String keyword = this.keywordProvider.doSwitch(element);

		final String stereotypeLabel = isShowingAppliedStereotype(diagram) ? buildStereotypeLabel(element) : null;

		final String name = this.nameProvider.doSwitch(element);
		final StringBuilder builder = new StringBuilder();
		if (keyword != null && !keyword.isEmpty()) {
			builder.append(keyword);
			if (multiline) {
				builder.append(ILabelConstants.NL);
			} else {
				builder.append(ILabelConstants.SPACE);
			}
		}
		if (stereotypeLabel != null && !stereotypeLabel.isEmpty()) {
			builder.append(stereotypeLabel);
			if (multiline) {
				builder.append(ILabelConstants.NL);
			} else {
				builder.append(ILabelConstants.SPACE);
			}
		}
		if (name != null && !name.isEmpty()) {
			builder.append(name);
		}

		return builder.toString();
	}

}
