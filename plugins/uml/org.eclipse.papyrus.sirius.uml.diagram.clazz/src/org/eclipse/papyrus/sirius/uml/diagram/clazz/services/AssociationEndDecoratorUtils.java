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
package org.eclipse.papyrus.sirius.uml.diagram.clazz.services;

import org.eclipse.papyrus.sirius.uml.diagram.common.core.services.AssociationServices;
import org.eclipse.sirius.diagram.EdgeArrows;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Property;

/**
 * Utility class used to calculate the decoration end of edge representing {@link Association} and {@link AssociationClass}
 * This code is adapted from org.eclipse.papyrus.uml.diagram.clazz.custom.edit.part.AbstractAssociationEditPart.refreshVisuals()
 * and from org.eclipse.papyrus.uml.diagram.common.figure.edge.AssociationFigure
 */
public class AssociationEndDecoratorUtils {

	/** the end of the association is an aggregation i.e. this a transparent diamond. */
	private static final int aggregation = 2;

	/** the end of the association is a composition so this a black diamond. */
	private static final int composition = 4;

	/** the end of the association is navigable so this is an arrow. */
	private static final int navigable = 1;

	/** the end of contained the property. */
	private static final int owned = 8;

	/**
	 * Adapted from org.eclipse.papyrus.uml.diagram.clazz.custom.edit.part.AbstractAssociationEditPart.refreshVisuals()
	 * 
	 * @param association
	 *            an association
	 * @return
	 *         an int representing the source type
	 */
	private static int getSourceType(final Association association) {
		if (association == null || association.getMemberEnds().size() < 2) {
			return 0;
		}
		final Property sourceProperty = AssociationServices.INSTANCE.getSourceProperty(association);
		final Property targetProperty = AssociationServices.INSTANCE.getTargetProperty(association);

		int sourceType = 0;

		// to display the dot.
		// owned?
		if (!targetProperty.getOwner().equals(association)) {
			sourceType += owned;
			sourceType += navigable;
		}
		// aggregation? for it the opposite is changed
		if (sourceProperty.getAggregation() == AggregationKind.SHARED_LITERAL) {
			sourceType += aggregation;
		}
		// composite? for it the opposite is changed
		if (sourceProperty.getAggregation() == AggregationKind.COMPOSITE_LITERAL) {
			sourceType += composition;
		}
		// navigable?
		if (association.getNavigableOwnedEnds().contains(targetProperty)) {
			sourceType += navigable;
		}

		return sourceType;
	}


	// duplicated from org.eclipse.papyrus.uml.diagram.clazz.custom.edit.part.AbstractAssociationEditPart.refreshVisuals()
	/**
	 * Adapted from org.eclipse.papyrus.uml.diagram.clazz.custom.edit.part.AbstractAssociationEditPart.refreshVisuals()
	 * 
	 * @param association
	 *            an association
	 * @return
	 *         an int representing the target type
	 */
	private static int getTargetType(final Association association) {
		if (association == null || association.getMemberEnds().size() < 2) {
			return 0;
		}

		final Property sourceProperty = AssociationServices.INSTANCE.getSourceProperty(association);
		final Property targetProperty =  AssociationServices.INSTANCE.getTargetProperty(association);

		int targetType = 0;
		// to display the dot.
		// owned?
		if (!sourceProperty.getOwner().equals(association)) {
			targetType += owned;
			targetType += navigable;
		}
		// aggregation? for it the opposite is changed
		if (targetProperty.getAggregation() == AggregationKind.SHARED_LITERAL) {
			targetType += aggregation;
		}

		// composite? for it the opposite is changed
		if (targetProperty.getAggregation() == AggregationKind.COMPOSITE_LITERAL) {
			targetType += composition;
		}

		// navigable?
		if (association.getNavigableOwnedEnds().contains(sourceProperty)) {
			targetType += navigable;
		}

		return targetType;
	}

	/**
	 * 
	 * @param association
	 *            an {@link Association}
	 * @return
	 *         the decorator to put on the source side of the edge
	 */
	public static EdgeArrows getSourceDecorator(final Association association) {
		return getDecorator(getSourceType(association));
	}

	/**
	 * 
	 * @param association
	 *            an {@link Association}
	 * @return
	 *         the decorator to put on the target side of the edge
	 */
	public static EdgeArrows getTargetDecorator(final Association association) {
		return getDecorator(getTargetType(association));
	}

	/**
	 * Adapted from org.eclipse.papyrus.uml.diagram.common.figure.edge.AssociationFigure
	 * 
	 * @param typeDecoration
	 *            the type of decoration to provide
	 * @return
	 *         the expected decoration
	 */
	private static EdgeArrows getDecorator(final int typeDecoration) {
		int ownedValue = typeDecoration / owned;
		int remain = typeDecoration % owned;
		int compositeValue = remain / composition;
		remain = remain % composition;
		int aggregationValue = remain / aggregation;
		remain = remain % aggregation;
		int navigationValue = remain / navigable;
		// the end association is contained by the association?
		if (ownedValue == 1) {
			// this is composite.
			if (compositeValue == 1) {
				if (navigationValue == 1) {
					return getOwnedNavigableCompositionDecoration();
				}
				return getOwnedCompositionDecoration();
			}
			// an aggregation?
			else if (aggregationValue == 1) {
				if (navigationValue == 1) {
					return getOwnedNavigableAggregationDecoration();
				}
				return getOwnedAggregationDecoration();
			}
			// Is it navigable?
			else if (navigationValue == 1) {
				return getOwnedNavigationDecoration();
			} else {
				return getOwnedDecoration();
			}
		} else {
			// this is composite.
			if (compositeValue == 1) {
				if (navigationValue == 1) {
					return getNavigableCompositionDecoration();
				}
				return getCompositionDecoration();
			}
			// an aggregation?
			else if (aggregationValue == 1) {
				if (navigationValue == 1) {
					return getNavigableAggregationDecoration();
				}
				return getAggregationDecoration();
			}
			// Is it naviagable?
			else if (navigationValue == 1) {
				return getNavigationDecoration();
			}
		}
		return EdgeArrows.NO_DECORATION_LITERAL;
	}

	/**
	 * @return
	 */
	private static EdgeArrows getNavigationDecoration() {
		return EdgeArrows.INPUT_ARROW_LITERAL;
	}

	/**
	 * @return
	 */
	private static EdgeArrows getAggregationDecoration() {
		return EdgeArrows.DIAMOND_LITERAL;
	}

	/**
	 * @return
	 */
	private static EdgeArrows getNavigableAggregationDecoration() {
		return EdgeArrows.INPUT_ARROW_WITH_DIAMOND_LITERAL;
	}

	/**
	 * @return
	 */
	private static EdgeArrows getCompositionDecoration() {
		return EdgeArrows.FILL_DIAMOND_LITERAL;
	}

	/**
	 * @return
	 */
	private static EdgeArrows getNavigableCompositionDecoration() {
		return EdgeArrows.INPUT_ARROW_WITH_FILL_DIAMOND_LITERAL;
	}

	/**
	 * @return
	 */
	private static EdgeArrows getOwnedDecoration() {
		return EdgeArrows.NO_DECORATION_LITERAL; // TODO just a DOT, but this representation doesn't exist in Sirius
	}

	/**
	 * @return
	 */
	private static EdgeArrows getOwnedNavigationDecoration() {
		return EdgeArrows.INPUT_ARROW_LITERAL; // TODO DOT + ARROW, but this representation doesn't exist in Sirius
	}

	/**
	 * @return
	 */
	private static EdgeArrows getOwnedAggregationDecoration() {
		return EdgeArrows.DIAMOND_LITERAL; // TODO : DOT + EMPTY_DIAMOND, but this representation doesn't exist in Sirius
	}

	/**
	 * @return
	 */
	private static EdgeArrows getOwnedNavigableAggregationDecoration() {
		return EdgeArrows.INPUT_ARROW_WITH_DIAMOND_LITERAL; // TODO : DOT + EMPTY_DIAMOND + ARROW, but this representation doesn't exist in Sirius
	}

	/**
	 * @return
	 */
	private static EdgeArrows getOwnedCompositionDecoration() {
		return EdgeArrows.FILL_DIAMOND_LITERAL; // TODO : DOT + FILL DIAMOND, , but this representation doesn't exist in Sirius
	}

	/**
	 * @return
	 */
	private static EdgeArrows getOwnedNavigableCompositionDecoration() {
		return EdgeArrows.INPUT_ARROW_WITH_FILL_DIAMOND_LITERAL; // TODO, DOT + FILL DIAMOND + ARROW, but this representation doesn't exist in Sirius
	}
}
