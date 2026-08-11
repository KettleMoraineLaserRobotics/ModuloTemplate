// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Random;

import com.revrobotics.spark.SparkMax;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;



public class Robot extends TimedRobot {
  private DriveStation driveStation;
  private RobotHardware hardware;
  double voltage = RobotController.getBatteryVoltage();
  int tick;


  Random random = new Random();


  @Override public void robotInit() {
    hardware = new RobotHardware();
    driveStation = new DriveStation(hardware);

  }

  public Robot() {
    
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run(); 
    voltage = RobotController.getBatteryVoltage();
    // Do this in either robot periodic or subsystem periodic
    
    SmartDashboard.putNumber("Battery Voltage", voltage);

  }


  @Override
  public void disabledInit() {
  
  }

  @Override
  public void disabledPeriodic() {
  
  }

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void autonomousExit() { 

  }

  @Override
  public void teleopInit() {

  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}

  @Override
  public void simulationPeriodic() {}

}