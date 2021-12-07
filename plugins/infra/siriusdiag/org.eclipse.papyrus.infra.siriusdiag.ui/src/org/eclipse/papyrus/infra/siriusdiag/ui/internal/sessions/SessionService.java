/*****************************************************************************
 * Copyright (c) 2021 CEA LIST and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Vincent Lorenzo (CEA LIST) <vincent.lorenzo@cea.fr> - Initial API and implementation
 *
 *****************************************************************************/
package org.eclipse.papyrus.infra.siriusdiag.ui.internal.sessions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.infra.architecture.ArchitectureDescriptionUtils;
import org.eclipse.papyrus.infra.architecture.listeners.ArchitectureDescriptionAdapterUtils;
import org.eclipse.papyrus.infra.core.architecture.RepresentationKind;
import org.eclipse.papyrus.infra.core.architecture.merged.MergedArchitectureViewpoint;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.infra.core.utils.ServiceUtils;
import org.eclipse.papyrus.infra.emf.gmf.util.GMFUnsafe;
import org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype;
import org.eclipse.papyrus.infra.siriusdiag.sirius.ISiriusSessionService;
import org.eclipse.papyrus.infra.siriusdiag.ui.Activator;
import org.eclipse.papyrus.infra.siriusdiag.ui.internal.listeners.SiriusArchitectureDescriptionAdapter;
import org.eclipse.papyrus.infra.siriusdiag.ui.modelresource.SiriusDiagramModel;
import org.eclipse.sirius.business.api.dialect.DialectManager;
import org.eclipse.sirius.business.api.query.FileQuery;
import org.eclipse.sirius.business.api.session.DefaultLocalSessionCreationOperation;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.ui.business.api.viewpoint.ViewpointSelectionCallback;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;
import org.eclipse.sirius.viewpoint.description.Viewpoint;

/**
 * This service is in charge to manage all stuff about Sirius Session
 */
public class SessionService implements ISiriusSessionService, ISiriusSessionViewpointUpdater {

	/**
	 * The service registry associated to this current SessionService
	 */
	private ServicesRegistry servicesRegistry;

	/**
	 * the current ModelSet
	 */
	private ModelSet modelSet;

	/**
	 * the current editing domain;
	 */
	private TransactionalEditingDomain editingDomain;

	/**
	 * The created session
	 */
	private Session createdSession = null;

	private SiriusArchitectureDescriptionAdapter architectureListener;

	public SessionService() {
		// nothing to do
	}

	/**
	 *
	 * @see org.eclipse.papyrus.infra.core.services.IService#init(org.eclipse.papyrus.infra.core.services.ServicesRegistry)
	 *
	 * @param servicesRegistry
	 *            the service registry associated to the current model
	 * @throws ServiceException
	 */
	@Override
	public void init(final ServicesRegistry servicesRegistry) throws ServiceException {
		this.servicesRegistry = servicesRegistry;
		if (this.servicesRegistry == null) {
			throw new ServiceException(NLS.bind("The service {0} can't be initialized because the ServicesRegistry is not found", ISiriusSessionService.SERVICE_ID));
		}
	}

	/**
	 *
	 * @see org.eclipse.papyrus.infra.core.services.IService#startService()
	 *
	 * @throws ServiceException
	 */
	@Override
	public void startService() throws ServiceException {
		this.modelSet = getModelSet();
		this.editingDomain = getEditingDomain();
		if (this.modelSet == null || this.editingDomain == null) {
			throw new ServiceException(NLS.bind("The service {0} can't start.", ISiriusSessionService.SERVICE_ID));
		}
		this.architectureListener = new SiriusArchitectureDescriptionAdapter(this);
		ArchitectureDescriptionAdapterUtils.registerListener(this.modelSet, this.architectureListener);
	}

	/**
	 *
	 * @see org.eclipse.papyrus.infra.core.services.IService#disposeService()
	 *
	 * @throws ServiceException
	 */
	@Override
	public void disposeService() throws ServiceException {
		if (this.architectureListener != null) {
			ArchitectureDescriptionAdapterUtils.unregisterListener(this.modelSet, this.architectureListener);
		}
		this.servicesRegistry = null;
		this.editingDomain = null;
		this.modelSet = null;
		this.createdSession = null;
	}

	/**
	 *
	 * @see org.eclipse.papyrus.infra.siriusdiag.sirius.ISiriusSessionService#getSiriusSession()
	 *
	 * @return
	 *         the Sirius Session associated to the {@link ServicesRegistry}. This method creates it if is doesn't yet exists
	 */
	@Override
	public Session getSiriusSession() {
		if (this.createdSession == null) {
			createdSession();
		}
		return this.createdSession;
	}

	/**
	 * This method creates the Sirius session
	 */
	private void createdSession() {
		if (this.servicesRegistry == null || this.modelSet == null || this.editingDomain == null) {
			return;
		}

		// 1. create the Sirius file resource URI
		URI siriusFileResource = modelSet.getURIWithoutExtension();
		siriusFileResource = siriusFileResource.appendFileExtension(SiriusConstants.SIRIUS_DIAGRAM_MODEL_FILE_EXTENSION);

		// 2. save the notation file
		saveNotationFile(modelSet);

		// 3. create the session
		this.createdSession = createSiriusSession(siriusFileResource);


		SiriusDiagramModel siriusModel = (SiriusDiagramModel) modelSet.getModel(SiriusDiagramModel.SIRIUS_DIAGRAM_MODEL_ID);
		Assert.isNotNull(siriusModel, NLS.bind("We can't find the '{0}' class.", SiriusDiagramModel.class.getName()));
		siriusModel.setSiriusSession(this.createdSession);



		// useless or not ?*
		/* Papyrus */SessionManager.INSTANCE.add(createdSession);
		/* Papyrus */SessionManager.INSTANCE.openSession(siriusFileResource, new NullProgressMonitor(), null);// check parameters

		URI umlURI = modelSet.getURIWithoutExtension().appendFileExtension("uml");//// $NON-NLS-1 //TODO uml as string is not very nice inside an infra plugin
		this.createdSession.addSemanticResource(umlURI, new NullProgressMonitor());
		this.createdSession.save(new NullProgressMonitor());

		// 4. update applied sirius viewpoints
		updateAppliedSiriusViewpoints();

		// VL : not sure if we need to save before or after the sirius viewpoint application.
		//initially we applied the sirius viewpoint AFTER the save
		// this.createdSession.save(new NullProgressMonitor());


	}

	/**
	 *
	 * @return
	 *         the editing domain or <code>null</code> if not found
	 */
	private final TransactionalEditingDomain getEditingDomain() {
		try {
			return ServiceUtils.getInstance().getTransactionalEditingDomain(this.servicesRegistry);
		} catch (ServiceException e) {
			Activator.log.error("EditingDomain not found", e); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 *
	 * @return
	 *         the {@link ModelSet} or <code>null</code> if not found
	 */
	private final ModelSet getModelSet() {
		try {
			return this.servicesRegistry.getService(ModelSet.class);
		} catch (ServiceException e) {
			Activator.log.error("ModelSet not found", e); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * This method calls ModelSet.save() to save the notation file if it doesn't yet exist
	 *
	 * @param modelSet
	 *            the current modelSet
	 */
	private final void saveNotationFile(final ModelSet modelSet) {
		// this action is required to avoid a crash when we create a new sirius diagram from the Papyrus "create model wizard"
		// TODO to avoid this bug, we should propose a patch to Sirius to check the file exists before trying to load it? (or a try catch)
		// bug is in org.eclipse.sirius.business.api.helper.SiriusResourceHelper.getCorrespondingViewpoint(Session, Viewpoint), line 136
		// so at this line : editingDomainResource.load(Collections.EMPTY_MAP);
		URI notationURi = modelSet.getURIWithoutExtension();
		notationURi = notationURi.appendFileExtension("notation");////$NON-NLS-1$
		boolean exists = modelSet.getURIConverter().exists(notationURi, null);
		if (!exists) {
			try {
				modelSet.save(new NullProgressMonitor());
			} catch (IOException e) {
				Activator.log.error("ModelSet can't be saved", e); //$NON-NLS-1$
			}
		}
	}

	/**
	 *
	 * @param siriusResourceURI
	 *            the resource uri to use to create the Session
	 * @return
	 *         the Session or <code>null</code>
	 */
	private Session createSiriusSession(final URI siriusResourceURI) {
		final DefaultLocalSessionCreationOperation operation = new PapyrusLocalSessionCreationOperation(siriusResourceURI, new NullProgressMonitor(), this.editingDomain);
		try {
			operation.execute();
			return operation.getCreatedSession();
		} catch (CoreException e) {
			Activator.log.error(NLS.bind("The resource {0} can't be created", siriusResourceURI), e); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 *
	 * @return
	 *         the collection of Sirius {@link Viewpoint} referenced in the current Papyrus architecture framework
	 */
	private final Collection<Viewpoint> collectSiriusViewpointInPapyrusArchitecture() {
		final ArchitectureDescriptionUtils utils = new ArchitectureDescriptionUtils(this.modelSet);
		final Collection<MergedArchitectureViewpoint> vp = utils.getArchitectureContext().getViewpoints();
		final Collection<Viewpoint> siriusViewpoints = new HashSet<>();
		for (MergedArchitectureViewpoint tmp : vp) {
			for (RepresentationKind current : tmp.getRepresentationKinds()) {
				if (current instanceof SiriusDiagramPrototype) {
					final DiagramDescription desc = ((SiriusDiagramPrototype) current).getDiagramDescription();
					final EObject currentV = desc.eContainer();
					if (currentV instanceof Viewpoint) {
						siriusViewpoints.add((Viewpoint) currentV);
					}
				}
			}
		}
		return siriusViewpoints;
	}

	/**
	 * @see org.eclipse.papyrus.infra.siriusdiag.sirius.ISiriusSessionViewpointUpdater#updateAppliedViewpoint()
	 *
	 */
	@Override
	public void updateAppliedSiriusViewpoints() {
		if (getEditingDomain() == null) {
			// we are disposing the service
			return;
		}
		try {
			// required to allow to open a sirius Diagram from the ModelExplorer on double click as first action!
			// if not we get this kind of exception : java.lang.IllegalStateException: Cannot modify resource set without a write transaction
			GMFUnsafe.write(getEditingDomain(), new UpdateSiriusViewpointRunnable(this.createdSession));
		} catch (InterruptedException | RollbackException e) {
			Activator.log.error(e);
		}

	}

	private class UpdateSiriusViewpointRunnable implements Runnable {

		private Session session;

		public UpdateSiriusViewpointRunnable(final Session session) {
			this.session = session;
		}

		/**
		 * @see java.lang.Runnable#run()
		 *
		 */
		@Override
		public void run() {
			final Collection<Viewpoint> toApply = collectSiriusViewpointInPapyrusArchitecture();
			final Collection<Viewpoint> currentAppliedViewpoints = this.session.getSelectedViewpoints(false);

			final Collection<Viewpoint> toUnapply = new ArrayList<>(currentAppliedViewpoints);
			toUnapply.removeAll(toApply);
			toApply.removeAll(currentAppliedViewpoints);

			final ViewpointSelectionCallback callBack = new ViewpointSelectionCallback();

			for (final Viewpoint previouslySelected : toUnapply) {
				callBack.deselectViewpoint(previouslySelected, this.session, new NullProgressMonitor());
			}

			for (Viewpoint current : toApply) {
				callBack.selectViewpoint(current, this.session, new NullProgressMonitor());
			}
		}
	}


	/**
	 *
	 * @see org.eclipse.papyrus.infra.siriusdiag.sirius.ISiriusSessionService#getSiriusDiagramDescriptionFromPapyrusPrototype(org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype, org.eclipse.emf.ecore.EObject)
	 *
	 * @param siriusDiagramPrototype
	 * @param context
	 * @return
	 *         the DiagramDescription corresponding to the {@link SiriusDiagramPrototype} and registered in the current session, or <code>null</code> if not found
	 */
	@Override
	public DiagramDescription getSiriusDiagramDescriptionFromPapyrusPrototype(final SiriusDiagramPrototype siriusDiagramPrototype, final EObject context) {
		if (getSiriusSession() == null || siriusDiagramPrototype == null || siriusDiagramPrototype.getDiagramDescription() == null) {
			return null;
		}
		final URI descriptionURI = siriusDiagramPrototype.getDiagramDescription().eResource().getURI();
		if (descriptionURI == null) {
			return null;
		}
		Collection<RepresentationDescription> desc = DialectManager.INSTANCE.getAvailableRepresentationDescriptions(this.createdSession.getSelectedViewpoints(false), context);
		if (desc.isEmpty()) { // TODO required?
			updateAppliedSiriusViewpoints();
		}
		desc = DialectManager.INSTANCE.getAvailableRepresentationDescriptions(this.createdSession.getSelectedViewpoints(false), context);
		final java.util.Iterator<RepresentationDescription> iter = desc.iterator();
		while (iter.hasNext()) {
			final RepresentationDescription current = iter.next();
			if (current instanceof DiagramDescription && current.eResource() != null) {
				final URI uri = current.eResource().getURI();
				if (descriptionURI.equals(uri)) {
					return (DiagramDescription) current;
				}
			}
		}

		return null;
	}

}
