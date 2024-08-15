package in.pratanumandal.pingme.security;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;
import java.io.IOException;
import java.io.Serializable;

public class SecureObject extends SealedObject {

    private final byte[] iv;

    public SecureObject(Serializable object, Cipher c, byte[] iv) throws IOException, IllegalBlockSizeException {
        super(object, c);
        this.iv = iv;
    }

    public byte[] getIV() {
        return iv;
    }

}
