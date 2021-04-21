package com.wf;

import java.util.concurrent.Semaphore;

import static com.wf.wffrjni.Release;
import static com.wf.wffrjni.VerifyFrameForEnroll;
import static com.wf.wffrjni.VerifyImageForEnrollJpegBuffer;
import static com.wf.wffrjni.confidenceValues;
import static com.wf.wffrjni.initialize;
import static com.wf.wffrjni.nameValues;

public class wffrapp {

    private static final int AssetError = 1;
    private static final int RecordError = 2;
    private static final int InitializeError = 3;
    public static int frInitialized = 0;
    public static int enrollTime = 10000;
    public static int recognizeTime = 100000;
    public static int recognitionSpoofing = 0;
    public static int enrollSpoofing = 0;
    public static int currentState = 0;
    public static long startTime;
    public static int timeRemaining = 0;
    static String assetPath = "";
    static int Process_Running_Error = 50;
    static Object[] DBnames;
    static int[] records;
    private static int state = 0;
    private static int[][] faceCoordinates;
    private static String[] names;
    private static float[] confidence;
    static Semaphore semaphore = new Semaphore(1);


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

    public static int getTimeLeft() {
        return timeRemaining;
    }

    public static int startExecution(byte[] cameraData, int frameWidth, int frameHeight, String name) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (currentState == 1 && state == 2) {
            Release();
            frInitialized = 0;

        }
        if (currentState == 2 && state == 1) {
            Release();
            frInitialized = 0;
        }


        if (assetPath != null && !assetPath.equals("")) {
            if (state == 0) {
                if (frInitialized == 1) {
                    Release();
                    frInitialized = 0;
                }
            } else if (state == 2) {
                String lastName = "";
                if (frInitialized == 0) {
                    int init = initialize(assetPath, frameWidth, frameHeight, frameWidth, 1, enrollSpoofing);
                    if (init != 0) {
                        System.out.println("WFFRJNI: Init Error: " + init);
                        semaphore.release();
                        return 3;
                    }

                    if (name != null && name.contains(" ")) {
                        lastName = name.substring(name.lastIndexOf(' '));
                        name = name.substring(0, name.lastIndexOf(' '));
                    }
                    int addRec = wffrjni.addRecord(name, lastName);
                    if (addRec != 0) {
                        System.out.println("WFFRJNI: Adding Record Error: " + addRec);
                        semaphore.release();
                        return 2;
                    }

                    frInitialized = 1;
                    startTime = System.currentTimeMillis();
                }

                faceCoordinates = wffrjni.enroll(cameraData, frameWidth, frameHeight);
                names = nameValues();
                confidence = confidenceValues();
                long currentSeconds = System.currentTimeMillis();
                long elapsedTime = currentSeconds - startTime;
                System.out.println("WFFRJNI: Enroll Time: " + elapsedTime);
                if (elapsedTime > (long) enrollTime) {
                    timeRemaining = 0;
                    Release();
                    frInitialized = 0;
                    state = 0;
                } else {
                    timeRemaining = (enrollTime / 1000) - (int) (elapsedTime / 1000);
                }
            } else if (state == 1) {
                long timeMillis1 = System.currentTimeMillis();
                if (frInitialized == 0) {
                    int init = initialize(assetPath, frameWidth, frameHeight, frameWidth, 0, recognitionSpoofing);
                    if (init != 0) {
                        System.out.println("WFFRJNI: Init Recognize Error: " + init);
                        semaphore.release();
                        return 3;
                    }

                    frInitialized = 1;
                    startTime = timeMillis1;
                }

                faceCoordinates = wffrjni.recognize(cameraData, frameWidth, frameHeight);
                names = nameValues();
                confidence = confidenceValues();
                long elapsedTime1 = timeMillis1 - startTime;
                System.out.println("WFFRJNI: Rec Time: " + elapsedTime1);
                if (elapsedTime1 > (long) recognizeTime) {
                    Release();
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

    /**
     * Force stop recognition/enroll process and release engine instance
     **/
    public static int stopExecution() {
        try {
            semaphore.acquire();
            if (frInitialized == 1) {
                Release();
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
                DBnames = wffrjni.getDbNameList();

                if (getDatabaseNames() != null)
                    records = wffrjni.getDbRecordList(getDatabaseNames().length);

                Release();
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
                Release();
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
                if (name != null && name.contains(" ")) {
                    lastname = name.substring(name.lastIndexOf(' '));
                    firstname = name.substring(0, name.lastIndexOf(' '));
                }
                int val = wffrjni.DeletePersonByNameFromDb(firstname, lastname);
                System.out.println("Val: " + val);
                Release();
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

                Release();
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


    public static int runEnrollFromJpegFile(String imageFileName, String name) {
        try {
            semaphore.acquire();
            if (currentState > 0 && frInitialized == 1) {
                Release();
                frInitialized = 0;
                currentState = 0;
                state = 0;
            }

            if (assetPath != null && !assetPath.equals("")) {

                int init = initialize(assetPath, 0, 0, 0, 1, 0);
                if (init != 0) {
                    System.out.println("WFFRJNI: Init Error: " + init);
                    semaphore.release();
                    return 3;
                }
                String lastName = "";
                if (name != null && name.contains(" ")) {
                    lastName = name.substring(name.lastIndexOf(' '));
                    name = name.substring(0, name.lastIndexOf(' '));

                }

                int addRec = wffrjni.addRecord(name, lastName);
                if (addRec != 0) {
                    System.out.println("WFFRJNI: Adding Record Error: " + addRec);
                    semaphore.release();
                    return 2;
                }
                faceCoordinates = wffrjni.enrollFromImageFile(imageFileName);
                names = nameValues();
                confidence = confidenceValues();

                Release();

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


    public static int runRecognizeFromJpegFile(String imageFileName) {

        try {
            semaphore.acquire();
            if (currentState > 0 && frInitialized == 1) {
                Release();
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

                faceCoordinates = wffrjni.recognizeFromImageFile(imageFileName);
                names = nameValues();
                confidence = confidenceValues();

                Release();

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

    public static int verifyImageForEnrollFromJpegBuffer(byte[] jpegBuffer) {
        try {
            semaphore.acquire();
            if (currentState > 0 && frInitialized == 1) {
                Release();
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

                faceCoordinates = VerifyImageForEnrollJpegBuffer(jpegBuffer, jpegBuffer.length);
                names = nameValues();
                confidence = confidenceValues();
                Release();

                semaphore.release();
                return 1;
            } else {
                semaphore.release();
                return 1;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 1;
        }
    }


    public static int verifyFrameForEnroll(byte[] framebuffer, int width, int height) {

        try {
            semaphore.acquire();
            if (currentState > 0 && frInitialized == 1) {
                Release();
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

                faceCoordinates = VerifyFrameForEnroll(framebuffer, width, height);
                names = nameValues();
                confidence = confidenceValues();
                Release();

                semaphore.release();
                return 1;
            } else {
                semaphore.release();
                return 1;
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
                Release();
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

                Release();

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

}
