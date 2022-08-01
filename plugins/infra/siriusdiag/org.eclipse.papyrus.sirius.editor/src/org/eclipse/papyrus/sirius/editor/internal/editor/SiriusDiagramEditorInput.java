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
package org.eclipse.papyrus.sirius.editor.internal.editor;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.sirius.editor.internal.messages.Messages;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.ui.business.api.session.IEditingSession;
import org.eclipse.sirius.ui.business.api.session.SessionEditorInput;
import org.eclipse.sirius.ui.business.api.session.SessionUIManager;
import org.eclipse.sirius.viewpoint.provider.SiriusEditPlugin;
import org.eclipse.ui.IPersistableElement;

/**
 * SiriusDiagramEditorInput.
 *
 */
public class SiriusDiagramEditorInput extends SessionEditorInput {

	/** The input for the Document widget */
	private final DSemanticDiagram diagram;

	private IStatus status;

	/**
	 *
	 * Constructor.
	 *
	 * @param siriusDiagram
	 *                          the diagram to edit
	 */
	public SiriusDiagramEditorInput(final DSemanticDiagram siriusDiagram, URI uri, Session session) {
		super(uri, siriusDiagram.getName(), session);
		this.diagram = siriusDiagram;
	}

	/**
	 *
	 * @return
	 *         the diagram for which we are opening an editor
	 */
	public DSemanticDiagram getSiriusDiagramPrototype() {
		return this.diagram;
	}

	/**
	 *
	 * @see org.eclipse.ui.IEditorInput#exists()
	 *
	 * @return
	 */
	@Override
	public boolean exists() {
		return false;// not required in a Papyrus context
	}

	/**
	 *
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 *
	 * @return
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return null; // not required in a Papyrus context
	}

	/**
	 *
	 * @see org.eclipse.ui.IEditorInput#getName()
	 *
	 * @return
	 */
	@Override
	public String getName() {
		final String name = this.diagram.getName();
		return name == null || name.isEmpty() ? Messages.SiriusDiagramEditorInput_NoName : name;
	}

	/**
	 *
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 *
	 * @return
	 */
	@Override
	public IPersistableElement getPersistable() {
		return null;// not required in a Papyrus context
	}

	/**
	 *
	 * @return
	 *         the description
	 */
	private String getDescription() {
		final String description = this.diagram.getDocumentation();
		return description == null || description.isEmpty() ? Messages.SiriusDiagramEditorInput_NoDescription : description;
	}

	/**
	 *
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 *
	 * @return
	 */
	@Override
	public String getToolTipText() {
		return NLS.bind(Messages.SiriusDiagramEditorInput_Tooltip, getName(), getDescription());
	}

	/**
	 *
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 *
	 * @param adapter
	 *                    the only supported type are {@link URI} and {@link diagram}
	 * @return
	 *         the uri of the file containing the {@link diagram} to edit or the {@link diagram} itself
	 */
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == URI.class) {
			return adapter.cast(this.diagram.eResource().getURI());
		}
		if (adapter == DSemanticDiagram.class) {
			return adapter.cast(this.diagram);
		}
		if (adapter == null) {
			return super.getAdapter(adapter);
		}
		return null;
	}

	/**
	 * Get the session.
	 *
	 * @param sessionModelURI
	 *                            the Session Resource URI
	 * @param restore
	 *                            true to restore the session if it is not instantiated
	 * @return the session if it can be found, <code>null</code> otherwise
	 *
	 * @since 0.9.0
	 */
	public Session getSession(URI sessionModelURI, boolean restore) {
		Session sessionFromURI;
		try {
			sessionFromURI = SessionManager.INSTANCE.getExistingSession(sessionModelURI);

			// A session adds and removes itself from the session manager during
			// open()/close()
			// If restore, we try to create a new one and open it only in this
			// case: the session lifecycle is not safe enough to try to open a
			// previously closed session.
			if (sessionFromURI == null && restore) {
				status = Status.OK_STATUS;
			}
			sessionFromURI = openSession(sessionModelURI, restore, sessionFromURI);
		} catch (OperationCanceledException e) {
			sessionFromURI = null;
			status = new Status(IStatus.CANCEL, SiriusEditPlugin.ID, e.getLocalizedMessage(), e); // $NON-NLS-1$
			// Silent catch: can happen if failing to retrieve the session from
			// its URI
			// CHECKSTYLE:OFF
		} catch (RuntimeException e) {
			// CHECKSTYLE:ON
			sessionFromURI = null;
			status = new Status(IStatus.ERROR, SiriusEditPlugin.ID, e.getLocalizedMessage(), e); // $NON-NLS-1$
			// Silent catch: can happen if failing to retrieve the session from
			// its URI
		}
		return sessionFromURI;
	}

	private static Session openSession(URI sessionModelURI, boolean restore, Session sessionFromURI) {
		Session session = sessionFromURI;
		if (session == null && restore) {
			session = SessionManager.INSTANCE.openSession(sessionModelURI, new NullProgressMonitor(), SiriusEditPlugin.getPlugin().getUiCallback(), true);
		}

		if (session != null && session.isOpen()) {
			// The SessionUIManager creates and open an IEditingSession when
			// a session is added to the SessionManager. This
			// IEditingSession is closed and removed from the ui manager
			// when the corresponding session is removed from the session
			// manager (closed).
			IEditingSession uiSession = SessionUIManager.INSTANCE.getUISession(session);
			if (uiSession == null && restore) {
				uiSession = SessionUIManager.INSTANCE.getOrCreateUISession(session);
			}
			if (uiSession != null && !uiSession.isOpen()) {
				uiSession.open();
			}
		}
		return session;
	}

}
