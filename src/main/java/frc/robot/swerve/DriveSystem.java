package frc.robot.swerve;

import java.util.logging.Handler;

import com.revrobotics.spark.SparkMax;

import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
 

public class DriveSystem extends SubsystemBase{
    SwerveModule testModule = new SwerveModule(0, 0, 0);

    public DriveSystem(){
        HAL.report(tResourceType.kResourceType_RobotDrive, tInstances.kRobotDriveSwerve_MaxSwerve);
    }

    public void drive(double xSpeed, double ySpeed){
        double xSpeedDelivered =  xSpeed * 3;
        double ySpeedDelivered = ySpeed * 3;

        var swerveModuleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(
            new ChassisSpeeds(xSpeedDelivered, ySpeedDelivered, 0));
        testModule.setDesiredState(swerveModuleStates[0]);
    }
}
