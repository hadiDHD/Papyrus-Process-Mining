/*****************************************************************************
 * Copyright (c) 2022 CEA LIST
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Ibtihel Khemir (CEA LIST) <ibtihel.khemir@cea.fr> - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.sirius.uml.diagram.statemachine.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.viewpoint.DSemanticDecorator;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Vertex;

/**
 * 
 */
public class StateMachineDiagramServices {
	/**
	 * A singleton instance to be accessed by other java services.
	 */
	public static final StateMachineDiagramServices INSTANCE = new StateMachineDiagramServices();

	/**
	 * TODO : adapted from ClassDiagram
	 * Get the target element of the Link.
	 * 
	 * @param source
	 *            the element ({@link Comment} or {@link Constraint}
	 * @return
	 *         the list of annotated elements if source if a {@link Comment} and the list of constrained elements if the source is a {@link Constraint} and <code>null</code> in other cases
	 */
	public static Collection<Element> link_getTarget_SMD(final Element source) {
		if (source instanceof Constraint) {
			final Constraint sourceElement = (Constraint) source;
			return sourceElement.getConstrainedElements();
		} else if (source instanceof Comment) {
			final Comment sourceElement = (Comment) source;
			return sourceElement.getAnnotatedElements();
		}
		return null;
	}

	/**
	 * TODO : adapted from ClassDiagram
	 * Service used to determine if the selected Link edge source could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the source element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean link_canReconnectSource_SMD(final Element context, final Element newSource) {
		// we want to avoid to change the semantic of a link : either is works on Constraint, either it works on Comment
		if (context instanceof Constraint && newSource instanceof Constraint) {
			return true;
		}
		if (context instanceof Comment && newSource instanceof Comment) {
			return true;
		}
		return false;
	}

	/**
	 * TODO : adapted from ClassDiagram
	 * Service used to determine if the selected Link edge target could be
	 * reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the source element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean link_canReconnectTarget_SMD(Element context, Element newSource) {
		return newSource instanceof State
				|| newSource instanceof Comment
				|| newSource instanceof Constraint
				|| newSource instanceof FinalState
				|| newSource instanceof Transition
				|| newSource instanceof Pseudostate;
	}

	/**
	 * TODO : adapted from ClassDiagram
	 * Service used to reconnect a Link source.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldsource
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @param otherEnd
	 *            Represents the view attached to the target of the link
	 */
	public void link_reconnectSource_SMD(final Element context, final Element oldSource, final Element newSource, final EObject otherEnd) {
		Element target = null;
		if (otherEnd instanceof DSemanticDecorator) {
			target = (Element) ((DSemanticDecorator) otherEnd).getTarget();
		}

		// remove the target from the old source
		if (oldSource instanceof Comment) {
			((Comment) oldSource).getAnnotatedElements().remove(target);
		} else if (oldSource instanceof Constraint) {
			((Constraint) oldSource).getConstrainedElements().remove(target);
		}

		// add the target to the new source
		if (newSource instanceof Comment) {
			((Comment) newSource).getAnnotatedElements().add(target);
		} else if (newSource instanceof Constraint) {
			((Constraint) newSource).getConstrainedElements().add(target);
		}
	}

	/**
	 * TODO : adapted from ClassDiagram
	 * Service used to reconnect a Link edge target.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldTarget
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @param otherEnd
	 *            Represents the view attached to the source of the link
	 */
	public void link_reconnectTarget_SMD(final Element context, final Element oldTarget, final Element newTarget, final EObject otherEnd) {
		Element source = null;
		if (otherEnd instanceof DSemanticDecorator) {
			source = (Element) ((DSemanticDecorator) otherEnd).getTarget();
		}

		if (source instanceof Comment) {
			((Comment) source).getAnnotatedElements().remove(oldTarget);
			((Comment) source).getAnnotatedElements().add(newTarget);
		} else if (source instanceof Constraint) {
			((Constraint) source).getConstrainedElements().remove(oldTarget);
			((Constraint) source).getConstrainedElements().add(newTarget);
		}
	}

	/**
	 * TODO : adapted from ClassDiagram
	 * Check if the source and target are valid for a ContextLink
	 * 
	 * @param context
	 *            the current context
	 * @param sourceView
	 *            the source view
	 * @param targetView
	 *            the target view
	 * @param source
	 *            the semantic source element
	 * @param target
	 *            the semantic target element
	 * @return true if the source and target are valid
	 */
	public boolean contextLink_isValidSourceAndTarget_SMD(final EObject context, final EObject sourceView, final EObject targetView, final Element source, final Element target) {
		boolean isValid = false;
		if (source == target) {
			// 1. we forbid reflexive Context of course
			return false;
		}

		// 2. semantic condition
		if (source instanceof Constraint) {
			isValid = target instanceof Namespace;
		}
		return isValid;
	}

	/**
	 * Service used to determine if the selected Transition edge source could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the source element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean transition_canReconnectSource(final Element context, final Element newSource) {
		return newSource instanceof FinalState
				|| newSource instanceof Pseudostate
				|| newSource instanceof State;
	}

	/**
	 * Service used to determine if the selected Transition edge target could be reconnected to an element.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param newTarget
	 *            Represents the source element pointed by the edge after reconnecting
	 * @return true if the edge could be reconnected
	 */
	public boolean transition_canReconnectTarget(final Element context, final Element newTarget) {
		return newTarget instanceof FinalState
				|| newTarget instanceof Pseudostate
				|| newTarget instanceof State;
	}

	/**
	 * Service used to reconnect a Transition source.
	 *
	 * @param transition
	 *            Element attached to the existing edge
	 * @param newSource
	 *            Represents the semantic element pointed by the edge after reconnecting
	 * @param oldsource
	 *            Represents the semantic element pointed by the edge before reconnecting
	 */
	public void transition_reconnectSource(final Transition transition, final Vertex oldSource, final Vertex newSource) {
		transition.setSource(newSource);
		final Region region = newSource.getContainer();
		final Element owner = transition.getOwner();
		if (owner != region) {
			region.getTransitions().add(transition);
		}
	}

	/**
	 * Service used to reconnect a Transition edge target.
	 *
	 * @param context
	 *            Element attached to the existing edge
	 * @param oldTarget
	 *            Represents the semantic element pointed by the edge before reconnecting
	 * @param newTarget
	 *            Represents the semantic element pointed by the edge after reconnecting
	 */
	public void transition_reconnectTarget(final Transition transition, final Vertex oldTarget, final Vertex newTarget) {
		transition.setTarget(newTarget);
	}

	/**
	 * 
	 * @param semanticContext
	 *            the context in which we are looking for {@link Transition}
	 * @return
	 *         {@link Transition} available in the context
	 */
	public Collection<Transition> transition_getSemanticCandidates(final EObject semanticContext) {
		final Collection<Transition> transitions = new HashSet<Transition>();
		if (semanticContext instanceof StateMachine) {
			final StateMachine stateMachine = (StateMachine) semanticContext;
			for (Region r : stateMachine.getRegions()) {
				transitions.addAll(getAllTransition(r));
			}
		}
		return transitions;
	}

	/**
	 * 
	 * @param reg
	 *            a UML {@link Region}
	 * @return
	 *         all {@link Transition} recursively
	 **/
	private static final Collection<Transition> getAllTransition(final Region reg) {
		final Collection<Transition> transitions = new HashSet<Transition>();
		final Iterator<NamedElement> iter = reg.getMembers().iterator();
		while (iter.hasNext()) {
			final NamedElement current = iter.next();
			if (current instanceof Region) {
				transitions.addAll(getAllTransition((Region) current));
			}
			if(current instanceof State) {
				transitions.addAll(getAllTransition((State) current));
			}
			if (current instanceof Transition) {
				transitions.add((Transition) current);
			}
		}
		return transitions;
	}
	
	/**
	 * 
	 * @param reg
	 *            a UML {@link State}
	 * @return
	 *         all {@link Transition} recursively
	 **/
	private static final Collection<Transition> getAllTransition(final State state) {
		final Collection<Transition> transitions = new HashSet<Transition>();
		final Iterator<Region> iter = state.getRegions().iterator();
		while (iter.hasNext()) {
			transitions.addAll(getAllTransition(iter.next()));
		}
		return transitions;
	}


}
