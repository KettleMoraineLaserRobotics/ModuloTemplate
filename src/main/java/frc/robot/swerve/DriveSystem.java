package frc.robot.swerve;

import java.util.logging.Handler;

import com.revrobotics.spark.SparkMax;

import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.ADIS16470_IMU.IMUAxis;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
 

public class DriveSystem extends SubsystemBase {
    //Set Swerve modules
    private final SwerveModule frontLeftSwerveModule = new SwerveModule(
        DriveConstants.frontLeftDriveCANID,
        DriveConstants.frontLeftSteerCANID,
        DriveConstants.kFrontLeftChassisAngularOffset
    );
    private final SwerveModule frontRightSwerveModule = new SwerveModule(
        DriveConstants.frontRightDriveCANID,
        DriveConstants.frontRightSteerCANID,
        DriveConstants.kFrontRightChassisAngularOffset
    );
    private final SwerveModule backLeftSwerveModule = new SwerveModule(
        DriveConstants.backLeftDriveCANID,
        DriveConstants.backLeftSteerCANID,
        DriveConstants.kBackLeftChassisAngularOffset
    );
    private final SwerveModule backRightSwerveModule = new SwerveModule(
        DriveConstants.backRightDriveCANID,
        DriveConstants.backRightSteerCANID,
        DriveConstants.kBackRightChassisAngularOffset
    );

    //Set gyro sensor
    private final ADIS16470_IMU gyro = new ADIS16470_IMU();

    //Set up odometry tracking
    SwerveDriveOdometry odometry = new SwerveDriveOdometry(
        DriveConstants.kDriveKinematics, 
        Rotation2d.fromDegrees(gyro.getAngle(IMUAxis.kZ)), 
        new SwerveModulePosition[] {
            frontLeftSwerveModule.getPosition(),
            frontRightSwerveModule.getPosition(),
            backLeftSwerveModule.getPosition(),
            backRightSwerveModule.getPosition()
    });


    public DriveSystem() {
        HAL.report(tResourceType.kResourceType_RobotDrive, tInstances.kRobotDriveSwerve_MaxSwerve);
    }

    @Override
    public void periodic() {
        //update odometry
        odometry.update(
            Rotation2d.fromDegrees(gyro.getAngle(IMUAxis.kZ)),
             new SwerveModulePosition[] {
                frontLeftSwerveModule.getPosition(),
                frontRightSwerveModule.getPosition(),
                backLeftSwerveModule.getPosition(),
                backRightSwerveModule.getPosition()
        });
    }

    /** 
     * Returns the currently-estimated pose of the robot.
     * 
     * @return The pose.
     */
    public Pose2d getPose(){
        return odometry.getPoseMeters();
    }

    /**
     * Resets the odometry to the specified pose.
     * 
     * @param pose The pose to which to set the odometry.
     */
    public void resetOdometry(Pose2d pose) {
        odometry.resetPosition(
            Rotation2d.fromDegrees(gyro.getAngle(IMUAxis.kZ)), 
            new SwerveModulePosition[] {
                frontLeftSwerveModule.getPosition(),
                frontRightSwerveModule.getPosition(),
                backLeftSwerveModule.getPosition(),
                backRightSwerveModule.getPosition()
            }, 
            pose);
    }

    /**
     * Method to drive the robot
     * 
     * @param xSpeed Speed of the robot in the x direction (forward and backward)
     * @param ySpeed Speed of the robot in the y direction (left and right)
     * @param rot Rotational speed of the robot 
     * @param fieldRelative Drive the robot field oriented if true
     */
    public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative){
        // Convert the commanded speeds into the correct units for the drivetrain
        double xSpeedDelivered = xSpeed * DriveConstants.kMaxSpeedMetersPerSecond;
        double ySpeedDelivered = ySpeed * DriveConstants.kMaxSpeedMetersPerSecond;
        double rotDelivered = rot * DriveConstants.kMaxAngularSpeed;

        var swerveModuleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(
        fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered,
                Rotation2d.fromDegrees(gyro.getAngle(IMUAxis.kZ)))
            : new ChassisSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered));

        setSwerveModuleStates(swerveModuleStates);
    }

    /**
     * Sets the wheel to and X formation to prevent movement
     */
    public void setX() {
        frontLeftSwerveModule.setDesiredState(new SwerveModuleState(0, new Rotation2d(45)));
        frontRightSwerveModule.setDesiredState(new SwerveModuleState(0, new Rotation2d(-45)));
        backLeftSwerveModule.setDesiredState(new SwerveModuleState(0, new Rotation2d(-45)));
        backRightSwerveModule.setDesiredState(new SwerveModuleState(0, new Rotation2d(45)));
    }

    /**
     * Sets the swerve module states.
     * 
     * @param swerveModuleStates the desired SwerveModule states.
     */
    public void setSwerveModuleStates(SwerveModuleState[] swerveModuleStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, DriveConstants.kMaxSpeedMetersPerSecond);

        frontLeftSwerveModule.setDesiredState(swerveModuleStates[0]);
        frontRightSwerveModule.setDesiredState(swerveModuleStates[1]);
        backLeftSwerveModule.setDesiredState(swerveModuleStates[2]);
        backRightSwerveModule.setDesiredState(swerveModuleStates[3]);
    }

    /** Resets the drive encoders to 0 */
    public void resetEncoders() {
        frontLeftSwerveModule.resetEncoders();
        frontRightSwerveModule.resetEncoders();
        backLeftSwerveModule.resetEncoders();
        backRightSwerveModule.resetEncoders();
    }

    /** Resets the direction of the robot (heading) */
    public void zeroHeading() {
        gyro.reset();
    }

    /**
     * Returns the heading of the robot
     * 
     * @return the robots heading in degrees from -180 to 180
     */
    public double getHeading() {
        return Rotation2d.fromDegrees(gyro.getAngle(IMUAxis.kZ)).getDegrees();
    }

    /**
     * Returns the turn rate of the robot
     * 
     * @return The turn rate of the robot, in degrees per second
     */
    public double getTurnRate() {
        return gyro.getRate(IMUAxis.kZ) * (DriveConstants.kGyroReversed ? -1.0 : 1.0);
    }
}
