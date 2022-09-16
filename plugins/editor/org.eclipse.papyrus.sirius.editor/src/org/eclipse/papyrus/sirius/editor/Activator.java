/******************************************************************************
 * Copyright (c) 2021-2022 CEA LIST, Artal Technologies
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
 *  Vincent LORENZO (CEA LIST) - vincent.lorenzo@cea.fr - bug 580744
 *****************************************************************************/
package org.eclipse.papyrus.sirius.editor;

import org.eclipse.papyrus.infra.core.log.LogHelper;
import org.eclipse.papyrus.infra.emf.spi.resolver.IEObjectResolver;
import org.eclipse.papyrus.infra.emf.utils.EMFHelper;
import org.eclipse.papyrus.sirius.editor.internal.emf.SiriusEditPartEObjectResolver;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "org.eclipse.papyrus.sirius.editor"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	public static LogHelper log;

	/**
	 * contribution to the OSGi Service used by {@link EMFHelper} to revolve Sirius EditPart into the represented semantic EObject
	 */
	private ServiceRegistration<IEObjectResolver> eobjectResolverReg;


	/**
	 * The constructor
	 */
	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		log = new LogHelper(getDefault());
		this.eobjectResolverReg = context.registerService(IEObjectResolver.class, SiriusEditPartEObjectResolver::resolve, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (this.eobjectResolverReg != null) {
			this.eobjectResolverReg.unregister();
			this.eobjectResolverReg = null;
		}
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
