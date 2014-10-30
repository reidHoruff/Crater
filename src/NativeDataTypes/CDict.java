package NativeDataTypes;

import Exceptions.CraterInternalException;

import java.util.HashMap;

/**
 * Created by reidhoruff on 10/14/14.
 */
public class CDict extends AbstractCIndexable {

    public HashMap<CDT, MetaCDT> dictionary;

    public CDict() {
        this.dictionary = new HashMap<CDT, MetaCDT>();
    }

    public MetaCDT put(CDT key, CDT value) {
        MetaCDT wrapper = value.withMetaWrapper();
        this.dictionary.put(key, wrapper);
        return wrapper;
    }

    public MetaCDT directPut(CDT key, MetaCDT value) {
        this.dictionary.put(key, value);
        return value;
    }

    public CDT get(CDT key) {
        if (this.dictionary.containsKey(key)) {
            return this.dictionary.get(key);
        }

        /**
         * the key doesn't exist so we send a metaCDT
         * with the ability to create the key
         * should the caller want to (meaning that it was called for
         * the left hand side)
         */
        return new DictAssignMetaCDT(this, key);
    }

    @Override
    public String getTypeName() {
        return "dict";
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        for (CDT key : this.dictionary.keySet()) {
            if (builder.length() > 1) {
                builder.append(", ");
            }
            builder.append(key.toString());
            builder.append(": ");
            builder.append(this.dictionary.get(key).toString());
        }

        builder.append("}");
        return builder.toString();
    }

    public int size() {
        return this.dictionary.size();
    }

    /**
     * operations
     */
    @Override
    public CDT siIndex(CDT index) {
        if (index instanceof CRange) {
            return super.siIndex(index);
        }

        return this.get(index);
    }

    @Override
    public CDT siContains(CDT other) {
        return new CBoolean(this.dictionary.containsKey(other));
    }
}
