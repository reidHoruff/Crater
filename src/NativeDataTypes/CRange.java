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

    public CList generateList() {
        CList list = new CList();
        for (int i = this.head; i < tail; i += this.increment) {
            list.addCDT(new CInteger(i));
        }
        return list;
    }

    public int length() {
        int len = 0;
        for (int i = this.head; i < this.tail; i += this.increment) {
            len += 1;
        }
        return len;
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
