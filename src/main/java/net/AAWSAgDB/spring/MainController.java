package net.AAWSAgDB.spring;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;

import net.AAWSAgDB.fileupload.ValuePair;
import net.AAWSAgDB.fileupload.dao.LoginDao;
import net.AAWSAgDB.fileupload.dao.UserDao;
import net.AAWSAgDB.fileupload.dao.StoreParameter;
import net.AAWSAgDB.fileupload.dao.DataAccessDao;
import net.AAWSAgDB.fileupload.dao.uploadDao;
import net.AAWSAgDB.fileupload.model.File;
import net.AAWSAgDB.fileupload.model.SpatialUploadDTO;
import net.AAWSAgDB.fileupload.model.UserRegistrationDTO;
import net.AAWSAgDB.fileupload.model.UserSessionProfile;
import net.AAWSAgDB.fileupload.service.EmailService;
import net.AAWSAgDB.fileupload.service.ExcelParsingService;

@Controller
public class MainController{
	String ukn;
    String fname;
	String process;
	String center;
	String logedname;
	String password;
    Connection conn;
    
    @RequestMapping(value="/", method = RequestMethod.GET)
	public ModelAndView visitHome() {
		return new ModelAndView("login");
	}
	  //Spring Security see this :
    @RequestMapping(value = "/login", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView login(@RequestParam(value = "error",required = false) String error,
        @RequestParam(value = "logout", required = false) String logout) {
        ModelAndView model = new ModelAndView();
        if (error!= null) {
        	
            model.addObject("error", "Invalid username or password!"); 
            model.setViewName("login");
        }
        else if (logout==null) {
            model.addObject("error", "Invalid username or password!");  
            model.setViewName("login");
        }
        return model;
    }
    @RequestMapping(value = "/welcome", method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView welcomePage() {
        ModelAndView modelAndView = new ModelAndView("welcome");
        modelAndView.addObject("message", "Welcome to the secured application!");
        return modelAndView;
    }
    
    @Autowired
	private DataAccessDao dataAccessDao;
    @RequestMapping(value = "/FetchWellMapCoordinates", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<File> streamBoreholeGeospatialVectors() {
        // Invokes your pre-existing clean DataAccessDao or loginDao instance method natively
        return dataAccessDao.access_well_name();
    }

    public static String getpassword(String password){
    	String geneatedpassword=null;
    	try{
    	MessageDigest md= MessageDigest.getInstance("SHA-256");	
    	byte [] bytes=md.digest(password.getBytes());
    	StringBuilder sb=new StringBuilder();
    	for(int i=0;i<bytes.length;i++){
    		sb.append(Integer.toString((bytes[i] & 0xff) + 0x00,16).toString());
    	}
    	geneatedpassword=sb.toString();
    	}
    	catch(NoSuchAlgorithmException ae){
    		ae.printStackTrace();
    	}
    	return geneatedpassword;
    }
    /**
     * Encrypts a raw password using BCryptPasswordEncoder.
     *
     * @param rawPassword The plain-text password to encrypt.
     * @return The resulting BCrypt hashed password (approx. 60 chars long).
     */
    public static String encryptPassword(String rawPassword) {
        // Create an instance of the encoder. The default strength (workload) is 10.
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Encode the password
        String hashedPassword = passwordEncoder.encode(rawPassword);
        return hashedPassword;
    }
    @Autowired
    private LoginDao loginDao;
    @Autowired
    private UserDao userDao;
    @RequestMapping(value = "/home", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView adminPage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, MessagingException, SQLException {
    	
    	 ModelAndView model = new ModelAndView();
    	 
    	 // 1. Enforce strict Spring Security context token verification checks
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication == null || !authentication.isAuthenticated()) {
             model.setViewName("login");
             return model;
         }
         
         String username;
         Object principal = authentication.getPrincipal();
         if (principal instanceof UserDetails) {
             username = ((UserDetails) principal).getUsername();
         } else {
             username = principal.toString();
         }

         // 2. Fetch unified credentials atomically using a clean model wrapper method
         UserSessionProfile sessionProfile = loginDao.getUserLandingProfile(username);
         if (sessionProfile == null || sessionProfile.getPrivilegeCode() == null) {
             model.setViewName("login");
             return model;
         }
         
         String fname = sessionProfile.getFullName();
         String pr_code = sessionProfile.getPrivilegeCode().trim();
         String userAuthority = sessionProfile.getAuthority();
         int atomicUserId = sessionProfile.getUserId(); // FIXED: Isolated thread context token allocation
         //System.out.println("User ID Is "+atomicUserId);

         // 3. Securely establish or retrieve the active HTTP session context
         HttpSession session = request.getSession(true);

         List<File> geo_techfile = new ArrayList<>();
         List<File> cnter_name = new ArrayList<>();
         List<File> regionCity = new ArrayList<>();
         List<File> project_Overlay = new ArrayList<>();
         List<File> legend = new ArrayList<>();
        
     // 4. Populate dynamic layout page metadata arrays securely via instance parameters
        try {
            List<Integer> loginid = new ArrayList<>();
            loginid.add(loginDao.validateCor(fname, username));
            cnter_name.addAll(userDao.usertype()); // FIXED: Swapped loose undeclared 'userDao' container instance
            regionCity.addAll(loginDao.region_city());
            project_Overlay.addAll(dataAccessDao.access_well_name());
            legend.addAll(loginDao.access_groupingBy_proType());
        } catch (Throwable ee) {
            System.err.println("Critical Exception: Metadata collection population failure encountered.");
            ee.printStackTrace();
        }

     // 5. Dynamic Multi-Route Role Authorization Routing Matrix Evaluation
        if ("110".equals(pr_code)) { // ROLE_ADMINISTRATOR
            model.addObject("data1", regionCity);
            if (session != null) {
                session.setAttribute("U_id1", atomicUserId); // Bounded using atomic thread tokens
                session.setAttribute("pr_typ", userAuthority);
                session.setAttribute("U_auto", fname);
                session.setAttribute("pr_code_active", pr_code);
            }
            model.setViewName("privilege_confirm");
            } 
        else if ("120".equals(pr_code)) { // ROLE_DATABASE_USER
            model.addObject("gr_pro", cnter_name);
            model.addObject("data", geo_techfile);
            if (session != null) {
                session.setAttribute("U_id1", atomicUserId);
                session.setAttribute("U_auto", fname);
                session.setAttribute("pr_code_active", pr_code);
            }
            model.setViewName("hompage");
            
        } 
        else if ("123".equals(pr_code)) { // ROLE_COMMON_USER
            if (session != null) {
                session.setAttribute("U_auto", fname);    
                session.setAttribute("userid", username);
                session.setAttribute("U_lname", sessionProfile.getFullName()); // Pulled natively from the explicit model payload
                session.setAttribute("pr_code_active", pr_code);
                session.setAttribute("U_id1", atomicUserId);
            }
            model.addObject("data1", regionCity);
            model.addObject("U_auto", fname);
            model.addObject("georef", project_Overlay);
            model.addObject("legend", legend);
            model.setViewName("corporateU");
            
        } 
        else if ("0".equals(pr_code)) { // ROLE_PASSWORD_RESET_MANDATORY
            if (session != null) {
                session.setAttribute("U_id1", atomicUserId);    
                session.setAttribute("pr_code_active", pr_code);
            }
            model.setViewName("passwordM");
        }
        else {
            model.setViewName("login");
        }

        return model;
    }
    @RequestMapping(value = "/RouteView", method = RequestMethod.GET)
    public ModelAndView routeAuthenticatedUserWorkspace(@RequestParam("view") String targetView, HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        HttpSession session = request.getSession(false);

        // Security Interceptor Guard Check: Prevent unauthenticated url-hijacking bypass parameters
        if (session == null || session.getAttribute("U_id1") == null) {
            model.setViewName("redirect:/login");
            return model;
        }
        List<File> regionCity = new ArrayList<>();
        List<File> project_Overlay = new ArrayList<>();
        List<File> legend = new ArrayList<>();
        
        regionCity.addAll(loginDao.region_city());
        project_Overlay.addAll(dataAccessDao.access_well_name());
        legend.addAll(loginDao.access_groupingBy_proType());

        String actualUserRole = (String) session.getAttribute("pr_code_active");
        int userID= (Integer) session.getAttribute("U_id1");

        // SECURITY CLEARANCE AUTHORIZATION MATRIX VERIFICATION RULES
        if ("corporateU".equalsIgnoreCase(targetView)) {
            // All active authenticated users are granted visibility rights to the Common User Portal
            model.addObject("data1", regionCity);
            model.addObject("pr_code_active", actualUserRole);
            model.addObject("georef", project_Overlay);
            model.addObject("legend", legend);
            model.setViewName("corporateU");
        } 
        else if ("hompage".equalsIgnoreCase(targetView) && ("110".equals(actualUserRole) || "120".equals(actualUserRole))) {
            // Only Database Users and Administrators are allowed to enter the management dashboard
        	if (session != null) {
                //session.setAttribute("U_auto", fname);    
                session.setAttribute("pr_code_active", actualUserRole);
                session.setAttribute("U_id1", userID);
            }
            model.setViewName("hompage");
        } else if ("privilege_confirm".equalsIgnoreCase(targetView) && "110".equals(actualUserRole)) {
            // Only authentic System Administrators can re-enter the root configuration console terminal
            model.setViewName("privilege_confirm");
        } else {
            // If authorization rules fail, safely lock them out to the access error page boundary
            model.setViewName("access_denied");
        }

        return model;
    }

    
    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/pre_authenticate", method = RequestMethod.POST)
    public ModelAndView sendMail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ModelAndView retmodel = new ModelAndView();
        
        List<File> usertype = new ArrayList<>();
        usertype.addAll(loginDao.region_city());
        retmodel.addObject("data1", usertype);
        retmodel.setViewName("privilege_confirm");

        String[] recipients = request.getParameterValues("U_email");
        String usertype1 = request.getParameter("usertype");

        try {
            emailService.sendPreAuthenticateMail(recipients, usertype1);
        } catch (UnsupportedEncodingException | MessagingException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            retmodel.addObject("error", "mail.ecdswc.com.et is not functional for now");
        }
        
        return retmodel;
    }
    //@Autowired
    //private EmailService emailService;

    @RequestMapping(value = "/userInformation", method = RequestMethod.POST)
    public ModelAndView elit_super(UserRegistrationDTO userForm) throws ServletException, IOException { 
        ModelAndView retmodel = new ModelAndView();
        
        // Populate standard drop-down models
        List<File> usertype = new ArrayList<>();
        usertype.addAll(loginDao.region_city());
        retmodel.addObject("data1", usertype);
        retmodel.setViewName("privilege_confirm");

        try {
            // 1. Verify existence using secure criteria parameters
            if (userDao.checkUserExists(userForm.getUserName())) {
                retmodel.addObject("existes1", "The User, " + userForm.getUserName() + " is Already Registered In Repository!!");
                return retmodel;
            }

            // 2. Encrypt and save profile transactionally
            String passwordsalted = encryptPassword(userForm.getPassword());
            userDao.registerEliteUser(userForm, passwordsalted);
            retmodel.addObject("preved1", "The User, " + userForm.getUserName() + " is Created In Your AAWSA gDB Repository!");

            // 3. Dispatch confirmation updates asynchronously or sequentially
            //emailService.sendRegistrationConfirmation(userForm.getUserEmail());

        } catch (SQLException e) {
            e.printStackTrace();
            retmodel.addObject("error1", "Database error occurred during user configuration.");
        } catch (Exception ex) {
            ex.printStackTrace();
            retmodel.addObject("error1", "mail.aawsa.com.et is not configured for now");
        }

        return retmodel;
    }
    
	// file upload
 // Near the top of your MainController.java
      @Autowired
      private uploadDao uploadDao;
       @Autowired
        private ExcelParsingService excelParsingService;
	    @Autowired
	    private StoreParameter storeParameter;
	    
	    @ResponseBody
	    @RequestMapping(value = "/Upload", method = RequestMethod.POST)
	    public ModelAndView handleBulkSpreadsheetImport(SpatialUploadDTO formInput, MultipartHttpServletRequest request, RedirectAttributes redirectAttributes) {
	        ModelAndView modelview = new ModelAndView();
	        List<File> uploadExecutionReport = new ArrayList<>();
	        
	        // Establish baseline fallback timestamp natively
	        java.sql.Date currentSqlTimestamp = new java.sql.Date(System.currentTimeMillis());
	        formInput.setRecorDate(currentSqlTimestamp);

	        // Core Routing Switch: Check if it's Scenario 1 (Spatial/Drilling) or Scenario 2 (Detailed Operations)
	        String wellIndex = request.getParameter("wellIndex");

	        try {
	            // =========================================================================
	            // ─── SCENARIO 1: DRILLING & PUMPING TEST HISTORY DATA IMPORT ───
	            // =========================================================================
	            if (wellIndex != null && !wellIndex.trim().isEmpty()) {
	                
	                // Extract Binary File Package attachments safely out of the multipart boundary stream
	                List<MultipartFile> masterExcelDataFiles = request.getFiles("productiveWelldata");
	                List<MultipartFile> technicalReports = request.getFiles("reportProWell");
	                List<MultipartFile> electromechanicalSpecs = request.getFiles("pumpingSdata");

	                MultipartFile excelFile = (masterExcelDataFiles != null && !masterExcelDataFiles.isEmpty()) ? masterExcelDataFiles.get(0) : null;
	                MultipartFile reportFile = (technicalReports != null && !technicalReports.isEmpty()) ? technicalReports.get(0) : null;
	                MultipartFile specFile = (electromechanicalSpecs != null && !electromechanicalSpecs.isEmpty()) ? electromechanicalSpecs.get(0) : null;

	                // Process file attachments if present and bind metadata straight to the transport DTO
	                if (reportFile != null && !reportFile.isEmpty()) {
	                    formInput.setWellreportName(reportFile.getOriginalFilename());
	                    formInput.setWellReportType(reportFile.getContentType());
	                    formInput.setWellRepoSize((int) reportFile.getSize());
	                    formInput.setWellRepoInputStream(reportFile.getInputStream());
	                }

	                if (specFile != null && !specFile.isEmpty()) {
	                    formInput.setPumpFilename(specFile.getOriginalFilename());
	                    formInput.setPumpFileType(specFile.getContentType());
	                    formInput.setPumpfileSize((int) specFile.getSize());
	                    formInput.setPumpdataStream(specFile.getInputStream());
	                }

	                // Transform binary spreadsheet cells into a clean sanitised string matrix array row-by-row
	                List<String[]> dataRowsMatrix = null;
	                if (excelFile != null && !excelFile.isEmpty()) {
	                    dataRowsMatrix = excelParsingService.parseDrillingLogsExcel(excelFile);
	                }

	                // Extract structural layout context parameters
	                int db_userID = request.getParameter("db_userId").isEmpty() ? 0 : Integer.parseInt(request.getParameter("db_userId"));
	                int subcityzoneId = request.getParameter("subcityzoneId").isEmpty() ? 0 : Integer.parseInt(request.getParameter("subcityzoneId"));
	                
	                String WellIndex=request.getParameter("wellIndex")==null ? "" : request.getParameter("wellIndex");
	                String Dpermit=request.getParameter("Dpermit")==null ? "" : request.getParameter("Dpermit");
	                
	                int wellField = request.getParameter("wellField").isEmpty() ? 0 : Integer.parseInt(request.getParameter("wellField"));



	                    // 6. Delegate the final parsed rows down to the atomic transactional business core inside uploadDao
	                    uploadExecutionReport = uploadDao.executeSpatialDataUploadPipeline(
	                        formInput, dataRowsMatrix, db_userID, subcityzoneId, wellField, 
	                        WellIndex,Dpermit);
	            } 
	            // =========================================================================
	            // ─── SCENARIO 2: FIXED - DETAIL OPERATIONAL WELL DATA BINDING ───
	            // =========================================================================
	            else {
	                // 1. Capture the multi-file array matching name="operationalFile" from our state-of-the-art form
	                List<MultipartFile> operationalDocuments = request.getFiles("operationalFile");
	                
	                // 2. Extract operational specific user tracking parameters securely
	                String rawUserIdII = request.getParameter("db_userIdII");
	                int db_userIDII = (rawUserIdII != null && !rawUserIdII.isEmpty()) ? Integer.parseInt(rawUserIdII) : 0;

	                // 3. Delegate file collection directly to your transaction-safe, optimized operational batch method
	                uploadExecutionReport = uploadDao.saveotherofficefile(operationalDocuments, db_userIDII);
	            }

	        } catch (Exception e) {
	            System.err.println("Fatal: Pipeline encountered an execution error at the controller layer.");
	            e.printStackTrace();
	            uploadExecutionReport.add(new File(0, "System transaction error occurred: " + e.getMessage(), "Error"));
	        }

	        // Prevent duplicate form entries by routing responses via flash redirects
	        redirectAttributes.addFlashAttribute("msg1", uploadExecutionReport);
	        modelview.setViewName("redirect:/successPage");
	        return modelview;
	    }
	    

    @RequestMapping(value = "/updateWCR", method = RequestMethod.POST)
    public ModelAndView updateWCRData(HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception {
    	ModelAndView modelview = new ModelAndView();
  		List<File> files = new ArrayList<File>();
  		int dbUserId=Integer.parseInt(request.getParameter("db_userUpdateWCR"));
    	int userID=Integer.parseInt(request.getParameter("CROwnerIdUName"));
    	int wellID=Integer.parseInt(request.getParameter("CRwellIdUName"));
    	int geolocID=Integer.parseInt(request.getParameter("CRGeolocUName"));
    	String wellCode=request.getParameter("CRwellCodeUName");
    	double easting=Double.parseDouble(request.getParameter("CRweEastingUName"));
    	double northing=Double.parseDouble(request.getParameter("CRNorthingUName"));
    	double longT=Double.parseDouble(request.getParameter("CRLongTUName"));
    	double lat=Double.parseDouble(request.getParameter("CRLatUName"));
    	double elivation=Double.parseDouble(request.getParameter("CRElevationUName"));
    	String constructionYear;
 		 if(request.getParameter("CRConstYUName")==null){
 			constructionYear="1970-01-01"; 
	  		 } 
 		 else{
 			constructionYear =request.getParameter("CRConstYUName");
	  		 }
 		java.util.Date dateraw= new SimpleDateFormat("yyyy-MM-dd").parse(constructionYear);
 		java.sql.Date wellConstractionDate= new java.sql.Date(dateraw.getTime());
    	String drillingLicense=request.getParameter("CRDrillingLUName");
    	double wellDepth=Double.parseDouble(request.getParameter("CRwellDepthUName"));
    	double wellYield=Double.parseDouble(request.getParameter("CRwellYieldUName"));
    	double SWL=Double.parseDouble(request.getParameter("CRSWLUName"));
    	double DWL=Double.parseDouble(request.getParameter("CRDWLUName"));
    	double specificCapacity=Double.parseDouble(request.getParameter("CRSpecificCUName"));
    	String wellOwnerName=request.getParameter("CROwnerCUName");
    	String wellOwnerEmail=request.getParameter("CREmailUName");
    	String wellOwnerHpone=request.getParameter("CRPhoneUName");
    	String mainAquifer=request.getParameter("CRmainaquiferName");
    	String CRwellTypeName=request.getParameter("CRwellTypeName");
    	String CRabanRWellName=request.getParameter("CRabanRWellName");
    	String CRsealedYNName=request.getParameter("CRsealedYNName");
    	String CRSealedDateName;
		 if(request.getParameter("CRSealedDateName")==null){
			 CRSealedDateName="1970-01-01"; 
	  		 } 
		 else{
			 CRSealedDateName =request.getParameter("CRSealedDateName");
	  		 }
		 java.util.Date sealedDate= new SimpleDateFormat("yyyy-MM-dd").parse(CRSealedDateName);
	 	 java.sql.Date wellConSealedDate= new java.sql.Date(sealedDate.getTime());
    	String CRpumpstatusName=request.getParameter("CRpumpstatusName");
    	String CRpumpInstalledDateName;
		 if(request.getParameter("CRpumpInstalledDateName")==null){
			 CRpumpInstalledDateName="1970-01-01"; 
	  		 } 
		 else{
			 CRpumpInstalledDateName =request.getParameter("CRpumpInstalledDateName");
	  		 }
		 java.util.Date installedDate= new SimpleDateFormat("yyyy-MM-dd").parse(CRpumpInstalledDateName);
	 	 java.sql.Date wellpumpInstallDate= new java.sql.Date(installedDate.getTime());
		double CRpumpCapacityName=Double.parseDouble(request.getParameter("CRpumpCapacityName"));
		String CRpumpHeadName=request.getParameter("CRpumpHeadName");
	    double CRpumpPosiName=Double.parseDouble(request.getParameter("CRpumpPosiName"));
	    double CRdischargeRateName=Double.parseDouble(request.getParameter("CRdischargeRateName"));
	    String CRgeneratorStaName=request.getParameter("CRgeneratorStaName");
	    double CRcapacityCapaName=Double.parseDouble(request.getParameter("CRcapacityCapaName"));
	    String CRSOwnerCatName=request.getParameter("CRSOwnerCatName");
	    double CRlargeCasingName=Double.parseDouble(request.getParameter("CRlargeCasingName"));
	    double CRtelCasingName=Double.parseDouble(request.getParameter("CRtelCasingName"));
    	String CRCasingMaterName=request.getParameter("CRCasingMaterName");
    	String SCADAName=request.getParameter("SCADAName");
    	String SCADACStatusName=request.getParameter("SCADACStatusName");
    	//System.out.println("User Id for Update WCR is "+userID+" and Well Id Is "+wellID+" and Well Cod is "+wellCode);
    	files.addAll(uploadDao.updateWCRData(userID,wellID,geolocID,wellCode,easting,northing,longT,lat,elivation,wellConstractionDate,
    			drillingLicense,wellDepth, wellYield, SWL, DWL, specificCapacity, wellOwnerName, wellOwnerEmail,wellOwnerHpone,dbUserId,
    			CRSOwnerCatName,CRlargeCasingName,CRtelCasingName,CRCasingMaterName,mainAquifer,CRwellTypeName,CRabanRWellName,CRsealedYNName,
    			wellConSealedDate,CRpumpstatusName,wellpumpInstallDate,CRpumpCapacityName,CRpumpHeadName,CRpumpPosiName,
    			CRdischargeRateName,CRgeneratorStaName,CRcapacityCapaName,SCADAName,SCADACStatusName));
    	redirectAttributes.addFlashAttribute("updateMsg", files);
		modelview.setViewName("redirect:/successPage");
        return modelview;
    }
    @RequestMapping(value = "/updateGWOD", method = RequestMethod.POST)
    public ModelAndView updateGWOData(HttpServletRequest request,RedirectAttributes redirectAttributes) throws Exception {
    	ModelAndView modelview = new ModelAndView();
  		List<File> files = new ArrayList<File>();
    	int userID=Integer.parseInt(request.getParameter("enumIdName"));
    	int wellID=Integer.parseInt(request.getParameter("wellIdName"));
    	String wellIndex=request.getParameter("wellIndexName");
    	String waterUserLName=request.getParameter("waterUserLName");
    	String operationStartDate;
 		 if(request.getParameter("opStartDName").equals("_")|| request.getParameter("opStartDName").equals("NR")){
 			operationStartDate="1970-01-01"; 
	  		 } 
 		 else{
 			operationStartDate =request.getParameter("opStartDName");
	  		 }
 		java.util.Date dateraw= new SimpleDateFormat("yyyy-MM-dd").parse(operationStartDate);
 		java.sql.Date wellOpernStartDate= new java.sql.Date(dateraw.getTime());
 		String enumCompanyName=request.getParameter("enumCompanyName");
    	String enumEmailName=request.getParameter("enumEmailName");
    	String enumEmailPhoneName=request.getParameter("enumEmailPhoneName");
    	//System.out.println("User Id for Update WCR is "+userID+" and Well Id Is "+wellID+" and Well Cod is "+wellCode);
    	files.addAll(uploadDao.updateGWOData(userID,wellID,wellIndex,waterUserLName,wellOpernStartDate,enumCompanyName,enumEmailName,
    			enumEmailPhoneName));
    	redirectAttributes.addFlashAttribute("updateMsg", files);
		modelview.setViewName("redirect:/successPage");
        return modelview;
    } 
    @RequestMapping(value = "/successPage", method = RequestMethod.GET)
    public ModelAndView showSuccessPage() {
    	 ModelAndView modelAndView = new ModelAndView("hompage"); // "successPage" is the logical view name
        return modelAndView;
    } 

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ModelAndView changePassword(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("login");
        
        try {
            int userID = Integer.parseInt(request.getParameter("userID"));
            String newPassword = request.getParameter("newPassword");
            String encryptedPassword = encryptPassword(newPassword);

            boolean isUpdated = userDao.changeUserPassword(userID, encryptedPassword);
            
            if (isUpdated) {
                modelAndView.addObject("changeSuccess", "Password changed. Login Please.");
            } else {
                modelAndView.addObject("changeFailed", "Information Mismatch, Password Not changed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("changeFailed", "An error occurred during password update execution.");
        }  
        return modelAndView;
    }

    @RequestMapping(value = "/changeForgatenPassword", method = RequestMethod.POST)
    public ModelAndView changeForgatenPassword(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("login");
        
        try {
            String changerFullName = request.getParameter("changerFullName");
            String changerUserName = request.getParameter("changerUserName");
            String changerEmail = request.getParameter("changerEmail");
            String changerPhoneNumber = request.getParameter("changerPhoneNumber");
            String changernewPassword = request.getParameter("changernewPassword");
            
            String encryptedPassword = encryptPassword(changernewPassword);

            boolean isReset = userDao.resetForgottenPassword(
                changerFullName, changerUserName, changerEmail, changerPhoneNumber, encryptedPassword
            );

            if (isReset) {
                modelAndView.addObject("success", "Password changed. Login Please.");
            } else {
                modelAndView.addObject("denay", "Information Mismatch, Password Not changed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("denay", "An error occurred during password reset execution.");
        }
        
        return modelAndView;
    }
    
 	// download non spatial files
 		@RequestMapping(value = "/downloadfile", method = RequestMethod.GET)
 		public void nonspatialfile(HttpServletRequest request, HttpServletResponse response) throws Throwable {
 	 		int fileId = Integer.parseInt(request.getParameter("file_id"));
 	 		String dataType=request.getParameter("DPTH_ID");
 	 		//System.out.println("Data Type For Download is "+dataType);
 	 		ValuePair file=dataAccessDao.downloadDPTHData(fileId,dataType);
 				//response.setContentType(file.contentType);
 				response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.name+ "\"");
 				//response.setContentLength(file.size1);
 				response.getOutputStream().write(file.ddat);		
 			
 			response.flushBuffer();
 		}
 		//input response
 		@ResponseBody
		@RequestMapping(value="/Upload_", method = RequestMethod.POST)
		public String respondto_interface(HttpServletRequest request) throws Throwable {
 			System.out.println("Id of Pushed button= ");	
			Gson gson = new Gson();
			return gson.toJson(null);
		}
 		
 		//get selected GHM by project name
 		@ResponseBody
		@RequestMapping(value="/GHM_selected", method = RequestMethod.POST)
		public String Select_GHM(HttpServletRequest request) throws Throwable {
 			int cat_name=Integer.parseInt(request.getParameter("cat"));
 			//System.out.println("Id of Project__= "+cat_name);
			Gson gson = new Gson();
			return gson.toJson(LoginDao.data_investigated(cat_name));
		}
 		//get Sample category
 		@ResponseBody
		@RequestMapping(value="/accountManagement", method = RequestMethod.POST)
		public String sample_cata(HttpServletRequest request) throws Throwable {
 			int adminID=Integer.parseInt(request.getParameter("usreID"));
 			//System.out.println("Id of Type__= "+type_Id);
			Gson gson = new Gson();
			return gson.toJson(loginDao.userAccountManagement(adminID));
		}
		//get category of GHM
 		@ResponseBody
		@RequestMapping(value="/updateUserAccount", method = RequestMethod.POST)
		public String updateUserAccount(HttpServletRequest request) throws Throwable {
 			int userID=Integer.parseInt(request.getParameter("userID"));
 			String accountStatus=request.getParameter("accountStatus");
 			int privilege=Integer.parseInt(request.getParameter("privilege"));
 			int adminID=Integer.parseInt(request.getParameter("1404Id"));
 			System.out.println("Account status= "+accountStatus+" and privilage = "+privilege);
			Gson gson = new Gson();
			return gson.toJson(loginDao.updateUserAccount(userID,privilege,accountStatus,adminID));
		}
        //Ground water Data mapping
 		@ResponseBody
		@RequestMapping(value = "/gWDBParam", method = RequestMethod.POST)
		public String gwDatabaseParameter(HttpServletRequest request) throws Throwable {
				String national_cor=request.getParameter("Na");
                //System.out.println("GW data types "+national_cor);
			Gson gson = new Gson();
			return gson.toJson(storeParameter.gwDBParam(national_cor));
		}
 		//Region City Mapping
 		@ResponseBody
		@RequestMapping(value="/regionCity", method = RequestMethod.POST)
		public String proj_center(HttpServletRequest request) throws Throwable {
				int  national_cor=Integer.parseInt(request.getParameter("regionC"));
			Gson gson = new Gson();
			return gson.toJson(storeParameter.region_class(national_cor));
		}
 		
 		//Sub city Zone Mapping
 		@ResponseBody
			@RequestMapping(value = "/subcityZone", method = RequestMethod.POST)
			public String prcomponent(HttpServletRequest request) throws Throwable {
 			    int searchkey=Integer.parseInt(request.getParameter("cityZone_id"));
				Gson gson = new Gson();
				return gson.toJson(storeParameter.subCityZone(searchkey));
			}
 			
 			//project Detail for request 
 			@ResponseBody
 			@RequestMapping(value = "/detailsPro", method = RequestMethod.POST)
 			public String accessspro_details(HttpServletRequest request) throws SQLException {
 				int center_id = Integer.parseInt(request.getParameter("cen_id"));
 				int process = Integer.parseInt(request.getParameter("process_id"));
 				int pro_id = Integer.parseInt(request.getParameter("proj_id"));
 				String desisup_id = request.getParameter("de_su_key");
 				Gson gson = new Gson();
 				return gson.toJson(null);
 			}
 			//for request send
 			//project Detail for request 
 			@ResponseBody
 			@RequestMapping(value = "/Sendreqeust", method = RequestMethod.POST)
 			public String sendrequest(HttpServletRequest request) throws SQLException {
 				int center_id = Integer.parseInt(request.getParameter("cen_id"));
 				int process = Integer.parseInt(request.getParameter("pro_id"));
 				int pro_id = Integer.parseInt(request.getParameter("proj_id"));
 				String username= request.getParameter("userId");
 				String fname= request.getParameter("fname");
 				String lname= request.getParameter("lname");
 				String desisup_id= request.getParameter("de_su_key");
 				List<String> listitem=new ArrayList<String>();
 				listitem.add(desisup_id);
 				Gson gson = new Gson();
 				for(String cat_id:listitem){
 	      return gson.toJson(uploadDao.ask_request(center_id, process, pro_id,cat_id,username,fname,lname));	
 				}
				return gson.toJson("");
 			}
 		
 	    @ResponseBody
 	    @RequestMapping(value = "/DPTHDataURL", method = RequestMethod.POST)
 	    public String getGroundwaterRegistryDatasets(HttpServletRequest request) throws Throwable {
 	        int sessionUserId = 0;
 	            sessionUserId = Integer.parseInt(request.getParameter("id_active"));
 	            //System.out.println("ID is "+sessionUserId);

 	        // 2. Pass the user ID down to your database extraction tier to pull permission states mapping variables
 	        com.google.gson.Gson gson = new com.google.gson.Gson();
 	        
 	        // Aligns with your exact platform coding style guidelines
 	        return gson.toJson(dataAccessDao.accessSecuredRepositoriesByCategory(sessionUserId));
 	    }

 		/**
 	     * Intercepts frontend verification checks to check user permission states.
 	     * ROUTE: /checkRepositoryAccess
 	     */
 	    @RequestMapping(value = "/checkRepositoryAccess", method = RequestMethod.POST)
 	    public void checkRepositoryAccess(HttpServletRequest request, HttpServletResponse response) throws IOException {
 	        response.setContentType("application/json");
 	        response.setCharacterEncoding("UTF-8");
 	        PrintWriter out = response.getWriter();

 	        String repoIdParam = request.getParameter("repo_id");
 	        String userIdParam = request.getParameter("user_id");

 	        int repoId = (repoIdParam != null && !repoIdParam.isEmpty()) ? Integer.parseInt(repoIdParam) : 0;
 	        int userId = (userIdParam != null && !userIdParam.isEmpty()) ? Integer.parseInt(userIdParam) : 0;

 	        // Pull privilege attributes safely straight out of active system session container
 	        HttpSession session = request.getSession(false);
 	        int userTypeCode = 0;
 	        if (session != null && session.getAttribute("pr_code_active") != null) {
 	            userTypeCode = Integer.parseInt(session.getAttribute("pr_code_active").toString());
 	        }

 	        // Query our leak-free DAO check method
 	        int currentStatus = dataAccessDao.checkRepositoryAccessPermission(repoId, userId);

 	        // Compile a lightweight JSON string payload response manually to bypass Jackson version crashes
 	        String jsonResponse = "{\"status\":" + currentStatus + ",\"userType\":" + userTypeCode + "}";
 	        
 	        out.print(jsonResponse);
 	        out.flush();
 	    }
 	    
 	    /**
 	     * Streams all active pending access requests in JSON format to the operator's workspace dashboard.
 	     * ROUTE: /FetchPendingRequests
 	     */
 	    @org.springframework.web.bind.annotation.ResponseBody
 	    @org.springframework.web.bind.annotation.RequestMapping(value = "/FetchPendingRequests", 
 	    method = org.springframework.web.bind.annotation.RequestMethod.POST)
 	    public String fetchPendingRequests(javax.servlet.http.HttpServletRequest request) throws Throwable {
 	        Gson gson = new Gson();
 	        return gson.toJson(dataAccessDao.accessPendingDownloadRequests());
 	    }
 	    /**
 	     * Executes the operator decision transaction (Approve/Decline).
 	     * ROUTE: /ProcessRequestAction
 	     */
 	   @org.springframework.web.bind.annotation.ResponseBody
 	    @org.springframework.web.bind.annotation.RequestMapping(value = "/ProcessRequestAction", 
 	    method = org.springframework.web.bind.annotation.RequestMethod.POST)
 	    public String processRequestAction(javax.servlet.http.HttpServletRequest request) throws Throwable {
 	        String requestIdParam = request.getParameter("request_id");
 	        String actionStatusParam = request.getParameter("action_status"); // 2: Approve, 3: Decline

 	        int requestId = (requestIdParam != null) ? Integer.parseInt(requestIdParam) : 0;
 	        int actionStatus = (actionStatusParam != null) ? Integer.parseInt(actionStatusParam) : 3;

 	        boolean isUpdated = dataAccessDao.updateRequestAuthorizationStatus(requestId, actionStatus);
 	        
 	        Gson gson = new Gson();
 	        return gson.toJson(isUpdated);
 	    }
 	    /**
 	     * Logs an official download access request inside the database.
 	     * ROUTE: /submitRepositoryRequest
 	     */
 	    @RequestMapping(value = "/submitRepositoryRequest", method = RequestMethod.POST)
 	    public void submitRepositoryRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
 	        response.setContentType("application/json");
 	        response.setCharacterEncoding("UTF-8");
 	        PrintWriter out = response.getWriter();

 	        String repoIdParam = request.getParameter("repo_id");
 	        String userIdParam = request.getParameter("user_id");
 	        //System.out.println("User ID "+userIdParam);

 	        int repoId = (repoIdParam != null && !repoIdParam.isEmpty()) ? Integer.parseInt(repoIdParam) : 0;
 	        int userId = (userIdParam != null && !userIdParam.isEmpty()) ? Integer.parseInt(userIdParam) : 0;

 	        boolean isLogged = dataAccessDao.submitNewAccessRequest(repoId, userId);

 	        String jsonResponse = "{\"success\":" + isLogged + "}";
 	        
 	        out.print(jsonResponse);
 	        out.flush();
 	    }
 		
 		@ResponseBody
 		@RequestMapping(value = "/CRUDOperationLog", method = RequestMethod.POST)
 		public String CRUDOperationLogFunction(HttpServletRequest request) throws Throwable{
 			Gson gson = new Gson();
 			return gson.toJson(storeParameter.accessCRUDOperationLog());
 		}
 		@ResponseBody
 		@RequestMapping(value = "/CRUDOperationLogByDate", method = RequestMethod.POST)
 		public String CRUDOperationLogByDate(HttpServletRequest request) throws Throwable{
 			String Bydate =  request.getParameter("CRUDdate");
 			//System.out.println("Date is "+Bydate);
 			Gson gson = new Gson();
 			return gson.toJson(storeParameter.accessCRUDOperationLogByDate(Bydate));
 		}
 		
 		//user details
 		@ResponseBody
 		@RequestMapping(value = "/userholder", method = RequestMethod.POST)
 		public String user_details(HttpServletRequest request) throws Throwable{
 			String id = request.getParameter("email");
 			System.out.println("Email Address= "+id);
 			Gson gson = new Gson();
 			return gson.toJson(loginDao.userDetails(id));
 		}
 		@ResponseBody
 		@RequestMapping(value = "/addParameter", method = RequestMethod.POST)
 		public String addParameter(HttpServletRequest request) throws Throwable{
 			String addtionalParam = request.getParameter("data_id");
 			int user_id = Integer.parseInt(request.getParameter("user_id"));
 			String content = request.getParameter("contentID");
 			int cityZId=Integer.parseInt(request.getParameter("cityZID"));
 			//System.out.println("Parameter is "+addtionalParam+", user ID is "+user_id+", Contents is "+content+"City Zone Id is "+cityZId);
 			Gson gson = new Gson();
 			return gson.toJson(uploadDao.addParameter(addtionalParam,user_id,content,cityZId));
 		}
 		@ResponseBody
 		@RequestMapping(value = "/accessByWellIndex", method = RequestMethod.POST)
 		public String granted_project(HttpServletRequest request) throws Throwable{
 			int user_id = Integer.parseInt(request.getParameter("userId"));
 			String wellIndex = request.getParameter("wellIndex");
 			int userType = Integer.parseInt(request.getParameter("userTypeWCR"));
 			//System.out.println("User Role is "+userType +" and Well Index is "+wellIndex);
 			Gson gson = new Gson();
 			return gson.toJson(dataAccessDao.accesswellIndex4DPTHData(user_id,wellIndex, userType));
 		}
 		@ResponseBody
 		@RequestMapping(value = "/access_mod_del", method = RequestMethod.POST)
 		public String access_mode_delete(HttpServletRequest request) throws Throwable{
 			int id = Integer.parseInt(request.getParameter("access_id"));
 			int userType=Integer.parseInt(request.getParameter("user_prvg"));
 			//System.out.println("User Role is "+userType);
 			//System.out.println("User Id from Elite User is "+id);
 			Gson gson = new Gson();
 			return gson.toJson(dataAccessDao.access_modify_delete(id, userType));
 		}
 		
 		@ResponseBody
 		@RequestMapping(value = "/access_mod_gwData", method = RequestMethod.POST)
 		public String access_mode_GWData(HttpServletRequest request) throws Throwable{
 			int userId = Integer.parseInt(request.getParameter("access_id"));
 			int userTpe = Integer.parseInt(request.getParameter("userRole"));
 			//System.out.println("User Role "+userTpe);
 			Gson gson = new Gson();
 			List<ValuePair> results = dataAccessDao.accessOperationalGWData(userId,userTpe);
 			return gson.toJson(results);
 		}
 		@ResponseBody
 		@RequestMapping(value = "/accessGWByWellIndex", method = RequestMethod.POST)
 		public String accessGWDataByWellIndex(HttpServletRequest request) throws Throwable{
 			int userId = Integer.parseInt(request.getParameter("userId"));
 			String wellIndex = request.getParameter("wellIndex");
 			int userType = Integer.parseInt(request.getParameter("userType"));
 			//System.out.println("User Type is "+userType +" and Well index is "+wellIndex);
 			Gson gson = new Gson();
 			List<ValuePair> results = dataAccessDao.accessOperationalGWDataByWellIndex(userId, wellIndex,userType);
 			return gson.toJson(results);
 		}
 		
 		@ResponseBody
 		@RequestMapping(value = "/accessDatatoUpdate", method = RequestMethod.POST)
 		public String acccessWCRforUpdate(HttpServletRequest request) throws Throwable {
 			int id = Integer.parseInt(request.getParameter("well_id"));
 			//System.out.println("User ID is "+id);
 			Gson gson = new Gson();
 			return gson.toJson(dataAccessDao.selectbyWellId(id));
 		}
 		@ResponseBody
 		@RequestMapping(value = "/accessGWODforUpdate", method = RequestMethod.POST)
 		public String acccessGWOforUpdate(HttpServletRequest request) throws Throwable {
 			int id = Integer.parseInt(request.getParameter("well_id"));
 			//System.out.println("Well Id to Access GWO data is "+id);
 			Gson gson = new Gson();
 			return gson.toJson(dataAccessDao.accessGWODbyWellId(id));
 		}
 		@ResponseBody
 		@RequestMapping(value = "/delete_WOfile", method = RequestMethod.POST)
 		public String _deleteWOFile(HttpServletRequest request) throws Throwable{
 	 		String wellID = request.getParameter("well_id");
 	 		String user_id=request.getParameter("user_id");
 			//System.out.println("Well to be deleted is = "+wellID +" and User id is = "+user_id);
 			Gson gson = new Gson();
 			List<File> refreshedReportList = dataAccessDao.executeWellDeletionPipeline(wellID, user_id, "Well Completion Report");

 			return gson.toJson(refreshedReportList);
 		}
 		@ResponseBody
 		@RequestMapping(value = "/delete_ODfile", method = RequestMethod.POST)
 		public String proj_deleteFile(HttpServletRequest request) throws Throwable{

 	 		String well_id = request.getParameter("well_id");
 	 		String user_id=request.getParameter("user_id");
 			//System.out.println("Well to be deleted using Operational Information is = "+well_id);
 			Gson gson = new Gson();
 			List<File> refreshedOperationalList = dataAccessDao.executeWellDeletionPipeline(well_id, user_id, "Operational");

 			return gson.toJson(refreshedOperationalList);
 		}
 		@ResponseBody
 		@RequestMapping(value = "/acc_region", method = RequestMethod.POST)
 		public String access_yearII(HttpServletRequest request) throws Throwable{
 			String reg_name = request.getParameter("reg_name");
 			//System.out.println("Name of Sub city: "+reg_name);
 			Gson gson = new Gson();
 			return gson.toJson( dataAccessDao.count_project(reg_name));
 		}
 		@ResponseBody
 		@RequestMapping(value = "/acc_Legend", method = RequestMethod.POST)
 		public String access_by_gened(HttpServletRequest request) throws Throwable{
 			String reg_name = request.getParameter("leg_name");
 			String wellFunction=request.getParameter("checkList");
 			//System.out.println("Parameter sent from client is : "+reg_name);
 			Gson gson = new Gson();
 			return gson.toJson( dataAccessDao.access_All_wellName_byLegend(reg_name,wellFunction));
 		}
 		@ResponseBody
 		@RequestMapping(value = "/acc_All_pro_data", method = RequestMethod.POST)
 		public String access_project(HttpServletRequest request) throws Throwable{
 			int Pro_def_id = Integer.parseInt(request.getParameter("pro_id"));
 			//System.out.println("Name of region: "+reg_name);
 			Gson gson = new Gson();
 			return gson.toJson( dataAccessDao.access_ReportByWellId(Pro_def_id));
 		}
 		@ResponseBody
 		@RequestMapping(value = "/acc_proNameId", method = RequestMethod.POST)
 		public String access_toOverlayProName(HttpServletRequest request) throws Throwable{
 			//System.out.println("Name of region: "+reg_name);
 			Gson gson = new Gson();
 			return gson.toJson(dataAccessDao.access_well_name());
 		}
 		@ResponseBody
 		@RequestMapping(value = "/acc_proName", method = RequestMethod.POST)
 		public String access_ProName(HttpServletRequest request) throws Throwable{
 			String Pro_name = request.getParameter("ProName");
 			//System.out.println("Project Name....: "+Pro_name);
 			Gson gson = new Gson();
 			return gson.toJson( dataAccessDao.access_specificWellName(Pro_name));
 		}
 		@ResponseBody
 		@RequestMapping(value = "/accessWellBy_parameters", method = RequestMethod.POST)
 		public String accessWellByWellStatus(HttpServletRequest request) throws Throwable {
 			String queryName = request.getParameter("param_name");
 			//System.out.println("Query Parameter = "+queryName);
 			Gson gson = new Gson();
 			return gson.toJson(dataAccessDao.accessByWellStatus(queryName));
 		}
 		@ResponseBody
 		@RequestMapping(value = "/accessWellBy_funcActive", method = RequestMethod.POST)
 		public String accessWellByWellFunctionStatus(HttpServletRequest request) throws Throwable {
 			Gson gson = new Gson();
 			String wellStatus = request.getParameter("param_name");
 			String functionStatus=request.getParameter("activeStatus");
 				return gson.toJson(dataAccessDao.accessByWellFunctionStatus(wellStatus,functionStatus));		
 		}
 		//Access Well Name Category
 		@ResponseBody
 		@RequestMapping(value = "/accessByOwnerCatname", method = RequestMethod.POST)
 		public String accessByOwnerCategpryname(HttpServletRequest request) throws Throwable {
 			Gson gson = new Gson();
 			String ownerCategoryName = request.getParameter("param_name");
 				return gson.toJson(dataAccessDao.accessByOwnerCategpryname(ownerCategoryName));		
 		}
 		//Access Well By Well Depth Range
 		@ResponseBody
 		@RequestMapping(value = "/accessByWellDepthByRange", method = RequestMethod.POST)
 		public String accessByWellDepthByRange(HttpServletRequest request) throws Throwable {
 			Gson gson = new Gson();
 			int depthRange = Integer.parseInt(request.getParameter("param_name"));
 				return gson.toJson(dataAccessDao.accessByWellDepthByRange(depthRange));		
 		}
 		@ResponseBody
 		@RequestMapping(value = "/accessByDischargeRange", method = RequestMethod.POST)
 		public String accessByCurrentDischargeRange(HttpServletRequest request) throws Throwable {
 			Gson gson = new Gson();
 			double dischargeRange = Double.parseDouble(request.getParameter("param_name"));
 				return gson.toJson(dataAccessDao.accessByCurrentDischargeRange(dischargeRange));		
 		}
 		@ResponseBody
 		@RequestMapping(value = "/accessBySWLRange", method = RequestMethod.POST)
 		public String accessByCurrentSWLRange(HttpServletRequest request) throws Throwable {
 			Gson gson = new Gson();
 			double dischargeRange = Double.parseDouble(request.getParameter("param_name"));
 				return gson.toJson(dataAccessDao.accessByCurrentSWLRange(dischargeRange));		
 		}
 		@ResponseBody
 		@RequestMapping(value = "/accessByDWLRange", method = RequestMethod.POST)
 		public String accessByCurrentDWLRange(HttpServletRequest request) throws Throwable {
 			Gson gson = new Gson();
 			double dischargeRange = Double.parseDouble(request.getParameter("param_name"));
 				return gson.toJson(dataAccessDao.accessByCurrentDWLRange(dischargeRange));		
 		}
 		@ResponseBody
 		@RequestMapping(value = "/accessByTRMTRange", method = RequestMethod.POST)
 		public String accessByCurrentTransimissivityRange(HttpServletRequest request) throws Throwable {
 			Gson gson = new Gson();
 			double dischargeRange = Double.parseDouble(request.getParameter("param_name"));
 			//System.out.println("Searched Key in Controllor is "+dischargeRange);
 				return gson.toJson(dataAccessDao.accessByCurrentTransimissivityRange(dischargeRange));		
 		}
 		
 		@ResponseBody
 		@RequestMapping(value = "/waterChemistryUrl", method = RequestMethod.POST)
 		public String waterChemistryUrl(HttpServletRequest request) throws Throwable{
 			int id = Integer.parseInt(request.getParameter("well_id"));
 			//System.out.println(id);
 			Gson gson = new Gson();
 			return gson.toJson(dataAccessDao.accessHydroChemistryData(id));
 		}
 		@ResponseBody
 		@RequestMapping(value = "/accessBYTDS", method = RequestMethod.POST)
 		public String accessBYTDS(HttpServletRequest request) throws Throwable {
 			Gson gson = new Gson();
 			double TDSRange = Double.parseDouble(request.getParameter("param_name"));
 			//System.out.println("TDS value is "+TDSRange);
 				return gson.toJson(dataAccessDao.accessBYTDS(TDSRange));		
 		}
 		@ResponseBody
 		@RequestMapping(value = "/accessBYEC", method = RequestMethod.POST)
 		public String accessBYEC(HttpServletRequest request) throws Throwable {
 			Gson gson = new Gson();
 			double ECRange = Double.parseDouble(request.getParameter("param_name"));
 			//System.out.println("EC value is "+ECRange);
 				return gson.toJson(dataAccessDao.accessBYEC(ECRange));		
 		}
 		@ResponseBody
 		@RequestMapping(value = "/accessBYFlouride", method = RequestMethod.POST)
 		public String accessBYFlouride(HttpServletRequest request) throws Throwable {
 			Gson gson = new Gson();
 			double ECRange = Double.parseDouble(request.getParameter("param_name"));
 			//System.out.println("EC value is "+ECRange);
 				return gson.toJson(dataAccessDao.accessBYFlouride(ECRange));		
 		}
 		@ResponseBody
 		@RequestMapping(value = "/accessBYIron", method = RequestMethod.POST)
 		public String accessBYIron(HttpServletRequest request) throws Throwable {
 			Gson gson = new Gson();
 			double ECRange = Double.parseDouble(request.getParameter("param_name"));
 			//System.out.println("EC value is "+ECRange);
 				return gson.toJson(dataAccessDao.accessBYIron(ECRange));		
 		}
 		@ResponseBody
 		@RequestMapping(value = "/accessBYNitrate", method = RequestMethod.POST)
 		public String accessBYNitrate(HttpServletRequest request) throws Throwable {
 			Gson gson = new Gson();
 			double No3Range = Double.parseDouble(request.getParameter("param_name"));
 			//System.out.println("EC value is "+ECRange);
 				return gson.toJson(dataAccessDao.accessBYNitrate(No3Range));		
 		}
 		@ResponseBody
 		@RequestMapping(value = "/accessBYTemprature", method = RequestMethod.POST)
 		public String accessBYTemprature(HttpServletRequest request) throws Throwable {
 			Gson gson = new Gson();
 			double tempratureRV = Double.parseDouble(request.getParameter("param_name"));
 			//System.out.println("Temp Renge Value is "+tempratureRV);
 				return gson.toJson(dataAccessDao.accessBYTemprature(tempratureRV));		
 		}
 		
 	    /**
 	     * Streams real-time borehole status and ownership count statistics to the frontend map workspace.
 	     * ROUTE: /FetchWellSummaryCounts
 	     */
 	    @org.springframework.web.bind.annotation.ResponseBody
 	    @org.springframework.web.bind.annotation.RequestMapping(value = "/FetchWellSummaryCounts", method = org.springframework.web.bind.annotation.RequestMethod.POST)
 	    public String fetchWellSummaryCounts(javax.servlet.http.HttpServletRequest request) throws Throwable {
 	        com.google.gson.Gson gson = new com.google.gson.Gson();
 	        return gson.toJson(dataAccessDao.compileWellInventoryMetrics());
 	    }

 		@ResponseBody
 		@RequestMapping(value = "/R_rport", method = RequestMethod.POST)
 		public String report_bar(HttpServletRequest request) throws Throwable{
 			Gson gson = new Gson();
 			return gson.toJson( dataAccessDao.reports_bar());
 		}
}