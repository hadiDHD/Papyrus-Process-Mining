/*******************************************************************************
 * Copyright (c) 2014, 2021 Obeo, CEA LIST, Artal Technologies
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *    Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - adaptation to integrate in Papyrus
 *******************************************************************************/

package org.eclipse.papyrus.uml.sirius.sequence.diagram.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.ActionExecutionSpecification;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.BehaviorExecutionSpecification;
import org.eclipse.uml2.uml.ExecutionOccurrenceSpecification;
import org.eclipse.uml2.uml.ExecutionSpecification;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.InteractionOperand;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageOccurrenceSpecification;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OccurrenceSpecification;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * The Class ExecutionService.
 */
public class ExecutionService {

	/**
	 * Execution end suffix.
	 */
	public static final String EXECUTION_END_SUFFIX = "_finish"; //$NON-NLS-1$


	/** The service. */
	static private ExecutionService service = null;


	/**
	 * Instantiates a new fragments service.
	 */
	private ExecutionService() {

	}

	/**
	 * Gets the single instance of FragmentsService.
	 *
	 * @return single instance of FragmentsService
	 */
	static public ExecutionService getInstance() {
		if (service == null) {
			service = new ExecutionService();
		}
		return service;
	}


	/**
	 * Gets the execution specifications.
	 *
	 * @param object
	 *            the object
	 * @return the execution specifications
	 */
	public List<ExecutionSpecification> getExecutionSpecifications(EObject object) {
		List<ExecutionSpecification> results = new ArrayList<ExecutionSpecification>();
		if (object instanceof Lifeline) {
			Lifeline lifeline = (Lifeline) object;
			Interaction interaction = lifeline.getInteraction();
			LifelineService.getInstance().getFragmentsFromLifeline(results, lifeline, interaction);
		} else if (object instanceof ExecutionSpecification) {
			List<ExecutionSpecification> containedNestedExecution = containedNestedExecution((ExecutionSpecification) object);
			results.addAll(containedNestedExecution);
		}
		return results;
	}






	/**
	 * Get the execution associated to a fragment.
	 *
	 * @param message
	 *            Message
	 * @return Execution
	 */
	public BehaviorExecutionSpecification getExecution(Message message) {
		if (message == null) {
			return null;
		}
		final Map<Message, BehaviorExecutionSpecification> behaviors = new HashMap<Message, BehaviorExecutionSpecification>();
		for (final InteractionFragment fragment : message.getInteraction().getFragments()) {
			if (fragment instanceof BehaviorExecutionSpecification) {
				final BehaviorExecutionSpecification behavior = (BehaviorExecutionSpecification) fragment;
				final OccurrenceSpecification behaviorStart = behavior.getStart();
				if (behaviorStart instanceof MessageOccurrenceSpecification
						&& message.equals(((MessageOccurrenceSpecification) behaviorStart).getMessage())) {
					behaviors.put(message, behavior);
				}
			}
		}
		return behaviors.get(message);
	}

	/**
	 * Get the execution associated to a fragment.
	 *
	 * @param occurence
	 *            Occurence
	 * @return Execution
	 */
	public BehaviorExecutionSpecification getExecution(EObject occurence) {
		if (occurence == null) {
			return null;
		}
		final Map<InteractionFragment, BehaviorExecutionSpecification> behaviors = new HashMap<InteractionFragment, BehaviorExecutionSpecification>();

		// final Interaction interaction = getEnclosingInteraction(occurence);
		List<EObject> fragments = FragmentsService.getInstance().getEnclosingFragments(occurence);
		for (final EObject fragment : fragments) {
			if (fragment instanceof BehaviorExecutionSpecification) {
				final BehaviorExecutionSpecification behavior = (BehaviorExecutionSpecification) fragment;
				// Get start
				behaviors.put(behavior.getStart(), behavior);
				// Get finish
				behaviors.put(behavior.getFinish(), behavior);
			}
		}
		return behaviors.get(occurence);
	}

	/**
	 * Create a typed execution. Execution could be created on lifeline or other parent execution.
	 *
	 * @param interaction
	 *            Interaction
	 * @param fragment
	 *            Lifeline or parent execution
	 * @param operation
	 *            Operation associated to execution
	 * @param startingEndPredecessor
	 *            Starting end predecessor
	 */
	public void createExecution(Interaction interaction, NamedElement fragment, Operation operation,
			NamedElement startingEndPredecessor) {
		final Lifeline lifeline = LifelineService.getInstance().getLifeline(fragment);

		final UMLFactory factory = UMLFactory.eINSTANCE;
		StringBuffer executionName;
		if (operation == null) {
			final List<BehaviorExecutionSpecification> behaviors = new ArrayList<BehaviorExecutionSpecification>();
			for (final InteractionFragment behavior : interaction.getFragments()) {
				if (behavior instanceof BehaviorExecutionSpecification) {
					behaviors.add((BehaviorExecutionSpecification) behavior);
				}
			}
			executionName = new StringBuffer("BehaviorExecution_").append(behaviors.size()); //$NON-NLS-1$
		} else {
			executionName = new StringBuffer(operation.getName());
		}

		// Create execution start
		final ExecutionOccurrenceSpecification startExec = factory.createExecutionOccurrenceSpecification();
		final StringBuffer startExecName = new StringBuffer(executionName).append("_start"); //$NON-NLS-1$
		startExec.setName(startExecName.toString());
		startExec.getCovereds().add(lifeline);

		// Create behavior
		final OpaqueBehavior behavior = factory.createOpaqueBehavior();
		behavior.setName(executionName.toString());
		behavior.setSpecification(operation);
		interaction.getOwnedBehaviors().add(behavior);
		final BehaviorExecutionSpecification execution = factory.createBehaviorExecutionSpecification();
		execution.setName(executionName.toString());
		execution.getCovereds().add(lifeline);
		execution.setBehavior(behavior);

		execution.setStart(startExec);
		startExec.setExecution(execution);

		// Create execution end
		final ExecutionOccurrenceSpecification endExec = factory.createExecutionOccurrenceSpecification();
		final StringBuffer endExecName = new StringBuffer(executionName).append(EXECUTION_END_SUFFIX);
		endExec.setName(endExecName.toString());
		endExec.getCovereds().add(lifeline);
		endExec.setExecution(execution);
		execution.setFinish(endExec);

		// Add and order fragments under the interaction
		final EList<InteractionFragment> fragments = interaction.getFragments();

		// Ordered fragments
		fragments.add(startExec);

		// If execution starts from an execution, add the new execution start
		// after the execution
		// specification
		if (startingEndPredecessor instanceof OccurrenceSpecification
				&& ExecutionService.getInstance().getExecution(startingEndPredecessor) != null
				&& startingEndPredecessor
						.equals(ExecutionService.getInstance().getExecution(startingEndPredecessor).getStart())) {
			fragments.move(fragments.indexOf(startingEndPredecessor) + 2, startExec);
		} else {
			// Message starts from a lifeline, add the message start after the
			// last starting predecessor
			// (message)
			fragments.move(fragments.indexOf(startingEndPredecessor) + 1, startExec);
		}
		fragments.add(execution);
		fragments.move(fragments.indexOf(startExec) + 1, execution);
		fragments.add(endExec);
		fragments.move(fragments.indexOf(execution) + 1, endExec);
	}

	/**
	 * Create execution.
	 *
	 * @param covered
	 *            Lifeline
	 * @param message
	 *            Message
	 * @return Execution
	 */
	public BehaviorExecutionSpecification createExecution(
			final Lifeline covered, final Message message) {
		final UMLFactory factory = UMLFactory.eINSTANCE;
		final OpaqueBehavior behavior = factory.createOpaqueBehavior();

		behavior.setName(message.getName());

		final BehaviorExecutionSpecification execution = factory.createBehaviorExecutionSpecification();
		execution.setName(message.getName());
		execution.getCovereds().add(covered);
		execution.setBehavior(behavior);

		return execution;
	}

	/**
	 * Create a typed execution. Execution could be created on lifeline or other parent execution.
	 *
	 * @param interaction
	 *            Interaction
	 * @param fragment
	 *            Lifeline or parent execution
	 * @param startingEndPredecessor
	 *            Starting end predecessor
	 */
	public void createExecution(Interaction interaction, NamedElement fragment,
			NamedElement startingEndPredecessor) {
		createExecution(interaction, fragment, null, startingEndPredecessor);
	}

	/**
	 * Delete execution.
	 *
	 * @param execution
	 *            Execution to delete
	 */
	public void deleteExecution(ExecutionSpecification execution) {
		if (execution == null) {
			return;
		}

		// Get fragments
		final InteractionFragment interaction = FragmentsService.getInstance().getEnclosingFragment(execution);

		// Delete opaque behavior
		if (execution instanceof BehaviorExecutionSpecification) {
			removeBehavior((BehaviorExecutionSpecification) execution);
		}

		// Delete start and finish behavior
		final List<InteractionFragment> fragments = interaction instanceof Interaction ? ((Interaction) interaction).getFragments() : ((InteractionOperand) interaction).getFragments();
		final OccurrenceSpecification start = execution.getStart();
		if (start instanceof ExecutionOccurrenceSpecification) {
			fragments.remove(start);
		}
		final OccurrenceSpecification finish = execution.getFinish();
		if (finish instanceof ExecutionOccurrenceSpecification) {
			fragments.remove(finish);
		}
		// Delete execution
		fragments.remove(execution);
	}



	/**
	 * Checks if is nested execution.
	 *
	 * @param exec
	 *            the exec
	 * @return true, if is nested execution
	 */
	public boolean isNestedExecution(ExecutionSpecification exec) {

		List<EObject> enclosingFragments = FragmentsService.getInstance().getEnclosingFragments(exec);

		OccurrenceSpecification start = exec.getStart();
		OccurrenceSpecification finish = exec.getFinish();

		int indexOfStart = enclosingFragments.indexOf(start);
		int indexOfFinish = enclosingFragments.indexOf(finish);

		for (EObject exec2 : enclosingFragments) {
			EList<Lifeline> covereds = exec.getCovereds();
			if (covereds != null && !covereds.isEmpty()) {
				if (!exec2.equals(exec) && exec2 instanceof ExecutionSpecification && !((ExecutionSpecification) exec2).getCovereds().isEmpty() && covereds.get(0).equals(((ExecutionSpecification) exec2).getCovereds().get(0))) {

					OccurrenceSpecification start2 = ((ExecutionSpecification) exec2).getStart();
					OccurrenceSpecification finish2 = ((ExecutionSpecification) exec2).getFinish();

					int indexOfStart2 = enclosingFragments.indexOf(start2);
					int indexOfFinish2 = enclosingFragments.indexOf(finish2);


					if (indexOfStart2 < indexOfStart && indexOfFinish2 > indexOfFinish) {
						return true;
					}


				}
			}
		}

		return false;
	}

	/**
	 * Apply execution.
	 *
	 * @param context
	 *            the context
	 * @param exe
	 *            the exe
	 * @param start
	 *            the start
	 * @param finish
	 *            the finish
	 */
	public void applyExecution(EObject context, ExecutionSpecification exe, ExecutionOccurrenceSpecification start, ExecutionOccurrenceSpecification finish) {
		if (context instanceof Lifeline) {
			((Lifeline) context).getCoveredBys().add(start);
			((Lifeline) context).getCoveredBys().add(exe);
			((Lifeline) context).getCoveredBys().add(finish);
		}
		if (context instanceof ExecutionSpecification) {
			EList<Lifeline> covereds = ((ExecutionSpecification) context).getCovereds();
			(covereds.get(0)).getCoveredBys().add(start);
			(covereds.get(0)).getCoveredBys().add(exe);
			(covereds.get(0)).getCoveredBys().add(finish);
		}

		start.setExecution(exe);
		finish.setExecution(exe);

		exe.setStart(start);
		exe.setFinish(finish);


	}


	/**
	 * Creates the execution occurrence specification.
	 *
	 * @param exec
	 *            the exec
	 * @param isStart
	 *            the is start
	 * @return the execution occurrence specification
	 */
	public ExecutionOccurrenceSpecification createExecutionOccurrenceSpecification(ExecutionSpecification exec, boolean isStart) {
		ExecutionOccurrenceSpecification createExecutionOccurrenceSpecification = UMLFactory.eINSTANCE.createExecutionOccurrenceSpecification();
		if (isStart) {
			exec.setStart(createExecutionOccurrenceSpecification);
		} else {
			exec.setFinish(createExecutionOccurrenceSpecification);
		}
		return createExecutionOccurrenceSpecification;
	}

	/**
	 * Checks if is nested execution.
	 *
	 * @param execParent
	 *            the exec
	 * @return true, if is nested execution
	 */
	private List<ExecutionSpecification> containedNestedExecution(ExecutionSpecification execParent) {
		List<ExecutionSpecification> results = new ArrayList<>();

		List<EObject> enclosingFragments = FragmentsService.getInstance().getEnclosingFragments(execParent);

		OccurrenceSpecification start = execParent.getStart();
		OccurrenceSpecification finish = execParent.getFinish();

		int indexOfStart = enclosingFragments.indexOf(start);
		int indexOfFinish = enclosingFragments.indexOf(finish);

		for (EObject exec2 : enclosingFragments) {
			if (!exec2.equals(execParent) && exec2 instanceof ExecutionSpecification && !execParent.getCovereds().isEmpty() && !((ExecutionSpecification) exec2).getCovereds().isEmpty()
					&& execParent.getCovereds().get(0).equals(((ExecutionSpecification) exec2).getCovereds().get(0))) {

				OccurrenceSpecification start2 = ((ExecutionSpecification) exec2).getStart();
				OccurrenceSpecification finish2 = ((ExecutionSpecification) exec2).getFinish();

				int indexOfStart2 = enclosingFragments.indexOf(start2);
				int indexOfFinish2 = enclosingFragments.indexOf(finish2);


				if (indexOfStart2 > indexOfStart && indexOfFinish2 < indexOfFinish) {
					results.add((ExecutionSpecification) exec2);
				}


			}
		}

		return results;
	}


	/**
	 * Removes the behavior.
	 *
	 * @param execution
	 *            the execution
	 */
	private void removeBehavior(BehaviorExecutionSpecification execution) {
		final Behavior behavior = execution.getBehavior();
		if (behavior != null) {
			FragmentsService.getInstance().getParentInteraction(execution).getOwnedBehaviors().remove(behavior);
		}
	}





}
