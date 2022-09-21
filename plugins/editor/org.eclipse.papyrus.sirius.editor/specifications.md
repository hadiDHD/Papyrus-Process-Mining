#Sirius Integration
 * The Sirius Diagramm must be integrated into the Papyrus SashEditor (like it is done for Papyrus GMF Diagram).
 * The Sirius Diagram comes with a Session system. This Session must always be available once Papyrus finished its starting process.
 * The Sirius session is required to create Diagram with aql precondition. So we need to have this Session before the Sirius diagram creation (see bug 580739). Here are a non exhaustive list of cases in which the session is required:
      * Creating a new Papyrus project without selecting diagram to diagram -> the Sirius Session must be initialized.
      * Creating a new Papyrus model inside an existing project, without selecting diagram to diagram -> the Sirius Session must be initialized.
      * Creating a new Papyrus project, selecting Papyrus GMF Diagram -> the Sirius Session must be initialized.
      * Creating a new Papyrus model inside an existing project, selecting Papyrus GMF Diagram -> the Sirius Session must be initialized.
      * Creating a new Papyrus project, selecting Papyrus Sirius Diagram -> the Sirius Session must be initialized.
      * Creating a new Papyrus model inside an existing project, selecting Papyrus Sirius Diagram -> the Sirius Session must be initialized.
      * Opening an existing Papyrus model with no diagrams -> the Sirius Session must be initialized.
      * Opening an existing Papyrus model with GMF diagrams -> the Sirius Session must be initialized.
      * Opening an existing Papyrus model with Sirius diagrams -> the Sirius Session must be initialized.
 * Sirius Diagrams are stored inside an aird file. This file must be created and saved in the same times than others Papyrus file (uml/notation/di).
