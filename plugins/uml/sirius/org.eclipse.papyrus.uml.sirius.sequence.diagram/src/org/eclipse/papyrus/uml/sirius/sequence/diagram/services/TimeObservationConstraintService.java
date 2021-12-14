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
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.sequence.diagram.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.LabelServices;
import org.eclipse.sirius.diagram.business.internal.metamodel.spec.DNodeSpec;
import org.eclipse.sirius.diagram.description.style.Side;
import org.eclipse.uml2.uml.CombinedFragment;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Duration;
import org.eclipse.uml2.uml.DurationConstraint;
import org.eclipse.uml2.uml.DurationInterval;
import org.eclipse.uml2.uml.DurationObservation;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ExecutionOccurrenceSpecification;
import org.eclipse.uml2.uml.ExecutionSpecification;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.InteractionOperand;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OccurrenceSpecification;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.TimeConstraint;
import org.eclipse.uml2.uml.TimeObservation;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * The Class TimeObservationConstraintService.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
public class TimeObservationConstraintService {

	/** The service. */
	static private TimeObservationConstraintService service = null;


	/**
	 * Instantiates a new fragments service.
	 */
	private TimeObservationConstraintService() {

	}

	/**
	 * Gets the single instance of FragmentsService.
	 *
	 * @return single instance of FragmentsService
	 */
	static public TimeObservationConstraintService getInstance() {
		if (service == null) {
			service = new TimeObservationConstraintService();
		}
		return service;
	}




	/**
	 * Gets the time constraint.
	 *
	 * @param context
	 *            the context
	 * @return the time constraint
	 */
	public List<InteractionFragment> getTimeConstraint(EObject context) {
		List<InteractionFragment> results = new ArrayList<>();
		if (context instanceof Interaction) {
			Interaction interaction = (Interaction) context;
			EList<Constraint> ownedRules = interaction.getOwnedRules();
			for (Constraint constraint : ownedRules) {
				if (constraint instanceof TimeConstraint) {
					EList<Element> constrainedElements = constraint.getConstrainedElements();
					for (Element element : constrainedElements) {
						if (element instanceof ExecutionOccurrenceSpecification) {
							results.add((ExecutionOccurrenceSpecification) element);
						}
					}
				}
			}
		}
		if (context instanceof TimeConstraint) {
			EList<Element> constrainedElements = ((TimeConstraint) context).getConstrainedElements();
			for (Element element : constrainedElements) {
				if (element instanceof ExecutionOccurrenceSpecification) {
					results.add((ExecutionOccurrenceSpecification) element);
				}
			}
		}

		return results;

	}



	/**
	 * Gets the side.
	 *
	 * @param context
	 *            the context
	 * @return the side
	 */
	public List<Side> getSide(EObject context) {
		List<Side> results = new ArrayList<>();
		results.add(Side.EAST);
		results.add(Side.WEST);
		if (context instanceof TimeConstraint) {
			EList<Element> constrainedElements = ((TimeConstraint) context).getConstrainedElements();
			for (Element element : constrainedElements) {
				if (element instanceof OccurrenceSpecification) {
					if (!ReorderService.getInstance().isStartOfExecution(element, FragmentsService.getInstance().getEnclosingFragments(element))) {
						results.add(Side.NORTH);
						return results;
					}
					else {
						results.add(Side.SOUTH);
						return results;
					}
				}
			}
		}
		if (context instanceof TimeObservation) {
			NamedElement event = ((TimeObservation) context).getEvent();
			if (!ReorderService.getInstance().isStartOfExecution(event, FragmentsService.getInstance().getEnclosingFragments(event))) {
				results.add(Side.NORTH);
				return results;
			}
			else {
				results.add(Side.SOUTH);
				return results;
			}
		}
		return null;
	}





	/**
	 * Creates the duration observation.
	 *
	 * @param context
	 *            the context
	 * @param sourceVariable
	 *            the source variable
	 * @param targetVariable
	 *            the target variable
	 * @return the duration observation
	 */
	public DurationObservation createDurationObservation(EObject context, EObject sourceVariable, EObject targetVariable) {
		if (context instanceof DNodeSpec) {
			EObject target = ((DNodeSpec) context).getTarget();
			if (target instanceof ExecutionSpecification) {
				InteractionFragment interaction = /* ((ExecutionSpecification) target).getEnclosingInteraction() */FragmentsService.getInstance().getEnclosingFragment(target);

				DurationObservation durationObservation = UMLFactory.eINSTANCE.createDurationObservation();
				interaction.getModel().getPackagedElements().add(durationObservation);
				durationObservation.setName(computeDefaultName(durationObservation));


				durationObservation.getEvents().add(((ExecutionSpecification) sourceVariable).getFinish());
				durationObservation.getEvents().add(((ExecutionSpecification) targetVariable).getStart());

				return durationObservation;
			}
		}
		return null;
	}



	/**
	 * Creates the duration constraint.
	 *
	 * @param context
	 *            the context
	 * @param sourceVariable
	 *            the source variable
	 * @param targetVariable
	 *            the target variable
	 * @return the duration constraint
	 */
	public DurationConstraint createDurationConstraint(EObject context, EObject sourceVariable, EObject targetVariable) {
		if (context instanceof DNodeSpec) {
			EObject target = ((DNodeSpec) context).getTarget();
			if (target instanceof ExecutionSpecification) {
				InteractionFragment interaction = FragmentsService.getInstance().getEnclosingFragment(target);

				DurationConstraint durationConstraint = UMLFactory.eINSTANCE.createDurationConstraint();
				getRules(interaction).add(durationConstraint);
				durationConstraint.setName(computeDefaultName(durationConstraint));

				DurationInterval durationInterval = UMLFactory.eINSTANCE.createDurationInterval();
				durationConstraint.setSpecification(durationInterval);
				durationInterval.setName(computeDefaultName(durationInterval));

				Duration min = UMLFactory.eINSTANCE.createDuration();
				interaction.getModel().getPackagedElements().add(min);
				durationInterval.setMin(min);
				min.setName(computeDefaultName(min));

				LiteralInteger minInteger = UMLFactory.eINSTANCE.createLiteralInteger();
				min.setExpr(minInteger);

				Duration max = UMLFactory.eINSTANCE.createDuration();
				interaction.getModel().getPackagedElements().add(max);
				durationInterval.setMax(max);
				max.setName(computeDefaultName(max));

				LiteralInteger maxInteger = UMLFactory.eINSTANCE.createLiteralInteger();
				max.setExpr(maxInteger);

				durationConstraint.getConstrainedElements().add(((ExecutionSpecification) sourceVariable).getFinish());
				durationConstraint.getConstrainedElements().add(((ExecutionSpecification) targetVariable).getStart());

				return durationConstraint;
			}
		}
		return null;
	}

	/**
	 * Gets the all duration observation.
	 *
	 * @param context
	 *            the context
	 * @return the all duration observation
	 */
	public List<DurationObservation> getAllDurationObservation(EObject context) {
		List<DurationObservation> results = new ArrayList<>();
		if (context instanceof Interaction) {
			List<DurationObservation> collect = ((Interaction) context).getModel().getPackagedElements().stream().filter(e -> e instanceof DurationObservation).map(e -> (DurationObservation) e).collect(Collectors.toList());
			results.addAll(collect);
			EList<InteractionFragment> fragments = ((Interaction) context).getFragments();
			for (InteractionFragment interactionFragment : fragments) {
				if (interactionFragment instanceof CombinedFragment) {
					results.addAll(getAllDurationObservation(interactionFragment));
				}
			}
		}


		return results;

	}


	/**
	 * Gets the time constraints.
	 *
	 * @param context
	 *            the context
	 * @return the time constraints
	 */
	public List<TimeConstraint> getTimeConstraints(EObject context) {
		List<TimeConstraint> results = new ArrayList<>();
		if (context instanceof ExecutionSpecification) {
			OccurrenceSpecification start = ((ExecutionSpecification) context).getStart();
			OccurrenceSpecification finish = ((ExecutionSpecification) context).getFinish();
			InteractionFragment interaction = FragmentsService.getInstance().getEnclosingFragment(context);
			EList<Constraint> ownedRules = null;
			if (interaction instanceof Interaction) {
				ownedRules = ((Interaction) interaction).getOwnedRules();
			}
			if (interaction instanceof InteractionOperand) {
				ownedRules = ((InteractionOperand) interaction).getOwnedRules();
			}
			for (Constraint constraint : ownedRules) {
				if (constraint instanceof TimeConstraint) {
					EList<Element> constrainedElements = constraint.getConstrainedElements();
					for (Element element : constrainedElements) {
						if (element.equals(start) || element.equals(finish)) {
							results.add((TimeConstraint) constraint);
						}
					}
				}
			}

		}
		return results;
	}


	/**
	 * Gets the time observations.
	 *
	 * @param context
	 *            the context
	 * @return the time observations
	 */
	public List<TimeObservation> getTimeObservations(EObject context) {
		List<TimeObservation> results = new ArrayList<>();
		if (context instanceof ExecutionSpecification) {
			OccurrenceSpecification start = ((ExecutionSpecification) context).getStart();
			OccurrenceSpecification finish = ((ExecutionSpecification) context).getFinish();

			Model model = ((ExecutionSpecification) context).getModel();
			EList<PackageableElement> packagedElements = model.getPackagedElements();
			for (PackageableElement packageableElement : packagedElements) {
				if (packageableElement instanceof TimeObservation) {
					if (((TimeObservation) packageableElement).getEvent().equals(start) || ((TimeObservation) packageableElement).getEvent().equals(finish)) {
						results.add((TimeObservation) packageableElement);
					}
				}
			}
		}

		return results;
	}

	///// private methode

	/**
	 * Gets the rules.
	 *
	 * @param interaction
	 *            the interaction
	 * @return the rules
	 */
	private List<Constraint> getRules(InteractionFragment interaction) {
		if (interaction instanceof InteractionOperand) {
			return FragmentsService.getInstance().getParentInteraction(interaction).getOwnedRules();
		}
		if (interaction instanceof Interaction) {
			return ((Interaction) interaction).getOwnedRules();
		}
		return null;
	}

	/**
	 * Compute default name.
	 *
	 * @param element
	 *            New element
	 * @return Name for the new element, he name will looks like
	 *         'ElementType'+total of existing elements of the same type.
	 */
	private String computeDefaultName(final EObject element) {
		return LabelServices.INSTANCE.computeDefaultName(element);
	}

}
