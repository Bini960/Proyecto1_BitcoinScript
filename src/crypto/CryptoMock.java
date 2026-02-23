package crypto;

public class CryptoMock {

    // Simula el algoritmo HASH160 agregando un prefijo al texto original
    public String hash160(String data) {
        return "hash160_" + data;
    }

    // Simula el algoritmo SHA256 agregando un prefijo al texto original
    public String sha256(String data) {
        return "sha256_" + data;
    }

    // Simula la verificación de una firma digital contra una llave pública
    public boolean checkSig(String sig, String pubKey) {
        return sig.equals("firma_valida")
                && pubKey != null
                && !pubKey.isEmpty();
    }
}
