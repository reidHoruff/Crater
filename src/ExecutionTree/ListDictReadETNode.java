package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import NativeDataTypes.CDT;
import NativeDataTypes.CInteger;
import NativeDataTypes.CList;

/**
 * Created by reidhoruff on 10/9/14.
 */
public class ListDictReadETNode extends ETNode {

    private ETNode listAccessee, startingIndex, endingIndex;

    public ListDictReadETNode(ETNode listAccessee, ETNode startingIndex, ETNode endingIndex) {
        this.listAccessee = listAccessee.setParent(this);
        this.startingIndex = startingIndex.setParent(this);

        if (endingIndex != null) {
            this.endingIndex = endingIndex.setParent(this);
        } else {
            this.endingIndex = null;
        }
    }

    @Override
    public void setChildrenVariableScope(CraterVariableScope scope) {
        this.listAccessee.setVariableScope(getVariableScope());
        this.startingIndex.setVariableScope(getVariableScope());
        if (this.endingIndex != null) {
            this.endingIndex.setVariableScope(getVariableScope());
        }
    }

    @Override
    public CDT execute() {
        CDT list = this.listAccessee.executeMetaSafe();
        CDT startingIndex = this.startingIndex.executeMetaSafe();
        CDT endingIndex = null;
        if (this.endingIndex != null) {
            endingIndex = this.endingIndex.executeMetaSafe();
        }

        if (list instanceof CList && startingIndex instanceof CInteger) {
            if (endingIndex instanceof CInteger) {
                return this.rangeListAccess(list.toCList(), startingIndex.toInt(), endingIndex.toInt());
            } else {
                return this.singleValueListAccess(list.toCList(), startingIndex.toInt());
            }
        }

        throw new CraterExecutionException("lists must be accessed via integers");
    }

    public CList rangeListAccess(CList list, int a, int b) {
        CList newList = new CList();
        for (int x = a; x < b; x++) {
            newList.addCDT(list.getElementMetaSafe(x).clone());
        }
        return newList;
    }

    public CDT singleValueListAccess(CList list, int index) {
        if (index < 0 || index >= list.getLength()) {
            throw new CraterExecutionException("index out of bounds");
        }

        return list.getElementWithMeta(index);
    }
}
