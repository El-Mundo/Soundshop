package math;

public class FFT {
	//Processing's default FFT function only support real-time decoding so we use an alternative algorithm
	//https://introcs.cs.princeton.edu/java/97data/FFT.java.html
		
	public static double[] fft(final double[] inputReal, double[] inputImag, boolean DIRECT) {
		//https://stackoverflow.com/questions/3287518/reliable-and-fast-fft-in-java
		int n = inputReal.length;
		
		double ld = Math.log(n) / Math.log(2.0);
		
		if(((int)ld) - ld != 0) {
			System.out.println("The number of elements is not a power of 2.");
			return null;
		}
		
		int nu = (int) ld;
		int n2 = n / 2;
		int nu1 = nu - 1;
		double[] xReal = new double[n];
		double[] xImag = new double[n];
		double tReal, tImag, p, arg, c, s;
		
		double constant;
		if (DIRECT)
			constant = -2 * Math.PI;
		else
			constant = 2 * Math.PI;
		
		for (int i = 0; i < n; i++) {
			xReal[i] = inputReal[i];
			xImag[i] = inputImag[i];
		}
		
		// First phase - calculation
	    int k = 0;
	    for (int l = 1; l <= nu; l++) {
	        while (k < n) {
	            for (int i = 1; i <= n2; i++) {
	                p = bitreverseReference(k >> nu1, nu);
	                // direct FFT or inverse FFT
	                arg = constant * p / n;
	                c = Math.cos(arg);
	                s = Math.sin(arg);
	                tReal = xReal[k + n2] * c + xImag[k + n2] * s;
	                tImag = xImag[k + n2] * c - xReal[k + n2] * s;
	                xReal[k + n2] = xReal[k] - tReal;
	                xImag[k + n2] = xImag[k] - tImag;
	                xReal[k] += tReal;
	                xImag[k] += tImag;
	                k++;
	            }
	            k += n2;
	        }
	        k = 0;
	        nu1--;
	        n2 /= 2;
	    }

	    // Second phase - recombination
	    k = 0;
	    int r;
	    while (k < n) {
	        r = bitreverseReference(k, nu);
	        if (r > k) {
	            tReal = xReal[k];
	            tImag = xImag[k];
	            xReal[k] = xReal[r];
	            xImag[k] = xImag[r];
	            xReal[r] = tReal;
	            xImag[r] = tImag;
	        }
	        k++;
	    }

	    double[] newArray = new double[xReal.length * 2];
	    double radice = 1 / Math.sqrt(n);
	    for (int i = 0; i < newArray.length; i += 2) {
	        int i2 = i / 2;
	        newArray[i] = xReal[i2] * radice;
	        newArray[i + 1] = xImag[i2] * radice;
	    }
	    return newArray;
	}
	
	private static int bitreverseReference(int j, int nu) {
	    int j2;
	    int j1 = j;
	    int k = 0;
	    for (int i = 1; i <= nu; i++) {
	        j2 = j1 / 2;
	        k = 2 * k + j1 - 2 * j2;
	        j1 = j2;
	    }
	    return k;
	}
	
	
	// compute the FFT of x[], assuming its length n is a power of 2
    public static Complex[] fft(Complex[] x) {
        int n = x.length;

        // base case
        if (n == 1) return new Complex[] { x[0] };

        // radix 2 Cooley-Tukey FFT
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }

        // compute FFT of even terms
        Complex[] even = new Complex[n/2];
        for (int k = 0; k < n/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] evenFFT = fft(even);

        // compute FFT of odd terms
        Complex[] odd  = even;  // reuse the array (to avoid n log n space)
        for (int k = 0; k < n/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] oddFFT = fft(odd);

        // combine
        Complex[] y = new Complex[n];
        for (int k = 0; k < n/2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex((float)Math.cos(kth), (float)Math.sin(kth));
            y[k]       = evenFFT[k].plus (wk.times(oddFFT[k]));
            y[k + n/2] = evenFFT[k].minus(wk.times(oddFFT[k]));
        }
        return y;
    }


    // compute the inverse FFT of x[], assuming its length n is a power of 2
    public static Complex[] ifft(Complex[] x) {
        int n = x.length;
        Complex[] y = new Complex[n];

        // take conjugate
        for (int i = 0; i < n; i++) {
            y[i] = x[i].conjugate();
        }

        // compute forward FFT
        y = fft(y);

        // take conjugate again
        for (int i = 0; i < n; i++) {
            y[i] = y[i].conjugate();
        }

        // divide by n
        for (int i = 0; i < n; i++) {
            y[i] = y[i].scale((float) (1.0 / n));
        }

        return y;

    }

    // compute the circular convolution of x and y
    public static Complex[] cconvolve(Complex[] x, Complex[] y) {

        // should probably pad x and y with 0s so that they have same length
        // and are powers of 2
        if (x.length != y.length) {
            throw new IllegalArgumentException("Dimensions don't agree");
        }

        int n = x.length;

        // compute FFT of each sequence
        Complex[] a = fft(x);
        Complex[] b = fft(y);

        // point-wise multiply
        Complex[] c = new Complex[n];
        for (int i = 0; i < n; i++) {
            c[i] = a[i].times(b[i]);
        }

        // compute inverse FFT
        return ifft(c);
    }


    // compute the linear convolution of x and y
    public static Complex[] convolve(Complex[] x, Complex[] y) {
        Complex ZERO = new Complex(0, 0);

        Complex[] a = new Complex[2*x.length];
        for (int i = 0;        i <   x.length; i++) a[i] = x[i];
        for (int i = x.length; i < 2*x.length; i++) a[i] = ZERO;

        Complex[] b = new Complex[2*y.length];
        for (int i = 0;        i <   y.length; i++) b[i] = y[i];
        for (int i = y.length; i < 2*y.length; i++) b[i] = ZERO;

        return cconvolve(a, b);
    }

    // compute the DFT of x[] via brute force (n^2 time)
    public static Complex[] dft(Complex[] x) {
        int n = x.length;
        Complex ZERO = new Complex(0, 0);
        Complex[] y = new Complex[n];
        for (int k = 0; k < n; k++) {
            y[k] = ZERO;
            for (int j = 0; j < n; j++) {
                int power = (k * j) % n;
                double kth = -2 * power *  Math.PI / n;
                Complex wkj = new Complex((float)Math.cos(kth), (float)Math.sin(kth));
                y[k] = y[k].plus(x[j].times(wkj));
            }
        }
        return y;
    }

    // display an array of Complex numbers to standard output
    public static void show(Complex[] x, String title) {
        System.out.println(title);
        System.out.println("-------------------");
        for (int i = 0; i < x.length; i++) {
        	System.out.println(x[i]);
        }
        System.out.println();
    }

}
