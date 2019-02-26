/*
 * GRAKN.AI - THE KNOWLEDGE GRAPH
 * Copyright (C) 2018 Grakn Labs Ltd
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
import graql.lang.statement.StatementType;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Represents the {@code relates} property on a RelationType.
 * This property can be queried, inserted or deleted.
 * This property relates a RelationType and a Role. It indicates that a Relation whose
 * type is this RelationType may have a role-player playing the given Role.
 */
public class RelatesProperty extends VarProperty {

    private final Statement role;
    private final Statement superRole;

    public RelatesProperty(Statement role, @Nullable Statement superRole) {
        if (role == null) {
            throw new NullPointerException("Null role");
        }
        this.role = role;
        this.superRole = superRole;
    }

    public Statement role() {
        return role;
    }

    public Statement superRole() {
        return superRole;
    }

    @Override
    public String keyword() {
        return Graql.Token.Property.RELATES.toString();
    }

    @Override
    public String property() {
        StringBuilder builder = new StringBuilder(role.getPrintableName());
        if (superRole != null) {
            builder.append(" as ").append(superRole.getPrintableName());
        }
        return builder.toString();
    }

    @Override
    public boolean isUnique() {
        return false;
    }

    @Override
    public Stream<Statement> types() {
        return Stream.of(role);
    }

    @Override
    public Stream<Statement> statements() {
        return superRole == null ? Stream.of(role) : Stream.of(role, superRole);
    }

    @Override
    public Class statementClass() {
        return StatementType.class;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof RelatesProperty) {
            RelatesProperty that = (RelatesProperty) o;
            return (this.role.equals(that.role) &&
                    Objects.equals(this.superRole, that.superRole));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = 1;
        h *= 1000003;
        h ^= this.role.hashCode();
        h *= 1000003;
        h ^= (superRole == null) ? 0 : this.superRole.hashCode();
        return h;
    }
}
