package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import NativeDataTypes.CDT;
import Scanning.Token;

/**
 * Created by reidhoruff on 10/8/14.
 */
public class IdentifierReferenceETNode extends ETNode {

    private final Token token;
    private final int hash;

    public IdentifierReferenceETNode(Token token) {
        this.token = token;
        this.hash = token.sequence.hashCode();
    }

    public CDT execute(CraterVariableScope scope) {
        return scope.getVariableReference(this.hash, this.token.sequence);
    }

    public void print(int level) {
        putSpaces(level);
        System.out.println(this.token.sequence);
    }
}
