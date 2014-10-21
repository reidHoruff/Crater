package CraterExecutionEnvironment;

import NativeDataTypes.CDT;
import Scanning.Token;

import java.util.ArrayList;

/**
 * Created by reidhoruff on 10/20/14.
 */
public class FunctionCallStackFrame {

    private final Token locationToken;
    private ArrayList<String> typeNames;

    public FunctionCallStackFrame(Token location) {
        this.locationToken = location;
        this.typeNames = new ArrayList<String>();
    }

    public void addParameterCallingType(CDT parameter) {
        this.typeNames.add(parameter.getTypeName());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");

        for (String typeNames : this.typeNames) {
            if (sb.length() > 1) {
                sb.append(", ");
            }

            sb.append(typeNames);
        }

        sb.append(")\n");

        if (this.locationToken != null) {
            sb.append(locationToken.toString());
        }

        return sb.toString();
    }
}
