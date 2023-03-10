/******************************************************************************
 * Copyright (c) 2014, 2015, 2021 THALES GLOBAL SERVICES, CEA LIST, Artal Technologies.
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
package org.eclipse.papyrus.sirius.uml.diagram.sequence.provider;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint;
import org.eclipse.sirius.diagram.description.tool.ReconnectionKind;
import org.eclipse.sirius.diagram.ui.graphical.edit.policies.SetConnectionBendpointsAndLabelCommmand;
import org.eclipse.sirius.diagram.ui.internal.operation.CenterEdgeEndModelChangeOperation;
import org.eclipse.sirius.diagram.ui.internal.refresh.GMFHelper;

/**
 * This {@link SetConnectionBendpointsAndLabelCommmand} overrides the execution
 * in order to set the {@link RelativeBendpoint} of the edge resulting of the
 * reconnection.
 * 
 * 
 */
@SuppressWarnings("restriction")
public class SetReconnectingConnectionBendpointsCommand extends SetConnectionBendpointsAndLabelCommmand {

	/**
	 * Helper used to access the edge after reconnection.
	 */
	private SiriusEdgeReconnectionHelper reconnectingEdgeHelper;

	/**
	 * Default constructor to set {@link RelativeBendpoint} on the reconnected
	 * edge.
	 * 
	 * @param editingDomain
	 *            current {@link TransactionalEditingDomain}
	 * @param reconnectionTarget
	 *            Reconnection target
	 * @param reconnectionTargetEdges
	 *            List of incoming/outgoing edges on the targeted edge before
	 *            reconnection
	 * @param reconnectionKind
	 *            Reuse ReconnectionKind to save if reconnecting the source or
	 *            the target of the edge.
	 */
	public SetReconnectingConnectionBendpointsCommand(TransactionalEditingDomain editingDomain, View reconnectionTarget, List<Edge> reconnectionTargetEdges, ReconnectionKind reconnectionKind) {
		super(editingDomain);
		this.reconnectingEdgeHelper = new SiriusEdgeReconnectionHelper(reconnectionTarget, reconnectionTargetEdges, reconnectionKind);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This method has been overridden in order to compute the sourceRefPoint
	 * using the reconnected edge instead of the previous, under reconnection,
	 * edge.
	 */
	@Override
	public Point getSourceRefPoint() {
		if (reconnectingEdgeHelper.isReconnectingSource()) {
			Edge edge = reconnectingEdgeHelper.getReconnectedEdge();
			if (edge != null) {
				Connection connection = (Connection) GMFHelper.getGraphicalEditPart(edge).get().getFigure();

				Point sourceRefPoint = connection.getSourceAnchor().getReferencePoint();
				connection.translateToRelative(sourceRefPoint);
				return sourceRefPoint;
			}
		}
		return super.getSourceRefPoint();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This method has been overridden in order to compute the targetRefPoint
	 * using the reconnected edge instead of the previous, under reconnection,
	 * edge.
	 */
	@Override
	public Point getTargetRefPoint() {
		if (reconnectingEdgeHelper.isReconnectingTarget()) {
			Edge edge = reconnectingEdgeHelper.getReconnectedEdge();
			if (edge != null) {
				Connection connection = (Connection) GMFHelper.getGraphicalEditPart(edge).get().getFigure();

				Point targetRefPoint = connection.getTargetAnchor().getReferencePoint();
				connection.translateToRelative(targetRefPoint);
				return targetRefPoint;
			}
		}
		return super.getTargetRefPoint();
	}

	/**
	 * Do execute with result.
	 *
	 * @param progressMonitor the progress monitor
	 * @param info the info
	 * @return the command result
	 * @throws ExecutionException the execution exception
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.commands.SetConnectionBendpointsCommand#doExecuteWithResult(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.runtime.IAdaptable)
	 */
	
	@Override
	protected CommandResult doExecuteWithResult(IProgressMonitor progressMonitor, IAdaptable info) throws ExecutionException {
		Assert.isNotNull(getNewPointList());
		Point sourceRefPoint = getSourceRefPoint();
		Assert.isNotNull(sourceRefPoint);
		Point targetRefPoint = getTargetRefPoint();
		Assert.isNotNull(targetRefPoint);

		// The edge recovery is the difference with the parent command
		Edge edge = reconnectingEdgeHelper.getReconnectedEdge();

		// if the reconnected edge do not exist, there is nothing to do here.
		if (edge != null) {
			List newBendpoints = new ArrayList();
			int numOfPoints = getNewPointList().size();
			for (short i = 0; i < numOfPoints; i++) {
				// The sourceRefPoint and targetRefPoint will be recovered from
				// the
				// reconnected edge
				Dimension s = getNewPointList().getPoint(i).getDifference(sourceRefPoint);
				Dimension t = getNewPointList().getPoint(i).getDifference(targetRefPoint);
				newBendpoints.add(new RelativeBendpoint(s.width, s.height, t.width, t.height));
			}

			RelativeBendpoints points = (RelativeBendpoints) edge.getBendpoints();
			points.setPoints(newBendpoints);

			CenterEdgeEndModelChangeOperation centerEdgeEndModelChangeOperation = new CenterEdgeEndModelChangeOperation(edge, false);
			centerEdgeEndModelChangeOperation.execute();
		}
		return CommandResult.newOKCommandResult();
	}
}
