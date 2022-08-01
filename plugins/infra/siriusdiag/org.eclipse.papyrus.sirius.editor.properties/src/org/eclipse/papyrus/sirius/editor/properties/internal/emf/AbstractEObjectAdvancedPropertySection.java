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

package org.eclipse.papyrus.sirius.editor.properties.internal.emf;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory.Descriptor;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory.Descriptor.Registry;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.papyrus.infra.emf.utils.EMFHelper;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.tabbed.AdvancedPropertySection;

/**
 * Property Section used to contribute to Papyrus Advanced Property View for EObject selection
 */
public abstract class AbstractEObjectAdvancedPropertySection extends AdvancedPropertySection {

	/**
	 * Constructor.
	 *
	 */
	public AbstractEObjectAdvancedPropertySection() {
		super();
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.AdvancedPropertySection#setInput(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 *
	 * @param part
	 * @param selection
	 */
	@Override
	public final void setInput(final IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			// ensure the selection represents an EObject
			final Object first = ((IStructuredSelection) selection).getFirstElement();
			final EObject eobject = EMFHelper.getEObject(first);
			if (eobject != null) {
				selection = new StructuredSelection(eobject);
			}
		}
		if (selection instanceof IStructuredSelection && ((IStructuredSelection) selection).getFirstElement() instanceof EObject) {
			final EObject selectedElement = (EObject) ((IStructuredSelection) selection).getFirstElement();
			final Registry registry = ComposedAdapterFactory.Descriptor.Registry.INSTANCE;
			final Collection<Object> types = new ArrayList<>();
			types.add(selectedElement.eClass().getEPackage());
			types.add(IItemPropertySource.class);


			// we look for the adapter factory registered for the metamodel of the object we want to edit
			final Descriptor descriptor = registry.getDescriptor(types);
			final AdapterFactory adapterFactory = descriptor.createAdapterFactory();
			this.page.setPropertySourceProvider(new AdapterFactoryContentProvider(adapterFactory) {

				/**
				 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider#createPropertySource(java.lang.Object, org.eclipse.emf.edit.provider.IItemPropertySource)
				 *
				 * @param object
				 * @param itemPropertySource
				 * @return
				 */
				@Override
				protected IPropertySource createPropertySource(final Object object, final IItemPropertySource itemPropertySource) {
					return AbstractEObjectAdvancedPropertySection.this.createPropertySource(object, itemPropertySource);
				}
			});
		} else {
			this.page.setPropertySourceProvider(null);
		}

		super.setInput(part, selection);
	}


	/**
	 *
	 * @param object
	 *            an object
	 * @param itemPropertySource
	 *            the item property source
	 * @return
	 *         the created {@link IPropertySource}
	 */
	public abstract IPropertySource createPropertySource(final Object object, final IItemPropertySource itemPropertySource);


}
