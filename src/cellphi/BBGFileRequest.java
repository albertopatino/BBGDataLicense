package cellphi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class BBGFileRequest extends BBGFile {
	
	List<String> fields=new ArrayList<String>();
	List<String> instruments=new ArrayList<String>();
	
	int nFields;
	
	String firmName;
	
	String programName;
	
	String compress;
	
	String programFlag;
	
	String closingValues;
	
	String userNumber;
	
	String sN;
	
	String wS;
	
	String outputFileName;
	
	boolean sof=false;
    
	boolean sod=false;
	
	public BBGFileRequest(String file) {
		super(file);
		
		firmName="defaultFirmName";
		programName="defaultProgramName";
		compress="defaultCompress";
		programFlag="defaultProgramFlag";
		closingValues="defaultClosing";
		userNumber="000000";
		sN="000000";
		wS="0";
	}

    public void processBBGRequestFile() throws FileNotFoundException {

        //Scanner is = new Scanner(new FileReader(bbgFileName));
    	BufferedReader is = new BufferedReader(new FileReader(bbgFileName));
        
        String line;
        
        try {
			while ((line=is.readLine()) != null) {
			    parseBBGLine(line); 
			}
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
        
        generateBBGReplyFile();
        
    }
    
    public String getOutputName() {
    	return outputFileName;
    }
    
    private void setOutputName(String outputName) {
    	outputFileName = outputName;
    }
    
    private String generateDate() {
    	DateFormat df = new SimpleDateFormat("yyyyMMdd");
    	
    	Calendar cal = Calendar.getInstance();
    	return df.format(cal.getTime());
    }
    
    private String now() {
        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
    	
    	Calendar cal = Calendar.getInstance();
    	return df.format(cal.getTime());
    }
    
    public void parseBBGLine(String line) {
        
		String token=new String("=");
        String command;

        StringTokenizer st=new StringTokenizer(line, token);
        int count=st.countTokens();
        
        String value="";
        
        if (count==2) {
            command=st.nextToken();
            value=st.nextToken();
        } else if (count==1){
            command=st.nextToken();
        } else {
            // Ignore Bad Syntax
        	command="INVALID";
        }
        BBGEntryId type;
                
        switch( command.toUpperCase() ){
		case "START-OF-FILE":
			type = BBGEntryId.START;
			break;
		case "FIRMNAME":
			type = BBGEntryId.FIRM_NAME;
			firmName=value;
			break;
		case "PROGRAMNAME":
			type = BBGEntryId.FIRM_NAME;
			programName=value;
			break;
		case "COMPRESS":
			type = BBGEntryId.COMPRESS;
			compress=value;
			break;
		case "PROGRAMFLAG":
			type = BBGEntryId.FLAGS;
			programFlag=value;
			break;
		case "CLOSINGVALUES":
			type = BBGEntryId.FLAGS;
			closingValues=value;
			break;
		case "USERNUMBER":
			type = BBGEntryId.FLAGS;
			userNumber=value;
			break;
		case "SN":
			type = BBGEntryId.FLAGS;
			sN=value;
			break;
		case "WS":
			type = BBGEntryId.FLAGS;
			wS=value;
			break;
		case "START-OF-FIELDS":
			sof=true;
			break;
		case "END-OF-FIELDS":
            sof=false;
			break;
		case "START-OF-DATA":
			sod=true;
			break;
		case "END-OF-DATA":
			sod=false;
			break;
		case "END-OF-FILE":
			break;
		case "INVALID":
			break;
		default:
			if (sof==true) {
				fields.add(command);
			} else
			    if (sod==true) {
			    	String ticker=command.substring(0, command.indexOf('|'));
				    instruments.add(ticker);
			    }
        	type=BBGEntryId.DATA;
        }
    }
    
    private void generateBBGReplyFile() throws FileNotFoundException {
    	
    	if (bbgFileName.length()==0)
    		throw new FileNotFoundException();
    	
    	outputFileName=bbgFileName.substring(0,bbgFileName.lastIndexOf(".req")+1)+"out";
    	
    	String outputFileName=getOutputName();
    	PrintWriter out = new PrintWriter(outputFileName);

    	out.println("START-OF-FILE");
    	
    	String date=generateDate();
    	out.println("RUNDATE"+"="+date);
    	
    	out.println("REPLYFILENAME"+"="+outputFileName);
    	
    	out.println("FIRMNAME"+"="+firmName);
    	
    	out.println("PROGRAMNAME"+"="+programName);
    	
    	out.println("COMPRESS"+"="+compress);
    	out.println("PROGRAMFLAG"+"="+programFlag);
    	out.println("CLOSINGVALUES"+"="+closingValues);
    	out.println("USERNUMBER"+"="+userNumber);
    	out.println("SN"+"="+sN);
    	out.println("WS"+"="+wS);

    	out.println("START-OF-FIELDS");
    	for(String s: fields ) {
    		out.println(s);
    	}
    	out.println("END-OF-FIELDS");
    	
    	nFields=fields.size();
    	
    	out.println("TIMESTARTED"+"="+now());
    	
    	out.println("START-OF-DATA");
    	
    	int row=0;
    	
    	for(String s: instruments ) {
    		row++;
    		String data = generateData(s,row);
    		out.println(data);
    	}
    	out.println("END-OF-DATA");
    	
    	out.println("TIMEFINISHED"+"="+now());
    	
    	out.println("END-OF-FILE");
    	
    	out.close();
	}
    
    private String generateData(String instrument,int nInstrument) {
    	
    	StringBuilder sb = new StringBuilder(instrument);
    	
    	sb.append("|0|"); sb.append(Integer.toString(nFields));sb.append("|");
    	
    	String decimal="";
    	
    	for (int i=0; i< nFields; i++) {
    		
    		if (nFields < 10) {
    			decimal=Integer.toString(i)+"00000"; 
    		} else {
    			if ( i < 10 ) {
    				decimal="0"+Integer.toString(i)+"0000";
    			} else {
    				decimal=Integer.toString(i)+"0000";
    			}
    		}
    		sb.append(Integer.toString(nInstrument));
    		sb.append(".");
    		sb.append(decimal);
    		sb.append("|");
    	}
    	return sb.toString();
    }
}
