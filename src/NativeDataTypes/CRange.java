package NativeDataTypes;

/**
 * Created by reidhoruff on 10/13/14.
 */
public class CRange extends CDT{

    public int head, tail, increment;

    public CRange(int head, int tail, int increment) {
        this.head = head;
        this.tail = tail;
        this.increment = increment;
    }

    @Override
    public String toString() {
        String s = "";
        s += Integer.toString(this.head);
        s += "..";
        s += Integer.toString(this.tail);
        s += " by ";
        s += Integer.toString(this.increment);
        return s;
    }

    @Override
    public String getTypeName() {
        return "range";
    }
}
