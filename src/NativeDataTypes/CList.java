package NativeDataTypes;

import BuiltinFunctions.CBuiltinMemberFunction;
import Exceptions.CraterExecutionException;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/8/14.
 */
public class CList extends AbstractCIndexable {

    private ArrayList<MetaCDT> items;

    public CList() {
        super();
        this.items = new ArrayList<MetaCDT>();
    }

    public CList addCDT(CDT item) {
        this.items.add(item.withMetaWrapper());
        return this;
    }

    public CDT length() {
        return new CInteger(this.items.size());
    }

    @Override
    public CDT clone() {
        CList list = new CList();
        for (CDT item : this.items) {
            list.addCDT(item.clone());
        }
        return list;
    }

    public static CList combineToFormNew(CList a, CList b) {
        CList list = new CList();
        for (CDT item : a.items) {
            list.addCDT(item.clone());
        }
        for (CDT item : b.items) {
            list.addCDT(item.clone());
        }
        return list;
    }

    public String toString() {
        String s = "[";// + Integer.toString(items.size()) + "|";
        for (int i = 0; i < items.size(); i++) {
            s += items.get(i).toString();
            if (i < items.size() - 1) {
                s += ", ";
            }
        }
        return s + "]";
    }

    public int getLength() {
        return this.items.size();
    }

    public CDT getElementWithMeta(int index) {
        return this.items.get(index);
    }

    public CDT getElementMetaSafe(int index) {
        return this.items.get(index).metaSafe();
    }

    /**
     * CDT simple operations
     */

    @Override
    public CDT siContains(CDT other) {
        for (CDT element : this.items) {
            if (element.siMutuallyEqualTo(other).toBool()) {
                return new CBoolean(true);
            }
        }
        return  new CBoolean(false);
    }

    @Override
    public CDT siPlus(CDT other) {
        CList newList = (CList)this.clone();
        if (other instanceof CList) {
            CList otherlist = other.toCList();
            for (CDT item : otherlist.items) {
                newList.items.add(item.clone().withMetaWrapper());
            }
            return newList;
        }
        return super.siPlus(other);
    }

    @Override
    public CDT siPlusEquals(CDT other) {
        this.addCDT(other);
        return this;
    }

    @Override
    public CDT siIndex(CDT index) {
        if (index instanceof CInteger) {
            return this.singleValueListAccess(index.toInt());
        }
        return super.siIndex(index);
    }

    @Override
    public CDT siAccessMember(String identifier) {
        if (identifier.equals("length")) {
            return this.length();
        }

        if (identifier.equals("append")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values) {
                    for (CDT value : values) {
                        this.host.siPlusEquals(value);
                    }
                    return this.host;
                }
            };
        }

        return super.siAccessMember(identifier);
    }

    public CList rangeListAccess(CRange range) {
        CList newList = new CList();
        for (int x = range.head; x < range.tail; x += range.increment) {
            newList.addCDT(this.getElementMetaSafe(x).clone());
        }
        return newList;
    }

    public CDT singleValueListAccess(int index) {
        if (index < 0 || index >= this.getLength()) {
            throw new CraterExecutionException("index out of bounds");
        }

        return this.getElementWithMeta(index);
    }

    /**
     * end CDT simple operations
     */

    public String getTypeName() {
        return "list";
    }
}
