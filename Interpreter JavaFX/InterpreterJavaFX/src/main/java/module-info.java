module com.davidcristian.interpreter {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.davidcristian.interpreter to javafx.fxml;
    exports com.davidcristian.interpreter;

    // CUSTOM
    opens com.davidcristian.interpreter.GUI to javafx.fxml;
    exports com.davidcristian.interpreter.GUI;


    opens com.davidcristian.interpreter.Controller to javafx.fxml;
    exports com.davidcristian.interpreter.Controller;

    opens com.davidcristian.interpreter.Repository to javafx.fxml;
    exports com.davidcristian.interpreter.Repository;

    opens com.davidcristian.interpreter.Exceptions to javafx.fxml;
    exports com.davidcristian.interpreter.Exceptions;


    opens com.davidcristian.interpreter.Model.ADT.Dictionary to javafx.fxml;
    exports com.davidcristian.interpreter.Model.ADT.Dictionary;

    opens com.davidcristian.interpreter.Model.ADT.Heap to javafx.fxml;
    exports com.davidcristian.interpreter.Model.ADT.Heap;

    opens com.davidcristian.interpreter.Model.ADT.List to javafx.fxml;
    exports com.davidcristian.interpreter.Model.ADT.List;

    opens com.davidcristian.interpreter.Model.ADT.Stack to javafx.fxml;
    exports com.davidcristian.interpreter.Model.ADT.Stack;


    opens com.davidcristian.interpreter.Model.Expression to javafx.fxml;
    exports com.davidcristian.interpreter.Model.Expression;

    opens com.davidcristian.interpreter.Model.Program to javafx.fxml;
    exports com.davidcristian.interpreter.Model.Program;

    opens com.davidcristian.interpreter.Model.Statement to javafx.fxml;
    exports com.davidcristian.interpreter.Model.Statement;

    opens com.davidcristian.interpreter.Model.Type to javafx.fxml;
    exports com.davidcristian.interpreter.Model.Type;

    opens com.davidcristian.interpreter.Model.Value to javafx.fxml;
    exports com.davidcristian.interpreter.Model.Value;
}