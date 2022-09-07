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

import org.eclipse.uml2.uml.Artifact;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.StructuredClassifier;
import org.eclipse.uml2.uml.Type;

/**
 * This class provides methods to manipulate {@link AssociationClass}
 */
public class AssociationClassServices {

	/**
	 * A singleton instance to be accessed by other java services.
	 */
	public static final AssociationClassServices INSTANCE = new AssociationClassServices();

	private AssociationClassServices() {
		// to prevent instantiation
	}

	/**
	 * This method returns the semantic element used as source by the graphical representation of the {@link AssociationClass}
	 * 
	 * @param associationClass
	 *            an {@link AssociationClass}
	 * @return
	 *         the type of the source of the {@link AssociationClass} link
	 */
	public Type getSourceType(final AssociationClass associationClass) {
		if (associationClass.getMemberEnds().size() == 2) {
			final Property targetproperty = associationClass.getMemberEnds().get(1); // index 1 is the property owned by the target
			return targetproperty.getType();
		}
		return null;
	}

	/**
	 * This method returns the semantic element used as target by the graphical representation of the {@link AssociationClass}
	 * 
	 * @param associationClass
	 *            an {@link AssociationClass}
	 * @return
	 *         the type of the source of the {@link AssociationClass} link
	 */
	public Type getTargetType(final AssociationClass associationClass) {
		if (associationClass.getMemberEnds().size() == 2) {
			final Property sourceProperty = associationClass.getMemberEnds().get(0); // index 0 is the property owned by the source
			return sourceProperty.getType();
		}
		return null;
	}

	/**
	 * This method returns the semantic element used as source by the graphical representation of the {@link AssociationClass}
	 * 
	 * @param associationClass
	 *            an {@link AssociationClass}
	 * @return
	 *         the {@link Property} of the source of the {@link AssociationClass} link or <code>null</code> when there are more than 2 memberEnds
	 */
	public Property getPropertyTypedWithSourceType(final AssociationClass associationClass) {
		if (associationClass.getMemberEnds().size() == 2) {
			return associationClass.getMemberEnds().get(1); // index 1 is the property owned by the target
		}
		return null;
	}

	/**
	 * This method returns the semantic element used as target by the graphical representation of the {@link AssociationClass}
	 * 
	 * @param associationClass
	 *            an {@link AssociationClass}
	 * @return
	 *         the {@link Property} of the source of the {@link AssociationClass} link or <code>null</code> when there are more than 2 memberEnds
	 */
	public Property getPropertyTypedWithTargetType(final AssociationClass associationClass) {
		if (associationClass.getMemberEnds().size() == 2) {
			return associationClass.getMemberEnds().get(0); // index 0 is the property owned by the source
		}
		return null;
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
		final Property sourceProperty = getPropertyTypedWithTargetType(associationClass);
		final Property targetProperty = getPropertyTypedWithSourceType(associationClass);

		// 1. update the target Property Type and Name with the new source value
		targetProperty.setType(newSource);
		targetProperty.setName(newSource.getName().toLowerCase());

		// 2. change the owner of the source property if it is owned by the Classifier
		if (sourceProperty.getOwner() == oldSource) {
			if (newSource instanceof Artifact) {
				((Artifact) newSource).getOwnedAttributes().add(sourceProperty);
			} else if (newSource instanceof DataType) {
				((DataType) newSource).getOwnedAttributes().add(sourceProperty);
			} else if (newSource instanceof Interface) {
				((Interface) newSource).getOwnedAttributes().add(sourceProperty);
			} else if (newSource instanceof Signal) {
				((Signal) newSource).getOwnedAttributes().add(sourceProperty);
			} else if (newSource instanceof StructuredClassifier) {
				((StructuredClassifier) newSource).getOwnedAttributes().add(sourceProperty);
			}
		}
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
		final Property sourceProperty = getPropertyTypedWithTargetType(associationClass);
		final Property targetProperty = getPropertyTypedWithSourceType(associationClass);

		// 1. update the source Property Type and Name with the new target value
		sourceProperty.setType(newTarget);
		sourceProperty.setName(newTarget.getName().toLowerCase());

		// 2. change the owner of the target property if it is owned by the Classifier
		if (targetProperty.getOwner() == oldTarget) {
			if (newTarget instanceof Artifact) {
				((Artifact) newTarget).getOwnedAttributes().add(targetProperty);
			} else if (newTarget instanceof DataType) {
				((DataType) newTarget).getOwnedAttributes().add(targetProperty);
			} else if (newTarget instanceof Interface) {
				((Interface) newTarget).getOwnedAttributes().add(targetProperty);
			} else if (newTarget instanceof Signal) {
				((Signal) newTarget).getOwnedAttributes().add(targetProperty);
			} else if (newTarget instanceof StructuredClassifier) {
				((StructuredClassifier) newTarget).getOwnedAttributes().add(targetProperty);
			}
		}
	}

}
