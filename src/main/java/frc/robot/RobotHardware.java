package frc.robot;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;



public class RobotHardware{

    private static RobotHardware instance = null;

    static final boolean LEFT_INVERSION_STATUS = false;


    /* CAN Ordering:
     * 0-20 (Avoided to exclude legacy setups)
     * 21-30 (Swerve Drive Modules)
     *     - Even: Magnitude
     *     - Odd: Angle
     * 31-40 (Encoders+)
     * 41-50 (Reserved for future uses)
     * 50-70 (Other Motors)

    //  TODO: Turn into enums swapappable 

     * Even = Drive
     * Odd = Steer
     * A = 20 = Front Right
     * B = 22 = Back Right
     * C = 24 = Front Left
     * D = 26 = Back Left
     */

    public static RobotHardware getInstance(){
        if(instance == null) instance = new RobotHardware();
        return instance;
    }


    public float speedLimiterDrive;
    public float speedLimiterSpin;

    public RobotHardware(){
        instance = this;

        speedLimiterDrive = 0.2f;
        speedLimiterSpin = 1.0f;



        //Example of one mechanism using two motors at once
        SparkMax exampleMotor = new SparkMax(0, MotorType.kBrushless);

        SparkMaxConfig exampleMotorConfig = new SparkMaxConfig();
        exampleMotorConfig
            .inverted(LEFT_INVERSION_STATUS)
            .idleMode(IdleMode.kCoast);
        exampleMotorConfig.encoder
            .positionConversionFactor(1) //Note to future self, use a factor of 1 for standard RPM
            .velocityConversionFactor(1);
        exampleMotorConfig.closedLoop
            .feedbackSensor(com.revrobotics.spark.FeedbackSensor.kPrimaryEncoder)
            .pid(0.0001, 0.0, 0.0)
            .velocityFF(1.0 / 5676.0); 
        exampleMotor.configure(exampleMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        //Example of one mechanism using two motors at once
        SparkMax leftLauncherMotor = new SparkMax(1, MotorType.kBrushless);
        SparkMax rightLauncherMotor = new SparkMax(2, MotorType.kBrushless);

        SparkMaxConfig leftLauncherMotorConfig = new SparkMaxConfig();
        leftLauncherMotorConfig
            .inverted(LEFT_INVERSION_STATUS)
            .idleMode(IdleMode.kCoast);
        leftLauncherMotorConfig.encoder
            .positionConversionFactor(1) //Note to future self, use a factor of 1 for standard RPM
            .velocityConversionFactor(1);
        leftLauncherMotorConfig.closedLoop
            .feedbackSensor(com.revrobotics.spark.FeedbackSensor.kPrimaryEncoder)
            .pid(0.0001, 0.0, 0.0)
            .velocityFF(1.0 / 5676.0); 
        leftLauncherMotor.configure(leftLauncherMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        SparkMaxConfig rightLauncherMotorConfig = new SparkMaxConfig();
        rightLauncherMotorConfig
            .inverted(!LEFT_INVERSION_STATUS)
            .idleMode(IdleMode.kCoast);
        rightLauncherMotorConfig.encoder
            .positionConversionFactor(1) //Note to future self, use a factor of 1 for standard RPM
            .velocityConversionFactor(1);
        rightLauncherMotorConfig.closedLoop
            .feedbackSensor(com.revrobotics.spark.FeedbackSensor.kPrimaryEncoder)
            .pid(0.0001, 0.0, 0.0)
            .velocityFF(1.0 / 5676.0); 
        rightLauncherMotor.configure(rightLauncherMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }
}