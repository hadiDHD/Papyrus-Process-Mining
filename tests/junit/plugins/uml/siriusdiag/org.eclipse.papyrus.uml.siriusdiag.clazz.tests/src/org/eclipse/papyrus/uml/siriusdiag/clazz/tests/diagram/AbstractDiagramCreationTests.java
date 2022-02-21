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
package org.eclipse.papyrus.uml.siriusdiag.clazz.tests.diagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.osgi.util.NLS;
import org.eclipse.papyrus.infra.architecture.representation.PapyrusRepresentationKind;
import org.eclipse.papyrus.infra.gmfdiag.common.helper.DiagramPrototype;
import org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramConstants;
import org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype;
import org.eclipse.papyrus.infra.siriusdiag.representation.architecture.AbstractCreateSiriusDiagramEditorCommand;
import org.eclipse.papyrus.infra.siriusdiag.ui.internal.sessions.SiriusConstants;
import org.eclipse.papyrus.infra.tools.util.ClassLoaderHelper;
import org.eclipse.papyrus.infra.viewpoints.policy.PolicyChecker;
import org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype;
import org.eclipse.papyrus.junit.framework.classification.tests.AbstractPapyrusTest;
import org.eclipse.papyrus.junit.utils.rules.PapyrusEditorFixture;
import org.eclipse.sirius.diagram.DSemanticDiagram;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.description.DAnnotation;
import org.eclipse.uml2.uml.Package;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;

/**
 * Abstract class to test the Sirius Diagram creation in Papyrus
 */
public abstract class AbstractDiagramCreationTests extends AbstractPapyrusTest {

	protected Package rootModel;

	/**
	 * The editor fixture
	 */
	@Rule
	public final PapyrusEditorFixture fixture = new PapyrusEditorFixture();

	/**
	 * 
	 */
	@Before
	public void setUp() {
		this.rootModel = this.fixture.getModel();
	}

	/**
	 *
	 * @return
	 *         the *.aird resource associated to the current model
	 */
	protected final Resource getAIRDResourceForCurrentModel() {
		for (final Resource current : this.fixture.getResourceSet().getResources()) {
			if (SiriusConstants.SIRIUS_DIAGRAM_MODEL_FILE_EXTENSION.equals(current.getURI().fileExtension())) {
				if (this.fixture.getModelResourceURI().trimFileExtension().equals(current.getURI().trimFileExtension())) {
					return current;
				}
			}
		}
		return null;
	}

	/**
	 *
	 * @param context
	 *            a semantic element
	 * @return
	 *         the {@link ViewPrototype} found for this context
	 */
	protected Collection<ViewPrototype> getCreatableDiagramPrototype(final EObject context) {
		final Collection<ViewPrototype> viewPrototype = new ArrayList<>();
		for (final ViewPrototype proto : PolicyChecker.getFor(context).getPrototypesFor(context)) {
			viewPrototype.add(proto);
		}
		return viewPrototype;
	}


	/**
	 *
	 * @param context
	 *            a semantic element
	 * @param type
	 *            the type of the wanted {@link ViewPrototype}
	 * @return
	 *         the {@link DiagramPrototype} or <code>null</code> if not found
	 */
	protected final ViewPrototype getDiagramPrototype(final EObject context, final String type) {
		for (final ViewPrototype current : getCreatableDiagramPrototype(context)) {
			if (current.getRepresentationKind() instanceof SiriusDiagramPrototype) {
				final SiriusDiagramPrototype pdp = (SiriusDiagramPrototype) current.getRepresentationKind();
				if (type.equals(((PapyrusRepresentationKind) pdp).getId())) {
					return current;
				}

			}
		}
		return null;
	}

	/**
	 * 
	 * @param semanticOwner
	 *            the semantic owner for the diagram to create
	 * @param diagramName
	 *            the name to set for the new diagram
	 * @param type
	 *            the type of the diagram to create
	 */
	protected void checkDiagramCreationFromSiriusDiagramPrototype(final EObject semanticOwner, final String diagramName, final String type) {
		// 1. we look for the view prototype required to create the document
		final ViewPrototype diagramPrototype = getDiagramPrototype(this.fixture.getRoot(), type);
		Assert.assertNotNull(NLS.bind("The DiagramPrototype to create a diagram of type {0} is not found.", type), diagramPrototype); //$NON-NLS-1$

		// 2. check if the aird resource exists in the ModelSet
		final Resource airdResource = getAIRDResourceForCurrentModel();
		Assert.assertNotNull("The aird resource is not in the ModelSet.", airdResource); //$NON-NLS-1$

		// 3. check aird resource is empty
		Assert.assertEquals("The aird resource is not empty", 0, airdResource.getContents().size()); //$NON-NLS-1$

		// 4. check the pdst file is not created
		boolean pdstFileExists = airdResource.getResourceSet().getURIConverter().exists(airdResource.getURI(), null);
		Assert.assertFalse("The aird file exists, but it should not, because it is empty and nothing has already been stored inside it.", pdstFileExists); //$NON-NLS-1$

		boolean succeed = diagramPrototype.instantiateOn(semanticOwner, diagramName, true);
		fixture.flushDisplayEvents();
		Assert.assertTrue("The instantiation of the diagram failed", succeed); //$NON-NLS-1$

		final List<EObject> airdContents = airdResource.getContents();
		Assert.assertEquals(2, airdContents.size());
		Assert.assertTrue(airdContents.get(0) instanceof DAnalysis);
		Assert.assertTrue(airdContents.get(1) instanceof DSemanticDiagram);

		final DSemanticDiagram dDiagram = (DSemanticDiagram) airdContents.get(1);
		Assert.assertEquals("The created diagram must be empty", 0, dDiagram.getDiagramElements().size()); //$NON-NLS-1$
		Assert.assertEquals("The created diagram hasn't the expected semanticOwner", semanticOwner, dDiagram.getTarget()); //$NON-NLS-1$
		Assert.assertEquals("The created diagram hasn't the expected name", diagramName, dDiagram.getName()); //$NON-NLS-1$
		final DAnnotation annotation = dDiagram.getDAnnotation(SiriusDiagramConstants.PAPYRUS_SIRIUS_DIAGRAM_IMPLEMENTATION_DANNOTATION_SOURCE);
		final String implementationId = annotation.getDetails().get(SiriusDiagramConstants.PAPYRUS_SIRIUS_DIAGRAM_IMPLEMENTATION_DANNOTATION_KEY);
		Assert.assertEquals("The created diagram hasn't the expected implementationId", type, implementationId); //$NON-NLS-1$

		Diagram diagram = fixture.getActiveDiagram().getDiagramView();
		Assert.assertNotNull("The created GMF Diagram must not be null", diagram); //$NON-NLS-1$
		Assert.assertEquals("The element associated to the GMF Diagram must be the DSemanticDiagram", dDiagram, diagram.getElement()); //$NON-NLS-1$
	}


	/**
	 * dispose the resource
	 */
	@After
	public void tearDown() {
		this.rootModel = null;
	}
}
