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
	public static final int Result_addr = 0x55555555;

	public static void selectr(int a) throws IOException {
		Data data = new Data();
		int addr = 0x22222222;
		int blkptr = buffer.readblockfromdisk(addr + 8 * 0x40);
		byte tmp[] = new byte[4];
		for (int i = 0; i < 4; i++) {
			tmp[i] = buffer.getData()[blkptr].getData()[i];
		}
		int ra = data.byte2int(tmp);
		buffer.freeBlockinBuffer(blkptr);
		if (a > ra) {
			blkptr = buffer.readblockfromdisk(addr + 12 * 0x40);
			for (int i = 0; i < 4; i++) {
				tmp[i] = buffer.getData()[blkptr].getData()[i];
			}
			ra = data.byte2int(tmp);
			buffer.freeBlockinBuffer(blkptr);
			if (a > ra) {
				blkptr = buffer.readblockfromdisk(addr + 14 * 0x40);
				for (int i = 0; i < 4; i++) {
					tmp[i] = buffer.getData()[blkptr].getData()[i];
				}
				ra = data.byte2int(tmp);
				buffer.freeBlockinBuffer(blkptr);
				if (a > ra) {
					blkptr = buffer.readblockfromdisk(addr + 15 * 0x40);
					selectsingleblock(buffer.getData()[blkptr], a);
				} else if (a == ra) {
					blkptr = buffer.readblockfromdisk(addr + 14 * 0x40);
					selectsingleblock(buffer.getData()[blkptr], a);
				} else if (a < ra) {
					blkptr = buffer.readblockfromdisk(addr + 13 * 0x40);
					for (int i = 0; i < 4; i++) {
						tmp[i] = buffer.getData()[blkptr].getData()[i];
					}
					ra = data.byte2int(tmp);
					buffer.freeBlockinBuffer(blkptr);
					if (a >= ra) {
						blkptr = buffer.readblockfromdisk(addr + 13 * 0x40);
						selectsingleblock(buffer.getData()[blkptr], a);
					} else {
						blkptr = buffer.readblockfromdisk(addr + 12 * 0x40);
						selectsingleblock(buffer.getData()[blkptr], a);
					}
				}
			} else if (a == ra) {
				blkptr = buffer.readblockfromdisk(addr + 12 * 0x40);
				selectsingleblock(buffer.getData()[blkptr], a);
			} else if (a < ra) {
				blkptr = buffer.readblockfromdisk(addr + 10 * 0x40);
				for (int i = 0; i < 4; i++) {
					tmp[i] = buffer.getData()[blkptr].getData()[i];
				}
				ra = data.byte2int(tmp);
				buffer.freeBlockinBuffer(blkptr);
				if (a > ra) {
					blkptr = buffer.readblockfromdisk(addr + 11 * 0x40);
					for (int i = 0; i < 4; i++) {
						tmp[i] = buffer.getData()[blkptr].getData()[i];
					}
					ra = data.byte2int(tmp);
					buffer.freeBlockinBuffer(blkptr);
					if (a >= ra) {
						blkptr = buffer.readblockfromdisk(addr + 11 * 0x40);
						selectsingleblock(buffer.getData()[blkptr], a);
					} else {
						blkptr = buffer.readblockfromdisk(addr + 10 * 0x40);
						selectsingleblock(buffer.getData()[blkptr], a);
					}
				} else if (a == ra) {
					blkptr = buffer.readblockfromdisk(addr + 10 * 0x40);
					selectsingleblock(buffer.getData()[blkptr], a);
				} else if (a < ra) {
					blkptr = buffer.readblockfromdisk(addr + 9 * 0x40);
					for (int i = 0; i < 4; i++) {
						tmp[i] = buffer.getData()[blkptr].getData()[i];
					}
					ra = data.byte2int(tmp);
					buffer.freeBlockinBuffer(blkptr);
					if (a >= ra) {
						blkptr = buffer.readblockfromdisk(addr + 9 * 0x40);
						selectsingleblock(buffer.getData()[blkptr], a);
					} else {
						blkptr = buffer.readblockfromdisk(addr + 8 * 0x40);
						selectsingleblock(buffer.getData()[blkptr], a);
					}
				}
			}
		} else if (a == ra) {
			blkptr = buffer.readblockfromdisk(addr + 8 * 0x40);
			selectsingleblock(buffer.getData()[blkptr], a);
		} else if (a < ra) {
			blkptr = buffer.readblockfromdisk(addr + 4 * 0x40);
			for (int i = 0; i < 4; i++) {
				tmp[i] = buffer.getData()[blkptr].getData()[i];
			}
			ra = data.byte2int(tmp);
			buffer.freeBlockinBuffer(blkptr);
			if (a > ra) {
				blkptr = buffer.readblockfromdisk(addr + 6 * 0x40);
				for (int i = 0; i < 4; i++) {
					tmp[i] = buffer.getData()[blkptr].getData()[i];
				}
				ra = data.byte2int(tmp);
				buffer.freeBlockinBuffer(blkptr);
				if (a > ra) {
					blkptr = buffer.readblockfromdisk(addr + 7 * 0x40);
					for (int i = 0; i < 4; i++) {
						tmp[i] = buffer.getData()[blkptr].getData()[i];
					}
					ra = data.byte2int(tmp);
					buffer.freeBlockinBuffer(blkptr);
					if (a >= ra) {
						blkptr = buffer.readblockfromdisk(addr + 7 * 0x40);
						selectsingleblock(buffer.getData()[blkptr], a);
					} else {
						blkptr = buffer.readblockfromdisk(addr + 6 * 0x40);
						selectsingleblock(buffer.getData()[blkptr], a);
					}
				} else if (a == ra) {
					blkptr = buffer.readblockfromdisk(addr + 6 * 0x40);
					selectsingleblock(buffer.getData()[blkptr], a);
				} else if (a < ra) {
					blkptr = buffer.readblockfromdisk(addr + 5 * 0x40);
					for (int i = 0; i < 4; i++) {
						tmp[i] = buffer.getData()[blkptr].getData()[i];
					}
					ra = data.byte2int(tmp);
					buffer.freeBlockinBuffer(blkptr);
					if (a >= ra) {
						blkptr = buffer.readblockfromdisk(addr + 5 * 0x40);
						selectsingleblock(buffer.getData()[blkptr], a);
					} else {
						blkptr = buffer.readblockfromdisk(addr + 4 * 0x40);
						selectsingleblock(buffer.getData()[blkptr], a);
					}
				}
			} else if (a == ra) {
				blkptr = buffer.readblockfromdisk(addr + 4 * 0x40);
				selectsingleblock(buffer.getData()[blkptr], a);
			} else if (a < ra) {
				blkptr = buffer.readblockfromdisk(addr + 2 * 0x40);
				for (int i = 0; i < 4; i++) {
					tmp[i] = buffer.getData()[blkptr].getData()[i];
				}
				ra = data.byte2int(tmp);
				buffer.freeBlockinBuffer(blkptr);
				if (a > ra) {
					blkptr = buffer.readblockfromdisk(addr + 3 * 0x40);
					for (int i = 0; i < 4; i++) {
						tmp[i] = buffer.getData()[blkptr].getData()[i];
					}
					ra = data.byte2int(tmp);
					buffer.freeBlockinBuffer(blkptr);
					if (a >= ra) {
						blkptr = buffer.readblockfromdisk(addr + 3 * 0x40);
						selectsingleblock(buffer.getData()[blkptr], a);
					} else {
						blkptr = buffer.readblockfromdisk(addr + 2 * 0x40);
						selectsingleblock(buffer.getData()[blkptr], a);
					}
				} else if (a == ra) {
					blkptr = buffer.readblockfromdisk(addr + 2 * 0x40);
					selectsingleblock(buffer.getData()[blkptr], a);
				} else if (a < ra) {
					blkptr = buffer.readblockfromdisk(addr + 0x40);
					for (int i = 0; i < 4; i++) {
						tmp[i] = buffer.getData()[blkptr].getData()[i];
					}
					ra = data.byte2int(tmp);
					buffer.freeBlockinBuffer(blkptr);
					if (a >= ra) {
						blkptr = buffer.readblockfromdisk(addr + 0x40);
						selectsingleblock(buffer.getData()[blkptr], a);
					} else {
						blkptr = buffer.readblockfromdisk(addr);
						selectsingleblock(buffer.getData()[blkptr], a);
					}
				}
			}
		}
	}

	public static void selectsingleblock(Block block, int a) throws IOException {
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

	public static void sortr() throws IOException {
		int addr = R_addr;
		while (buffer.getNumfreeblk() != 0) {
			buffer.readblockfromdisk(addr);
			addr = addr + 0x40;
		}
		Block[] blocks = buffer.getData();
		Data data = new Data();
		addr = R_addr;
		for (int i = 0; i < 8; i++) {
			byte[] blockdata = blocks[i].getData();
			int m = 0;
			int[] as = new int[7];
			int[] bs = new int[7];
			for (int j = 0; j < 7; j++) {
				byte[] tmp = new byte[4];
				for (int k = 0; k < 4; k++) {
					tmp[k] = blockdata[m];
					m += 1;
				}
				int ra = data.byte2int(tmp);
				as[j] = ra;
				for (int k = 0; k < 4; k++) {
					tmp[k] = blockdata[m];
					m += 1;
				}
				int rb = data.byte2int(tmp);
				bs[j] = rb;
			}
			for (int j = 0; j < 7; j++) {
				for (int k = 0; k < 6 - j; k++) {
					if (as[k] > as[k + 1]) {
						int tmpa = as[k];
						as[k] = as[k + 1];
						as[k + 1] = tmpa;
						int tmpb = bs[k];
						bs[k] = bs[k + 1];
						bs[k + 1] = tmpb;
					}
				}
			}
			int n = 0;
			for (int j = 0; j < 7; j++) {
				byte[] write = data.int2byte(as[j]);
				for (int k = 0; k < 4; k++) {
					blockdata[n] = write[k];
					n += 1;
				}
				write = data.int2byte(bs[j]);
				for (int k = 0; k < 4; k++) {
					blockdata[n] = write[k];
					n += 1;
				}
			}
			byte[] write = data.int2byte(0);
			for (int k = 0; k < 4; k++) {
				blockdata[n] = write[k];
				n += 1;
			}
			write = data.int2byte(addr + 0x40);
			for (int k = 0; k < 4; k++) {
				blockdata[n] = write[k];
				n += 1;
			}
			blocks[i].setData(blockdata);
			buffer.writeblocktodisk(i, addr);
			addr += 0x40;
		}
		buffer.free();
		while (buffer.getNumfreeblk() != 0) {
			buffer.readblockfromdisk(addr);
			addr = addr + 0x40;
		}
		blocks = buffer.getData();
		data = new Data();
		addr = R_addr + 0x40 * 8;
		for (int i = 0; i < 8; i++) {
			byte[] blockdata = blocks[i].getData();
			int m = 0;
			int[] as = new int[7];
			int[] bs = new int[7];
			for (int j = 0; j < 7; j++) {
				byte[] tmp = new byte[4];
				for (int k = 0; k < 4; k++) {
					tmp[k] = blockdata[m];
					m += 1;
				}
				int ra = data.byte2int(tmp);
				as[j] = ra;
				for (int k = 0; k < 4; k++) {
					tmp[k] = blockdata[m];
					m += 1;
				}
				int rb = data.byte2int(tmp);
				bs[j] = rb;
			}
			for (int j = 0; j < 7; j++) {
				for (int k = 0; k < 6 - j; k++) {
					if (as[k] > as[k + 1]) {
						int tmpa = as[k];
						as[k] = as[k + 1];
						as[k + 1] = tmpa;
						int tmpb = bs[k];
						bs[k] = bs[k + 1];
						bs[k + 1] = tmpb;
					}
				}
			}
			int n = 0;
			for (int j = 0; j < 7; j++) {
				byte[] write = data.int2byte(as[j]);
				for (int k = 0; k < 4; k++) {
					blockdata[n] = write[k];
					n += 1;
				}
				write = data.int2byte(bs[j]);
				for (int k = 0; k < 4; k++) {
					blockdata[n] = write[k];
					n += 1;
				}
			}
			byte[] write = data.int2byte(0);
			for (int k = 0; k < 4; k++) {
				blockdata[n] = write[k];
				n += 1;
			}
			write = data.int2byte(addr + 0x40);
			for (int k = 0; k < 4; k++) {
				blockdata[n] = write[k];
				n += 1;
			}
			blocks[i].setData(blockdata);
			buffer.writeblocktodisk(i, addr);
			addr += 0x40;
		}
		buffer.free();
		addr = R_addr;
		for (int m = 0; m < 4; m++) {
			while (buffer.getNumfreeblk() != 4) {
				buffer.readblockfromdisk(addr);
				addr = addr + 0x40;
			}
			addr -= 4 * 0x40;
			blocks = buffer.getData();
			Queue<Integer>[] aqueues = new Queue[4];
			Queue<Integer>[] bqueues = new Queue[4];
			for (int i = 0; i < 4; i++) {
				aqueues[i] = new LinkedList<>();
				bqueues[i] = new LinkedList<>();
				byte[] blockdata = blocks[i].getData();
				int n = 0;
				for (int j = 0; j < 7; j++) {
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
			Block writeBlock[] = new Block[4];
			for (int j = 0; j < 4; j++) {
				writeBlock[j] = buffer.getNewBlockinBuffer();
			}
			List<Byte> tmpBytes = new ArrayList<Byte>();
			int f = 0;
			while (true) {
				int[] as = new int[4];
				int[] bs = new int[4];
				for (int j = 0; j < 4; j++) {
					// System.out.println(j);
					as[j] = aqueues[j].peek();
					bs[j] = bqueues[j].peek();
				}
				int amin = Integer.MAX_VALUE;
				int flag = 0;
				for (int j = 0; j < 4; j++) {
					if (as[j] < amin) {
						amin = as[j];
						flag = j;
					}
				}
				int choosea = aqueues[flag].poll();
				// System.out.println(choosea);
				int chooseb = bqueues[flag].poll();
				aqueues[flag].offer(Integer.MAX_VALUE);
				bqueues[flag].offer(Integer.MAX_VALUE);
				if (choosea == Integer.MAX_VALUE) {
					break;
				}
				for (int j = 0; j < 4; j++) {
					tmpBytes.add(data.int2byte(choosea)[j]);
				}
				for (int j = 0; j < 4; j++) {
					tmpBytes.add(data.int2byte(chooseb)[j]);
				}
				if (tmpBytes.size() == 56) {
					byte[] datatofill = new byte[64];
					for (int j = 0; j < 56; j++) {
						datatofill[j] = tmpBytes.get(j).byteValue();
					}
					for (int j = 0; j < 4; j++) {
						datatofill[56 + j] = data.int2byte(0)[j];
					}
					// System.out.println(addr);
					for (int j = 0; j < 4; j++) {
						datatofill[60 + j] = data.int2byte(addr)[j];
					}
					addr = addr + 0x40;
					// System.out.println(f);
					writeBlock[f].setData(datatofill);
					f += 1;
					tmpBytes = new ArrayList<>();
					if (f == 4) {
						f = 0;
					}
				}
			}
			addr = R_addr + m * 4 * 0x40;
			for (int j = 4; j < 8; j++) {
				// data.decode(buffer.getData()[j].getData());
				buffer.writeblocktodisk(j, addr);
				addr += 0x40;
			}
			buffer.free();
		}
		buffer.free();
		addr = R_addr;
		int blockaddr[] = new int[4];
		int l = 0;
		while (buffer.getNumfreeblk() != 4) {
			buffer.readblockfromdisk(addr);
			blockaddr[l] = addr;
			addr += 4 * 0x40;
			l += 1;
		}
		blocks = buffer.getData();
		int n[] = new int[4];

		List<Byte> bytes = new ArrayList<>();
		Block writeBlock[] = new Block[4];
		for (int j = 0; j < 4; j++) {
			writeBlock[j] = buffer.getNewBlockinBuffer();
		}
		int cnt = 0;
		addr = 0x22222222;
		while (cnt < 112) {
			int choosea = Integer.MAX_VALUE;
			int chooseb = 0;
			int ra[] = new int[4];
			int rb[] = new int[4];
			int flag = 0;
			for (int i = 0; i < 4; i++) {
				byte[] tmp = new byte[4];
				for (int j = 0; j < 4; j++) {
					tmp[j] = blocks[i].getData()[n[i]];
					n[i] += 1;
				}
				ra[i] = data.byte2int(tmp);
				for (int j = 0; j < 4; j++) {
					tmp[j] = blocks[i].getData()[n[i]];
					n[i] += 1;
				}
				rb[i] = data.byte2int(tmp);
			}
			for (int i = 0; i < 4; i++) {
				if (ra[i] < choosea) {
					choosea = ra[i];
					chooseb = rb[i];
					flag = i;
				}
			}
			for (int i = 0; i < 4; i++) {
				if (i != flag) {
					n[i] -= 8;
				}
			}
			for (int i = 0; i < 4; i++) {
				bytes.add(data.int2byte(choosea)[i]);
			}
			for (int i = 0; i < 4; i++) {
				bytes.add(data.int2byte(chooseb)[i]);
			}
			cnt += 1;

			if (bytes.size() == 56) {
				byte[] datatofill = new byte[64];
				for (int j = 0; j < 56; j++) {
					datatofill[j] = bytes.get(j).byteValue();
				}
				for (int j = 0; j < 4; j++) {
					datatofill[56 + j] = data.int2byte(0)[j];
				}
				// System.out.println(addr);
				for (int j = 0; j < 4; j++) {
					datatofill[60 + j] = data.int2byte(addr + 0x40)[j];
				}
				writeBlock[0].setData(datatofill);
				buffer.writeblocktodisk(4, addr);
				addr = addr + 0x40;
				bytes = new ArrayList<>();
			}
			if (n[flag] == 56) {
				n[flag] = 0;
				buffer.freeBlockinBuffer(flag);
				blockaddr[flag] += 0x40;
				if (blockaddr[flag] == R_addr + 4 * 0x40 || blockaddr[flag] == R_addr + 8 * 0x40
						|| blockaddr[flag] == R_addr + 12 * 0x40 || blockaddr[flag] == R_addr + 16 * 0x40) {
					byte[] datatofill = new byte[64];
					int tmp = 0;
					for (int j = 0; j < 16; j++) {
						for (int k = 0; k < 4; k++) {
							datatofill[tmp] = data.int2byte(Integer.MAX_VALUE)[k];
							tmp += 1;
						}
					}
					blocks[flag].setData(datatofill);
				} else {
					buffer.readblockfromdisk(blockaddr[flag]);
				}
			}

		}
	}

	public static void sorts() throws IOException {
		int addr = S_addr;		
		Block[] blocks = buffer.getData();
		Data data = new Data();
		addr = S_addr;
		for (int o = 0; o < 4; o++) {
			while (buffer.getNumfreeblk() != 0) {
				buffer.readblockfromdisk(addr);
				addr = addr + 0x40;
			}
			addr=S_addr+o*8*0x40;
			blocks = buffer.getData();
			for (int i = 0; i < 8; i++) {
				byte[] blockdata = blocks[i].getData();
				int m = 0;
				int[] as = new int[7];
				int[] bs = new int[7];
				for (int j = 0; j < 7; j++) {
					byte[] tmp = new byte[4];
					for (int k = 0; k < 4; k++) {
						tmp[k] = blockdata[m];
						m += 1;
					}
					int ra = data.byte2int(tmp);
					as[j] = ra;
					for (int k = 0; k < 4; k++) {
						tmp[k] = blockdata[m];
						m += 1;
					}
					int rb = data.byte2int(tmp);
					bs[j] = rb;
				}
				for (int j = 0; j < 7; j++) {
					for (int k = 0; k < 6 - j; k++) {
						if (as[k] > as[k + 1]) {
							int tmpa = as[k];
							as[k] = as[k + 1];
							as[k + 1] = tmpa;
							int tmpb = bs[k];
							bs[k] = bs[k + 1];
							bs[k + 1] = tmpb;
						}
					}
				}
				int n = 0;
				for (int j = 0; j < 7; j++) {
					byte[] write = data.int2byte(as[j]);
					for (int k = 0; k < 4; k++) {
						blockdata[n] = write[k];
						n += 1;
					}
					write = data.int2byte(bs[j]);
					for (int k = 0; k < 4; k++) {
						blockdata[n] = write[k];
						n += 1;
					}
				}
				byte[] write = data.int2byte(0);
				for (int k = 0; k < 4; k++) {
					blockdata[n] = write[k];
					n += 1;
				}
				write = data.int2byte(addr + 0x40);
				for (int k = 0; k < 4; k++) {
					blockdata[n] = write[k];
					n += 1;
				}
				blocks[i].setData(blockdata);
				buffer.writeblocktodisk(i, addr);
				addr += 0x40;
			}
			buffer.free();
		}
		addr = S_addr;
		for (int m = 0; m < 8; m++) {
			while (buffer.getNumfreeblk() != 4) {
				buffer.readblockfromdisk(addr);
				addr = addr + 0x40;
			}
			addr -= 4 * 0x40;
			blocks = buffer.getData();
			Queue<Integer>[] aqueues = new Queue[4];
			Queue<Integer>[] bqueues = new Queue[4];
			for (int i = 0; i < 4; i++) {
				aqueues[i] = new LinkedList<>();
				bqueues[i] = new LinkedList<>();
				byte[] blockdata = blocks[i].getData();
				int n = 0;
				for (int j = 0; j < 7; j++) {
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
			Block writeBlock[] = new Block[4];
			for (int j = 0; j < 4; j++) {
				writeBlock[j] = buffer.getNewBlockinBuffer();
			}
			List<Byte> tmpBytes = new ArrayList<Byte>();
			int f = 0;
			while (true) {
				int[] as = new int[4];
				int[] bs = new int[4];
				for (int j = 0; j < 4; j++) {
					// System.out.println(j);
					as[j] = aqueues[j].peek();
					bs[j] = bqueues[j].peek();
				}
				int amin = Integer.MAX_VALUE;
				int flag = 0;
				for (int j = 0; j < 4; j++) {
					if (as[j] < amin) {
						amin = as[j];
						flag = j;
					}
				}
				int choosea = aqueues[flag].poll();
				// System.out.println(choosea);
				int chooseb = bqueues[flag].poll();
				aqueues[flag].offer(Integer.MAX_VALUE);
				bqueues[flag].offer(Integer.MAX_VALUE);
				if (choosea == Integer.MAX_VALUE) {
					break;
				}
				for (int j = 0; j < 4; j++) {
					tmpBytes.add(data.int2byte(choosea)[j]);
				}
				for (int j = 0; j < 4; j++) {
					tmpBytes.add(data.int2byte(chooseb)[j]);
				}
				if (tmpBytes.size() == 56) {
					byte[] datatofill = new byte[64];
					for (int j = 0; j < 56; j++) {
						datatofill[j] = tmpBytes.get(j).byteValue();
					}
					for (int j = 0; j < 4; j++) {
						datatofill[56 + j] = data.int2byte(0)[j];
					}
					// System.out.println(addr);
					for (int j = 0; j < 4; j++) {
						datatofill[60 + j] = data.int2byte(addr)[j];
					}
					addr = addr + 0x40;
					// System.out.println(f);
					writeBlock[f].setData(datatofill);
					f += 1;
					tmpBytes = new ArrayList<>();
					if (f == 4) {
						f = 0;
					}
				}
			}
			addr = S_addr + m * 4 * 0x40;
			for (int j = 4; j < 8; j++) {
				// data.decode(buffer.getData()[j].getData());
				buffer.writeblocktodisk(j, addr);
				addr += 0x40;
			}
			buffer.free();
		}
		buffer.free();
		for(int o=0;o<2;o++) {
			buffer.free();
			addr = S_addr+16*o*0x40;
			int blockaddr[] = new int[4];
			int l = 0;
			while (buffer.getNumfreeblk() != 4) {
				buffer.readblockfromdisk(addr);
				blockaddr[l] = addr;
				addr += 4 * 0x40;
				l += 1;
			}
			blocks = buffer.getData();
			int n[] = new int[4];
			List<Byte> bytes = new ArrayList<>();
			Block writeBlock[] = new Block[4];
			for (int j = 0; j < 4; j++) {
				writeBlock[j] = buffer.getNewBlockinBuffer();
			}
			int cnt = 0;
			addr = 0x44444444+16*o*0x40;
			while (cnt < 112) {
				int choosea = Integer.MAX_VALUE;
				int chooseb = 0;
				int ra[] = new int[4];
				int rb[] = new int[4];
				int flag = 0;
				for (int i = 0; i < 4; i++) {
					byte[] tmp = new byte[4];
					for (int j = 0; j < 4; j++) {
						tmp[j] = blocks[i].getData()[n[i]];
						n[i] += 1;
					}
					ra[i] = data.byte2int(tmp);
					for (int j = 0; j < 4; j++) {
						tmp[j] = blocks[i].getData()[n[i]];
						n[i] += 1;
					}
					rb[i] = data.byte2int(tmp);
				}
				for (int i = 0; i < 4; i++) {
					if (ra[i] < choosea) {
						choosea = ra[i];
						chooseb = rb[i];
						flag = i;
					}
				}
				for (int i = 0; i < 4; i++) {
					if (i != flag) {
						n[i] -= 8;
					}
				}
				for (int i = 0; i < 4; i++) {
					bytes.add(data.int2byte(choosea)[i]);
				}
				for (int i = 0; i < 4; i++) {
					bytes.add(data.int2byte(chooseb)[i]);
				}
				cnt += 1;
				if (bytes.size() == 56) {
					byte[] datatofill = new byte[64];
					for (int j = 0; j < 56; j++) {
						datatofill[j] = bytes.get(j).byteValue();
					}
					for (int j = 0; j < 4; j++) {
						datatofill[56 + j] = data.int2byte(0)[j];
					}
					for (int j = 0; j < 4; j++) {
						datatofill[60 + j] = data.int2byte(addr + 0x40)[j];
					}
					writeBlock[0].setData(datatofill);
					buffer.writeblocktodisk(4, addr);
					addr = addr + 0x40;
					bytes = new ArrayList<>();
				}
				if (n[flag] == 56) {
					n[flag] = 0;
					buffer.freeBlockinBuffer(flag);
					blockaddr[flag] += 0x40;
					if (blockaddr[flag] == S_addr + 4 * 0x40+16*o*0x40 || blockaddr[flag] == S_addr + 8 * 0x40+16*o*0x40 
							|| blockaddr[flag] == S_addr + 12 * 0x40 +16*o*0x40 || blockaddr[flag] == S_addr + 16 * 0x40+16*o*0x40 ) {
						byte[] datatofill = new byte[64];
						int tmp = 0;
						for (int j = 0; j < 16; j++) {
							for (int k = 0; k < 4; k++) {
								datatofill[tmp] = data.int2byte(Integer.MAX_VALUE)[k];
								tmp += 1;
							}
						}
						blocks[flag].setData(datatofill);
					} else {
						buffer.readblockfromdisk(blockaddr[flag]);
					}
				}

			}
		}
		buffer.free();
		addr=0x44444444;
		int blkptr1=buffer.readblockfromdisk(addr);
		int blkptr2=buffer.readblockfromdisk(addr+16*0x40);
		int cnt=0;
		int blockaddr[] = new int[2];
		blockaddr[0]=addr;
		blockaddr[1]=addr+16*0x40;
		blocks = buffer.getData();
		int n[] = new int[2];
		List<Byte> bytes = new ArrayList<>();
		addr = 0x77777777;
		while(cnt<224) {
			int choosea = Integer.MAX_VALUE;
			int chooseb = 0;
			int ra[] = new int[2];
			int rb[] = new int[2];
			int flag = 0;
			for (int i = 0; i <2; i++) {
				byte[] tmp = new byte[4];
				for (int j = 0; j < 4; j++) {
					tmp[j] = blocks[i].getData()[n[i]];
					n[i] += 1;
				}
				ra[i] = data.byte2int(tmp);
				for (int j = 0; j < 4; j++) {
					tmp[j] = blocks[i].getData()[n[i]];
					n[i] += 1;
				}
				rb[i] = data.byte2int(tmp);
			}
			for (int i = 0; i < 2; i++) {
				if (ra[i] < choosea) {
					choosea = ra[i];
					chooseb = rb[i];
					flag = i;
				}
			}
			for (int i = 0; i < 2; i++) {
				if (i != flag) {
					n[i] -= 8;
				}
			}
			for (int i = 0; i < 4; i++) {
				bytes.add(data.int2byte(choosea)[i]);
			}
			for (int i = 0; i < 4; i++) {
				bytes.add(data.int2byte(chooseb)[i]);
			}
			cnt+=1;
			if (bytes.size() == 56) {
				byte[] datatofill = new byte[64];
				for (int j = 0; j < 56; j++) {
					datatofill[j] = bytes.get(j).byteValue();
				}
				for (int j = 0; j < 4; j++) {
					datatofill[56 + j] = data.int2byte(0)[j];
				}
				for (int j = 0; j < 4; j++) {
					datatofill[60 + j] = data.int2byte(addr + 0x40)[j];
				}
				buffer.getData()[blkptr2+1].setData(datatofill);
				buffer.writeblocktodisk(2, addr);
				addr = addr + 0x40;
				bytes = new ArrayList<>();
			}
			if (n[flag] == 56) {
				n[flag] = 0;
				buffer.freeBlockinBuffer(flag);
				blockaddr[flag] += 0x40;
				if (blockaddr[flag] == 0x44444444 + 16* 0x40 || blockaddr[flag] == 0x44444444 + 32* 0x40 ) {
					byte[] datatofill = new byte[64];
					int tmp = 0;
					for (int j = 0; j < 16; j++) {
						for (int k = 0; k < 4; k++) {
							datatofill[tmp] = data.int2byte(Integer.MAX_VALUE)[k];
							tmp += 1;
						}
					}
					blocks[flag].setData(datatofill);
				} else {
					buffer.readblockfromdisk(blockaddr[flag]);
				}
			}
			
		}
	}

	public static void decodeoutput() throws IOException {
		int addr = 0x22222222;
		Data data = new Data();
		for (int i = 0; i < 16; i++) {
			System.out.println((i+1)+".");
			List<String> singlefile = Files.readLines(new File("src/blocks/" + addr + ".blk"), Charsets.UTF_8);
			byte[] tmp = new byte[64];
			for (int j = 0; j < 56; j++) {
				tmp[j] = Byte.parseByte(singlefile.get(j));
			}
			int n = 0;
			for (int j = 0; j < 7 ;j++) {
				byte[] tmpa = new byte[4];
				for (int k = 0; k < 4; k++) {
					tmpa[k] = tmp[n];
					n += 1;
				}
				int ra = data.byte2int(tmpa);
				for (int k = 0; k < 4; k++) {
					tmpa[k] = tmp[n];
					n += 1;
				}
				int rb = data.byte2int(tmpa);
				System.out.println("( " + ra + " , " + rb + " )");
			}
			addr += 0x40;			
			
		}
	}

	public static void main(String[] args) {
		try {
			//sorts();
			sortr();
			decodeoutput();
			//selectr(31);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

}
