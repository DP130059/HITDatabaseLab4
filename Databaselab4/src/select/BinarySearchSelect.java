package select;

import java.awt.RadialGradientPaint;
import java.io.IOException;

import data.Data;
import extmem.Block;
import extmem.Buffer;

public class BinarySearchSelect {
	public static Buffer buffer = new Buffer(512, 64);
	public static final int R_addr = 0x11111111;
	public static final int S_addr = 0x33333333;

	public static void selectr(int a) {

	}
	public static void sortr() throws IOException {
		int addr = R_addr;
		while (buffer.getNumfreeblk() != 0) {
			buffer.readblockfromdisk(addr);
			addr = addr + 0x40;
		}
		Block[] blocks = buffer.getData();
		Data data = new Data();
		addr=R_addr;
		for (int i = 0; i < 8; i++) {			
			byte[] blockdata = blocks[i].getData();
			int m = 0;
			int [] as=new int[7];
			int [] bs=new int[7];
			for (int j = 0; j < 7; j++) {
				byte[] tmp = new byte[4];
				for (int k = 0; k < 4; k++) {
					tmp[k] = blockdata[m];
					m += 1;
				}
				int ra = data.byte2int(tmp);
				as[j]=ra;
				for (int k = 0; k < 4; k++) {
					tmp[k] = blockdata[m];
					m += 1;
				}
				int rb = data.byte2int(tmp);
				bs[j]=rb;
			}
			for(int j=0;j<7;j++) {
				for(int k=0;k<7-i;k++) {
					if(as[k]>as[k+1]) {
						int tmpa=as[k];
						as[k]=as[k+1];
						as[k+1]=tmpa;
						int tmpb=bs[k];
						bs[k]=bs[k+1];
						bs[k+1]=tmpb;
					}
				}
			}
			int n=0;
			for(int j=0;j<7;j++) {
				byte[] write=data.int2byte(as[j]);
				for(int k=0;k<4;k++) {
					blockdata[n]=write[k];
					n+=1;
				}
				write=data.int2byte(bs[j]);
				for(int k=0;k<4;k++) {
					blockdata[n]=write[k];
					n+=1;
				}
			}
			byte[] write=data.int2byte(0);
			for(int k=0;k<4;k++) {
				blockdata[n]=write[k];
				n+=1;
			}
			write=data.int2byte(addr+0x40);
			for(int k=0;k<4;k++) {
				blockdata[n]=write[k];
				n+=1;
			}
			blocks[i].setData(blockdata);
			buffer.writeblocktodisk(i, addr);
			addr+=0x40;
		}
		buffer.free();
		while (buffer.getNumfreeblk() != 0) {
			buffer.readblockfromdisk(addr);
			addr = addr + 0x40;
		}
		blocks = buffer.getData();
		data = new Data();
		addr=R_addr+0x40*8;
		for (int i = 0; i < 8; i++) {			
			byte[] blockdata = blocks[i].getData();
			int m = 0;
			int [] as=new int[7];
			int [] bs=new int[7];
			for (int j = 0; j < 7; j++) {
				byte[] tmp = new byte[4];
				for (int k = 0; k < 4; k++) {
					tmp[k] = blockdata[m];
					m += 1;
				}
				int ra = data.byte2int(tmp);
				as[j]=ra;
				for (int k = 0; k < 4; k++) {
					tmp[k] = blockdata[m];
					m += 1;
				}
				int rb = data.byte2int(tmp);
				bs[j]=rb;
			}
			for(int j=0;j<7;j++) {
				for(int k=0;k<7-i;k++) {
					if(as[k]>as[k+1]) {
						int tmpa=as[k];
						as[k]=as[k+1];
						as[k+1]=tmpa;
						int tmpb=bs[k];
						bs[k]=bs[k+1];
						bs[k+1]=tmpb;
					}
				}
			}
			int n=0;
			for(int j=0;j<7;j++) {
				byte[] write=data.int2byte(as[j]);
				for(int k=0;k<4;k++) {
					blockdata[n]=write[k];
					n+=1;
				}
				write=data.int2byte(bs[j]);
				for(int k=0;k<4;k++) {
					blockdata[n]=write[k];
					n+=1;
				}
			}
			byte[] write=data.int2byte(0);
			for(int k=0;k<4;k++) {
				blockdata[n]=write[k];
				n+=1;
			}
			write=data.int2byte(addr+0x40);
			for(int k=0;k<4;k++) {
				blockdata[n]=write[k];
				n+=1;
			}
			blocks[i].setData(blockdata);
			buffer.writeblocktodisk(i, addr);
			addr+=0x40;
		}
		
	}

	public static void main(String[] args) {
		// TODO 自动生成的方法存根

	}

}
