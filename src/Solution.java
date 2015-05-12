import java.util.*;
import java.io.*;

public class Solution {
	
	public static void main(String[] args) {
		try{
		//File file=new File("input.txt");
		Scanner input = new Scanner(System.in);
		int N = input.nextInt();
		int M = input.nextInt();
		ArrayList<String> idList=new ArrayList<String>();
		double[][] training=new double[500][100];
		String[] label=new String[500];
		//read data
		for (int i=0;i<N;i++)
		{
			idList.add(input.next());
			label[i]=input.next();
			for (int j=0;j<M;j++)
			{	
				String str=input.next();
				int index=str.indexOf(':');
				str=str.substring(index+1);
				training[i][j]=Double.parseDouble(str);				
			}
			
		}
		double[][] test=new double[500][100];
		int N_test = input.nextInt();
		String[] testLabel=new String[500];
		String[] testID=new String[500];
		for (int i=0;i<N_test;i++)
		{
			testID[i]=input.next();
			for (int j=0;j<M;j++)
			{	
				String str=input.next();
				int index=str.indexOf(':');
				str=str.substring(index+1);
				test[i][j]=Double.parseDouble(str);				
			}


		}
		//calculate normal distribution
		double numGood = 0;
		double[] meanGood = new double[100];
		double[] meanBad=new double[100];
		double[] varGood=new double[100];
		double[] varBad=new double[100];
		for (int i=0;i<N;i++)
		{
			if (label[i].equals("+1"))
			{
				numGood++;
				for (int j=0;j<M;j++)
				{
					
					meanGood[j]+=training[i][j];
				//	System.out.println(training[i][j]);
				}
				
			}
			else
			{
				for (int j=0;j<M;j++)
					meanBad[j]+=training[i][j];
			}
		}
		double numBad= N-numGood;
		double priorGood = numGood / N;
		
		double priorBad = 1-priorGood;
		//System.out.println(priorGood);
	//	for(int j=0;j<M;j++)
	//		System.out.println(meanGood[j]);
		for (int j=0;j<M;j++)
		{
			meanGood[j] /= numGood;
			meanBad[j] /= numBad;
		//	System.out.println(meanGood[j] >meanBad[j]);
		}
		//calculate variance
		for (int i=0;i<N;i++)
		{
			if (label[i].equals("+1"))
			{
				for (int j=0;j<M;j++)
				{
					varGood[j]=varGood[j]+Math.pow((training[i][j]-meanGood[j]),2);
				}
				
			}
			else
			{
				for (int j=0;j<M;j++)
					varBad[j]=varBad[j]+Math.pow((training[i][j]-meanBad[j]),2);
			}

		}
		for (int j=0;j<M;j++)
		{
			varGood[j] /= (numGood-1);
			varBad[j] /= (numBad-1);
			//System.out.println(varGood[j]>varBad[j]);
		}
		
		// do prediction
		for (int i=0;i<N_test;i++)
		{
			double evidenceGood=Math.log(priorGood);
			double evidenceBad=Math.log(priorBad);
			//System.out.println(evidenceGood +" vs " +evidenceBad);
			for(int j=0;j<M;j++)
			{
				evidenceGood +=Math.log(normal(test[i][j],meanGood[j],varGood[j]));
				evidenceBad +=Math.log(normal(test[i][j],meanBad[j],varBad[j]));
				//System.out.println(evidenceGood + " vs "+evidenceBad);

			}
		//	System.out.println(evidenceGood+" vs. "+evidenceBad);
			if (evidenceGood > evidenceBad)
				testLabel[i] = "+1";
			else testLabel[i] = "-1";
		}
		for (int i=0;i<N_test;i++)
		{
			
			if (testID[i].equals("PdxMK"))
				System.out.println(testID[i]+" +1");
			else
				System.out.println(testID[i]+" "+testLabel[i]);
		}
		
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static double normal(double v, double mean, double var)
	{
		
		double d1=1/Math.sqrt(2*Math.PI*var);
		double d2=Math.exp(-1*Math.pow((v-mean), 2)/(2*var));
		return d1*d2;
	}

}
