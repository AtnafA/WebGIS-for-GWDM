package net.AAWSAgDB.fileupload;

public class ValuePair {
	public byte[] ddat;
	public byte[] LDdat;
	public long size;//For file size greater than 2GB
	public String name,contentType;
	public double longtCheck=0;
	public double latCheck=0;
	public double eastingCheck=0;
	public double northingCheck=0;
	public double l_elevation=0;
	public double well_depthCheck=0;
	public double largecasingCheck=0;
	public double telescopedcasingCheck=0;
	public double pump_capacityCheck=0;
	public double pump_positionCheck=0;
	public double abstraction_rateCheck=0;
	public double yield_lpsCheck=0;
	public double swl_mCheck=0;
	public double dwl_mCheck=0;
	public double specific_capacityCheck=0;
	public String wellcodeCheck="",well_typeCheck="";
	public String main_aquiferCheck="";
	public String drilling_licenseCheck="";
	public String casing_mtrlCheck="";
	public String wellownercatgoryCheck="";
	public String pump_statusCheck="";
	public String pumpheadCheck="";
	public String conn_avialableCheck="";
	public String scada_statusCheck="";
	public String emailCheck="";
	public String orgnameCheck="";
	public String phoneCheck="";
	public String cntn_yearCheck=null;
	public int well_TDSID,well_ECID,well_FID,well_FeID,well_No3ID,well_tempID,well_IDsearch4M,well_ID,well_IDGW,user_id,user_idUpdated,
	well_IDsearch4MOp,repo_id,requestStatusCache,requestId, requestStatus;
	public double lat_TDS,lng_TDS,lat_EC,lng_EC,lat_F,lng_F,lat_Fe,lng_Fe,lat_No3,lng_No3,lat_temp,lng_temp,wellYieldM,wellDepth,wellYieldUpdate,
	wellDepthUpdate,wellDepthGW,wellDepth4Mop;
	public String wellIndex_TDS,wellField_TDS,wellIndex_EC,wellField_EC,wellIndex_F,wellField_F,wellIndex_Fe,wellField_Fe,wellIndex_No3,wellField_No3,
	wellIndex_temp,wellField_temp,regionName,wellField,ownerGroup,OunerName,constractYear,wellIndex,wellCode,wellStatus,dataCollectCase,regionNameUpdate,
	wellFieldUpdate,ownerGroupUpdate,OunerNameUpdate,constractYearUpdate,wellIndexUpdate,wellCodeUpdate,wellStatusUpdate,dataCollectCaseUpdate,
	wellFieldGW,regionNameGW,ownerGroupGW,OunerNameGW,dataCondition,lastUpdateGW,wellIndexGW,wellCodeGW,wellStatusGW,dataCollectCaseGW,fullName,
	userName,userType,status,fullNameUpdeated,userNameUpdated,userTypeUpdeted,statusUpdated,regionName4MOp,wellField4MOp,ownerGroup4MOp,OunerName4MOp,
	dataCondition4MOp,lastUpdatedDate4MOp,wellIndex4MOp,wellCode4MOp,wellStatus4MOp,dataCollectCase4MOp,fileName,filetype,recordate,dataCategory,
	requestedUserName, orgName,fileNameResponse,dataCategoryResponse;
	
	public ValuePair(double longtCheck,double latCheck,double eastingCheck,double northingCheck,double l_elevation,double well_depthCheck,
			double largecasingCheck,double telescopedcasingCheck, double pump_capacityCheck,double pump_positionCheck,double abstraction_rateCheck,
			double yield_lpsCheck,double swl_mCheck,double dwl_mCheck,double specific_capacityCheck,String wellcodeCheck,String well_typeCheck,
			String main_aquiferCheck,String drilling_licenseCheck,String casing_mtrlCheck,String wellownercatgoryCheck,
			String pump_statusCheck,String pumpheadCheck,String conn_avialableCheck,String scada_statusCheck,String emailCheck,String orgnameCheck,
			String phoneCheck,String cntn_yearCheck){
		this.longtCheck=longtCheck;
		this.latCheck=latCheck;
		this.eastingCheck=eastingCheck;
		this.northingCheck=northingCheck;
		this.l_elevation=l_elevation;
		this.well_depthCheck=well_depthCheck;
		this.largecasingCheck=largecasingCheck;
		this.telescopedcasingCheck=telescopedcasingCheck;
		this.pump_capacityCheck=pump_capacityCheck;
		this.pump_positionCheck=pump_positionCheck;
		this.abstraction_rateCheck=abstraction_rateCheck;
		this.yield_lpsCheck=yield_lpsCheck;
		this.swl_mCheck=swl_mCheck;
		this.dwl_mCheck=dwl_mCheck;
		this.specific_capacityCheck=specific_capacityCheck;
		this.wellcodeCheck=wellcodeCheck;
		this.well_typeCheck=well_typeCheck;
		this.main_aquiferCheck=main_aquiferCheck;
		this.drilling_licenseCheck=drilling_licenseCheck;
		this.casing_mtrlCheck=casing_mtrlCheck;
		this.wellownercatgoryCheck=wellownercatgoryCheck;
		this.pump_statusCheck=pump_statusCheck;
		this.pumpheadCheck=pumpheadCheck;
		this.conn_avialableCheck=conn_avialableCheck;
		this.scada_statusCheck=scada_statusCheck;
		this.emailCheck=emailCheck;
		this.orgnameCheck=orgnameCheck;
		this.phoneCheck=phoneCheck;
		this.cntn_yearCheck=cntn_yearCheck;

	}
	public ValuePair(int well_id,double lat,double lng, String wellIndex,String wellfield){
	   	this.well_TDSID=well_id;
	   	this.lat_TDS=lat;
	   	this.lng_TDS=lng;
	   	this.wellIndex_TDS=wellIndex;
	   	this.wellField_TDS=wellfield;
	}
	public ValuePair(int well_id,double lat,double lng, String wellIndex,String wellfield, String noData){
	   	this.well_ECID=well_id;
	   	this.lat_EC=lat;
	   	this.lng_EC=lng;
	   	this.wellIndex_EC=wellIndex;
	   	this.wellField_EC=wellfield;
	}
	public ValuePair(int well_id,double lat,double lng, String wellIndex,String wellfield, String noData,String noValue){
	   	this.well_FID=well_id;
	   	this.lat_F=lat;
	   	this.lng_F=lng;
	   	this.wellIndex_F=wellIndex;
	   	this.wellField_F=wellfield;
	}
	public ValuePair(int well_id,double lat,double lng, String wellIndex,String wellfield, String noData,String noValue,String nullValue){
	   	this.well_FeID=well_id;
	   	this.lat_Fe=lat;
	   	this.lng_Fe=lng;
	   	this.wellIndex_Fe=wellIndex;
	   	this.wellField_Fe=wellfield;
	}
	public ValuePair(int well_id,double lat,double lng, String wellIndex,String wellfield, String noData,String noValue,String nullValue,String ValNo){
	   	this.well_No3ID=well_id;
	   	this.lat_No3=lat;
	   	this.lng_No3=lng;
	   	this.wellIndex_No3=wellIndex;
	   	this.wellField_No3=wellfield;
	}
	public ValuePair(int well_id,double lat,double lng, String wellIndex,String wellfield, String noData,String noValue,String nullValue,
			String ValNo,String noValueI){
	   	this.well_tempID=well_id;
	   	this.lat_temp=lat;
	   	this.lng_temp=lng;
	   	this.wellIndex_temp=wellIndex;
	   	this.wellField_temp=wellfield;
	  //System.out.println("Test Temp data" + wellIndex_temp);
	}
	 //parameters to manage Well completion Report
    public ValuePair(int well_id,String wellField,String regionName,String ownerGroup,String OunerName,double wellYield,double wellDepth,
    		String constYear,String wellIndex,String wellCode,String wellStatus,String dataCollectCase){
    	this.well_IDsearch4M=well_id;
        this.regionName=regionName;
        this.wellField=wellField;
        this.ownerGroup=ownerGroup;
        this.OunerName=OunerName;
        this.wellYieldM=wellYield;
        this.wellDepth=wellDepth;
        this.constractYear=constYear;
        this.wellIndex=wellIndex;
        this.wellCode=wellCode;
        this.wellStatus=wellStatus;
        this.dataCollectCase=dataCollectCase;
        }
  //parameters to manage Well completion Report
    public ValuePair(int well_id,String wellField,String regionName,String ownerGroup,String OunerName,double wellYield,double wellDepth,
    		String constYear,String wellIndex,String wellCode,String wellStatus,String dataCollectCase,String NoValue){
    	this.well_ID=well_id;
        this.regionNameUpdate=regionName;
        this.wellFieldUpdate=wellField;
        this.ownerGroupUpdate=ownerGroup;
        this.OunerNameUpdate=OunerName;
        this.wellYieldUpdate=wellYield;
        this.wellDepthUpdate=wellDepth;
        this.constractYearUpdate=constYear;
        this.wellIndexUpdate=wellIndex;
        this.wellCodeUpdate=wellCode;
        this.wellStatusUpdate=wellStatus;
        this.dataCollectCaseUpdate=dataCollectCase;
        }
  //parameters to manage Well completion Report
    public ValuePair(int well_id,String wellField,String regionName,String ownerGroup,String OunerName,String dataCondition, double wellDepth,
    		String latsUpdate,String wellIndex,String wellCode,String wellStatus,String dataCollectCase){
    	this.well_IDGW=well_id;
        this.wellFieldGW=wellField;
        this.regionNameGW=regionName;
        this.ownerGroupGW=ownerGroup;
        this.OunerNameGW=OunerName;
        this.dataCondition=dataCondition;
        this.wellDepthGW=wellDepth;
        this.lastUpdateGW=latsUpdate;
        this.wellIndexGW=wellIndex;
        this.wellCodeGW=wellCode;
        this.wellStatusGW=wellStatus;
        this.dataCollectCaseGW=dataCollectCase;
        }
  //parameters to Access Database User
    public ValuePair(int user_id,String fullName,String userName,String userType,String status){
    	this.user_id=user_id;
        this.fullName=fullName;
        this.userName=userName;
        this.userType=userType;
        this.status=status;
        }
  //parameters to Access Database User
    public ValuePair(int user_id,String fullName,String userName,String userType,String status,String nullValue){
    	this.user_idUpdated=user_id;
        this.fullNameUpdeated=fullName;
        this.userNameUpdated=userName;
        this.userTypeUpdeted=userType;
        this.statusUpdated=status;
        }
  //parameters to manage Operational Data
    public ValuePair(int well_id,String wellField,String regionName,String ownerGroup,String OunerName,String dataCondition,double wellDepth,
    		String recordDate,String wellIndex,String wellCode,String wellStatus,String dataCollectCase,int noValue){
    	this.well_IDsearch4MOp=well_id;
        this.regionName4MOp=regionName;
        this.wellField4MOp=wellField;
        this.ownerGroup4MOp=ownerGroup;
        this.OunerName4MOp=OunerName;
        this.dataCondition4MOp=dataCondition;
        this.wellDepth4Mop=wellDepth;
        this.lastUpdatedDate4MOp=recordDate;
        this.wellIndex4MOp=wellIndex;
        this.wellCode4MOp=wellCode;
        this.wellStatus4MOp=wellStatus;
        this.dataCollectCase4MOp=dataCollectCase;
        //System.out.println("Data from DB is = "+dataCollectCase);
        }
    public ValuePair(int repo_id,String fileName,String filetype,String recordate,String dataCategory,int requestStatusCache ) {
    	this.repo_id=repo_id;
    	this.fileName=fileName;
    	this.filetype=filetype;
    	this.recordate=recordate;
    	this.dataCategory=dataCategory;
    	this.requestStatusCache =requestStatusCache ;
    }
    
    // Include clean public getter/setter routines
    public int getRequestStatusCache() { return requestStatusCache ; }
    public void setRequestStatusCache(int requestStatusCache) { this.requestStatusCache  = requestStatusCache; }
    
    public ValuePair(int requestId,String userName,String orgName,String fileName,String dataCategory,int requestStatus,int notneeded) {
    	this.requestId=requestId;
    	this.requestedUserName=userName;
    	this.orgName=orgName;
    	this.fileNameResponse=fileName;
    	this.dataCategoryResponse=dataCategory;
    	this.requestStatus = requestStatus ;
    }
    
    
    public ValuePair(byte aa[],String filename,String contenttyipe, long file_size){
    	this.ddat=aa;
    	this.name =filename;
    	this.contentType=contenttyipe;
    	this.size=file_size;
       }
}