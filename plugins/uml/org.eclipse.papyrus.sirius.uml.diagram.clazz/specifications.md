The content of this file describes specific choices of implementation/limitation.

#Abstraction
The **Abstraction** is owned by the nearest Package of the source.
The *semantic candidate expression* must return all **Abstraction** existing in the Package and in its children.

#Constraint
technical requirement:
The **ContextLink** changes the owner of the **Constraint**, so creating a **ContextLink** can results in the constraint disappearing. To avoid this behavior, the *semantic candidate expression* must return all **Constraints** found in the **Package** (diagram background) and in its children.

#ContainmentLink
technical requirement: The target view of the **ContainmentLink** must be located on the diagram background to ease the development of the diagram.  

#Dependency
The **Dependency** is owned by the nearest Package of the source.
The *semantic candidate expression* must return all **Dependency** existing in the Package and in its children.

#ElementImport
The **ElementImport** is owned by the source element.

#Generalization
The **Generalization** is owned by the source element.

##GeneralizationSet
The **GeneralizationSet** is owned by the nearest Package of the source **Generalization**.

##InformationFlow
The **InformationFlow** is owned by the nearest Package of the source **InformationFlow**.

##InstanceSpecification Link
The **InstanceSpecification** is owned by the nearest Package of the source **InstanceSpecification**. (unlike in GMF Diagram). In GMF Diagram they are always inside the Package at the root of the UML model.