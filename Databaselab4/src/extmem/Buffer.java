package extmem;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Buffer {
	private int numIO;
	private int bufsize;
	private int blksize;
	private int numallblock;
	private int numfreeblk;
	private Block[] data;
	
	public int getNumIO() {
		return numIO;
	}
	public int getBufsize() {
		return bufsize;
	}
	public int getBlksize() {
		return blksize;
	}
	public int getNumallblock() {
		return numallblock;
	}
	public int getNumfreeblk() {
		return numfreeblk;
	}
	public Block[] getData() {
		return data;
	}
	public Buffer(int bufsize,int blksize)
	{
		this.numIO=0;
		this.bufsize=bufsize;
		this.blksize=blksize;
		this.numallblock=bufsize/(blksize);
		this.numfreeblk=this.numallblock;
		data=new Block[numallblock];
		for(int i=0;i<numallblock;i++) {
			data[i]=new Block(this.blksize, 0);
		}
	}
	public void free() {
		this.numallblock=bufsize/(blksize);
		this.numfreeblk=this.numallblock;
		data=new Block[numallblock];
		for(int i=0;i<numallblock;i++) { 
			data[i]=new Block(this.blksize, 0);
		}
	}
	public Block getNewBlockinBuffer() {
		if(this.numfreeblk==0) {
			System.out.println("Buffer is Full!");
			return null;
		}
		for(int i=0;i<numallblock;i++) {
			if(data[i].isAvailable()) {
				data[i].setNotAvailable();
				this.numfreeblk-=1;
				return data[i];
			}
		}
		return null;
	}
	public void freeBlockinBuffer(int i) {
		this.numfreeblk+=1;
		data[i]=new Block(this.blksize, 0);
	}
	public boolean dropblockondisk(long addr) {
		addr=addr&0xFFFFFFFF;
		File file =new File("src/blocks/"+addr+".blk");
		if(!file.exists()) {
			System.out.println("File doesn't exists!");
			return false;
		}
		file.delete();
		return true;
	}
	public int readblockfromdisk(long addr) throws IOException {
		addr=addr&0xFFFFFFFF;
		File file =new File("src/blocks/"+addr+".blk");
		if(this.numfreeblk==0) {
			System.out.println("Buffer Overflows!");
			return -1;
		}
		int result=0;
		for(int i=0;i<this.data.length;i++) {
			if(data[i].isAvailable()) {
				result=i;
				break;
			}
		}
		data[result].setNotAvailable();
		List<String>readlines=Files.readLines(file, Charsets.UTF_8);
		byte[] resultdata=new byte[this.blksize];
		for(int i=0;i<readlines.size();i++) {
			resultdata[i]=Byte.parseByte(readlines.get(i));
		}
		data[result].setData(resultdata);
		this.numfreeblk-=1;
		this.numIO+=1;
		return result;
	}
	public boolean writeblocktodisk(int blkptr,long addr) throws IOException {
		addr=addr&0xFFFFFFFF;
		File file =new File("src/blocks/"+addr+".blk");
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<this.blksize;i++) {
			sb.append(this.data[blkptr].getData()[i]);
			sb.append("\n");
		}
		this.data[blkptr].setAvailable();
		this.numfreeblk+=1;
		this.numIO+=1;
		Files.write(sb, file, Charsets.UTF_8);
		return true;
	}
	
}
