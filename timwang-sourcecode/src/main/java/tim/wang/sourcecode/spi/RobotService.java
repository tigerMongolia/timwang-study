package tim.wang.sourcecode.spi;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * @author wangjun
 * @date 2020-06-28
 */
public class RobotService {
    public Robot getRobot() {
        ServiceLoader<Robot> serializers = ServiceLoader.load(Robot.class);
        final Optional<Robot> serializer = StreamSupport.stream(serializers.spliterator(), false)
                .findFirst();
        return serializer.orElse(new OptimusPrime());
    }

    public static void main(String[] args) {
        RobotService robotService = new RobotService();
        Robot robot = robotService.getRobot();
        robot.sayHello();
    }
}
