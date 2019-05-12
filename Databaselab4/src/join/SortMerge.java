package join;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.IntToDoubleFunction;

import org.omg.PortableInterceptor.INACTIVE;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import data.Data;
import extmem.Block;
import extmem.Buffer;

public class SortMerge {
	public static final int R_addr = 0x22222222;
	public static final int S_addr = 0x77777777;
	public static final int Result_addr = 0x55555555;
	public static Buffer buffer = new Buffer(512, 64);
	public static Multimap<Integer, Integer> R_index = ArrayListMultimap.create();
	public static Multimap<Integer, Integer> S_index = ArrayListMultimap.create();

	public static void createrindex() throws IOException {
		int addr = R_addr;
		// int resultaddr=R_result_addr;
		Data data = new Data();
		for (int m = 0; m < 2; m++) {
			addr = R_addr + 8 * m * 0x40;
			while (buffer.getNumfreeblk() != 0) {
				buffer.readblockfromdisk(addr);
				addr += 0x40;
			}
			addr = R_addr + 8 * m * 0x40;
			Block[] blocks = buffer.getData();
			for (int i = 0; i < 8; i++) {
				int n = 0;
				for (int j = 0; j < 7; j++) {
					byte tmp[] = new byte[4];
					for (int k = 0; k < 4; k++) {
						tmp[k] = blocks[i].getData()[n];
						n += 1;
					}
					n += 4;
					//System.out.println(data.byte2int(tmp));
					if (!R_index.containsEntry(data.byte2int(tmp), addr))
						R_index.put(data.byte2int(tmp), addr);
				}
				addr += 0x40;
			}
			buffer.free();
		}
	}

	public static void createsindex() throws IOException {
		int addr = S_addr;
		// int resultaddr=R_result_addr;
		Data data = new Data();
		for (int m = 0; m < 4; m++) {
			addr = S_addr + 8 * m * 0x40;
			while (buffer.getNumfreeblk() != 0) {
				buffer.readblockfromdisk(addr);
				addr += 0x40;
			}
			addr = S_addr + 8 * m * 0x40;
			Block[] blocks = buffer.getData();
			for (int i = 0; i < 8; i++) {
				int n = 0;
				for (int j = 0; j < 7; j++) {
					byte tmp[] = new byte[4];
					for (int k = 0; k < 4; k++) {
						tmp[k] = blocks[i].getData()[n];
						n += 1;
					}
					n += 4;
					//System.out.println(data.byte2int(tmp));
					if (!S_index.containsEntry(data.byte2int(tmp), addr))
						S_index.put(data.byte2int(tmp), addr);
				}
				addr += 0x40;
			}
			buffer.free();
		}

	}
	
	public static void join() throws IOException {
		Data data=new Data();
		int resultaddr=Result_addr;
		createrindex();
		buffer.free();
		createsindex();
		buffer.free();
		Set<Integer>rkeySet=R_index.keySet();
		Iterator<Integer>rIterator=rkeySet.iterator();
		int resultcnt=0;
		byte[] writedata=new byte[64];
		Block resultBlock=buffer.getNewBlockinBuffer();
		while (rIterator.hasNext()) {
			int rkey = (int) rIterator.next();
			if(!S_index.containsKey(rkey))
				continue;
			else {
				List<Integer>raddr=(List<Integer>) R_index.get(rkey);
				List<Integer>rbs=new ArrayList<>();
				for(int i=0;i<raddr.size();i++) {
					int addr=raddr.get(i);
					int rblkptr=buffer.readblockfromdisk(addr);
					List<Integer>tmp=selectsingleblock(buffer.getData()[rblkptr], rkey);		
					rbs.addAll(tmp);
					buffer.freeBlockinBuffer(rblkptr);
				}
				List<Integer>saddr=(List<Integer>) S_index.get(rkey);
				List<Integer>sds=new ArrayList<>();
				for(int j=0;j<saddr.size();j++) {
					int ad=saddr.get(j);
					int sblkptr=buffer.readblockfromdisk(ad);
					List<Integer>tmp=selectsingleblock(buffer.getData()[sblkptr], rkey);
					sds.addAll(tmp);
					buffer.freeBlockinBuffer(sblkptr);
				}
				for(int i=0;i<rbs.size();i++) {
					int rb=rbs.get(i);
					for(int j=0;j<sds.size();j++) {
						int sd=sds.get(j);
						System.out.println("( " + rkey + " , " + rb + " , " + sd + " )");
						byte []tmp = data.int2byte(rkey);
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
			}			
		}
	}
	public static List<Integer> selectsingleblock(Block block, int a) throws IOException {
		List<Integer> resultdata=new ArrayList<>();
		int ra[] = new int[7];
		int rb[] = new int[7];
		Data data = new Data();
		int m = 0;
		for (int i = 0; i < 7; i++) {
			byte[] tmp = new byte[4];
			for (int j = 0; j < 4; j++) {
				tmp[j] = block.getData()[m];
				m += 1;
			}
			ra[i] = data.byte2int(tmp);
			for (int j = 0; j < 4; j++) {
				tmp[j] = block.getData()[m];
				m += 1;
			}
			rb[i] = data.byte2int(tmp);
		}
		m = 0;
		
		for (int i = 0; i < 7; i++) {
			if (ra[i] == a) {
				resultdata.add(rb[i]);
			}
		}
		return resultdata;
	}

	public static void main(String[] args) {
		try {
			join();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

}
