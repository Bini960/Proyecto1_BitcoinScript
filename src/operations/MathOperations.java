package operations;

import crypto.CryptoMock;
import java.util.ArrayDeque;

public class MathOperations implements Opcode {

    @Override
    public void execute(String opcode, ArrayDeque<String> stack, CryptoMock crypto) {
        // Todas las operaciones matemáticas básicas requieren 2 operandos
        if (stack.size() < 2) {
            throw new RuntimeException("Elementos insuficientes para operación matemática: " + opcode);
        }

        // El primer elemento en salir es el segundo operando B
        // Se convierte el String a número entero para poder hacer matemáticas
        int b = Integer.parseInt(stack.pop());
        // El segundo elemento en salir es el primer operando A
        int a = Integer.parseInt(stack.pop());

        // Se evalúa la instrucción matemática
        switch (opcode) {
            case "OP_ADD":
                // se suma (a + b), se transforma de nuevo a texto y se coloca en la pila
                stack.push(String.valueOf(a + b));
                break;
                
            case "OP_SUB":
                stack.push(String.valueOf(a - b));
                break;
                
            case "OP_NUMEQUALVERIFY":
                // Verifica si los dos números son iguales. Si no lo son la transacción falla
                if (a != b) {
                    throw new RuntimeException("Fallo numérico en OP_NUMEQUALVERIFY");
                }
                break;
                
            case "OP_LESSTHAN":
                // Verifica si 'a' es menor que 'b'. Si es cierto mete 1, si no, mete 0
                stack.push((a < b) ? "1" : "0");
                break;
                
            case "OP_GREATERTHAN":
                stack.push((a > b) ? "1" : "0");
                break;
                
            case "OP_LESSTHANOREQUAL":
                stack.push((a <= b) ? "1" : "0");
                break;
                
            case "OP_GREATERTHANOREQUAL":
                stack.push((a >= b) ? "1" : "0");
                break;
                
            default:
                break;
        }
    }
}