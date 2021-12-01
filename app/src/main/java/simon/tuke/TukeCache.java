package simon.tuke;

import java.io.Serializable;

public interface TukeCache {
    void add(String key, Serializable value,int time);
    Serializable get(String key);
    void remove(String key);
    void removeAll();
}
