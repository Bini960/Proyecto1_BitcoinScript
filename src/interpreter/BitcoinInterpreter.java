package interpreter;

import crypto.CryptoMock;
import java.util.ArrayDeque;
import java.util.Stack;
import operations.CryptoOperations;
import operations.LogicOperations;
import operations.MathOperations;
import operations.StackOperations;

/**
 * Interprete de Bitcoin Script.
 * Evalua secuencias de tokens utilizando un enfoque basado en pila.
 */
public class BitcoinInterpreter {

    // ATRIBUTOS 
    // ArrayDeque ofrece mejor rendimiento que Stack
    private ArrayDeque<String> stack;
    
    // El servicio de simulaciones criptográficas
    private CryptoMock crypto;
    
    // Instancias de las familias de operaciones
    private StackOperations stackOps;
    private LogicOperations logicOps; 
    private MathOperations mathOps;   
    private CryptoOperations cryptoOps; 

    /**
     * Constructor del interprete
     * Inicializa la memoria principal y las distintas operaciones.
     */
    public BitcoinInterpreter() {
        // se inicializan todos los objetos en memoria al crear el intérprete
        this.stack = new ArrayDeque<>();
        this.crypto = new CryptoMock();
        this.stackOps = new StackOperations();
        this.logicOps = new LogicOperations(); 
        this.mathOps = new MathOperations();   
        this.cryptoOps = new CryptoOperations();
    }

    /**
     * Obtiene la pila principal de la transaccion.
     *
     * @return El ArrayDeque que representa la pila de datos
     */
    // Devuelve la referencia a la pila para ver su estado actual
    public ArrayDeque<String> getStack() {
        return this.stack;
    }

    /**
     * Evalua un script completo de Bitcoin.
     *
     * @param scriptTokens Arreglo de cadenas que representan las instrucciones y datos.
     * @param view         Instancia de la vista para imprimir el rastro de la ejecucion.
     * @param trace        Bandera para activar la impresion paso a paso.
     * @return true si la transaccion es valida, false en caso contrario.
     * @throws Exception Si ocurre un error fatal durante la evaluacion de una operacion.
     */
    public boolean evaluate(String[] scriptTokens, view.View view, boolean trace) throws Exception {
        
        // Pila auxiliar para manejar bloques OP_IF anidados
        Stack<Boolean> conditionStack = new Stack<>();
        
        // se recorre el script completo de izquierda a derecha
        for (String token : scriptTokens) {
            
            
            
            // LÓGICA DE CONTROL DE FLUJO (IF/ELSE)
            // Manejo de control de flujo
            if (token.equals("OP_IF")) {
                if (stack.isEmpty()) {
                    throw new RuntimeException("Pila vacía en OP_IF");
                }

                boolean condition = !stack.pop().equals("0");

                if (!conditionStack.isEmpty() && !conditionStack.peek()) {
                    conditionStack.push(false);
                } else {
                    conditionStack.push(condition);
                }
                continue;

            } else if (token.equals("OP_ELSE")) {
                if (conditionStack.isEmpty()) {
                    throw new RuntimeException("OP_ELSE sin OP_IF");
                }

                boolean current = conditionStack.pop();
                
            } else if (token.equals("OP_ELSE")) {
                if (!conditionStack.isEmpty()) {
                    boolean current = conditionStack.pop();
                    conditionStack.push(!current);
                }
                continue;
                
            } else if (token.equals("OP_ENDIF")) {
                if (!conditionStack.isEmpty()) {
                    conditionStack.pop();
                }
                continue;
            }

            // IIgnora cualquier operación si estamos dentro de un bloque OP_IF que ya ha evaluado a falso
            if (!conditionStack.isEmpty() && !conditionStack.peek()) {
                continue; 
            }

            // Enruta la ejecución según el tipo de token (OP_ vs dato literal)
            if (token.startsWith("OP_")) {
                
                // Se enruta hacia las Operaciones de Pila
                if (token.equals("OP_DUP") || token.equals("OP_DROP") || token.equals("OP_SWAP") || token.equals("OP_OVER")) {
                    stackOps.execute(token, stack, crypto);
                    
                // Se enruta hacia las Operaciones Lógicas
                } else if (token.equals("OP_EQUAL") || token.equals("OP_EQUALVERIFY") || token.equals("OP_NOT") || token.equals("OP_BOOLAND") || token.equals("OP_BOOLOR")) {
                    logicOps.execute(token, stack, crypto);
                    
                // Se enruta hacia las Operaciones Matemáticas
                } else if (token.equals("OP_ADD") || token.equals("OP_SUB") || token.equals("OP_NUMEQUALVERIFY") || token.equals("OP_LESSTHAN") || token.equals("OP_GREATERTHAN") || token.equals("OP_LESSTHANOREQUAL") || token.equals("OP_GREATERTHANOREQUAL")) {
                    mathOps.execute(token, stack, crypto);
                    
                // Se enruta hacia las Operaciones Criptográficas (Agregado OP_HASH256)
                } else if (token.equals("OP_HASH160") || token.equals("OP_SHA256") || token.equals("OP_HASH256") || token.equals("OP_CHECKSIG") || token.equals("OP_CHECKSIGVERIFY")) {
                    cryptoOps.execute(token, stack, crypto);
                    
                // Casos especiales de control directo
                } else if (token.equals("OP_VERIFY")) {
                    if (stack.isEmpty() || stack.pop().equals("0")) {
                        throw new RuntimeException("La validación falló en la instrucción OP_VERIFY");
                    }
                } else if (token.equals("OP_RETURN")) {
                    throw new RuntimeException("Ejecución abortada por instrucción OP_RETURN");
                    
                } else if (token.matches("OP_\\d+")) {
                    stack.push(token.replace("OP_", ""));
                    
                } else if (token.equals("OP_FALSE")) {
                    stack.push("0");
                }
                
            } else {
                // Si no es un OP_, es un dato literal y se inserta a la pila
                stack.push(token);
            }

            // Modo que imprime el token actual y el estado de la pila después de cada operación
            if (trace && view != null) {
                view.printTrace(token, stack);
            }
        }

        if (stack.isEmpty()) {
            return false;
        }
        
        return !stack.peek().equals("0");
    }
}