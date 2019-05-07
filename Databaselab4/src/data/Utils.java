package data;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import extmem.Buffer;

public class Utils {
	public static final int R_addr=0x11111111;
	public static final int S_addr=0x33333333;
	public static void fillindata() throws IOException {
		Data data=new Data();
		byte[] rdata=data.getRdata();
		byte[] sdata=data.getSdata();
		int addr=R_addr;
		int k=0;
		for(int i=0;i<16;i++) {			
			File blkFile=new File("src/blocks/"+addr+".blk");
			StringBuffer sb=new StringBuffer();
			for(int j=0;j<56;j++) {
				sb.append(rdata[k]);
				k=k+1;
				sb.append("\n");
			}
			for(int j=0;j<4;j++) {
				sb.append(data.int2byte(0)[j]+"\n");
			}
			for(int j=0;j<4;j++) {
				sb.append(data.int2byte(addr)[j]+"\n");
			}
			Files.write(sb, blkFile, Charsets.UTF_8);
			addr+=0x40;
		}
		addr=S_addr;
		k=0;
		for(int i=0;i<32;i++) {
			File blkFile=new File("src/blocks/"+addr+".blk");
			StringBuffer sb=new StringBuffer();
			for(int j=0;j<56;j++) {
				sb.append(sdata[k]);
				k=k+1;
				sb.append("\n");
			}
			for(int j=0;j<4;j++) {
				sb.append(data.int2byte(0)[j]+"\n");
			}
			for(int j=0;j<4;j++) {
				sb.append(data.int2byte(addr)[j]+"\n");
			}
			Files.write(sb, blkFile, Charsets.UTF_8);
			addr+=0x40;
		}
	}
	public static void main(String args[]) {
		try {
			fillindata();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}
