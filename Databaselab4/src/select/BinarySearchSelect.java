package select;

import java.awt.RadialGradientPaint;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

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
				for(int k=0;k<6-j;k++) {
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
				for(int k=0;k<6-j;k++) {
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
		addr=R_addr;
		for(int m=0;m<4;m++) {
			while(buffer.getNumfreeblk()!=4) {
				buffer.readblockfromdisk(addr);
				addr=addr+0x40;
			}
			addr-=4*0x40;
			blocks=buffer.getData();
			Queue<Integer>[] aqueues=new Queue[4];
			Queue<Integer>[] bqueues=new Queue[4];
			for(int i=0;i<4;i++) {
				aqueues[i]=new LinkedList<>();
				bqueues[i]=new LinkedList<>();
				byte[] blockdata=blocks[i].getData();
				int n=0;
				for(int j=0;j<7;j++) {
					byte[] tmp = new byte[4];
					for (int k = 0; k < 4; k++) {
						tmp[k] = blockdata[n];
						n += 1;
					}
					int ra = data.byte2int(tmp);
					aqueues[i].offer(ra);
					for (int k = 0; k < 4; k++) {
						tmp[k] = blockdata[n];
						n += 1;
					}
					int rb = data.byte2int(tmp);
					bqueues[i].offer(rb);
				}
			}
			Block writeBlock[]=new Block[4];
			for(int j=0;j<4;j++) {
				writeBlock[j]=buffer.getNewBlockinBuffer();
			}
			List<Byte>tmpBytes=new ArrayList<Byte>();
			int f=0;
			while(true) {				
				int[] as=new int[4];
				int[] bs=new int [4];
				for(int j=0;j<4;j++) {
					//System.out.println(j);
					as[j]=aqueues[j].peek();
					bs[j]=bqueues[j].peek();
				}
				int amin=Integer.MAX_VALUE;
				int flag=0;
				for(int j=0;j<4;j++) {
					if(as[j]<amin)
					{
						amin=as[j];
						flag=j;
					}
				}
				int choosea=aqueues[flag].poll();
				//System.out.println(choosea);
				int chooseb=bqueues[flag].poll();
				aqueues[flag].offer(Integer.MAX_VALUE);
				bqueues[flag].offer(Integer.MAX_VALUE);
				if(choosea==Integer.MAX_VALUE) {
					break;
				}
				for(int j=0;j<4;j++) {
					tmpBytes.add(data.int2byte(choosea)[j]);					
				}
				for(int j=0;j<4;j++) {
					tmpBytes.add(data.int2byte(chooseb)[j]);					
				}
				if(tmpBytes.size()==56) {
					byte[] datatofill=new byte[64];
					for(int j=0;j<56;j++) {
						datatofill[j]=tmpBytes.get(j).byteValue();
					}
					for(int j=0;j<4;j++) {
						datatofill[56+j]=data.int2byte(0)[j];
					}
					//System.out.println(addr);
					for(int j=0;j<4;j++) {
						datatofill[60+j]=data.int2byte(addr)[j];
					}
					addr=addr+0x40;
					//System.out.println(f);
					writeBlock[f].setData(datatofill);
					f+=1;
					tmpBytes=new ArrayList<>();
					if(f==4) {
						f=0;
					}
				}
			}
			addr=R_addr+m*4*0x40;
			for(int j=4;j<8;j++) {
				//data.decode(buffer.getData()[j].getData());
				buffer.writeblocktodisk(j, addr);
				addr+=0x40;
			}
			buffer.free();
		}
		buffer.free();
		addr=R_addr;
		while(buffer.getNumfreeblk()!=4) {
			buffer.readblockfromdisk(addr);
			addr+=4*0x40;
		}
		blocks=buffer.getData();
	}
	public static void decodeoutput() throws IOException {
		int addr=R_addr;
		Data data=new Data();
		for(int i=0;i<16;i++) {
			List<String>singlefile=Files.readLines(new File("src/blocks/"+addr+".blk"), Charsets.UTF_8);
			byte[]tmp=new byte[64];
			for(int j=0;j<64;j++) {
				tmp[j]=Byte.parseByte(singlefile.get(j));
			}
			int n=0;
			for(int j=0;j<8;j++) {
				byte[] tmpa=new byte[4];
				for(int k=0;k<4;k++) {
					tmpa[k]=tmp[n];
					n+=1;
				}
				int ra=data.byte2int(tmpa);
				for(int k=0;k<4;k++) {
					tmpa[k]=tmp[n];
					n+=1;
				}
				int rb=data.byte2int(tmpa);
				System.out.println("( "+ra+" , "+rb+" )");
			}
			addr+=0x40;
		}
	}
	public static void main(String[] args) {
		try {
			sortr();
			decodeoutput();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

}
