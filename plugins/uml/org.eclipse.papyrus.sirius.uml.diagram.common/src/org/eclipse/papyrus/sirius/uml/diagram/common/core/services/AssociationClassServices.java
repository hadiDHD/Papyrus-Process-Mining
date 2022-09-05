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

import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

/**
 * This class provides methods to manipulate {@link AssociationClass}
 */
public class AssociationClassServices extends AssociationServices {

	/**
	 * A singleton instance to be accessed by other java services.
	 */
	public static final AssociationClassServices INSTANCE = new AssociationClassServices();

	private AssociationClassServices() {
		// to prevent instantiation
		super();
	}

	/**
	 * This method returns the semantic element used as source by the graphical representation of the {@link AssociationClass}
	 * 
	 * @param associationClass
	 *            an {@link AssociationClass}
	 * @return
	 *         the type of the source of the {@link AssociationClass} link or <code>null</code> when the number of memberEnds is different than 2
	 */
	public Type getSourceType(final AssociationClass associationClass) {
		return super.getSourceType(associationClass);
	}

	/**
	 * This method returns the semantic element used as target by the graphical representation of the {@link AssociationClass}
	 * 
	 * @param associationClass
	 *            an {@link AssociationClass}
	 * @return
	 *         the type of the source of the {@link AssociationClass} link or <code>null</code> when the number of memberEnds is different than 2
	 */
	public Type getTargetType(final AssociationClass associationClass) {
		return super.getTargetType(associationClass);
	}

	/**
	 * This method returns the {@link Property} used as source by the {@link AssociationClass}.
	 * 
	 * @param associationClass
	 *            an {@link AssociationClass}
	 * @return
	 *         the {@link Property} of the source of the {@link AssociationClass} link or <code>null</code> when the number of memberEnds is different than 2
	 */
	public Property getSourceProperty(final AssociationClass associationClass) {
		return super.getSourceProperty(associationClass);
	}

	/**
	 * This method returns the {@link Property} used as target by the {@link AssociationClass}.
	 * 
	 * @param associationClass
	 *            an {@link AssociationClass}
	 * @return
	 *         the {@link Property} of the source of the {@link AssociationClass} link or <code>null</code> when the number of memberEnds is different than 2
	 */
	public Property getTargetProperty(final AssociationClass associationClass) {
		return super.getTargetProperty(associationClass);
	}



	/**
	 * Service used to determinate if the selected {@link AssociationClass} source could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean canReconnectSource(final Element context, final Element newSource) {
		return newSource instanceof Class
				|| newSource instanceof Enumeration
				|| newSource instanceof Interface
				|| newSource instanceof PrimitiveType;
	}

	/**
	 * Service used to determine if the selected {@link AssociationClass} target could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean canReconnectTarget(final Element context, final Element newTarget) {
		// same conditions for source and target
		return canReconnectSource(context, newTarget);
	}

	/**
	 * This method updates the source of an {@link AssociationClass}
	 * 
	 * @param associationClass
	 *            the {@link AssociationClass}
	 * @param oldSource
	 *            the old source of the link
	 * @param newSource
	 *            the new source of link
	 */
	public void reconnectSource(final AssociationClass associationClass, final Classifier oldSource, final Classifier newSource) {
		super.reconnectSource(associationClass, oldSource, newSource);
	}

	/**
	 * This method updates the target of an {@link AssociationClass}
	 * 
	 * @param associationClass
	 *            the {@link AssociationClass}
	 * @param oldTarget
	 *            the old target of the link
	 * @param newTarget
	 *            the new target of link
	 */
	public void reconnectTarget(final AssociationClass associationClass, final Classifier oldTarget, final Classifier newTarget) {
		super.reconnectTarget(associationClass, oldTarget, newTarget);
	}

}
