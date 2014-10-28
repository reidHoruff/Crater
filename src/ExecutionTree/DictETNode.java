package ExecutionTree;

import CraterExecutionEnvironment.CraterVariableScope;
import CraterHelpers.ETNodePair;
import NativeDataTypes.CDT;
import NativeDataTypes.CDict;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/14/14.
 */
public class DictETNode extends ETNode {

    public ArrayList<ETNodePair> pairs;

    public DictETNode(ArrayList<ETNodePair> pairs) {
        this.pairs = pairs;

        for (ETNodePair p : this.pairs) {
            p.key.setParent(this);
            p.value.setParent(this);
        }
    }

    public DictETNode() {
        this.pairs = new ArrayList<ETNodePair>();
    }

    public void put(ETNode key, ETNode value) {
        this.pairs.add(new ETNodePair(key, value));
        key.setParent(this);
        value.setParent(this);
    }

    @Override
    public CDT execute(CraterVariableScope scope) {
        CDict dict = new CDict();

        for (ETNodePair p : this.pairs) {
            dict.put(p.key.executeMetaSafe(scope), p.value.executeMetaSafe(scope));
        }

        return dict;
    }
}
