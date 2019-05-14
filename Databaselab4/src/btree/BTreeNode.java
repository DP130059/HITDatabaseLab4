package btree;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class BTreeNode {
    /**
     * B���Ľ�
     */
    public int M;

    /**
     * �ؼ����б�
     */
    public LinkedList<Integer> values;
    public Multimap<Integer, Integer>addrs;

    /**
     * ���ڵ�
     */
    public BTreeNode parent;

    /**
     * �����б�
     */
    public LinkedList<BTreeNode> children;

    /**
     * ����һ�ÿյ�B-��
     */
    private BTreeNode() {
    	this.addrs=ArrayListMultimap.create();
        this.values = new LinkedList<Integer>();
        this.children = new LinkedList<BTreeNode>();
    }

    /**
     * ����һ�ÿյ�m��B-��
     *
     * @param m B-���Ľ�
     */
    public BTreeNode(int m) {
        this();
        if(m < 3) {
            throw new RuntimeException("The order of B-Tree should be greater than 2.");
        }
        this.M = m;
    }

    /**
     * ���ݸ��ڵ㹹��һ���յĺ��ӽڵ�
     *
     * @param parent ���ڵ�
     */
    public BTreeNode(BTreeNode parent) {
        this(parent.M);
        this.parent = parent;
    }

    /**
     * ��B-��������ݣ����ҵ����ڵ㣬�Ӹ��ڵ������Ҳ����λ�ã�����
     * B-�����������ظ������ݣ�����������е�ֵ���׳��쳣����.
     * ������ܻ�����µĸ��ڵ㣬�ᵼ�µ�ǰ�ڵ㲻���Ǹ��ڵ㣬�����µĸ��ڵ�
     *
     * @param e Ҫ�����Ԫ��
     * @return ������ɺ�ĸ��ڵ�
     */
    public BTreeNode insert(int e,List<Integer>addrList) {
        if(isEmpty()) {
            values.add(e);
            addrs.putAll(e,addrList);
            children.add(new BTreeNode(this));
            children.add(new BTreeNode(this));
            return this;
        }
        BTreeNode p = getRoot().search(e);
        if(!p.isEmpty()) {
            throw new RuntimeException("cannot insert duplicate key to B-Tree, key: " + e);
        }
        insertNode(p.parent, e, addrList,new BTreeNode(p.M));
        return getRoot();
    }

    private void insertNode(BTreeNode node, int e,List<Integer>addrList, BTreeNode eNode) {
        int valueIndex = 0;
        while(valueIndex < node.values.size() && node.values.get(valueIndex) < e) {
            valueIndex++;
        }
        node.values.add(valueIndex, e);
        node.addrs.putAll(e,addrList);
        eNode.parent = node;
        node.children.add(valueIndex+1, eNode);
        if(node.values.size() > M-1) {
            // ��ȡ�����ؼ���
            int upIndex = M/2;
            int up = node.values.get(upIndex);
            Multimap<Integer, Integer>upMultimap=ArrayListMultimap.create();
            Set<Integer>tmpSet=new HashSet<>();
            tmpSet.addAll(node.addrs.keySet());
            Iterator<Integer>tmpIterator=tmpSet.iterator();
            while (tmpIterator.hasNext()) {
				Integer key = (Integer) tmpIterator.next();
				if(key>up)
					upMultimap.putAll(key, node.addrs.removeAll(key));
			}
            // ��ǰ�ڵ��Ϊ���������֣��󲿵�parent���䣬�Ҳ���parent���������ؼ����Ҳ�
            BTreeNode rNode = new BTreeNode(M);
            rNode.values = new LinkedList(node.values.subList(upIndex+1, M));
            rNode.children = new LinkedList(node.children.subList(upIndex+1, M+1));
            rNode.addrs=upMultimap;
            /*  ����rNode.children�Ǵ�node.children���������,��parent��ָ��node��
                ������Ҫ��rNode.children��parent��Ϊָ��rNode
             */
            for(BTreeNode rChild : rNode.children) {
                rChild.parent = rNode;
            }
            node.values = new LinkedList(node.values.subList(0, upIndex));
            node.children = new LinkedList(node.children.subList(0, upIndex+1));
            // �Ӹ��ڵ���������ѡȡ�����ؼ�����Ϊ�µĸ��ڵ�
            if(node.parent == null) {
                node.parent = new BTreeNode(M);
                node.parent.values.add(up);
                node.parent.addrs.putAll(up, node.addrs.get(up));
                node.parent.children.add(node);
                node.parent.children.add(rNode);
                rNode.parent = node.parent;
                return;
            }
            // ���ڵ����ӹؼ��֣��ݹ����
            insertNode(node.parent, up, (List<Integer>) node.addrs.get(up),rNode);
        }
    }

    /**
     * ��B-����ɾ���ؼ��֣����ҵ����ڵ㣬�Ӹ��ڵ�������Ҫɾ���Ĺؼ��֣����
     * �ؼ��ֲ����ڣ��׳�ɾ���쳣��������ڣ����ؼ��ֲ������²���ն˽ڵ㣨Ҷ�ӽڵ��
     * ��һ�㣩����ʱֻ��Ҫ�ؼ��ֺ͹ؼ��ֽڵ���ڵ��������е���СֵN������Ȼ��ɾ��N��
     * ������ת��Ϊ��ɾ���ؼ��������²���ն˽ڵ�����������ֻ�����۹ؼ��������²�
     * ���ն˽ڵ���������ʱ��Ϊ���������
     * 1. ��ɾ���ؼ������ڵĽڵ�ؼ��������ڵ��� ceil(M/2)
     * 2. ��ɾ���ؼ������ڵĽڵ�ؼ��������� ceil(M/2)-1���Ҹýڵ��������ֵܣ������ֵܣ�
     * �еĹؼ��������� ceil(M/2)-1��ֻ�轫���ֵܽڵ��е���С������󣩹ؼ������Ƶ�
     * ˫�׽ڵ��У�����˫�׽ڵ���С�ڣ�����ڣ������ƶ��ؼ��ֵĽ��ڹؼ������Ƶ���ɾ
     * �ؼ������ڵĽڵ��С�
     * 3. ��ɾ���ؼ������ڵĽڵ�ؼ��������� ceil(M/2)-1���������ֵܽڵ�Ĺؼ�����������
     * ceil(M/2)-1������ýڵ������ֵ�A������ɾ���ؼ���֮�������ڵĽڵ���ʣ��Ĺؼ��ֺ�
     * �������ã�����˫�׽ڵ��е�ָ��A�����ؼ���һ�𣬺ϲ���A��ȥ����û�����ֵ���ϲ���
     * ���ֵ��У�����ʱ˫�׽ڵ�Ĺؼ�����������һ��������˵�����ؼ�����С��ceil(M/2)-1��
     * ���˫�׽ڵ����ݹ鴦��
     *
     * ɾ�����ܻ�����µĸ��ڵ㣬�ᵼ�µ�ǰ�ڵ㲻���Ǹ��ڵ�
     *
     * @param e Ҫɾ����Ԫ��
     * @return ɾ����ɺ�ĸ��ڵ�
     */
    public BTreeNode delete(int e) {
        if(isEmpty()) {
            return this;
        }
        BTreeNode p = getRoot().search(e);
        if(p.isEmpty()) {
            throw new RuntimeException("the key to be deleted is not exist, key: " + e);
        }
        int valueIndex = 0;
        while(valueIndex < p.values.size() && p.values.get(valueIndex) < e) {
            valueIndex++;
        }
        // ���p�������²���ն˽ڵ�
        if(!p.children.get(0).isEmpty()) {
            BTreeNode rMin = p.children.get(valueIndex);
            while(!rMin.children.get(0).isEmpty()) {
                rMin = rMin.children.get(0);
            }
            p.values.set(valueIndex, rMin.values.get(0));
            return delete(rMin, valueIndex, 0);
        }
        return delete(p, valueIndex, 0);
    }

    /**
     * ɾ��ָ���ڵ��еĹؼ��ֺͺ��ӣ�������ɾ�������B-����������
     * @param target Ŀ��ڵ�
     * @param valueIndex �ؼ�������
     * @param childIndex ��������
     * @return ɾ����ɺ�ĸ��ڵ�
     */
    private BTreeNode delete(BTreeNode target, int valueIndex, int childIndex) {
        target.values.remove(valueIndex);
        target.children.remove(childIndex);
        if(target.children.size() >= Math.ceil(M/2.0)) {
            return target.getRoot();
        }
        if(target.isRoot()) {
            if(target.children.size() > 1) {
                return target;
            }else {
                BTreeNode newRoot = target.children.get(0);
                newRoot.parent = null;
                return newRoot;
            }
        }
        int parentChildIndex = 0;
        while(parentChildIndex < target.parent.children.size() && target.parent.children.get(parentChildIndex) != target) {
            parentChildIndex++;
        }
        if(parentChildIndex > 0 && target.parent.children.get(parentChildIndex-1).values.size() >= Math.ceil(M/2.0)) {
            // ���ֵܹؼ��������� ceil(M/2)-1
            int downKey = target.parent.values.get(parentChildIndex-1);
            BTreeNode leftSibling = target.parent.children.get(parentChildIndex-1);
            int upKey = leftSibling.values.remove(leftSibling.values.size()-1);
            BTreeNode mergeChild = leftSibling.children.remove(leftSibling.children.size()-1);
            target.values.add(0, downKey);
            target.children.add(0, mergeChild);
            target.parent.values.set(parentChildIndex-1, upKey);
            return target.getRoot();
        }else if(parentChildIndex < target.parent.children.size()-1 &&
                target.parent.children.get(parentChildIndex+1).values.size() >= Math.ceil(M/2.0)) {
            // ���ֵܹؼ��������� ceil(M/2)-1
            int downKey = target.parent.values.get(parentChildIndex);
            BTreeNode rightSibling = target.parent.children.get(parentChildIndex+1);
            int upKey = rightSibling.values.remove(0);
            BTreeNode mergeChild = rightSibling.children.remove(0);
            target.values.add(downKey);
            target.children.add(mergeChild);
            target.parent.values.set(parentChildIndex, upKey);
            return target.getRoot();
        }else {
            // �����ֵܹؼ������������� ceil(M/2)-1
            int parentValueIndex;
            if(parentChildIndex > 0) {
                // ��������ֵܣ������ֵܺϲ�
                BTreeNode leftSibling = target.parent.children.get(parentChildIndex-1);
                // ���ϸ��ڵ�ؼ���
                parentValueIndex = parentChildIndex - 1;
                int downKey = target.parent.values.get(parentValueIndex);
                leftSibling.values.add(downKey);
                // ����Ŀ��ڵ��ʣ����Ϣ
                leftSibling.values.addAll(target.values);
                target.children.forEach(c -> c.parent=leftSibling);
                leftSibling.children.addAll(target.children);
            }else {
                // û�����ֵܺ����ֵܺϲ�
                BTreeNode rightSibling = target.parent.children.get(parentChildIndex+1);
                // ���ϸ��ڵ�ؼ���
                parentValueIndex = parentChildIndex;
                int downKey = target.parent.values.get(parentValueIndex);
                rightSibling.values.add(0, downKey);
                // ����Ŀ��ڵ��ʣ����Ϣ
                rightSibling.values.addAll(0, target.values);
                target.children.forEach(c -> c.parent=rightSibling);
                rightSibling.children.addAll(0, target.children);
            }
            // �ݹ�ɾ�����ڵ�ؼ��ֺͺ���
            return delete(target.parent, parentValueIndex, parentChildIndex);
        }
    }

    /**
     * �ӵ�ǰ�ڵ����²���Ŀ��ֵtarget
     *
     * @param target
     * @return �ҵ��򷵻��ҵ��Ľڵ㣬�������򷵻�Ҷ�ӽڵ�
     */
    public BTreeNode search(int target) {
        if(isEmpty()) {
            return this;
        }
        int valueIndex = 0;
        while(valueIndex < values.size() && values.get(valueIndex) <= target) {
            if(values.get(valueIndex) == target) {
                return this;
            }
            valueIndex++;
        }
        return children.get(valueIndex).search(target);
    }

    /**
     * ��ȡ���ڵ�
     *
     * @return ���ڵ�
     */
    public BTreeNode getRoot() {
        BTreeNode p = this;
        while(!p.isRoot()) {
            p = p.parent;
        }
        return p;
    }

    /**
     * �жϵ�ǰ�ڵ��Ƿ��ǿսڵ�
     *
     * @return �սڵ㷵��true, �ǿսڵ㷵��false
     */
    public boolean isEmpty() {
        if(values == null || values.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * �жϵ�ǰ�ڵ��Ƿ��Ǹ��ڵ�
     *
     * @return �Ǹ��ڵ㷵��true, ���Ƿ���false
     */
    public boolean isRoot() {
        return parent == null;
    }

    /*
     * ��յ�ǰ�ڵ�, ��������ϵ
     */
    public void clear() {
        values.clear();
        children.clear();
    }

    /**
     * �Ե�ǰ�ڵ�Ϊ�����ڿ���̨��ӡB-��
     */
    public void print() {
        printNode(this, 0);
    }

    /**
     * ����̨��ӡ�ڵ�ĵݹ����
     *
     * @param node Ҫ��ӡ�ڵ�
     * @param depth �ݹ����
     */
    private void printNode(BTreeNode node, int depth) {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < depth; i++) {
            sb.append("|    ");
        }
        if(depth > 0) {
            sb.append("|----");
        }
        sb.append(node.values);
        System.out.println(sb.toString());
        for(BTreeNode child : node.children) {
            printNode(child, depth+1);
        }
    }
}