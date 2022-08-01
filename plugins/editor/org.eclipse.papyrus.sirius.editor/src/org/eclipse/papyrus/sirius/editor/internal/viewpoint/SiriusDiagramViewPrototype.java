/******************************************************************************
 * Copyright (c) 2021, 2022 CEA LIST, Artal Technologies
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *  Vincent Lorenzo (CEA LIST) - vincent.lorenzo@cea.fr - Bug 579701
 *****************************************************************************/

package org.eclipse.papyrus.sirius.editor.internal.viewpoint;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.infra.emf.utils.ServiceUtilsForEObject;
import org.eclipse.papyrus.sirius.editor.representation.ICreateSiriusDiagramEditorCommand;
import org.eclipse.papyrus.sirius.editor.representation.SiriusDiagramPrototype;
import org.eclipse.papyrus.sirius.editor.Activator;
import org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.description.DiagramDescription;

/**
 * Represents a prototype of Sirius Diagram View for the viewpoints infrastructure.
 */
public class SiriusDiagramViewPrototype extends ViewPrototype implements ExtendedViewPrototype<DSemanticDiagram> {

	private final ICreateSiriusDiagramEditorCommand command;

	/**
	 * Constructor.
	 *
	 * @param prototype
	 *            The PapyrusDocument representation
	 */
	public SiriusDiagramViewPrototype(final SiriusDiagramPrototype prototype, final ICreateSiriusDiagramEditorCommand command) {
		super(prototype);
		this.command = command;
	}

	/**
	 * 
	 * @see org.eclipse.papyrus.sirius.editor.internal.viewpoint.ExtendedViewPrototype#canInstantianteOn(org.eclipse.emf.ecore.EObject)
	 *
	 * @param selection
	 *            the selected element to used as semantic context to create a new diagram
	 * @return
	 *         <code>true</code> if the Sirius VSM allows the creation
	 */
	@Override
	public boolean canInstantianteOn(final EObject selection) {
		// here we call the Sirius API to check the condition of creation
		final DiagramDescription diagramDescription = getRepresentationKind().getDiagramDescription();
		return (DialectManager.INSTANCE.canCreate(selection, diagramDescription, false));
	}

	/**
	 * @see org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype#isOwnerReassignable()
	 *
	 * @return
	 */
	@Override
	public boolean isOwnerReassignable() {
		// Users can always move diagram that are part of their viewpoint
		return true;
	}

	/**
	 * @see org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype#instantiateOn(org.eclipse.emf.ecore.EObject, java.lang.String, boolean)
	 *
	 * @param owner
	 * @param name
	 * @param openCreatedView
	 * @return
	 */
	@Override
	public boolean instantiateOn(EObject owner, String name, boolean openCreatedView) {
		return instantiateOn(owner, owner, name, openCreatedView) != null;
	}

	/**
	 * @see org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype#instantiateOn(org.eclipse.emf.ecore.EObject)
	 *
	 * @param owner
	 * @return
	 */
	@Override
	public boolean instantiateOn(EObject owner) {
		return instantiateOn(owner, null);
	}

	/**
	 * @see org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype#instantiateOn(org.eclipse.emf.ecore.EObject, java.lang.String)
	 *
	 * @param owner
	 * @param name
	 * @return
	 */
	@Override
	public boolean instantiateOn(EObject owner, String name) {
		if (this.command != null) {
			// TODO VL useless or not ?
			ServicesRegistry registry;
			try {
				registry = ServiceUtilsForEObject.getInstance().getServiceRegistry(owner);
			} catch (ServiceException ex) {
				Activator.log.error(ex);
				return false;
			}
			ModelSet modelSet;
			try {
				modelSet = registry.getService(ModelSet.class);
			} catch (ServiceException ex) {
				Activator.log.error(ex);
				return false;
			}
		}
		return instantiateOn(owner, name, true);
	}

	/**
	 * @see org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype#getOwnerOf(org.eclipse.emf.ecore.EObject)
	 *
	 * @param view
	 * @return
	 */
	@Override
	public EObject getOwnerOf(EObject view) {
		// it is graphical context
		return ((DSemanticDiagram) view).eContainer();
	}

	/**
	 * @see org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype#getRootOf(org.eclipse.emf.ecore.EObject)
	 *
	 * @param view
	 * @return
	 */
	@Override
	public EObject getRootOf(EObject view) {
		// it is semantic context
		return ((DSemanticDiagram) view).getTarget();
	}

	/**
	 * @see org.eclipse.papyrus.sirius.editor.internal.viewpoint.ExtendedViewPrototype#instantiateOn(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject, java.lang.String, boolean)
	 *
	 * @param semanticOwner
	 * @param graphicalOwner
	 * @param name
	 * @param openCreatedView
	 * @return
	 */
	@Override
	public DSemanticDiagram instantiateOn(final EObject semanticOwner, final EObject graphicalOwner, final String name, final boolean openCreatedView) {
		return command.execute(this, name, semanticOwner, graphicalOwner, openCreatedView);
	}



	@Override
	public Command getCommandChangeRoot(EObject view, final EObject target) {
		final DSemanticDiagram diagram = (DSemanticDiagram) view;
		final EObject previous = diagram.getTarget();
		return new AbstractCommand("Change diagram root element") { //$NON-NLS-1$
			@Override
			public void execute() {
				diagram.setTarget(target);
			}

			@Override
			public void undo() {
				diagram.setTarget(previous);
			}

			@Override
			public void redo() {
				diagram.setTarget(target);
			}

			@Override
			protected boolean prepare() {
				return true;
			}
		};
	}

	/**
	 * @since 3.0
	 */
	@Override
	public SiriusDiagramPrototype getRepresentationKind() {
		return (SiriusDiagramPrototype) representationKind;
	}

	/**
	 * @see org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype#getCommandChangeOwner(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
	 *
	 * @param view
	 * @param target
	 * @return
	 */
	@Override
	public Command getCommandChangeOwner(EObject view, final EObject target) {
		final DSemanticDiagram diagram = (DSemanticDiagram) view;
		final EObject previous = diagram.getTarget();
		return new AbstractCommand("Change diagram owner") { //$NON-NLS-1$
			@Override
			public void execute() {
				// DiagramUtils.setOwner(diagram, target);
			}

			@Override
			public void undo() {
				// DiagramUtils.setOwner(diagram, previous);
			}

			@Override
			public void redo() {
				// DiagramUtils.setOwner(diagram, target);
			}

			@Override
			protected boolean prepare() {
				return true;
			}
		};
	}

}
