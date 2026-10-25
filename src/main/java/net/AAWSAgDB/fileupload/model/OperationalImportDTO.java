package net.AAWSAgDB.fileupload.model;

import java.sql.Date;

public class OperationalImportDTO {
    private String wellCellIndex;
    private String wellCellCode;
	private String waterUsageLicence;
    private Date dataRecordedDate;
    private String enumratorCampanyN;
    private String enumratorName;
    private String enumratoremail;
    private String enumratorPhone;
    private String currentWellStatus;
    private Date dateOfFunctionStoped;
    private String reasonNonFunctioning;
    private String functioningwellCondn;
    private String inActiveReason;
    private String wellHeadCondition;
    private String poorWellHeadReason;
    private String drainageCondition;
    private String powerSource;
    private String standByGeneratorAvialable;
    private String generatorStatus;
    private double generatorPower;
    private String observationPiPAvialable;
    private String statusOfObservationPIP;
    private String SCADAConnection;
    private String SCADAstatus;
    private String monitoringSencorIns;
    private String designedGeneratoCapacity;
    private double staticWL;
    private double dynamicWL;
	private double abstractionRate;
    private double elecConductivity;
    private double pHValue;
    private double temprature;
    private double testDurationHrs;
    private String waterColor;
    private String waterOdor;
    private String SensorTypes [];
    private Date operationStartedDate;
    private String goodcondition;
    private String goodwtrquality;
    private String highdrawdown;
    private String yielddeclining;
    private String rapidyielddecline;
    private String poorwaterQuality_mineral;
    private String extrnalpolun;
    private String changepumpcapacity;
    private String loyerpumpposition;
    private String wellcleaning;
    private String comprehensive_reha;
    private String qualitytest_disinf;
    private String mantaindrainage;
    private String headmantainreconst;
    private String abandonmentseal;
    private String abandonmentreason;
    private String rehabconducted;
    private Date rehabdate;
    private String rehabCompanyName;
    private String rehabEmail;
    private String rehabPhone;
    private double swl_before;
    private double swl_after;
    private double dwl_before;
    private double dwl_after;
    private double pump_pos;
    private double pump_power;
    private double yield;
    private double spfc_capacity;
    private double effciency;
    private double hconductivity;
    private double transimitivity;
    private double storativity;
    private String waterquality;
    private String qltyproblem;
    
    public String getGoodcondition() {
		return goodcondition;
	}
	public void setGoodcondition(String goodcondition) {
		this.goodcondition = goodcondition;
	}
	public String getGoodwtrquality() {
		return goodwtrquality;
	}
	public void setGoodwtrquality(String goodwtrquality) {
		this.goodwtrquality = goodwtrquality;
	}
	public String getHighdrawdown() {
		return highdrawdown;
	}
	public void setHighdrawdown(String highdrawdown) {
		this.highdrawdown = highdrawdown;
	}
	public String getYielddeclining() {
		return yielddeclining;
	}
	public void setYielddeclining(String yielddeclining) {
		this.yielddeclining = yielddeclining;
	}
	public String getRapidyielddecline() {
		return rapidyielddecline;
	}
	public void setRapidyielddecline(String rapidyielddecline) {
		this.rapidyielddecline = rapidyielddecline;
	}
	public String getPoorwaterQuality_mineral() {
		return poorwaterQuality_mineral;
	}
	public void setPoorwaterQuality_mineral(String poorwaterQuality_mineral) {
		this.poorwaterQuality_mineral = poorwaterQuality_mineral;
	}
	public String getExtrnalpolun() {
		return extrnalpolun;
	}
	public void setExtrnalpolun(String extrnalpolun) {
		this.extrnalpolun = extrnalpolun;
	}
	public String getChangepumpcapacity() {
		return changepumpcapacity;
	}
	public void setChangepumpcapacity(String changepumpcapacity) {
		this.changepumpcapacity = changepumpcapacity;
	}
	public String getLoyerpumpposition() {
		return loyerpumpposition;
	}
	public void setLoyerpumpposition(String loyerpumpposition) {
		this.loyerpumpposition = loyerpumpposition;
	}
	public String getWellcleaning() {
		return wellcleaning;
	}
	public void setWellcleaning(String wellcleaning) {
		this.wellcleaning = wellcleaning;
	}
	public String getComprehensive_reha() {
		return comprehensive_reha;
	}
	public void setComprehensive_reha(String comprehensive_reha) {
		this.comprehensive_reha = comprehensive_reha;
	}
	public String getQualitytest_disinf() {
		return qualitytest_disinf;
	}
	public void setQualitytest_disinf(String qualitytest_disinf) {
		this.qualitytest_disinf = qualitytest_disinf;
	}
	public String getMantaindrainage() {
		return mantaindrainage;
	}
	public void setMantaindrainage(String mantaindrainage) {
		this.mantaindrainage = mantaindrainage;
	}
	public String getHeadmantainreconst() {
		return headmantainreconst;
	}
	public void setHeadmantainreconst(String headmantainreconst) {
		this.headmantainreconst = headmantainreconst;
	}
	public String getAbandonmentseal() {
		return abandonmentseal;
	}
	public void setAbandonmentseal(String abandonmentseal) {
		this.abandonmentseal = abandonmentseal;
	}
	public String getAbandonmentreason() {
		return abandonmentreason;
	}
	public void setAbandonmentreason(String abandonmentreason) {
		this.abandonmentreason = abandonmentreason;
	}
	public String getRehabconducted() {
		return rehabconducted;
	}
	public void setRehabconducted(String rehabconducted) {
		this.rehabconducted = rehabconducted;
	}
	public Date getRehabdate() {
		return rehabdate;
	}
	public void setRehabdate(Date rehabdate) {
		this.rehabdate = rehabdate;
	}
	public String getRehabCompanyName() {
		return rehabCompanyName;
	}
	public void setRehabCompanyName(String rehabCompanyName) {
		this.rehabCompanyName = rehabCompanyName;
	}
	public String getRehabEmail() {
		return rehabEmail;
	}
	public void setRehabEmail(String rehabEmail) {
		this.rehabEmail = rehabEmail;
	}
	public String getRehabPhone() {
		return rehabPhone;
	}
	public void setRehabPhone(String rehabPhone) {
		this.rehabPhone = rehabPhone;
	}
	public double getSwl_before() {
		return swl_before;
	}
	public void setSwl_before(double swl_before) {
		this.swl_before = swl_before;
	}
	public double getSwl_after() {
		return swl_after;
	}
	public void setSwl_after(double swl_after) {
		this.swl_after = swl_after;
	}
	public double getDwl_before() {
		return dwl_before;
	}
	public void setDwl_before(double dwl_before) {
		this.dwl_before = dwl_before;
	}
	public double getDwl_after() {
		return dwl_after;
	}
	public void setDwl_after(double dwl_after) {
		this.dwl_after = dwl_after;
	}
	public double getPump_pos() {
		return pump_pos;
	}
	public void setPump_pos(double pump_pos) {
		this.pump_pos = pump_pos;
	}
	public double getPump_power() {
		return pump_power;
	}
	public void setPump_power(double pump_power) {
		this.pump_power = pump_power;
	}
	public double getYield() {
		return yield;
	}
	public void setYield(double yield) {
		this.yield = yield;
	}
	public double getSpfc_capacity() {
		return spfc_capacity;
	}
	public void setSpfc_capacity(double spfc_capacity) {
		this.spfc_capacity = spfc_capacity;
	}
	
	public double getEffciency() {
		return effciency;
	}
	public void setEffciency(double effciency) {
		this.effciency = effciency;
	}
	public double getHconductivity() {
		return hconductivity;
	}
	public void setHconductivity(double hconductivity) {
		this.hconductivity = hconductivity;
	}
	public double getTransimitivity() {
		return transimitivity;
	}
	public void setTransimitivity(double transimitivity) {
		this.transimitivity = transimitivity;
	}
	public double getStorativity() {
		return storativity;
	}
	public void setStorativity(double storativity) {
		this.storativity = storativity;
	}
	public String getWaterquality() {
		return waterquality;
	}
	public void setWaterquality(String waterquality) {
		this.waterquality = waterquality;
	}
	public String getQltyproblem() {
		return qltyproblem;
	}
	public void setQltyproblem(String qltyproblem) {
		this.qltyproblem = qltyproblem;
	}
	public String getDesignedGeneratoCapacity() {
		return designedGeneratoCapacity;
	}
	public void setDesignedGeneratoCapacity(String designedGeneratoCapacity) {
		this.designedGeneratoCapacity = designedGeneratoCapacity;
	}
	public String[] getSensorTypes() {
		return SensorTypes;
	}
	public void setSensorTypes(String[] sensorTypes) {
		SensorTypes = sensorTypes;
	}
    public String getEnumratorCampanyN() {
		return enumratorCampanyN;
	}
	public void setEnumratorCampanyN(String enumratorCampanyN) {
		this.enumratorCampanyN = enumratorCampanyN;
	}
	public String getEnumratorName() {
		return enumratorName;
	}
	public void setEnumratorName(String enumratorName) {
		this.enumratorName = enumratorName;
	}
	public String getEnumratoremail() {
		return enumratoremail;
	}
	public void setEnumratoremail(String enumratoremail) {
		this.enumratoremail = enumratoremail;
	}
	public String getEnumratorPhone() {
		return enumratorPhone;
	}
	public void setEnumratorPhone(String enumratorPhone) {
		this.enumratorPhone = enumratorPhone;
	}
	public String getCurrentWellStatus() {
		return currentWellStatus;
	}
	public void setCurrentWellStatus(String currentWellStatus) {
		this.currentWellStatus = currentWellStatus;
	}
	public Date getDateOfFunctionStoped() {
		return dateOfFunctionStoped;
	}
	public void setDateOfFunctionStoped(Date dateOfFunctionStoped) {
		this.dateOfFunctionStoped = dateOfFunctionStoped;
	}
	public String getReasonNonFunctioning() {
		return reasonNonFunctioning;
	}
	public void setReasonNonFunctioning(String reasonNonFunctioning) {
		this.reasonNonFunctioning = reasonNonFunctioning;
	}
	public String getFunctioningwellCondn() {
		return functioningwellCondn;
	}
	public void setFunctioningwellCondn(String functioningwellCondn) {
		this.functioningwellCondn = functioningwellCondn;
	}
	public String getInActiveReason() {
		return inActiveReason;
	}
	public void setInActiveReason(String inActiveReason) {
		this.inActiveReason = inActiveReason;
	}
	public String getWellHeadCondition() {
		return wellHeadCondition;
	}
	public void setWellHeadCondition(String wellHeadCondition) {
		this.wellHeadCondition = wellHeadCondition;
	}
	public String getPoorWellHeadReason() {
		return poorWellHeadReason;
	}
	public void setPoorWellHeadReason(String poorWellHeadReason) {
		this.poorWellHeadReason = poorWellHeadReason;
	}
	public String getDrainageCondition() {
		return drainageCondition;
	}
	public void setDrainageCondition(String drainageCondition) {
		this.drainageCondition = drainageCondition;
	}
	public String getPowerSource() {
		return powerSource;
	}
	public void setPowerSource(String powerSource) {
		this.powerSource = powerSource;
	}
	public String getStandByGeneratorAvialable() {
		return standByGeneratorAvialable;
	}
	public void setStandByGeneratorAvialable(String standByGeneratorAvialable) {
		this.standByGeneratorAvialable = standByGeneratorAvialable;
	}
	public String getGeneratorStatus() {
		return generatorStatus;
	}
	public void setGeneratorStatus(String generatorStatus) {
		this.generatorStatus = generatorStatus;
	}
	public double getGeneratorPower() {
		return generatorPower;
	}
	public void setGeneratorPower(double generatorPower) {
		this.generatorPower = generatorPower;
	}
	public String getObservationPiPAvialable() {
		return observationPiPAvialable;
	}
	public void setObservationPiPAvialable(String observationPiPAvialable) {
		this.observationPiPAvialable = observationPiPAvialable;
	}
	public String getStatusOfObservationPIP() {
		return statusOfObservationPIP;
	}
	public void setStatusOfObservationPIP(String statusOfObservationPIP) {
		this.statusOfObservationPIP = statusOfObservationPIP;
	}
	public String getSCADAConnection() {
		return SCADAConnection;
	}
	public void setSCADAConnection(String sCADAConnection) {
		SCADAConnection = sCADAConnection;
	}
	public String getSCADAstatus() {
		return SCADAstatus;
	}
	public void setSCADAstatus(String sCADAstatus) {
		SCADAstatus = sCADAstatus;
	}
	public String getMonitoringSencorIns() {
		return monitoringSencorIns;
	}
	public void setMonitoringSencorIns(String monitoringSencorIns) {
		this.monitoringSencorIns = monitoringSencorIns;
	}
	public double getStaticWL() {
		return staticWL;
	}
	public void setStaticWL(double staticWL) {
		this.staticWL = staticWL;
	}
	public double getDynamicWL() {
		return dynamicWL;
	}
	public void setDynamicWL(double dynamicWL) {
		this.dynamicWL = dynamicWL;
	}
	public double getAbstractionRate() {
		return abstractionRate;
	}
	public void setAbstractionRate(double abstractionRate) {
		this.abstractionRate = abstractionRate;
	}
	public double getElecConductivity() {
		return elecConductivity;
	}
	public void setElecConductivity(double elecConductivity) {
		this.elecConductivity = elecConductivity;
	}
	public double getpHValue() {
		return pHValue;
	}
	public void setpHValue(double pHValue) {
		this.pHValue = pHValue;
	}
	public double getTemprature() {
		return temprature;
	}
	public void setTemprature(double temprature) {
		this.temprature = temprature;
	}
	public String getWaterColor() {
		return waterColor;
	}
	public void setWaterColor(String waterColor) {
		this.waterColor = waterColor;
	}
	public String getWaterOdor() {
		return waterOdor;
	}
	public void setWaterOdor(String waterOdor) {
		this.waterOdor = waterOdor;
	}
	public Date getOperationStartedDate() {
		return operationStartedDate;
	}
	public void setOperationStartedDate(Date operationStartedDate) {
		this.operationStartedDate = operationStartedDate;
	}
	public double getTestDurationHrs() {
		return testDurationHrs;
	}
	public void setTestDurationHrs(double testDurationHrs) {
		this.testDurationHrs = testDurationHrs;
	}
	public double getPumpPosition() {
		return pumpPosition;
	}
	public void setPumpPosition(double pumpPosition) {
		this.pumpPosition = pumpPosition;
	}
	public double getPumpCapacity() {
		return pumpCapacity;
	}
	public void setPumpCapacity(double pumpCapacity) {
		this.pumpCapacity = pumpCapacity;
	}
	public String getPumpHead() {
		return pumpHead;
	}
	public void setPumpHead(String pumpHead) {
		this.pumpHead = pumpHead;
	}
	public String getCyieldDyield() {
		return cyieldDyield;
	}
	public void setCyieldDyield(String cyieldDyield) {
		this.cyieldDyield = cyieldDyield;
	}
	public String getGwPotableStatus() {
		return gwPotableStatus;
	}
	public void setGwPotableStatus(String gwPotableStatus) {
		this.gwPotableStatus = gwPotableStatus;
	}
	public String getNonPotableReason() {
		return nonPotableReason;
	}
	public void setNonPotableReason(String nonPotableReason) {
		this.nonPotableReason = nonPotableReason;
	}
	public String getWellProductivity() {
		return wellProductivity;
	}
	public void setWellProductivity(String wellProductivity) {
		this.wellProductivity = wellProductivity;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	 public String getWellCellCode() {
			return wellCellCode;
		}
		public void setWellCellCode(String wellCellCode) {
			this.wellCellCode = wellCellCode;
		}
	private double pumpPosition;
    private double pumpCapacity;
    private String pumpHead;
    private String cyieldDyield;
    private String gwPotableStatus;
    private String nonPotableReason;
    private String wellProductivity;
    private String remark;

    // Standard public getters and setters for all 113 variables go here...
    public String getWellCellIndex() { return wellCellIndex; }
    public void setWellCellIndex(String wellCellIndex) { this.wellCellIndex = wellCellIndex; }

    public Date getDataRecordedDate() { return dataRecordedDate; }
    public void setDataRecordedDate(Date dataRecordedDate) { this.dataRecordedDate = dataRecordedDate; }

    public String getWaterUsageLicence() { return waterUsageLicence; }
    public void setWaterUsageLicence(String waterUsageLicence) { this.waterUsageLicence = waterUsageLicence; }
}