/******************************************************************************
 * Copyright (c) 2021, 2022 CEA LIST, Artal Technologies and Obeo
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
 *    Jessy Mallet (Obeo) - jessy.mallet@obeo.fr - Modify Lost/Found Message creation
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.sequence.diagram.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.LabelServices;
import org.eclipse.papyrus.uml.sirius.common.diagram.core.services.OperationServices;
import org.eclipse.sirius.diagram.model.business.internal.spec.DNodeSpec;
import org.eclipse.sirius.diagram.sequence.ordering.EventEnd;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.BehaviorExecutionSpecification;
import org.eclipse.uml2.uml.DestructionOccurrenceSpecification;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ExecutionOccurrenceSpecification;
import org.eclipse.uml2.uml.ExecutionSpecification;
import org.eclipse.uml2.uml.Gate;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.InteractionOperand;
import org.eclipse.uml2.uml.InteractionUse;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageEnd;
import org.eclipse.uml2.uml.MessageOccurrenceSpecification;
import org.eclipse.uml2.uml.MessageSort;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OccurrenceSpecification;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.StateInvariant;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * The Class MessageService.
 */
@SuppressWarnings("restriction")
public class MessageService {


	/**
	 * Signal name suffix.
	 */
	public static final String SIGNAL_SUFFIX = "_signal"; //$NON-NLS-1$

	/**
	 * Sender message name suffix.
	 */
	public static final String SENDER_MESSAGE_SUFFIX = "_sender"; //$NON-NLS-1$

	/**
	 * Receiver message name suffix.
	 */
	public static final String RECEIVER_MESSAGE_SUFFIX = "_receiver"; //$NON-NLS-1$


	/** The service. */
	static private MessageService service = null;


	/**
	 * Instantiates a new fragments service.
	 */
	private MessageService() {

	}

	/**
	 * Gets the single instance of FragmentsService.
	 *
	 * @return single instance of FragmentsService
	 */
	static public MessageService getInstance() {
		if (service == null) {
			service = new MessageService();
		}
		return service;
	}
	
	/**
	 * Complete Found {@link Message} creation with its {@link MessageOccurrenceSpecification} receive event.
	 *
	 * @param context
	 *            the context which hold the created Found {@link Message}
	 * @param foundMessage
	 *            the created Found {@link Message}.
	 */
	public void completeFoundMessageWithMsgOccSpec(EObject context, Message foundMessage) {
		Lifeline lifeline= null;
		if (context instanceof Lifeline) {
			lifeline = (Lifeline) context;
		} else if (context instanceof ExecutionOccurrenceSpecification) {
			lifeline = ((ExecutionOccurrenceSpecification) context).getCovered();
		}			
		Interaction interaction = lifeline.getInteraction();
		
		foundMessage.setName(computeDefaultName(foundMessage));

		MessageOccurrenceSpecification occurenceMessageReceive = UMLFactory.eINSTANCE.createMessageOccurrenceSpecification();
		occurenceMessageReceive.setMessage(foundMessage);
		
		if (context instanceof ExecutionOccurrenceSpecification) {
			ReorderService.getInstance().replaceExecByMessage(occurenceMessageReceive, interaction.getFragments(), (ExecutionOccurrenceSpecification) context);
		} else {
			interaction.getFragments().add(occurenceMessageReceive);
		}
		
		lifeline.getCoveredBys().add(occurenceMessageReceive);
		occurenceMessageReceive.setName(computeDefaultName(occurenceMessageReceive) + "ReceiveEvent"); //$NON-NLS-1$
		EAnnotation annotation = EcoreFactory.eINSTANCE.createEAnnotation();
		annotation.setSource(occurenceMessageReceive.getName() + "FOUNDMESSAGE"); //$NON-NLS-1$
		occurenceMessageReceive.getEAnnotations().add(annotation);

		foundMessage.setMessageSort(MessageSort.SYNCH_CALL_LITERAL);
		foundMessage.setReceiveEvent(occurenceMessageReceive);
	}



	/**
	 * Checks if is lost found target valid.
	 *
	 * @param context
	 *            the context
	 * @return true, if is lost found target valid
	 */
	public boolean isLostFoundTargetValid(EObject context) {

		if (context instanceof Lifeline) {
			return true;
		}
		if (context instanceof ExecutionOccurrenceSpecification) {
			return true;
		}
		return false;


	}
	
	/**
	 * Complete Lost {@link Message} creation with its {@link MessageOccurrenceSpecification} Send Event.
	 *
	 * @param context
	 *            the context which hold the created Lost {@link Message}
	 * @param lostMessage
	 *            the created Lost {@link Message}.
	 */
	public void completeLostMessageWithMsgOccSpec(EObject context, Message lostMessage) {
		Lifeline lifeline = null;
		if (context instanceof Lifeline) {
			lifeline = (Lifeline) context;
		} else if (context instanceof ExecutionOccurrenceSpecification) {
			lifeline = ((ExecutionOccurrenceSpecification) context).getCovered();			
		}
		Interaction interaction = lifeline.getInteraction();

		lostMessage.setName(computeDefaultName(lostMessage));

		MessageOccurrenceSpecification occurenceMessageSend = UMLFactory.eINSTANCE.createMessageOccurrenceSpecification();
		occurenceMessageSend.setMessage(lostMessage);

		if (context instanceof ExecutionOccurrenceSpecification) {
			ReorderService.getInstance().replaceExecByMessage(occurenceMessageSend, interaction.getFragments(), (ExecutionOccurrenceSpecification) context);

		} else {
			interaction.getFragments().add(occurenceMessageSend);
		}
		lifeline.getCoveredBys().add(occurenceMessageSend);
		occurenceMessageSend.setName(computeDefaultName(occurenceMessageSend) + "SendEvent"); //$NON-NLS-1$
		EAnnotation annotation = EcoreFactory.eINSTANCE.createEAnnotation();
		annotation.setSource(occurenceMessageSend.getName() + "LOSTMESSAGE"); //$NON-NLS-1$
		occurenceMessageSend.getEAnnotations().add(annotation);

		lostMessage.setMessageSort(MessageSort.SYNCH_CALL_LITERAL);
		lostMessage.setSendEvent(occurenceMessageSend);
	}


	/**
	 * Gets the lost receive annotation.
	 *
	 * @param context
	 *            the context
	 * @return the lost receive annotation
	 */
	public EObject getLostReceiveAnnotation(EObject context) {
		if (context instanceof Message) {
			MessageEnd sendEvent = ((Message) context).getSendEvent();
			EList<EAnnotation> eAnnotations = sendEvent.getEAnnotations();
			for (EAnnotation eAnnotation : eAnnotations) {
				if (eAnnotation.getSource().equals(sendEvent.getName() + "LOSTMESSAGE")) { //$NON-NLS-1$
					return eAnnotation;
				}
			}
			return sendEvent;
		}

		return null;
	}
	
	/**
	 * Gets the found send annotation.
	 *
	 * @param context
	 *            the context
	 * @return the found send annotation
	 */
	public EObject getFoundSendAnnotation(EObject context) {
		if (context instanceof Message) {
			MessageEnd receiveEvent = ((Message) context).getReceiveEvent();
			EList<EAnnotation> eAnnotations = receiveEvent.getEAnnotations();
			for (EAnnotation eAnnotation : eAnnotations) {
				if (eAnnotation.getSource().equals(receiveEvent.getName() + "FOUNDMESSAGE")) { //$NON-NLS-1$
					return eAnnotation;
				}
			}
			return receiveEvent;
		}

		return null;
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
		EObject startingEndPredecessorSemanticEnd = null;
		EObject finishingEndPredecessorSemanticEnd = null;
		InteractionFragment interaction = null;
		if (targetFragment instanceof Lifeline) {
			interaction = ((Lifeline) targetFragment).getInteraction();
		} else {
			interaction = FragmentsService.getInstance().getEnclosingFragment(targetFragment);
		}
		if (startingEndPredecessor != null) {
			startingEndPredecessorSemanticEnd = startingEndPredecessor.getSemanticEnd();
		}
		if (finishingEndPredecessor != null) {
			finishingEndPredecessorSemanticEnd = finishingEndPredecessor.getSemanticEnd();
		}
		Message replyMessage = createReplyMessage(interaction, targetFragment, sourceFragment,
				startingEndPredecessorSemanticEnd, finishingEndPredecessorSemanticEnd, null);
		getMessages(interaction).add(replyMessage);

		final MessageOccurrenceSpecification senderEventMessage = (MessageOccurrenceSpecification) replyMessage
				.getSendEvent();
		final MessageOccurrenceSpecification receiverEventMessage = (MessageOccurrenceSpecification) replyMessage
				.getReceiveEvent();

		final List<EObject> fragments = FragmentsService.getInstance().getFragmentsAndAnnotation(interaction);
		final BehaviorExecutionSpecification predecessorExecution = ExecutionService.getInstance().getExecution(
				startingEndPredecessorSemanticEnd);
		// If predecessor is the beginning of an execution add message after the
		// execution
		if (startingEndPredecessorSemanticEnd != null && startingEndPredecessorSemanticEnd instanceof OccurrenceSpecification
				&& predecessorExecution != null
				&& startingEndPredecessorSemanticEnd.equals(predecessorExecution.getStart())) {
			fragments.add(fragments.indexOf(predecessorExecution) + 1, senderEventMessage);
		}
		// Else set it directly after the predecessor
		else {
			fragments.add(fragments.indexOf(startingEndPredecessorSemanticEnd) + 1, senderEventMessage);
		}

		fragments.add(fragments.indexOf(senderEventMessage) + 1, receiverEventMessage);

		FragmentsService.getInstance().updateFragmentList(startingEndPredecessorSemanticEnd != null ? startingEndPredecessorSemanticEnd : interaction, fragments);
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
		EObject startingEndPredecessorSemanticEnd = null;
		if (startingEndPredecessor != null) {
			startingEndPredecessorSemanticEnd = startingEndPredecessor.getSemanticEnd();
		}

		if (context instanceof DNodeSpec) {
			EObject target = ((DNodeSpec) context).getTarget();
			Interaction interaction = null;
			if (target instanceof InteractionFragment) {
				interaction = FragmentsService.getInstance().getParentInteraction((InteractionFragment) target);
			}
			if (target instanceof Lifeline) {
				interaction = ((Lifeline) target).getInteraction();
			}
			Message deleteMessage = UMLFactory.eINSTANCE.createMessage();
			interaction.getMessages().add(deleteMessage);
			deleteMessage.setName(computeDefaultName(deleteMessage));

			MessageOccurrenceSpecification occurenceMessageSend = UMLFactory.eINSTANCE.createMessageOccurrenceSpecification();
			interaction.getFragments().add(occurenceMessageSend);
			LifelineService.getInstance().getLifeline(sourceVariable).getCoveredBys().add(occurenceMessageSend);
			occurenceMessageSend.setName(computeDefaultName(occurenceMessageSend) + "SendEvent");

			DestructionOccurrenceSpecification occurenceMessageReceive = UMLFactory.eINSTANCE.createDestructionOccurrenceSpecification();
			interaction.getFragments().add(occurenceMessageReceive);
			occurenceMessageReceive.setName(computeDefaultName(occurenceMessageReceive) + "ReceiveEvent");
			targetVariable.getCoveredBys().add(occurenceMessageReceive);

			deleteMessage.setMessageSort(MessageSort.DELETE_MESSAGE_LITERAL);
			deleteMessage.setSendEvent(occurenceMessageSend);
			deleteMessage.setReceiveEvent(occurenceMessageReceive);


			List<EObject> fragments = FragmentsService.getInstance().getEnclosingFragments(interaction);
			if (sourceVariable instanceof ExecutionOccurrenceSpecification) {
				MessageOccurrenceSpecification sendEvent = (MessageOccurrenceSpecification) deleteMessage.getSendEvent();

				ExecutionSpecification sourceExecution = ((ExecutionOccurrenceSpecification) sourceVariable).getExecution();

				fragments.remove(sendEvent);
				replaceByMessageOccurrence((ExecutionOccurrenceSpecification) sourceVariable, fragments, sendEvent, sourceExecution);
				if (startingEndPredecessorSemanticEnd != null && startingEndPredecessorSemanticEnd.equals(sourceVariable)) {
					startingEndPredecessorSemanticEnd = sendEvent;
				}
			}

			boolean startOfExecution = startingEndPredecessorSemanticEnd == null ? true : ReorderService.getInstance().isStartOfExecution(startingEndPredecessorSemanticEnd, fragments);
			int index = -1;
			if (startingEndPredecessorSemanticEnd == null) {
				index = 0;
			} else {
				index = startingEndPredecessorSemanticEnd.equals(deleteMessage.getSendEvent()) ? fragments.indexOf(startingEndPredecessorSemanticEnd) : fragments.indexOf(startingEndPredecessorSemanticEnd) + (startOfExecution ? 1 : 2);
			}

			/////////////////////////

			fragments.remove(
					deleteMessage.getSendEvent());
			fragments.add(index,
					deleteMessage.getSendEvent());

			fragments.remove(
					deleteMessage.getReceiveEvent());
			fragments.add(fragments.indexOf(deleteMessage.getSendEvent()) + 1,
					deleteMessage.getReceiveEvent());


			FragmentsService.getInstance().updateFragmentList(interaction, fragments);


			///////////////////////



			return deleteMessage;
		}
		return null;
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

		Element startingEndPredecessorSemanticEnd = null;
		if (startingEndPredecessor != null) {
			startingEndPredecessorSemanticEnd = (Element) startingEndPredecessor.getSemanticEnd();
		}

		if (context instanceof DNodeSpec) {
			EObject target = ((DNodeSpec) context).getTarget();
			Interaction interaction = null;
			if (target instanceof InteractionFragment) {
				interaction = FragmentsService.getInstance().getParentInteraction((InteractionFragment) target);
			} else if (target instanceof Lifeline) {
				interaction = ((Lifeline) target).getInteraction();
			}

			Message createMessage = createMessage(interaction, sourceV, targetV);
			List<EObject> fragments = FragmentsService.getInstance().getEnclosingFragments(interaction);
			if (sourceV instanceof ExecutionOccurrenceSpecification) {
				MessageOccurrenceSpecification sendEvent = (MessageOccurrenceSpecification) createMessage.getSendEvent();

				ExecutionSpecification sourceExecution = ((ExecutionOccurrenceSpecification) sourceV).getExecution();

				fragments.remove(sendEvent);
				replaceByMessageOccurrence((ExecutionOccurrenceSpecification) sourceV, fragments, sendEvent, sourceExecution);
				if (startingEndPredecessorSemanticEnd != null && startingEndPredecessorSemanticEnd.equals(sourceV)) {
					startingEndPredecessorSemanticEnd = sendEvent;
				}
			}

			/////////////////////////

			boolean startOfExecution = startingEndPredecessorSemanticEnd == null ? true : ReorderService.getInstance().isStartOfExecution(startingEndPredecessorSemanticEnd, fragments);
			int index = -1;
			if (startingEndPredecessorSemanticEnd == null) {
				index = 0;
			} else {
				index = startingEndPredecessorSemanticEnd.equals(createMessage.getSendEvent()) ? fragments.indexOf(startingEndPredecessorSemanticEnd) : fragments.indexOf(startingEndPredecessorSemanticEnd) + (startOfExecution ? 1 : 2);
			}

			fragments.remove(
					createMessage.getSendEvent());
			fragments.add(index,
					createMessage.getSendEvent());

			fragments.remove(
					createMessage.getReceiveEvent());
			fragments.add(fragments.indexOf(createMessage.getSendEvent()) + 1,
					createMessage.getReceiveEvent());





			///////////////////////



			FragmentsService.getInstance().updateFragmentList(interaction, fragments);

			return createMessage;
		}
		return null;
	}



	/**
	 * Creates the synchronous message.
	 *
	 * @param source
	 *            the source
	 * @param target
	 *            the target
	 * @return the message
	 */
	public Message createMessage(EObject source, Lifeline target) {

		if (source instanceof ExecutionOccurrenceSpecification) {
			InteractionFragment interaction = FragmentsService.getInstance().getEnclosingFragment(target);
			final Message message = createMessage((Interaction) interaction, source, target);
			List<EObject> enclosingFragments = FragmentsService.getInstance().getEnclosingFragments(target);

			MessageOccurrenceSpecification sendEvent = (MessageOccurrenceSpecification) message.getSendEvent();

			ExecutionSpecification sourceExecution = ((ExecutionOccurrenceSpecification) source).getExecution();

			replaceByMessageOccurrence((ExecutionOccurrenceSpecification) source, enclosingFragments, sendEvent, sourceExecution);

			FragmentsService.getInstance().updateFragmentList(interaction, enclosingFragments);
			return message;
		}
		return null;
	}

	/**
	 * Creates the message.
	 *
	 * @param interaction
	 *            the interaction
	 * @param sourceV
	 *            the source V
	 * @param targetV
	 *            the target V
	 * @return the message
	 */
	private Message createMessage(Interaction interaction, EObject sourceV, Lifeline targetV) {
		Message createMessage = UMLFactory.eINSTANCE.createMessage();
		interaction.getMessages().add(createMessage);
		createMessage.setName(computeDefaultName(createMessage));
		MessageOccurrenceSpecification occurenceMessageSend = UMLFactory.eINSTANCE.createMessageOccurrenceSpecification();
		interaction.getFragments().add(occurenceMessageSend);
		LifelineService.getInstance().getLifeline(sourceV).getCoveredBys().add(occurenceMessageSend);
		occurenceMessageSend.setName(computeDefaultName(occurenceMessageSend) + "SendEvent");
		MessageOccurrenceSpecification occurenceMessageReceive = UMLFactory.eINSTANCE.createMessageOccurrenceSpecification();
		interaction.getFragments().add(occurenceMessageReceive);
		occurenceMessageReceive.setName(computeDefaultName(occurenceMessageReceive) + "ReceiveEvent");
		targetV.getCoveredBys().add(occurenceMessageReceive);

		createMessage.setMessageSort(MessageSort.CREATE_MESSAGE_LITERAL);
		createMessage.setSendEvent(occurenceMessageSend);
		createMessage.setReceiveEvent(occurenceMessageReceive);
		return createMessage;
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

		InteractionFragment interaction = FragmentsService.getInstance().getEnclosingFragment(target);

		final Message message = createSynchronousMessage(interaction, source, target,
				null, null, null);

		getMessages(interaction).add(message);




		manageMessageEnds(target, source, message);


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
		EObject startingEndPredecessorSemanticEnd = null;
		if (startingEndPredecessor != null) {
			startingEndPredecessorSemanticEnd = startingEndPredecessor.getSemanticEnd();
		}
		EObject finishingEndPredecessorSemanticEnd = null;
		if (finishingEndPredecessor != null) {
			finishingEndPredecessorSemanticEnd = finishingEndPredecessor.getSemanticEnd();
		}
		// Get associated class and interaction
		org.eclipse.uml2.uml.Type type;
		InteractionFragment interaction;
		if (target instanceof Lifeline) {
			type = LifelineService.getInstance().getType((Lifeline) target);
			interaction = ((Lifeline) target).getInteraction();
		} else  if (target instanceof ExecutionOccurrenceSpecification) {
			ExecutionOccurrenceSpecification execOccSpec = (ExecutionOccurrenceSpecification) target;
			Lifeline lifeline = execOccSpec.getCovered();
			type = LifelineService.getInstance().getType(lifeline);
			interaction = lifeline.getInteraction();
		} else {
			type = LifelineService.getInstance().getType(((ExecutionSpecification) target).getCovereds().get(0));
			interaction = FragmentsService.getInstance().getEnclosingFragment(target);
		}
		final Operation operation = OperationServices.INSTANCE.createOperation(type);
		// Create message

		createAsynchronousMessage(interaction, source, target, false,
				startingEndPredecessorSemanticEnd, finishingEndPredecessorSemanticEnd, operation);
	}


	/**
	 * Gets the behaviors.
	 *
	 * @param interaction
	 *            the interaction
	 * @return the behaviors
	 */
	public List<Behavior> getBehaviors(InteractionFragment interaction) {
		if (interaction instanceof InteractionOperand) {
			return FragmentsService.getInstance().getParentInteraction(interaction).getOwnedBehaviors();
		}
		if (interaction instanceof Interaction) {
			return ((Interaction) interaction).getOwnedBehaviors();
		}
		return null;
	}

	/**
	 * Gets the messages.
	 *
	 * @param interaction
	 *            the interaction
	 * @return the messages
	 */
	public List<Message> getMessages(InteractionFragment interaction) {
		if (interaction instanceof InteractionOperand) {
			return FragmentsService.getInstance().getParentInteraction(interaction).getMessages();
		}
		if (interaction instanceof Interaction) {
			return ((Interaction) interaction).getMessages();
		}
		return null;
	}



	/**
	 * Create asynchronous message.
	 *
	 * @param interaction
	 *            Interaction
	 * @param createOperation
	 *            Operation must be created true otherwise false
	 * @param sourceFragment
	 *            Source
	 * @param targetFragment
	 *            Target
	 * @param operation
	 *            Operation
	 * @return Message
	 */
	public Message createAsynchronousMessage(InteractionFragment interaction, boolean createOperation,
			NamedElement sourceFragment, NamedElement targetFragment, Operation operation) {
		final UMLFactory factory = UMLFactory.eINSTANCE;

		boolean createSourceMessOcc = !(sourceFragment instanceof Gate);
		boolean createTargetMessOcc = !(targetFragment instanceof Gate);

		final Message message = factory.createMessage();

		final Lifeline source = LifelineService.getInstance().getLifeline(sourceFragment);
		final Lifeline target = LifelineService.getInstance().getLifeline(targetFragment);

		String messageName = ""; //$NON-NLS-1$
		if (operation == null) {
			messageName = "Message_" + Integer.toString(getMessages(interaction).size()); //$NON-NLS-1$
		} else {
			messageName = operation.getName();
		}

		message.setName(messageName);
		message.setMessageSort(MessageSort.ASYNCH_CALL_LITERAL);

		// Create message send event
		if (createSourceMessOcc) {
		final MessageOccurrenceSpecification senderEventMessage = factory
				.createMessageOccurrenceSpecification();
		senderEventMessage.setName(message.getName() + SENDER_MESSAGE_SUFFIX);
		senderEventMessage.getCovereds().add(source);
		senderEventMessage.setMessage(message);
		message.setSendEvent(senderEventMessage);
	} else {
		message.setSendEvent((Gate) sourceFragment);
	}

	if (createTargetMessOcc) {
		// Create message receive event
		final MessageOccurrenceSpecification receiverEventMessage = factory
				.createMessageOccurrenceSpecification();
		receiverEventMessage.setName(message.getName() + RECEIVER_MESSAGE_SUFFIX);
		receiverEventMessage.getCovereds().add(target);
		receiverEventMessage.setMessage(message);


		message.setReceiveEvent(receiverEventMessage);
	} else {
		message.setReceiveEvent((Gate) targetFragment);
	}

		return message;
	}

	/**
	 * Creates the synchronous message.
	 *
	 * @param target
	 *            the target
	 * @param source
	 *            the source
	 */
	public void createASynchronousMessage(NamedElement target, NamedElement source) {
		InteractionFragment interaction = FragmentsService.getInstance().getEnclosingFragment(target);
		final Message message = createAsynchronousMessage(interaction, false, source, target, null);
		getMessages(interaction).add(message);
		manageMessageEnds(target, source, message);
	}

	/**
	 * Manage message ends.
	 *
	 * @param target the target
	 * @param source the source
	 * @param message the message
	 */
	private void manageMessageEnds(NamedElement target, NamedElement source, final Message message) {
		if (source instanceof ExecutionOccurrenceSpecification) {
			List<EObject> enclosingFragments = FragmentsService.getInstance().getEnclosingFragments(source);
			MessageOccurrenceSpecification sendEvent = (MessageOccurrenceSpecification) message.getSendEvent();
			ExecutionSpecification sourceExecution = ((ExecutionOccurrenceSpecification) source).getExecution();
			replaceByMessageOccurrence((ExecutionOccurrenceSpecification) source, enclosingFragments, sendEvent, sourceExecution);
			FragmentsService.getInstance().updateFragmentList(FragmentsService.getInstance().getEnclosingFragment(source), enclosingFragments);
		}
		if (source instanceof Gate) {

		}
		if (target instanceof ExecutionOccurrenceSpecification) {
			List<EObject> enclosingFragments = FragmentsService.getInstance().getEnclosingFragments(target);
			MessageOccurrenceSpecification receiveEvent = (MessageOccurrenceSpecification) message.getReceiveEvent();
			ExecutionSpecification targetExecution = ((ExecutionOccurrenceSpecification) target).getExecution();
			replaceByMessageOccurrence((ExecutionOccurrenceSpecification) target, enclosingFragments, receiveEvent, targetExecution);
			FragmentsService.getInstance().updateFragmentList(FragmentsService.getInstance().getEnclosingFragment(target), enclosingFragments);
		}
		if (target instanceof Gate) {


		}
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
		EObject startingEndPredecessorSemanticEnd = null;
		if (startingEndPredecessor != null) {
			startingEndPredecessorSemanticEnd = startingEndPredecessor.getSemanticEnd();
		}
		EObject finishingEndPredecessorSemanticEnd = null;
		if (finishingEndPredecessor != null) {
			finishingEndPredecessorSemanticEnd = finishingEndPredecessor.getSemanticEnd();
		}
		// Get associated class and interaction
		org.eclipse.uml2.uml.Type type;
		InteractionFragment interaction;
		boolean createExecution = false;
		if (target instanceof Lifeline) {
			createExecution = true;
			type = LifelineService.getInstance().getType((Lifeline) target);
			interaction = ((Lifeline) target).getInteraction();
		} else {
			type = LifelineService.getInstance().getType(((InteractionFragment) target).getCovereds().get(0));
			interaction = FragmentsService.getInstance().getEnclosingFragment(target);
			;
		}
		final Operation operation = OperationServices.INSTANCE.createOperation(type);
		// Create message
		createSynchronousMessage(interaction, source, target, createExecution, true,
				startingEndPredecessorSemanticEnd, finishingEndPredecessorSemanticEnd, operation);
	}


	/**
	 * Check if element is a valid message end.
	 *
	 * @param element
	 *            Element
	 * @return True if element is a valid message end
	 */
	public boolean isValidMessageEnd(Element element) {
		// [preTarget->filter(uml::Lifeline).represents.type<>null or
		// preTarget->filter(uml::ExecutionSpecification).covered.represents.type<>null
		// or
		// preTarget->filter(uml::Lifeline).clientDependency.supplier.oclAsType(uml::Property).classifier<>null
		// or
		// preTarget->filter(uml::ExecutionSpecification).covered.clientDependency.supplier.classifier<>null/]
		return element instanceof Lifeline /* && ((Lifeline)element).getRepresents() != null */
				|| element instanceof ExecutionSpecification
						&& isCoveredTypeSet((ExecutionSpecification) element)
				|| element instanceof Lifeline /* && ((Lifeline)element).getRepresents() != null */
				|| element instanceof Gate
				|| element instanceof StateInvariant
				|| element instanceof InteractionUse
				|| element instanceof OccurrenceSpecification;
	}

	/**
	 * Check if element is a valid message end.
	 *
	 * @param preTarget
	 *            Element
	 * @return True if element is a valid message end
	 */
	public boolean isValidMessageEnd(EObject preTarget) {
		if (preTarget == null) {
			return false;
		}

		if (preTarget instanceof Lifeline) {
			return isValidMessageEndForLifeline(preTarget);

		} else if (preTarget instanceof ExecutionSpecification) {
			for (final Lifeline lifeline : ((ExecutionSpecification) preTarget).getCovereds()) {
				final boolean result = isValidMessageEndForLifeline(lifeline);
				if (result) {
					return true;
				}
			}
		} else if (preTarget instanceof StateInvariant) {
			return true;

		} else if (preTarget instanceof InteractionUse) {
			return true;
		} else if (preTarget instanceof ExecutionOccurrenceSpecification) {
			return true;
		} else if (preTarget instanceof Gate) {
			return true;
		}

		return false;
	}

	/**
	 * Check if message is not a reply message.
	 *
	 * @param message
	 *            Message
	 * @return True if message is not a reply message
	 */
	public boolean isNotReply(Message message) {
		return !isReply(message);
	}

	/**
	 * Check if message is a reply message.
	 *
	 * @param message
	 *            Message
	 * @return True if message is a reply message
	 */
	public boolean isReply(Message message) {
		if (message == null) {
			return false;
		}
		return MessageSort.REPLY_LITERAL.equals(message.getMessageSort());
	}


	/**
	 * Checks if is receive message event.
	 *
	 * @param event
	 *            the event
	 * @return true, if is receive message event
	 */
	public boolean isReceiveMessageEvent(MessageOccurrenceSpecification event) {
		Message message = event.getMessage();
		MessageEnd receiveEvent = message.getReceiveEvent();
		if (receiveEvent.equals(event)) {
			return true;
		}
		return false;
	}


	/**
	 * Find occurrence specification context for a receive event.
	 *
	 * @param message
	 *            Message
	 * @return Occurrence specification context
	 */
	public NamedElement findOccurrenceSpecificationContextForReceiveEvent(Message message) {


		MessageEnd receiveEvent = message.getReceiveEvent();
		if (receiveEvent == null) {
			return null;
		}
		if (receiveEvent instanceof Gate) {
			return receiveEvent;
		}
		if (receiveEvent instanceof Gate) {
			return message.getReceiveEvent();
		}
		if (message.getSendEvent() instanceof Gate) {
			return message.getReceiveEvent();
		}
		return findOccurrenceSpecificationContext((OccurrenceSpecification) receiveEvent);
	}


	/**
	 * Find occurrence specification context for a send event.
	 *
	 * @param message
	 *            Message
	 * @return Occurrence specification context
	 */
	public NamedElement findOccurrenceSpecificationContextForSendEvent(Message message) {
		if (message.getReceiveEvent() instanceof Gate) {
			return message.getSendEvent();
		}
		if (message.getSendEvent() instanceof Gate) {
			return message.getSendEvent();
		}
		return findOccurrenceSpecificationContext((OccurrenceSpecification) message.getSendEvent());
	}


	/**
	 * Check if message is a synchronous call.
	 *
	 * @param message
	 *            Message
	 * @return True if message is a synchronous call
	 */
	public boolean isSynchCall(Message message) {
		if (message == null) {
			return false;
		}
		return MessageSort.ASYNCH_CALL_LITERAL.equals(message.getMessageSort());
	}

	/**
	 * Gets the invocation message.
	 *
	 * @param object
	 *            the object
	 * @return the invocation message
	 */
	public Message getInvocationMessage(EObject object) {

		if (object instanceof Message) {
			EObject eContainer = object.eContainer();
			String name = ((Message) object).getName();
			if (eContainer instanceof Interaction) {
				// TODO, il faut trouver une autre solution pour trouver le message d'invokation que par le nom. Car celui ci pourrait etre chang� par l'utilisateur apres coup.
				String invokationName = name.replace("_reply", "");
				return ((Interaction) eContainer).getMessage(invokationName);
			}
		}
		return null;

	}

	/**
	 * Gets the message associated elements.
	 *
	 * @param msg
	 *            the msg
	 * @return the message associated elements
	 */
	public Collection<EObject> getMessageAssociatedElements(Message msg) {
		Collection<EObject> messageAssociatedElements = new LinkedHashSet<EObject>();
		messageAssociatedElements.add(msg);
		messageAssociatedElements.add(msg.getSendEvent());
		messageAssociatedElements.add(msg.getReceiveEvent());
		messageAssociatedElements.add(((MessageOccurrenceSpecification) msg.getSendEvent()).getCovered());
		messageAssociatedElements.add(((DestructionOccurrenceSpecification) msg.getReceiveEvent()).getCovered());
		return messageAssociatedElements;
	}

	/**
	 * Delete message.
	 *
	 * @param message
	 *            Message to delete
	 */
	public void delete(Message message) {
		if (message == null) {
			return;
		}

		// Get fragments
		final Interaction interaction = (Interaction) message.eContainer();
		final List<InteractionFragment> fragments = interaction.getFragments();

		// Delete start and finish message and if an execution is associated to
		// the message remove also the
		// execution
		final MessageOccurrenceSpecification receiveMessage = (MessageOccurrenceSpecification) message
				.getReceiveEvent();
		if (receiveMessage != null) {
			// If message is a synchronous message delete also the reply message
//			if (MessageSort.SYNCH_CALL_LITERAL.equals(message.getMessageSort())) {
//				final Message reply = getReplyMessage(message);
//				if (reply != null) {
//					delete(reply);
//				}
//			}

			final BehaviorExecutionSpecification execution = ExecutionService.getInstance().getExecution(receiveMessage);
			if (execution != null) {
				ExecutionService.getInstance().deleteExecution(execution);
			}
			fragments.remove(receiveMessage);
		}
		final MessageOccurrenceSpecification sendMessage = (MessageOccurrenceSpecification) message
				.getSendEvent();
		if (sendMessage != null) {
			final BehaviorExecutionSpecification execution = ExecutionService.getInstance().getExecution(sendMessage);
			if (execution != null) {
				ExecutionService.getInstance().deleteExecution(execution);
			}
			// Delete signal
			final List<PackageableElement> packagedElements = new ArrayList<PackageableElement>();
			packagedElements.addAll(message.getNearestPackage().getPackagedElements());
			for (final PackageableElement packageableElement : packagedElements) {
				if (packageableElement instanceof Signal) {
					final Signal signal = (Signal) packageableElement;
					if (signal.getName().startsWith(message.getName())) {
						signal.destroy();
					}
				}
			}
			fragments.remove(message.getSendEvent());
		}
		// Delete message
		interaction.getMessages().remove(message);
	}


	/**
	 * Get the reply message associated to a message.
	 *
	 * @param message
	 *            Message
	 * @return Reply message if exists otherwise null
	 */
	public Message getReplyMessage(Message message) {
		// To get the reply message associated to a message
		// Get the execution associated to message if exists
		final BehaviorExecutionSpecification execution = ExecutionService.getInstance().getExecution(message);
		if (execution != null) {
			// Get the end execution occurrence
			final OccurrenceSpecification end = execution.getFinish();
			if (end instanceof MessageOccurrenceSpecification) {
				// Get the message
				return ((MessageOccurrenceSpecification) end).getMessage();
			}
		}
		// else in case of message without execution search by name
		for (final Message messageReply : message.getInteraction().getMessages()) {
			if (MessageSort.REPLY_LITERAL.equals(messageReply.getMessageSort())
					&& messageReply.getName().startsWith(message.getName())) {
				return messageReply;
			}
		}
		return null;
	}





	////// private method

	/**
	 * create a reply message for a synchronous message.
	 *
	 * @param interaction
	 *            interaction.
	 * @param sourceFragment
	 *            source
	 * @param targetFragment
	 *            target
	 * @param startingEndPredecessor
	 *            starting end predecessor
	 * @param finishingEndPredecessor
	 *            finishing end predecessor
	 * @param message
	 *            a synchronous message
	 * @return a reply message
	 */
	private Message createReplyMessage(InteractionFragment interaction, NamedElement sourceFragment,
			NamedElement targetFragment, EObject startingEndPredecessor, EObject finishingEndPredecessor,
			Message message) {

		final UMLFactory factory = UMLFactory.eINSTANCE;

		final Lifeline source = LifelineService.getInstance().getLifeline(sourceFragment);
		final Lifeline target = LifelineService.getInstance().getLifeline(targetFragment);

		// Create reply message
		final Message replyMessage = factory.createMessage();
		String replyName = "";
		if (message == null) {
			replyName = "Message_" + getMessages(interaction).size();
		} else {
			replyName = message.getName();
		}
		replyMessage.setName(replyName + "_reply"); //$NON-NLS-1$
		replyMessage.setMessageSort(MessageSort.REPLY_LITERAL);
		getMessages(interaction).add(replyMessage);

		// Create reply message send event
		final MessageOccurrenceSpecification senderEventReplyMessage = factory
				.createMessageOccurrenceSpecification();
		senderEventReplyMessage.setName(replyMessage.getName() + SENDER_MESSAGE_SUFFIX);
		senderEventReplyMessage.getCovereds().add(target);
		senderEventReplyMessage.setMessage(replyMessage);

		// Create reply message receive event
		final MessageOccurrenceSpecification receiverEventReplyMessage = factory
				.createMessageOccurrenceSpecification();
		receiverEventReplyMessage.setName(replyMessage.getName() + RECEIVER_MESSAGE_SUFFIX);
		receiverEventReplyMessage.getCovereds().add(source);
		receiverEventReplyMessage.setMessage(replyMessage);

		replyMessage.setSendEvent(senderEventReplyMessage);
		replyMessage.setReceiveEvent(receiverEventReplyMessage);

		return replyMessage;
	}

	/**
	 * Replace by message occurrence.
	 *
	 * @param execOcc
	 *            the exec occ
	 * @param enclosingFragments
	 *            the enclosing fragments
	 * @param event
	 *            the event
	 * @param execution
	 *            the execution
	 */
	private void replaceByMessageOccurrence(ExecutionOccurrenceSpecification execOcc, List<EObject> enclosingFragments, MessageOccurrenceSpecification event, ExecutionSpecification execution) {
		if (execOcc.getName().endsWith("start")) {
			int indexOfSource = enclosingFragments.indexOf(execOcc);
			enclosingFragments.remove(execOcc);
			enclosingFragments.add(indexOfSource, event);
			execution.setStart(event);
			execOcc.setCovered(null);
		}
		if (execOcc.getName().endsWith("finish")) {
			int indexOfSource = enclosingFragments.indexOf(execOcc);
			enclosingFragments.remove(execOcc);
			enclosingFragments.add(indexOfSource, event);
			execution.setFinish(event);
			execOcc.setCovered(null);
		}
	}

	/**
	 * Create asynchronous typed message.
	 *
	 * @param interaction
	 *            Interaction
	 * @param sourceFragment
	 *            Source
	 * @param targetFragment
	 *            Target
	 * @param createExecution
	 *            Set to true to create an execution
	 * @param startingEndPredecessor
	 *            Starting end predecessor
	 * @param finishingEndPredecessor
	 *            Finishing end predecessor
	 * @param operation
	 *            Operation associated to message
	 * @return the message
	 */
	private Message createAsynchronousMessage(InteractionFragment interaction, NamedElement sourceFragment,
			NamedElement targetFragment, boolean createExecution, EObject startingEndPredecessor,
			EObject finishingEndPredecessor, Operation operation) {
		final Lifeline target = LifelineService.getInstance().getLifeline(targetFragment);

		final BehaviorExecutionSpecification predecessorExecution = ExecutionService.getInstance().getExecution(
				startingEndPredecessor);



		final UMLFactory factory = UMLFactory.eINSTANCE;
		List<EObject> fragments = FragmentsService.getInstance().computeFragments(sourceFragment, startingEndPredecessor, interaction);

		// Create message
		final Message message = createAsynchronousMessage(interaction, createExecution, sourceFragment,
				targetFragment, operation);

		if (null != operation) {
			message.setSignature(operation);
		}

		getMessages(interaction).add(message);

		// Create execution or signal
		BehaviorExecutionSpecification execution = null;
		if (createExecution) {
			execution = ExecutionService.getInstance().createExecution(/* operation, */target, message);
			getBehaviors(interaction).add(execution.getBehavior());
		} else {
			final Signal signal = factory.createSignal();
			signal.setName(message.getName() + SIGNAL_SUFFIX);
			message.getNearestPackage().getPackagedElements().add(signal);
		}



		final MessageOccurrenceSpecification senderEventMessage = (MessageOccurrenceSpecification) message
				.getSendEvent();
		final MessageOccurrenceSpecification receiverEventMessage = (MessageOccurrenceSpecification) message
				.getReceiveEvent();

		ExecutionSpecification sourceExecution = null;
		ExecutionOccurrenceSpecification endExec = null;
		if (execution != null) {
			execution.setStart(receiverEventMessage);
			endExec = factory.createExecutionOccurrenceSpecification();
			endExec.setName(execution.getName() + ExecutionService.EXECUTION_END_SUFFIX);
			endExec.getCovereds().add(target);
			endExec.setExecution(execution);
			execution.setFinish(endExec);
		} else if (targetFragment instanceof ExecutionOccurrenceSpecification) {
			ExecutionOccurrenceSpecification execOccSpec = (ExecutionOccurrenceSpecification) targetFragment;
			sourceExecution = execOccSpec.getExecution();
		}

		// If predecessor is the beginning of an execution add message after the
		// execution
		if (sourceFragment instanceof OccurrenceSpecification) {
			// List<EObject> enclosingFragments = FragmentsService.getInstance().getEnclosingFragments(sourceFragment);
			MessageOccurrenceSpecification sendEvent = (MessageOccurrenceSpecification) message.getSendEvent();
			sourceExecution = ((ExecutionOccurrenceSpecification) sourceFragment).getExecution();
			replaceByMessageOccurrence((ExecutionOccurrenceSpecification) sourceFragment, fragments, sendEvent, sourceExecution);
		} else {
			if (startingEndPredecessor != null && startingEndPredecessor instanceof OccurrenceSpecification
					&& predecessorExecution != null
					&& startingEndPredecessor.equals(predecessorExecution.getStart())) {
				fragments.add(fragments.indexOf(predecessorExecution) + 1, senderEventMessage);
			}
			// Else set it directly after the predecessor
			else {
				fragments.add(fragments.indexOf(startingEndPredecessor) + 1, senderEventMessage);
			}
		}

		// fragments.add(receiverEventMessage);
		if (targetFragment instanceof OccurrenceSpecification && sourceExecution == null) {
			// List<EObject> enclosingFragments = FragmentsService.getInstance().getEnclosingFragments(sourceFragment);
			MessageOccurrenceSpecification receiveEvent = (MessageOccurrenceSpecification) message.getReceiveEvent();
			sourceExecution = ((ExecutionOccurrenceSpecification) sourceFragment).getExecution();
			replaceByMessageOccurrence((ExecutionOccurrenceSpecification) targetFragment, fragments, receiveEvent, sourceExecution);
		} if (targetFragment instanceof OccurrenceSpecification && sourceExecution != null) {
			ExecutionOccurrenceSpecification execOccSpec = (ExecutionOccurrenceSpecification) targetFragment;
			sourceExecution = execOccSpec.getExecution();
			replaceByMessageOccurrence((ExecutionOccurrenceSpecification) execOccSpec, fragments, receiverEventMessage, sourceExecution);
			fragments.remove(senderEventMessage);
			fragments.add(fragments.indexOf(receiverEventMessage), senderEventMessage);
		} else {
			fragments.add(fragments.indexOf(senderEventMessage) + 1, receiverEventMessage);
		}
		if (execution != null) {
			// fragments.add(execution);
			// fragments.add(endExec);
			fragments.add(fragments.indexOf(receiverEventMessage) + 1, execution);
			fragments.add(fragments.indexOf(execution) + 1, endExec);
		}
		FragmentsService.getInstance().updateFragmentList(startingEndPredecessor != null ? startingEndPredecessor : interaction, fragments);

		return message;
	}

	/**
	 * Create synchronous typed message.
	 *
	 * @param interaction
	 *            Interaction
	 * @param sourceFragment
	 *            Source
	 * @param targetFragment
	 *            Target
	 * @param createReply
	 *            the create reply
	 * @param createExecution
	 *            set to true to create an execution
	 * @param startingEndPredecessor
	 *            Starting end predecessor
	 * @param finishingEndPredecessor
	 *            Finishing end predecessor
	 * @param operation
	 *            Operation associated with the message
	 */
	private void createSynchronousMessage(InteractionFragment interaction, NamedElement sourceFragment,
			NamedElement targetFragment, boolean createReply, boolean createExecution, EObject startingEndPredecessor,
			EObject finishingEndPredecessor, Operation operation) {
		final Lifeline target = LifelineService.getInstance().getLifeline(targetFragment);
		final BehaviorExecutionSpecification predecessorExecution = ExecutionService.getInstance().getExecution(
				startingEndPredecessor);

		final UMLFactory factory = UMLFactory.eINSTANCE;
		List<EObject> fragments = FragmentsService.getInstance().computeFragments(sourceFragment, startingEndPredecessor, interaction);
		final Message message = createSynchronousMessage(interaction, sourceFragment, targetFragment,
				startingEndPredecessor, finishingEndPredecessor, operation);

		getMessages(interaction).add(message);

		if (null != operation) {
			message.setSignature(operation);
		}


		ExecutionSpecification execution = targetFragment instanceof ExecutionSpecification ? (ExecutionSpecification) targetFragment : null;
		Message replyMessage = null;
		if (createReply) {


			// Create behavior
			// ExecutionSpecification execution = null;
			if (createExecution) {
				execution = ExecutionService.getInstance().createExecution(/* operation, */target, message);
				getBehaviors(interaction).add(((BehaviorExecutionSpecification) execution).getBehavior());
			} else {
				// Create signal
				final Signal signal = factory.createSignal();
				signal.setName(message.getName() + SIGNAL_SUFFIX);
				message.getNearestPackage().getPackagedElements().add(signal);
			}
			if (execution != null) {
				execution.setStart((MessageOccurrenceSpecification) message.getReceiveEvent());
			}

			replyMessage = createReplyMessage(interaction, sourceFragment, targetFragment,
					startingEndPredecessor, finishingEndPredecessor, message);
			getMessages(interaction).add(replyMessage);

			if (execution != null) {
				execution.setFinish((MessageOccurrenceSpecification) replyMessage.getSendEvent());
			}
		}
		// Add and order fragments under the interaction
		// If predecessor is the beginning of an execution add message after the
		// execution
		if (sourceFragment instanceof OccurrenceSpecification) {
			MessageOccurrenceSpecification sendEvent = (MessageOccurrenceSpecification) message.getSendEvent();
			ExecutionSpecification sourceExecution = ((ExecutionOccurrenceSpecification) sourceFragment).getExecution();
			replaceByMessageOccurrence((ExecutionOccurrenceSpecification) sourceFragment, fragments, sendEvent, sourceExecution);
		} else {
			if (startingEndPredecessor != null && startingEndPredecessor instanceof OccurrenceSpecification
					&& predecessorExecution != null
					&& startingEndPredecessor.equals(predecessorExecution.getStart())) {
				fragments.add(fragments.indexOf(predecessorExecution) + 1,
						message.getSendEvent());
				// Else set it directly after the predecessor
			} else {
				fragments.add(fragments.indexOf(startingEndPredecessor) + 1,
						message.getSendEvent());
			}
		}
		if (targetFragment instanceof OccurrenceSpecification) {
			MessageOccurrenceSpecification receiveEvent = (MessageOccurrenceSpecification) message.getReceiveEvent();
			ExecutionSpecification sourceExecution = ((ExecutionOccurrenceSpecification) sourceFragment).getExecution();
			replaceByMessageOccurrence((ExecutionOccurrenceSpecification) targetFragment, fragments, receiveEvent, sourceExecution);
		} else {
			fragments.add(fragments.indexOf(message.getSendEvent()) + 1,
					message.getReceiveEvent());
		}
		if (replyMessage != null) {
			fragments.add(replyMessage.getSendEvent());
		}
		if (execution != null) {
			if (fragments.contains(execution)) {
				fragments.remove(execution);
				fragments.add(fragments.indexOf(message.getReceiveEvent()) + 1, execution);
			} else {
				fragments.add(fragments.indexOf(message.getReceiveEvent()) + 1, execution);
			}
			if (replyMessage != null) {
				fragments.remove(replyMessage.getSendEvent());
				fragments.add(fragments.indexOf(execution) + 1,
						replyMessage.getSendEvent());
			}
		} else {
			{
				fragments.remove(replyMessage.getSendEvent());
				fragments.add(fragments.indexOf(message.getReceiveEvent()) + 1,
						replyMessage.getSendEvent());
			}
		}
		if (replyMessage != null) {
			if (fragments.contains(
					replyMessage.getReceiveEvent())) {
				fragments.remove(replyMessage.getReceiveEvent());
				fragments.add(fragments.indexOf(replyMessage.getSendEvent()) + 1,
						replyMessage.getReceiveEvent());
			} else {
				fragments.add(fragments.indexOf(replyMessage.getSendEvent()) + 1,
						replyMessage.getReceiveEvent());
			}
		}
		FragmentsService.getInstance().updateFragmentList(startingEndPredecessor != null ? startingEndPredecessor : interaction, fragments);
	}

	/**
	 * Create synchronous typed message.
	 *
	 * @param interaction
	 *            interaction
	 * @param sourceFragment
	 *            source
	 * @param targetFragment
	 *            target
	 * @param startingEndPredecessor
	 *            starting end predecessor
	 * @param finishingEndPredecessor
	 *            finishing end predecessor
	 * @param operation
	 *            operation associated with the message
	 * @return a synchronous message
	 */
	private Message createSynchronousMessage(InteractionFragment interaction, NamedElement sourceFragment,
			NamedElement targetFragment, EObject startingEndPredecessor, EObject finishingEndPredecessor,
			Operation operation) {

		final UMLFactory factory = UMLFactory.eINSTANCE;

		boolean createSourceMessOcc = !(sourceFragment instanceof Gate);
		boolean createTargetMessOcc = !(targetFragment instanceof Gate);


		final Lifeline source = LifelineService.getInstance().getLifeline(sourceFragment);
		final Lifeline target = LifelineService.getInstance().getLifeline(targetFragment);

		String messageName = ""; //$NON-NLS-1$
		if (operation == null) {
			messageName = "Message_" + getMessages(interaction).size(); //$NON-NLS-1$
		} else {
			messageName = operation.getName();
		}

		final Message message = factory.createMessage();
		message.setName(messageName);
		message.setMessageSort(MessageSort.SYNCH_CALL_LITERAL);

		// Create message send event
		if (createSourceMessOcc) {
			final MessageOccurrenceSpecification senderEventMessage = factory
					.createMessageOccurrenceSpecification();
			senderEventMessage.setName(message.getName() + SENDER_MESSAGE_SUFFIX);
			if (source != null) {
				senderEventMessage.getCovereds().add(source);
			}
			senderEventMessage.setMessage(message);
			message.setSendEvent(senderEventMessage);
		} else {
			message.setSendEvent((Gate) sourceFragment);
		}
		// Create message receive event
		if (createTargetMessOcc) {
			final MessageOccurrenceSpecification receiverEventMessage = factory
					.createMessageOccurrenceSpecification();
			receiverEventMessage.setName(message.getName() + RECEIVER_MESSAGE_SUFFIX);
			if (target != null) {
				receiverEventMessage.getCovereds().add(target);
			}
			receiverEventMessage.setMessage(message);
			message.setReceiveEvent(receiverEventMessage);
		} else {
			message.setReceiveEvent((Gate) targetFragment);
		}
		return message;

	}

	/**
	 * Check if is covered type.
	 *
	 * @param element
	 *            Execution specification
	 * @return True if is a covered type
	 */
	private boolean isCoveredTypeSet(ExecutionSpecification element) {
		if (element == null) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if is valid message end for lifeline.
	 *
	 * @param preTarget
	 *            the pre target
	 * @return true, if is valid message end for lifeline
	 */
	private boolean isValidMessageEndForLifeline(EObject preTarget) {
		return true;
	}

	/**
	 * Retrieves the context element ({@link Lifeline} or {@link ExecutionSpecification}) of the given
	 * {@link OccurrenceSpecification}.
	 *
	 * @param occurrenceSpecification
	 *            the {@link OccurrenceSpecification} for which to find the context
	 * @return the {@link ExecutionSpecification} on which the given {@link OccurrenceSpecification} is
	 *         attached or otherwise, the {@link Lifeline}otherwise it is attached to.
	 */
	private NamedElement findOccurrenceSpecificationContext(OccurrenceSpecification occurrenceSpecification) {
		if (occurrenceSpecification == null) {
			return null;
		}
		EList<Lifeline> covereds = occurrenceSpecification.getCovereds();
		if (covereds == null || covereds.isEmpty()) {
			return null;
		}
		final Lifeline lifeline = covereds.get(0);
		final Stack<NamedElement> context = new Stack<NamedElement>();
		context.add(lifeline);

		final List<EObject> allFragments = FragmentsService.getInstance().getEnclosingFragments(occurrenceSpecification);

		final List<InteractionFragment> fragments = new ArrayList<InteractionFragment>();
		for (final EObject fragment : allFragments) {
			if (fragment instanceof InteractionFragment) {
				if (((InteractionFragment) fragment).getCovered(lifeline.getName()) != null) {
					fragments.add((InteractionFragment) fragment);
				}
			}
		}

		for (int i = 0; i < fragments.size(); i++) {
			final InteractionFragment e = fragments.get(i);
			InteractionFragment en;
			if (i + 1 < fragments.size()) {
				en = fragments.get(i + 1);
			} else {
				en = null;
			}

			if (e instanceof MessageOccurrenceSpecification && en != null
					&& en instanceof ExecutionSpecification) {
				context.add(en);
			}

			if (e instanceof ExecutionOccurrenceSpecification) {
				if (en == null || !(en instanceof ExecutionSpecification)) {
					if (!context.isEmpty()) {
						context.pop();
					}
				}
			}

			// Found our element
			if (e == occurrenceSpecification) {
				if (!context.isEmpty()) {
					return context.peek();
				}
			}

			if (e instanceof ExecutionOccurrenceSpecification) {
				if (en != null && en instanceof ExecutionSpecification) {
					context.add(fragments.get(i + 1));
				}
			}

			if (e instanceof MessageOccurrenceSpecification && FragmentsService.getInstance().isEnd(e, fragments)) {
				context.pop();
			}
		}

		return lifeline;
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
		if (sourceView instanceof DNodeSpec && targetView instanceof DNodeSpec) {
			String nameSource = ((DNodeSpec) sourceView).getActualMapping().getName();
			String nameTarget = ((DNodeSpec) targetView).getActualMapping().getName();
			return nameSource.equalsIgnoreCase("Observation") && nameTarget.equals("Observation");
		}


		return false;
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

		if (sourceView instanceof DNodeSpec && targetView instanceof DNodeSpec) {
			String nameSource = ((DNodeSpec) sourceView).getActualMapping().getName();
			String nameTarget = ((DNodeSpec) targetView).getActualMapping().getName();
			boolean isGateSource = nameSource.equalsIgnoreCase("Gate") || nameSource.equalsIgnoreCase("GateBorder");
			boolean isGateTarget = nameTarget.equalsIgnoreCase("Gate") || nameTarget.equalsIgnoreCase("GateBorder");
			return isGateSource || isGateTarget;
		}


		return false;

	}

	/**
	 * Pre condition message basic.
	 *
	 * @param object
	 *            the object
	 * @return true, if successful
	 */
	public boolean preConditionMessageBasic(EObject object) {
		if (object instanceof Message) {
			MessageSort messageSort = ((Message) object).getMessageSort();
			MessageEnd sendEvent = ((Message) object).getSendEvent();
			MessageEnd receiveEvent = ((Message) object).getReceiveEvent();

			return (messageSort == MessageSort.SYNCH_CALL_LITERAL || messageSort == MessageSort.ASYNCH_CALL_LITERAL) && (receiveEvent instanceof MessageOccurrenceSpecification) && (sendEvent instanceof MessageOccurrenceSpecification);

		}


		return false;
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
