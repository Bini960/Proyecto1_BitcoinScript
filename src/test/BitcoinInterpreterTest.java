package test;

import interpreter.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayDeque;

public class BitcoinInterpreterTest {

    // ====================================
    // PRUEBAS DE OPERACIONES MATEMÁTICAS
    // ====================================
    @Test
    public void testSumaBasica() throws Exception {
        BitcoinInterpreter interpreter = new BitcoinInterpreter();
        // Script: mete 5, mete 7 (5 + 7 = 12)
        // Como no hay nada más, la cima final es 12
        String[] script = {"5", "7", "OP_ADD"};
        
        boolean resultado = interpreter.evaluate(script, null, false);
        
        // Se verifica que la validación sea exitosa
        assertTrue("La transacción debería ser válida", resultado);
        
        // Se verifica que el resultado exacto en la pila sea 12
        ArrayDeque<String> stack = interpreter.getStack();
        assertEquals("La suma de 5 y 7 debe ser 12", "12", stack.peek());
    }

    @Test
    public void testRestaYComparacion() throws Exception {
        BitcoinInterpreter interpreter = new BitcoinInterpreter();
        // Script: 10 - 4 = 6. Luego evalúa si 6 es mayor que 5 (OP_GREATERTHAN).
        // 6 > 5 es verdadero, por lo que deja un 1 en la pila.
        String[] script = {"10", "4", "OP_SUB", "5", "OP_GREATERTHAN"};
        
        boolean resultado = interpreter.evaluate(script, null, false);
        
        assertTrue(resultado);
        assertEquals("1", interpreter.getStack().peek());
    }

    // ==============================   
    // PRUEBAS DE OPERACIONES LÓGICAS
    // ==============================
    @Test
    public void testIgualdadVerdadera() throws Exception {
        BitcoinInterpreter interpreter = new BitcoinInterpreter();
        // Script: mete "Hola", mete "Hola", evalúa si son iguales
        String[] script = {"Hola", "Hola", "OP_EQUAL"};
        
        boolean resultado = interpreter.evaluate(script, null, false);
        
        assertTrue(resultado);
        assertEquals("1", interpreter.getStack().peek());
    }

    @Test
    public void testOperadorNot() throws Exception {
        BitcoinInterpreter interpreter = new BitcoinInterpreter();
        // Script: Empuja 0 que es falso, aplica OP_NOT, lo vuelve verdadero 1
        String[] script = {"0", "OP_NOT"};
        
        boolean resultado = interpreter.evaluate(script, null, false);
        
        assertTrue(resultado);
        assertEquals("1", interpreter.getStack().peek());
    }

}