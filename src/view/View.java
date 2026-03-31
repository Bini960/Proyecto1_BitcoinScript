package view;

import java.util.ArrayDeque;

/**
 * Clase responsable de la presentacion visual del interprete en la consola
 * Separa la logica principal de la interfaz de usuario
 */
public class View {

    /**
     * Imprime en consola la instruccion procesada y el estado de la memoria en ese momento.
     *
     * @param opcode La palabra o token que se acaba de evaluar.
     * @param stack  La pila en su estado resultante despues de la evaluacion.
     */
    public void printTrace(String opcode, ArrayDeque<String> stack) {
        System.out.println("Instruccion: " + opcode);
        System.out.println("Pila actual: " + stack.toString());
        System.out.println("-------------------------");
    }

    /**
     * Muestra el mensaje final indicando si la transaccion cumplio las condiciones o no.
     *
     * @param isValid Booleano que dicta el exito o fracaso final del script.
     */
    public void printResult(boolean isValid) {
        if (isValid) {
            System.out.println("RESULTADO: Transaccion Valida");
        } else {
            System.out.println("RESULTADO: Transaccion Invalida");
        }
    }

    /**
     * Imprime un mensaje especifico cuando el interprete atrapa un error de ejecucion.
     *
     * @param message El detalle de la excepcion atrapada.
     */
    public void printError(String message) {
        System.out.println("ERROR: " + message);
    }
}