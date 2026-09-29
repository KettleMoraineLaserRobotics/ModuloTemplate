/*----------------------------------------------------------------------------*/
/* Copyright (c) 2026 FRC Team 2077. All Rights Reserved.                     */
/* Open Source Software - may be modified and shared by FRC teams.            */
/*----------------------------------------------------------------------------*/

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.FollowPathCommand;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.*;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.Constants.OIConstants;
import frc.robot.command.*;
import frc.robot.command.ElasticVisualsControl.SwitchTo;
import frc.robot.control.DriveJoystick;
import frc.robot.control.DriveXboxController;
import frc.robot.swerve.DriveSystem;


/**
 * This class is intended to be the center point of defining actions that can be utilized during teleop segments of
 * control. This is where we should define what USB port joysticks should be registered as in `FRC Driver Station`'s usb
 * menu. As well as define what buttons on primary/technical driver's controllers should do what.
 * */
public class DriveStation {
    // Controller port numbers
    // Joysticks that support rotation
    private static final int DRIVE_JOYSTICK_PORT = 0;

    // Joysticks that do not support rotation
    private static final int NUMPAD_PORT = 5;

    private final DriveXboxController driveStick;
    private final Joystick technicalStick;

    private final DriveSystem robotDrive = new DriveSystem();
        
        
    public DriveStation(RobotHardware hardware) {
        /** Set the driver's control method this MUST be a {@link DriveStick} implementation */
        driveStick = getXbox();

        /** Set the technical control method. This can be any {@link Joystick} implementation */
        technicalStick = getNumpad();

        bind(hardware);
    }


    /**
     * This method binds any subsystem's default command and bind commands to a user's chosen
     * control method.
     */
    public void bind(RobotHardware hardware) {

        robotDrive.setDefaultCommand(
            new RunCommand(
                () -> robotDrive.drive(
                -MathUtil.applyDeadband(driveStick.getLeftY(), OIConstants.kDriveDeadband),
                -MathUtil.applyDeadband(driveStick.getLeftX(), OIConstants.kDriveDeadband),
                -MathUtil.applyDeadband(driveStick.getRightX(), OIConstants.kDriveDeadband),
                false),
            robotDrive)          
        );

        bindDriverControl(hardware, driveStick);
        bindTechnicalControl(hardware, technicalStick);
    }

    /** Bind primary driver's button commands here */
    private void bindDriverControl(RobotHardware hardware, DriveXboxController primary) {

        new JoystickButton(primary, Button.kRightBumper.value)
            .whileTrue(new RunCommand(
                () -> robotDrive.setX(),
                robotDrive));
        
        new JoystickButton(primary, Button.kStart.value)
            .onTrue(new InstantCommand(
                () -> robotDrive.zeroHeading(),
                robotDrive));  
    }

    /** Bind technical driver button commands here */
    private void bindTechnicalControl(RobotHardware hardware, Joystick secondary) {
        
    }

    private static DriveXboxController getXbox(){
        return new DriveXboxController(DRIVE_JOYSTICK_PORT).setDriveSensitivity(.25,1)
                                                       .setRotationSensitivity(.05,1);
    }

    private static Joystick getNumpad() {
        return new Joystick(NUMPAD_PORT);
    }
}
