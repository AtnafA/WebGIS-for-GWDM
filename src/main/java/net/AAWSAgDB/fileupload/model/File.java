package net.AAWSAgDB.fileupload.model;

import java.io.InputStream;
import java.util.List;

public final class File{
	private String title;
    public String corpodata;
    public String emp_name,emp_center,emp_process,respond_torequest,grant_proname;
    public String spatial_or_non,Le_pro_name_ov,Le_pro_code,Le_Pyear;
    public static String catagory;
    public static String na_cor;
    public String prepared_by,data_class,project_type,pro_type;
    public String cata_gory,cata_gory1,project_id_st,cata_goryI,region_tooltip,Pyear_to,Lpro_type;
    public String date,ds_subname,proj_name,proj_client;
    public int folderid,ds_subid, grant_proid,count_fgroup;
    public static String vecra;
    public String regnam,basename,class_name,datacl,na_name,file_gr,user_name,year,awa_yaer,class_nameII,scz_name;
    public String date_range,station_no,station_name,raw_data_avialable,pro_name,gerClassName,project_componet;
    public String mainAqc,wellTyp,email,orgName,phoneN,/*Parameter that handles All the well field names*/ptName,
    /*count and group data by operational and complation rport*/file_group;
    public String wellIndex,wellField,wellOwenrCat,dataCollectCase,wellStatus,potablityStatus,powerSource,scadaConn,scadasensorT,
    wellFuncStatus,wellFuncCondition;
    public int well_id,well_IDsearch4M,well_IDsearch4MOp,newlyAddedParamID,addwellFieldId,addwellOwnerId,addCasingMId,addwellTId,addAbanRId;
    public int user_id,Lname,Userdetail,pro_id,count_proname,Leg_Id_pro,ptId,regClassId,wellIDforReport,enumID,wellIdUWCRMessage;
    public int regid,scz_Id,well_idUpdate,pump_capacity,pumphead,pump_position,na_id=0,well_idforODUpdate,geolocID,wellIdGWOUMessage,
    		ownerID,well_idParam,logNumber,logNumberdate,well_ownerNameID,well_DepthID,well_idHChem,wellCount,well_DischargeID,well_SWLID,
    		well_DWLID,well_TRMTID,errorID;
    public String well_index,well_type,mainAquifer,Cyear,wellStatusOverlay,wellOwner,currentWS,reasonNonF,functionWellCondi,inActiveRe,potableS,
    potableSnonPotabableR,dataCond,recordDate,operationStartDate,wellStatusAfterConst,wellFieldOverlay,design_current,
    /*variables used for manage data*/regionName,ownerGroup,OunerName,constractYear,wellCode,wellIndexforReport,newlyAddeddParam,
    addwellFieldName,addwellOwnerName,addCasingMName,addwellTName,addAbanRName,wellCodeUpdate,constYearUpdate,userCompany,
    functionCondition,pumpStatus,pumpInstalDate,wellIndexUpdate,dataCondition,lastUpdatedDate,drillingLicense,
    errorType,errormessage,furtherInfo;
    public String wellIndexforODUpdate,enumCompanyName,enumEmail,enumPhone,waterUseLicense,oprerationStartDate,updateGOPUMessage,dsicription,
    WellWCRSubDiscription,updateGWODicussion,updateWCRMessage,updateWCRDicussion,wellIndex4Query,wellField4Query,ownerCat,casingMaterial,
    mainAquiferUp,wellType,abandonedReason,sealedYN,dateSealed,functionWellCon,pumpStatusUpdate,pumpInstalledDate,pumpheadUpdate,gen_status,
    wellIndex4Query_ownerName,wellField4Query_ownerName,wellIndex4Query_DepthRange,wellField4Query_DepthRange;
    public double wellDepth,specificCapacity,wellYield,wellSWL,wellDWL,/*Managing Parameter*/wellYieldM,pumpPosition,abstractionRate,pumpPositionOpn,
    wellSWLOpen,wellDWLOpn,easting,northing,longT,lat,elivation,wellDepthUpdate,wellYieldUpdate,swlUpdate,dwlUpdate,specificCapacityUpdate
    ,abstraction_rate,lat4Query,lng4Query,largCasing,telescopCasing,dischargeRate,pumpCapacity,pumpPositionUpdate,gen_power;
    public double file_size, locx,locy,locx2,locy2,Le_locx,Le_locy,Le_locx2,Le_locy2,wellEficiency,well_diamM,geoLocxWellRepo,geoLocYWellRepo,
    lat4Query_ownerName,lng4Query_ownerName,lat4Query_DepthRange,lng4Query_DepthRange,longtude,latitude,pH,totalHardness_caco3,flouride,soduim,
    chloride,calcium,iron,manganese,magnisium,temprature,sulfate,nitrate,TDS,electricConductivity,potasium,bicarbonet,posphate,lat4Query_DischargeRange,
    lng4Query_DischargeRange,lat4Query_SWLRange,lng4Query_SWLRange,lat4Query_DWLRange,lng4Query_DWLRange,lat4Query_TRMTRange,lng4Query_TRMTRange;
    public java.util.Date constYear,constYearM;
    public String well_indexLog,userName,actionTook,dateActionTook,dataSheet,well_indexLogdate,userNamedate,actionTookdate,dateActionTookdate,
    dataSheetdate,wellFieldHChem,wellIndexHChem,wellownerGroup,waterColor,waterOdor,wellIndex4Query_DischargeRange,wellField4Query_DischargeRange,
    wellIndex4Query_SWLRange,wellField4Query_SWLRange,wellIndex4Query_DWLRange,wellField4Query_DWLRange,wellIndex4Query_TRMTRange,
    wellField4Query_TRMTRange,sucityZoneName,subcityZone, subcityzone;
    
 // Declaring properties as private shields them from unauthorized external overrides
    private int Id_pro;
    private String pro_name_ov;
    private String pro_code;
    private String Pyear;
    private String Gpro_type;
    private int count_project;
    
    public int getWellId() {
		return Id_pro;
	}
	public void setWellId(int id_pro) {
		Id_pro = id_pro;
	}
	public String getWellField() {
		return pro_name_ov;
	}
	public void setWellField(String pro_name_ov) {
		this.pro_name_ov = pro_name_ov;
	}
	public String getWellIndex() {
		return pro_code;
	}
	public void setWellIndex(String pro_code) {
		this.pro_code = pro_code;
	}
	public String getPyear() {
		return Pyear;
	}
	public void setPyear(String pyear) {
		Pyear = pyear;
	}
	public String getWellType() {
		return Gpro_type;
	}
	public void setWellType(String gpro_type) {
		Gpro_type = gpro_type;
	}
	public int getCount_project() {
		return count_project;
	}
	public void setCount_project(int count_project) {
		this.count_project = count_project;
	}
	public File() {
		
	}
	
	public File(int id){
    this.user_id=id;
    }
    public File(int wellIDforReport, String WellWCRSubDiscription, String wellIndexforReport) { 
		this.wellIDforReport=wellIDforReport;
    	this.wellIndexforReport=wellIndexforReport;
    	this.WellWCRSubDiscription=WellWCRSubDiscription;
	}
    public File(int well_codeId, String well_Codename,String dsicription, int noValue) { 
		this.regid=well_codeId;
    	this.regnam=well_Codename;
    	this.dsicription=dsicription;
	}
    public File(String respond_torequest){
        this.respond_torequest=respond_torequest;
        }
    public File(int newlyAddedParamID, String newlyAddeddParam,Object obj) { 
		this.newlyAddedParamID=newlyAddedParamID;
    	this.newlyAddeddParam=newlyAddeddParam;
	}
    public File(int addwellFieldId, String addwellFieldName,int sss) { 
		this.addwellFieldId=addwellFieldId;
    	this.addwellFieldName=addwellFieldName;
	}
    public File(int addwellOwnerId, String addwellOwnerName,int sss,Object obj) { 
		this.addwellOwnerId=addwellOwnerId;
    	this.addwellOwnerName=addwellOwnerName;
	}
    public File(int addCasingMId, String addCasingMName,Object obj,int sss) { 
		this.addCasingMId=addCasingMId;
    	this.addCasingMName=addCasingMName;
	}
    public File(int addwellTId, String addwellTName,Object obj,int sss,int contlr) { 
		this.addwellTId=addwellTId;
    	this.addwellTName=addwellTName;
	}
    public File(int addAbanRId, String addAbanRName,int sss,int contlr,Object obj) { 
		this.addAbanRId=addAbanRId;
    	this.addAbanRName=addAbanRName;
	}
    public File(int wellIdUWCRMessage, String updateWCRMessage,String updateWCRDicussion,File kk){ 
		this.wellIdUWCRMessage=wellIdUWCRMessage;
    	this.updateWCRMessage=updateWCRMessage;
    	this.updateWCRDicussion=updateWCRDicussion;
    	//System.out.println("Message from Update WCR Entities Well Index is "+updateWCRMessage+" and Well Id is "+wellIdUWCRMessage);
	}
    public File(int wellIdGWOUMessage, String updateGOPUMessage,String updateGWODicussion,double kk){ 
		this.wellIdGWOUMessage=wellIdGWOUMessage;
    	this.updateGOPUMessage=updateGOPUMessage;
    	this.updateGWODicussion=updateGWODicussion;
    	//System.out.println("Message from Update GWOD Entities Well Index is "+updateGOPUMessage+" and Well Id is "+wellIdGWOUMessage);
	}
    public File(String pro_type,float latlong, int count_project){
    	this.pro_type=pro_type;
    	this.count_project=count_project;
    }
  //File Access controller
    public File(int well_id,String wellCode,String wellInde,String mainAqc,String wellTyp,java.sql.Date constYear,String email,String orgName,
    		String phoneN){
  	      this.well_id=well_id;
    	 // this.wellCode = wellCode;
          this.wellIndex = wellInde;
          this.mainAqc = mainAqc;
          this.wellTyp=wellTyp;
          this.constYear=constYear;
          this.email=email;
          this.orgName=orgName;
          this.phoneN=phoneN;
      } 
    //parameters to manage Well completion Report
    public File(int well_id,String wellField,String regionName,String ownerGroup,String OunerName,double wellYield,double wellDepth,
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
    //parameters to manage Operational Data
    public File(int well_id,String wellField,String regionName,String ownerGroup,String OunerName,String dataCondition,double wellDepth,
    		String recordDate,String wellIndex,String wellCode,String wellStatus,String dataCollectCase){
    	this.well_IDsearch4MOp=well_id;
        this.regionName=regionName;
        this.wellField=wellField;
        this.ownerGroup=ownerGroup;
        this.OunerName=OunerName;
        this.dataCondition=dataCondition;
        this.wellDepth=wellDepth;
        this.lastUpdatedDate=recordDate;
        this.wellIndex=wellIndex;
        this.wellCode=wellCode;
        this.wellStatus=wellStatus;
        this.dataCollectCase=dataCollectCase;
        }
    //Populating Well Index over Map (Default)
    public File(int Id_pro, String pro_name,double locx,double locy, double locx2,double locy2,String Pro_code,String Pyear,String Gpro_type,
    		String subcityzone){
    	this.Id_pro=Id_pro;
        this.pro_name_ov=pro_name;
        this.locx=locx;
        this.locy=locy;
        this.locx2=locx2;
        this.locy2=locy2;
        this.pro_code=Pro_code;
        this.Pyear=Pyear;
        this.Gpro_type=Gpro_type;
        this.subcityzone=subcityzone;
        }
    //Populating Well Index over Map using Well field Legend Filtering
    public File(int Id_pro, String pro_name, double locx,double locy, double locx2,double locy2,String Pro_code,String Pyear, String pro_type){
    	this.Leg_Id_pro=Id_pro;
        this.Le_pro_name_ov=pro_name;
        this.Le_locx=locx;
        this.Le_locy=locy;
        this.Le_locx2=locx2;
        this.Le_locy2=locy2;
        this.Le_pro_code=Pro_code;
        this.Le_Pyear=Pyear;
        this.Lpro_type=pro_type;
        }
    //Populating Well Index over Map (By Well Index Searching)
    public File(int Id_pro, String pro_name,double locx,double locy, double locx2,double locy2,String Pro_code,String Pyear,String Gpro_type,
    		String not_avialable,String NoValue){
    	this.Id_pro=Id_pro;
        this.pro_name_ov=pro_name;
        this.pro_code=Pro_code;
        this.locx=locx;
        this.locy=locy;
        this.locx2=locx2;
        this.locy2=locy2;
        this.Pyear=Pyear;
        this.Gpro_type=Gpro_type;
    }
    public File(String well_index,double geoLocx,double geoLocY,String well_Owner,String Cyear,double wellDepth,double wellYield,double wellSWL,
    		double wellDWL,double speCapacity,String wellStatus,String wellFieldOverlay,double pumpPosition,
    		double abstractionRate,double wellSWLOpen,double wellDWLOpn,String currentWS,String reasonNonF,String functionWellCondi,String inActiveRe,
    		String potableS,String potableSnonPotabableR,String dataCond,String recordDate,String operationStartDate,double pumpPositionOpn,
    		String design_current,String sucityZoneName){
    	this.well_index=well_index;
        this.wellOwner=well_Owner;
        this.geoLocxWellRepo=geoLocx;
        this.geoLocYWellRepo=geoLocY;
        this.Cyear=Cyear; 
        this.wellDepth=wellDepth;
        this.wellYield=wellYield;
        this.wellSWL=wellSWL;
        this.wellDWL=wellDWL;
        this.specificCapacity=speCapacity;
        this.wellStatusOverlay=wellStatus;
        this.wellFieldOverlay=wellFieldOverlay;
        this.pumpPosition=pumpPosition;
        this.abstractionRate=abstractionRate;
        this.wellSWLOpen=wellSWLOpen;
        this.wellDWLOpn=wellDWLOpn;
        this.currentWS=currentWS;
        this.reasonNonF=reasonNonF;
        this.functionWellCondi=functionWellCondi;
        this.inActiveRe=inActiveRe;
        this.potableS=potableS;
        this.potableSnonPotabableR=potableSnonPotabableR;
        this.dataCond=dataCond;
        this.recordDate=recordDate;
        this.operationStartDate=operationStartDate;
        this.pumpPositionOpn=pumpPositionOpn;
        this.design_current=design_current;
        this.sucityZoneName=sucityZoneName;
        }
    public File (int well_id,String wellCode, double easting,double northing,double longt,double lat,double elevation,String cnYear,
			  double wellDepth,double wellYield,double SWL,double DWL,double specificCapacity,String userCompany,String userEmail,
			  String userPhone,String wellIndex,String drillingLicense,int geolocID,int ownerID,String ownerCat,double largCasing,
			  double telescopCasing,String casingMaterial,String mainAquiferUp,String wellType,String abandonedReason,String sealedYN,
			  String dateSealed,String pumpStatusUpdate,String pumpInstalledDate,double pumpCapacity,String pumpheadUpdate,
			  double pumpPositionUpdate,double dischargeRate,double gen_power,String gen_status){
    	this.well_idUpdate=well_id;
    	this.wellCodeUpdate=wellCode;
    	this.easting = easting;
    	this.northing = northing;
    	this.longT=longt;
    	this.lat = lat;
    	this.elivation=elevation;
    	this.constYearUpdate=cnYear;
    	this.wellDepth=wellDepth;
    	this.wellYieldUpdate=wellYield;
    	this.swlUpdate=SWL;
    	this.dwlUpdate=DWL;
    	this.specificCapacityUpdate=specificCapacity;
    	this.userCompany=userCompany;
    	this.functionCondition=userEmail;
    	this.pumpStatus=userPhone;
    	this.wellIndexUpdate=wellIndex;
    	this.drillingLicense=drillingLicense;
    	this.geolocID=geolocID;
    	this.ownerID=ownerID;
    	this.ownerCat=ownerCat;
    	this.largCasing=largCasing;
    	this.telescopCasing=telescopCasing;
    	this.casingMaterial=casingMaterial;
    	this.mainAquiferUp=mainAquiferUp;
    	this.wellType=wellType;
    	this.abandonedReason=abandonedReason;
    	this.sealedYN=sealedYN;
    	this.dateSealed=dateSealed;
    	this.pumpStatusUpdate=pumpStatusUpdate;
    	this.pumpInstalledDate=pumpInstalledDate;
    	this.pumpCapacity=pumpCapacity;
    	this.pumpheadUpdate=pumpheadUpdate;
    	this.pumpPositionUpdate=pumpPositionUpdate;
    	this.dischargeRate=dischargeRate;
    	this.gen_power=gen_power;
    	this.gen_status=gen_status;
	}
    public File(int well_id,double lat,double lng, String wellIndex,String wellfield,String Constyear, int nn){
    	this.well_idParam=well_id;
    	this.lat4Query=lat;
    	this.lng4Query=lng;
    	this.wellIndex4Query=wellIndex;
    	this.wellField4Query=wellfield;
    	this.year=Constyear;
    }
    public File(int well_id,double lat,double lng, String wellIndex,String wellfield,String Active){
    	this.well_idParam=well_id;
    	this.lat4Query=lat;
    	this.lng4Query=lng;
    	this.wellIndex4Query=wellIndex;
    	this.wellField4Query=wellfield;
    }    
    public File(String ptName,int ptId){
    	this.ptName=ptName;
    	this.ptId=ptId;
    }
    public File(String ptOName,int ptOId, List mm){
    	this.gerClassName=ptOName;
    	this.regClassId=ptOId;
    }
    public File(String pt_comoponetName,int ptcomonentId, List mm,List kk){
    	this.scz_name=pt_comoponetName;
    	this.scz_Id=ptcomonentId;
    }
    //Parameters accessed to Update Groundwater Operational data
    public File(int well_id,String wellIndex,String enumCompanyName,String enumEmail,String enumPhone,String waterUseLicense,
    		String oprerationStartDate,int userID){
        this.well_idforODUpdate=well_id;
        this.wellIndexforODUpdate=wellIndex;
        this.enumCompanyName=enumCompanyName;
        this.enumEmail=enumEmail;
        this.enumPhone=enumPhone;
        this.waterUseLicense=waterUseLicense;
        this.oprerationStartDate=oprerationStartDate;
        this.enumID=userID;
        }
    public File(String file_group,int count_fgroup, float size_file){
        this.file_group=file_group;
        this.count_fgroup=count_fgroup;
        this.file_size=size_file;
        }
    public File(int date,String year,float bb, int size_file){
        this.year=year;
        }
   public File(int date,String region_tooltip,int count_proname,int non_val){
	   this.region_tooltip=region_tooltip;
        this.count_proname=count_proname;
        }
    public File(String pro_name,int pro_id, String count_proname){
        this.pro_name=pro_name;
        this.pro_id=pro_id;
        }
    public File(String emp_name, int def_value,String emp_center,String emp_process){
        this.emp_name=emp_name;
        this.emp_center=emp_center;
        this.emp_process=emp_process;
        }
    public File(int ds_subid, int def_value,int def_val, String ds_subname){
        this.ds_subid=ds_subid;
        this.ds_subname=ds_subname;
        }
	public File(String spa_name, int cat_id,int folid,String raw_data,String folname) { 
		this.folderid=folid;
		this.raw_data_avialable=raw_data;
	}
	public File(int grant_proid, int [] not_val, String grant_proname){ 
		this.grant_proid=grant_proid;
    	this.grant_proname=grant_proname;
	}
	public File(int claid, String clasname,String sss,String pro_id,int mm,int All){ 
    	this.class_nameII=clasname;
    	this.cata_goryI=sss;
    	this.project_id_st=pro_id;
    	 //System.out.println("Project Name from Dao "+cata_goryI); 
	}
   public File(int logNumber, String well_indexLog,String userName,String actionTook,String dateActionTook,String dataSheet){ 
		this.logNumber=logNumber;
		this.well_indexLog=well_indexLog;
		this.userName=userName;
		this.actionTook=actionTook;
		this.dateActionTook=dateActionTook;
		this.dataSheet=dataSheet;
	}
   public File(int logNumberdate, String well_indexLogdate,String userNamedate,String actionTookdate,String dateActionTookdate,String dataSheetdate,
		   int nonValue){ 
		this.logNumberdate=logNumberdate;
		this.well_indexLogdate=well_indexLogdate;
		this.userNamedate=userNamedate;
		this.actionTookdate=actionTookdate;
		this.dateActionTookdate=dateActionTookdate;
		this.dataSheetdate=dataSheetdate;
	}
   public File(int well_id,double lat,double lng, String wellIndex,String wellfield,String Active,String no_data,int wellCount){
   	this.well_ownerNameID=well_id;
   	this.lat4Query_ownerName=lat;
   	this.lng4Query_ownerName=lng;
   	this.wellIndex4Query_ownerName=wellIndex;
   	this.wellField4Query_ownerName=wellfield;
   	this.wellCount=wellCount;
   } 
   public File(int well_id,double lat,double lng, String wellIndex,String wellfield,String Active,String no_data,String no_dataI){
	   	this.well_DepthID=well_id;
	   	this.lat4Query_DepthRange=lat;
	   	this.lng4Query_DepthRange=lng;
	   	this.wellIndex4Query_DepthRange=wellIndex;
	   	this.wellField4Query_DepthRange=wellfield;
	   }
   public File(int well_id,double lat,double lng, String wellIndex,String wellfield,String Active,String no_data,String no_dataI,String notDefined){
	   	this.well_DischargeID=well_id;
	   	this.lat4Query_DischargeRange=lat;
	   	this.lng4Query_DischargeRange=lng;
	   	this.wellIndex4Query_DischargeRange=wellIndex;
	   	this.wellField4Query_DischargeRange=wellfield;
	   } 
   public File(int well_id,double lat,double lng, String wellIndex,String wellfield,String Active,String no_data,String no_dataI,String notDefined,
		   String undefined){
	   	this.well_SWLID=well_id;
	   	this.lat4Query_SWLRange=lat;
	   	this.lng4Query_SWLRange=lng;
	   	this.wellIndex4Query_SWLRange=wellIndex;
	   	this.wellField4Query_SWLRange=wellfield;
	   } 
   public File(int well_id,double lat,double lng, String wellIndex,String wellfield,String Active,String no_data,String no_dataI,String notDefined,
		   String undefined,String NotDefined){
	   	this.well_DWLID=well_id;
	   	this.lat4Query_DWLRange=lat;
	   	this.lng4Query_DWLRange=lng;
	   	this.wellIndex4Query_DWLRange=wellIndex;
	   	this.wellField4Query_DWLRange=wellfield;
	   } 
   public File(int well_id,double lat,double lng, String wellIndex,String wellfield,String Active,String no_data,String no_dataI,String notDefined,
		   String undefined,String NotDefined,String nonAnswer){
	   	this.well_TRMTID=well_id;
	   	this.lat4Query_TRMTRange=lat;
	   	this.lng4Query_TRMTRange=lng;
	   	this.wellIndex4Query_TRMTRange=wellIndex;
	   	this.wellField4Query_TRMTRange=wellfield;
	   } 
 //parameters to manage Well completion Report
   public File(int well_id,String wellField,String wellIndex,String wellownerGroup,double longtude,double latitude,
   		double pH,double totalHardness_caco3,double flouride,double soduim,double chloride,double calcium,double iron,double manganese,double magnisium,
   		double temprature,double sulfate,double nitrate,double TDS,double electricConductivity,double potasium,double bicarbonet,double posphate,
   		String waterColor,String waterOdor,String subcityZone){
       this.well_idHChem=well_id;
       this.wellFieldHChem=wellField;
       this.wellIndexHChem=wellIndex;
       this.wellownerGroup=wellownerGroup;
       this.longtude=longtude;
       this.latitude=latitude;
       this.pH=pH;
       this.totalHardness_caco3=totalHardness_caco3;
       this.flouride=flouride;
       this.soduim=soduim;
       this.chloride=chloride;
       this.calcium=calcium;
       this.iron=iron;
       this.manganese=manganese;
       this.magnisium=magnisium;
       this.temprature=temprature;
       this.sulfate=sulfate;
       this.nitrate=nitrate;
       this.TDS=TDS;
       this.electricConductivity=electricConductivity;
       this.potasium=potasium;
       this.bicarbonet=bicarbonet;
       this.posphate=posphate;
       this.waterColor=waterColor;
       this.waterOdor=waterOdor;
       this.subcityZone=subcityZone;
   }
   
		public File(int folid,String folname,byte[]aa){
			this.folderid=folid;
	    	//this.folder=folname;
		}	
		public File(int re_id,String re_name,InputStream io) {
			this.regid=re_id;
			this.regnam=re_name;
		}				
	    public void setTitle(String title) {
	        this.title = title;
	    }
	    public String getTitle() {
	        return this.title;
	    }	
	    public File(int errorID,String errorType,String errormessage,String furtherInfo,int noValue) {
			this.regid=errorID;
			this.regnam=errorType;
			this.dsicription=errormessage;
			this.furtherInfo=furtherInfo;
		}	
}