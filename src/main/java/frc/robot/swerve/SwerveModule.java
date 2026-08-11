package frc.robot.swerve;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class SwerveModule {
    private final SparkMax driveMotor;
    private final SparkMax steerMotor;

    private final RelativeEncoder drivingEncoder;
    private final AbsoluteEncoder steeringEncoder;

    private final SparkClosedLoopController drivingClosedLoopController;
    private final SparkClosedLoopController steeringClosedLoopController;

    private double chassisAngularOffset = 0;
    private SwerveModuleState desiredState = new SwerveModuleState(0.0, new Rotation2d());

    /**
     * Constructs a MAXSwerveModule and configures the driving and turning motor,
     * encoder, and PID controller. This configuration is specific to the REV
     * MAXSwerve Module built with NEOs, SPARKS MAX, and a Through Bore
     * Encoder.
     */
    public SwerveModule(int driveCANID, int steerCANID, double chassisAngularOffset){
        driveMotor = new SparkMax(driveCANID, MotorType.kBrushless);
        steerMotor = new SparkMax(steerCANID, MotorType.kBrushless);

        drivingEncoder = driveMotor.getEncoder();
        steeringEncoder = steerMotor.getAbsoluteEncoder();

        drivingClosedLoopController = driveMotor.getClosedLoopController();
        steeringClosedLoopController = steerMotor.getClosedLoopController();

        driveMotor.configure(Configs.MAXSwerveModule.drivingConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        steerMotor.configure(Configs.MAXSwerveModule.steeringConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);

        this.chassisAngularOffset = chassisAngularOffset;
        desiredState.angle = new Rotation2d(steeringEncoder.getPosition());
        drivingEncoder.setPosition(0);
    }

    /**
     * Returns the current state of the module.
     *
     * @return The current state of the module.
     */
    public SwerveModuleState getState(){
        // Apply chassis angular offset to the encoder position to get the position
        // relative to the chassis.
        return new SwerveModuleState(drivingEncoder.getVelocity(),
            new Rotation2d(steeringEncoder.getPosition() - chassisAngularOffset));
    }

    /**
     * Returns the current position of the module.
     *
     * @return The current position of the module.
     */
    public SwerveModulePosition getPosition(){
        // Apply chassis angular offset to the encoder position to get the position
        // relative to the chassis.
        return new SwerveModulePosition(drivingEncoder.getPosition(),
            new Rotation2d(steeringEncoder.getPosition() - chassisAngularOffset));
    }

    /**
     * Sets the desired state for the module.
     *
     * @param desiredState Desired state with speed and angle.
     */
    public void setDesiredState(SwerveModuleState desieredState){
        // Apply chassis angular offset to the desired state.
        SwerveModuleState correctedDesiredState = new SwerveModuleState();
        correctedDesiredState.speedMetersPerSecond = desiredState.speedMetersPerSecond;
        correctedDesiredState.angle = desiredState.angle.plus(Rotation2d.fromRadians(chassisAngularOffset));

        // Optimize the reference state to avoid spinning further than 90 degrees.
        correctedDesiredState.optimize(new Rotation2d(steeringEncoder.getPosition()));

        // Command driving and turning SPARKS towards their respective setpoints.
        drivingClosedLoopController.setSetpoint(correctedDesiredState.speedMetersPerSecond, ControlType.kVelocity);
        steeringClosedLoopController.setSetpoint(correctedDesiredState.angle.getRadians(), ControlType.kPosition);

        this.desiredState = desieredState;
    }

    /** Zeroes all the SwerveModule encoders. */
    public void resetEncoders() {
        drivingEncoder.setPosition(0);
    }
}
