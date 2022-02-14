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

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.AbstractDiagramServices;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.LabelServices;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.description.style.Side;
import org.eclipse.sirius.diagram.sequence.SequenceDDiagram;
import org.eclipse.sirius.diagram.sequence.ordering.EventEnd;
import org.eclipse.sirius.diagram.sequence.ordering.SingleEventEnd;
import org.eclipse.sirius.diagram.ui.business.api.view.SiriusGMFHelper;
import org.eclipse.uml2.uml.CombinedFragment;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DurationConstraint;
import org.eclipse.uml2.uml.DurationObservation;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ExecutionOccurrenceSpecification;
import org.eclipse.uml2.uml.ExecutionSpecification;
import org.eclipse.uml2.uml.GeneralOrdering;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageSort;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.TimeConstraint;
import org.eclipse.uml2.uml.TimeObservation;

/**
 * A set of services to handle the Sequence diagram.
 *
 * @author Melanie Bats <a href="mailto:melanie.bats@obeo.fr">melanie.bats@obeo.fr</a>
 */
public class SequenceDiagramServices extends AbstractDiagramServices {


	/** The comment service. */
	private CommentService commentService = CommentService.getInstance();

	/** The constraint service. */
	private ConstraintService constraintService = ConstraintService.getInstance();

	/** The execution service. */
	private ExecutionService executionService = ExecutionService.getInstance();

	/** The combined fragment operand service. */
	private CombinedAndOperandService combinedFragmentOperandService = CombinedAndOperandService.getInstance();

	/** The reorder service. */
	private ReorderService reorderService = ReorderService.getInstance();

	/** The message service. */
	private MessageService messageService = MessageService.getInstance();

	/** The lifeline service. */
	private LifelineService lifelineService = LifelineService.getInstance();
	/** The time observation constraint service. */
	private TimeObservationConstraintService timeObservationConstraintService = TimeObservationConstraintService.getInstance();
	/** The fragments service. */
	private FragmentsService fragmentsService = FragmentsService.getInstance();


	/** The is break. */
	private static boolean isBreak = false;


	/////////////////////// METHODE CALL FROM ODESIGN///////////////////////////


	// -------- FRAGMENTS MANAGEMENT



	/**
	 * Gets the finish.
	 *
	 * @param o
	 *            the o
	 * @return the finish
	 */
	public EObject getFinish(EObject o) {
		return fragmentsService.getFinish(o);
	}


	/**
	 * Gets the start.
	 *
	 * @param o
	 *            the o
	 * @return the start
	 */
	public EObject getStart(EObject o) {
		return fragmentsService.getStart(o);
	}

	/**
	 * Make union.
	 *
	 * @param context
	 *            the context
	 * @return the list
	 */
	public List<InteractionFragment> makeUnion(EObject context) {
		return fragmentsService.makeUnion(context);
	}



	/**
	 * Eol precondition.
	 *
	 * @param p
	 *            the p
	 * @return true, if successful
	 */
	public boolean eolPrecondition(Lifeline p) {
		return fragmentsService.eolPrecondition(p);
	}

	/**
	 * Gets the fragments ordering ends.
	 *
	 * @param object
	 *            the object
	 * @return the fragments ordering ends
	 */
	public List<EObject> getFragmentsOrderingEnds(EObject object) {
		return fragmentsService.getFragmentsOrderingEnds(object);
	}



	// ------------ MANAGEMENT DIAGRAM FEATURE
	/**
	 * Get sequence diagram label.
	 *
	 * @param interaction
	 *            Interaction associated to sequence diagram
	 * @return SEquence diagram label
	 */
	public String getSequenceDiagramName(Interaction interaction) {
		return LabelServices.INSTANCE.getSequenceDiagramName(interaction);
	}

	/**
	 * Compute semantic elements.
	 *
	 * @param exec
	 *            the exec
	 * @return the collection
	 */
	public Collection<EObject> computeSemanticElements(ExecutionSpecification exec) {
		return new LinkedHashSet<EObject>(Arrays.asList(exec, exec.getStart(), exec.getFinish(), exec.getCovereds().get(0)));
	}


	// -------- MANAGEMENT TIME, OBSERVATION AND CONSTRAINTE


	/**
	 * Gets the time constraint.
	 *
	 * @param context
	 *            the context
	 * @return the time constraint
	 */
	public List<InteractionFragment> getTimeConstraint(EObject context) {
		return timeObservationConstraintService.getTimeConstraint(context);
	}



	/**
	 * Gets the side.
	 *
	 * @param context
	 *            the context
	 * @return the side
	 */
	public List<Side> getSide(EObject context) {
		return timeObservationConstraintService.getSide(context);
	}



	/**
	 * Creates the general ordering.
	 *
	 * @param context
	 *            the context
	 * @param sourceVariable
	 *            the source variable
	 * @param targetVariable
	 *            the target variable
	 * @return the general ordering
	 */
	public GeneralOrdering createGeneralOrdering(EObject context, EObject sourceVariable, EObject targetVariable) {
		return GeneralOrderingService.getInstance().createGeneralOrdering(context, sourceVariable, targetVariable);
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
		return timeObservationConstraintService.createDurationObservation(context, sourceVariable, targetVariable);
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

		return timeObservationConstraintService.createDurationConstraint(context, sourceVariable, targetVariable);
	}





	/**
	 * Gets the all duration observation.
	 *
	 * @param context
	 *            the context
	 * @return the all duration observation
	 */
	public List<DurationObservation> getAllDurationObservation(EObject context) {
		return timeObservationConstraintService.getAllDurationObservation(context);
	}


	/**
	 * Gets the time constraints.
	 *
	 * @param context
	 *            the context
	 * @return the time constraints
	 */
	public List<TimeConstraint> getTimeConstraints(EObject context) {
		return timeObservationConstraintService.getTimeConstraints(context);
	}


	/**
	 * Checks if is constrained time.
	 *
	 * @param context the context
	 * @return true, if is constrained time
	 */
	public boolean isConstrainedTime(EObject context) {
		return constraintService.isConstrainedTime(context);
	}



	/**
	 * Gets the time observations.
	 *
	 * @param context
	 *            the context
	 * @return the time observations
	 */
	public List<TimeObservation> getTimeObservations(EObject context) {
		return timeObservationConstraintService.getTimeObservations(context);
	}


	/**
	 * Gets the source constrained element.
	 *
	 * @param context
	 *            the context
	 * @return the source constrained element
	 */
	public EObject getSourceConstrainedElement(EObject context) {
		return constraintService.getSourceConstrainedElement(context);
	}

	/**
	 * Gets the target constrained element.
	 *
	 * @param context
	 *            the context
	 * @return the target constrained element
	 */
	public EObject getTargetConstrainedElement(EObject context) {
		return constraintService.getTargetConstrainedElement(context);
	}


	/**
	 * Gets the source constrained element.
	 *
	 * @param context
	 *            the context
	 * @return the source constrained element
	 */
	public EObject getSourceObservation(EObject context) {
		return constraintService.getSourceObservation(context);
	}

	/**
	 * Gets the target constrained element.
	 *
	 * @param context
	 *            the context
	 * @return the target constrained element
	 */
	public EObject getTargetObservation(EObject context) {
		return constraintService.getTargetObservation(context);
	}


	// ------------------- MANGEMENT LIFELINE


	/**
	 * Compute lifeline comment label.
	 *
	 * @param lifeline
	 *            Lifeline
	 * @return LAbel
	 */
	public String computeLifelineCommentLabel(Lifeline lifeline) {
		return lifelineService.computeLifelineCommentLabel(lifeline);
	}

	/**
	 * Check if lifeline is representing a property.
	 *
	 * @param element
	 *            Lifeline
	 * @return True iflifeline is representing a property
	 */
	public boolean isRepresentingProperty(Lifeline element) {
		return lifelineService.isRepresentingProperty(element);
	}

	/**
	 * Delete lifeline.
	 *
	 * @param lifeline
	 *            Lifeline to delete
	 */
	public void delete(Lifeline lifeline) {
		lifelineService.delete(lifeline);
	}



	// --------------- MANAGE MESSAGE



	/**
	 * Checks if is break.
	 *
	 * @param context
	 *            the context
	 * @return true, if is break
	 */
	public boolean isNotBreak(EObject context) {
		return !isBreak;
	}

	/**
	 * Inits the break.
	 *
	 * @param context
	 *            the context
	 */
	public void initBreak(EObject context) {
		isBreak = false;
	}

	/**
	 * Check is selection.
	 *
	 * @param sourceView
	 *            the source view
	 * @param targetView
	 *            the target view
	 * @return true, if successful
	 */
	public boolean checkIsOccurenceSpecSelection(EObject sourceView, EObject targetView) {
		boolean checkIsOccurenceSpecSelection = messageService.checkIsOccurenceSpecSelection(sourceView, targetView);
		if (checkIsOccurenceSpecSelection) {
			isBreak = true;
		}
		return checkIsOccurenceSpecSelection;
	}


	/**
	 * Check is gate selection.
	 *
	 * @param sourceView
	 *            the source view
	 * @param targetView
	 *            the target view
	 * @return true, if successful
	 */
	public boolean checkIsGateSelection(EObject sourceView, EObject targetView) {
		boolean checkIsGateSelection = messageService.checkIsGateSelection(sourceView, targetView);
		if (checkIsGateSelection) {
			isBreak = true;
		}
		return checkIsGateSelection;
	}





	/**
	 * Check is exection selection.
	 *
	 * @param sourceView
	 *            the source view
	 * @param targetView
	 *            the target view
	 * @return true, if successful
	 */
	public boolean checkIsExecutionSelection(EObject sourceView, EObject targetView) {
		boolean checkIsExecutionSelection = messageService.checkIsExecutionSelection(sourceView, targetView);
		if (checkIsExecutionSelection) {
			isBreak = true;
		}
		return checkIsExecutionSelection;
	}


	/**
	 * Delete message.
	 *
	 * @param context
	 *            the context
	 * @param sourceVariable
	 *            the source variable
	 * @return the message
	 */
	public Message foundMessage(EObject context, EObject sourceVariable) {
		return messageService.foundMessage(context, sourceVariable);
	}


	/**
	 * Delete message.
	 *
	 * @param context
	 *            the context
	 * @param sourceVariable
	 *            the source variable
	 * @return the message
	 */
	public Message lostMessage(EObject context, EObject sourceVariable) {
		return messageService.lostMessage(context, sourceVariable);
	}

	/**
	 * Gets the lost receive annotation.
	 *
	 * @param context
	 *            the context
	 * @return the lost receive annotation
	 */
	public EObject getLostReceiveAnnotation(EObject context) {

		return messageService.getLostReceiveAnnotation(context);
	}


	/**
	 * Checks if is lost found target valid.
	 *
	 * @param context
	 *            the context
	 * @return true, if is lost found target valid
	 */
	public boolean isLostFoundTargetValid(EObject context) {
		return messageService.isLostFoundTargetValid(context);
	}


	/**
	 * Creates the reply message.
	 *
	 * @param context
	 *            the context
	 * @param sourceFragment
	 *            the source fragment
	 * @param targetFragment
	 *            the target fragment
	 * @param startingEndPredecessor
	 *            the starting end predecessor
	 * @param finishingEndPredecessor
	 *            the finishing end predecessor
	 */
	public void createReplyMessage(EObject context, NamedElement sourceFragment,
			NamedElement targetFragment, EventEnd startingEndPredecessor, EventEnd finishingEndPredecessor) {
		messageService.createReplyMessage(context, sourceFragment, targetFragment, startingEndPredecessor, finishingEndPredecessor);
	}



	/**
	 * Create Delete message.
	 *
	 * @param context
	 *            the context
	 * @param sourceVariable
	 *            the source variable
	 * @param targetVariable
	 *            the target variable
	 * @param startingEndPredecessor
	 *            the starting end predecessor
	 * @param finishingEndPredecessor
	 *            the finishing end predecessor
	 * @return the message
	 */
	public Message deleteMessage(EObject context, EObject sourceVariable, Lifeline targetVariable, EventEnd startingEndPredecessor, EventEnd finishingEndPredecessor) {
		return messageService.deleteMessage(context, sourceVariable, targetVariable, startingEndPredecessor, finishingEndPredecessor);
	}

	/**
	 * Creates the message.
	 *
	 * @param context
	 *            the context
	 * @param sourceV
	 *            the source V
	 * @param targetV
	 *            the target V
	 * @param startingEndPredecessor
	 *            the starting end predecessor
	 * @param finishingEndPredecessor
	 *            the finishing end predecessor
	 * @return the message
	 */
	public Message createMessage(EObject context, EObject sourceV, Lifeline targetV, EventEnd startingEndPredecessor, EventEnd finishingEndPredecessor) {
		return messageService.createMessage(context, sourceV, targetV, startingEndPredecessor, finishingEndPredecessor);
	}

	/**
	 * Creates the message.
	 *
	 * @param target
	 *            the target
	 * @param source
	 *            the source
	 * @return the message
	 */
	public Message createMessage(Lifeline target, EObject source) {
		return messageService.createMessage(source, target);
	}


	/**
	 * Creates the synchronous message.
	 *
	 * @param target
	 *            the target
	 * @param source
	 *            the source
	 */
	public void createSynchronousMessage(NamedElement target, NamedElement source) {
		messageService.createSynchronousMessage(target, source);
	}

	/**
	 * Creates the A synchronous message.
	 *
	 * @param target
	 *            the target
	 * @param source
	 *            the source
	 */
	public void createASynchronousMessage(NamedElement target, NamedElement source) {
		messageService.createASynchronousMessage(target, source);
	}



	/**
	 * Create an operation and an asynchronous message from a lifeline or an execution. Create the operation
	 * in the class and the asynchronous message in the interaction.
	 *
	 * @param target
	 *            Target message element, it could be a lifeline or an execution
	 * @param source
	 *            Source message element, it could be a lifeline or an execution
	 * @param startingEndPredecessor
	 *            Start predecessor
	 * @param finishingEndPredecessor
	 *            Finish predecessor
	 */
	public void createOperationAndAsynchMessage(NamedElement target, NamedElement source,
			EventEnd startingEndPredecessor, EventEnd finishingEndPredecessor) {
		messageService.createOperationAndAsynchMessage(target, source, startingEndPredecessor, finishingEndPredecessor);
	}



	/**
	 * Create an operation and an synchronous message from a lifeline or an execution. Create the operation in
	 * the class and the synchronous message in the interaction.
	 *
	 * @param target
	 *            Target message element, it could be a lifeline or an execution
	 * @param source
	 *            Source message element, it could be a lifeline or an execution
	 * @param startingEndPredecessor
	 *            Start predecessor
	 * @param finishingEndPredecessor
	 *            Finish predecessor
	 */
	public void createOperationAndSynchMessage(NamedElement target, NamedElement source,
			EventEnd startingEndPredecessor, EventEnd finishingEndPredecessor) {
		messageService.createOperationAndSynchMessage(target, source, startingEndPredecessor, finishingEndPredecessor);
	}




	/**
	 * Check if element is a valid message end.
	 *
	 * @param element
	 *            Element
	 * @return True if element is a valid message end
	 */
	public boolean isValidMessageEnd(Element element) {
		return messageService.isValidMessageEnd(element);
	}

	/**
	 * Check if element is a valid message end.
	 *
	 * @param preTarget
	 *            Element
	 * @return True if element is a valid message end
	 */
	public boolean isValidMessageEnd(EObject preTarget) {
		return messageService.isValidMessageEnd(preTarget);
	}





	/**
	 * Check if message is not a reply message.
	 *
	 * @param message
	 *            Message
	 * @return True if message is not a reply message
	 */
	public boolean isNotReply(Message message) {
		return messageService.isNotReply(message);
	}

	/**
	 * Check if message is a reply message.
	 *
	 * @param message
	 *            Message
	 * @return True if message is a reply message
	 */
	public boolean isReply(Message message) {
		return messageService.isReply(message);
	}

	/**
	 * Find occurrence specification context for a receive event.
	 *
	 * @param message
	 *            Message
	 * @return Occurrence specification context
	 */
	public NamedElement findOccurrenceSpecificationContextForReceiveEvent(Message message) {
		return messageService.findOccurrenceSpecificationContextForReceiveEvent(message);
	}


	/**
	 * Find occurrence specification context for a send event.
	 *
	 * @param message
	 *            Message
	 * @return Occurrence specification context
	 */
	public NamedElement findOccurrenceSpecificationContextForSendEvent(Message message) {
		return messageService.findOccurrenceSpecificationContextForSendEvent(message);
	}

	/**
	 * Check if message is a synchronous call.
	 *
	 * @param message
	 *            Message
	 * @return True if message is a synchronous call
	 */
	public boolean isSynchCall(Message message) {
		return messageService.isSynchCall(message);
	}

	/**
	 * Gets the invocation message.
	 *
	 * @param object
	 *            the object
	 * @return the invocation message
	 */
	public Message getInvocationMessage(EObject object) {
		return messageService.getInvocationMessage(object);
	}

	/**
	 * Gets the message associated elements.
	 *
	 * @param msg
	 *            the msg
	 * @return the message associated elements
	 */
	public Collection<EObject> getMessageAssociatedElements(Message msg) {
		return messageService.getMessageAssociatedElements(msg);
	}

	/**
	 * Delete message.
	 *
	 * @param message
	 *            Message to delete
	 */
	public void delete(Message message) {
		messageService.delete(message);
	}

	/**
	 * Pre condition message basic.
	 *
	 * @param object
	 *            the object
	 * @return true, if successful
	 */
	public boolean preConditionMessageBasic(EObject object) {
		return messageService.preConditionMessageBasic(object);
	}

	/**
	 * Pre condition message basic.
	 *
	 * @param object
	 *            the object
	 * @return true, if successful
	 */
	public boolean preConditionNotMessageBasic(EObject object) {
		if (object instanceof Message) {
			if (((Message) object).getMessageSort() == MessageSort.CREATE_MESSAGE_LITERAL || ((Message) object).getMessageSort() == MessageSort.DELETE_MESSAGE_LITERAL || ((Message) object).getMessageSort() == MessageSort.REPLY_LITERAL) {
				return false;
			}
		}
		return !messageService.preConditionMessageBasic(object);
	}


	// ---------------------- REORDER MANAGEMENT


	/**
	 * Reorder new fragments.
	 *
	 * @param object
	 *            the object
	 * @param startingEndPredecessor
	 *            the starting end predecessor
	 * @param finishingEndPredecessor
	 *            the finishing end predecessor
	 */
	public void reorderNewFragments(EObject object, EObject startingEndPredecessor, EObject finishingEndPredecessor) {
		reorderService.reorderNewFragments(object, startingEndPredecessor, finishingEndPredecessor);


	}

	/**
	 * Reorder new lifeline.
	 *
	 * @param object
	 *            the object
	 * @param newInstance
	 *            the newInstance to create
	 * @param predecessor
	 *            the lifeline predecessor
	 */
	public void reorderNewLifeline(EObject container, EObject newInstance, EObject predecessor) {
		reorderService.reorderNewLifeline(container, newInstance, predecessor);
	}

	/**
	 * Reorder new fragments.
	 *
	 * @param object
	 *            the object
	 * @param startingEndPredecessor
	 *            the starting end predecessor
	 * @param finishingEndPredecessor
	 *            the finishing end predecessor
	 * @param instance
	 *            the instance
	 */
	public void reorderNewFragments(EObject object, EObject startingEndPredecessor, EObject finishingEndPredecessor, EObject instance) {
		reorderService.reorderNewFragments(object, startingEndPredecessor, finishingEndPredecessor, instance);
	}

	/**
	 * Reorder new fragments.
	 *
	 * @param object
	 *            the object
	 * @param startingEndPredecessor
	 *            the starting end predecessor
	 * @param finishingEndPredecessor
	 *            the finishing end predecessor
	 * @param start
	 *            the start
	 * @param finish
	 *            the finish
	 * @param instance
	 *            the instance
	 */
	public void reorderNewFragments(EObject object, EventEnd startingEndPredecessor, EventEnd finishingEndPredecessor, EObject start, EObject finish, EObject instance) {
		reorderService.reorderNewFragments(object, startingEndPredecessor, finishingEndPredecessor, start, finish, instance);
	}


	/**
	 * Reorder new combined fragment.
	 *
	 * @param object
	 *            the object
	 * @param startingEndPredecessor
	 *            the starting end predecessor
	 * @param finishingEndPredecessor
	 *            the finishing end predecessor
	 * @param instance
	 *            the instance
	 */
	public void reorderNewCombinedFragment(EObject object, SingleEventEnd startingEndPredecessor, SingleEventEnd finishingEndPredecessor, EObject instance/* , EObject ocStart, EObject oc */) {
		reorderService.reorderNewCombinedFragment(object, startingEndPredecessor, finishingEndPredecessor, instance);
	}

	/**
	 * Choose the context of combined fragment creation.
	 *
	 * @param context
	 *            the context
	 */
	public EObject chooseContext(EObject context) {
		if (context instanceof SequenceDDiagram)
			return ((SequenceDDiagram) context).getTarget();
		return context;
	}

	/**
	 * Reorder lifeline horizontally.
	 *
	 * @param movedLifeline
	 *            moved lifeline
	 * @param predecessorBefore
	 *            lifeline predecessor before
	 * @param predecessorAfter
	 *            lifeline predecessor after
	 */
	public void reorderLifeline(Lifeline movedLifeline, Lifeline predecessorBefore,
			Lifeline predecessorAfter) {
		reorderService.reorderLifeline(movedLifeline, predecessorBefore, predecessorAfter);
	}


	/**
	 * Reorder fragment.
	 *
	 * @param fragment
	 *            Fragment
	 * @param startingEndPredecessorAfter
	 *            Starting end predecessor after reorder
	 * @param finishingEndPredecessorAfter
	 *            Finishing end predecessor after reorder
	 */
	public void reorderFragment(Element fragment, EventEnd startingEndPredecessorAfter,
			EventEnd finishingEndPredecessorAfter) {
		reorderService.reorderFragment(fragment, startingEndPredecessorAfter, finishingEndPredecessorAfter);
	}


	/**
	 * Gets the predecessor.
	 *
	 * @param context
	 *            the context
	 * @return the predecessor
	 */
	public EObject getPredecessor(EObject context) {
		return null;
	}


	/**
	 * Reconnect.
	 *
	 * @param context            the context
	 * @param edgeView            the edge view
	 * @param source the source
	 * @param view            the view
	 */
	public void connectToEvent(EObject context, EObject edgeView, EObject source, EObject view) {
		View gmfView = SiriusGMFHelper.getGmfView((DNode) view);
		reorderService.connectToEvent(context, edgeView, source, view);
	}


	// ----------- COMBINEDFRAGMENT MANAGEMENT

	/**
	 * Gets the enclosing fragment.
	 *
	 * @param fragment the fragment
	 * @return the enclosing fragment
	 */
	public InteractionFragment getEnclosingFragment(EObject fragment) {
		return fragmentsService.getEnclosingFragment(fragment);
	}

	/**
	 * Delete combined fragment.
	 *
	 * @param combinedFragment
	 *            the combined fragment
	 */
	public void deleteCombinedFragment(CombinedFragment combinedFragment) {
		combinedFragmentOperandService.deleteCombinedFragment(combinedFragment);
	}


	/**
	 * Creates the E annotations.
	 *
	 * @param context
	 *            the context
	 */
	public void createEAnnotations(EObject context) {
		combinedFragmentOperandService.createEAnnotations(context);
	}

	/**
	 * Manage operand fragment.
	 *
	 * @param context
	 *            the context
	 * @param startingEndPredecessor
	 *            the starting end predecessor
	 * @param finishingEndPredecessor
	 *            the finishing end predecessor
	 */
	public void manageOperandFragment(EObject context, EventEnd startingEndPredecessor, EventEnd finishingEndPredecessor) {
		combinedFragmentOperandService.manageOperandFragment(context, startingEndPredecessor, finishingEndPredecessor);
	}


	// ---------- EXECUTION MANAGEMENT


	/**
	 * Gets the execution specifications.
	 *
	 * @param object
	 *            the object
	 * @return the execution specifications
	 */
	public List<ExecutionSpecification> getExecutionSpecifications(EObject object) {
		return executionService.getExecutionSpecifications(object);
	}




	/**
	 * Delete execution.
	 *
	 * @param execution
	 *            Execution to delete
	 */
	public void deleteExecution(ExecutionSpecification execution) {
		executionService.deleteExecution(execution);
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
		executionService.applyExecution(context, exe, start, finish);
	}



	/**
	 * Target finder expression.
	 *
	 * @param object
	 *            the object
	 * @return the list
	 */
	// -------------CONSTRAINT AND COMMENT SERVICE
	public List<?> targetFinderExpression(EObject object) {
		if (object instanceof Comment) {
			return commentService.targeFinderExpressionForComment((Comment) object);
		}
		if (object instanceof Constraint) {
			return constraintService.targeFinderExpressionForConstraint((Constraint) object);
		}
		return null;

	}


	/**
	 * Gets the constraint label.
	 *
	 * @param object
	 *            the object
	 * @return the constraint label
	 */
	public String getConstraintLabel(EObject object) {
		return constraintService.getConstraintLabel(object);
	}


	/**
	 * Log.
	 *
	 * @param container
	 *            the container
	 * @return true, if successful
	 */
	public boolean log(EObject container) {
		return true;
	}

}
