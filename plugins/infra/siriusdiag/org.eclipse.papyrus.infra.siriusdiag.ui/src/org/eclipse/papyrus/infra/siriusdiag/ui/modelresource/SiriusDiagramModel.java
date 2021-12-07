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

package org.eclipse.papyrus.infra.siriusdiag.ui.modelresource;

import java.io.IOException;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.infra.core.resource.AbstractDynamicModel;
import org.eclipse.papyrus.infra.core.resource.BadArgumentExcetion;
import org.eclipse.papyrus.infra.core.resource.NotFoundException;
import org.eclipse.papyrus.infra.siriusdiag.ui.internal.sessions.PapyrusSession;
import org.eclipse.papyrus.infra.siriusdiag.ui.internal.sessions.SiriusConstants;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.ui.business.api.session.SessionUIManager;

/**
 * This class manages PapyrusDocument in aird model resource.
 */
public class SiriusDiagramModel extends AbstractDynamicModel<DSemanticDiagram> {

	private Session siriusSession = null;

	/**
	 * Document Model ID.
	 */
	public static final String SIRIUS_DIAGRAM_MODEL_ID = "org.eclipse.papyrus.infra.siriusdiag.ui.DSemanticDiagram"; //$NON-NLS-1$

	/**
	 * the file extension where document are stored.
	 */
	private static final String SIRIUS_DIAGRAM_MODEL_FILE_EXTENSION = SiriusConstants.SIRIUS_DIAGRAM_MODEL_FILE_EXTENSION;

	/**
	 *
	 * Constructor.
	 *
	 */
	public SiriusDiagramModel() {
		super();
	}

	/**
	 *
	 * @param session
	 *            the Sirius Session
	 */
	public void setSiriusSession(Session session) {
		this.siriusSession = session;
	}

	/**
	 * @see org.eclipse.papyrus.infra.core.resource.AbstractBaseModel#getModelFileExtension()
	 *
	 * @return
	 */
	@Override
	protected String getModelFileExtension() {
		return SIRIUS_DIAGRAM_MODEL_FILE_EXTENSION;
	}

	/**
	 * @see org.eclipse.papyrus.infra.core.resource.AbstractModel#getIdentifier()
	 *
	 * @return
	 */
	@Override
	public String getIdentifier() {
		return SIRIUS_DIAGRAM_MODEL_ID;
	}

	/**
	 * @see org.eclipse.papyrus.infra.core.resource.AbstractBaseModel#loadModel(org.eclipse.emf.common.util.URI)
	 *
	 * @param uriWithoutExtension
	 */
	@Override
	public void loadModel(URI uriWithoutExtension) {
		// It is a common use case that this resource does not (and will not)
		// exist
		if (exists(uriWithoutExtension)) {
			try {
				super.loadModel(uriWithoutExtension);
			} catch (Exception ex) {
				createModel(uriWithoutExtension);
			}
		}

		if (resource == null) {
			createModel(uriWithoutExtension);
		}
	}


	/**
	 * Add a new initialized document to the aird model.
	 *
	 * @param siriusDiagram
	 * @param context
	 *            we need the context to be able to calculate the resource name were the DSemanticDiagram will be saved.
	 *            because this value is maybe not yet set to {@link DSemanticDiagram#setSemanticContext(EObject)}
	 */
	public void addDiagram(final DSemanticDiagram siriusDiagram, final EObject context) {
		if (context != null) { // we check the resource for control mode feature
			if (siriusSession != null) {
				final Resource sessionRes = siriusSession.getSessionResource();
				if (!sessionRes.getContents().contains(siriusDiagram)) {
					sessionRes.getContents().add(siriusDiagram);
				}
				SessionUIManager.INSTANCE.getOrCreateUISession(this.siriusSession);// TODO fait dans SessionEditroInput.openSession
			}
		}
	}

	/**
	 *
	 * @see org.eclipse.papyrus.infra.core.resource.IEMFModel#canPersist(org.eclipse.emf.ecore.EObject)
	 *
	 * @param object
	 * @return
	 */
	@Override
	public boolean canPersist(EObject object) {
		return (getResource() != null) && isSupportedRoot(object);
	}

	/**
	 *
	 * @param object
	 * @return
	 */
	protected boolean isSupportedRoot(EObject object) {
		return object instanceof DSemanticDiagram;
	}

	/**
	 *
	 * @see org.eclipse.papyrus.infra.core.resource.IEMFModel#persist(org.eclipse.emf.ecore.EObject)
	 *
	 * @param object
	 */
	@Override
	public void persist(EObject object) {
		if (!canPersist(object)) {
			throw new IllegalArgumentException("cannot persist " + object); //$NON-NLS-1$
		}

		getResource().getContents().add(object);
	}

	/**
	 * @see org.eclipse.papyrus.infra.core.resource.AbstractBaseModel#configureResource(org.eclipse.emf.ecore.resource.Resource)
	 *
	 * @param resource
	 */
	@Override
	protected void configureResource(Resource resource) {
		super.configureResource(resource);
	}

	/**
	 * Get a diagram by its name.
	 *
	 * @param diagramName
	 *            Name of the diagram. This is the name set by the user.
	 * @return
	 * @throws NotFoundException
	 * @throws BadArgumentExcetion
	 */
	public DSemanticDiagram getDiagram(final String diagramName) throws NotFoundException, BadArgumentExcetion {
		if (diagramName == null || diagramName.length() == 0) {
			throw new BadArgumentExcetion("Diagram name should not be null and size should be >0."); //$NON-NLS-1$
		}
		for (Resource current : modelSet.getResources()) {
			for (EObject element : current.getContents()) {
				if (element instanceof DSemanticDiagram) {
					DSemanticDiagram diagram = (DSemanticDiagram) element;

					if (diagramName.equals(diagram.getName())) {
						// Found
						return diagram;

					}
				}
			}
		}
		// not found
		throw new NotFoundException(NLS.bind("No Diagram named '{0}' can be found in Model.", diagramName)); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.papyrus.infra.core.resource.AbstractBaseModel#unload()
	 *
	 */
	@Override
	public void unload() {
		// TODO : the way used to initialized the sirius session here is not satisfying.
		// if we create open a Papyrus model with a Sirius diagram, but without creating new one, the field won't be initialized,
		// so the sirius session won't be properly closed
		if (this.siriusSession != null) {
			this.siriusSession.close(new NullProgressMonitor());
			this.siriusSession = null;
		}
		super.unload();
	}

	/**
	 * @see org.eclipse.papyrus.infra.core.resource.AbstractDynamicModel#saveModel()
	 *
	 * @throws IOException
	 */
	@Override
	public void saveModel() throws IOException {
		if (this.siriusSession != null) {
			if (this.siriusSession instanceof PapyrusSession) {
				((PapyrusSession) this.siriusSession).papyrusSave(null, new NullProgressMonitor());
			} else {
				// need to force the save
				resource.save(null);
			}
		}
	}

}
