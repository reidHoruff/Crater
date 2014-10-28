package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import Scanning.Token;

/**
 * Created by reidhoruff on 10/27/14.
 */
public class MemberAccessorETNode extends ETNode {

    private String accessorName;
    private ETNode accessee;

    public MemberAccessorETNode(ETNode accessee, Token accessorName) {
        this.accessee = accessee.setParent(this);
        this.accessorName = accessorName.sequence;
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        return this.accessee.executeMetaSafe(scope).siAccessMember(this.accessorName);
    }
}
