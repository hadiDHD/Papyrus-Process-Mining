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
package org.eclipse.papyrus.infra.siriusdiag.ui.internal.menus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype;
import org.eclipse.papyrus.infra.siriusdiag.ui.internal.viewpoint.SiriusDiagramViewPrototype;
import org.eclipse.papyrus.infra.viewpoints.policy.DynamicContribution;
import org.eclipse.papyrus.infra.viewpoints.policy.PolicyChecker;
import org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype;


/**
 * Represent the dynamic contribution of the Sirius Diagram Template Views menu.
 *
 */
public class SiriusDiagramDynamicContribution extends DynamicContribution {

	/**
	 * Constructor.
	 */
	public SiriusDiagramDynamicContribution() {
	}

	/**
	 * Constructor.
	 *
	 * @param id
	 */
	public SiriusDiagramDynamicContribution(String id) {
		super(id);
	}

	/**
	 * @see org.eclipse.ui.actions.CompoundContributionItem#getContributionItems()
	 *
	 * @return
	 */
	@Override
	protected IContributionItem[] getContributionItems() {
		final EObject selection = getSelection();
		if (selection == null) {
			return new IContributionItem[0];
		}

		// build a list of all the available prototypes
		List<ViewPrototype> data = new ArrayList<>();
		for (final ViewPrototype proto : PolicyChecker.getFor(selection).getPrototypesFor(selection)) {

			if (proto instanceof SiriusDiagramViewPrototype && proto.getRepresentationKind() instanceof SiriusDiagramPrototype) {
				final SiriusDiagramViewPrototype siriusPrototype = (SiriusDiagramViewPrototype) proto;
				if (siriusPrototype.canInstantianteOn(selection)) {
					data.add(proto);
				}
			}
		}

		Collections.sort(data, new ViewPrototype.Comp());

		// build the full labels
		List<String> labels = new ArrayList<>(data.size());
		String last = null;
		boolean first = true;
		for (ViewPrototype item : data) {
			String label = item.getLabel();
			if (last != null && last.equals(label)) {
				// name collision
				if (first) {
					labels.set(labels.size() - 1, data.get(labels.size() - 1).getFullLabel());
					first = false;
				}
				labels.add(item.getFullLabel());
			} else {
				labels.add(label);
				last = label;
				first = true;
			}
		}

		// build the menu
		List<IContributionItem> items = new ArrayList<>(data.size());
		for (int i = 0; i != data.size(); i++) {
			final ViewPrototype proto = data.get(i);
			String label = labels.get(i);
			items.add(new ActionContributionItem(new Action(label, proto.getIconDescriptor()) {
				@Override
				public void run() {
					proto.instantiateOn(selection);
				}
			}));
		}
		return items.toArray(new IContributionItem[items.size()]);
	}

}
