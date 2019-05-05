package extmem;

import com.google.common.primitives.UnsignedBytes;

public class Buffer {
	private int numIO;
	private int bufsize;
	private int blksize;
	private int numallblock;
	private int numfreeblk;
	private Block[] data;
	public Buffer(int bufsize,int blksize)
	{
		this.numIO=0;
		this.bufsize=bufsize;
		this.blksize=blksize;
		this.numallblock=bufsize/(blksize+1);
		this.numfreeblk=this.numallblock;
		data=new Block[numallblock];
		for(int i=0;i<numallblock;i++) {
			data[i]=new Block(this.blksize, 0);
		}
	}
	public void free() {
		this.numIO=0;
		this.bufsize=bufsize;
		this.blksize=blksize;
		this.numallblock=bufsize/(blksize+1);
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
		data[i].setNotAvailable();
	}
	
}
