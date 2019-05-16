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

package graql.lang.pattern;

import graql.lang.Graql;
import graql.lang.statement.Statement;
import graql.lang.statement.Variable;

import java.util.Objects;
import javax.annotation.CheckReturnValue;
import java.util.Collections;
import java.util.Set;

/**
 * A class representing a negation of patterns. All inner patterns must not match in a query.
 *
 * @param <T> the type of patterns in this negation
 */
public class Negation<T extends Pattern> implements Pattern {

    private final T pattern;

    public Negation(T pattern) {
        if (pattern == null) {
            throw new NullPointerException("Null patterns");
        }
        this.pattern = pattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Negation<?> negation = (Negation<?>) o;
        return Objects.equals(pattern, negation.pattern);
    }

    @Override
    public int hashCode() {
        return pattern.hashCode();
    }

    @CheckReturnValue
    public T getPattern(){ return pattern;}

    @Override
    public Disjunction<Conjunction<Statement>> getDisjunctiveNormalForm() {
        return pattern.getDisjunctiveNormalForm();
    }

    @Override
    public Disjunction<Conjunction<Pattern>> getNegationDNF() {
        if(pattern.isNegation()) return pattern.asNegation().getPattern().getNegationDNF();
        return Graql.or(Collections.singleton(Graql.and(Collections.singleton(this))));
    }

    @Override
    public boolean isNegation() { return true; }

    @Override
    public Negation<?> asNegation() { return this; }

    @Override
    public Set<Variable> variables() {
        return pattern.variables();
    }

    @Override
    public String toString() {
        StringBuilder negation = new StringBuilder();
        negation.append(Graql.Token.Operator.NOT).append(Graql.Token.Char.SPACE);

        if (pattern instanceof Conjunction<?>) {
            negation.append(pattern.toString());
        } else {
            negation.append(Graql.Token.Char.CURLY_OPEN).append(Graql.Token.Char.SPACE);
            negation.append(pattern.toString());
            negation.append(Graql.Token.Char.SPACE).append(Graql.Token.Char.CURLY_CLOSE);
            negation.append(Graql.Token.Char.SEMICOLON);
        }

        return negation.toString();
    }
}

