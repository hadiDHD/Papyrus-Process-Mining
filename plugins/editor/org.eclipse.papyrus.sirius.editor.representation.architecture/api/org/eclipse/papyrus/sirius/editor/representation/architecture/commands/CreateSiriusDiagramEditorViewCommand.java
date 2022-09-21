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
 *  Aurelien Didier (ARTAL) - aurelien.didier51@gmail.com - Initial API and implementation
 *****************************************************************************/

package org.eclipse.papyrus.sirius.editor.representation.architecture.commands;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.sirius.editor.representation.SiriusDiagramConstants;
import org.eclipse.papyrus.sirius.editor.representation.SiriusDiagramPrototype;
import org.eclipse.papyrus.sirius.editor.representation.architecture.Activator;
import org.eclipse.papyrus.sirius.editor.sirius.ISiriusSessionService;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.viewpoint.description.DAnnotation;
import org.eclipse.sirius.viewpoint.description.DescriptionFactory;

/**
 * Create a DSemanticDiagram Editor view
 */
public class CreateSiriusDiagramEditorViewCommand extends AbstractCreatePapyrusEditorViewCommand<DSemanticDiagram> {

	/**
	 * the {@link SiriusDiagramPrototype} used to create the {@link diagram} model and its editor view
	 */
	private final SiriusDiagramPrototype prototype;

	private DSemanticDiagram diagram;

	/**
	 *
	 * Constructor.
	 *
	 * @param domain
	 * @param diagramTemplatePrototype
	 * @param diagramName
	 * @param diagramMainTitle
	 * @param semanticContext
	 * @param graphicalContext
	 * @param openAfterCreation
	 */
	public CreateSiriusDiagramEditorViewCommand(final TransactionalEditingDomain domain, final SiriusDiagramPrototype diagramTemplatePrototype, final String diagramName, final String diagramMainTitle, final EObject semanticContext,
			final EObject graphicalContext, final boolean openAfterCreation, final String diagramId) {
		super(domain, "Create new Sirius Diagram", diagramName, semanticContext, graphicalContext, openAfterCreation, diagramId); //$NON-NLS-1$
		this.prototype = diagramTemplatePrototype;
	}

	/**
	 *
	 * Constructor.
	 *
	 * @param domain
	 * @param diagramTemplatePrototype
	 * @param diagramName
	 * @param diagramMainTitle
	 * @param semanticContext
	 * @param openAfterCreation
	 */
	public CreateSiriusDiagramEditorViewCommand(final TransactionalEditingDomain domain, final SiriusDiagramPrototype diagramTemplatePrototype, final String diagramName, final String diagramMainTitle, final EObject semanticContext,
			final boolean openAfterCreation, final String diagramId) {
		this(domain, diagramTemplatePrototype, diagramName, diagramMainTitle, semanticContext, null, openAfterCreation, diagramId);
	}

	/**
	 *
	 * @return
	 *         the {@link ISiriusSessionService} to use or <code>null</code> if not found
	 */
	private ISiriusSessionService getSiriusSessionService() {
		final ServicesRegistry servReg = getServiceRegistry(this.semanticContext);
		try {
			return (ISiriusSessionService) servReg.getService(ISiriusSessionService.SERVICE_ID);
		} catch (ServiceException e) {
			Activator.log.error(e);
		}
		return null;
	}


	/**
	 *
	 * @see org.eclipse.emf.transaction.RecordingCommand#doExecute()
	 *
	 */
	@Override
	protected void doExecute() {
		final ISiriusSessionService sessionService = getSiriusSessionService();
		if (sessionService == null || this.prototype == null) {
			return;
		}
		final DiagramDescription diagramDescription = sessionService.getSiriusDiagramDescriptionFromPapyrusPrototype(this.prototype, this.semanticContext);
		if (diagramDescription == null) {
			return;
		}
		final Session session = sessionService.getSiriusSession();

		if (DialectManager.INSTANCE.canCreate(this.semanticContext, diagramDescription)) {

			// required to be able to create the diagram
			sessionService.attachSession(this.semanticContext);

			// TODO : find a better way for that
			// this annotation is used to retrieve the ViewPrototype for a given diagram
			// TODO try to create a dialect manager
			// TODO : write that in the documentation
			this.diagram = (DSemanticDiagram) DialectManager.INSTANCE.createRepresentation(this.editorViewName, this.semanticContext, diagramDescription, session, new NullProgressMonitor());
			DAnnotation annotation = DescriptionFactory.eINSTANCE.createDAnnotation();
			annotation.setSource(SiriusDiagramConstants.PAPYRUS_SIRIUS_DIAGRAM_IMPLEMENTATION_DANNOTATION_SOURCE);
			annotation.getDetails().put(SiriusDiagramConstants.PAPYRUS_SIRIUS_DIAGRAM_IMPLEMENTATION_DANNOTATION_KEY, this.prototype.getId());

			this.diagram.getEAnnotations().add(annotation);

			attachToResource(semanticContext, diagram);


			if (this.openAfterCreation) {
				openEditor(diagram);
			}
			if (diagram.eResource() != null) {
				// we suppose all is ok
				this.createdEditorView = diagram;
			}
		}
	}
}
