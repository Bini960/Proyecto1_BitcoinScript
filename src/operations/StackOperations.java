package operations;

import crypto.CryptoMock;
import java.util.ArrayDeque;

public class StackOperations implements Opcode {

    @Override
    public void execute(String opcode, ArrayDeque<String> stack, CryptoMock crypto) {
        
        // Evita errores si la pila esta vacia antes de operar
        if (stack.isEmpty()) {
            return; 
        }

        // Selecciona la accion segun la palabra leida
        switch (opcode) {
            case "OP_DUP":
                // Obtiene el valor de la cima sin sacarlo
                String top = stack.peek();
                // Inserta una copia exacta en la cima
                stack.push(top);
                break;

            case "OP_DROP":
                // Elimina permanentemente el valor en la cima
                stack.pop();
                break;

            case "OP_SWAP":
                // Verifica que existan al menos dos elementos para intercambiar
                if (stack.size() >= 2) {
                    // Extrae los dos primeros elementos
                    String first = stack.pop();
                    String second = stack.pop();
                    
                    // Los inserta en orden inverso
                    stack.push(first);
                    stack.push(second);
                }
                break;

            case "OP_OVER":
                // Verifica que existan al menos dos elementos
                if (stack.size() >= 2) {
                    // Extrae temporalmente el primero para ver el segundo
                    String tempFirst = stack.pop();
                    String target = stack.peek();
                    
                    // Regresa el primero a su lugar
                    stack.push(tempFirst);
                    // Inserta la copia del segundo elemento en la cima
                    stack.push(target);
                }
                break;
                
            default:
                // Ignora operaciones no reconocidas por esta clase
                break;
        }
    }
}