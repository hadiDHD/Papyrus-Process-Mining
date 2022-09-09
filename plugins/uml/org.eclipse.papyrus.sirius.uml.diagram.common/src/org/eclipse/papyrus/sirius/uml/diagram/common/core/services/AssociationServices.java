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
package org.eclipse.papyrus.sirius.uml.diagram.common.core.services;

import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Artifact;
import org.eclipse.uml2.uml.Association;
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
 * Services to handle typed Association concerns.
 *
 */
public class AssociationServices {
	/**
	 * A singleton instance to be accessed by other java services.
	 */
	public static final AssociationServices INSTANCE = new AssociationServices();

	/**
	 * Hidden constructor.
	 */
	protected AssociationServices() {
		// to prevent instantiation but allow inheritance
	}
	/**
	 * This method returns the semantic element used as source by the graphical representation of the {@link Association}
	 * 
	 * @param association
	 *            an {@link Association}
	 * @return
	 *         the type of the source of the {@link Association} link or <code>null</code> when the number of memberEnds is different than 2
	 */
	public Type getSourceType(final Association association) {
		if (association.getMemberEnds().size() == 2) {
			final Property targetproperty = association.getMemberEnds().get(1); // index 1 is the property owned by the target
			return targetproperty.getType();
		}
		return null;
	}

	/**
	 * This method returns the semantic element used as target by the graphical representation of the {@link Association}
	 * 
	 * @param association
	 *            an {@link Association}
	 * @return
	 *         the type of the source of the {@link Association} link or <code>null</code> when the number of memberEnds is different than 2
	 */
	public Type getTargetType(final Association association) {
		if (association.getMemberEnds().size() == 2) {
			final Property sourceProperty = association.getMemberEnds().get(0); // index 0 is the property owned by the source
			return sourceProperty.getType();
		}
		return null;
	}

	/**
	 * This method returns the semantic element used as source by the graphical representation of the {@link Association}
	 * 
	 * @param association
	 *            an {@link Association}
	 * @return
	 *         the {@link Property} of the source of the {@link Association} link or <code>null</code> when the number of memberEnds is different than 2
	 */
	public Property getPropertyTypedWithSourceType(final Association association) {
		if (association.getMemberEnds().size() == 2) {
			return association.getMemberEnds().get(1); // index 1 is the property owned by the target
		}
		return null;
	}

	/**
	 * This method returns the semantic element used as target by the graphical representation of the {@link Association}
	 * 
	 * @param association
	 *            an {@link Association}
	 * @return
	 *         the {@link Property} of the source of the {@link Association} link or <code>null</code> when the number of memberEnds is different than 2
	 */
	public Property getPropertyTypedWithTargetType(final Association association) {
		if (association.getMemberEnds().size() == 2) {
			return association.getMemberEnds().get(0); // index 0 is the property owned by the source
		}
		return null;
	}
	
	/**
	 * Service used to determinate if the selected {@link Association} source could be reconnected to an element.
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
	 * Service used to determine if the selected {@link Association} target could be reconnected to an element.
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
	 * This method updates the source of an {@link Association}
	 * 
	 * @param association
	 *            the {@link Association}
	 * @param oldSource
	 *            the old source of the link
	 * @param newSource
	 *            the new source of link
	 */
	public void reconnectSource(final Association association, final Classifier oldSource, final Classifier newSource) {
		final Property sourceProperty = getPropertyTypedWithTargetType(association);
		final Property targetProperty = getPropertyTypedWithSourceType(association);

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
	 * This method updates the target of an {@link Association}
	 * 
	 * @param association
	 *            the {@link Association}
	 * @param oldTarget
	 *            the old target of the link
	 * @param newTarget
	 *            the new target of link
	 */
	public void reconnectTarget(final Association association, final Classifier oldTarget, final Classifier newTarget) {
		final Property sourceProperty = getPropertyTypedWithTargetType(association);
		final Property targetProperty = getPropertyTypedWithSourceType(association);

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

	public boolean isComposite(Property property) {
		return property != null && property.isComposite();
	}

	private boolean isNavigable(Property property) {
		return property != null && property.isNavigable();
	}

	public boolean isShared(Property property) {
		return property != null && AggregationKind.SHARED_LITERAL.equals(property.getAggregation());
	}

	/**
	 * Check is an association source is composite.
	 *
	 * @param association
	 *            Association
	 * @return True if source is composite
	 */
	public boolean sourceIsComposite(Association association) {
		final Property source = AssociationServices.INSTANCE.getPropertyTypedWithSourceType(association);
		return isComposite(source);
	}

	/**
	 * Check is an association source is navigable.
	 *
	 * @param association
	 *            Association
	 * @param element
	 *            Edge element
	 * @return True if source is navigable
	 */
	public boolean sourceIsNavigable(Association association, DDiagramElement element) {
		final Property source = AssociationServices.INSTANCE.getPropertyTypedWithSourceType(association);
		return isNavigable(source);
	}

	/**
	 * Check is an association source is navigable and composite.
	 *
	 * @param association
	 *            Association
	 * @return True if source is navigable and composite
	 */
	public boolean sourceIsNavigableAndTargetIsComposite(Association association) {
		final Property source = AssociationServices.INSTANCE.getPropertyTypedWithSourceType(association);
		final Property target = AssociationServices.INSTANCE.getPropertyTypedWithTargetType(association);
		return isNavigable(source) && isComposite(target);
	}

	/**
	 * Check is an association source is navigable and shared.
	 *
	 * @param association
	 *            Association
	 * @return True if source is navigable and shared
	 */
	public boolean sourceIsNavigableAndTargetIsShared(Association association) {
		final Property source = AssociationServices.INSTANCE.getPropertyTypedWithSourceType(association);
		final Property target = AssociationServices.INSTANCE.getPropertyTypedWithTargetType(association);
		return isNavigable(source) && isShared(target);
	}

	/**
	 * Check is an association source is shared.
	 *
	 * @param association
	 *            Association
	 * @return True if source is shared
	 */
	public boolean sourceIsShared(Association association) {
		final Property source = AssociationServices.INSTANCE.getPropertyTypedWithSourceType(association);
		return isShared(source);
	}

	/**
	 * Check is an association target is composite.
	 *
	 * @param association
	 *            Association
	 * @return True if target is composite
	 */
	public boolean targetIsComposite(Association association) {
		final Property target = AssociationServices.INSTANCE.getPropertyTypedWithTargetType(association);
		return isComposite(target);
	}

	/**
	 * Check is an association target is navigable.
	 *
	 * @param association
	 *            Association
	 * @return True if target is navigable
	 */
	public boolean targetIsNavigable(Association association) {
		final Property target = AssociationServices.INSTANCE.getPropertyTypedWithTargetType(association);
		return isNavigable(target);
	}

	/**
	 * Check is an association target is navigable and composite.
	 *
	 * @param association
	 *            Association
	 * @return True if target is navigable and composite
	 */
	public boolean targetIsNavigableAndSourceIsComposite(Association association) {
		final Property target = AssociationServices.INSTANCE.getPropertyTypedWithTargetType(association);
		final Property source = AssociationServices.INSTANCE.getPropertyTypedWithSourceType(association);
		return isNavigable(target) && isComposite(source);
	}

	/**
	 * Check is an association target is navigable and shared.
	 *
	 * @param association
	 *            Association
	 * @return True if target is navigable and shared
	 */
	public boolean targetIsNavigableAndSourceIsShared(Association association) {
		final Property target = AssociationServices.INSTANCE.getPropertyTypedWithTargetType(association);
		final Property source = AssociationServices.INSTANCE.getPropertyTypedWithSourceType(association);
		return isNavigable(target) && isShared(source);
	}

	/**
	 * Check is an association target is shared.
	 *
	 * @param association
	 *            Association
	 * @return True if target is shared
	 */
	public boolean targetIsShared(Association association) {
		final Property target = AssociationServices.INSTANCE.getPropertyTypedWithTargetType(association);
		return isShared(target);
	}
}
