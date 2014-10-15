package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import Scanning.Token;

/**
 * Created by reidhoruff on 10/8/14.
 */
public class IdentifierReferenceETNode extends ETNode {

    private Token token;

    public IdentifierReferenceETNode(Token token) {
        this.token = token;
    }

    public CDT execute() {
        return this.getVariableScope().getVariableReference(this.token.sequence);
    }

    public void setChildrenVariableScope(CraterVariableScope scope) {
        //...
    }

    public void print(int level) {
        putSpaces(level);
        System.out.println(this.token.sequence);
    }
}
