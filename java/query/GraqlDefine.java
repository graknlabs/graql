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

package graql.lang.query;

import graql.lang.Graql;
import graql.lang.variable.TypeVariable;

import java.util.List;
import java.util.Objects;

import static graql.lang.Graql.Token.Char.NEW_LINE;
import static graql.lang.Graql.Token.Char.SEMICOLON;
import static java.util.stream.Collectors.joining;

public class GraqlDefine extends GraqlQuery {

    private final List<TypeVariable> variables;
    private final int hash;

    public GraqlDefine(List<TypeVariable> variables) {
        if (variables == null || variables.isEmpty()) {
            throw new IllegalArgumentException("Define Query missing type variables");
        }
        this.variables = variables;
        this.hash = Objects.hash(this.variables);
    }

    public List<TypeVariable> variables() {
        return variables;
    }

    @Override
    public String toString() {
        StringBuilder query = new StringBuilder();
        query.append(Graql.Token.Command.DEFINE);

        if (variables.size() > 1) query.append(NEW_LINE);
        else query.append(Graql.Token.Char.SPACE);

        query.append(variables().stream().map(TypeVariable::toString).collect(joining("" + SEMICOLON + NEW_LINE)));
        query.append(SEMICOLON);
        return query.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraqlDefine that = (GraqlDefine) o;
        return this.variables.equals(that.variables);
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
