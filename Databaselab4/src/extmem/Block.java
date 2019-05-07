package extmem;

import java.util.Arrays;

public class Block {
	private int size;
	private byte[] data;
	private boolean available;
	public Block(int size,int next) {
		this.size=size;
		this.data=new byte[size];
		this.available=true;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.available=false;
		this.data = data;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setNotAvailable() {
		this.available = false;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Block [size=");
		builder.append(size);
		builder.append(", data=");
		builder.append(Arrays.toString(data));
		builder.append(", available=");
		builder.append(available);
		builder.append("]");
		return builder.toString();
	}
	public void setAvailable() {
		this.available = true;
	}

}
