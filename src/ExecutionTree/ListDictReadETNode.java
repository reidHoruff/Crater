package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.*;

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
    public CDT execute(CraterVariableScope scope) {
        CDT list = this.listAccessee.executeMetaSafe(scope);
        CDT index = this.index.executeMetaSafe(scope);
        CDT value = list.siIndex(index);

        return value;
    }
}
