package util;
//Author Zbigniew Lisak (zjlisak1@sheffield.ac.uk)
//Last updated : 05.12.2021

public class Pose {
    public float posX, posZ, rotY, bodyPitch, bodyRoll, headPitch, headYaw, leftEarAngle, rightEarAngle;
    public Pose(float posX, float posZ, float rotY, float bodyPitch, float bodyRoll, float headPitch, float headYaw, float leftEarAngle, float rightEarAngle){
        this.posX = posX;
        this.posZ = posZ;
        this.rotY = rotY;
        this.bodyPitch = bodyPitch;
        this.bodyRoll = bodyRoll;
        this.headPitch = headPitch;
        this.headYaw = headYaw;
        this.leftEarAngle = leftEarAngle;
        this.rightEarAngle = rightEarAngle;
    }
}
