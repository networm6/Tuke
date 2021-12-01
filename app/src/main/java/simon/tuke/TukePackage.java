package simon.tuke;

import java.io.Serializable;
import java.util.Objects;

/**
 * 包装需要缓存的对象
 */
public class TukePackage {
    public Serializable obj;
    public int size;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TukePackage that = (TukePackage) o;
        return size == that.size && Objects.equals(obj, that.obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(obj, size);
    }
}
