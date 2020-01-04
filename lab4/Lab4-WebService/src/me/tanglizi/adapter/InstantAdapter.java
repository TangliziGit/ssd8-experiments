package me.tanglizi.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.Instant;

/**
 * InstantAdapter provides [long - instant] marshaling and unmarshaling method in xml.
 *
 * @author Zhang Chunxu
 */
public class InstantAdapter extends XmlAdapter<Long, Instant> {

    @Override
    public Long marshal(Instant instant) throws Exception {
        return instant.toEpochMilli();
    }

    @Override
    public Instant unmarshal(Long epochMilli) throws Exception {
        return Instant.ofEpochMilli(epochMilli);
    }

}
