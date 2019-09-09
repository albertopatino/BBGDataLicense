package cellphi;

public class SwitchTest {
	public final static String UNO="UNO";
	public final static String DOS="DOS";
	public final static String TRES="TRES";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int i=0;
		int argsCount=args.length;
		System.out.println("Parameters: "+Integer.toString(argsCount));

		int value = 0;
		if ( argsCount > 0) {
			while(i < argsCount){
				switch(args[i].toUpperCase()) {
				    case UNO: 
				    	value=1;
				    	break;			    
				    case DOS: 
				    	value=2;
				    	break;
				    case TRES: 
				    	value=3;
				    	break;
				    default: 
				    	value=-1; //UNKNOWN
				    	break;
				}
				i++;
				System.out.println("Value is "+Integer.toString(value));
			}
		}
		else {
			System.out.println("No parameters");
		}
	}
}
