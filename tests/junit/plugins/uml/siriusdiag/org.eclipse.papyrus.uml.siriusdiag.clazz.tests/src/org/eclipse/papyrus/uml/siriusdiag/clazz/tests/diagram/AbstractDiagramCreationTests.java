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
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.infra.architecture.representation.PapyrusRepresentationKind;
import org.eclipse.papyrus.infra.gmfdiag.common.AbstractPapyrusGmfCreateDiagramCommandHandler;
import org.eclipse.papyrus.infra.gmfdiag.common.helper.DiagramPrototype;
import org.eclipse.papyrus.infra.gmfdiag.representation.PapyrusDiagram;
import org.eclipse.papyrus.infra.gmfdiag.style.PapyrusDiagramStyle;
import org.eclipse.papyrus.infra.gmfdiag.style.StylePackage;
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
	 *         the {@link PapyrusDocumentPrototype} found for this context
	 */
	protected Collection<DiagramPrototype> getCreatableDiagramPrototype(final EObject context) {
		final Collection<DiagramPrototype> viewPrototype = new ArrayList<>();
		for (final ViewPrototype proto : PolicyChecker.getFor(context).getPrototypesFor(context)) {
			if (proto instanceof DiagramPrototype) {
				viewPrototype.add((DiagramPrototype) proto);
			}
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
	 *            the type of the wanted {@link DiagramPrototype}
	 * @return
	 *         the {@link DiagramPrototype} or <code>null</code> if not found
	 */
	protected final DiagramPrototype getDiagramPrototype(final EObject context, final String type) {
		for (final DiagramPrototype current : getCreatableDiagramPrototype(context)) {
			String toto = current.getImplementation();
			if (current.getRepresentationKind() instanceof PapyrusDiagram) {
				final PapyrusDiagram pdp = current.getRepresentationKind();
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
		final DiagramPrototype docProto = getDiagramPrototype(this.fixture.getRoot(), kindID);
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
		final String cmdClassName = docProto.getRepresentationKind().getCreationCommandClass();
		Assert.assertNotNull("The creation command to create the diagram of type " + kindID + " is not registered", cmdClassName);
		Class<?> cmdClass = ClassLoaderHelper.loadClass(cmdClassName);
		Object newClass = null;
		try {
			newClass = cmdClass.getDeclaredConstructor(new Class<?>[0]).newInstance(new Object[0]);
		} catch (InstantiationException | IllegalAccessException e) {
			throw e; // we propagate the exception
		}

		Assert.assertTrue(newClass instanceof AbstractPapyrusGmfCreateDiagramCommandHandler); // TODO maybe a better way for sirius that looks like to Papyrus-Model2doc
		final AbstractPapyrusGmfCreateDiagramCommandHandler createEditorCommand = (AbstractPapyrusGmfCreateDiagramCommandHandler) newClass;

		// 6. create a new document template
		// TODO : several method are probably available

		Diagram result = createEditorCommand.createDiagram(this.fixture.getModelSet(), this.fixture.getRoot(), this.fixture.getRoot(), docProto, "niceDocumentTemplate", true);
		// TODO here result==null, it is strange

		Assert.assertNotNull("The creation of diagram kind " + kindID + " failed", result);
		fixture.flushDisplayEvents();
		Diagram diagram = fixture.getActiveDiagram().getDiagramView();
		Assert.assertEquals(result, diagram);// must always be true
		final PapyrusDiagramStyle diagramStyle = (PapyrusDiagramStyle) diagram.getStyle(StylePackage.eINSTANCE.getPapyrusDiagramStyle());
		Assert.assertEquals("The created diagram has not the expected kindID", kindID, diagramStyle.getDiagramKindId());
		Assert.assertEquals("The created diagram has not the expected type", type, diagram.getType());

		// TODO to be continued with undo and redo

		// Assert.assertEquals("The pdst file doesn't contains the expected number of element.", 1, pdstResource.getContents().size());
		// final EObject pdstContents = pdstResource.getContents().get(0);
		// Assert.assertTrue(pdstContents instanceof TextDocumentTemplate);
		//
		// this.fixture.save();
		// // 7. check the pdst file is now created
		// pdstFileExists = pdstResource.getResourceSet().getURIConverter().exists(pdstResource.getURI(), null);
		// Assert.assertTrue("The pdst file doesn't exists, but it should exist, because it contains a DocumentTemplate", pdstFileExists);
		//
		// // 8. check Undo/Redo
		// final CommandStack stack = this.fixture.getEditingDomain().getCommandStack();
		// stack.undo();
		// this.fixture.save();
		//
		// // 8.1 the pdst file must continue to exists but it must be empty
		// pdstFileExists = pdstResource.getResourceSet().getURIConverter().exists(pdstResource.getURI(), null);
		// Assert.assertTrue("The pdst file doesn't exists, but it must exist even after a undoing the creation", pdstFileExists);
		// Assert.assertEquals("The pdst file doesn't contains the expected number of element after undoing the creation.", 0, pdstResource.getContents().size());
		//
		// stack.redo();
		// this.fixture.save();
		//
		// // 8.2 the pdst file must continue to exists but it must not be empty
		// pdstFileExists = pdstResource.getResourceSet().getURIConverter().exists(pdstResource.getURI(), null);
		// Assert.assertTrue("The pdst file doesn't exists, but it must exist after redoing the creation", pdstFileExists);
		// Assert.assertEquals("The pdst file must contains the expected number of element after redoing the creation.", 1, pdstResource.getContents().size());
	}
}
