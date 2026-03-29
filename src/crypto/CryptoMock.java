package crypto;
/**
 * Clase que simula operaciones criptograficas para el interprete de Bitcoin.
 * Provee metodos mock para hashing y verificacion de firmas sin utilizar criptografia real.
 */
public class CryptoMock {
    /**
     * Aplica una simulacion del algoritmo HASH160.
     *
     * @param data El texto original a procesar.
     * @return El texto resultante con el prefijo "hash160_".
     */
    public String hash160(String data) {
        return "hash160_" + data;
    }

    /**
     * Aplica una simulacion del algoritmo SHA256.
     *
     * @param data El texto original a procesar.
     * @return El texto resultante con el prefijo "sha256_".
     */
    public String sha256(String data) {
        return "sha256_" + data;
    }

    /**
     * Simula la verificación de una firma digital contra una llave pública.
     *
     * @param sig   La firma digital a verificar.
     * @param pubKey La llave pública contra la cual verificar la firma.
     * @return true si la firma es válida, false en caso contrario.
     */
    public boolean checkSig(String sig, String pubKey) {
        return sig.equals("firma_valida")
                && pubKey != null
                && !pubKey.isEmpty();
    }
}
