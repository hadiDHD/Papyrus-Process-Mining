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
 *    Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and others
 *****************************************************************************/
package org.eclipse.papyrus.uml.sirius.sequence.diagram.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.sirius.diagram.AbstractDNode;
import org.eclipse.sirius.diagram.DNode;
import org.eclipse.sirius.diagram.business.api.query.EObjectQuery;
import org.eclipse.sirius.diagram.description.tool.ReconnectionKind;
import org.eclipse.sirius.diagram.ui.provider.Messages;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * The Class SiriusEdgeReconnectionHelper.
 *
 * @author Yann Binot (Artal Technologies) <yann.binot@artal.fr>
 */
public class SiriusEdgeReconnectionHelper {

	/** List of incoming/outgoing edges on the targeted edge before reconnection. */
	private List<Edge> reconnectionTargetEdges;

	/** Reconnection target. */
	private View reconnectionTarget;

	/**
	 * Reuse ReconnectionKind to save if reconnecting the source or the target
	 * of the edge.
	 */
	private ReconnectionKind reconnectionKind;

	/**
	 * Default constructor of this query.
	 * 
	 * @param reconnectionTarget
	 *            Reconnection target
	 * @param reconnectionTargetEdges
	 *            List of incoming/outgoing edges on the targeted edge before
	 *            reconnection
	 * @param reconnectionKind
	 *            Reuse ReconnectionKind to save if reconnecting the source or
	 *            the target of the edge.
	 */
	public SiriusEdgeReconnectionHelper(View reconnectionTarget, List<Edge> reconnectionTargetEdges, ReconnectionKind reconnectionKind) {
		this.reconnectionTarget = reconnectionTarget;
		this.reconnectionTargetEdges = new ArrayList<Edge>(reconnectionTargetEdges);
		assert reconnectionKind == ReconnectionKind.RECONNECT_SOURCE_LITERAL || reconnectionKind == ReconnectionKind.RECONNECT_TARGET_LITERAL : Messages.EdgeReconnectionHelper_invalidReconnectionKind;
		this.reconnectionKind = reconnectionKind;
	}


	/**
	 * Gets the reconnected edge.
	 *
	 * @return the reconnected edge
	 */
	public Edge getReconnectedEdge() {
		Edge edge = null;
		if (reconnectionKind == ReconnectionKind.RECONNECT_SOURCE_LITERAL) {
			final List<Edge> sourceEdges = new ArrayList<Edge>(reconnectionTarget.getSourceEdges());
			Iterables.removeAll(sourceEdges, reconnectionTargetEdges);
			Predicate<Edge> notToReconnectingEdge = new Predicate<Edge>() {

				@Override
				public boolean apply(Edge input) {
					return !sourceEdges.contains(input.getTarget());
				}
			};
			// For case with reconnect edge source from borderedNode to
			// borderedNode
			// when borderedNode is mapping with EReference
			if (sourceEdges.isEmpty() && reconnectionTarget.getElement() instanceof AbstractDNode) {
				AbstractDNode abstractDNode = (AbstractDNode) reconnectionTarget.getElement();
				List<DNode> borderedNodes = abstractDNode.getOwnedBorderedNodes();
				if (!borderedNodes.isEmpty()) {
					DNode borderedNode = borderedNodes.get(borderedNodes.size() - 1);
					Collection<EObject> inverseReferences = new EObjectQuery(borderedNode).getInverseReferences(NotationPackage.eINSTANCE.getView_Element());
					Node nodeSource = Iterables.getOnlyElement(Iterables.filter(inverseReferences, Node.class));
					List<Edge> sourceEdgesOfBorderedNode = new ArrayList<Edge>(nodeSource.getSourceEdges());
					edge = Iterables.getOnlyElement(Iterables.filter(sourceEdgesOfBorderedNode, notToReconnectingEdge));
				}
			} else {
				if (reconnectionTarget.getElement() != null) {
					edge = Iterables.getOnlyElement(Iterables.filter(sourceEdges, notToReconnectingEdge));
				}
			}
		} else {
			final List<Edge> targetEdges = new ArrayList<Edge>(reconnectionTarget.getTargetEdges());
			Iterables.removeAll(targetEdges, reconnectionTargetEdges);
			Predicate<Edge> notFromReconnectingEdge = new Predicate<Edge>() {

				@Override
				public boolean apply(Edge input) {
					return !targetEdges.contains(input.getSource());
				}
			};
			// For case with reconnect edge target from borderedNode to
			// borderedNode
			// when borderedNode is mapping with EReference
			if (targetEdges.isEmpty() && reconnectionTarget.getElement() instanceof AbstractDNode) {
				AbstractDNode abstractDNode = (AbstractDNode) reconnectionTarget.getElement();
				List<DNode> borderedNodes = abstractDNode.getOwnedBorderedNodes();
				if (!borderedNodes.isEmpty()) {
					DNode borderedNode = borderedNodes.get(borderedNodes.size() - 1);
					Collection<EObject> inverseReferences = new EObjectQuery(borderedNode).getInverseReferences(NotationPackage.eINSTANCE.getView_Element());
					Node nodeTarget = Iterables.getOnlyElement(Iterables.filter(inverseReferences, Node.class));
					List<Edge> targetEdgesOfBorderedNode = new ArrayList<Edge>(nodeTarget.getTargetEdges());
					edge = Iterables.getOnlyElement(Iterables.filter(targetEdgesOfBorderedNode, notFromReconnectingEdge));
				}
			} else {
				if (reconnectionTarget.getElement() != null) {
					edge = Iterables.getOnlyElement(Iterables.filter(targetEdges, notFromReconnectingEdge));
				}
			}
		}
		return edge;
	}

	/**
	 * Checks if is reconnecting source.
	 *
	 * @return true, if is reconnecting source
	 */
	public boolean isReconnectingSource() {
		return reconnectionKind.equals(ReconnectionKind.RECONNECT_SOURCE_LITERAL);
	}

	/**
	 * Checks if is reconnecting target.
	 *
	 * @return true, if is reconnecting target
	 */
	public boolean isReconnectingTarget() {
		return reconnectionKind.equals(ReconnectionKind.RECONNECT_TARGET_LITERAL);
	}
}
