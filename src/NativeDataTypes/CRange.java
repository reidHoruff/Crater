package NativeDataTypes;

import BuiltinFunctions.CBuiltinMemberFunction;
import CraterExecutionEnvironment.CraterVariableScope;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/13/14.
 */
public class CRange extends CDT{

    public CDT head, tail, increment;

    public CRange(CDT head, CDT tail, CDT increment) {
        this.head = head;
        this.tail = tail;
        this.increment = increment;
    }

    public CList generateList() {
        CList list = new CList();
        return list;
    }

    public int length() {
        int len = 0;
        return len;
    }

    @Override
    public CDT siContains(CDT other) {
        if (other instanceof CInteger || other instanceof CFloat) {
            return new CBoolean(other.siGreaterThanOrEqual(this.head).toBool() && other.siLessThan(this.tail).toBool());
        }
        return super.siContains(other);
    }

    @Override
    public CDT siAccessMember(String identifier, CraterVariableScope accessor) {
        /**
         * expand()
         * generates list
         */
        if (identifier.equals("expand")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values) {
                    return ((CRange)this.host).generateList();
                }
            };
        }
        return super.siAccessMember(identifier, accessor);
    }

    @Override
    public String toString() {
        String s = "";
        s += this.head.toString();
        s += "..";
        s += this.tail.toString();
        s += " by ";
        s += this.increment.toString();
        return s;
    }

    @Override
    public String getTypeName() {
        return "range";
    }
}
