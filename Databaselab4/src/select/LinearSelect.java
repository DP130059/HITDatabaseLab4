package select;

import java.io.IOException;

import data.Data;
import extmem.Block;
import extmem.Buffer;

public class LinearSelect {
	public static Buffer buffer = new Buffer(512, 64);
	public static final int R_addr = 0x11111111;
	public static final int S_addr = 0x33333333;
	public static void selectr(int a) throws IOException {
		StringBuffer sb=new StringBuffer();
		int addr=R_addr;
		while(buffer.getNumfreeblk()!=0) {
			buffer.readblockfromdisk(addr);
			addr=addr+0x40;
		}
		Block[] blocks=buffer.getData();
		Data data=new Data();
		for(int i=0;i<8;i++) {
			byte[] blockdata=blocks[i].getData();
			int m=0;
			for(int j=0;j<7;j++) {
				byte[] tmp=new byte[4];
				for(int k=0;k<4;k++) {
					tmp[k]=blockdata[m];
					m+=1;
				}
				int ra=data.byte2int(tmp);
				for(int k=0;k<4;k++) {
					tmp[k]=blockdata[m];
					m+=1;
				}
				int rb=data.byte2int(tmp);
				if(ra==a) {
					sb.append("( " + ra + " , " + rb + " )\n");
				}
			}
		}
		buffer.free();
		while(buffer.getNumfreeblk()!=0) {
			buffer.readblockfromdisk(addr);
			addr=addr+0x40;
		}
		blocks=buffer.getData();
		data=new Data();
		for(int i=0;i<8;i++) {
			byte[] blockdata=blocks[i].getData();
			int m=0;
			for(int j=0;j<7;j++) {
				byte[] tmp=new byte[4];
				for(int k=0;k<4;k++) {
					tmp[k]=blockdata[m];
					m+=1;
				}
				int ra=data.byte2int(tmp);
				for(int k=0;k<4;k++) {
					tmp[k]=blockdata[m];
					m+=1;
				}
				int rb=data.byte2int(tmp);
				if(ra==a) {
					sb.append("( " + ra + " , " + rb + " )\n");
				}
			}
		}
		System.out.println(sb.toString());
	}
	public static void selects(int c) throws IOException {
		StringBuffer sb=new StringBuffer();
		int addr=S_addr;
		System.out.println(buffer.getNumallblock());
		System.out.println(buffer.getNumfreeblk());
		buffer.free();
		for(int n=0;n<4;n++) {
			while(buffer.getNumfreeblk()!=0) {
				buffer.readblockfromdisk(addr);
				addr=addr+0x40;
			}
			Block[] blocks=buffer.getData();
			Data data=new Data();
			for(int i=0;i<8;i++) {
				byte[] blockdata=blocks[i].getData();
				int m=0;
				for(int j=0;j<7;j++) {
					byte[] tmp=new byte[4];
					for(int k=0;k<4;k++) {
						tmp[k]=blockdata[m];
						m+=1;
					}
					int ra=data.byte2int(tmp);
					for(int k=0;k<4;k++) {
						tmp[k]=blockdata[m];
						m+=1;
					}
					int rb=data.byte2int(tmp);
					if(ra==c) {
						sb.append("( " + ra + " , " + rb + " )\n");
					}
				}
			}
			buffer.free();
		}
		System.out.println(sb.toString());
	}
	public static void main(String args[]) {
		try {
			selects(24);
			System.out.println(buffer.getNumIO());
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
