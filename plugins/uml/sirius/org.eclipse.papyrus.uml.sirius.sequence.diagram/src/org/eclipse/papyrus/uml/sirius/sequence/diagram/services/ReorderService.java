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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.LabelServices;
import org.eclipse.papyrus.uml.sirius.sequence.diagram.utils.ReorderSequenceRegistry;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeSpec;
import org.eclipse.sirius.diagram.sequence.ordering.EventEnd;
import org.eclipse.sirius.diagram.sequence.ordering.SingleEventEnd;
import org.eclipse.uml2.uml.CombinedFragment;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ExecutionOccurrenceSpecification;
import org.eclipse.uml2.uml.ExecutionSpecification;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.InteractionOperand;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageEnd;
import org.eclipse.uml2.uml.MessageOccurrenceSpecification;
import org.eclipse.uml2.uml.MessageSort;
import org.eclipse.uml2.uml.OccurrenceSpecification;
import org.eclipse.uml2.uml.StateInvariant;

/**
 * The Class ReorderService.
 *
 */
public class ReorderService {

	/** The fragment service. */
	private final FragmentsService fragmentService = FragmentsService.getInstance();

	/** The service. */
	static private ReorderService service = null;

	/**
	 * Instantiates a new fragments service.
	 */
	private ReorderService() {

	}

	/**
	 * Gets the single instance of FragmentsService.
	 *
	 * @return single instance of FragmentsService
	 */
	static public ReorderService getInstance() {
		if (service == null) {
			service = new ReorderService();
		}
		return service;
	}

	/**
	 * Reorder new fragments.
	 *
	 * @param object                  the object
	 * @param startingEndPredecessor  the starting end predecessor
	 * @param finishingEndPredecessor the finishing end predecessor
	 */
	public void reorderNewFragments(EObject object, EObject startingEndPredecessor, EObject finishingEndPredecessor) {
		if (object instanceof ExecutionSpecification) {
			OccurrenceSpecification start = ((ExecutionSpecification) object).getStart();
			OccurrenceSpecification finish = ((ExecutionSpecification) object).getFinish();
			Interaction interaction = ((ExecutionSpecification) object).getEnclosingInteraction();
			reorderNewFragments(interaction, (SingleEventEnd) startingEndPredecessor,
					(SingleEventEnd) finishingEndPredecessor, (InteractionFragment) start, (InteractionFragment) finish,
					object);
		}

	}

	/**
	 * Reorder new fragments.
	 *
	 * @param object                  the object
	 * @param startingEndPredecessor  the starting end predecessor
	 * @param finishingEndPredecessor the finishing end predecessor
	 * @param instance                the instance
	 */
	public void reorderNewFragments(EObject object, EObject startingEndPredecessor, EObject finishingEndPredecessor,
			EObject instance) {
		reorderNewFragments(object, (EventEnd) startingEndPredecessor, (EventEnd) finishingEndPredecessor, null, null,
				instance);
	}

	/**
	 * Reorder new fragments.
	 *
	 * @param object                  the object
	 * @param startingEndPredecessor  the starting end predecessor
	 * @param finishingEndPredecessor the finishing end predecessor
	 * @param start                   the start
	 * @param finish                  the finish
	 * @param instance                the instance
	 */
	public void reorderNewFragments(EObject object, EventEnd startingEndPredecessor, EventEnd finishingEndPredecessor,
			EObject start, EObject finish, EObject instance) {
		if (object instanceof Interaction) {

			EObject startSemanticEnd = startingEndPredecessor != null ? startingEndPredecessor.getSemanticEnd() : null;
			EObject finishSemanticEnd = finishingEndPredecessor != null ? finishingEndPredecessor.getSemanticEnd()
					: null;

			List<EObject> fragmentsource = fragmentService.getEnclosingFragments(instance);
			List<EObject> fragments = null;
			Interaction parentInteraction = null;
			if (startSemanticEnd != null) {
				fragments = fragmentService.getEnclosingFragments(startSemanticEnd);
			} else {
				parentInteraction = getParentInteraction(instance);
				fragments = fragmentService.getFragmentsAndAnnotation(parentInteraction);
			}
			if (!fragmentsource.equals(fragments) && instance instanceof ExecutionSpecification) {
				fragmentsource.remove(((ExecutionSpecification) instance).getStart());
				fragmentsource.remove(instance);
				fragmentsource.remove(((ExecutionSpecification) instance).getFinish());

				fragments.add(((ExecutionSpecification) instance).getStart());
				fragments.add(instance);
				fragments.add(((ExecutionSpecification) instance).getFinish());
			}

			// add new Execution case
			if ((startSemanticEnd == null && finishSemanticEnd == null)
					|| ((startSemanticEnd != null && finishSemanticEnd != null)
							&& (startSemanticEnd.equals(finishSemanticEnd)))) {
				int indexOf = fragments.indexOf(startSemanticEnd);
				if (indexOf < fragments.size()) {
					int index = 0;
					if (start != null) {
						index++;
						fragments.remove(start);
						fragments.add(indexOf + index, start);
					}
					index++;
					fragments.remove(instance);
					fragments.add(indexOf + index, instance);
					if (finish != null) {
						index++;
						fragments.remove(finish);
						fragments.add(indexOf + index, finish);
					}
				} else {
					if (start != null) {
						fragments.remove(start);
					}
					fragments.remove(instance);
					if (finish != null) {
						fragments.remove(finish);
					}
					if (start != null) {
						fragments.add(start);
					}
					fragments.add(instance);
					if (finish != null) {
						fragments.add(finish);
					}
				}
			}
			fragmentService.updateFragmentList(parentInteraction != null ? parentInteraction : startSemanticEnd,
					fragments);
		}

	}

	/**
	 * Reorder.
	 *
	 * @param associedElements                        the associed elements
	 * @param startingEndPredecessorAfterSemanticEnd  the starting end predecessor
	 *                                                after semantic end
	 * @param finishingEndPredecessorAfterSemanticEnd the finishing end predecessor
	 *                                                after semantic end
	 */
	public void reorder(List<Element> associedElements, EObject startingEndPredecessorAfterSemanticEnd,
			EObject finishingEndPredecessorAfterSemanticEnd) {
		Element element = associedElements.get(0);
		Interaction interaction = null;
		List<EObject> fragments = null;
		if (element instanceof Message) {
			interaction = ((Message) element).getInteraction();
			fragments = fragmentService.computeFragments(((Message) element).getSendEvent(),
					startingEndPredecessorAfterSemanticEnd, interaction);
		}
		if (element instanceof InteractionFragment) {
			interaction = fragmentService.getParentInteraction((InteractionFragment) element);
			fragments = fragmentService.computeFragments(element, startingEndPredecessorAfterSemanticEnd, interaction);
		}

		Map<Element, Integer> map = new HashMap<>();

		for (Element associated : associedElements) {
			if (associated instanceof ExecutionSpecification) {
				OccurrenceSpecification start = ((ExecutionSpecification) associated).getStart();
				OccurrenceSpecification finish = ((ExecutionSpecification) associated).getFinish();
				map.put(start, fragments.indexOf(start));
				map.put(associated, fragments.indexOf(associated));
				map.put(finish, fragments.indexOf(finish));
			}
		}
		List<Element> values = new ArrayList<Element>(map.keySet());
		Collections.sort(values, new Comparator<Element>() {

			@Override
			public int compare(Element o1, Element o2) {
				return map.get(o1).compareTo(map.get(o2));
			}

		});

		int indexOfStart = fragments.indexOf(startingEndPredecessorAfterSemanticEnd);
		for (Element element2 : values) {
			int indexOf = fragments.indexOf(element2);
			if (indexOf <= indexOfStart) {
				indexOfStart--;
			}
			fragments.remove(element2);
		}

		fragments.addAll(indexOfStart + 1, values);

		fragmentService.updateFragmentList(
				startingEndPredecessorAfterSemanticEnd != null ? startingEndPredecessorAfterSemanticEnd : interaction,
				fragments);
	}

	/**
	 * Reorder new combined fragment.
	 *
	 * @param object                  the object
	 * @param startingEndPredecessor  the starting end predecessor
	 * @param finishingEndPredecessor the finishing end predecessor
	 * @param instance                the instance
	 */
	public void reorderNewCombinedFragment(EObject object, SingleEventEnd startingEndPredecessor,
			SingleEventEnd finishingEndPredecessor, EObject instance/* , EObject ocStart, EObject oc */) {
		if (object instanceof Interaction) {
			Interaction interaction = (Interaction) object;
			List<EObject> fragments = fragmentService.getFragmentsAndAnnotation(interaction);

			EObject startSemanticEnd = startingEndPredecessor != null ? startingEndPredecessor.getSemanticEnd() : null;

			// add new Execution case
			int indexOf = fragments.indexOf(startSemanticEnd);
			if (indexOf < fragments.size()) {
				int index = 0;
				index++;
				fragments.remove(instance);
				fragments.add(indexOf + index, instance);
			} else {
				fragments.remove(instance);
				fragments.add(instance);
			}
			fragmentService.updateFragmentList(startSemanticEnd != null ? startSemanticEnd : interaction, fragments);
		}

	}
	
	/**
	 * 
	 * @param container   the InstanceRole
	 * @param newLifeline the lifeline to create
	 * @param predecessor the lifeline that is right after the one we are adding
	 */
	public void reorderNewLifeline(EObject self,EObject variable,Object op) {
		if (self instanceof Interaction) {
			final Interaction ownedInteraction = (Interaction) self;
			final EList<Lifeline> lifelines = ownedInteraction.getLifelines();
			if (op == null) {
				lifelines.move(0, (Lifeline) variable);
			} else {
				int pos = lifelines.indexOf(op);
				lifelines.move(pos + 1, (Lifeline) variable);
			}
		}
	}
	
	/**
	 * 
	 * @param container   the InstanceRole
	 * @param newLifeline the lifeline to create
	 * @param predecessor the lifeline that is right after the one we are adding
	 */
	public void reorderNewLifeline(EObject self,Lifeline variable,Object op) {
		if (op instanceof Lifeline) {
			if (self instanceof Interaction) {
				final Interaction ownedInteraction = (Interaction) self;
				final EList<Lifeline> lifelines = ownedInteraction.getLifelines();
				if (op == null) {
					lifelines.add(0, (Lifeline) variable);
				} else {
					int pos = lifelines.indexOf(op);
					lifelines.add(pos + 1, (Lifeline) variable);
				}
			}
		}
	}

	
	/**
	 * Reorder lifeline horizontally.
	 *
	 * @param movedLifeline     moved lifeline
	 * @param predecessorBefore lifeline predecessor before
	 * @param predecessorAfter  lifeline predecessor after
	 */
	public void reorderLifeline(Lifeline movedLifeline, Lifeline predecessorBefore, Lifeline predecessorAfter) {
		final Interaction ownedInteraction = movedLifeline.getInteraction();
		final EList<Lifeline> lifelines = ownedInteraction.getLifelines();
		final int movedLifelineIndex = lifelines.indexOf(movedLifeline);
		if (predecessorAfter != null) {
			final int predecessorAfterIndex = lifelines.indexOf(predecessorAfter);
			if (movedLifelineIndex > predecessorAfterIndex) {
				// Moved from the right to the left
				lifelines.move(predecessorAfterIndex + 1, movedLifeline);
				return;
			}
			if (movedLifelineIndex < predecessorAfterIndex) {
				// Moved from the left to the right
				lifelines.move(predecessorAfterIndex, movedLifeline);
				return;
			}
		} else {
			// moved at the beginning
			lifelines.move(0, movedLifeline);
		}
	}

	/**
	 * Reorder fragment.
	 *
	 * @param fragment                     Fragment
	 * @param startingEndPredecessorAfter  Starting end predecessor after reorder
	 * @param finishingEndPredecessorAfter Finishing end predecessor after reorder
	 */
	public void reorderFragment(Element fragment, EventEnd startingEndPredecessorAfter,
			EventEnd finishingEndPredecessorAfter) {

		final EObject startingEndPredecessorAfterSemanticEnd = startingEndPredecessorAfter == null ? null
				: (EObject) startingEndPredecessorAfter.getSemanticEnd();
		final EObject finishingEndPredecessorAfterSemanticEnd = finishingEndPredecessorAfter == null ? null
				: (EObject) finishingEndPredecessorAfter.getSemanticEnd();
		if (fragment instanceof CombinedFragment) {
			reorder((CombinedFragment) fragment, startingEndPredecessorAfterSemanticEnd,
					finishingEndPredecessorAfterSemanticEnd);
		} else if (fragment instanceof ExecutionSpecification) {
			reorder((ExecutionSpecification) fragment, startingEndPredecessorAfterSemanticEnd,
					finishingEndPredecessorAfterSemanticEnd);
		} else if (fragment instanceof Message) {
			reorder((Message) fragment, startingEndPredecessorAfterSemanticEnd,
					finishingEndPredecessorAfterSemanticEnd);
		} else if (fragment instanceof StateInvariant) {
			reorder((StateInvariant) fragment, startingEndPredecessorAfterSemanticEnd,
					finishingEndPredecessorAfterSemanticEnd);
		}
	}

	///////// Reorder private method

	/**
	 * Reconnect.
	 *
	 * @param context  the context
	 * @param edgeView the edge view
	 * @param source   the source
	 * @param view     the view
	 */
	public void connectToEvent(EObject context, EObject edgeView, EObject source, EObject view) {

		if (view instanceof DNodeSpec) {
			EObject target = ((DNodeSpec) view).getTarget();
			if (target instanceof ExecutionOccurrenceSpecification) {
				if (context instanceof Message) {
					MessageEnd receiveEvent = ((Message) context).getReceiveEvent();
					ExecutionSpecification execution = ((ExecutionOccurrenceSpecification) target).getExecution();
					if (execution.getStart().equals(target)) {
						execution.setStart((OccurrenceSpecification) receiveEvent);
					}
					if (execution.getFinish().equals(target)) {
						execution.setFinish((OccurrenceSpecification) receiveEvent);
					}
					InteractionFragment enclosingFragment = fragmentService.getEnclosingFragment(execution);
					if (enclosingFragment instanceof Interaction) {
						((Interaction) enclosingFragment).getFragments().remove(target);
					}
					if (enclosingFragment instanceof InteractionOperand) {
						((InteractionOperand) enclosingFragment).getFragments().remove(target);
					}
				}
			}
		}
	}

	/**
	 * Reorder combined fragment.
	 *
	 * @param combinedFragment             Moved combined fragment
	 * @param startingEndPredecessorAfter  Fragment preceding moved combined
	 *                                     fragment start before the beginning of
	 *                                     reorder operation
	 * @param finishingEndPredecessorAfter Fragment preceding moved combined
	 *                                     fragment finish before the beginning of
	 *                                     reorder operation
	 */
	private void reorder(CombinedFragment combinedFragment, EObject startingEndPredecessorAfter,
			EObject finishingEndPredecessorAfter) {

		final Interaction interaction = combinedFragment.getEnclosingInteraction();
		final List<EObject> fragments = fragmentService.getFragmentsAndAnnotation(interaction);

		final boolean combinedFragmentStartPredecessorChanged = combinedFragmentStartPredecessorChanged(
				combinedFragment, startingEndPredecessorAfter);

		if (combinedFragmentStartPredecessorChanged) {
			EAnnotation startOc = null;
			EAnnotation endOc = null;
			for (EObject interactionFragment : fragments) {
				if (interactionFragment instanceof EAnnotation) {
					if (((EAnnotation) interactionFragment).getSource().equals(combinedFragment.getName() + "_start")) {
						startOc = (EAnnotation) interactionFragment;
					}
					if (((EAnnotation) interactionFragment).getSource().equals(combinedFragment.getName() + "_end")) {
						endOc = (EAnnotation) interactionFragment;
					}
				}
			}

			fragments.remove(combinedFragment);
			fragments.remove(startOc);
			fragments.remove(endOc);
			if (isStartOfExecution(startingEndPredecessorAfter, fragments)) {
				fragments.add(fragmentService.getFragmentIndex(startingEndPredecessorAfter, fragments) + 2, startOc);
				fragments.add(fragmentService.getFragmentIndex(startingEndPredecessorAfter, fragments) + 3,
						combinedFragment);
				fragments.add(fragmentService.getFragmentIndex(startingEndPredecessorAfter, fragments) + 4, endOc);
			} else {
				fragments.add(fragmentService.getFragmentIndex(startingEndPredecessorAfter, fragments) + 1, startOc);
				fragments.add(fragmentService.getFragmentIndex(startingEndPredecessorAfter, fragments) + 2,
						combinedFragment);
				fragments.add(fragmentService.getFragmentIndex(startingEndPredecessorAfter, fragments) + 3, endOc);
			}
		}
		fragmentService.updateFragmentList(interaction, fragments);
	}

	/**
	 * Reorder.
	 *
	 * @param combinedFragment             the combined fragment
	 * @param startingEndPredecessorAfter  the starting end predecessor after
	 * @param finishingEndPredecessorAfter the finishing end predecessor after
	 */
	private void reorder(StateInvariant combinedFragment, EObject startingEndPredecessorAfter,
			EObject finishingEndPredecessorAfter) {

		final Interaction interaction = combinedFragment.getEnclosingInteraction();
		final List<EObject> fragments = fragmentService.getFragmentsAndAnnotation(interaction);

		final boolean combinedFragmentStartPredecessorChanged = stateInvariantStartPredecessorChanged(combinedFragment,
				startingEndPredecessorAfter);

		if (combinedFragmentStartPredecessorChanged) {
			fragments.remove(combinedFragment);
			if (isStartOfExecution(startingEndPredecessorAfter, fragments)) {
				fragments.add(fragmentService.getFragmentIndex(startingEndPredecessorAfter, fragments) + 2,
						combinedFragment);
			} else {
				fragments.add(fragmentService.getFragmentIndex(startingEndPredecessorAfter, fragments) + 1,
						combinedFragment);
			}
		}
		fragmentService.updateFragmentList(interaction, fragments);
	}

	/**
	 * Reorder execution.
	 *
	 * @param execution                    Moved execution
	 * @param startingEndPredecessorAfter  Fragment preceding moved execution start
	 *                                     before the beginning of reorder operation
	 * @param finishingEndPredecessorAfter Fragment preceding moved execution finish
	 *                                     before the beginning of reorder operation
	 */
	private void reorder(ExecutionSpecification execution, EObject startingEndPredecessorAfter,
			EObject finishingEndPredecessorAfter) {
		Interaction parentInteraction = fragmentService.getParentInteraction(execution);
		List<EObject> fragments = fragmentService.computeFragments(execution, startingEndPredecessorAfter,
				parentInteraction);

		final boolean executionFinishPredecessorChanged = executionFinishPredecessorChanged(execution,
				finishingEndPredecessorAfter);
		final boolean executionStartPredecessorChanged = executionStartPredecessorChanged(execution,
				startingEndPredecessorAfter);

		OccurrenceSpecification start = execution.getStart();
		OccurrenceSpecification finish = execution.getFinish();

		if (executionStartPredecessorChanged) {
			if (start instanceof MessageOccurrenceSpecification) {
				List<EObject> fragmentsource = FragmentsService.getInstance().getEnclosingFragments(execution);
				replaceMessageOccByExecOcc(parentInteraction, fragmentsource, start, true);
				start = execution.getStart();
			}
			fragments.remove(start);
			fragments.remove(execution);
			// manage start execution moving
			if (ReorderSequenceRegistry.getInstance().containsKey(startingEndPredecessorAfter)) {
				startingEndPredecessorAfter = ReorderSequenceRegistry.getInstance().get(startingEndPredecessorAfter);
			}
			addFragments(startingEndPredecessorAfter, fragments, start);

			// manage execution moving
			fragments.add(fragmentService.getFragmentIndex(start, fragments) + 1, execution);

		}
		if (executionFinishPredecessorChanged) {
			if (finish instanceof MessageOccurrenceSpecification) {
				List<EObject> fragmentsource = FragmentsService.getInstance().getEnclosingFragments(execution);
				replaceMessageOccByExecOcc(parentInteraction, fragmentsource, finish, false);
				finish = execution.getFinish();
			}
			fragments.remove(finish);
			// manage finish execution moving
			if (ReorderSequenceRegistry.getInstance().containsKey(finishingEndPredecessorAfter)) {
				finishingEndPredecessorAfter = ReorderSequenceRegistry.getInstance().get(finishingEndPredecessorAfter);
			}
			addFragments(finishingEndPredecessorAfter, fragments, finish);

		}
		fragmentService.updateFragmentList(
				startingEndPredecessorAfter != null ? startingEndPredecessorAfter : parentInteraction, fragments);
	}

	/**
	 * Replace message occ by exec occ.
	 *
	 * @param parentInteraction the parent interaction
	 * @param fragments         the fragments
	 * @param occ               the occ
	 * @param isStart           the is start
	 */
	private void replaceMessageOccByExecOcc(Interaction parentInteraction, List<EObject> fragments,
			OccurrenceSpecification occ, boolean isStart) {
		// 1. get the opposite messageOcc
		// MessageOccurrenceSpecification oppStart =
		// getMessageOccOpposite((MessageOccurrenceSpecification) occ);
		// 1.1 check if opposite mesageOcc is linked at Execution
		ExecutionSpecification assExec = getExecutionFromMessageOccurence((MessageOccurrenceSpecification) occ);

		if (assExec != null) {
			// 1.2 create the ExectionOcc
			boolean isStartOcc = assExec.getStart().equals(occ);
			ExecutionOccurrenceSpecification newExecOcc = ExecutionService.getInstance()
					.createExecutionOccurrenceSpecification(assExec, isStartOcc);
			ReorderSequenceRegistry.getInstance().put(occ, newExecOcc);
			parentInteraction.getFragments().add(newExecOcc);
			newExecOcc.setName(computeDefaultName(newExecOcc));
			parentInteraction.getFragments().remove(newExecOcc);
			// 1.3 replace the opposite message occ by the ExecutionOcc
			if (isStartOcc) {
				assExec.setStart(newExecOcc);

			} else {
				assExec.setFinish(newExecOcc);
			}
			newExecOcc.setExecution(assExec);
			int indexOfExec = fragments.indexOf(assExec);
			if (isStartOcc) {
				fragments.add(indexOfExec, newExecOcc);
			} else {
				if (indexOfExec >= fragments.size() - 1) {
					fragments.add(newExecOcc);
				} else {
					fragments.add(indexOfExec + 1, newExecOcc);
				}
			}
		}
	}

	/**
	 * Adds the fragments.
	 *
	 * @param endPredecessorAfter the end predecessor after
	 * @param fragments           the fragments
	 * @param occ                 the occ
	 */
	private void addFragments(EObject endPredecessorAfter, List<EObject> fragments, OccurrenceSpecification occ) {
		if (isStartOfExecution(endPredecessorAfter, fragments)) {
			fragments.add(fragmentService.getFragmentIndex(endPredecessorAfter, fragments) + 2, occ);
		} else {
			fragments.add(endPredecessorAfter == null ? 0
					: fragmentService.getFragmentIndex(endPredecessorAfter, fragments) + 1, occ);
		}
	}

	/**
	 * Reorder message.
	 *
	 * @param message                      Moved message
	 * @param startingEndPredecessorAfter  Fragment preceding moved execution start
	 *                                     before the beginning of reorder operation
	 * @param finishingEndPredecessorAfter Fragment preceding moved execution finish
	 *                                     before the beginning of reorder operation
	 */
	private void reorder(Message message, EObject startingEndPredecessorAfter, EObject finishingEndPredecessorAfter) {
		if (isMoveAuthorized(message)) {
			final Interaction interaction = message.getInteraction();

			MessageEnd sendEvent = message.getSendEvent();
			MessageEnd receiveEvent = message.getReceiveEvent();

			final List<EObject> fragments = fragmentService.computeFragments(sendEvent, startingEndPredecessorAfter,
					interaction);

			final boolean messageStartPredecessorChanged = messageStartPredecessorChanged(message,
					startingEndPredecessorAfter);

			if (ReorderSequenceRegistry.getInstance().containsKey(sendEvent)) {
				ExecutionOccurrenceSpecification execOcc1 = (ExecutionOccurrenceSpecification) ReorderSequenceRegistry
						.getInstance().get(sendEvent);
				fragments.remove(sendEvent);
				replaceExecByMessage(sendEvent, fragments, execOcc1);

				if (ReorderSequenceRegistry.getInstance().containsKey(receiveEvent)) {
					ExecutionOccurrenceSpecification execOcc2 = (ExecutionOccurrenceSpecification) ReorderSequenceRegistry
							.getInstance().get(receiveEvent);
					replaceExecByMessage(receiveEvent, fragments, execOcc2);
				} else {

					ExecutionSpecification executionFromMessageOccurence = getExecutionFromMessageOccurence(
							(MessageOccurrenceSpecification) receiveEvent);
					if (executionFromMessageOccurence != null) {
						boolean isStart = executionFromMessageOccurence.getStart().equals(receiveEvent);
						replaceMessageOccByExecOcc(interaction, fragments,
								(MessageOccurrenceSpecification) receiveEvent, isStart);
						int indexOfSendEvent = fragments.indexOf(sendEvent);
						fragments.remove(receiveEvent);
						if (indexOfSendEvent >= fragments.size() - 1) {
							fragments.add(receiveEvent);
						} else {
							fragments.add(indexOfSendEvent + 1, receiveEvent);
						}
					}

				}

			} else if (ReorderSequenceRegistry.getInstance().containsKey(receiveEvent)) {
				ExecutionOccurrenceSpecification execOcc1 = (ExecutionOccurrenceSpecification) ReorderSequenceRegistry
						.getInstance().get(receiveEvent);
				fragments.remove(receiveEvent);
				replaceExecByMessage(receiveEvent, fragments, execOcc1);

				ExecutionSpecification executionFromMessageOccurence = getExecutionFromMessageOccurence(
						(MessageOccurrenceSpecification) sendEvent);
				if (executionFromMessageOccurence != null) {
					boolean isStart = executionFromMessageOccurence.getStart().equals(sendEvent);
					replaceMessageOccByExecOcc(interaction, fragments, (MessageOccurrenceSpecification) sendEvent,
							isStart);
					int indexOfSendEvent = fragments.indexOf(receiveEvent);
					fragments.remove(sendEvent);
					if (indexOfSendEvent >= fragments.size() - 1) {
						fragments.add(sendEvent);
					} else {
						fragments.add(indexOfSendEvent, sendEvent);
					}
				}

			}

			else {
				if (messageStartPredecessorChanged) {
					boolean startOfExecution = isStartOfExecution(startingEndPredecessorAfter, fragments);
					int fragmentIndex = fragmentService.getFragmentIndex(startingEndPredecessorAfter, fragments);
					int indexOfSend = fragments.indexOf(sendEvent);
					if (indexOfSend <= fragmentIndex) {
						fragmentIndex--;
					}
					fragments.remove(sendEvent);
					int indexOfReceive = fragments.indexOf(receiveEvent);
					if (indexOfReceive <= fragmentIndex) {
						fragmentIndex--;
					}
					fragments.remove(receiveEvent);

					if (startOfExecution) {
						fragments.add(fragmentIndex + 2, sendEvent);
					} else {
						fragments.add(fragmentIndex + 1, sendEvent);
					}

					fragments.add(fragmentService.getFragmentIndex(sendEvent, fragments) + 1, receiveEvent);
				}
			}

			// Move reply for synchronous message
			if (message.getMessageSort().equals(MessageSort.SYNCH_CALL_LITERAL)) {
				final Message replyMessage = MessageService.getInstance().getReplyMessage(message);
				if (replyMessage != null) {
					fragments.remove(replyMessage.getSendEvent());
					fragments.remove(replyMessage.getReceiveEvent());

					if (isStartOfExecution(receiveEvent, fragments)) {
						fragments.add(fragmentService.getFragmentIndex(receiveEvent, fragments) + 2,
								replyMessage.getSendEvent());
					} else {
						fragments.add(fragmentService.getFragmentIndex(receiveEvent, fragments) + 1,
								replyMessage.getSendEvent());
					}

					fragments.add(fragmentService.getFragmentIndex(replyMessage.getSendEvent(), fragments) + 1,
							replyMessage.getReceiveEvent());
				}
			}
			fragmentService.updateFragmentList(
					startingEndPredecessorAfter != null ? startingEndPredecessorAfter : interaction, fragments);
		}

	}

	/**
	 * Replace exec by message.
	 *
	 * @param sendEvent the send event
	 * @param fragments the fragments
	 * @param execOcc1  the exec occ 1
	 */
	public void replaceExecByMessage(MessageEnd sendEvent, final List<EObject> fragments,
			ExecutionOccurrenceSpecification execOcc1) {
		int indexOfExec1 = fragments.indexOf(execOcc1);
		ExecutionSpecification execution1 = execOcc1.getExecution();
		if (execution1.getStart().equals(execOcc1)) {
			execution1.setStart((OccurrenceSpecification) sendEvent);
		} else {
			execution1.setFinish((OccurrenceSpecification) sendEvent);
		}
		fragments.remove(execOcc1);
		fragments.add(indexOfExec1, sendEvent);
	}

	/**
	 * Replace exec by message.
	 *
	 * @param sendEvent the send event
	 * @param fragments the fragments
	 * @param execOcc1  the exec occ 1
	 */
	
	public void replaceExecByMessage(MessageEnd sendEvent, final EList<InteractionFragment> fragments,
			ExecutionOccurrenceSpecification execOcc1) {
		int indexOfExec1 = fragments.indexOf(execOcc1);
		ExecutionSpecification execution1 = execOcc1.getExecution();
		if (execution1.getStart().equals(execOcc1)) {
			execution1.setStart((OccurrenceSpecification) sendEvent);
		} else {
			execution1.setFinish((OccurrenceSpecification) sendEvent);
		}
		fragments.remove(execOcc1);
		fragments.add(indexOfExec1, (MessageOccurrenceSpecification) sendEvent);
	}

	/**
	 * Gets the parent interaction.
	 *
	 * @param fragment the fragment
	 * @return the parent interaction
	 */
	private Interaction getParentInteraction(EObject fragment) {
		if (fragment instanceof InteractionFragment) {
			return fragmentService.getParentInteraction((InteractionFragment) fragment);
		}
		return null;
	}

	/**
	 * Gets the execution from message occurence.
	 *
	 * @param messageOcc the message occ
	 * @return the execution from message occurence
	 */
	public ExecutionSpecification getExecutionFromMessageOccurence(MessageOccurrenceSpecification messageOcc) {

		List<EObject> enclosingFragments = fragmentService.getEnclosingFragments(messageOcc);
		for (EObject eEObject : enclosingFragments) {
			if (eEObject instanceof ExecutionSpecification) {
				if (((ExecutionSpecification) eEObject).getStart().equals(messageOcc)
						|| ((ExecutionSpecification) eEObject).getFinish().equals(messageOcc)) {
					return (ExecutionSpecification) eEObject;
				}
			}
		}

		return null;
	}

	/**
	 * Checks if is start of execution.
	 *
	 * @param startingEndPredecessorAfter the starting end predecessor after
	 * @param fragments                   the fragments
	 * @return true, if is start of execution
	 */
	public boolean isStartOfExecution(EObject startingEndPredecessorAfter, List<EObject> fragments) {

		if (startingEndPredecessorAfter instanceof ExecutionOccurrenceSpecification) {
			final ExecutionOccurrenceSpecification executionStart = (ExecutionOccurrenceSpecification) startingEndPredecessorAfter;
			if (executionStart.getExecution().getStart().equals(executionStart)) {
				return true;
			}
		}

		if (startingEndPredecessorAfter instanceof MessageOccurrenceSpecification) {
			if (fragments.indexOf(startingEndPredecessorAfter) + 1 < fragments.size()) {
				final EObject candidate = fragments.get(fragments.indexOf(startingEndPredecessorAfter) + 1);
				if (candidate instanceof ExecutionSpecification) {
					final ExecutionSpecification behaviorExecution = (ExecutionSpecification) candidate;
					if (behaviorExecution.getStart().equals(startingEndPredecessorAfter)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Checks if is move authorized.
	 *
	 * @param message the message
	 * @return true, if is move authorized
	 */
	private boolean isMoveAuthorized(Message message) {
		if (message.getMessageSort().equals(MessageSort.REPLY_LITERAL)) {
			return false;
		}
		return true;
	}

	/**
	 * Combined fragment start predecessor changed.
	 *
	 * @param combinedFragment            the combined fragment
	 * @param startingEndPredecessorAfter the starting end predecessor after
	 * @return true, if successful
	 */
	private boolean combinedFragmentStartPredecessorChanged(CombinedFragment combinedFragment,
			EObject startingEndPredecessorAfter) {
		final Interaction interaction = combinedFragment.getEnclosingInteraction();
		final List<EObject> fragments = fragmentService.getFragmentsAndAnnotation(interaction);
		final EObject initialPredecessor = fragments.get(fragments.indexOf(combinedFragment) - 1);

		return !initialPredecessor.equals(startingEndPredecessorAfter);
	}

	/**
	 * State invariant start predecessor changed.
	 *
	 * @param stateInvariant              the state invariant
	 * @param startingEndPredecessorAfter the starting end predecessor after
	 * @return true, if successful
	 */
	private boolean stateInvariantStartPredecessorChanged(StateInvariant stateInvariant,
			EObject startingEndPredecessorAfter) {
		final Interaction interaction = stateInvariant.getEnclosingInteraction();
		final List<EObject> fragments = fragmentService.getFragmentsAndAnnotation(interaction);
		int indexOf = fragments.indexOf(stateInvariant);
		if (indexOf == 0) {
			return true;
		}
		final EObject initialPredecessor = fragments.get(indexOf - 1);

		return !initialPredecessor.equals(startingEndPredecessorAfter);
	}

	/**
	 * Execution start predecessor changed.
	 *
	 * @param execution                   the execution
	 * @param startingEndPredecessorAfter the starting end predecessor after
	 * @return true, if successful
	 */
	private boolean executionStartPredecessorChanged(ExecutionSpecification execution,
			EObject startingEndPredecessorAfter) {
		return true;
	}

	/**
	 * Execution finish predecessor changed.
	 *
	 * @param execution                    the execution
	 * @param finishingEndPredecessorAfter the finishing end predecessor after
	 * @return true, if successful
	 */
	private boolean executionFinishPredecessorChanged(ExecutionSpecification execution,
			EObject finishingEndPredecessorAfter) {
		final List<EObject> fragments = fragmentService.getEnclosingFragments(execution);
		int index = fragments.indexOf(execution.getFinish());
		if (index == 0) {
			return false;
		}
		final EObject initialPredecessor = fragments.get(index - 1);

		return !initialPredecessor.equals(finishingEndPredecessorAfter);
	}

	/**
	 * Message start predecessor changed.
	 *
	 * @param message                     the message
	 * @param startingEndPredecessorAfter the starting end predecessor after
	 * @return true, if successful
	 */
	private boolean messageStartPredecessorChanged(Message message, EObject startingEndPredecessorAfter) {
		final InteractionFragment interaction = fragmentService.getEnclosingFragment(message.getSendEvent());
		final List<EObject> fragments = fragmentService.getFragmentsAndAnnotation(interaction);
		int index = fragments.indexOf(message.getSendEvent()) - 1;
		if (index == -1) {
			return true;
		}
		final EObject initialPredecessor = fragments.get(index);

		return !initialPredecessor.equals(startingEndPredecessorAfter);

	}

	/**
	 * Compute default name.
	 *
	 * @param element New element
	 * @return Name for the new element, he name will looks like 'ElementType'+total
	 *         of existing elements of the same type.
	 */
	private String computeDefaultName(final EObject element) {
		return LabelServices.INSTANCE.computeDefaultName(element);
	}

}
