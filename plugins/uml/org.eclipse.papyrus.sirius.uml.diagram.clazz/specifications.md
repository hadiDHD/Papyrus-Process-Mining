The content of this file describes specific choices of implementation/limitation.

#Abstraction
The **Abstraction** is owned by the nearest parent Package of the source.
The *semantic candidate expression* must return all **Abstraction** existing in the Package and in its children.

#Association
The **Association** is owned by the nearest Package of the source.
The *semantic candidate expression* must return all **Association** existing in the Package and in its children. The **Association** references two members ends. 
The first memberEnd is the **Property** typed with the target. The second memberEnd is the **Property** typed with the source.

The palette proposes 4 kins of **Association** to create.

##Directed Association
- an **Association** created from class A (source) to class B (target):
	- the class A owns a **Property** b typed with B with a multiplicity [0..1], aggregation is set to **none**,
	- the association owns a **Property** a, typed with A, with a multiplicity set to 1, aggregation is set to **none**,
	- the first memberEnd (index 0) is the **Property** typed with B, the second memberEnd (index 1) is the **Property** typed with A.
	
##Composite Association
- an **Association** created from class A (source) to class B (target):
	- the class A owns a **Property** b typed with B with a multiplicity [0..1], aggregation is set to **composite**,
	- the association owns a **Property** a, typed with A, with a multiplicity set to 1, aggregation is set to **none**,
	- the first memberEnd (index 0) is the **Property** typed with B, the second memberEnd (index 1) is the **Property** typed with A.

##Share Association
- an **Association** created from class A (source) to class B (target):
	- the class A owns a **Property** b typed with B with a multiplicity [0..1], aggregation is set to **share**,
	- the association owns a **Property** a, typed with A, with a multiplicity set to 1, aggregation is set to **none**,
	- the first memberEnd (index 0) is the **Property** typed with B, the second memberEnd (index 1) is the **Property** typed with A.

##Association
- an **Association** created from class A (source) to class B (target):
	- the classes A and B don't own properties
	- the association owns two **Properties**, a:A [0..1] and b:B [1]. The aggregation field is set to **none** for these 2 properties,
	- the first memberEnd (index 0) is the **Property** typed with B, the second memberEnd (index 1) is the **Property** typed with A.
	
#AssociationClass
The **AssociationClass** is owned by the nearest parent Package of the source. The **AssociationClass** owns the two end members. 
The first memberEnd is the **Property** typed with the target. The second memberEnd is the **Property** typed with the source.
Basically, as it is done for **Association**, the first memberEnd is the source and the second memberEnd is the target.

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

#TemplateBinding
The **TemplateBinding** is owned by the source of the link. The source must be a **TemplateableElement** and the target a **TemplateableSignature**.
Technically, we create a **TemplateBinding** between a **TemplateableElement** and an element owning a **TemplateableSignature**.

#Usage
The **Usage** is owned by the nearest parent Package of the source.
