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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.diagram.DDiagram;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.DEdge;
import org.eclipse.sirius.diagram.EdgeTarget;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeListSpec;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeSpec;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;

import com.google.common.collect.Lists;

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
	private AssociationServices() {

	}

	/**
	 * Create association end.
	 *
	 * @param type
	 *            type of end
	 * @return property
	 */
	public Property createAssociationEnd(Type type) {
		final Property property = UMLFactory.eINSTANCE.createProperty();
		property.setName(getAssociationEndsName(type));
		property.setType(type);
		property.setLower(0);
		property.setUpper(-1);
		return property;
	}
	
	/**
	 * Create association class end.
	 *
	 * @param type
	 *            type of end
	 * @return property
	 */
	public Property createAssociationClassEnd(Type type) {
		final Property property = UMLFactory.eINSTANCE.createProperty();
		property.setName(getAssociationEndsName(type));
		property.setType(type);
		return property;
	}
	
    /**
	 * Precondition for n-ary association creation.
	 *
	 * @param object
	 *            selected association
	 * @return true if association is binary and no end have no qualifiers
	 */
	public boolean createNaryAssociationPrecondition(EObject object) {
		// aql:self.oclIsKindOf(uml::Association) and
		// self.oclAsType(uml::Association).getEndTypes()->size()<=2
		if (object instanceof Association) {
			boolean res = true;
			// if (((Association)object).getMemberEnds().size() == 2) {
			for (final Property end : ((Association) object).getMemberEnds()) {
				if (!end.getQualifiers().isEmpty()) {
					res = false;
				}
			}
			// }
			return res;
		}
		return false;
	}

	/**
	 * Compute Association end name.
	 *
	 * @param type
	 *            type of end
	 * @return name
	 */
	public String getAssociationEndsName(Type type) {
		String name = ((NamedElement) type).getName();
		if (!com.google.common.base.Strings.isNullOrEmpty(name)) {
			final char c[] = name.toCharArray();
			c[0] = Character.toLowerCase(c[0]);
			name = new String(c) + 's';
		}
		return name;
	}

	/**
	 * Return the source of an association.
	 *
	 * @param association
	 *            the {@link Association} context
	 * @return first end of the association
	 */
	public Property getSource(Association association) {
		if (association.getMemberEnds() != null && association.getMemberEnds().size() > 0) {
			return association.getMemberEnds().get(0);
		}
		return null;
	}

	/**
	 * Get the source of an n-ary association.
	 *
	 * @param association
	 *            the association
	 * @param view
	 *            the edge to retrieve the source end
	 * @return end
	 */
	public Property getSourceEndAssociation(Association association, DDiagramElement view) {

		if (view instanceof DEdge) {
			final DEdge edge = (DEdge) view;
			final List<Property> members = association.getMemberEnds();
			final EdgeTarget edgeSource = edge.getSourceNode();
			List<EObject> sourceSemanticElements = new ArrayList<EObject>();

			if (edgeSource instanceof DNodeListSpec) {
				// edge source is a classifier
				final DNodeListSpec source = (DNodeListSpec) edgeSource;
				sourceSemanticElements = source.getSemanticElements();
				for (final EObject sourceEObject : sourceSemanticElements) {
					for (final Property member : members) {
						if (sourceEObject.equals(member.getType())) {
							return member;
						}
					}
				}
			} else if (edgeSource instanceof DNodeSpec) {
				// edge source is a qualifier
				sourceSemanticElements = ((DNodeSpec) edgeSource).getSemanticElements();
				for (final EObject sourceEObject : sourceSemanticElements) {
					for (final Property member : members) {
						if (sourceEObject.equals(member)) {
							return member;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Get the type of the association source end.
	 *
	 * @param association
	 *            Association
	 * @return Type of the source
	 */
	public Type getSourceType(Association association) {
		final Property source = getSource(association);
		if (source != null) {
			return source.getType();
		}
		return null;
	}

	/**
	 * Return the target of an association.
	 *
	 * @param association
	 *            the {@link Association} context
	 * @return second end of the association
	 */
	public Property getTarget(Association association) {
		if (association.getMemberEnds() != null && association.getMemberEnds().size() > 1) {
			return association.getMemberEnds().get(1);
		}
		return null;
	}

	/**
	 * Get the type of the association target end.
	 *
	 * @param association
	 *            Association
	 * @return Type of the target
	 */
	public Type getTargetType(Association association) {
		final Property target = getTarget(association);
		if (target != null) {
			return target.getType();
		}
		return null;
	}

	/**
	 * Get types.
	 *
	 * @param association
	 *            Association
	 * @return List of types
	 */
	public List<Type> getTypes(Association association) {
		final List<Type> types = Lists.newArrayList();
		types.add(getSourceType(association));
		types.add(getTargetType(association));
		return types;
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
		final Property source = AssociationServices.INSTANCE.getSource(association);
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
		final Property source = AssociationServices.INSTANCE.getSourceEndAssociation(association, element);
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
		final Property source = AssociationServices.INSTANCE.getSource(association);
		final Property target = AssociationServices.INSTANCE.getTarget(association);
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
		final Property source = AssociationServices.INSTANCE.getSource(association);
		final Property target = AssociationServices.INSTANCE.getTarget(association);
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
		final Property source = AssociationServices.INSTANCE.getSource(association);
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
		final Property target = AssociationServices.INSTANCE.getTarget(association);
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
		final Property target = AssociationServices.INSTANCE.getTarget(association);
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
		final Property target = AssociationServices.INSTANCE.getTarget(association);
		final Property source = AssociationServices.INSTANCE.getSource(association);
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
		final Property target = AssociationServices.INSTANCE.getTarget(association);
		final Property source = AssociationServices.INSTANCE.getSource(association);
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
		final Property target = AssociationServices.INSTANCE.getTarget(association);
		return isShared(target);
	}
}
