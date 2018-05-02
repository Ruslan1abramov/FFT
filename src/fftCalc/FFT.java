package fftCalc;
//ruslan abramov 306847393
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FFT {
	
	static final String inFileName = "input.txt";
	static final String outFileName = "output.txt";
	static ArrayList<ComplexNumber> polynom = new ArrayList<ComplexNumber>();
	static ArrayList<ComplexNumber> rootOfUnity = new ArrayList<ComplexNumber>();
	static int numberOfRoots;
	static Scanner fileIn = null;
	static FileWriter fileWriter = null;
	static PrintWriter printWriter = null;
			
	public static void main(String[] args) {
		fileManager(inFileName);
		FFT_calc myVector = new FFT_calc(polynom , rootOfUnity);
		System.out.println(myVector.fftVectorString());
		
		/*
		polynom = new ArrayList<ComplexNumber>();;
		fileManager("output.txt");
		FFT_calc myVector1 = new FFT_calc(polynom , rootOfUnity);
		System.out.println(myVector.fftVectorString());
		
		
		fftRev(fftmull2Vectors(myVector.getFftVector(),myVector1.getFftVector()));
		*/
		
		
		writeOutput(myVector.fftVectorString());
	}
	public static void fileManager(String file){
		try {
			fileIn = new Scanner(new FileReader(file));
			readAndProcessData();
			fileIn.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void readAndProcessData(){
		//reading first line
		if(fileIn.hasNextLine()){
			double log2OfN = Math.log(Integer.parseInt(fileIn.nextLine()))/Math.log(2);
			//we want numberOfRoots to be a power of 2
			//add 1 in case we reduced the value of LOG2(n)
			numberOfRoots = log2OfN -(int)log2OfN != 0 ? (int)log2OfN + 1 : (int)log2OfN;
			numberOfRoots = (int)Math.pow(2 ,numberOfRoots);
			if(fileIn.hasNextLine()){
				parsePolynom(fileIn.nextLine());
				rootOfUnityGenerator();
			}
		}
		
	}
	
	public static void parsePolynom(String line){
		String[] complexCoefficient = line.split(",");
		for(int counter = 0 ; counter < complexCoefficient.length ; counter++){
			//splitting a sting that is build this way : a+bi
			String[] complexNumbers = complexCoefficient[counter].split("\\+");
			//after the split - complexNumbers[0] = a , complexNumbers[1] = bi
			double real = Double.parseDouble(complexNumbers[0]);
			//converting b to a double
			double imaginary = Double.parseDouble(complexNumbers[1].substring(0, complexNumbers[1].length() -1));
			polynom.add(new ComplexNumber (real , imaginary));
		}
	}
	
	public static void rootOfUnityGenerator(){
		double baseAngle = Math.PI*2/numberOfRoots;
		for(int counter = 0 ; counter < numberOfRoots ; counter++){
			rootOfUnity.add(new ComplexNumber(Math.cos(baseAngle*counter), Math.sin(baseAngle*counter)));
		}
	}
	
	public static String polynomToString(){
		String polynomString = "";
		for(ComplexNumber complexNum : polynom){
			polynomString += complexNum.toString() + "x^" + (polynom.size() - polynom.indexOf(complexNum) - 1) +" +";
		}
		return polynomString.substring(0, polynomString.length()-1);
	}
	
	public static void writeOutput(String outMsg){
		try {
			fileWriter = new FileWriter(new File(outFileName));
			printWriter = new PrintWriter(fileWriter);
			
			printWriter.println(outMsg);
			printWriter.close();
			fileWriter.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<ComplexNumber> fftmull2Vectors(ArrayList<ComplexNumber> fft1 , ArrayList<ComplexNumber> fft2){
		ArrayList<ComplexNumber> fftMull = new ArrayList<ComplexNumber>();
		for(int counter = 0 ; counter < fft1.size() ; counter++)
			fftMull.add(ComplexNumber.multiply(fft1.get(counter), fft2.get(counter)));
		return fftMull;
	}
	
	public static ArrayList<ComplexNumber> fftRev(ArrayList<ComplexNumber> fftVector){
		ArrayList<ComplexNumber> polynomial = new ArrayList<ComplexNumber>();
		
		ComplexNumber omega = new ComplexNumber(Math.cos(Math.PI*2/numberOfRoots), Math.sin(Math.PI*2/numberOfRoots));
		
		for(int counter = 0 ; counter < fftVector.size() ; counter++){
			ComplexNumber complexCoefficient = fftVector.get(0);
			for(int innerCounter = 1 ; innerCounter < fftVector.size() ; innerCounter++){
				ComplexNumber factor = omega;
				for(int factorPow = 0 ; factorPow < counter * innerCounter ; factorPow++)
					factor.multiply(omega);
				factor.multiply(new ComplexNumber(-1 , 0));
				complexCoefficient.add(ComplexNumber.multiply(fftVector.get(innerCounter), factor));
			}
			complexCoefficient.multiply(new ComplexNumber(1/ fftVector.size() , 0));
			polynomial.add(complexCoefficient);
		}
		System.out.println(polynomial);
		return polynomial;
	}

}
