package extmem;

import java.io.IOException;

import data.Data;

public class Test {
	public static void main(String args[]) {
		Buffer buffer=new Buffer(128,32);
		Block block=buffer.getNewBlockinBuffer();
		byte[] databyte=new byte[32];
		int k=0;
		for(int i=0;i<8;i++) {
			byte[] tmp=new Data().int2byte(i);
			for(int j=0;j<4;j++) {
				databyte[k]=tmp[j];
				k+=1;
			}
		}
		/*for(int i=0;i<32;i++) {
			System.out.println(databyte[i]);
		}*/
		block.setData(databyte);
		
		try {
			buffer.writeblocktodisk(0, 0x55555555);
			int blkptr=buffer.readblockfromdisk(0x55555555);
			databyte=buffer.getData()[blkptr].getData();
			k=0;
			for(int i=0;i<8;i++) {
				byte[] tmp=new byte[4];
				for(int j=0;j<4;j++) {
					tmp[j]=databyte[k];
					k=k+1;
				}
				System.out.println(new Data().byte2int(tmp));
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}
