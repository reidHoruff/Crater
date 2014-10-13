package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterParserException;
import NativeDataTypes.CBoolean;
import NativeDataTypes.CDT;
import Scanning.Token;

/**
 * Created by reidhoruff on 10/9/14.
 */
public class BooleanLiteralETNode extends ETNode {

    public boolean value;

    public BooleanLiteralETNode(Token token) {
        if (token.sequence.equalsIgnoreCase("true")) {
            this.value = true;
        } else if (token.sequence.equalsIgnoreCase("false")) {
            this.value = false;
        } else {
            throw new CraterParserException("what is this boolean literal shit?!");
        }
    }

    public CDT execute() {
        return new CBoolean(this.value);
    }

    public void setChildrenVariableScope(CraterVariableScope scope) {
        //ain't got no kids son...
    }
}
