package operations;

import crypto.CryptoMock;
import java.util.ArrayDeque;

public class CryptoOperations implements Opcode {

    @Override
    public void execute(String opcode, ArrayDeque<String> stack, CryptoMock crypto) {

        if (stack.isEmpty()) {
            throw new RuntimeException("Pila vacia para operacion criptografica: " + opcode);
        }

        switch (opcode) {

            case "OP_SHA256":
                String dataSha = stack.pop();
                stack.push(crypto.sha256(dataSha));
                break;

            case "OP_HASH160":
                String dataHash160 = stack.pop();
                stack.push(crypto.hash160(dataHash160));
                break;

            case "OP_HASH256":
                String dataHash256 = stack.pop();
                stack.push(crypto.sha256(crypto.sha256(dataHash256)));
                break;

            case "OP_CHECKSIG":

                if (stack.size() < 2) {
                    throw new RuntimeException("Elementos insuficientes para OP_CHECKSIG");
                }

                String pubKey = stack.pop();
                String sig = stack.pop();

                boolean isValid = crypto.checkSig(sig, pubKey);
                stack.push(isValid ? "1" : "0");
                break;

            case "OP_CHECKSIGVERIFY":

                if (stack.size() < 2) {
                    throw new RuntimeException("Elementos insuficientes para OP_CHECKSIGVERIFY");
                }

                String pubKeyV = stack.pop();
                String sigV = stack.pop();

                if (!crypto.checkSig(sigV, pubKeyV)) {
                    throw new RuntimeException("Fallo de verificacion en OP_CHECKSIGVERIFY");
                }
                break;

            default:
                break;
        }
    }
}
