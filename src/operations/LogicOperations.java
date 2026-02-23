package operations;

import crypto.CryptoMock;
import java.util.ArrayDeque;

public class LogicOperations implements Opcode {

    @Override
    public void execute(String opcode, ArrayDeque<String> stack, CryptoMock crypto) {
        // Validar que haya suficientes elementos para operar
        // La mayoría de operaciones lógicas necesitan 2 elementos, excepto OP_NOT que solo necesita 1
        if (stack.isEmpty() || (stack.size() < 2 && !opcode.equals("OP_NOT"))) {
            // Si no hay suficientes elementos, se lanza un error que el intérprete atrapará
            throw new RuntimeException("Elementos insuficientes en la pila para " + opcode);
        }

        // se evalúa qué instrucción específica mandó el intérprete
        switch (opcode) {
            case "OP_EQUAL":
                String val1 = stack.pop();
                String val2 = stack.pop();
                // Si son iguales, se mete un 1 de verdadero, si no, un 0 de falso
                // se usa .equals() porque se comparan Strings
                stack.push(val1.equals(val2) ? "1" : "0");
                break;

            case "OP_EQUALVERIFY":
                String v1 = stack.pop();
                String v2 = stack.pop();
                // Falla la instrucción si no son iguales lanzando una excepción
                if (!v1.equals(v2)) {
                    throw new RuntimeException("Fallo de verificación en OP_EQUALVERIFY");
                }
                break;

            case "OP_NOT":
                String top = stack.pop();
                // Si el elemento es un 0 de falso se invierte empujando un 1 de verdadero
                // Si es cualquier otra cosa verdadera, se mete un 0
                stack.push(top.equals("0") ? "1" : "0");
                break;

            case "OP_BOOLAND":
                boolean b1 = !stack.pop().equals("0");
                boolean b2 = !stack.pop().equals("0");

                // ambos deben ser verdaderos para meter un 1
                stack.push((b1 && b2) ? "1" : "0");
                break;

            case "OP_BOOLOR":
                boolean or1 = !stack.pop().equals("0");
                boolean or2 = !stack.pop().equals("0");

                // con que uno sea verdadero, se mete un 1
                stack.push((or1 || or2) ? "1" : "0");
                break;
                
            default:
                break;
        }
    }
}