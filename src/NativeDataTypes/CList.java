package NativeDataTypes;

import BuiltinFunctions.CBuiltinMemberFunction;
import CraterExecutionEnvironment.CraterVariableScope;
import Exceptions.CraterExecutionException;
import ExecutionTree.ETNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
        return CInteger.gimmie(this.items.size());
    }

    public ArrayList<MetaCDT> getItems() {
        return items;
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
    public CDT siIndex(CDT index) {
        if (index instanceof CInteger) {
            return this.singleValueListAccess((int)index.toInt());
        }
        return super.siIndex(index);
    }

    @Override
    public CDT siAccessMember(String identifier, CraterVariableScope accessor) {
        if (identifier.equals("length")) {
            return this.length();
        }

        if (identifier.equals("append")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {
                    for (CDT value : values) {
                        this.host.siPlusEquals(value);
                    }
                    return this.host;
                }
            };
        }

        if (identifier.equals("map")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {
                    CDT lam = values.get(0);
                    CList map = new CList();
                    for (CDT value : ((CList)this.host).getItems()) {
                        map.addCDT(lam.callWithSingleArgument(value.metaSafe(), parent));
                    }
                    return map;
                }
            };
        }

        if (identifier.equals("each")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {
                    CDT lam = values.get(0);
                    for (CDT value : ((CList)this.host).getItems()) {
                        lam.toCFunction().callWithSingleArgument(value, parent);
                    }
                    return CNone.get();
                }
            };
        }

        if (identifier.equals("empty")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {
                    return new CBoolean(((CList)this.host).isEmpty());
                }
            };
        }

        if (identifier.equals("sort")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {
                    ((CList)this.host).sort();
                    return this.host;
                }
            };
        }

        if (identifier.equals("head")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {
                    return ((CList)this.host).head();
                }
            };
        }

        if (identifier.equals("tail")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {
                    return ((CList)this.host).tail();
                }
            };
        }

        if (identifier.equals("shuffle")) {
            return new CBuiltinMemberFunction(this) {
                @Override
                public CDT callWithArguments(ArrayList<CDT> values, ETNode parent) {
                    ((CList)this.host).shuffle();
                    return this.host;
                }
            };
        }

        return super.siAccessMember(identifier, accessor);
    }

    public void shuffle() {
        Collections.shuffle(this.items);
    }

    public void sort() {
        Collections.sort(this.items, new Comparator<MetaCDT>() {
            @Override
            public int compare(MetaCDT o1, MetaCDT o2) {
                return (int)o1.metaSafe().siCompareTo(o2.metaSafe()).toInt();
            }
        });
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public CList rangeListAccess(CRange range) {
        CList newList = new CList();
        /*
        for (int x = range.head; x < range.tail; x += range.increment) {
            newList.addCDT(this.getElementMetaSafe(x).clone());
        }
        */
        return newList;
    }

    public CDT head() {
        return singleValueListAccess(0);
    }

    public CDT tail() {
        return singleValueListAccess(this.getLength()-1);
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
