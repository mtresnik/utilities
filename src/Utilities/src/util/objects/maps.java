package util.objects;

import java.util.HashMap;
import java.util.Map;

public final class maps {

    public static <K, V> Map<V, K> invertUniqueMap(Map<K, V> inputMap) {
        Map<V, K> retMap = new HashMap<V, K>();
        for (Map.Entry<K, V> entry : inputMap.entrySet()) {
            retMap.put(entry.getValue(), entry.getKey());
        }
        return retMap;
    }

    private maps() {

    }

}
