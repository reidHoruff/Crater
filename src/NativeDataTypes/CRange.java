package NativeDataTypes;

import BuiltinFunctions.CBuiltinMemberFunction;
import CraterExecutionEnvironment.CraterVariableScope;
import ExecutionTree.ETNode;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/13/14.
 */
public class CRange extends CDT{

    private CDT head, tail, increment, lazyHead, lazyTail;
    private boolean include, backwards;

    public CRange(CDT head, CDT tail, CDT increment, boolean include) {
        this.head = head;
        this.tail = tail;
        this.increment = increment;
        this.include = include;

        if (head instanceof EndCDT) {
            this.lazyHead = head;
        }

        else if (tail instanceof EndCDT) {
            this.lazyTail = head;
        }

        else {
            this.backwards = head.siGreaterThan(tail).toBool();
        }
    }

    public boolean isLazy() {
        return this.lazyHead != null || this.lazyTail != null;
    }

    public CDT getHead() {
        return this.head;
    }

    public CDT getTail() {
        return this.tail;
    }

    public void lazyRealize(CDT value) {
        if (this.lazyHead != null) {
            this.head = value;
        }

        else if (this.lazyTail != null) {
            this.tail = value;
        }

        this.backwards = head.siGreaterThan(tail).toBool();
    }

    public CDT getActualIncrement() {
        if (this.backwards) {
            return this.increment.siMultiply(CInteger.gimmie(-1));
        }
        else {
            return this.increment;
        }
    }

    public CList generateList() {
        CList list = new CList();
        CDT value = this.head.clone();
        CDT inc = this.getActualIncrement();

        while (true) {
            if (!this.siContains(value).toBool()) break;
            list.addCDT(value);
            value = value.siPlus(inc);
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
                public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {
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
