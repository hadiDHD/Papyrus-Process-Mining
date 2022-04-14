/******************************************************************************
 * Copyright (c) 2014, 2022 Obeo, CEA LIST, Artal Technologies
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Obeo - initial API and implementation
 *  Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - adaptation to integrate in Papyrus
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.common.diagram.core.services;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElementContainer;
import org.eclipse.sirius.ecore.extender.business.api.accessor.EcoreMetamodelDescriptor;
import org.eclipse.sirius.ecore.extender.business.api.accessor.MetamodelDescriptor;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.ProfileApplication;
import org.eclipse.uml2.uml.Stereotype;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Services to handle typed Setereotype concerns.
 */
public class StereotypeServices {
	/**
	 * A singleton instance to be accessed by other java services.
	 */
	public static final StereotypeServices INSTANCE = new StereotypeServices();

	/**
	 * Get all the stereotype applications according to the selected diagram.
	 *
	 * @param diagram
	 *            Current diagram
	 * @return Stereotype applications
	 */
	public Collection<Object> getAllStereotypeApplications(DDiagram diagram) {
		final Collection<Object> results = Lists.newArrayList();
		for (final DDiagramElementContainer container : diagram.getContainers()) {
			final EObject target = container.getTarget();
			if (target instanceof Element) {
				results.addAll(((Element)target).getStereotypeApplications());
			}
		}
		return results;
	}

	/**
	 * Get base class associated to a stereotype application.
	 *
	 * @param stereotypeApplication
	 *            Stereotype application
	 * @return Base class
	 */
	public Element getBaseClass(EObject stereotypeApplication) {
		return (Element)stereotypeApplication
				.eGet(stereotypeApplication.eClass().getEStructuralFeature("base_Class")); //$NON-NLS-1$
	}

	/**
	 * Get stereotype application label.
	 *
	 * @param stereotypeApplication
	 *            Stereotype application
	 * @return The stereotype name.
	 */
	public String getStereotypeApplicationLabel(EObject stereotypeApplication) {
		return stereotypeApplication.eClass().getName();
	}

}
