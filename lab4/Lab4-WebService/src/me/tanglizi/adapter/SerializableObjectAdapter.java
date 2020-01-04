package me.tanglizi.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.*;
import java.util.Base64;

/**
 * SerializableObjectAdapter provides [String - Serializable object] marshaling and unmarshaling method in xml.
 *
 * @author Zhang Chunxu
 */
public class SerializableObjectAdapter extends XmlAdapter<String, Serializable> {

    @Override
    public Serializable unmarshal(String v) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(v);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream stream = new ObjectInputStream(byteArrayInputStream);
        return (Serializable) stream.readObject();
    }

    @Override
    public String marshal(Serializable v) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream stream = new ObjectOutputStream(byteArrayOutputStream);
        stream.writeObject(v);

        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }
}
