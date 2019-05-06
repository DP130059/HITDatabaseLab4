package data;

import java.sql.SQLNonTransientConnectionException;
import java.util.Random;

public class Data {
	public byte[] getRdata() {
		byte[] result = new byte[896];
		int[] a = new int[112];
		int[] b = new int[112];
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 112; i++) {
			a[i] = new Random().nextInt(40);
			b[i] = new Random().nextInt(1000);
			sb.append("( " + a[i] + " , " + b[i] + " )\n");
		}
		System.out.println(sb.toString());
		int k = 0;
		for (int i = 0; i < 112; i++) {
			byte[] tmpbytes = int2byte(a[i]);
			for (int j = 0; j < tmpbytes.length; j++) {
				result[k] = tmpbytes[j];
				k += 1;
			}
			tmpbytes = int2byte(b[i]);
			for (int j = 0; j < tmpbytes.length; j++) {
				result[k] = tmpbytes[j];
				k += 1;
			}

		}
		return result;

	}

	public byte[] getSdata() {
		byte[] result = new byte[896 * 2];
		int[] a = new int[112 * 2];
		int[] b = new int[112 * 2];
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 112 * 2; i++) {
			int tmp = new Random().nextInt(60);
			while (tmp < 20) {
				tmp = new Random().nextInt(60);
			}
			a[i] = tmp;
			b[i] = new Random().nextInt(1000);
			sb.append("( " + a[i] + " , " + b[i] + " )\n");
		}
		System.out.println(sb.toString());
		int k = 0;
		for (int i = 0; i < 112 * 2; i++) {
			byte[] tmpbytes = int2byte(a[i]);
			for (int j = 0; j < tmpbytes.length; j++) {
				result[k] = tmpbytes[j];
				k += 1;
			}
			tmpbytes = int2byte(b[i]);
			for (int j = 0; j < tmpbytes.length; j++) {
				result[k] = tmpbytes[j];
				k += 1;
			}

		}
		return result;

	}

	public byte[] int2byte(int num) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) ((num >> 24) & 0xff);
		bytes[1] = (byte) ((num >> 16) & 0xff);
		bytes[2] = (byte) ((num >> 8) & 0xff);
		bytes[3] = (byte) (num & 0xff);
		return bytes;
	}

	public int byte2int(byte[] bytes) {
		return (bytes[0] & 0xff) << 24 | (bytes[1] & 0xff) << 16 | (bytes[2] & 0xff) << 8 | (bytes[3] & 0xff);
	}

	public void decode(byte[] bytes) {
		int[] a = new int[bytes.length / 8];
		int[] b = new int[bytes.length / 8];
		StringBuffer sb = new StringBuffer();
		int k=0;
		for (int i = 0; i < bytes.length / 8; i++) {
			byte[] tmp=new byte[4];
			for(int j=0;j<4;j++) {
				tmp[j]=bytes[k];
				k+=1;
			}
			a[i]=byte2int(tmp);
			for(int j=0;j<4;j++) {
				tmp[j]=bytes[k];
				k+=1;
			}
			b[i]=byte2int(tmp);
			sb.append("( " + a[i] + " , " + b[i] + " )\n");
		}
		System.out.println(sb.toString());
	}
	public static void main(String args[]) {
		Data data=new Data();
		byte[] rdata=data.getSdata();
		System.out.println("Decoding...");
		data.decode(rdata);
	}

}
