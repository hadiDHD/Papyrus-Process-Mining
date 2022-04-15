/*******************************************************************************
 * Copyright (c) 2009, 2011, 2021 Obeo,  CEA LIST, Artal Technologies
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
 *    Yann Binot (ARTAL) - yann.binot@artal.fr - adaptation to integrate in Papyrus
 *******************************************************************************/
package org.eclipse.papyrus.uml.sirius.sequence.diagram.services;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.CombinedFragment;
import org.eclipse.uml2.uml.ConnectableElement;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.ExecutionSpecification;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionFragment;
import org.eclipse.uml2.uml.InteractionOperand;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageOccurrenceSpecification;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.OccurrenceSpecification;
import org.eclipse.uml2.uml.Type;

/**
 * The Class LifelineService.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
public class LifelineService {


	/** The service. */
	static private LifelineService service = null;


	/**
	 * Instantiates a new fragments service.
	 */
	private LifelineService() {

	}

	/**
	 * Gets the single instance of FragmentsService.
	 *
	 * @return single instance of FragmentsService
	 */
	static public LifelineService getInstance() {
		if (service == null) {
			service = new LifelineService();
		}
		return service;
	}


	/**
	 * Get type associated to a lifeline.
	 *
	 * @param target
	 *            Lifeline
	 * @return Type
	 */
	public Type getType(Lifeline target) {
		if (target.getRepresents() != null) {
			return target.getRepresents().getType();
		}

		if (target.getClientDependencies() != null && !target.getClientDependencies().isEmpty()) {
			return ((InstanceSpecification) target.getClientDependencies().get(0).getSuppliers().get(0))
					.getClassifiers().get(0);
		}
		return null;

	}

	/**
	 * Gets the fragments.
	 *
	 * @param results
	 *            the results
	 * @param lifeline
	 *            the lifeline
	 * @param interaction
	 *            the interaction
	 * @return the fragments
	 */
	public void getFragmentsFromLifeline(List<ExecutionSpecification> results, Lifeline lifeline, EObject interaction) {
		EList<InteractionFragment> fragments = null;
		if (interaction instanceof Interaction) {
			fragments = ((Interaction) interaction).getFragments();
		}
		if (interaction instanceof InteractionOperand) {
			fragments = ((InteractionOperand) interaction).getFragments();
		}
		for (InteractionFragment interactionFragment : fragments) {
			if (interactionFragment instanceof ExecutionSpecification) {
				if (interactionFragment.getCovereds().contains(lifeline)) {
					if (!ExecutionService.getInstance().isNestedExecution((ExecutionSpecification) interactionFragment)) {
						results.add((ExecutionSpecification) interactionFragment);
					}
				}
			}
			if (interactionFragment instanceof CombinedFragment) {
				EList<InteractionOperand> operands = ((CombinedFragment) interactionFragment).getOperands();
				for (InteractionOperand interactionOperand : operands) {
					getFragmentsFromLifeline(results, lifeline, interactionOperand);
				}
			}
		}
	}


	/**
	 * Get the lifeline associated to a fragment.
	 *
	 * @param fragment
	 *            Fragment
	 * @return Lifeline if exists otherwise null
	 */
	public Lifeline getLifeline(EObject fragment) {
		if (fragment instanceof Lifeline) {
			return (Lifeline) fragment;
		} else if (fragment instanceof InteractionFragment) {
			final List<Lifeline> lifelines = ((InteractionFragment) fragment).getCovereds();
			if (lifelines != null && !lifelines.isEmpty()) {
				return lifelines.get(0);
			}
		}
		return null;
	}

	



	/**
	 * Compute lifeline comment label.
	 *
	 * @param lifeline
	 *            Lifeline
	 * @return LAbel
	 */
	public String computeLifelineCommentLabel(Lifeline lifeline) {
		final ConnectableElement represent = lifeline.getRepresents();
		// ['current container :
		// '+self.oclAsType(uml::Lifeline).represents.eContainer().oclAsType(uml::NamedElement).name/]
		final EList<Dependency> dependencies = lifeline.getClientDependencies();

		if (represent != null) {
			final EObject container = represent.eContainer();
			if (dependencies.size() == 0) {
				if (container != null && container instanceof NamedElement) {
					return "current container : " + ((NamedElement) container).getName(); //$NON-NLS-1$
				}
			} else {
				// ['current container :
				// '+self.oclAsType(uml::Lifeline).represents.eContainer().name+'\n
				// context dependency:
				// '+self.oclAsType(uml::Lifeline).clientDependency.supplier.name->sep('::')/]
				if (container != null && container instanceof NamedElement) {
					final EList<NamedElement> suppliers = dependencies.get(0).getSuppliers();
					if (suppliers != null && suppliers.size() > 0) {
						final EObject supplier = suppliers.get(0);
						if (supplier != null && supplier instanceof NamedElement) {
							return "current container : " + ((NamedElement) container).getName() //$NON-NLS-1$
									+ " context dependency: " + ((NamedElement) supplier).getName(); //$NON-NLS-1$
						}
					}
				}
			}
		} else {
			if (dependencies != null) {
				final EList<NamedElement> suppliers = dependencies.get(0).getSuppliers();
				if (dependencies.size() > 1) {
					// ['context dependency:
					// '+self.oclAsType(uml::Lifeline).clientDependency.supplier.name->sep('::')/]
					if (suppliers != null && suppliers.size() > 0) {
						final EObject supplier = suppliers.get(0);
						if (supplier != null && supplier instanceof NamedElement) {
							return "context dependency: " + ((NamedElement) supplier).getName(); //$NON-NLS-1$
						}
					}
				} else if (dependencies.size() == 1) {
					// ['current container :
					// '+self.oclAsType(uml::Lifeline).clientDependency.supplier.eContainer().name/]
					if (suppliers != null && suppliers.size() > 0) {
						final EObject supplier = suppliers.get(0);
						if (supplier != null) {
							final EObject container = supplier.eContainer();
							if (container != null && container instanceof NamedElement) {
								return "current container : " + ((NamedElement) container).getName(); //$NON-NLS-1$
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Check if lifeline is representing a property.
	 *
	 * @param element
	 *            Lifeline
	 * @return True iflifeline is representing a property
	 */
	public boolean isRepresentingProperty(Lifeline element) {
		return false;
	}

	/**
	 * Delete lifeline.
	 *
	 * @param lifeline
	 *            Lifeline to delete
	 */
	public void delete(Lifeline lifeline) {
		if (lifeline == null) {
			return;
		}
		// Delete dependency
		deleteContext(lifeline);

		// Delete all executions
		for (final ExecutionSpecification execution : executionSemanticCandidates(lifeline)) {
			if (execution instanceof ExecutionSpecification) {
				ExecutionService.getInstance().deleteExecution(execution);
			}
		}

		// Delete all messages
		for (final Message message : getAllMessages(lifeline)) {
			MessageService.getInstance().delete(message);
		}

		// Delete lifeline
		lifeline.destroy();
	}

	/**
	 * Find the first level of {@link ExecutionSpecification} in the given {@link InteractionFragment} list.
	 *
	 * @param lifeline
	 *            the {@link Lifeline} which is covered by the searched {@link ExecutionSpecification}
	 * @param candidateFragments
	 *            a sub-list of {@link InteractionFragment} to inspect for the first
	 *            {@link ExecutionSpecification} level.
	 * @return {@link List} of the {@link ExecutionSpecification}
	 */
	private List<ExecutionSpecification> getFirstLevelExecutions(Lifeline lifeline,
			final List<InteractionFragment> candidateFragments) {
		final List<ExecutionSpecification> executions = new ArrayList<ExecutionSpecification>();
		ExecutionSpecification subExec = null;
		for (final InteractionFragment fragment : candidateFragments) {
			if (fragment instanceof ExecutionSpecification && fragment.getCovereds().contains(lifeline)) {
				// Element on the same lifeline
				if (subExec == null) {
					subExec = (ExecutionSpecification) fragment;
				}
			} else if (fragment instanceof OccurrenceSpecification && subExec != null
					&& fragment.equals(subExec.getFinish())) {
				executions.add(subExec);
				subExec = null;
			}
			if (fragment instanceof CombinedFragment) {
				EList<InteractionOperand> operands = ((CombinedFragment) fragment).getOperands();
				for (InteractionOperand operand : operands) {
					executions.addAll(getFirstLevelExecutions(lifeline, operand.getFragments()));
				}

			}
		}
	
		return executions;
	}

	/**
	 * Finds the first level of {@link ExecutionSpecification} in the context of the given {@link Lifeline}.
	 *
	 * @param lifeline
	 *            the context.
	 * @return the {@link ExecutionSpecification} semantic candidates.
	 */
	private List<ExecutionSpecification> executionSemanticCandidates(Lifeline lifeline) {
		return getFirstLevelExecutions(lifeline, lifeline.getInteraction().getFragments());
	}

	/**
	 * Get all messages associated to lifeline.
	 *
	 * @param lifeline
	 *            Lifeline
	 * @return Messages associated to lifeline
	 */
	private List<Message> getAllMessages(Lifeline lifeline) {
		final List<Message> messages = new ArrayList<Message>();
		if (lifeline != null && lifeline.getInteraction() != null) {
			for (final Message message : lifeline.getInteraction().getMessages()) {
				for (final Lifeline coveredLifeline : ((MessageOccurrenceSpecification) message.getSendEvent())
						.getCovereds()) {
					if (lifeline.equals(coveredLifeline)) {
						messages.add(message);
					}
				}
			}
		}
		return messages;
	}


	/**
	 * Delete the client dependency used for the context.
	 *
	 * @param lifeline
	 *            the lifeline
	 */
	// add an eannotation
	private void deleteContext(Lifeline lifeline) {
		final Object[] dependencies = lifeline.getClientDependencies().toArray();

		for (int i = 0; i < dependencies.length; i++) {
			EcoreUtil.delete((Dependency) dependencies[i]);
		}

	}

}
