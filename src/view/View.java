package view;

import java.util.ArrayDeque;

public class View {

    // Imprime el estado de la pila paso a paso
    public void printTrace(String opcode, ArrayDeque<String> stack) {
        System.out.println("Instruccion: " + opcode);
        System.out.println("Pila actual: " + stack.toString());
        System.out.println("-------------------------");
    }

    // Muestra el veredicto final de la transaccion
    public void printResult(boolean isValid) {
        if (isValid) {
            System.out.println("RESULTADO: Transaccion Valida (Exito)");
        } else {
            System.out.println("RESULTADO: Transaccion Invalida (Fallo)");
        }
    }

    // Muestra mensajes de error en la consola
    public void printError(String message) {
        System.out.println("ERROR: " + message);
    }
}