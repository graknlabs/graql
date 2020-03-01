/*
 * Copyright (C) 2020 Grakn Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package graql.lang.property;

import graql.lang.Graql;
import graql.lang.statement.Statement;
import graql.lang.statement.StatementInstance;

import java.util.stream.Stream;

/**
 * Represents the {@code isa} property on a Thing.
 * This property can be queried and inserted.
 * THe property is defined as a relation between an Thing and a Type.
 * When matching, any subtyping is respected. For example, if we have {@code $bob isa man}, {@code man sub person},
 * {@code person sub entity} then it follows that {@code $bob isa person} and {@code bob isa entity}.
 */
public class IsaProperty extends VarProperty {

    private final Statement type;
    private final boolean explicit;

    public IsaProperty(Statement type) {
        this(type, false);
    }

    public IsaProperty(Statement type, boolean explicit) {
        if (type == null) {
            throw new NullPointerException("Null type");
        }
        this.type = type;
        this.explicit = explicit;
    }

    public Statement type() {
        return type;
    }

    @Override
    public String keyword() {
        if (!explicit) {
            return Graql.Token.Property.ISA.toString();
        } else {
            return Graql.Token.Property.ISAX.toString();
        }
    }

    @Override
    public final String property() {
        return type().getPrintableName();
    }

    @Override
    public boolean isExplicit() {
        return explicit;
    }

    @Override
    public final boolean isUnique() {
        return true;
    }

    @Override
    public final Stream<Statement> types() {
        return Stream.of(type());
    }

    @Override
    public final Stream<Statement> statements() {
        return Stream.of(type());
    }

    @Override
    public Class statementClass() {
        return StatementInstance.class;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IsaProperty that = (IsaProperty) o;

        // We ignore `directType` because the object is irrelevant
        return (this.type().equals(that.type()) &&
                this.isExplicit() == that.isExplicit());
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= this.keyword().hashCode();
        h *= 1000003;
        h ^= this.type().hashCode();
        return h;
    }
}
