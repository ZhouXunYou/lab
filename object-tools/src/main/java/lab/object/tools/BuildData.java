package lab.object.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class BuildData {
	public static void main(String[] args) throws Exception {
		Random random = new Random();
		
		String[] kpiCodes = new String[] {"10010110011","10010110021","10010110031","10010110041","10010110051"};
		String[] instances = new String[] {"CPU_1","CPU_2","CPU_3","CPU_4","CPU_5","CPU_6","CPU_7","CPU_8","CPU_9","CPU_10","CPU_11","CPU_12","CPU_13","CPU_14","CPU_15","CPU_16"};
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long startTime = c.getTimeInMillis();
		int count = 12;
		InputStream in = new FileInputStream("C:\\Users\\ZhouXunYou\\Desktop\\mo.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String moLine = null;
		Set<String> mos = new HashSet<>();
		while((moLine=br.readLine())!=null) {
			mos.add(moLine);
		}
		br.close();
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\ZhouXunYou\\Desktop\\data.txt")));
		for(int i=0;i<count;i++) {
			for(String mo:mos) {
				for(String instance:instances) {
					for(String kpiCode:kpiCodes) {
						int data = random.nextInt(100 - 0 + 1) + 0;
						String line = String.format("%s,%s,%s,%d,%d,LINUX,AGENT\n", mo,instance,kpiCode,startTime,data);
						bw.write(line);
					}
				}
			}
			startTime+=(1000*60*5);
		}
		bw.flush();
		bw.close();
		
	}
}
