package test;

import interpreter.BitcoinInterpreter;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitcoinInterpreterTest {

    // ==============================
    // COBERTURA OPERACIONES DE PILA
    // ==============================
    @Test
    public void testOperacionesDePilaBasicas() throws Exception {
        BitcoinInterpreter interpreter = new BitcoinInterpreter();
        // Simulamos un flujo de manipulación de pila
        // A -> [A]
        // OP_DUP -> [A, A]
        // B -> [B, A, A]
        // OP_SWAP -> Intercambia los dos de arriba, [A, B, A] 
        // OP_DROP -> Elimina la cima [B, A]
        // Al final, el elemento que queda en la cima es B
        String[] script = {"A", "OP_DUP", "B", "OP_SWAP", "OP_DROP"};
        
        boolean resultado = interpreter.evaluate(script, null, false);
        
        // Verificamos que no falló y que el valor final es el que calculamos
        assertTrue("La transacción debe completarse con éxito", resultado);
        assertEquals("El elemento final en la pila debe ser B", "B", interpreter.getStack().peek());
    }

    @Test
    public void testOperacionOver() throws Exception {
        BitcoinInterpreter interpreter = new BitcoinInterpreter();
        // Probamos OP_OVER, que copia el segundo elemento y lo pone en la cima
        // Pila resultante esperada: 1, 2, 1. La cima será 1
        String[] script = {"1", "2", "OP_OVER"};
        
        boolean resultado = interpreter.evaluate(script, null, false);
        
        assertTrue(resultado);
        assertEquals("La cima debe ser 1 tras ejecutar OP_OVER", "1", interpreter.getStack().peek());
    }

    // =================================
    // COBERTURA OPERACIONES MATEMÁTICAS
    // =================================
    @Test
    public void testMatematicasBasicas() throws Exception {
        BitcoinInterpreter interpreter = new BitcoinInterpreter();
        // Hacemos una suma simple y luego evaluamos si el resultado es menor o igual a 20.
        // Como es verdadero, debería dejar un "1" en la pila.
        String[] script = {"10", "5", "OP_ADD", "20", "OP_LESSTHANOREQUAL"};
        
        boolean resultado = interpreter.evaluate(script, null, false);
        
        assertTrue(resultado);
        assertEquals("Debería dejar un 1 (true) en la pila", "1", interpreter.getStack().peek());
    }

    @Test
    public void testNumEqualVerifyExitoso() throws Exception {
        BitcoinInterpreter interpreter = new BitcoinInterpreter();
        // Verificamos que OP_NUMEQUALVERIFY no aborte la ejecución si los números son iguales
        // Como esta instrucción consume los números y no deja nada, metemos un 1 al final para que sea válida.
        String[] script = {"8", "8", "OP_NUMEQUALVERIFY", "1"};
        
        boolean resultado = interpreter.evaluate(script, null, false);
        
        assertTrue("La transacción debe pasar la verificación numérica correctamente", resultado);
    }

    // ==============================
    // COBERTURA OPERACIONES LÓGICAS
    // ==============================
    @Test
    public void testOperadoresBooleanos() throws Exception {
        BitcoinInterpreter interpreter = new BitcoinInterpreter();
        // Probamos las compuertas lógicas OR y AND en una sola pasada.
        // (1 OR 0) da 1, luego (1 AND 1) da 1.
        String[] script = {"1", "0", "OP_BOOLOR", "1", "OP_BOOLAND"};
        
        boolean resultado = interpreter.evaluate(script, null, false);
        
        assertTrue(resultado);
        assertEquals("El resultado de la lógica booleana debe ser 1", "1", interpreter.getStack().peek());
    }

    // ====================================
    // COBERTURA OPERACIONES CRIPTOGRÁFICAS
    // ====================================
    @Test
    public void testVerificacionDeFirmaSimulada() throws Exception {
        BitcoinInterpreter interpreter = new BitcoinInterpreter();
        // Validamos que nuestra simulación criptográfica (CryptoMock) funciona en el intérprete
        String[] script = {"firma_valida", "llave_publica_123", "OP_CHECKSIG"};
        
        boolean resultado = interpreter.evaluate(script, null, false);
        
        assertTrue("Una firma válida simulada debe retornar éxito", resultado);
        assertEquals("1", interpreter.getStack().peek());
    }

    @Test
    public void testHashingSimulado() throws Exception {
        BitcoinInterpreter interpreter = new BitcoinInterpreter();
        // Verificamos que el hashing transforma el dato correctamente usando nuestro servicio mock
        String[] script = {"datos_secretos", "OP_HASH160"};
        
        interpreter.evaluate(script, null, false);
        
        assertEquals("hash160_datos_secretos", interpreter.getStack().peek());
    }

    // ========================
    // CONDICIONALES ANIDADOS
    // ========================
    @Test
    public void testCondicionalIfElseBasico() throws Exception {
        BitcoinInterpreter interpreter = new BitcoinInterpreter();
        // Probamos el flujo de control básico. 
        // Como le pasamos un "0" falso al OP_IF, debe ignorar la primera parte y ejecutar el bloque ELSE.
        String[] script = {"0", "OP_IF", "Verdadero", "OP_ELSE", "Falso", "OP_ENDIF"};
        
        boolean resultado = interpreter.evaluate(script, null, false);
        
        assertTrue(resultado);
        assertEquals("Al ser la condición inicial 0, la cima debe ser el texto del bloque ELSE", "Falso", interpreter.getStack().peek());
    }

    @Test
    public void testCondicionalesAnidados() throws Exception {
        BitcoinInterpreter interpreter = new BitcoinInterpreter();
        // Prueba avanzada, un bloque IF dentro de otro bloque IF
        // El primer IF es verdadero, entra. El segundo IF es falso, salta a su ELSE interno.
        // Por lo tanto, el resultado esperado en la pila es "RamaB".
        String[] script = {"1", "OP_IF", "0", "OP_IF", "RamaA", "OP_ELSE", "RamaB", "OP_ENDIF", "OP_ENDIF"};
        
        boolean resultado = interpreter.evaluate(script, null, false);
        
        assertTrue("La evaluación de condicionales anidados no debe fallar", resultado);
        assertEquals("Debe resolver correctamente la rama interna y empujar 'RamaB'", "RamaB", interpreter.getStack().peek());
    }
}