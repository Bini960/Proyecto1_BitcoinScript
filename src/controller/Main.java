package controller;

import interpreter.BitcoinInterpreter;
import view.View;
/**
 * Clase principal que controla la ejecucion del Interprete de Bitcoin
 */
public class Main {

    /**
     * Punto de entrada principal del programa.
     * * @param args Argumentos de linea de comandos (ej. --trace).
     */
    public static void main(String[] args) {

        BitcoinInterpreter interpreter = new BitcoinInterpreter();

        // 🔹 Script de demostración con OP_IF
        String scriptDemoIf = "1 OP_IF dato_verdadero OP_ELSE dato_falso OP_ENDIF";

        // Convertir el String a arreglo (como lo espera evaluate)
        String[] scriptArray = scriptDemoIf.split(" ");

        // Ejecutar con trace activado (true)
        View view = new View();
        boolean result = interpreter.evaluate(scriptArray, view, true);

        // Mostrar resultado final
        System.out.println("Resultado final del stack: " + interpreter.getStack());
        System.out.println("Evaluación final: " + result);



        // Imprime los mensajes de bienvenida en la consola
        System.out.println("Iniciando Interprete de Bitcoin Script...");

       // Deteccion automatica del argumento --trace desde la consola
        boolean trace = false;
        for (String arg : args) {
            if (arg.equals("--trace")) {
                trace = true;
                System.out.println("[Argumento --trace]\n");
                break;
            }
        }

        // DEMOSTRACION 1: Transaccion P2PKH Exitosa
        System.out.println("--Ejecutando transaccion P2PKH de prueba (Exitosa)");
        
        // Define el script de desbloqueo (scriptSig) que contiene la firma y la llave
        String scriptSig = "firma_valida llave_publica_andres";

        // Define el script de bloqueo (scriptPubKey) con la estructura estandar P2PKH
        String scriptPubKey = "OP_DUP OP_HASH160 hash160_llave_publica_andres OP_EQUALVERIFY OP_CHECKSIG";

        // Inicia el proceso de ejecucion de la transaccion
        run(scriptSig, scriptPubKey, trace);


        // DEMOSTRACION 2: Transaccion P2PKH Fallida
        System.out.println("\n>>> --Ejecutando transaccion P2PKH de prueba (Firma Incorrecta)");
        
        // Define un script de desbloqueo con una firma erronea para provocar el fallo
        String scriptSigIncorrecto = "firma_falsa llave_publica_andres";

        // Inicia el proceso usando el script incorrecto pero contra el mismo candado original
        run(scriptSigIncorrecto, scriptPubKey, trace);
    }

    /**
     * Controla la preparacion y validacion de los scripts.
     *
     * @param scriptSig    El script de desbloqueo (credenciales del usuario).
     * @param scriptPubKey El script de bloqueo (condiciones originales).
     * @param trace        Indica si se debe imprimir el rastro paso a paso.
     */
    public static void run(String scriptSig, String scriptPubKey, boolean trace) {

        // Instancia el motor del interprete y la clase encargada de imprimir resultados
        BitcoinInterpreter interpreter = new BitcoinInterpreter();
        View view = new View();

        // Concatena ambos scripts simulando el orden de lectura de la red Bitcoin
        String scriptCompleto = scriptSig + " " + scriptPubKey;
        
        // Separa la cadena de texto en un arreglo de instrucciones individuales
        String[] tokens = scriptCompleto.split(" ");

        try {

            // Ejecuta el ciclo de lectura y guarda el veredicto final
            boolean isValid = interpreter.evaluate(tokens, view, trace);
            
            // Muestra en consola si la transaccion fue exitosa o rechazada
            view.printResult(isValid);

        } catch (Exception e) {

            // Atrapa excepciones fatales (como fallos en OP_EQUALVERIFY) y muestra el motivo
            view.printError(e.getMessage());
            
            // Marca la transaccion como invalida debido al error detectado
            view.printResult(false);
        }
    }
}