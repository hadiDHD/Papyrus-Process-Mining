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
 *  Rengin Battal (ARTAL) - rengin.battal@artal.fr - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.sirius.uml.diagram.sequence.tests;

import static org.junit.Assert.assertFalse;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.sirius.junit.utils.rules.SiriusDiagramEditorFixture;
import org.eclipse.sirius.diagram.DDiagramElement;
import org.eclipse.sirius.diagram.sequence.SequenceDDiagram;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/** 
 * Tools for the tests of Sequence diagram
 * @author battal
 *
 */
public class SequenceDiagramTestsTool 
{
	public SequenceDiagramTestsTool()
	{
		// nothing to do here
	}
	
	/**
	 * Get a list of View elements for a specified element type
	 * @param fixture the current Sirius diagram editor fixture
	 * @param viewpointElementPredicate the researched element type
	 * @return a list of View elements corresponding to the specified element type
	 */
	public static List<View> getView(SiriusDiagramEditorFixture fixture, Predicate<DDiagramElement> viewpointElementPredicate) {
		final SequenceDDiagram element = (SequenceDDiagram) fixture.getActiveDiagram().getDiagramView().getElement();
		List<DDiagramElement> elements = Lists.newArrayList(Iterables.filter(element.getDiagramElements(), viewpointElementPredicate));
		assertFalse(elements.isEmpty());
		List<View> results = elements.stream().map(e -> fixture.findEditPart(e)).filter(e -> e instanceof IGraphicalEditPart).map(e -> ((IGraphicalEditPart) e).getNotationView()).collect(Collectors.toList());
		return results;
	}
}
