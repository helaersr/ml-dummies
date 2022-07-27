/**
 * 
 */
package mltutorial;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import cern.jet.random.Exponential;
import cern.jet.random.Normal;
import cern.jet.stat.Gamma;
import cern.jet.stat.Probability;

import Jama.Matrix;

/**
 * @author Raphaël Helaers
 *
 */
public class Tools {
	//Parameters of the Normal distribution used to generate random multiplicators
	private static final double normalMean = 1;
	private static final double normalSD = 0.5;
		

	public static String doubletoString (double x, int d) {
		if (x != 0 && Math.abs(x) < Math.pow(10, -d)){
	    if (Double.isNaN(x) || Double.isInfinite(x)) {
	      return "" + x;
	    }
	    DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	    dfs.setDecimalSeparator('.');
	    dfs.setGroupingSeparator(',');
	    String card = "";
	    for (int i=0 ; i < d ; i++) card += "#";
	    NumberFormat formatter = new DecimalFormat("0."+card+"E0", dfs);
	    return formatter.format(x);			
		}else{
			NumberFormat fmt = NumberFormat.getInstance(Locale.US);
			if (fmt instanceof DecimalFormat) { 		 
				DecimalFormatSymbols symb = new DecimalFormatSymbols(Locale.US);
				symb.setGroupingSeparator(' ');
				((DecimalFormat) fmt).setDecimalFormatSymbols(symb);
				((DecimalFormat) fmt).setMaximumFractionDigits(d);
				//((DecimalFormat) fmt).setMinimumFractionDigits(d);
				((DecimalFormat) fmt).setGroupingUsed(true);
			}
			String s = fmt.format(x);
			return s;
		}
	}

	public static String doubleToPercent(double x, int d) {
		x *= 100;
		NumberFormat fmt = NumberFormat.getInstance(Locale.US);
		if (fmt instanceof DecimalFormat) { 		 
			DecimalFormatSymbols symb = new DecimalFormatSymbols(Locale.US);
			symb.setGroupingSeparator(' ');
			((DecimalFormat) fmt).setDecimalFormatSymbols(symb);
			((DecimalFormat) fmt).setMaximumFractionDigits(d);
			//((DecimalFormat) fmt).setMinimumFractionDigits(d);
			((DecimalFormat) fmt).setGroupingUsed(true);
		}
		String s = fmt.format(x) + "%";		
		return s;		
	}
	
	public static boolean isIdentity(Matrix M){
		for (int i=0 ; i < M.getRowDimension() ; i++){
			for (int j=0 ; j < M.getColumnDimension() ; j++){
				if (i==j){
					if (M.get(i, j) != 1) return false;
				}else{
					if (M.get(i, j) != 0) return false;
				}
			}			
		}
		return true;
	}
	
	/**
	 * Return a random number greater or equal to 0 and smaller than max
	 * @param max
	 * @return
	 */
	public static int randInt(int max){
		return (int)Math.floor(Math.random()*max);
	}
	
	public static double positiveNormalRand(){
		double rand = 1;
		do{
			rand = Normal.staticNextDouble(normalMean, normalSD);
		}while (rand <= 0.4);
		return rand;
	}
	
	/**
	 * Random number taken from an exponential of lambda 2. 0.5 is added to it so the minimum becomes 0.5 and mean 1.
	 * @return
	 */
	public static double exponentialMultiplierRand(){
		return Exponential.staticNextDouble(2) + 0.5;
	}
	
	public static double percentagePointChi2 (double prob, double v) {
	/* returns z so that Prob{x<z}=prob where x is Chi2 distributed with df=v
	   returns -1 if in error.
	   0.000002<prob<0.999998
	   RATNEST FORTRAN by
	       Best DJ & Roberts DE (1975) The percentage points of the 
	       Chi2 distribution.  Applied Statistics 24: 385-388.  (AS91)
	   Converted into C by Ziheng Yang, Oct. 1993.
	   Converted into Java by Raphael Helaers, Oct. 2008
	*/
		double e=.5e-6, aa=.6931471805, p=prob, g, small=1e-6;
		double xx, c, ch, a=0,q=0,p1=0,p2=0,t=0,x=0,b=0,s1,s2,s3,s4,s5,s6;

		if (p<small)   return(0);
		if (p>1-small) return(9999);
		if (v<=0)      return (-1);

		g = Gamma.logGamma(v/2);
		xx=v/2;   c=xx-1;
		if (v < -1.24*Math.log(p)) {
			ch=Math.pow((p*xx*Math.exp(g+xx*aa)), 1/xx);
			if (ch-e<0) return (ch);
		}else{
			if (v <=.32){
				ch=0.4;   a=Math.log(1-p);
				do{ 
					q=ch;  p1=1+ch*(4.67+ch);  p2=ch*(6.73+ch*(6.66+ch));
					t=-0.5+(4.67+2*ch)/p1 - (6.73+ch*(13.32+3*ch))/p2;
					ch-=(1-Math.exp(a+g+.5*ch+c*aa)*p2/p1)/t;
				}while (Math.abs(q/ch-1)-.01 > 0); 
			}else{
				x = Probability.normalInverse(p);
				p1=0.222222/v;   ch=v*Math.pow((x*Math.sqrt(p1)+1-p1), 3.0);
				if (ch>2.2*v+6)  ch=-2*(Math.log(1-p)-c*Math.log(.5*ch)+g);
			}
		}
		do{
			q=ch;   p1=.5*ch;
			if ((t=Gamma.incompleteGamma(xx, p1))<0) return -1;
			p2=p-t;
			t=p2*Math.exp(xx*aa+g+p1-c*Math.log(ch));   
			b=t/ch;  a=0.5*t-b*c;

			s1=(210+a*(140+a*(105+a*(84+a*(70+60*a))))) / 420;
			s2=(420+a*(735+a*(966+a*(1141+1278*a))))/2520;
			s3=(210+a*(462+a*(707+932*a)))/2520;
			s4=(252+a*(672+1182*a)+c*(294+a*(889+1740*a)))/5040;
			s5=(84+264*a+c*(175+606*a))/2520;
			s6=(120+c*(346+127*c))/5040;
			ch+=t*(1+0.5*t*s1-b*c*(s1-b*(s2-b*(s3-b*(s4-b*(s5-b*s6))))));
		}while (Math.abs(q/ch-1) > e) ;
		return (ch);
	}
		
}
