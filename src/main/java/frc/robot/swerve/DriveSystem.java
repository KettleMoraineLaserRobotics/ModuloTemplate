package frc.robot.swerve;

import java.util.logging.Handler;

import com.revrobotics.spark.SparkMax;

import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.ADIS16470_IMU.IMUAxis;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
 

public class DriveSystem extends SubsystemBase{
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


    public DriveSystem(){
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

    public void drive(double xSpeed, double ySpeed){
        double xSpeedDelivered =  xSpeed * 3;
        double ySpeedDelivered = ySpeed * 3;

        var swerveModuleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(
            new ChassisSpeeds(xSpeedDelivered, ySpeedDelivered, 0));
        testModule.setDesiredState(swerveModuleStates[0]);
    }
}
