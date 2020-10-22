package com.huawei.arengine.demos.java.recorder;

import com.huawei.hiar.ARPose;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector4f;

public class CsvPose {
    private static final String HEADER = "frame,tx,ty,tz,qx,qy,qz,qw,yaw,pitch,roll\n";

    protected String frameId;
    protected ARPose p;
    protected double yaw;
    protected double pitch;
    protected double roll;

    private static Character DELIMITER = ',';

    ////north : https://github.com/google-ar/arcore-android-sdk/issues/119
    public CsvPose(String frameId, ARPose pose) {
        this.frameId = frameId;
        this.p = pose;

        Vector4f q = new Vector4f(pose.qx(), pose.qy(), pose.qz(), pose.qw());
        // https://answers.unity.com/questions/416169/finding-pitchrollyaw-from-quaternions.html
        pitch = Math.toDegrees(Math.atan2(2 * q.x * q.w - 2 * q.y * q.z, 1 - 2 * q.x * q.x - 2 * q.z * q.z));
        yaw = Math.toDegrees(Math.atan2(2 * q.y * q.w - 2 * q.x * q.z, 1 - 2 * q.y * q.y - 2 * q.z * q.z));
        roll = Math.toDegrees(Math.asin(2 * q.x * q.y + 2 * q.z * q.w));
    }

    public String toCsvRow() {
        StringBuilder sb = new StringBuilder();
        sb.append(frameId).append(DELIMITER)
                .append(p.tx()).append(DELIMITER)
                .append(p.ty()).append(DELIMITER)
                .append(p.tz()).append(DELIMITER)
                .append(p.qx()).append(DELIMITER)
                .append(p.qy()).append(DELIMITER)
                .append(p.qz()).append(DELIMITER)
                .append(p.qw()).append(DELIMITER)
                .append(yaw).append(DELIMITER)
                .append(pitch).append(DELIMITER)
                .append(roll);
        return sb.toString();
    }

    public String toString() {
        String str = String.format("Pose t: %.3f %.3f %.3f \nq: %.3f %.3f %.3f %.3f\n", p.tx(), p.ty(), p.tz(), p.qx(), p.qy(), p.qz(), p.qw());
        String angles = String.format("Pitch: %.3f Yaw: %.3f Roll: %.3f", pitch, yaw, roll);
        return str+angles;
    }

    public static List<String> toCsvRows(List<CsvPose> poses) {
        List<String> rows = new ArrayList<>(poses.size()+1);
        rows.add(CsvPose.HEADER);
        for (CsvPose pose : poses) {
            String row = pose.toCsvRow();
            rows.add(row);
        }
        return rows;
    }
}
