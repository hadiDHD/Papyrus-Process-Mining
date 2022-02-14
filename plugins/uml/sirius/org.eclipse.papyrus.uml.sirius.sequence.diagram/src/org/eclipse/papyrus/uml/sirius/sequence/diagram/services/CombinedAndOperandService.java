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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.sirius.diagram.sequence.ordering.EventEnd;
import org.eclipse.uml2.uml.CombinedFragment;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.InteractionOperand;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OccurrenceSpecification;

/**
 * The Class CombinedAndOperandService provides services to manage CombinedFragment and InteractionOperand.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
public class CombinedAndOperandService {


	/** The service. */
	static private CombinedAndOperandService service = null;


	/**
	 * Instantiates a new fragments service.
	 */
	private CombinedAndOperandService() {

	}

	/**
	 * Gets the single instance of FragmentsService.
	 *
	 * @return single instance of FragmentsService
	 */
	static public CombinedAndOperandService getInstance() {
		if (service == null) {
			service = new CombinedAndOperandService();
		}
		return service;
	}

	/**
	 * Delete combined fragment.
	 *
	 * @param combinedFragment
	 *            the combined fragment
	 */
	public void deleteCombinedFragment(CombinedFragment combinedFragment) {

		Interaction enclosingInteraction = combinedFragment.getEnclosingInteraction();
		EList<InteractionFragment> fragments = enclosingInteraction.getFragments();
		OccurrenceSpecification startOC = null;
		OccurrenceSpecification endOC = null;
		for (InteractionFragment interactionFragment : fragments) {
			if (interactionFragment instanceof OccurrenceSpecification) {
				EList<EAnnotation> eAnnotations = interactionFragment.getEAnnotations();
				for (EAnnotation annotation : eAnnotations) {
					if (annotation.getSource().equals(combinedFragment.getName() + "_start")) {
						startOC = (OccurrenceSpecification) interactionFragment;
					}
					if (annotation.getSource().equals(combinedFragment.getName() + "_end")) {
						endOC = (OccurrenceSpecification) interactionFragment;
					}
				}
			}
		}

		fragments.remove(startOC);
		fragments.remove(endOC);

		int indexOfFragment = fragments.indexOf(combinedFragment);


		EList<InteractionOperand> operands = combinedFragment.getOperands();
		List<InteractionFragment> opeFragmentToMove = new ArrayList<>();
		for (InteractionOperand interactionOperand : operands) {

			EList<InteractionFragment> fragmentsOperand = interactionOperand.getFragments();
			OccurrenceSpecification operandStart = null;
			for (InteractionFragment fragmentOperand : fragmentsOperand) {
				List<EAnnotation> annotationsOperand = fragmentOperand.getEAnnotations();
				for (EAnnotation annotatationOp : annotationsOperand) {
					if (annotatationOp.getSource().equals(interactionOperand.getName() + "_start")) {
						operandStart = (OccurrenceSpecification) fragmentOperand;
						break;
					}
				}
			}
			fragmentsOperand.remove(operandStart);
			opeFragmentToMove.addAll(fragmentsOperand);
		}

		fragments.addAll(indexOfFragment, opeFragmentToMove);
		fragments.remove(combinedFragment);

	}


	/**
	 * Creates the E annotations.
	 * These EAnnotations allow to have Ends around the context element.
	 *
	 * @param context
	 *            the context the element to have need EAnnotations ends
	 */
	public void createEAnnotations(EObject context) {
		if (context instanceof EModelElement) {
			EAnnotation annotStart = EcoreFactory.eINSTANCE.createEAnnotation();
			annotStart.setSource(((NamedElement) context).getName() + "_start");
			((EModelElement) context).getEAnnotations().add(annotStart);

			EAnnotation annotEnd = EcoreFactory.eINSTANCE.createEAnnotation();
			annotEnd.setSource(((NamedElement) context).getName() + "_end");
			((EModelElement) context).getEAnnotations().add(annotEnd);
		}
	}

	/**
	 * Manage operand fragment.
	 *
	 * @param context                 the context
	 * @param startingEndPredecessor  the starting end predecessor
	 * @param finishingEndPredecessor the finishing end predecessor
	 */
	public void manageOperandFragment(EObject context, EventEnd startingEndPredecessor,
			EventEnd finishingEndPredecessor) {
		EObject startingEndPredecessorSemanticEnd = null;
		if (startingEndPredecessor != null) {
			startingEndPredecessorSemanticEnd = (EObject) startingEndPredecessor.getSemanticEnd();
		}
		EObject finishingEndPredecessorSemanticEnd = null;
		if (finishingEndPredecessor != null) {
			finishingEndPredecessorSemanticEnd = (EObject) finishingEndPredecessor.getSemanticEnd();
		}
		if (context instanceof InteractionOperand) {
			InteractionFragment interactionFragment = null;
			if (finishingEndPredecessorSemanticEnd instanceof OccurrenceSpecification) {
				interactionFragment = ((OccurrenceSpecification) finishingEndPredecessorSemanticEnd).getEnclosingInteraction();
			} else {
				if (finishingEndPredecessorSemanticEnd instanceof EAnnotation) {
					interactionFragment  = (InteractionOperand) context;
				}
			}
			List<EObject> fragments = FragmentsService.getInstance().getFragmentsAndAnnotation(interactionFragment);
			int fromIndex = startingEndPredecessorSemanticEnd != null
					? fragments.indexOf(startingEndPredecessorSemanticEnd) + 1
					: 0;
			int toIndex = fragments.indexOf(finishingEndPredecessorSemanticEnd);
			List<EObject> subList = new ArrayList<>(fragments.subList(fromIndex, toIndex + 1));
			fragments.removeAll(subList);
			for (EObject object : subList) {
				if (object instanceof InteractionFragment) {
					((InteractionOperand) context).getFragments().add((InteractionFragment) object);
				}
			}
		}
	}

}
