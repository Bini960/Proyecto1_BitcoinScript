package operations;

import crypto.CryptoMock;
import java.util.ArrayDeque;

/**
 * Interfaz que define el contrato para todas las operaciones del interprete.
 */
public interface Opcode {
    
    /**
     * Ejecuta una instruccion sobre la pila
     *
     * @param opcode La operacion especifica a ejecutar.
     * @param stack  La estructura de memoria LIFO (pila) actual.
     * @param crypto La instancia criptografica para validaciones.
     */
    void execute(String opcode, ArrayDeque<String> stack, CryptoMock crypto);
    
}