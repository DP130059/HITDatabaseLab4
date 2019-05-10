package projection;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import data.Data;
import extmem.Block;
import extmem.Buffer;

public class Projection {
	public static final int R_addr=0x22222222;
	public static final int S_addr=0x77777777;
	public static final int R_result_addr=0x11111111;
	public static final int S_result_addr=0x33333333;
	public static Buffer buffer=new Buffer(512, 64);
	public static void R_projection(boolean flag) throws IOException {
		int addr=R_addr;
		int resultaddr=R_result_addr;
		Data data=new Data();
		for(int m=0;m<4;m++) {
			while(buffer.getNumfreeblk()!=4) {
				buffer.readblockfromdisk(addr);
				addr+=0x40;
			}
			//addr=R_addr+m*4*0x40;
			Block[] blocks=buffer.getData();
			for(int i=0;i<2;i++) {
				byte[] result=new byte[64];
				int n=0;
				for(int j=i*2;j<i*2+2;j++) {
					byte[] singleblock=blocks[j].getData();
					int k=0;
					if(flag)
						k=0;
					else {
						k=4;
					}
					for(int h=0;h<7;h++) {
						for(int l=0;l<4;l++) {
							//System.out.println(n+" , "+k);
							result[n]=singleblock[k];
							k+=1;
							n+=1;
						}
						k+=4;						
					}
				}
				Block writeBlock=buffer.getNewBlockinBuffer();
				writeBlock.setData(result);
			}
			for(int i=0;i<2;i++) {
				buffer.writeblocktodisk(i+4, resultaddr);
				resultaddr+=0x40;
			}
			buffer.free();
		}
	}
	public static void S_projection(boolean flag) throws IOException {
		int addr=S_addr;
		int resultaddr=S_result_addr;
		Data data=new Data();
		for(int m=0;m<8;m++) {
			while(buffer.getNumfreeblk()!=4) {
				buffer.readblockfromdisk(addr);
				addr+=0x40;
			}
			//addr=R_addr+m*4*0x40;
			Block[] blocks=buffer.getData();
			for(int i=0;i<2;i++) {
				byte[] result=new byte[64];
				int n=0;
				for(int j=i*2;j<i*2+2;j++) {
					byte[] singleblock=blocks[j].getData();
					int k=0;
					if(flag)
						k=0;
					else {
						k=4;
					}
					for(int h=0;h<7;h++) {
						for(int l=0;l<4;l++) {
							//System.out.println(n+" , "+k);
							result[n]=singleblock[k];
							k+=1;
							n+=1;
						}
						k+=4;						
					}
				}
				Block writeBlock=buffer.getNewBlockinBuffer();
				writeBlock.setData(result);
			}
			for(int i=0;i<2;i++) {
				buffer.writeblocktodisk(i+4, resultaddr);
				resultaddr+=0x40;
			}
			buffer.free();
		}
	}
	public static void decodeoutput() throws IOException {
		int addr = S_result_addr;
		Data data = new Data();
		for (int i = 0; i < 16; i++) {
			System.out.println((i+1)+".");
			List<String> singlefile = Files.readLines(new File("src/blocks/" + addr + ".blk"), Charsets.UTF_8);
			byte[] tmp = new byte[64];
			for (int j = 0; j < 56; j++) {
				tmp[j] = Byte.parseByte(singlefile.get(j));
			}
			int n = 0;
			for (int j = 0; j < 14 ;j++) {
				byte[] tmpa = new byte[4];
				for (int k = 0; k < 4; k++) {
					tmpa[k] = tmp[n];
					n += 1;
				}
				int ra = data.byte2int(tmpa);
				System.out.println("( " + ra +  " )");
			}
			addr += 0x40;			
			
		}
	}
	public static void main(String args[]) {
		try {
			S_projection(true);
			decodeoutput();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}
