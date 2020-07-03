package tim.wang.sourcecode.loadblance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wangjun
 * @date 2020-07-02
 */
public class ServerInfos {
    private static final List<String> ipList =
                    Stream.of("192.168.0.31", "192.168.0.92", "192.168.0.12", "192.168.0.43", "192.168.0.100")
                    .collect(Collectors.toList());

    private static Map<String, Integer> ipWeightList = new HashMap<>();

    static {
        ipWeightList.put("192.168.0.31", 1);
        ipWeightList.put("192.168.0.92", 5);
        ipWeightList.put("192.168.0.12", 2);
        ipWeightList.put("192.168.0.43", 4);
    }

    static Map<String, Integer> getIpWeightList() {
        return ipWeightList;
    }

    static List<String> getIpList() {
        return ipList;
    }
}
