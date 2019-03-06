package utilities;

public class Utilities {

	public static boolean debug = false;
	public static boolean info = true;

	public static void printDebug(String msg) {
		if(Utilities.debug){
			System.out.println(msg);
		}
	}

	public static void printInfo(String msg){
		if(Utilities.info){
			System.out.println(msg);
		}
	}
}
