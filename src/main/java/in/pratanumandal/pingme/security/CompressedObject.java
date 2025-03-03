package in.pratanumandal.pingme.security;

import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class CompressedObject implements Serializable {

    private final byte[] compressedData;

    public CompressedObject(Object object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DeflaterOutputStream dos = new DeflaterOutputStream(baos);
             ObjectOutputStream oos = new ObjectOutputStream(dos)) {
            oos.writeObject(object);
        }
        compressedData = baos.toByteArray();
    }

    public final Object getObject() throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
        try (InflaterInputStream iis = new InflaterInputStream(bais);
             ObjectInputStream ois = new ObjectInputStream(iis)) {
            return ois.readObject();
        }
    }

}
