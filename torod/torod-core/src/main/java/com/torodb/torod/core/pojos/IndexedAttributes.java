
package com.torodb.torod.core.pojos;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.torodb.torod.core.language.AttributeReference;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public class IndexedAttributes implements Serializable {
    private static final long serialVersionUID = 1L;
    private final ImmutableList<AttributeReference> attributes;
    private final ImmutableMap<AttributeReference, Boolean> orderingInfo;

    private IndexedAttributes(
            ImmutableList<AttributeReference> attributes,
            ImmutableMap<AttributeReference, Boolean> attToIndex) {
        this.orderingInfo = attToIndex;
        this.attributes = attributes;
    }

    public List<AttributeReference> getIndexedAttributes() {
        return attributes;
    }

    public boolean ascendingOrdered(AttributeReference attRef) {
        Boolean ascendigOrdered = orderingInfo.get(attRef);
        if (ascendigOrdered == null) {
            throw new IllegalArgumentException("Attribute " + attRef +
                    " is not indexed by this index");
        }
        return ascendigOrdered;
    }

    public boolean contains(AttributeReference attRef) {
        return orderingInfo.containsKey(attRef);
    }

    public Iterable<Map.Entry<AttributeReference, Boolean>> entrySet() {
        return orderingInfo.entrySet();
    }

    public int size() {
        return attributes.size();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash +
                (this.orderingInfo != null ? this.orderingInfo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IndexedAttributes other = (IndexedAttributes) obj;
        if (this.attributes != other.attributes &&
                (this.attributes == null ||
                !this.attributes.equals(other.attributes))) {
            return false;
        }
        if (this.orderingInfo != other.orderingInfo &&
                (this.orderingInfo == null ||
                !this.orderingInfo.equals(other.orderingInfo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (AttributeReference attribute : attributes) {
            sb.append(attribute).append(' ');
            if (orderingInfo.get(attribute)) {
                sb.append("(asc)");
            }
            else {
                sb.append("(desc)");
            }
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        
        return sb.toString();
    }

    public static class Builder {

        private final ImmutableList.Builder<AttributeReference> attributes;
        private final ImmutableMap.Builder<AttributeReference, Boolean> orderingInfo;

        public Builder() {
            attributes = ImmutableList.builder();
            orderingInfo = ImmutableMap.builder();
        }

        public Builder addAttribute(AttributeReference attRef, boolean ascendingOrder) {
            attributes.add(attRef);
            orderingInfo.put(attRef, ascendingOrder);
            return this;
        }

        public IndexedAttributes build() {
            return new IndexedAttributes(
                    attributes.build(),
                    orderingInfo.build()
            );
        }
    }

}
