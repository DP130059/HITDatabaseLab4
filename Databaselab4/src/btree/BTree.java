package btree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import data.Data;
import extmem.Block;
import extmem.Buffer;

public class BTree {
	private static BTreeNode tree=new BTreeNode(3);
	
	public static void main(String args[]) {
		tree=tree.insert(0,Lists.newArrayList(1,2,3));
		tree=tree.insert(1,Lists.newArrayList(1,2,3));
		tree=tree.insert(2,Lists.newArrayList(1,2,3));
		tree=tree.insert(3,Lists.newArrayList(1,2,3));
		tree=tree.search(3);
		Multimap<Integer, Integer>addrsMultimap=tree.addrs;
		Set<Integer>keySet=addrsMultimap.keySet();
		Iterator<Integer>kIterator=keySet.iterator();
		while (kIterator.hasNext()) {
			Integer integer = (Integer) kIterator.next();
			List<Integer>tmp=(List<Integer>) addrsMultimap.get(integer);
			for(int i=0;i<tmp.size();i++) {
				System.out.println("( "+integer+" , "+tmp.get(i)+" )");
			}
		}
	}
	

}
