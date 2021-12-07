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

package org.eclipse.papyrus.infra.siriusdiag.properties.internal.emf;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.papyrus.emf.ui.editor.factories.AbstractEStructuralFeatureDialogEditorFactory;
import org.eclipse.swt.widgets.Composite;

/**
 * Property Descriptor used to contribute to the Papyrus Advanced Property View
 */
public class CustomPropertyDescriptor extends PropertyDescriptor {

	/**
	 * the factory to use to edit the property
	 */
	private final AbstractEStructuralFeatureDialogEditorFactory factory;

	/**
	 * Constructor.
	 *
	 * @param object
	 * @param itemPropertyDescriptor
	 */
	public CustomPropertyDescriptor(final Object object, final IItemPropertyDescriptor itemPropertyDescriptor, final AbstractEStructuralFeatureDialogEditorFactory factory) {
		super(object, itemPropertyDescriptor);
		this.factory = factory;
	}

	/**
	 * @see org.eclipse.emf.edit.ui.provider.PropertyDescriptor#createPropertyEditor(org.eclipse.swt.widgets.Composite)
	 *
	 * @param composite
	 * @return
	 */
	@Override
	public final CellEditor createPropertyEditor(final Composite composite) {
		return this.factory.createEditor(this.object, this.itemPropertyDescriptor, composite);
	}

	/**
	 * @see org.eclipse.emf.edit.ui.provider.PropertyDescriptor#getLabelProvider()
	 *
	 * @return
	 */
	@Override
	public final ILabelProvider getLabelProvider() {
		return this.factory.getOrCreateLabelProvider();
	}

}
