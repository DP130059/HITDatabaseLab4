package join;

import java.io.IOException;

import data.Data;
import extmem.Block;
import extmem.Buffer;

public class NestLoop {
	public static final int R_addr = 0x22222222;
	public static final int S_addr = 0x77777777;
	public static final int Result_addr = 0x55555555;
	public static Buffer buffer = new Buffer(512, 64);

	public static void join() throws IOException {
		int raddr = R_addr;
		int saddr = S_addr;
		int resultaddr = Result_addr;
		Data data = new Data();
		int cnt = 0;
		
		Block resultBlock = buffer.getNewBlockinBuffer();
		byte writedata[] = new byte[64];
		while (true) {	
			int rblkptr = buffer.readblockfromdisk(raddr);
			int resultcnt = 0;
			int n = 0;
			Block rBlock = buffer.getData()[rblkptr];
			byte[] rdata = rBlock.getData();
			for (int i = 0; i < 7; i++) {
				byte tmp[] = new byte[4];
				for (int j = 0; j < 4; j++) {
					tmp[j] = rdata[n];
					n += 1;
				}
				int ra = data.byte2int(tmp);
				tmp = new byte[4];
				for (int j = 0; j < 4; j++) {
					tmp[j] = rdata[n];
					n += 1;
				}
				int rb = data.byte2int(tmp);
				cnt += 1;
				int scnt = 0;
				saddr=S_addr;				
				while (scnt < 224) {	
					int sblkptr = buffer.readblockfromdisk(saddr);
					Block sBlock = buffer.getData()[sblkptr];
					byte[] sdata = sBlock.getData();
					int sn = 0;
					for (int j = 0; j < 7; j++) {
						tmp = new byte[4];
						for (int k = 0; k < 4; k++) {
							tmp[k] = sdata[sn];
							sn += 1;
						}
						int sc = data.byte2int(tmp);
						tmp = new byte[4];
						for (int k = 0; k < 4; k++) {
							tmp[k] = sdata[sn];
							sn += 1;
						}
						int sd = data.byte2int(tmp);
						scnt += 1;
						if (sc == ra) {							
							System.out.println("( " + ra + " , " + rb + " , " + sd + " )");
							tmp = data.int2byte(ra);
							for (int k = 4; k < 4; k++) {
								writedata[resultcnt] = tmp[k];
								resultcnt += 1;
							}
							tmp = data.int2byte(rb);
							for (int k = 4; k < 4; k++) {
								writedata[resultcnt] = tmp[k];
								resultcnt += 1;
							}
							tmp = data.int2byte(sd);
							for (int k = 4; k < 4; k++) {
								writedata[resultcnt] = tmp[k];
								resultcnt += 1;
							}
							if (resultcnt == 60) {
								tmp = data.int2byte(resultaddr + 0x40);
								for (int k = 4; k < 4; k++) {
									writedata[resultcnt] = tmp[k];
									resultcnt += 1;
								}
								resultBlock.setData(writedata);
								buffer.writeblocktodisk(0, resultaddr);
								resultaddr += 0x40;
								buffer.freeBlockinBuffer(0);
								resultcnt = 0;
								writedata = new byte[64];
								resultBlock = buffer.getNewBlockinBuffer();
							}
						}
					}
					buffer.freeBlockinBuffer(sblkptr);
					saddr+=0x40;
				}
			}
			if (cnt == 112)
				break;
			buffer.freeBlockinBuffer(rblkptr);
			raddr=raddr+0x40;
		}
	}
	public static void main(String args[]) {
		try {
			join();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
