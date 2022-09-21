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
package org.eclipse.papyrus.sirius.editor.internal.runnables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.infra.architecture.ArchitectureDescriptionUtils;
import org.eclipse.papyrus.infra.core.architecture.RepresentationKind;
import org.eclipse.papyrus.infra.core.architecture.merged.MergedArchitectureViewpoint;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.sirius.editor.representation.SiriusDiagramPrototype;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.diagram.description.DiagramDescription;
import org.eclipse.sirius.ui.business.api.viewpoint.ViewpointSelection;
import org.eclipse.sirius.ui.business.api.viewpoint.ViewpointSelectionCallback;
import org.eclipse.sirius.viewpoint.description.Viewpoint;



/**
 * This runnable is used to update the applied Sirius Viewpoint
 */
public class UpdateSiriusViewpointRunnable implements Runnable {

	/**
	 * the Sirius Session
	 */
	protected final Session session;

	/**
	 * The Papyrus modelSet;
	 */
	protected final ModelSet modelSet;

	/**
	 * 
	 * Constructor.
	 *
	 * @param session
	 *            the Sirius Session for which we want to update the viewpoints
	 */
	public UpdateSiriusViewpointRunnable(final Session session, final ModelSet modelSet) {
		this.session = session;
		this.modelSet = modelSet;
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

		final ViewpointSelectionCallback callBack = new ViewpointSelectionCallback();
		for (Viewpoint vp : toApply) {
			Viewpoint matchingSelectedViewpoint = getMatchingSelectedViewpoint(currentAppliedViewpoints, vp);
			if (matchingSelectedViewpoint == null) {
				callBack.selectViewpoint(vp, this.session, new NullProgressMonitor());
			} else {
				toUnapply.remove(matchingSelectedViewpoint);
			}
		}

		for (final Viewpoint previouslySelected : toUnapply) {
			callBack.deselectViewpoint(previouslySelected, this.session, new NullProgressMonitor());
		}
	}

	/**
	 * Get the viewpoint among selectedViewpoint corresponding to the viewpoint to apply.
	 * 
	 * @param currentAppliedViewpoints
	 *            list of selected viewpoints
	 * @param vp
	 *            the viewpoint to apply
	 * @return the viewpoint among selectedViewpoint corresponding to the viewpoint to apply.
	 */
	private Viewpoint getMatchingSelectedViewpoint(Collection<Viewpoint> currentAppliedViewpoints, Viewpoint vp) {
		for (Viewpoint cvp : currentAppliedViewpoints) {
			if (cvp.getName() != null && cvp.getName().equals(vp.getName())) {
				return cvp;
			}
		}
		return null;
	}

	/**
	 *
	 * @return
	 *         the collection of Sirius {@link Viewpoint} referenced in the current Papyrus architecture framework
	 */
	private final Collection<Viewpoint> collectSiriusViewpointInPapyrusArchitecture() {
		if (this.session == null || this.session.getSemanticResources().size() <= 0) {
			return Collections.emptyList();
		}
		String fileExtension = this.session.getSemanticResources().iterator().next().getURI().fileExtension();
		Set<Viewpoint> availableViewpoints = ViewpointSelection.getViewpoints(fileExtension);
		final ArchitectureDescriptionUtils utils = new ArchitectureDescriptionUtils(this.modelSet);
		final Collection<MergedArchitectureViewpoint> vp = utils.getArchitectureContext().getViewpoints();
		final Collection<Viewpoint> siriusViewpoints = new HashSet<>();
		for (MergedArchitectureViewpoint tmp : vp) {
			for (RepresentationKind current : tmp.getRepresentationKinds()) {
				if (current instanceof SiriusDiagramPrototype) {
					final DiagramDescription desc = ((SiriusDiagramPrototype) current).getDiagramDescription();
					final EObject currentV = desc.eContainer();
					if (currentV instanceof Viewpoint) {
						Viewpoint appliedViewpoint = availableViewpoints.stream().filter(v -> v.getName().equals(((Viewpoint) currentV).getName())).findFirst().orElse((Viewpoint) currentV);
						siriusViewpoints.add(appliedViewpoint);
					}
				}
			}
		}
		return siriusViewpoints;
	}
}
