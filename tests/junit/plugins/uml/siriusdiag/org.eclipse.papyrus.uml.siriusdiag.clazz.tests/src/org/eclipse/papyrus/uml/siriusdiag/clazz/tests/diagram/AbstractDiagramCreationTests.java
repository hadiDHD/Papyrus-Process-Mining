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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.papyrus.infra.architecture.representation.PapyrusRepresentationKind;
import org.eclipse.papyrus.infra.siriusdiag.representation.SiriusDiagramPrototype;
import org.eclipse.papyrus.infra.siriusdiag.representation.architecture.CreatePapyrusSiriusClassDiagramEditorCommand;
import org.eclipse.papyrus.infra.siriusdiag.representation.architecture.commands.CreateSiriusDiagramEditorViewCommand;
import org.eclipse.papyrus.infra.siriusdiag.ui.internal.sessions.SiriusConstants;
import org.eclipse.papyrus.infra.tools.util.ClassLoaderHelper;
import org.eclipse.papyrus.infra.viewpoints.policy.PolicyChecker;
import org.eclipse.papyrus.infra.viewpoints.policy.ViewPrototype;
import org.eclipse.papyrus.junit.framework.classification.tests.AbstractPapyrusTest;
import org.eclipse.papyrus.junit.utils.rules.PapyrusEditorFixture;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;

/**
 * Adapted from Model2Doc tests, class AbstractDocumentTemplateCreationTest
 */
@SuppressWarnings("nls")
public abstract class AbstractDiagramCreationTests extends AbstractPapyrusTest {

	/**
	 * The editor fixture
	 */
	@Rule
	public final PapyrusEditorFixture fixture = new PapyrusEditorFixture();

	@Before
	public void setUp() {
		// TODO find a nice Papyrus way to check there no diagram available in the initial model
	}

	/**
	 *
	 * @return
	 *         the *.aird resource associated to the current model
	 */
	protected final Resource getAIRDResourceForCurrentModel() {// TODO rename me as getAIRDResourceForCurrentModel for Sirius
		for (final Resource current : this.fixture.getResourceSet().getResources()) {
			if (SiriusConstants.SIRIUS_DIAGRAM_MODEL_FILE_EXTENSION.equals(current.getURI().fileExtension())) {
				if (this.fixture.getModelResourceURI().trimFileExtension().equals(current.getURI().trimFileExtension())) {
					return current;
				}
			}
		}
		return null;
	}
	//
	// /**
	// *
	// * @param context
	// * a semantic element
	// * @return
	// * the {@link PapyrusDocumentPrototype} found for this context
	// */
	// protected Collection<PapyrusDocumentPrototype> getCreatablePapyrusDocumentPrototype(final EObject context) {
	// Collection<PapyrusDocumentPrototype> data = new ArrayList<>();
	//
	// for (final ViewPrototype proto : PolicyChecker.getFor(context).getPrototypesFor(context)) {
	// if (!(proto.getRepresentationKind() instanceof PapyrusDocumentPrototype)) {
	// continue;
	// }
	// data.add((PapyrusDocumentPrototype) proto.getRepresentationKind());
	// }
	// return data;
	// }

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

	// /**
	// *
	// * @param context
	// * a semantic element
	// * @return
	// * the collection of {@link DocumentTemplatePrototype} found for this context
	// */
	// protected Collection<DocumentTemplatePrototype> getCreatableDocumentTemplatePrototype(final EObject context) {
	// Collection<DocumentTemplatePrototype> data = new ArrayList<>();
	// for (final PapyrusDocumentPrototype proto : getCreatablePapyrusDocumentPrototype(context)) {
	// data.add(proto.getDocumentTemplatePrototype());
	// }
	// return data;
	// }
	//
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
	 * @param kindId
	 *            the kindId of the diagram to create
	 * @throws Exception
	 */
	protected void checkDocumentCreation(final String kindID, final String type) throws Exception {

		// 1. we look for the view prototype required to create the document
		final ViewPrototype docProto = getDiagramPrototype(this.fixture.getRoot(), kindID);
		Assert.assertNotNull("The DiagramPrototype to create a diagram of type " + kindID + " is not found.", docProto);

		// 2. check if the aird resource exists in the ModelSet
		// final Resource pdstResource = getPDSTResourceForCurrentModel();
		// Assert.assertNotNull("The pdst resource is not in the ModelSet.", pdstResource);

		// 3. check pdst resource is empty
		// Assert.assertEquals("The pdst resource is not empty", 0, pdstResource.getContents().size());

		// 4. check the pdst file is not created
		// boolean pdstFileExists = pdstResource.getResourceSet().getURIConverter().exists(pdstResource.getURI(), null);
		// Assert.assertFalse("The pdst file exists, but it should not, because it is empty and nothing has already been stored inside it.", pdstFileExists);

		// 5. get the creation command
		SiriusDiagramPrototype siriusDiagramProto = (SiriusDiagramPrototype) docProto.getRepresentationKind();
		final String cmdClassName = siriusDiagramProto.getCreationCommandClass();
		Assert.assertNotNull("The creation command to create the diagram of type " + kindID + " is not registered", cmdClassName);
		Class<?> cmdClass = ClassLoaderHelper.loadClass(cmdClassName);
		Object newClass = null;
		try {
			newClass = cmdClass.getDeclaredConstructor(new Class<?>[0]).newInstance(new Object[0]);
		} catch (InstantiationException | IllegalAccessException e) {
			throw e; // we propagate the exception
		}

		Assert.assertTrue(newClass instanceof CreatePapyrusSiriusClassDiagramEditorCommand); // TODO maybe a better way for sirius that looks like to Papyrus-Model2doc
		final CreatePapyrusSiriusClassDiagramEditorCommand createEditorCommand = (CreatePapyrusSiriusClassDiagramEditorCommand) newClass;

		// TODO : several method are probably available

		CreateSiriusDiagramEditorViewCommand result = createEditorCommand.createDSemanticDiagramEditorCreationCommand(this.fixture.getModelSet().getTransactionalEditingDomain(), siriusDiagramProto, "New class diagram", type, this.fixture.getRoot(), false,
				kindID);
		// result.execute();
		// // (this.fixture.getModelSet(), this.fixture.getRoot(), this.fixture.getRoot(), docProto, "classDiagram", true);
		// // TODO here result==null, it is strange
		//
		// Assert.assertNotNull("The creation of diagram kind " + kindID + " failed", result);
		// fixture.flushDisplayEvents();
		// Diagram diagram = fixture.getActiveDiagram().getDiagramView();
		// Assert.assertEquals(result, diagram);// must always be true
		// final PapyrusDiagramStyle diagramStyle = (PapyrusDiagramStyle) diagram.getStyle(StylePackage.eINSTANCE.getPapyrusDiagramStyle());
		// Assert.assertEquals("The created diagram has not the expected kindID", kindID, diagramStyle.getDiagramKindId());
		// Assert.assertEquals("The created diagram has not the expected type", type, diagram.getType());

	}
}
