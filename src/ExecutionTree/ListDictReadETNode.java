package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import NativeDataTypes.CDT;
import NativeDataTypes.CInteger;
import NativeDataTypes.CList;
import NativeDataTypes.CRange;

/**
 * Created by reidhoruff on 10/9/14.
 */
public class ListDictReadETNode extends ETNode {

    private ETNode listAccessee, index;

    public ListDictReadETNode(ETNode listAccessee, ETNode index) {
        this.listAccessee = listAccessee.setParent(this);
        this.index = index.setParent(this);
    }

    @Override
    public void setChildrenVariableScope(CraterVariableScope scope) {
        this.listAccessee.setVariableScope(getVariableScope());
        this.index.setVariableScope(getVariableScope());
    }

    @Override
    public CDT execute() {
        CDT list = this.listAccessee.executeMetaSafe();
        CDT index = this.index.executeMetaSafe();
        return list.siIndex(index);

        /**
        if (list instanceof CList) {
            if (index instanceof CRange) {
                return this.rangeListAccess(list.toCList(), index.toCRange());
            } else if (index instanceof CInteger) {
                return this.singleValueListAccess(list.toCList(), index.toInt());
            }
        }

        throw new CraterExecutionException("lists must be accessed via integers or range");
         */
    }
}
