package NativeDataTypes;

import BuiltinFunctions.CBuiltinMemberFunction;
import CraterExecutionEnvironment.CraterVariableScope;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/13/14.
 */
public class CRange extends CDT{

    public CDT head, tail, increment;
    private boolean include, backwards;

    public CRange(CDT head, CDT tail, CDT increment, boolean include) {
        this.head = head;
        this.tail = tail;
        this.increment = increment;
        this.include = include;
        this.backwards = head.siGreaterThan(tail).toBool();
    }

    public CList generateList() {
        CList list = new CList();
        CDT value = this.head.clone();
        list.addCDT(value);
        while (true) {
            if (!this.siContains(value).toBool()) break;
            value = value.siPlus(this.increment);
            list.addCDT(value);
        }
        return list;
    }

    public int length() {
        int len = 0;
        return len;
    }

    @Override
    public CDT siContains(CDT other) {
        if (other instanceof CInteger || other instanceof CFloat) {
            if (this.backwards) {
                if (this.include) {
                    return new CBoolean(other.siLessThanOrEqual(this.head).toBool() && other.siGreaterThanOrEqual(this.tail).toBool());
                } else {
                    return new CBoolean(other.siLessThanOrEqual(this.head).toBool() && other.siGreaterThan(this.tail).toBool());
                }
            } else {
                if (this.include) {
                    return new CBoolean(other.siGreaterThanOrEqual(this.head).toBool() && other.siLessThanOrEqual(this.tail).toBool());
                } else {
                    return new CBoolean(other.siGreaterThanOrEqual(this.head).toBool() && other.siLessThan(this.tail).toBool());
                }
            }
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
