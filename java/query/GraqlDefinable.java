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

import graql.lang.common.GraqlToken;
import graql.lang.common.exception.ErrorMessage;
import graql.lang.common.exception.GraqlException;
import graql.lang.pattern.Definable;
import graql.lang.pattern.schema.Rule;
import graql.lang.pattern.variable.TypeVariable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static graql.lang.common.GraqlToken.Char.NEW_LINE;
import static graql.lang.common.GraqlToken.Char.SEMICOLON;
import static graql.lang.common.GraqlToken.Command.DEFINE;
import static graql.lang.common.GraqlToken.Command.UNDEFINE;
import static graql.lang.common.exception.ErrorMessage.MISSING_DEFINABLES;
import static java.util.stream.Collectors.joining;

abstract class GraqlDefinable extends GraqlQuery {

    private final GraqlToken.Command keyword;
    private final List<TypeVariable> typeVariables = new ArrayList<>();
    private final List<Rule> rules = new ArrayList<>();
    private final int hash;

    GraqlDefinable(final GraqlToken.Command keyword, final List<Definable> definables) {
        assert keyword == DEFINE || keyword == UNDEFINE;
        if (definables == null || definables.isEmpty()) throw GraqlException.of(MISSING_DEFINABLES.message());
        final LinkedList<TypeVariable> typeVars = new LinkedList<>();
        for (Definable definable : definables) {
            if (definable.isRule()) rules.add(definable.asRule());
            if (definable.isTypeVariable()) typeVars.add(definable.asTypeVariable());
        }
        while (!typeVars.isEmpty()) {
            final TypeVariable v = typeVars.removeFirst();
            if (!v.isLabelled()) throw GraqlException.of(ErrorMessage.INVALID_DEFINE_QUERY_VARIABLE.message());
            else v.constraints().forEach(c -> typeVars.addAll(c.variables()));
            typeVariables.add(v);
        }

        this.keyword = keyword;
        this.hash = Objects.hash(this.keyword, this.typeVariables, this.rules);
    }

    public final List<TypeVariable> variables() {
        return typeVariables;
    }
    public final List<Rule> rules() {
        return rules;
    }

    @Override
    public final String toString() {
        final StringBuilder query = new StringBuilder();
        query.append(keyword);

        if (typeVariables.size() + rules.size() > 1) query.append(NEW_LINE);
        else query.append(GraqlToken.Char.SPACE);

        query.append(variables().stream().map(TypeVariable::toString).collect(joining("" + SEMICOLON + NEW_LINE)));
        query.append(rules().stream().map(Rule::toString).collect(joining("" + SEMICOLON + NEW_LINE)));
        query.append(SEMICOLON);
        return query.toString();
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GraqlDefinable that = (GraqlDefinable) o;
        return this.keyword.equals(that.keyword) && this.typeVariables.equals(that.typeVariables) && this.rules.equals(that.rules);
    }

    @Override
    public final int hashCode() {
        return hash;
    }
}
