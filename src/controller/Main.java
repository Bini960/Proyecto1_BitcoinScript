package controller;

import interpreter.BitcoinInterpreter;
import view.View;

public class Main {

    // Punto de entrada principal del programa
    public static void main(String[] args) {

        // Imprime los mensajes de bienvenida en la consola
        System.out.println("Iniciando Interprete de Bitcoin Script...");
        System.out.println("Ejecutando transaccion P2PKH de prueba...\n");

        // Define el script de desbloqueo (scriptSig) que contiene la firma y la llave
        String scriptSig = "firma_valida llave_publica_andres";

        // Define el script de bloqueo (scriptPubKey) con la estructura estandar P2PKH
        String scriptPubKey =
                "OP_DUP OP_HASH160 hash160_llave_publica_andres OP_EQUALVERIFY OP_CHECKSIG";

        // Activa el modo de depuracion para mostrar el rastro de la pila en consola
        boolean trace = true;

        // Inicia el proceso de ejecucion de la transaccion
        run(scriptSig, scriptPubKey, trace);
    }

    // Orquesta la preparacion y validacion de los scripts
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

            // Atrapa excepciones fatales (ej. fallos en OP_EQUALVERIFY) y muestra el motivo
            view.printError(e.getMessage());
            
            // Marca la transaccion como invalida debido al error detectado
            view.printResult(false);
        }
    }
}