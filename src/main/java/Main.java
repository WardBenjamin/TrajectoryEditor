import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by Benjamin Ward on 11/24/2016.
 */

public class Main {

    static double max_velocity = 1.7, max_acceleration = 2.0, max_jerk = 60.0;
    static double P = 1.0, I = 0.0, D = 0.0, V = 1 / max_velocity, A = 0;

    static int currentLeftEncoderPosition = 0, currentRightEncoderPosition = 0, encoderTicksPerRevolution = 1000;
    static double wheelDiameter = .1016; // 4 in = .1016 m

    static double gyroValueDegree = 0;

    static EncoderFollower left, right;
    static Trajectory trajectory;

    public static void main(String[] args) {
        Waypoint[] points = new Waypoint[] {
                new Waypoint(-4, -1, Pathfinder.d2r(-45)),      // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
                new Waypoint(-2, -2, 0),                        // Waypoint @ x=-2, y=-2, exit angle=0 radians
                new Waypoint(0, 0, 0)                           // Waypoint @ x=0, y=0,   exit angle=0 radians
        };

        Trajectory.Config  config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC,
                Trajectory.Config.SAMPLES_HIGH, 0.05, max_velocity, max_acceleration, max_jerk); // Delta time 0.05 = 1/20
        trajectory = Pathfinder.generate(points, config);

        File file = new File("output.csv");
        Pathfinder.writeToCSV(file, trajectory);
    }

    public static void initialize()
    {
        TankModifier modifier = new TankModifier(trajectory).modify(0.4064); // 16 in = .4064 m

        left = new EncoderFollower(trajectory);
        right = new EncoderFollower(trajectory);

        left.configureEncoder(currentLeftEncoderPosition, encoderTicksPerRevolution, wheelDiameter);
        right.configureEncoder(currentRightEncoderPosition, encoderTicksPerRevolution, wheelDiameter);

        // Proportional gain. Usually this will be quite high
        // Integral gain. This is unused for motion profiling
        // Derivative gain. Tweak this if you are unhappy with the tracking of the trajectory
        // Velocity ratio. This is 1 over the maximum velocity you provided in the
        //      trajectory configuration (it translates m/s to a -1 to 1 scale that your motors can read)
        // Acceleration gain. Tweak this if you want to get to a higher or lower speed quicker
        left.configurePIDVA(P, I, D, V, A);
        right.configurePIDVA(P, I, D, V, A);
    }

    public static void periodic()
    {
        double l = left.calculate(currentLeftEncoderPosition);
        double r = right.calculate(currentRightEncoderPosition);

        double gyroHeading = gyroValueDegree;    // Assuming the gyro is giving a value in degrees
        double desiredHeading = Pathfinder.r2d(left.getHeading());  // Should also be in degrees

        double angleDifference
                = Pathfinder.boundHalfDegrees(desiredHeading - gyroHeading); // Limits degree to -180, 180
        double turn = 0.8 * (-1.0/80.0) * angleDifference;

        setLeftMotors(l + turn);
        setRightMotors(r - turn);
    }

    public static void setLeftMotors(double power) {}
    public static void setRightMotors(double power) {}
}
