package select;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import data.Data;
import extmem.Block;
import extmem.Buffer;

public class LinearSelect {
	public static Buffer buffer = new Buffer(512, 64);
	public static final int R_addr = 0x11111111;
	public static final int S_addr = 0x33333333;
	public static final int Result_addr = 0x55555555;

	public static void selectr(int a) throws IOException {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		List<String>results=new ArrayList<>();
		int addr = R_addr;
		int resultaddr = Result_addr;
		for (int n = 0; n < 2; n++) {
			while (buffer.getNumfreeblk() != 0) {
				buffer.readblockfromdisk(addr);
				addr = addr + 0x40;
			}
			Block[] blocks = buffer.getData();
			Data data = new Data();
			for (int i = 0; i < 8; i++) {
				byte[] blockdata = blocks[i].getData();
				int m = 0;
				for (int j = 0; j < 7; j++) {
					sb2=new StringBuffer();
					byte[] tmp = new byte[4];
					for (int k = 0; k < 4; k++) {
						tmp[k] = blockdata[m];
						m += 1;
					}
					int ra = data.byte2int(tmp);
					for (int k = 0; k < 4; k++) {
						tmp[k] = blockdata[m];
						m += 1;
					}
					int rb = data.byte2int(tmp);
					if (ra == a) {
						byte[] write = data.int2byte(ra);
						for (int k = 0; k < 4; k++) {
							results.add(write[k]+"\n");
						}
						write = data.int2byte(rb);
						for (int k = 0; k < 4; k++) {
							results.add(write[k]+"\n");
						}
						sb.append("( " + ra + " , " + rb + " )\n");
					}
					
				}
			}
			buffer.free();
		}
		Data data =new Data();
		sb2=new StringBuffer();
		List<String>cpresult=new ArrayList<>();
		cpresult.addAll(results);
		Iterator<String>iterator=cpresult.iterator();
		int size=results.size();
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			if(size>=7*24) {
				for(int i=0;i<7;i++) {
					sb2.append(string);
					iterator.remove();
					size-=1;
				}
				byte[] write = data.int2byte(0);
				for (int k = 0; k < 4; k++) {
					sb2.append(write[k]);
					sb2.append("\n");
				}
				resultaddr += 0x40;
				write = data.int2byte(resultaddr);
				for (int k = 0; k < 4; k++) {
					sb2.append(write[k]);
					sb2.append("\n");
				}
				Files.write(sb2, new File("src/blocks/" + (resultaddr - 0x40) + ".blk"), Charsets.UTF_8);
				sb2=new StringBuffer();
			}else {
				sb2.append(string);
			}
		}
		Files.write(sb2, new File("src/blocks/" + (resultaddr) + ".blk"), Charsets.UTF_8);
		System.out.println(sb.toString());
	}

	public static void selects(int c) throws IOException {
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		List<String>results=new ArrayList<>();
		int addr = S_addr;
		int resultaddr = Result_addr;
		System.out.println(buffer.getNumallblock());
		System.out.println(buffer.getNumfreeblk());
		buffer.free();
		for (int n = 0; n < 4; n++) {
			while (buffer.getNumfreeblk() != 0) {
				buffer.readblockfromdisk(addr);
				addr = addr + 0x40;
			}
			Block[] blocks = buffer.getData();
			Data data = new Data();
			for (int i = 0; i < 8; i++) {
				byte[] blockdata = blocks[i].getData();
				int m = 0;
				for (int j = 0; j < 7; j++) {
					byte[] tmp = new byte[4];
					for (int k = 0; k < 4; k++) {
						tmp[k] = blockdata[m];
						m += 1;
					}
					int ra = data.byte2int(tmp);
					for (int k = 0; k < 4; k++) {
						tmp[k] = blockdata[m];
						m += 1;
					}
					int rb = data.byte2int(tmp);
					if (ra == c) {
						byte[] write = data.int2byte(ra);
						for (int k = 0; k < 4; k++) {
							results.add(write[k]+"\n");
						}
						write = data.int2byte(rb);
						for (int k = 0; k < 4; k++) {
							results.add(write[k]+"\n");
						}
						sb.append("( " + ra + " , " + rb + " )\n");
					}
				}
			}
			buffer.free();
		}
		Data data =new Data();
		sb2=new StringBuffer();
		List<String>cpresult=new ArrayList<>();
		cpresult.addAll(results);
		Iterator<String>iterator=cpresult.iterator();
		int size=results.size();
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			if(size>=7*24) {
				for(int i=0;i<7;i++) {
					sb2.append(string);
					iterator.remove();
					size-=1;
				}
				byte[] write = data.int2byte(0);
				for (int k = 0; k < 4; k++) {
					sb2.append(write[k]);
					sb2.append("\n");
				}
				resultaddr += 0x40;
				write = data.int2byte(resultaddr);
				for (int k = 0; k < 4; k++) {
					sb2.append(write[k]);
					sb2.append("\n");
				}
				Files.write(sb2, new File("src/blocks/" + (resultaddr - 0x40) + ".blk"), Charsets.UTF_8);
				sb2=new StringBuffer();
			}else {
				sb2.append(string);
			}
		}
		Files.write(sb2, new File("src/blocks/" + (resultaddr) + ".blk"), Charsets.UTF_8);
		System.out.println(sb.toString());
	}

	public static void main(String args[]) {
		try {
			selectr(24);
			System.out.println(buffer.getNumIO());
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
