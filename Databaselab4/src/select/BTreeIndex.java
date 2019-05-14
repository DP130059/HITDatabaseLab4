package select;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import btree.BTreeNode;
import data.Data;
import extmem.Block;
import extmem.Buffer;

public class BTreeIndex {
	private static BTreeNode Rtree=new BTreeNode(3);
	private static BTreeNode Stree=new BTreeNode(3);
	public static final int R_addr = 0x22222222;
	public static final int S_addr = 0x77777777;
	public static final int R_result_addr = 0x55555555;
	public static final int S_result_addr = 0x33333333;
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
	public static void createRtree() {
		Set<Integer>keyset=R_index.keySet();
		//System.out.println(keyset.size());
		Iterator<Integer>keyIterator=keyset.iterator();
		while (keyIterator.hasNext()) {
			Integer integer = (Integer) keyIterator.next();
			Rtree=Rtree.insert(integer, (List<Integer>)R_index.get(integer));
		}
	}
	public static void createStree() {
		Set<Integer>keyset=S_index.keySet();
		Iterator<Integer>keyIterator=keyset.iterator();
		while (keyIterator.hasNext()) {
			Integer integer = (Integer) keyIterator.next();
			Rtree.insert(integer, (List<Integer>)R_index.get(integer));
		}
	}
	public static void selectsingleblock(Block block, int a,int Result_addr) throws IOException {
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
		Block tmpBlock = buffer.getNewBlockinBuffer();
		byte[] result = new byte[64];
		m = 0;
		boolean flag = false;
		for (int i = 0; i < 7; i++) {
			if (ra[i] == a) {
				flag = true;
				System.out.println("( " + ra[i] + " , " + rb[i] + " )");
				byte[] tmp = data.int2byte(ra[i]);
				for (int j = 0; j < 4; j++) {
					result[m] = tmp[j];
					m += 1;
				}
				tmp = data.int2byte(rb[i]);
				for (int j = 0; j < 4; j++) {
					result[m] = tmp[j];
					m += 1;
				}
			}
		}
		if (!flag) {
			System.out.println("The element doesn't exist in database!");
		} else {
			tmpBlock.setData(result);
			buffer.writeblocktodisk(1, Result_addr);
		}
	}
	public static void selectr(int a) throws IOException {
		buffer.free();
		List<Integer>addrList=(List<Integer>) Rtree.search(a).addrs.get(a);
		//System.out.println(addrList.size());
		for(int i=0;i<addrList.size();i++) {
			int addr=addrList.get(i);
			int blkptr=buffer.readblockfromdisk(addr);
			Block block=buffer.getData()[blkptr];
			selectsingleblock(block, a, R_result_addr);
			buffer.freeBlockinBuffer(blkptr);
		}
	}
	public static void selects(int a) throws IOException {
		buffer.free();
		List<Integer>addrList=(List<Integer>) Stree.search(a).addrs.get(a);
		//System.out.println(addrList.size());
		for(int i=0;i<addrList.size();i++) {
			int addr=addrList.get(i);
			int blkptr=buffer.readblockfromdisk(addr);
			Block block=buffer.getData()[blkptr];
			selectsingleblock(block, a, R_result_addr);
			buffer.freeBlockinBuffer(blkptr);
		}
	}
	public static void main(String args[]) {
		try {
			createsindex();
			buffer.free();
			createrindex();
			buffer.free();
			createRtree();
			buffer.free();
			createStree();
			selectr(16);
			selects(59);
			//showindex();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	

}
