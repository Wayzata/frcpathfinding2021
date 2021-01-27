/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2020 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.auto.MPController;
import frc.robot.auto.Trajectories;

public class Robot extends TimedRobot {

  // Creates our Motion Profile Controller and Trajectories class
  Trajectories trajectories = new Trajectories();
  MPController mpController;
  Command autoCommand; 

  @Override
  public void robotInit() {
    mpController = new MPController();
  }

  @Override
  public void robotPeriodic() {
    mpController.drive.putEncoder();
    mpController.drive.putGyro();
    mpController.drive.putWheelVelocities();
    CommandScheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
    // Initialize Auto Drive System

    mpController.drive.setupMotorConfigs();

    // Reset encoders
    mpController.drive.resetEncoders();

    // Initialize our odometry
    mpController.drive.initializeOdometry();

    // Ensure our odometry is at 0
    mpController.drive.reset();

    // Update our odometry
    mpController.drive.periodic();

    // Schedules follower command to be run
    mpController.createMecanumFollowerCommand(trajectories.generateAutoTrajectoryFromCurrentPose(3, mpController.drive.getPose()), Units.metersToFeet(1.5)).schedule();
  
  }

  @Override
  public void autonomousPeriodic() {
    // Continue updating odometry while we use it in autonomous
    mpController.drive.periodic();
    System.out.println(mpController.drive.getPose());
  }

  @Override
  public void teleopInit() {
    // Ensure no motion profiles are still running when transitioning into teleop
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void disabledInit() {
    // Cancel any commands that were running
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }
}
