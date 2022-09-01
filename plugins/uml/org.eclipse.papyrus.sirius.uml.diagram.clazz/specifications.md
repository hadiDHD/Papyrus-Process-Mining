The content of this file describes specific choices of implementation/limitation.

#Abstraction
The **Abstraction** is owned by the nearest parent Package of the source.
The *semantic candidate expression* must return all **Abstraction** existing in the Package and in its children.

#Constraint
technical requirement:
The **ContextLink** changes the owner of the **Constraint**, so creating a **ContextLink** can results in the constraint disappearing. To avoid this behavior, the *semantic candidate expression* must return all **Constraints** found in the **Package** (diagram background) and in its children.

#ContainmentLink
technical requirement: The target view of the **ContainmentLink** must be located on the diagram background to ease the development of the diagram.  

#Dependency
The **Dependency** is owned by the nearest parent Package of the source.
The *semantic candidate expression* must return all **Dependency** existing in the Package and in its children.

#ElementImport
The **ElementImport** is owned by the source element.

#Generalization
The **Generalization** is owned by the source element.

##GeneralizationSet
The **GeneralizationSet** is owned by the nearest parent Package of the source **Generalization**.

##InformationFlow
The **InformationFlow** is owned by the nearest parent Package of the source.

##InstanceSpecification Link
The **InstanceSpecification** is owned by the nearest parent Package of the source **InstanceSpecification**. (unlike in GMF Diagram). In GMF Diagram they are always inside the Package at the root of the UML model.

##InterfaceRealization
The **InterfaceRealization** is owned by the source of the link which is a **BehavioredClassifier**.

##Link (Constaint#constrainedElements and Comment#annotatedElement)
technical requirement: We forbid to change the semantic of a link by changing its source from a Comment to a Constraint (and vice-versa).

##PackageImport
The **PackageImport** is owned by the source of the link which is a **Package**.

##PackageMerge
The **PackageMerge** is owned by the source of the link which is a **Package**.

#Realization
The **Realization** is owned by the nearest parent Package of the source.

#Substitution
The **Substitution** is owned by the source of the link. The source and target must be **Classifier**.
