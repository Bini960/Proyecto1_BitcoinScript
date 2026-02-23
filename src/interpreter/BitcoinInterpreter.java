package interpreter;

import crypto.CryptoMock;
import java.util.ArrayDeque;
import operations.StackOperations;

public class BitcoinInterpreter {

    private ArrayDeque<String> stack;
    private CryptoMock crypto;
    private StackOperations stackOps;

    public BitcoinInterpreter() {
        // Inicializa la memoria principal y el simulador criptografico
        this.stack = new ArrayDeque<>();
        this.crypto = new CryptoMock();
        this.stackOps = new StackOperations();
    }

    // Devuelve la pila actual
    public ArrayDeque<String> getStack() {
        return this.stack;
    }

    // Procesa el script completo palabra por palabra
    public boolean evaluate(String[] scriptTokens, view.View view, boolean trace) {
        // Controla si las instrucciones actuales deben ejecutarse o ignorarse
        boolean ejecutando = true;

        for (String token : scriptTokens) {
            
            // Evalua el control de flujo
            if (token.equals("OP_IF")) {
                if (stack.isEmpty()) return false;
                String top = stack.pop();
                ejecutando = !top.equals("0");
                continue;
            } else if (token.equals("OP_ELSE")) {
                ejecutando = !ejecutando;
                continue;
            } else if (token.equals("OP_ENDIF")) {
                ejecutando = true;
                continue;
            }

            // Ignora la instruccion si esta dentro de un IF falso
            if (!ejecutando) {
                continue;
            }

            // Clasifica y ejecuta la operacion
            if (token.startsWith("OP_")) {
                
                // Enruta a operaciones de pila
                if (token.equals("OP_DUP") || token.equals("OP_DROP") || token.equals("OP_SWAP") || token.equals("OP_OVER")) {
                    stackOps.execute(token, stack, crypto);
                } 
                // Detiene la ejecucion si la verificacion falla
                else if (token.equals("OP_VERIFY")) {
                    if (stack.isEmpty() || stack.pop().equals("0")) {
                        return false;
                    }
                }
                // LOGIC Y MATH OPERATIONS
                
            } else {
                // Empuja el dato literal a la pila
                stack.push(token);
            }

            // Imprime el rastro si el modo trace esta activado
            if (trace && view != null) {
                view.printTrace(token, stack);
            }
        }

        // Verifica la regla final de validez
        if (stack.isEmpty()) {
            return false;
        }
        return !stack.peek().equals("0");
    }
}