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
package org.eclipse.papyrus.uml.diagram.specs.extractor.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.papyrus.infra.gmfdiag.paletteconfiguration.ChildConfiguration;
import org.eclipse.papyrus.infra.gmfdiag.paletteconfiguration.DrawerConfiguration;
import org.eclipse.papyrus.infra.gmfdiag.paletteconfiguration.ElementDescriptor;
import org.eclipse.papyrus.infra.gmfdiag.paletteconfiguration.PaletteConfiguration;
import org.eclipse.papyrus.infra.gmfdiag.paletteconfiguration.StackConfiguration;
import org.eclipse.papyrus.infra.gmfdiag.paletteconfiguration.ToolConfiguration;

/**
 * This class reads the palette description
 */
public class PaletteDescriptionReader {

	/**
	 * The URI of the loaded palette
	 */
	private URI paletteURI;

	protected final Map<String, Collection<String>> nodesMetaclassesVSelementTypesID = new TreeMap<String, Collection<String>>();


	protected final Map<String, Collection<String>> edgesMetaclassesVSelementTypesID = new TreeMap<String, Collection<String>>();

	protected final Map<String, String> nodesByPaletteToolID = new TreeMap<>();

	protected final Map<String, String> edgesByPaletteToolID = new TreeMap<>();

	/**
	 * This map associated each elementType id (key) to a "metaclass" (palette element)
	 */
	protected final Map<String, String> elementTypesVSMetaclass = new HashMap<>();

	/**
	 * 
	 * Constructor.
	 *
	 * @param paletteURI
	 *            The URI of the loaded palette
	 */
	public PaletteDescriptionReader(final URI paletteURI) {
		this.paletteURI = paletteURI;
		readPaletteDescription();
	}

	/**
	 * this method load and read the palette description and fill the fields of this class
	 */
	protected void readPaletteDescription() {
		// we load the palette configuration to extract all elements type id
		// (we can't reference them directly because we won't be in the same ResourceSet!
		final ResourceSet set = new ResourceSetImpl();
		final Resource res = set.getResource(this.paletteURI, true);
		if (res.getContents().size() == 1 && res.getContents().get(0) instanceof PaletteConfiguration) {
			final PaletteConfiguration conf = (PaletteConfiguration) res.getContents().get(0);
			final Iterator<DrawerConfiguration> iter = conf.getDrawerConfigurations().iterator();
			while (iter.hasNext()) {
				final DrawerConfiguration configuration = iter.next();
				if (isNodeGroup(configuration)) {
					fillMap(configuration, this.nodesMetaclassesVSelementTypesID);
					fillMapByPaletteID(configuration, this.nodesByPaletteToolID);
				} else if (isEdgesGroup(configuration)) {
					fillMap(configuration, this.edgesMetaclassesVSelementTypesID);
					fillMapByPaletteID(configuration, this.edgesByPaletteToolID);
				} else {
					throw new UnsupportedOperationException("MUST BE COMPLETED - 2"); //$NON-NLS-1$
				}
			}
		} else {
			throw new UnsupportedOperationException("MUST BE COMPLETED - 1"); //$NON-NLS-1$
		}
	}

	protected boolean isNodeGroup(DrawerConfiguration conf) {
		return conf.getId().toLowerCase().contains("node"); //$NON-NLS-1$
	}

	protected boolean isEdgesGroup(DrawerConfiguration conf) {
		return conf.getId().toLowerCase().contains("edge") //$NON-NLS-1$
				// deployement diagram and maybe others
				|| conf.getId().toLowerCase().contains("link") //$NON-NLS-1$
				// clazz diagram and maybe others
				|| conf.getId().toLowerCase().contains("relationship"); //$NON-NLS-1$
	}

	protected void fillMap(final DrawerConfiguration drawerConfiguration, final Map<String, Collection<String>> map) {
		for (final ChildConfiguration conf : drawerConfiguration.getOwnedConfigurations()) {
			fillMap(conf, map);
		}
	}

	protected void fillMap(final ChildConfiguration childConfiguration, final Map<String, Collection<String>> map) {
		if (childConfiguration instanceof ToolConfiguration) {
			final ToolConfiguration tmp = (ToolConfiguration) childConfiguration;
			final String label = tmp.getLabel();
			Collection<String> elementTypes = new ArrayList<>();
			map.put(label, elementTypes);
			for (final ElementDescriptor current : tmp.getElementDescriptors()) {
				final String identifier = current.getElementType().getIdentifier();
				this.elementTypesVSMetaclass.put(identifier, label);
				elementTypes.add(identifier);
			}
		} else if (childConfiguration instanceof StackConfiguration) {
			final StackConfiguration stackConfiguration = (StackConfiguration) childConfiguration;
			for (ChildConfiguration current : stackConfiguration.getOwnedConfigurations()) {
				fillMap(current, map);
			}
		}
	}

	protected void fillMapByPaletteID(final DrawerConfiguration drawerConfiguration, Map<String, String> paletteID) {
		for (final ChildConfiguration conf : drawerConfiguration.getOwnedConfigurations()) {
			fillMapByPaletteID(conf, paletteID);
		}
	}

	protected void fillMapByPaletteID(final ChildConfiguration childConfiguration, Map<String, String> paletteID) {
		if (childConfiguration instanceof ToolConfiguration) {
			final ToolConfiguration tmp = (ToolConfiguration) childConfiguration;
			paletteID.put(tmp.getLabel(), tmp.getId());
		} else if (childConfiguration instanceof StackConfiguration) {
			final StackConfiguration stackConfiguration = (StackConfiguration) childConfiguration;
			for (ChildConfiguration current : stackConfiguration.getOwnedConfigurations()) {
				fillMapByPaletteID(current, paletteID);
			}
		}
	}

	/**
	 * 
	 * @param map
	 *            a map associating "metaclass" to their elementsType identified by their ids
	 * @return
	 *         a map associating "metaclass" to their elementsType
	 */
	private Map<String, Collection<IElementType>> getElementTypes(final Map<String, Collection<String>> map) {
		final Map<String, Collection<IElementType>> realMap = new TreeMap<>();
		for (final Entry<String, Collection<String>> current : map.entrySet()) {
			final Collection<IElementType> types = new ArrayList<>();
			realMap.put(current.getKey(), types);
			for (final String typeId : current.getValue()) {
				types.add(ElementTypeRegistry.getInstance().getType(typeId));
			}

		}

		return realMap;
	}

	/**
	 * 
	 * @return
	 *         a map associating nodes "metaclass" to their elementsType
	 */
	public Map<String, Collection<IElementType>> getNodesElementsTypes() {
		return getElementTypes(this.nodesMetaclassesVSelementTypesID);
	}

	/**
	 * 
	 * @return
	 *         a map associating edges "metaclass" to their elementsType
	 */
	public Map<String, Collection<IElementType>> getEdgesElementsTypes() {
		return getElementTypes(edgesMetaclassesVSelementTypesID);
	}

	/**
	 * 
	 * @param type
	 *            an elementType
	 * @return
	 *         a the metaclass (palette element label) associated to this elementType
	 */
	public String getMetaclass(final IElementType type) {
		return getMetaclass(type.getId());
	}

	/**
	 * 
	 * @param elementTypeID
	 *            an elementTypeID
	 * @return
	 *         a the metaclass (palette element label) associated to this elementTypeID
	 */
	public String getMetaclass(final String elementTypeID) {
		return this.elementTypesVSMetaclass.get(elementTypeID);
	}

	/**
	 * 
	 * @return
	 *         the set of the "metaclass" name (palette element label) fot the nodes
	 */
	public Set<String> getMetaclassNodes() {
		return this.nodesMetaclassesVSelementTypesID.keySet();
	}

	/**
	 * 
	 * @return
	 *         the set of the "metaclass" name (palette element label) fot the edges
	 */
	public Set<String> getMetaclassEdges() {
		return this.edgesMetaclassesVSelementTypesID.keySet();
	}

	public Map<String, String> getNodesToodIdsByMetaclass() {
		return this.edgesByPaletteToolID;
	}

	public Map<String, String> getEdgesToodIdsByMetaclass() {
		return this.edgesByPaletteToolID;
	}

	public Map<String, Collection<String>> getMetaclassNodesVSElementTypesID() {
		return this.nodesMetaclassesVSelementTypesID;
	}

	public Map<String, Collection<String>> getMetaclassEdgesVSElementTypesID() {
		return this.edgesMetaclassesVSelementTypesID;
	}
}
