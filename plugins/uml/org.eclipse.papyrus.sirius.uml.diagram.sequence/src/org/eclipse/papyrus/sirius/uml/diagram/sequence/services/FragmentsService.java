/******************************************************************************
 * Copyright (c) 2021,2022 CEA LIST, Artal Technologies and Obeo
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
 *    Jessy Mallet (Obeo) - jessy.mallet@obeo.fr - Add FoundMessage annotation
 *****************************************************************************/
package org.eclipse.papyrus.sirius.uml.diagram.sequence.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.BehaviorExecutionSpecification;
import org.eclipse.uml2.uml.CombinedFragment;
import org.eclipse.uml2.uml.DestructionOccurrenceSpecification;
import org.eclipse.uml2.uml.ExecutionSpecification;
import org.eclipse.uml2.uml.Gate;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.InteractionOperand;
import org.eclipse.uml2.uml.InteractionUse;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageOccurrenceSpecification;

/**
 * The Class FragmentsService.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
public class FragmentsService {

	/** The service. */
	static private FragmentsService service = null;


	/**
	 * Instantiates a new fragments service.
	 */
	private FragmentsService() {

	}

	/**
	 * Gets the single instance of FragmentsService.
	 *
	 * @return single instance of FragmentsService
	 */
	static public FragmentsService getInstance() {
		if(service==null) {
			service = new FragmentsService();
		}
		return service;
	}





	/**
	 * Gets the finish.
	 *
	 * @param o
	 *            the o
	 * @return the finish
	 */
	public EObject getFinish(EObject o) {
		if (o instanceof ExecutionSpecification) {
			return ((ExecutionSpecification) o).getFinish();
		}
		if (o instanceof CombinedFragment) {

			EList<EAnnotation> eAnnotations = ((CombinedFragment) o).getEAnnotations();
			if (eAnnotations != null && !eAnnotations.isEmpty()) {
				for (EAnnotation annot : eAnnotations) {
					if (annot.getSource().equals(((CombinedFragment) o).getName() + "_end")) {
						return annot;
					}
				}
			}
		}
		if (o instanceof InteractionOperand) {
			EList<EAnnotation> eAnnotations = ((InteractionOperand) o).getEAnnotations();
			if (eAnnotations != null && !eAnnotations.isEmpty()) {
				for (EAnnotation annot : eAnnotations) {
					if (annot.getSource().equals(((InteractionOperand) o).getName() + "_end")) {
						return annot;
					}
				}
			}
		}
		return o;
	}


	/**
	 * Gets the start.
	 *
	 * @param o
	 *            the o
	 * @return the start
	 */
	public EObject getStart(EObject o) {
		if (o instanceof ExecutionSpecification) {
			return ((ExecutionSpecification) o).getStart();
		}
		if (o instanceof CombinedFragment) {


			EList<EAnnotation> eAnnotations = ((CombinedFragment) o).getEAnnotations();
			if (eAnnotations != null && !eAnnotations.isEmpty()) {
				for (EAnnotation annot : eAnnotations) {
					if (annot.getSource().equals(((CombinedFragment) o).getName() + "_start")) {
						return annot;
					}
				}
			}
		}
		if (o instanceof InteractionOperand) {
			EList<EAnnotation> eAnnotations = ((InteractionOperand) o).getEAnnotations();
			if (eAnnotations != null && !eAnnotations.isEmpty()) {
				for (EAnnotation annot : eAnnotations) {
					if (annot.getSource().equals(((InteractionOperand) o).getName() + "_start")) {
						return annot;
					}
				}
			}
		}
		return o;
	}

	/**
	 * Gets the fragments ordering ends.
	 *
	 * @param object
	 *            the object
	 * @return the fragments ordering ends
	 */
	public List<EObject> getFragmentsOrderingEnds(EObject object) {
		List<EObject> results = new ArrayList<>();
		if (object instanceof Interaction) {
	
			fillFragmentOrderingInteraction(object, results);
		}
		if (object instanceof InteractionOperand) {
			fillFragmentOrderingInteractionOperand(object, results);
		}
		if (object instanceof CombinedFragment) {
			fillFragmentOrderingCombinedFragment(object, results);
		}
	
		return results;
	}

	/**
	 * Fill fragment ordering combined fragment.
	 *
	 * @param object the object
	 * @param results the results
	 */
	private void fillFragmentOrderingCombinedFragment(EObject object, List<EObject> results) {
		EList<InteractionOperand> operands = ((CombinedFragment) object).getOperands();
		for (InteractionOperand interactionOperand : operands) {
			results.addAll(getFragmentsOrderingEnds(interactionOperand));
		}
	}

	/**
	 * Fill fragment ordering interaction operand.
	 *
	 * @param object the object
	 * @param results the results
	 */
	private void fillFragmentOrderingInteractionOperand(EObject object, List<EObject> results) {
		EList<InteractionFragment> fragments = ((InteractionOperand) object).getFragments();
		EList<EAnnotation> eAnnotations = ((InteractionOperand) object).getEAnnotations();
		if (eAnnotations != null && !eAnnotations.isEmpty()) {
			for (EAnnotation annot : eAnnotations) {
				if (annot.getSource().equals(((InteractionOperand) object).getName() + "_start")) {
					results.add(annot);
				}
			}
		}
		for (InteractionFragment interactionFragment : fragments) {
			fillIInteractionFragment(results, interactionFragment);
			if (interactionFragment instanceof CombinedFragment) {
				results.addAll(getFragmentsOrderingEnds(interactionFragment));
			}
		}
		if (eAnnotations != null && !eAnnotations.isEmpty()) {
			for (EAnnotation annot : eAnnotations) {
				if (annot.getSource().equals(((InteractionOperand) object).getName() + "_end")) {
					results.add(annot);
				}
			}
		}
	}

	/**
	 * Fill fragment ordering interaction.
	 *
	 * @param object the object
	 * @param results the results
	 */
	private void fillFragmentOrderingInteraction(EObject object, List<EObject> results) {
		EList<InteractionFragment> fragments = ((Interaction) object).getFragments();
		for (InteractionFragment interactionFragment : fragments) {
			if (interactionFragment instanceof CombinedFragment) {
				EList<EAnnotation> eAnnotations = interactionFragment.getEAnnotations();
				if (eAnnotations != null && !eAnnotations.isEmpty()) {
					for (EAnnotation annot : eAnnotations) {
						if (annot.getSource().equals(((CombinedFragment) interactionFragment).getName() + "_start")) {
							results.add(annot);
						}
					}
				}
			}
			fillIInteractionFragment(results, interactionFragment);
			if (interactionFragment instanceof CombinedFragment) {

				results.addAll(getFragmentsOrderingEnds(interactionFragment));
				EList<EAnnotation> eAnnotations = interactionFragment.getEAnnotations();
				if (eAnnotations != null && !eAnnotations.isEmpty()) {
					for (EAnnotation annot : eAnnotations) {
						if (annot.getSource().equals(((CombinedFragment) interactionFragment).getName() + "_end")) {
							results.add(annot);
						}
					}
				}
			}

		}
	}

	/**
	 * Fill I interaction fragment.
	 *
	 * @param results the results
	 * @param interactionFragment the interaction fragment
	 */
	private void fillIInteractionFragment(List<EObject> results, InteractionFragment interactionFragment) {

		results.add(interactionFragment);
		if (interactionFragment instanceof MessageOccurrenceSpecification) {
			EList<EAnnotation> eAnnotations = interactionFragment.getEAnnotations();
			for (EAnnotation eAnnotation : eAnnotations) {
				if (eAnnotation.getSource().equals(interactionFragment.getName() + "LOSTMESSAGE") || eAnnotation.getSource().equals(interactionFragment.getName() + "FOUNDMESSAGE")) { //$NON-NLS-1$ //$NON-NLS-2$
					results.add(eAnnotation);
				}
			}
		}
	}

	/**
	 * Gets the enclosing fragments.
	 *
	 * @param interactionFragment
	 *            the interaction fragment
	 * @return the enclosing fragments
	 */
	public List<EObject> getEnclosingFragments(EObject interactionFragment) {
		final InteractionFragment enclosingFragment = getEnclosingFragment(interactionFragment);
		List<EObject> fragments = null;
		if (enclosingFragment instanceof Interaction) {
			fragments = getFragmentsAndAnnotation(enclosingFragment);
		}
		if (enclosingFragment instanceof InteractionOperand) {
			fragments = getFragmentsAndAnnotation(enclosingFragment);
		}
		return fragments;
	}

	/**
	 * Gets the fragments and annotation.
	 *
	 * @param root
	 *            the root
	 * @return the fragments and annotation
	 */
	public List<EObject> getFragmentsAndAnnotation(InteractionFragment root) {
		List<EObject> results = new ArrayList<>();
		if (root instanceof Interaction) {
			EList<InteractionFragment> fragments = ((Interaction) root).getFragments();
			getFragmentsAndAnnotations(results, fragments);
		}
		if (root instanceof InteractionOperand) {
	
			EList<InteractionFragment> fragments = ((InteractionOperand) root).getFragments();
			getFragmentsAndAnnotations(results, fragments);
	
		}

		return results;
	}

	/**
	 * Gets the enclosing fragment.
	 *
	 * @param fragment
	 *            the fragment
	 * @return the enclosing fragment
	 */
	public InteractionFragment getEnclosingFragment(EObject fragment) {
		if (fragment instanceof Interaction || fragment instanceof InteractionOperand) {
			return (InteractionFragment) fragment;
		}
	
		if (fragment instanceof InteractionFragment) {
			Interaction enclosingInteraction = ((InteractionFragment) fragment).getEnclosingInteraction();
			if (enclosingInteraction == null) {
				return ((InteractionFragment) fragment).getEnclosingOperand();
			}
			return enclosingInteraction;
		}
		if (fragment instanceof EAnnotation) {
			return getEnclosingFragment(fragment.eContainer());
		}
		if (fragment instanceof Lifeline) {
			return ((Lifeline) fragment).getInteraction();
		}
		if (fragment instanceof Message) {
			return ((Message) fragment).getInteraction();
		}
		if (fragment instanceof Gate) {
			InteractionOperand operand = ((Gate) fragment).getOperand();
			if (operand == null) {
				return getEnclosingFragment(fragment.eContainer());
			}
			return operand;
		}
		return null;
	
	}

	/**
	 * Gets the parent interaction.
	 *
	 * @param interactionFragment
	 *            the interaction fragment
	 * @return the parent interaction
	 */
	public Interaction getParentInteraction(InteractionFragment interactionFragment) {
		if (interactionFragment instanceof InteractionOperand) {
			return getParentInteraction((InteractionFragment) interactionFragment.eContainer());
		}
		Interaction enclosingInteraction = interactionFragment.getEnclosingInteraction();
		if (enclosingInteraction == null) {
			InteractionOperand enclosingOperand = interactionFragment.getEnclosingOperand();
			return getParentInteraction((InteractionFragment) enclosingOperand.eContainer());
		}
		return enclosingInteraction;
	}

	/**
	 * Get the index of a predecessor fragment.
	 *
	 * @param fragment
	 *            Fragment to search
	 * @param fragments
	 *            List of fragments
	 * @return Index of fragment if exists otherwise 0
	 */
	public int getFragmentIndex(EObject fragment, final List<EObject> fragments) {
		if (fragment != null) {
			return fragments.indexOf(fragment);
		}
		return -1;
	}

	/**
	 * Make union.
	 *
	 * @param context
	 *            the context
	 * @return the list
	 */
	public List<EObject> makeUnion(EObject context) {
		List<EObject> fragments = FragmentsService.getInstance()
				.getFragmentsAndAnnotation((InteractionFragment) context);
		return fragments;
	}

	/**
	 * Get observation points list.
	 *
	 * @param context the context
	 * @return the list
	 */
	public List<EObject> getObservationPoints(EObject context) {
		List<EObject> fragments = FragmentsService.getInstance()
				.getFragmentsAndAnnotation((InteractionFragment) context);
		 
		fragments = fragments.stream().filter(p -> !(p instanceof CombinedFragment) && !(p instanceof ExecutionSpecification) && !(p instanceof InteractionUse))
				.collect(Collectors.toList());
		return fragments;
	}


	/**
	 * Eol precondition.
	 *
	 * @param p
	 *            the p
	 * @return true, if successful
	 */
	public boolean eolPrecondition(Lifeline p) {
		Interaction i = p.getInteraction();
		EList<InteractionFragment> fragments = i.getFragments();
		for (InteractionFragment interactionFragment : fragments) {
			if (interactionFragment instanceof DestructionOccurrenceSpecification) {
				Lifeline covered = ((DestructionOccurrenceSpecification) interactionFragment).getCovered();
				if (covered != null && covered.equals(p)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Compute fragments.
	 *
	 * @param execution
	 *            the execution
	 * @param startingEndPredecessorAfter
	 *            the starting end predecessor after
	 * @param parentInteraction
	 *            the parent interaction
	 * @return the list
	 */
	public List<EObject> computeFragments(EObject execution, EObject startingEndPredecessorAfter, InteractionFragment parentInteraction) {
		List<EObject> fragmentsource = FragmentsService.getInstance().getEnclosingFragments(execution);
		List<EObject> fragments = null;
		if (startingEndPredecessorAfter != null) {
			fragments = FragmentsService.getInstance().getEnclosingFragments(startingEndPredecessorAfter);
		} else {

			fragments = /* getParentInteraction(execution).getFragments() */FragmentsService.getInstance().getFragmentsAndAnnotation(parentInteraction);
		}
		if (fragmentsource != null && !fragmentsource.equals(fragments)/* && execution instanceof ExecutionSpecification */) {
			if (execution instanceof ExecutionSpecification) {
				fragmentsource.remove(((ExecutionSpecification) execution).getStart());
			}
			fragmentsource.remove(execution);
			if (execution instanceof ExecutionSpecification) {
				fragmentsource.remove(((ExecutionSpecification) execution).getFinish());


				fragments.add(((ExecutionSpecification) execution).getStart());
			}
			fragments.add(execution);
			if (execution instanceof ExecutionSpecification) {
				fragments.add(((ExecutionSpecification) execution).getFinish());
			}
		}
		return fragments;
	}



	/**
	 * Update fragment list.
	 *
	 * @param startingEndPredecessorAfter
	 *            the starting end predecessor after
	 * @param fragments
	 *            the fragments
	 */
	public void updateFragmentList(EObject startingEndPredecessorAfter, List<EObject> fragments) {
		InteractionFragment enclosingFragment = FragmentsService.getInstance().getEnclosingFragment(startingEndPredecessorAfter);
		List<InteractionFragment> fragmentToUpdate = new ArrayList<>();
		if (enclosingFragment instanceof Interaction) {
			fragmentToUpdate = ((Interaction) enclosingFragment).getFragments();
		}
		if (enclosingFragment instanceof InteractionOperand) {
			fragmentToUpdate = ((InteractionOperand) enclosingFragment).getFragments();
		}
		fragmentToUpdate.clear();
		for (EObject interactionFragment : fragments) {
			if (interactionFragment instanceof InteractionFragment) {
				if (enclosingFragment.eContainer() != interactionFragment) {
					fragmentToUpdate.add((InteractionFragment) interactionFragment);
				}
			}
		}
	}

	/**
	 * Check if fragment is an execution finish.
	 *
	 * @param endCandidate
	 *            Element to check
	 * @param fragments
	 *            Defined fragments
	 * @return True if element is the end of an execution
	 */
	public boolean isEnd(InteractionFragment endCandidate, List<InteractionFragment> fragments) {
		final List<InteractionFragment> executionFinishes = new ArrayList<InteractionFragment>();
		for (final InteractionFragment fragment : fragments) {
			if (fragment instanceof BehaviorExecutionSpecification) {
				// Get start
				final BehaviorExecutionSpecification behavior = (BehaviorExecutionSpecification) fragment;
				// Get finish
				executionFinishes.add(behavior.getFinish());
			}
		}
		return executionFinishes.contains(endCandidate);
	}

	/**
	 * Gets the fragments and annotations.
	 *
	 * @param results
	 *            the results
	 * @param fragments
	 *            the fragments
	 * @return the fragments and annotations
	 */
	private void getFragmentsAndAnnotations(List<EObject> results, EList<InteractionFragment> fragments) {
		for (InteractionFragment interactionFragment : fragments) {
			if (interactionFragment instanceof CombinedFragment) {
				EList<EAnnotation> eAnnotations = interactionFragment.getEAnnotations();
				for (EAnnotation annot : eAnnotations) {
					if (annot.getSource().equals(((CombinedFragment) interactionFragment).getName() + "_start")) { //$NON-NLS-1$
						results.add(annot);
					}
				}
				fillIInteractionFragment(results, interactionFragment);
				for (EAnnotation annot : eAnnotations) {
					if (annot.getSource().equals(((CombinedFragment) interactionFragment).getName() + "_end")) { //$NON-NLS-1$
						results.add(annot);
					}
				}			
				for (InteractionOperand operand : ((CombinedFragment) interactionFragment).getOperands()) {
						getFragmentsAndAnnotations(results, operand.getFragments());
				}
			} else {
				fillIInteractionFragment(results, interactionFragment);
			}
		}
	}

}
