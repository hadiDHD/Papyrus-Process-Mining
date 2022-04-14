/******************************************************************************
 * Copyright (c) 2021 CEA LIST, Artal Technologies
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *    Yann Binot (ARTAL) - yann.binot@artal.fr - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.sequence.diagram.services;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DurationConstraint;
import org.eclipse.uml2.uml.DurationObservation;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ExecutionOccurrenceSpecification;
import org.eclipse.uml2.uml.ExecutionSpecification;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageOccurrenceSpecification;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OccurrenceSpecification;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.TimeConstraint;
import org.eclipse.uml2.uml.ValueSpecification;

/**
 * The Class ConstraintService.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
public class ConstraintService {

	/** The service. */
	static private ConstraintService service = null;

	/**
	 * Instantiates a new fragments service.
	 */
	private ConstraintService() {

	}

	/**
	 * Gets the single instance of FragmentsService.
	 *
	 * @return single instance of FragmentsService
	 */
	static public ConstraintService getInstance() {
		if (service == null) {
			service = new ConstraintService();
		}
		return service;
	}

	/**
	 * Return all constriantedElement for given Constraint <b> Exception for
	 * PartDeploymentLink (return its DeployedElement) <b>.
	 *
	 * @param constraint the constraint
	 * @return the list
	 */
	public List<?> targeFinderExpressionForConstraint(Constraint constraint) {
		List<EObject> result = new ArrayList<>();
		EList<Element> constrainedElements = constraint.getConstrainedElements();
		result.addAll(constrainedElements);
		return result;
	}

	/**
	 * Gets the constraint label.
	 *
	 * @param object the object
	 * @return the constraint label
	 */
	public String getConstraintLabel(EObject object) {

		if (object instanceof Constraint) {

			String name = ((Constraint) object).getName();
			EList<String> languages = null;
			EList<String> bodies = null;
			ValueSpecification specification = ((Constraint) object).getSpecification();
			if (specification instanceof OpaqueExpression) {
				languages = ((OpaqueExpression) specification).getLanguages();
				bodies = ((OpaqueExpression) specification).getBodies();
			}
			String langage = "";
			if (languages != null && !languages.isEmpty()) {
				langage = languages.get(0);
			}
			String body = "";
			if (bodies != null && !bodies.isEmpty()) {
				body = bodies.get(0);
			}

			String result = name + "\n" + "{{" + langage + "}" + body + "}";
			return result;

		}
		return "";
	}

	/**
	 * Gets the source constrained element.
	 *
	 * @param context the context
	 * @return the source constrained element
	 */
	public EObject getSourceConstrainedElement(EObject context) {

		if (context instanceof DurationConstraint) {

			EList<Element> constrainedElements = ((DurationConstraint) context).getConstrainedElements();
			Element element = constrainedElements.get(0);

			if (element instanceof ExecutionSpecification) {
				return (((ExecutionSpecification) element).getStart());
			}
			
			if (element instanceof Message) {
				return (((Message) element).getSendEvent());
			}

			if (element instanceof OccurrenceSpecification || element instanceof EAnnotation) {
				return element;
			}

		}

		return null;
	}

	/**
	 * Gets the target constrained element.
	 *
	 * @param context the context
	 * @return the target constrained element
	 */
	public EObject getTargetConstrainedElement(EObject context) {

		if (context instanceof DurationConstraint) {

			EList<Element> constrainedElements = ((DurationConstraint) context).getConstrainedElements();
			Element element = constrainedElements.get(constrainedElements.size() - 1);

			if (element instanceof ExecutionSpecification) {
				return (((ExecutionSpecification) element).getStart());
			}
			
			if (element instanceof Message) {
				return (((Message) element).getSendEvent());
			}

			if (element instanceof OccurrenceSpecification || element instanceof EAnnotation) {
				return element;
			}
		}
		return null;
	}

	/**
	 * Gets the source observation.
	 *
	 * @param context the context
	 * @return the source observation
	 */
	public EObject getSourceObservation(EObject context) {
		if (context instanceof DurationObservation) {
			EList<NamedElement> events = ((DurationObservation) context).getEvents();

			Element element = events.get(0);
			if (element instanceof OccurrenceSpecification) {
				return element;
			}

			if (element instanceof EAnnotation) {
				return ((EAnnotation) element);
			}


		}
		return null;
	}

	/**
	 * Gets the target observation.
	 *
	 * @param context the context
	 * @return the target observation
	 */
	public EObject getTargetObservation(EObject context) {
		if (context instanceof DurationObservation) {
			EList<NamedElement> events = ((DurationObservation) context).getEvents();

			Element element = events.get(events.size() - 1);
			if (element instanceof OccurrenceSpecification) {
				return element;
			}

			if (element instanceof EAnnotation) {
				return ((EAnnotation) element);
			}
		}
		return null;
	}
}
