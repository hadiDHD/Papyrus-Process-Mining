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
package org.eclipse.papyrus.sirius.editor.internal.readonly;

import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.papyrus.infra.core.resource.AbstractReadOnlyHandler;
import org.eclipse.papyrus.infra.core.resource.ReadOnlyAxis;
import org.eclipse.sirius.diagram.description.tool.SourceEdgeViewCreationVariable;

import com.google.common.base.Optional;

/**
 * This class has been created to fix the Bug 580517: [Sirius][ClassDiagram] ReconnectionTools throws Read-Only exceptions
 * 
 */
public class SiriusReadOnlyHandler extends AbstractReadOnlyHandler {
	/**
	 * Constructor.
	 *
	 * @param editingDomain
	 */
	public SiriusReadOnlyHandler(EditingDomain editingDomain) {
		super(editingDomain);
	}

	/**
	 * @see org.eclipse.papyrus.infra.core.resource.AbstractReadOnlyHandler#isReadOnly(java.util.Set, org.eclipse.emf.ecore.EObject)
	 *
	 * @param axes
	 * @param eObject
	 * @return
	 */
	@Override
	public Optional<Boolean> isReadOnly(Set<ReadOnlyAxis> axes, EObject eObject) {
		if (eObject instanceof SourceEdgeViewCreationVariable) {
			//allow to fix the bug for all reconnection tools
			return Optional.of(Boolean.FALSE);
		}
		return Optional.absent();
	}

	/**
	 * 
	 * @see org.eclipse.papyrus.infra.core.resource.AbstractReadOnlyHandler#anyReadOnly(org.eclipse.emf.common.util.URI[])
	 *
	 * @param arg0
	 * @return
	 */
	@Override
	public Optional<Boolean> anyReadOnly(URI[] arg0) {
		return Optional.absent();
	}

	/**
	 * 
	 * @see org.eclipse.papyrus.infra.core.resource.AbstractReadOnlyHandler#isReadOnly(org.eclipse.emf.ecore.EObject)
	 *
	 * @param arg0
	 * @return
	 */
	@Override
	public Optional<Boolean> isReadOnly(EObject arg0) {
		return Optional.absent();
	}

	/**
	 * 
	 * @see org.eclipse.papyrus.infra.core.resource.AbstractReadOnlyHandler#makeWritable(org.eclipse.emf.common.util.URI[])
	 *
	 * @param arg0
	 * @return
	 */
	@Override
	public Optional<Boolean> makeWritable(URI[] arg0) {
		return Optional.absent();
	}

	/**
	 * 
	 * @see org.eclipse.papyrus.infra.core.resource.AbstractReadOnlyHandler#makeWritable(org.eclipse.emf.ecore.EObject)
	 *
	 * @param arg0
	 * @return
	 */
	@Override
	public Optional<Boolean> makeWritable(EObject arg0) {
		return Optional.absent();
	}

	/**
	 * @see org.eclipse.papyrus.infra.core.resource.IReadOnlyHandler2#anyReadOnly(java.util.Set, org.eclipse.emf.common.util.URI[])
	 *
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	@Override
	public Optional<Boolean> anyReadOnly(Set<ReadOnlyAxis> arg0, URI[] arg1) {
		return Optional.absent();
	}

	/**
	 * @see org.eclipse.papyrus.infra.core.resource.IReadOnlyHandler2#makeWritable(java.util.Set, org.eclipse.emf.common.util.URI[])
	 *
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	@Override
	public Optional<Boolean> makeWritable(Set<ReadOnlyAxis> arg0, URI[] arg1) {
		return Optional.absent();
	}

}