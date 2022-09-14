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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.viewpoint.FontFormat;
import org.eclipse.uml2.uml.BehavioralFeature;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Feature;

/**
 * This service provides styling methods
 */
public class StyleServices {
	
	/**
	 * This method returns italic style for abstract element and underline style for static element
	 * <ul>
	 * <li>isStatic is provider by {@link Feature}</li>
	 * <li>isAbstract is provided by {@link Classifier} and {@link BehavioralFeature} </li>
	 * </ul>
	 * 
	 * warning {@link BehavioralFeature} can be static AND abstract in the same time
	 * 
	 * 
	 * @param element
	 *            a UML element
	 * @return
	 *         a collection of FontFomat to apply on the label
	 */
	public Collection<FontFormat> getLabelStyle(final EObject element) {
		final List<FontFormat> fontFormats = new ArrayList<FontFormat>();
		if (element instanceof Classifier && ((Classifier) element).isAbstract()) {
			fontFormats.add(FontFormat.ITALIC_LITERAL);
		}
		if (element instanceof Feature && ((Feature) element).isStatic()) {
			fontFormats.add(FontFormat.UNDERLINE_LITERAL);
		}
		if (element instanceof BehavioralFeature && ((BehavioralFeature) element).isAbstract()) {
			fontFormats.add(FontFormat.ITALIC_LITERAL);
		}
		return fontFormats;
	}
}
