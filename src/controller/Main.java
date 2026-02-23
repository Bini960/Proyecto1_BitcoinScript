package controller;

import interpreter.BitcoinInterpreter;
import view.View;

public class Main {

    public static void main(String[] args) {

        System.out.println("Iniciando Interprete de Bitcoin Script...");
        System.out.println("Ejecutando transaccion P2PKH de prueba...\n");

        String scriptSig = "firma_valida llave_publica_andres";

        String scriptPubKey =
                "OP_DUP OP_HASH160 hash160_llave_publica_andres OP_EQUALVERIFY OP_CHECKSIG";

        boolean trace = true;

        run(scriptSig, scriptPubKey, trace);
    }

    public static void run(String scriptSig, String scriptPubKey, boolean trace) {

        BitcoinInterpreter interpreter = new BitcoinInterpreter();
        View view = new View();

        String scriptCompleto = scriptSig + " " + scriptPubKey;
        String[] tokens = scriptCompleto.split(" ");

        try {

            boolean isValid = interpreter.evaluate(tokens, view, trace);
            view.printResult(isValid);

        } catch (Exception e) {

            view.printError(e.getMessage());
            view.printResult(false);
        }
    }
}
