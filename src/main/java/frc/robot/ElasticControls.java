// package frc.robot;

// import edu.wpi.first.util.sendable.Sendable;
// import edu.wpi.first.util.sendable.SendableBuilder;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import frc.robot.subsystems.CommandSwerveDrivetrain;

// public class ElasticControls {

//     private CommandSwerveDrivetrain drivetrain;

//     public ElasticControls(CommandSwerveDrivetrain drivetrain) {
//         this.drivetrain = drivetrain;
//         SmartDashboard.putData("Swerve Drive", new Sendable() {

//         @Override
//         public void initSendable(SendableBuilder builder) {
//             builder.setSmartDashboardType("SwerveDrive");

//             builder.addDoubleProperty("Back Right Angle", () -> drivetrain.getModule(0).getSteerMotor().getPosition().getValueAsDouble(), null);
//             builder.addDoubleProperty("Back Right Velocity", () -> drivetrain.getModule(0).getDriveMotor().getVelocity().getValueAsDouble(), null);

//             builder.addDoubleProperty("Back Left Angle", () -> drivetrain.getModule(1).getSteerMotor().getPosition().getValueAsDouble(), null);
//             builder.addDoubleProperty("Back Left Velocity", () -> drivetrain.getModule(1).getDriveMotor().getVelocity().getValueAsDouble(), null);
//             builder.addDoubleProperty("Front Left Angle", () -> drivetrain.getModule(2).getSteerMotor().getPosition().getValueAsDouble(), null);
//             builder.addDoubleProperty("Front Left Velocity", () -> drivetrain.getModule(2).getDriveMotor().getVelocity().getValueAsDouble(), null);

//             builder.addDoubleProperty("Front Right Angle", () -> drivetrain.getModule(3).getSteerMotor().getPosition().getValueAsDouble(), null);
//             builder.addDoubleProperty("Front Right Velocity", () -> drivetrain.getModule(3).getDriveMotor().getVelocity().getValueAsDouble(), null);
            
//         }
//         });
//     }
// }