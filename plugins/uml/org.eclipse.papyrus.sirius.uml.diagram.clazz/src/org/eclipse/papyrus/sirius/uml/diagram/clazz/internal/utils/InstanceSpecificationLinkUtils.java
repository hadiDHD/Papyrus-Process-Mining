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
package org.eclipse.papyrus.sirius.uml.diagram.clazz.internal.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.papyrus.uml.tools.utils.NamedElementUtil;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.InstanceValue;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Slot;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * Utils methods for InstanceSpecificationLink
 * 
 * These methods are duplicated from org.eclipse.papyrus.uml.service.types.internal.ui.advice.InstanceSpecificationLinkEditHelperAdvice
 * Some of them are also duplicated from org.eclipse.papyrus.uml.service.types.internal.ui.commands.InstanceSpecificationLinkCreateCommand
 */
public class InstanceSpecificationLinkUtils {

	/** Annotation InstanceEnd source name */
	public static final String INSTANCE_END = "InstanceEnd"; //$NON-NLS-1$

	/**
	 * Gets the inherited classifier.
	 *
	 * @param classifier
	 *            the classifier
	 * @param alreadyParsedClassifiers
	 *            the set of classifiers already parsed
	 * @return the inherited classifier
	 */
	private static Set<Classifier> getInheritedClassifier(Classifier classifier, Set<Classifier> alreadyParsedClassifiers) {


		// Initialise set of Classifier from Generalisation
		Set<Classifier> generalizationClassifiers = new HashSet<Classifier>();


		// Keep track of parsed Classifier to avoid loop
		Set<Classifier> parsedClassifiersSet = new HashSet<Classifier>();
		if (alreadyParsedClassifiers != null) {
			parsedClassifiersSet.addAll(alreadyParsedClassifiers);
		}

		// Explore only Classifier which are not already parsed
		if (!parsedClassifiersSet.contains(classifier)) {
			parsedClassifiersSet.add(classifier);

			// Explore all generalisation of Classifier
			EList<Classifier> classifierGeneralizations = classifier.parents();
			generalizationClassifiers.addAll(classifierGeneralizations);

			for (Classifier generalClassifier : classifierGeneralizations) {
				generalizationClassifiers.addAll(getInheritedClassifier(generalClassifier, parsedClassifiersSet));
			}
		}

		return generalizationClassifiers;
	}

	/**
	 * Gets the instance associations.
	 *
	 * @param instance
	 *            the instance
	 * @return the instance associations
	 */
	private static HashSet<Association> getInstanceAssociations(InstanceSpecification instance) {
		// Initialise set of associations
		HashSet<Association> instanceAssociationsSet = new HashSet<Association>();
		// Extract all associations of Instance Specification's classifiers
		Iterator<Classifier> iterator = getSpecificationClassifier(instance).iterator();
		while (iterator.hasNext()) {
			Classifier classifier = iterator.next();
			instanceAssociationsSet.addAll(classifier.getAssociations());
		}
		return instanceAssociationsSet;
	}

	/**
	 * Gets the specification classifiers.
	 *
	 * @param instance
	 *            the instance
	 * @return the specification classifiers
	 */
	public static Set<Classifier> getSpecificationClassifier(InstanceSpecification instance) {
		// Initialise Set of Classifiers
		Set<Classifier> specificationClassicfiersSet = new HashSet<Classifier>();
		// Explore first rank classifiers
		for (Classifier classifier : instance.getClassifiers()) {

			// Explore only Classifier which are not already in Set
			if (!specificationClassicfiersSet.contains(classifier)) {
				specificationClassicfiersSet.add(classifier);
				specificationClassicfiersSet.addAll(getInheritedClassifier(classifier, null));
			}
		}
		return specificationClassicfiersSet;
	}

	/**
	 * Gets the instance associations.
	 *
	 * @param sourceInstance
	 *            the source instance
	 * @param targetInstance
	 *            the target instance
	 * @return the instance associations
	 */
	private static Set<Association> getInstanceAssociations(InstanceSpecification sourceInstance, InstanceSpecification targetInstance) {
		Set<Association> instanceAssociationsSet = new HashSet<Association>();
		// Extract all associations of Instance Specification's classifiers
		Iterator<Association> sourceAssociationsIterator = getInstanceAssociations(sourceInstance).iterator();
		Set<Classifier> sourceClassifiers = getSpecificationClassifier(sourceInstance);
		Set<Classifier> targetClassifiers = getSpecificationClassifier(targetInstance);
		while (sourceAssociationsIterator.hasNext()) {
			Association nextAssociation = sourceAssociationsIterator.next();
			if (checkAssociationEndType(nextAssociation, sourceClassifiers, targetClassifiers)) {
				instanceAssociationsSet.add(nextAssociation);
			}
		}
		return instanceAssociationsSet;
	}

	/**
	 * 
	 * @param association
	 *            an {@link Association}
	 * @param sourceClassifiers
	 *            collection of Classifier used as source
	 * @param targetClassifiers
	 *            collection of Classifier used as target
	 * @return
	 *         <code>true</code> if the association uses as source a classifier from sourceClassifiers or from targetClassifiers and uses as target a classifier from sourceClassifiers or from targetClassifiers
	 */
	private static boolean checkAssociationEndType(Association association, Set<Classifier> sourceClassifiers, Set<Classifier> targetClassifiers) {
		if (association.getMemberEnds().size() != 2) {
			return false;
		}
		Type sourceAssociationEnd = association.getMemberEnds().get(0).getType();
		Type targetAssociationEnd = association.getMemberEnds().get(1).getType();
		for (Classifier nextSourceClassifier : sourceClassifiers) {
			for (Classifier nextTargetClassifier : targetClassifiers) {
				if ((nextSourceClassifier == sourceAssociationEnd && nextTargetClassifier == targetAssociationEnd) || //
						(nextSourceClassifier == targetAssociationEnd && nextTargetClassifier == sourceAssociationEnd)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param source
	 *            {@link InstanceSpecification} used as source
	 * @param target
	 *            {@link InstanceSpecification} used as target
	 * @return
	 *         a collection of Association using these {@link InstanceSpecification} as source/target
	 */
	public static Set<Association> getModelAssociations(InstanceSpecification source, InstanceSpecification target) {
		if (source == null || target == null) {
			return Collections.emptySet();
		}
		return getInstanceAssociations(source, target);
	}

	/**
	 * add an end in the instancespecification link by adding a eannotation if not exist
	 * duplicated from org.eclipse.papyrus.uml.service.types.internal.ui.commands.InstanceSpecificationLinkCreateCommand
	 *
	 * @param instanceLink
	 * @param end
	 *            to add
	 */
	public static void addEnd(InstanceSpecification instanceLink, InstanceSpecification end) {
		EAnnotation endtypes = instanceLink.getEAnnotation(INSTANCE_END);
		if (endtypes == null) {
			endtypes = instanceLink.createEAnnotation(INSTANCE_END);
		}
		endtypes.getReferences().add(end);
	}

	/**
	 * This method create the {@link Slot} for the {@link InstanceSpecification} representing an {@link Association}
	 * 
	 * @param selectedAssociation
	 *            the selected {@link Association}
	 * @param instanceSpecification
	 *            the {@link InstanceSpecification} to link to the {@link Association}
	 * @param source
	 *            the {@link InstanceSpecification} representing the 'source' of the Association
	 * @param target
	 *            the {@link InstanceSpecification} representing the 'target' of the Association
	 * @param sourceSpecificationClassifiersSet
	 *            the classifier used as sources
	 * @param targetSpecificationClassifiersSet
	 *            the classifier used as targetsF
	 */
	public static void setupSlots(Association selectedAssociation, InstanceSpecification instanceSpecification, InstanceSpecification source, InstanceSpecification target, Set<Classifier> sourceSpecificationClassifiersSet,
			Set<Classifier> targetSpecificationClassifiersSet) {
		if (selectedAssociation == null) {
			return;
		}
		// Creation of slots into the good instance by taking in account the association
		Iterator<Property> proIterator = selectedAssociation.getMemberEnds().iterator();
		while (proIterator.hasNext()) {
			Property property = proIterator.next();
			Slot slot = UMLFactory.eINSTANCE.createSlot();
			slot.setDefiningFeature(property);
			if (sourceSpecificationClassifiersSet.contains(property.getOwner())) {
				source.getSlots().add(slot);
				associateValue((target), slot, property.getType());
			} else if (targetSpecificationClassifiersSet.contains(property.getOwner())) {
				target.getSlots().add(slot);
				associateValue((source), slot, property.getType());
			} else {
				instanceSpecification.getSlots().add(slot);
				if (sourceSpecificationClassifiersSet.contains(property.getType())) {
					associateValue((source), slot, property.getType());
				} else {
					associateValue((target), slot, property.getType());
				}
			}
		}
	}

	/**
	 * create an instanceValue for the slot (owner) with the reference to InstanceSpecification and the good type
	 *
	 * @param instanceSpecification
	 *            that is referenced by the instanceValue
	 * @param owner
	 *            of the instance value
	 * @param type
	 *            of the instanceValue
	 * @return a instanceValue
	 */
	public static InstanceValue associateValue(InstanceSpecification instanceSpecification, Slot owner, Type type) {
		InstanceValue instanceValue = UMLFactory.eINSTANCE.createInstanceValue();
		instanceValue.setName(NamedElementUtil.getDefaultNameWithIncrementFromBase(instanceValue.eClass().getName(), owner.eContents()));
		instanceValue.setType(type);
		instanceValue.setInstance(instanceSpecification);
		owner.getValues().add(instanceValue);
		return instanceValue;
	}
}
