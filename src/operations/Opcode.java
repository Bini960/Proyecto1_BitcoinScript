package operations;

import crypto.CryptoMock;
import java.util.ArrayDeque;

public interface Opcode {
    
    // Ejecuta la instruccion correspondiente modificando la pila
    void execute(String opcode, ArrayDeque<String> stack, CryptoMock crypto);
    
}