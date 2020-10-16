package NativeDataTypes;

import Exceptions.CraterExecutionException;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/20/14.
 */
public class CTuple extends AbstractCIndexable {

    private ArrayList<MetaCDT> items;

    public CTuple() {
        super();
        this.items = new ArrayList<MetaCDT>();
    }

    public CTuple addCDT(CDT item) {
        this.items.add(new MetaCDT(item));
        return this;
    }

    public CDT length() {
        return CInteger.gimmie(this.items.size());
    }

    @Override
    public CDT clone() {
        CTuple tuple = new CTuple();
        for (CDT item : this.items) {
            tuple.addCDT(item.clone());
        }
        return tuple;
    }

    public static CTuple combineToFormNew(CTuple a, CTuple b) {
        CTuple list = new CTuple();
        for (CDT item : a.items) {
            list.addCDT(item.clone());
        }
        for (CDT item : b.items) {
            list.addCDT(item.clone());
        }
        return list;
    }

    public String toString() {
        String s = "(";// + Integer.toString(items.size()) + "|";
        for (int i = 0; i < items.size(); i++) {
            s += items.get(i).toString();
            if (i < items.size() - 1) {
                s += ", ";
            }
        }
        return s + ")";
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
        return new CBoolean(false);
    }

    @Override
    public CDT siPlus(CDT other) {
        CTuple newtuple = (CTuple)this.clone();
        if (other instanceof CTuple) {
            CTuple otherTuple = other.toCTuple();
            for (CDT item : otherTuple.items) {
                newtuple.items.add(item.metaSafe().withMetaWrapper());
            }
            return newtuple;
        }
        return super.siPlus(other);
    }

    @Override
    public CDT siIndex(CDT index) {
        if (index instanceof CInteger) {
            return this.singleValueListAccess((int)index.toInt());
        }
        return super.siIndex(index);
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
        return "tuple";
    }
}
