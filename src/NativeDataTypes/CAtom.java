package NativeDataTypes;

import Scanning.Token;

/**
 * Created by reidhoruff on 10/15/14.
 */
public class CAtom extends CDT {

    public String name;

    public CAtom(String name) {
        this.name = name;
    }

    public CAtom(Token token) {
        this.name = token.sequence.substring(1);
    }

    @Override
    public String toString() {
        return "@" + this.name;
    }

    @Override
    public String getTypeName() {
        return "atom";
    }

    /**
     * operations
     */

    @Override
    public CDT siMutuallyEqualTo(CDT other) {
        if (other instanceof CAtom) {
            return new CBoolean(this.name.equals(((CAtom) other).name));
        }
        return new CBoolean(false);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
