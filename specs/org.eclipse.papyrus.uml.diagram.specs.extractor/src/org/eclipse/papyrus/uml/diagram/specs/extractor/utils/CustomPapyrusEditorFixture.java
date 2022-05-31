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
 *  Vincent Lorenzo (CEA LIST) <vincent.lorenzo@cea.fr> - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.uml.diagram.specs.extractor.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.Tool;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.infra.gmfdiag.common.service.palette.AspectUnspecifiedTypeConnectionTool.CreateAspectUnspecifiedTypeConnectionRequest;
import org.eclipse.papyrus.infra.gmfdiag.common.service.palette.AspectUnspecifiedTypeCreationTool;
import org.eclipse.papyrus.infra.gmfdiag.common.utils.DiagramEditPartsUtil;
import org.eclipse.papyrus.junit.utils.rules.PapyrusEditorFixture;
import org.junit.Assert;

/**
 * 
 * This class requires the patch https://git.eclipse.org/r/c/papyrus/org.eclipse.papyrus/+/194241
 *
 */
public class CustomPapyrusEditorFixture extends PapyrusEditorFixture {

	/**
	 * To suppress terminal exception on the super implementation
	 * 
	 * @see org.eclipse.papyrus.junit.utils.rules.PapyrusEditorFixture#getNewEditPart(org.eclipse.gef.EditPart, java.util.Collection)
	 *
	 * @param context
	 * @param viewDescriptors
	 * @return
	 */
	@Override
	protected IGraphicalEditPart getNewEditPart(EditPart context, Collection<? extends ViewDescriptor> viewDescriptors) {
		return viewDescriptors.stream()
				.map(desc -> desc.getAdapter(View.class)).map(View.class::cast)
				.filter(Objects::nonNull)
				.map(view -> DiagramEditPartsUtil.getEditPartFromView(view, context))
				.filter(IGraphicalEditPart.class::isInstance).map(IGraphicalEditPart.class::cast)
				.findAny().orElse(null);
	}

	/**
	 * To suppress terminal exception on the super implementation
	 * 
	 * @see org.eclipse.papyrus.junit.utils.rules.PapyrusEditorFixture#execute(org.eclipse.gef.commands.Command)
	 *
	 * @param command
	 */
	public void execute(org.eclipse.gef.commands.Command command) {
		// assertThat("No command", command, notNullValue());
		// assertThat(command, CommandMatchers.GEF.canExecute());
		if (command != null && command.canExecute()) {
			getActiveDiagramEditor().getDiagramEditDomain().getDiagramCommandStack().execute(command);
		}
		flushDisplayEvents();
	}

	/**
	 * To suppress terminal exception on the super implementation when the target==null
	 * 
	 * @see org.eclipse.papyrus.junit.utils.rules.PapyrusEditorFixture#execute(org.eclipse.gef.commands.Command)
	 *
	 * @param command
	 */
	@Override
	public IGraphicalEditPart createShape(EditPart parent, IElementType type, Point location, Dimension size) {
		CreateViewRequest request = CreateViewRequestFactory.getCreateShapeRequest(type, ((IGraphicalEditPart) parent).getDiagramPreferencesHint());

		request.setLocation(location);
		request.setSize(size);

		// Some edit-parts in some diagrams depend on the creation tool setting this
		CreateElementRequest semanticRequest = null;
		if (!request.getViewDescriptors().isEmpty()) {
			semanticRequest = (CreateElementRequest) ((CreateElementRequestAdapter) request.getViewDescriptors().get(0).getElementAdapter()).getAdapter(CreateElementRequest.class);
			if (semanticRequest != null) {
				semanticRequest.setParameter(AspectUnspecifiedTypeCreationTool.INITIAL_MOUSE_LOCATION_FOR_CREATION, location.getCopy());
			}
		}

		EditPart target = parent.getTargetEditPart(request);
		if (target == null) { // changed to avoid exception (see initial papyrus implementation
			return null;
		}
		org.eclipse.gef.commands.Command command = target.getCommand(request);
		execute(command);

		return getNewEditPart(parent, request.getViewDescriptors());
	}


	/**
	 * It takes too many times to really execute the edges creation, so we just check if the command can be executed or not
	 * 
	 * @param paletteToolId
	 * @param sourceEditPart
	 * @param targetEditPart
	 * @param sourceLocation
	 * @param targetLocation
	 * @return
	 * @throws Exception
	 */
	public boolean canCreateLink(String paletteToolId, EditPart sourceEditPart, EditPart targetEditPart, Point sourceLocation, Point targetLocation) throws Exception {
		Tool tool = getPaletteTool(paletteToolId);
		Assert.assertNotNull(NLS.bind("The requested tool ({0}) wasn't found in the current active diagram", paletteToolId), tool); //$NON-NLS-1$
		CreateAspectUnspecifiedTypeConnectionRequest request = (CreateAspectUnspecifiedTypeConnectionRequest) getAspectUnspecifiedCreateRequest(tool);

		request.setLocation(sourceLocation);
		request.setSourceEditPart(sourceEditPart);
		request.setType(RequestConstants.REQ_CONNECTION_START);
		org.eclipse.gef.commands.Command sourceCommand = sourceEditPart.getCommand(request); // Initialize the source
		if (sourceCommand == null || !sourceCommand.canExecute()) {
			return false;
		}

		request.setTargetEditPart(targetEditPart);
		request.setType(RequestConstants.REQ_CONNECTION_END);
		request.setLocation(targetLocation);
		//
		org.eclipse.gef.commands.Command completeCommand = targetEditPart.getCommand(request);
		if (completeCommand == null || !completeCommand.canExecute()) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param context
	 * @param request
	 * @return
	 */
	protected EditPart getNewEdgeEditPart(final DiagramEditPart context, final Collection<?> request) {
		for (final Object current : request) {
			if (current instanceof CreateConnectionViewAndElementRequest) {
				CreateConnectionViewAndElementRequest tmp = (CreateConnectionViewAndElementRequest) current;
				return getNewEditPart(context, Collections.singleton(tmp.getConnectionViewAndElementDescriptor()));
			}
		}
		return null;
	}
}
