package org.firstinspires.ftc.teamcode;

//import android.util.Range;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by kfrankfurth on 4/27/2018.
 */

@TeleOp(name="holonomicDrive", group = "Concept")
//@Disabled
/*
   Robot wheel mapping:
          X FRONT X
        X           X
      X  FL       FR  X
              X
             XXX
              X
      X  BL       BR  X
        X           X
          X       X

 */

public class holonomicDrive extends OpMode {



    public  DcMotor motorFrontRight;
    public DcMotor motorFrontLeft;
    public DcMotor motorBackRight;
    public DcMotor motorBackLeft;

    public DcMotor motorMainLift;

    public  DcMotor motorgRight;
    public  DcMotor motorgLeft;
    public Servo leftArm;
    public Servo rightArm;


    //lift values
    private final double LIFT_UP = 0.75;
    private final double LIFT_DOWN = -0.75;

    // servo values
    private static final double MID_SERVO =  0.5 ;

    //grabber values
    private final double suckIn =1.0;
    private final double pushOut =-1.0;

    /* Constructor*/




    public holonomicDrive() {

    }


    @Override
    public void init() {


        // hardWareMap for DC motors see diagram above
        // drive train hm
        motorFrontRight = hardwareMap.get(DcMotor.class, "FrontRight");
        motorFrontLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
        motorBackRight = hardwareMap.get(DcMotor.class, "BackRight");
        motorBackLeft = hardwareMap.get(DcMotor.class, "BackLeft");

       // lift hm
        motorMainLift = hardwareMap.get(DcMotor.class, "MainLift");

       //grabber hm
        motorgRight = hardwareMap.get(DcMotor.class, "gRight");
        motorgLeft = hardwareMap.get(DcMotor.class, "gLeft");

        // servo arms
        leftArm = hardwareMap.get(Servo.class, "leftArm");
       rightArm = hardwareMap.get(Servo.class, "rightArm");


        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);

        // lift inst
       motorMainLift.setDirection(DcMotor.Direction.REVERSE);

        // grabber int
       motorgRight.setDirection(DcMotor.Direction.FORWARD);
       motorgLeft.setDirection(DcMotor.Direction.FORWARD);//

        // servo arm inst
        leftArm.setPosition(MID_SERVO);
        rightArm.setPosition(MID_SERVO);

    }

    @Override


    public void loop() {


        // left stick controls direction
        // right stick controls rotation

        float gamepad1LeftY = gamepad1.left_stick_y;
        float gamepad1LeftX = gamepad1.left_stick_x;
        float gamepad1RightX = gamepad1.right_stick_x;

        // holonomic formulas

        float FrontLeft = -gamepad1LeftY - gamepad1LeftX - gamepad1RightX;
        float FrontRight = gamepad1LeftY - gamepad1LeftX - gamepad1RightX;
        float BackRight = gamepad1LeftY + gamepad1LeftX - gamepad1RightX;
        float BackLeft = -gamepad1LeftY + gamepad1LeftX - gamepad1RightX;

        //lift controls



        //Right and Left values never exceed +/- 1

        FrontLeft = Range.clip(FrontLeft, -1, 1);
        FrontRight = Range.clip(FrontRight, -1, 1);
        BackLeft = Range.clip(BackLeft, -1, 1);
        BackRight = Range.clip(BackRight, -1, 1);

        //scale the joystick values so that the never exceed +/-1
        FrontLeft = -(float) scaleInput(FrontLeft);
        FrontRight = -(float) scaleInput(FrontRight);
        BackLeft = -(float) scaleInput(BackLeft);
        BackRight  = -(float) scaleInput(BackRight);

        //motor values
        motorFrontLeft.setPower(FrontLeft);
        motorFrontRight.setPower(FrontRight);
        motorBackLeft.setPower(BackLeft);
        motorBackRight.setPower(BackRight);


        //lift inputs from controller
        if (gamepad1.y)
            motorMainLift.setPower(LIFT_UP);
        else if (gamepad1.a)
            motorMainLift.setPower(LIFT_DOWN);
        else
            motorMainLift.setPower(0.0);


        //
        if (gamepad1.left_trigger)
            .setPower();
        else if gamepad1.right_trigger)
            .setPower();




    //Debugging

        telemetry.addData("Text","***Robot Data***");
        telemetry.addData("Joy XL YL XR",String.format("%.2f",gamepad1LeftX)+" "+
            String.format("%.2f",gamepad1LeftY)+" "+String.format("%.2f",gamepad1RightX));
        telemetry.addData("f left pwr","front left  pwr: "+String.format("%.2f",FrontLeft));
        telemetry.addData("f right pwr","front right pwr: "+String.format("%.2f",FrontRight));
		telemetry.addData("b right pwr","back right pwr: "+String.format("%.2f",BackRight));
		telemetry.addData("b left pwr","back left pwr: "+String.format("%.2f",BackLeft));
       // telemetry.addData( "liftUp", "button y" + String.format());
        // telemetry.addData( "liftdown", "button y" + String.format());

}
    @Override
    public void stop() {

    }

    /*
	 * This method scales the joystick input so for low joystick values, the
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */

    double scaleInput(double dVal){
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10,
                0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50,
                0.60, 0.72, 0.85, 1.00,
                1.00 };

        int index = (int) (dVal * 16.0);

        if (index < 0){
            index = -index;
        }
        if (index > 16){
            index =16;
        }

        double dScale = 0.0;
        if (dVal < 0){
            dScale = -scaleArray[index];
        }else{
            dScale = scaleArray[index];
        }

        return dScale;


    }

}
