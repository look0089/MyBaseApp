package com.hgl.bskbridgeaidl;

interface bskbridgeaidl {
	String aidlGetBridgeVersion();	//
	
	void aidlGetSerialConnectionStatus();	//
	
	void aidlReadPrivateData();
	
	void aidlWritePrivate(in char [] data);
	
	void aidlGetProductModel();	//
	
	void aidlGetProductName();	//
	
	void aidlSetProductName(String name); //
	
	void aidlGetProductID(); //
	
	void aidlGetSoftwareVersion();	//
	
	void aidlGetHardwareVersion();	//
	
	void aidlGetProtocolVersion();	//
	
	void aidlGetBootVersion();	//
	
	void aidlSingleOpenDoor();	//
	
	void aidlKeepOpenDoor();	//
	
	void aidlKeepCloseDoor();	//
	
	void aidlCloseDoorAndClearStatus();	//
	
	void aidlGetDoorStatus();	//
	
	void aidlGetDoorLockType();	//
	
	void aidlSetDoorLockType(int type);	//
	
	void aidlGetSingleOpenDoorDelayTime();	//
	
	void aidlSetSingleOpenDoorDelayTime(int second); //
	
	void aidlGetStartupDoorStatus();	//
	
	void aidlSetStartupDoorStatus(int status); //
	
	void aidlGetFLKEnableStatus();	//̬
	
	void aidlSetFLKEnableStatus(int status);	//״̬
	
	void aidlGetDoorCmdStatus();	//̬
	
	void aidlGetSingleOpenDoorPasswd();	//
	
	void aidlSetSingleOpenDoorPasswd(String passwd);	//
	
	void aidlGetKeepOpenDoorPasswd();	//
	
	void aidlSetKeepOpenDoorPasswd(String passwd);	//
	
	void aidlGetKeepCloseDoorPasswd(); //
	
	void aidlSetKeepCloseDoorPasswd(String passwd);	//
	
	void aidlGetResetPasswd();	//
	
	void aidlSetResetPasswd(String passwd);	//
	
	void aidlGetResetAndroidPasswd();	//
	
	void aidlSetResetAndroidPasswd(String passwd);	//
	
	void aidlGetLockTypeIsEMPasswd();	//
	
	void aidlSetLockTypeIsEMPasswd(String passwd);	//
	
	void aidlGetLockTypeIsECPasswd();	//
	
	void aidlSetLockTypeIsECPasswd(String passwd);	//
	void aidlGetRecoverDefConfigPasswd();	//
	
	void aidlSetRecoverDefConfigPasswd(String passwd);	//
	
	void aidlGetAlarmAndOpenDoorPasswd();	//
	
	void aidlSetAlarmAndOpenDoorPasswd(String passwd);	//
	
	void aidlGetAddRootPasswd();	//
	void aidlSetAddRootPasswd(String passwd);	//
	
	void aidlGetAddUserPasswd();	//
	void aidlSetAddUserPasswd(String passwd);	//
	
	void aidlGetDelOneCardPasswd();	//
	void aidlSetDelOneCardPasswd(String passwd);	//
	
	void aidlGetDelAllCardPasswd();	//
	void aidlSetDelAllCardPasswd(String passwd);	//
	
	void aidlSetTickTaskEn(int status);	//
	
	int aidlGetTickTaskEn();	//
	
	void aidlSetDaemonConfig(int En, int reset_flag, int interval, 
	
	int resetCount, int timeout1, int timeout2, int timeout3);	//
	
	void aidlGetDaemonConfig();	//
	
	void aidlGetEnv();
	
	void aidlSoftReset();	//
	
	void aidlSetLocalAuthEnableStatus(int status);	//
	//void aidlLocalAuthSetOnOff(int status);	//���ؼ�Ȩ���أ�0-��off,1-��on
	
	void aidlGetLocalAuthEnableStatus();
	//void aidlLocalAuthGetOnOff();
	
	void aidlLocalAuthFind(in char [] num);	//
	void aidlLocalAuthFindStr(String num);	//
	
	void aidlLocalAuthGetNumMoreInfo(in char [] num);	//
	void aidlLocalAuthGetNumMoreInfoStr(String num);
	
	void aidlLocalAuthGetTotalNum();	//
	
	void aidlLocalAuthGetAllNum();	//
	
	void aidlLocalAuthAddUser(in char [] num);	//
	void aidlLocalAuthAddUserStr(String num);
	
	void aidlLocalAuthAddRoot(in char [] num);	//
	void aidlLocalAuthAddRootString(String num);
	
	void aidlLocalAuthAddVistor(in char [] num, int count, in char [] start_time, in char [] end_time);	//
	
	void aidlLocalAuthAddVistorStr(String num, int count, in char [] start_time, in char [] end_time);
	
	void aidlLocalAuthDelOne(in char [] num);	//
	void aidlLocalAuthDelOneStr(String num);
	
	void aidlLocalAuthDelAll();	//
	
	void aidlLocalAuthFindAttached(in char [] num);	//
	
	void aidlLocalAuthFindAttachedStr(String num);
	
	void aidlLocalAuthAddAttached(in char [] num1, in char [] num2);	//
	
	void aidlLocalAuthAddAttachedStr(String num1, String num2);
	
	void aidlLocalAuthDelAttached(in char [] num1, in char [] num2);	//
	
	void aidlLocalAuthDelAttachedStr(String num1, String num2);
	
	void aidlSoftKeyBoard(char key);	//
	
	void aidlOTAStart(int firmwareLocation, int firmwareType, String firmwarePath);	//
	
	void aidlOTAAbort();	//
	
	void aidlGetHollStatus();	//̬
	
	void aidlGetLedStatus();	//
	
	void aidlSetLedStatus(int region, int status); //
	void aidlGetLockStatus();	//
	
	void aidlGetKeepOpenDoorEnableStatus();	//
	void aidlSetKeepOpenDoorEnableStatus(int status);	//

	void aidlGetKeepCloseDoorEnableStatus();	//̬
	
	void aidlSetKeepCloseDoorEnableStatus(int status);	//
	
	void aidlGetBuzzerStatus();	//
	
	void aidlSetbuzzerStatus(int status);	//̬
	
	void aidlGetLogEnableStatus();	//
	void aidlSetLogEnableStatus(int status);	//
	
	void aidlGetLogNum();	//
	
	void aidlGetAllLog(int del);	//
	
	void aidlClearLog();	//
	
	void aidlGetRTC();	//
	
	void aidlSetRTC(int year, int month, int mday, int hour, int min, int sec);	//
	
	void aidlBootOTAStart(int firmwareLocation, String firmwarePath);	//
	
	
	int aidlGetIrcutAin();	//
	int aidlSetIrcutAin(int value);	//
	
	int aidlGetIrcutBin();	//
	int aidlSetIrcutBin(int value);	//
	
	int aidlGetIRLED();		//
	int aidlSetIRLED(int value);
	
	int aidlGetWLED();	//
	int aidlSetWLED(int value);
	
	int aidlGetApSwUSB();	//
	int aidlSetApSwUSB(int value);
	
	int aidlGetPIR();	//
	
	void aidlGetModules();	//
	
	int aidlGetLS();	//
}
