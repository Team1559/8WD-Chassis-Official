/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    Joystick joy;
    Drive d;
    boolean auto = false;
    Solenoid shift1;
    Solenoid shift2;
    int gear = 0;
    double shiftPoint = 0;
    
    public void robotInit() {
        joy = new Joystick(1);
        d = new Drive();
        shift1 = new Solenoid(1);
        shift2 = new Solenoid(2);
        gear = 1;
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        d.drive(joy);
        if(joy.getRawButton(7)){
            auto = !auto;
        }
        
        //set shift point
        shiftPoint = joy.getRawAxis(3);
        
        //ALWAYS DO THIS!
        SmartDashboard.putDouble("SPEED", getSpeed());
        SmartDashboard.putDouble("THROTTLE", getThrottle(joy));
        SmartDashboard.putBoolean("AUTO_MODE", auto);
        SmartDashboard.putDouble("SHIFT_POINT", shiftPoint);
        
        if(auto){//automatic
            
            switch(gear){
                
                case 1: //gear 1
                    if((getSpeed() > shiftPoint) && !joy.getRawButton(1)){
                        gear2();
                        gear = 2;
                    }
                    
                break;
                    
                case 2: //gear 2
                    if((getSpeed() < shiftPoint) && (getThrottle(joy) >= .75)){
                        gear1();
                        gear = 1;
                    }
                    
                    if(getSpeed() < .1){
                        
                        gear1();
                        gear = 1;
                        
                    }
                    
                break;
                    
                
            }
            
        } else {//manual
            if(joy.getRawButton(4)){
                gear1();
            } else if(joy.getRawButton(5)){
                gear2();
            }
        }
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    public void gear1(){
        shift1.set(false);
        shift2.set(true);
        System.out.println("GEAR 1");
        SmartDashboard.putBoolean("GEAR1", true);
        SmartDashboard.putBoolean("GEAR2", false);
    }
    
    public void gear2(){
        shift1.set(true);
        shift2.set(false);
        System.out.println("GEAR 2");
        SmartDashboard.putBoolean("GEAR1", false);
        SmartDashboard.putBoolean("GEAR2", true);
    }
    
    public double getSpeed(){ //eventually read current from roborio
        
        return (d.left.getSpeed()+d.right.getSpeed())/2;
                       
    }
    
    public double getThrottle(Joystick joy){
        
        return joy.getRawAxis(1);
        
    }
    
}
