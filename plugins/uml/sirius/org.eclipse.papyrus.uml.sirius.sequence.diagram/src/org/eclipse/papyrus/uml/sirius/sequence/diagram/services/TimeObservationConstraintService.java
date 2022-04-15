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
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.LabelServices;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeSpec;
import org.eclipse.sirius.diagram.description.style.Side;
import org.eclipse.sirius.viewpoint.DRepresentationElement;
import org.eclipse.uml2.uml.CombinedFragment;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Duration;
import org.eclipse.uml2.uml.DurationConstraint;
import org.eclipse.uml2.uml.DurationInterval;
import org.eclipse.uml2.uml.DurationObservation;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ExecutionSpecification;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.InteractionOperand;
import org.eclipse.uml2.uml.LiteralInteger;
import org.eclipse.uml2.uml.Message;
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
	public List<TimeConstraint> getTimeConstraint(EObject context) {
		List<TimeConstraint> results= getTimeConstraints(context);
		results = results.stream().filter(con -> con.getConstrainedElements().contains(context)).collect(Collectors.toList());
		return results;
	}

	/**
	 * Gets the time constraint.
	 *
	 * @param context the context
	 * @return the time constraint
	 */
	public List<TimeObservation> getTimeObservation(EObject context) {
		List<TimeObservation> results= getTimeObservations(context);
		results = results.stream().filter(con -> context.equals(con.getEvent())).collect(Collectors.toList());
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
					} else {
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
			} else {
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
		EObject target = context;
		InteractionFragment interaction = null;
		if (context instanceof DRepresentationElement) {
			target = ((DRepresentationElement) context).getTarget();
		}
		if (target instanceof ExecutionSpecification 
				|| target instanceof EAnnotation
				|| target instanceof OccurrenceSpecification 
				|| target instanceof Message) {
			interaction = FragmentsService.getInstance().getEnclosingFragment(target);
		}
		if (interaction != null) {
			DurationObservation durationObservation = UMLFactory.eINSTANCE.createDurationObservation();
			interaction.getModel().getPackagedElements().add(durationObservation);
			durationObservation.setName(computeDefaultName(durationObservation));

			if (sourceVariable instanceof ExecutionSpecification) {
				durationObservation.getEvents().add(((ExecutionSpecification) sourceVariable).getFinish());
			} else if (sourceVariable instanceof OccurrenceSpecification) {
				durationObservation.getEvents().add((NamedElement) sourceVariable);
			} else if (sourceVariable instanceof Message) {
				durationObservation.getEvents().add((((Message) sourceVariable)).getSendEvent());
			}
			if (targetVariable instanceof ExecutionSpecification) {
				durationObservation.getEvents().add(((ExecutionSpecification) targetVariable).getStart());
			} else if (targetVariable instanceof OccurrenceSpecification) {
				durationObservation.getEvents().add((NamedElement) targetVariable);
			} else if (targetVariable instanceof Message) {
				durationObservation.getEvents().add((((Message) targetVariable)).getSendEvent());
			}

			return durationObservation;
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
	public DurationConstraint createDurationConstraint(EObject context, EObject sourceVariable,
			EObject targetVariable) {
		EObject target = context;
		if (context instanceof DRepresentationElement) {
			target = ((DRepresentationElement) context).getTarget();
		}
		InteractionFragment interaction = null;
		if (target instanceof ExecutionSpecification || target instanceof EAnnotation
				|| target instanceof OccurrenceSpecification || target instanceof Message) {
			interaction = FragmentsService.getInstance().getEnclosingFragment(target);
		}
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
		if (sourceVariable instanceof EAnnotation || sourceVariable instanceof OccurrenceSpecification) {
			durationConstraint.getConstrainedElements().add((Element) sourceVariable);
		}
		if (sourceVariable instanceof ExecutionSpecification) {
			durationConstraint.getConstrainedElements().add(((ExecutionSpecification) sourceVariable).getStart());
		}
		if (sourceVariable instanceof Message) {
			durationConstraint.getConstrainedElements().add(((Message) sourceVariable).getSendEvent());
		}
		if (targetVariable instanceof EAnnotation || targetVariable instanceof OccurrenceSpecification) {
			durationConstraint.getConstrainedElements().add((Element) targetVariable);
		}
		if (targetVariable instanceof ExecutionSpecification) {
			durationConstraint.getConstrainedElements().add(((ExecutionSpecification) targetVariable).getFinish());
		}
		if (targetVariable instanceof Message) {
			durationConstraint.getConstrainedElements().add(((Message) targetVariable).getSendEvent());
		}
		return durationConstraint;
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
			List<DurationObservation> collect = ((Interaction) context).getModel().getPackagedElements().stream()
					.filter(e -> e instanceof DurationObservation)
					.map(e -> (DurationObservation) e)
					.collect(Collectors.toList());
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
		if (!(context instanceof Interaction) && context instanceof InteractionFragment) {
			context = FragmentsService.getInstance().getParentInteraction((InteractionFragment) context);
		}
		if (context instanceof Interaction) {
			EList<Constraint> ownedRules = ((Interaction) context).getOwnedRules();
			for (Constraint constraint : ownedRules) {
				if (constraint instanceof TimeConstraint) {
					results.add((TimeConstraint) constraint);
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
		if (context instanceof Element) {
			Model model = ((Element) context).getModel();
			EList<PackageableElement> packagedElements = model.getPackagedElements();
			for (PackageableElement packageableElement : packagedElements) {
				if (packageableElement instanceof TimeObservation) {
					results.add((TimeObservation) packageableElement);
				}
			}
		}
		return results;
	}

	/**
	 * Gets the rules.
	 *
	 * @param interaction the interaction
	 * @return the rules
	 */

	public EObject getEventOrSelf(EObject obj) {
		if (obj instanceof OccurrenceSpecification) {
			return obj;
		}
		if (obj instanceof ExecutionSpecification) {
			return ((ExecutionSpecification) obj).getStart();
		}
		if (obj instanceof Message) {
			return ((Message) obj).getSendEvent();
		}
		return null;
	}

	public void deleteTimeObservation(EObject context) {
		List<TimeObservation> tos = getTimeObservation(context);
		for (TimeObservation to : tos) {
			to.destroy();
		}
	}

	public void deleteTimeConstraint(EObject context) {
		List<TimeConstraint> tcs = getTimeConstraint(context);
		for (TimeConstraint tc : tcs) {
			tc.destroy();
		}
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
