package com.wf;


import android.util.Log;

import java.util.concurrent.Semaphore;

import static com.wf.wffrjni.ExtractPersonByName;
import static com.wf.wffrjni.initialize;

public class wffrdualcamapp {

    private static final int AssetError = 1;
    private static final int RecordError = 2;
    private static final int InitializeError = 3;
    public static int frInitialized = 0;
    public static int enrollTime = 50;
    public static int recognizeTime = 50;
    public static int recognitionSpoofing = 1;
    public static int enrollSpoofing = 1;
    public static int currentState = 0;
    public static long startTime;
    public static int timeRemaining = 0;
    public static String assetPath = "";
    static int Process_Running_Error = 50;
    static Object[] DBnames;
    static int[] records;
    private static int state = 0;
    private static int[][] faceCoordinates;
    private static int idCard_ret;
    private static String[] names;
    private static float[] confidence;
    static Semaphore semaphore = new Semaphore(1);

    public static int finish_state = 1;


    public static int getFinishState() {
        return finish_state;
    }


    public static int getState() {
        return state;
    }

    public static void setState(int value) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        state = value;

        semaphore.release();

    }

    public static void setAssetPath(String path) {
        assetPath = path;
    }

    public static String getAssetPath() {
        return assetPath;
    }

    public static long getTimeLeft() {
        return timeRemaining;
    }

    public static int startExecution(byte[] cameraDataColor, byte[] cameraDataIR, int frameWidth, int frameHeight, String firstName, String lastName, boolean detectonlymode) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (currentState == 1 && state == 2) {
            System.out.println("WFFRJNI: Recognizing already running, stopping the current process and release resources.");
            System.out.println("WFFRJNI: Release");
            wffrjni.Release();
            frInitialized = 0;

        }
        if (currentState == 2 && state == 1) {
            System.out.println("WFFRJNI: Enrolling already running, stopping the current process and release resources.");
            System.out.println("WFFRJNI: Release");
            wffrjni.Release();
            frInitialized = 0;
        }

        if (assetPath != null && !assetPath.equals("")) {
            if (state == 0) {
                if (frInitialized == 1) {
                    System.out.println("WFFRJNI: Release");
                    wffrjni.Release();
                    frInitialized = 0;
                }
            } else if (state == 2) {
                if (frInitialized == 0) {
                    int init = initialize(assetPath, frameWidth, frameHeight, frameWidth, 1, enrollSpoofing);
                    if (init != 0) {
                        System.out.println("WFFRJNI: Init Error: " + init);
                        semaphore.release();
                        return 3;
                    }

                    System.out.println("WFFRJNI: Enroll Init");

                    int addRec = wffrjni.addRecord(firstName, lastName);
                    if (addRec != 0) {
                        System.out.println("WFFRJNI: Adding Record Error: " + addRec);
                        semaphore.release();
                        return 2;
                    }
                    frInitialized = 1;
                    startTime = System.currentTimeMillis();
                }

                faceCoordinates = wffrjni.enrollDualcam(cameraDataColor, cameraDataIR, frameWidth, frameHeight);
                names = wffrjni.nameValues();
                confidence = wffrjni.confidenceValues();
                long currentSeconds = System.currentTimeMillis();
                long elapsedTime = currentSeconds - startTime;
                System.out.println("WFFRJNI: Enroll Time: " + elapsedTime);
                if (elapsedTime > (long) enrollTime) {
                    System.out.println("WFFRJNI: Release");
                    timeRemaining = 0;
                    wffrjni.Release();
                    frInitialized = 0;
                    state = 0;
                } else {
                    timeRemaining = (enrollTime / 1000) - (int) (elapsedTime / 1000);
                }
                int ss = ExtractPersonByName(firstName, lastName);
            } else if (state == 1) {
                long timeMillis1 = System.currentTimeMillis();
                if (frInitialized == 0) {
                    int init = initialize(assetPath, frameWidth, frameHeight, frameWidth, 0, recognitionSpoofing);
                    if (init != 0) {
                        System.out.println("WFFRJNI: Init Recognize Error: " + init);
                        semaphore.release();
                        return 3;
                    }

                    System.out.println("WFFRJNI: Recognize Init");
                    frInitialized = 1;
                    startTime = timeMillis1;
                }
                wffrjni.SetDetectionAlgoType(3);
                wffrjni.SetDetectionOnlyMode(1);
                faceCoordinates = wffrjni.recognizeDualcam(cameraDataColor, cameraDataIR, frameWidth, frameHeight);
                int[] facetype = wffrjni.faceType();
                if (faceCoordinates != null && faceCoordinates.length > 0 && facetype[0] == 1) {

                    wffrjni.SetDetectionAlgoType(1);
                    if (!detectonlymode)
                        wffrjni.SetDetectionOnlyMode(0);
                    faceCoordinates = wffrjni.recognizeDualcam(cameraDataColor, cameraDataIR, frameWidth, frameHeight);

                }

                names = wffrjni.nameValues();
                confidence = wffrjni.confidenceValues();
                long elapsedTime1 = timeMillis1 - startTime;
                System.out.println("WFFRJNI: Rec Time: " + elapsedTime1);
                if (elapsedTime1 > (long) recognizeTime) {
                    System.out.println("WFFRJNI: Release");
                    wffrjni.Release();
                    finish_state = -1;
                    frInitialized = 0;
                    state = 0;
                    timeRemaining = 0;
                } else {
                    timeRemaining = (recognizeTime / 1000 - (int) elapsedTime1 / 1000);
                }
            }
            currentState = state;
            semaphore.release();
            return 0;
        } else {
            semaphore.release();
            return 1;
        }
    }

    public static void releaseEngine() {
        wffrjni.Release();
        state = 0;
        frInitialized = 0;
    }

    /**
     * Force stop recognition/enroll process and release engine instance
     **/
    public static int stopExecution() {
        try {
            semaphore.acquire();
            if (frInitialized == 1) {
                wffrjni.Release();
                frInitialized = 0;
                state = 0;
            }

            semaphore.release();
            return 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 1;
        }
    }


    public static int getDatabase() {
        try {
            semaphore.acquire();

            if ((getState() == 0) && (frInitialized == 0)) {
                int init = initialize(assetPath, 0, 0, 0, 0, 0);
                if (init != 0) {
                    System.out.println("WFFRJNI: Init DB Error: " + init);
                    semaphore.release();
                    return 3;
                }

                System.out.println("WFFRJNI: DB Init");

                DBnames = wffrjni.getDbNameList();

                if (getDatabaseNames() != null)
                    records = wffrjni.getDbRecordList(getDatabaseNames().length);

                wffrjni.Release();
                state = 0;
                frInitialized = 0;

                semaphore.release();
                return 0;
            } else {
                semaphore.release();
                return Process_Running_Error;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 1;
        }
    }


    public static int[][] getFaceCoordinates() {

        return faceCoordinates;
    }

    public static String[] getNames() {
        return names;
    }

    public static float[] getConfidence() {
        return confidence;
    }

    public static Object[] getDatabaseNames() {


        return DBnames;
    }

    public static int[] getDatabaseRecords() {

        return records;
    }

    public static int deletePerson(int recordID) {

        try {
            semaphore.acquire();

            if ((getState() == 0) && (frInitialized == 0)) {
                int init = initialize(assetPath, 0, 0, 0, 0, 0);
                System.out.println("Init: " + init);
                if (init != 0) {
                    System.out.println("WFFRJNI: Init DB Error: " + init);
                    semaphore.release();
                    return 3;
                }
                int val = wffrjni.DeletePersonFromDb(recordID);
                System.out.println("Val: " + val);
                wffrjni.Release();
                state = 0;
                frInitialized = 0;

                semaphore.release();
                return val;
            } else {
                semaphore.release();
                return Process_Running_Error;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static int deletePersonbyName(String name) {

        try {
            semaphore.acquire();
            if ((getState() == 0) && (frInitialized == 0)) {
                int init = initialize(assetPath, 0, 0, 0, 0, 0);
                System.out.println("Init: " + init);
                if (init != 0) {
                    System.out.println("WFFRJNI: Init DB Error: " + init);
                    semaphore.release();
                    return 3;
                }
                String firstname = name;
                String lastname = "";
                System.out.println("WFFRJNI: Enroll Init");
                if (name != null && name.contains(" ")) {
                    lastname = name.substring(name.lastIndexOf(' '));
                    firstname = name.substring(0, name.lastIndexOf(' '));
                }
                int val = wffrjni.DeletePersonByNameFromDb(firstname, lastname);
                System.out.println("Val: " + val);
                wffrjni.Release();
                state = 0;
                frInitialized = 0;

                semaphore.release();
                return val;
            } else {
                semaphore.release();
                return Process_Running_Error;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static int deleteDatabase() {
        try {
            semaphore.acquire();
            if ((getState() == 0) && (frInitialized == 0)) {
                int init = initialize(assetPath, 0, 0, 0, 0, 0);
                if (init != 0) {
                    System.out.println("WFFRJNI: Init DB Error: " + init);
                    semaphore.release();
                    return 3;
                }
                int val = wffrjni.DeleteDatabase();

                wffrjni.Release();
                state = 0;
                frInitialized = 0;

                semaphore.release();
                return val;
            } else {

                semaphore.release();
                return Process_Running_Error;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 1;
        }
    }


    public static int updateFromPCDB() {
        try {
            semaphore.acquire();
            if (currentState > 0 && frInitialized == 1) {
                System.out.println("WFFRJNI: Video mode already running, stopping the current process and release resources.");
                System.out.println("WFFRJNI: Release");
                wffrjni.Release();
                frInitialized = 0;
                currentState = 0;
                state = 0;
            }

            if (assetPath != null && !assetPath.equals("")) {

                int init = initialize(assetPath, 0, 0, 0, 0, 0);
                if (init != 0) {
                    System.out.println("WFFRJNI: Init Recognize Error: " + init);
                    semaphore.release();
                    return 3;
                }

                wffrjni.Release();

                semaphore.release();
                return 0;
            } else {
                semaphore.release();
                return 1;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 1;
        }

    }

    //获取身份证图片进行录入
    public static float IdCardeEnroll(String idPhoto_path, byte[] cameraDataColor, byte[] cameraDataIR, int frameWidth, int frameHeight) {
        wffrjniID.initialize(assetPath, 0);
        idCard_ret = wffrjniID.enrollFromImageFile(idPhoto_path);
        int ret_id = idCard_ret;
        float confs_value = -0.1f;
        int[][] faceArray = wffrjniID.recognize(cameraDataColor, frameWidth, frameHeight);
        float hold = wffrjniID.GetRecognitionThreshold();
        if (faceArray != null) {
            if (faceArray.length > 0) {
                float[] confs = wffrjniID.confidenceValues();
                if (confs[0] > 0)
                    confs_value = confs[0];
            }
        }

        wffrjniID.Release();
        return confs_value;
    }

    /**
     * 根据firstName, lastName 获取人脸数据db
     */
    public static void get_people_db(String firstName, String lastName) {
        Log.e("get_people_db", assetPath);
        initialize(assetPath, 0, 0, 0, 0, 0);
        int i = ExtractPersonByName(firstName, lastName);
        Log.e("get_people_db", "生成pid0:" + i);
    }

    public static int inti_wffrjni() {
        int init_ret = initialize(assetPath, 0, 0, 0, 0, 0);
        wffrjni.Release();
        return init_ret;
    }

    public static int inti_wffrjni_re() {
        int init = initialize(assetPath, 0, 0, 0, 0, recognitionSpoofing);
        if (init != 0) {
            System.out.println("WFFRJNI: Init Recognize Error: " + init);
            semaphore.release();
            return 3;
        }
        wffrjni.Release();
        System.out.println("WFFRJNI: Recognize Init");
        return init;
    }

    /**
     * 根据firstName, lastName 进行删除db
     * 只有在识别的模式下才可以删除
     */
    public static int deleteDBbyName(String firstName, String lastName) {
        int del_ret = wffrjni.DeletePersonByNameFromDb(firstName, lastName);
        if (del_ret != 0) {
            int init = initialize(assetPath, 0, 0, 0, 0, recognitionSpoofing);
            del_ret = wffrjni.DeletePersonByNameFromDb(firstName, lastName);
            wffrjni.Release();
        }
        inti_wffrjni();
        return del_ret;
    }
}
