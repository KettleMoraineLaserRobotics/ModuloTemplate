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
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
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
    // Common controller port numbers
    // Joysticks that support rotation
    private static final int DRIVE_JOYSTICK_PORT = 0;
    private static final int DRIVE_XBOX_PORT = 1;
    private static final int FLYSKY_PORT = 2;

    // Joysticks that do not support rotation
    private static final int TECHNICAL_JOYSTICK_PORT = 4;
    private static final int NUMPAD_PORT = 5;

    private final DriveXboxController driveStick;
    private final Joystick technicalStick;

    private final XboxController halfSwitch;

    private final CommandXboxController driveNewJoystick = new CommandXboxController(0);

    private final CommandXboxController joystick = new CommandXboxController(0);

    private final DriveSystem driveTest = new DriveSystem();


    public DriveStation(RobotHardware hardware) {
        /** Set the driver's control method this MUST be a {@link DriveStick} implementation */
        //driveStick = getFlysky();
        //driveStick = getJoystick();
        driveStick = getXbox();
        halfSwitch = new XboxController(DRIVE_XBOX_PORT);

        /** Set the technical control method. This can be any {@link Joystick} implementation */
        //technicalStick = getTechnicalJoystick();
        technicalStick = getNumpad();
        

        bind(hardware);

        driveTest.setDefaultCommand(
            new RunCommand(
                () -> driveTest.drive(
                -MathUtil.applyDeadband(driveStick.getLeftY(), OIConstants.kDriveDeadband),
                -MathUtil.applyDeadband(driveStick.getLeftX(), OIConstants.kDriveDeadband)),
            driveTest)
            
        );
    }


    /**
     * This method binds any subsystem's default command and bind commands to a user's chosen
     * control method.
     */
    public void bind(RobotHardware hardware) {

        // Setup basic robot movement commands
        // hardware.getPosition().setDefaultCommand(new CardinalMovement((DriveXboxController) driveStick, halfSwitch));
        // hardware.getHeading().setDefaultCommand(new RotationMovement(driveStick, halfSwitch));

        bindDriverControl(hardware, driveStick);
        bindTechnicalControl(hardware, technicalStick);
    }

    /** Bind primary driver's button commands here */
    private static void bindDriverControl(RobotHardware hardware, DriveXboxController primary) {
     
    }

    /** Bind technical driver button commands here */
    private void bindTechnicalControl(RobotHardware hardware, Joystick secondary) {
        
    }



    /** Normal (silver/brighter) joystick that supports rotation */
    private static DriveJoystick getDriveNewJoystick() {
        return new DriveJoystick(DRIVE_JOYSTICK_PORT).setDriveSensitivity(.15, 5)
                                                     .setRotationSensitivity(.1, 1);
    }

    /** Flysky Drone Controller */
    private static DriveJoystick getFlysky() {
        return new DriveJoystick(FLYSKY_PORT, 4).setDriveSensitivity(.3, 1)
                                                .setRotationSensitivity(.05, 2.5);
    }

    private static DriveXboxController getXbox(){
        return new DriveXboxController(DRIVE_JOYSTICK_PORT).setDriveSensitivity(.25,1)
                                                       .setRotationSensitivity(.05,1);
    }

    /** Currently the darker joystick that doesn't support rotation */
    private static Joystick getTechnicalJoystick() {
        return new Joystick(TECHNICAL_JOYSTICK_PORT);
    }

    private static Joystick getNumpad() {
        return new Joystick(NUMPAD_PORT);
    }


    public CommandXboxController getController(){
        return driveNewJoystick;
    }


}
