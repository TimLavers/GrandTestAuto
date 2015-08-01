package org.grandtestauto;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class Name implements Comparable<Name> {
    private String[] parts;
    private String separator;

    public static Name dotSeparatedName( String name ) {
        return new Name( name.split( "\\." ), "." );
    }

    public static Name camelCaseName( String name ) {
        assert name.length() > 0: "Name should not be empty.";
        List<String> pieces = new LinkedList<>();
        int left = 0;
        int right = -1;
        int i = 0;
        for (char c : name.toCharArray()) {
            if (Character.isUpperCase( c )) {
                if (right >= 0) {
                    pieces.add( name.substring( left, right ) );
                }
                left = i;
            }
            i++;
            right = i;
        }
        pieces.add( name.substring( left, right ) );
        return new Name( pieces.toArray( new String[pieces.size()] ), "" );
    }

    private Name( String[] parts, String separator ) {
        this.parts = parts;
        this.separator = separator;
    }

    public Name( Name base, String newPart ) {//todo as above
        parts = new String[base.parts.length + 1];
        System.arraycopy( base.parts, 0, parts, 0, base.parts.length );
        parts[parts.length - 1] = newPart;
        separator = base.separator;
    }

    public boolean matches( Name other ) {
        //Cannot match if the other has fewer parts.
        if (other.parts.length < parts.length) return false;

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            String otherPart = other.parts[i];
            if (!otherPart.startsWith( part )) return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder( "" );
        boolean first = true;
        for (String part : parts) {
            if (first) {
                first = false;
            } else {
                sb.append( separator );
            }
            sb.append( part );
        }
        return sb.toString();
    }

    public int compareTo( @NotNull Name o ) {
        int n = Math.min( parts.length, o.parts.length );
        int i = 0;
        while (i < n) {
            int diff = parts[i].compareTo( o.parts[i] );
            i++;
            if (diff != 0) return diff;
        }
        //To get to this point, the names are equal along their shared length,
        //so return the difference in lengths.
        return parts.length - o.parts.length;
    }
}
