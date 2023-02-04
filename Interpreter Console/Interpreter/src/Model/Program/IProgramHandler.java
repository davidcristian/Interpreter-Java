package Model.Program;

import Model.Expression.*;
import Model.Statement.*;
import Model.Type.BoolType;
import Model.Type.IntType;
import Model.Type.ReferenceType;
import Model.Type.StringType;
import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.StringValue;

public interface IProgramHandler {
    IStatement[] PROGRAMS = new IStatement[] {
            // A2
            new CompoundStatement(new VariableDeclarationStatement("v", new IntType()), new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(2))), new PrintStatement(new VariableExpression("v")))),
            new CompoundStatement(new VariableDeclarationStatement("a", new IntType()), new CompoundStatement(new VariableDeclarationStatement("b", new IntType()), new CompoundStatement(new AssignmentStatement("a", new ArithmeticExpression('+', new ValueExpression(new IntValue(2)), new ArithmeticExpression('*', new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5))))), new CompoundStatement(new AssignmentStatement("b", new ArithmeticExpression('+', new VariableExpression("a"), new ValueExpression(new IntValue(1)))), new PrintStatement(new VariableExpression("b")))))),
            new CompoundStatement(new VariableDeclarationStatement("a", new BoolType()), new CompoundStatement(new VariableDeclarationStatement("v", new IntType()), new CompoundStatement(new AssignmentStatement("a", new ValueExpression(new BoolValue(true))), new CompoundStatement(new IfStatement(new VariableExpression("a"), new AssignmentStatement("v", new ValueExpression(new IntValue(2))), new AssignmentStatement("v", new ValueExpression(new IntValue(3)))), new PrintStatement(new VariableExpression("v")))))),

            // A3
            new CompoundStatement(new VariableDeclarationStatement("varf", new StringType()), new CompoundStatement(new AssignmentStatement("varf", new ValueExpression(new StringValue("resources/test.in"))), new CompoundStatement(new OpenFileStatement(new VariableExpression("varf")), new CompoundStatement(new VariableDeclarationStatement("varc", new IntType()), new CompoundStatement(new ReadFileStatement(new VariableExpression("varf"), "varc"), new CompoundStatement(new PrintStatement(new VariableExpression("varc")), new CompoundStatement(new ReadFileStatement(new VariableExpression("varf"), "varc"), new CompoundStatement(new PrintStatement(new VariableExpression("varc")), new CloseFileStatement(new VariableExpression("varf")))))))))),
            // separate relational expression examples
            new CompoundStatement(new VariableDeclarationStatement("a", new IntType()), new CompoundStatement(new VariableDeclarationStatement("b", new IntType()), new CompoundStatement(new AssignmentStatement("a", new ValueExpression(new IntValue(1))), new CompoundStatement(new IfStatement(new RelationalExpression(">", new VariableExpression("a"), new ValueExpression(new IntValue(1))), new AssignmentStatement("b", new ValueExpression(new IntValue(200))), new AssignmentStatement("b", new ValueExpression(new IntValue(100)))), new PrintStatement(new VariableExpression("b")))))),
            new CompoundStatement(new VariableDeclarationStatement("a", new IntType()), new CompoundStatement(new VariableDeclarationStatement("b", new IntType()), new CompoundStatement(new AssignmentStatement("a", new ValueExpression(new IntValue(2))), new CompoundStatement(new IfStatement(new RelationalExpression(">", new VariableExpression("a"), new ValueExpression(new IntValue(1))), new AssignmentStatement("b", new ValueExpression(new IntValue(200))), new AssignmentStatement("b", new ValueExpression(new IntValue(100)))), new PrintStatement(new VariableExpression("b")))))),

            // A4
            new CompoundStatement(new VariableDeclarationStatement("v", new ReferenceType(new IntType())), new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))), new CompoundStatement(new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))), new CompoundStatement(new NewStatement("a", new VariableExpression("v")), new CompoundStatement(new PrintStatement(new VariableExpression("v")), new PrintStatement(new VariableExpression("a"))))))),
            new CompoundStatement(new VariableDeclarationStatement("v", new ReferenceType(new IntType())), new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))), new CompoundStatement(new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))), new CompoundStatement(new NewStatement("a", new VariableExpression("v")), new CompoundStatement(new PrintStatement(new HeapReadExpression(new VariableExpression("v"))), new PrintStatement(new ArithmeticExpression('+', new HeapReadExpression(new HeapReadExpression(new VariableExpression("a"))), new ValueExpression(new IntValue(5))))))))),
            new CompoundStatement(new VariableDeclarationStatement("v", new ReferenceType(new IntType())), new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))), new CompoundStatement(new PrintStatement(new HeapReadExpression(new VariableExpression("v"))), new CompoundStatement(new HeapWriteStatement("v", new ValueExpression(new IntValue(30))), new PrintStatement(new ArithmeticExpression('+', new HeapReadExpression(new VariableExpression("v")), new ValueExpression(new IntValue(5)))))))),
            // 12 (below) should error with unsafe gc
            new CompoundStatement(new VariableDeclarationStatement("v", new ReferenceType(new IntType())), new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))), new CompoundStatement(new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))), new CompoundStatement(new NewStatement("a", new VariableExpression("v")), new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(30))), new PrintStatement(new HeapReadExpression(new HeapReadExpression(new VariableExpression("a"))))))))),
            new CompoundStatement(new VariableDeclarationStatement("v", new IntType()), new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(4))), new CompoundStatement(new WhileStatement(new RelationalExpression(">", new VariableExpression("v"), new ValueExpression(new IntValue(0))), new CompoundStatement(new PrintStatement(new VariableExpression("v")), new AssignmentStatement("v", new ArithmeticExpression('-', new VariableExpression("v"), new ValueExpression(new IntValue(1)))))), new PrintStatement(new VariableExpression("v"))))),

            // A5
            new CompoundStatement(new VariableDeclarationStatement("v", new IntType()), new CompoundStatement(new VariableDeclarationStatement("a", new ReferenceType(new IntType())), new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(10))), new CompoundStatement(new NewStatement("a", new ValueExpression(new IntValue(22))), new CompoundStatement(new ForkStatement(new CompoundStatement(new HeapWriteStatement("a", new ValueExpression(new IntValue(30))), new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(32))), new CompoundStatement(new PrintStatement(new VariableExpression("v")), new PrintStatement(new HeapReadExpression(new VariableExpression("a"))))))), new CompoundStatement(new PrintStatement(new VariableExpression("v")), new PrintStatement(new HeapReadExpression(new VariableExpression("a"))))))))),

            // A6
            // separate type checker examples (will not get added to menu)
            // assignment statement
            new CompoundStatement(new VariableDeclarationStatement("v", new BoolType()), new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(2))), new PrintStatement(new VariableExpression("v")))),
            // relational expression
            new CompoundStatement(new VariableDeclarationStatement("a", new BoolType()), new CompoundStatement(new VariableDeclarationStatement("b", new IntType()), new CompoundStatement(new AssignmentStatement("a", new ValueExpression(new BoolValue(true))), new CompoundStatement(new IfStatement(new RelationalExpression(">", new VariableExpression("a"), new ValueExpression(new IntValue(1))), new AssignmentStatement("b", new ValueExpression(new IntValue(200))), new AssignmentStatement("b", new ValueExpression(new IntValue(100)))), new PrintStatement(new VariableExpression("b")))))),
    };
}
