package btree;

import java.util.ArrayList;
import java.util.List;

public class BTreeNode {
	private int index;
	private int addr;
	private BTreeNode parent;
	private List<BTreeNode>children=new ArrayList<>();
	
	public BTreeNode(int index,int addr) {
		this.index=index;
		this.addr=addr;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getAddr() {
		return addr;
	}

	public void setAddr(int addr) {
		this.addr = addr;
	}

	public BTreeNode getParent() {
		return parent;
	}

	public void setParent(BTreeNode parent) {
		this.parent = parent;
	}

	public List<BTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<BTreeNode> children) {
		this.children = children;
	}
	public void addchildren(BTreeNode node) {
		this.children.add(node);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BTreeNode [index=");
		builder.append(index);
		builder.append(", addr=");
		builder.append(addr);
		builder.append(", parent=");
		builder.append(parent);
		builder.append(", children=");
		builder.append(children);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + addr;
		result = prime * result + ((children == null) ? 0 : children.hashCode());
		result = prime * result + index;
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BTreeNode other = (BTreeNode) obj;
		if (addr != other.addr)
			return false;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (index != other.index)
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}
	

}
