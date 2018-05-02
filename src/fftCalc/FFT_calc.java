package fftCalc;
//ruslan abramov 306847393
import java.util.ArrayList;

public class FFT_calc {
	private ArrayList<ComplexNumber> polynom;
	private ArrayList<ComplexNumber> rootOfUnity;
	private ArrayList<ComplexNumber> fftVector;
	
	public FFT_calc(ArrayList<ComplexNumber> polynom , ArrayList<ComplexNumber> rootOfUnity){
		this.rootOfUnity = rootOfUnity;
		this.polynom = polynom;
		this.setFftVector(caluculateFFT(this.polynom, 0));
	}
	
	private ArrayList<ComplexNumber> caluculateFFT(ArrayList<ComplexNumber> myPolynom, int level){
		//halting condition
		//if the recursing we return the number of over Coefficient  
		if(myPolynom.size() == 1){
			ArrayList<ComplexNumber> fftVector = new ArrayList<ComplexNumber>();
			for(int counter = 0 ;  counter < rootOfUnity.size() ; counter++)
				fftVector.add(myPolynom.get(0));
			return fftVector;
		}
		ArrayList<ComplexNumber> polynomEven = polynomEven(myPolynom);
		ArrayList<ComplexNumber> polynomOdd = polynomOdd(myPolynom);
		
		//calculating the fft Coefficients
		ArrayList<ComplexNumber> fftVectorEven = caluculateFFT(polynomEven, level+1);
		ArrayList<ComplexNumber> fftVectorOdd =caluculateFFT(polynomOdd, level+1);
		//factoring the odd Polynomial
		ArrayList<ComplexNumber> fftVectorOddMullByX = mullFFTodd(fftVectorOdd, level +1);
		
		return addFFTVector(fftVectorEven , fftVectorOddMullByX);
		
	}
	
	//adding the odd and even Polynomial
	private ArrayList<ComplexNumber> addFFTVector(ArrayList<ComplexNumber> fftVectorEven ,ArrayList<ComplexNumber> fftVectorOdd ){
		ArrayList<ComplexNumber>finalFFTVector = new ArrayList<ComplexNumber> ();

		for(int counter = 0 ; counter < fftVectorOdd.size() ; counter++){
			finalFFTVector.add(ComplexNumber.add(fftVectorOdd.get(counter), fftVectorEven.get(counter)));
			
			}
		return finalFFTVector;
	}
	//factoring the odd Polynomial
	private ArrayList<ComplexNumber> mullFFTodd(ArrayList<ComplexNumber> fftVectorOdd  , int level){
		ArrayList<ComplexNumber> mullFFTodd = new ArrayList<ComplexNumber> ();
		for(int counter = 0 ; counter < fftVectorOdd.size() ; counter++){
			ComplexNumber complexNum = fftVectorOdd.get(counter);
			//factoring the odd Polynomial level times
			for(int counter2 = 0 ; counter2 < level ; counter2++)
				complexNum = complexNum.multiply(rootOfUnity.get(counter));
			mullFFTodd.add(complexNum);
			}
		return mullFFTodd;
	}
	//creating a Polynomial from even Coefficients
	private ArrayList<ComplexNumber> polynomEven(ArrayList<ComplexNumber> myPolynom){
		ArrayList<ComplexNumber> polynomEven = new ArrayList<ComplexNumber> ();
		for(int counter = 0 ; counter < myPolynom.size(); counter++){
			if( myPolynom.size() % 2 != 0){
				if( counter % 2 == 0)
					polynomEven.add(myPolynom.get(counter));
			}
			else{
				if( counter % 2 == 1)
					polynomEven.add(myPolynom.get(counter));
			}
		}
		return polynomEven;
	}
	
	//creating a Polynomial from odd Coefficients
	private ArrayList<ComplexNumber> polynomOdd(ArrayList<ComplexNumber> myPolynom){
		ArrayList<ComplexNumber> polynomOdd = new ArrayList<ComplexNumber> ();
		for(int counter = 0 ; counter < myPolynom.size(); counter++){
			if( myPolynom.size() % 2 != 0){
				if( counter % 2 == 1)
					polynomOdd.add(myPolynom.get(counter));
			}
			else{
				if( counter % 2 == 0)
					polynomOdd.add(myPolynom.get(counter));
			}
		}
		return polynomOdd;
	}

	public ArrayList<ComplexNumber> getFftVector() {
		return fftVector;
	}

	public void setFftVector(ArrayList<ComplexNumber> fftVector) {
		this.fftVector = fftVector;
	}
	
	public String fftVectorString(){
		String fftString = "";
		for(ComplexNumber complexNum : getFftVector())
			fftString += complexNum.toString() +", ";
		return fftString.substring(0, fftString.length() -2);
	}
	
}
