/*
 * GRAKN.AI - THE KNOWLEDGE GRAPH
 * Copyright (C) 2019 Grakn Labs Ltd
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

import com.google.common.collect.Sets;
import graql.lang.Graql;
import graql.lang.statement.Statement;
import graql.lang.statement.Variable;
import graql.lang.util.Collections;

import javax.annotation.CheckReturnValue;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * A class representing a disjunction (or) of patterns. Any inner pattern must match in a query
 *
 * @param <T> the type of patterns in this disjunction
 */
public class Disjunction<T extends Pattern> implements Pattern {

    private final LinkedHashSet<T> patterns;

    public Disjunction(Set<T> patterns) {
        if (patterns == null) {
            throw new NullPointerException("Null patterns");
        }
        this.patterns = patterns.stream()
                .map(Objects::requireNonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disjunction<?> that = (Disjunction<?>) o;
        return Objects.equals(patterns, that.patterns);
    }

    @Override
    public int hashCode() {
        return patterns.hashCode();
    }

    /**
     * @return the patterns within this disjunction
     */
    @CheckReturnValue
    public Set<T> getPatterns() {
        return patterns;
    }

    @Override
    public Disjunction<Conjunction<Statement>> getDisjunctiveNormalForm() {
        // Concatenate all disjunctions into one big disjunction
        Set<Conjunction<Statement>> dnf = getPatterns().stream()
                .flatMap(p -> p.getDisjunctiveNormalForm().getPatterns().stream())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Graql.or(dnf);
    }

    @Override
    public Disjunction<Conjunction<Pattern>> getNegationDNF() {
        Set<Conjunction<Pattern>> dnf = getPatterns().stream()
                .flatMap(p -> p.getNegationDNF().getPatterns().stream())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return Graql.or(dnf);
    }

    @Override
    public Set<Variable> variables() {
        return getPatterns().stream().map(Pattern::variables).reduce(Sets::intersection).orElse(Collections.set());
    }

    @Override
    public String toString() {
        StringBuilder disjunction = new StringBuilder();

        Iterator<T> patternIter = patterns.iterator();
        while (patternIter.hasNext()) {
            Pattern pattern = patternIter.next();
            disjunction.append(Graql.Token.Char.CURLY_OPEN).append(Graql.Token.Char.SPACE);

            if (pattern instanceof Conjunction<?>) {
                disjunction.append(((Conjunction<? extends Pattern>) pattern).getPatterns().stream()
                                           .map(Object::toString)
                                           .collect(Collectors.joining(Graql.Token.Char.SPACE.toString())));
            } else {
                disjunction.append(pattern.toString());
            }

            disjunction.append(Graql.Token.Char.SPACE).append(Graql.Token.Char.CURLY_CLOSE);

            if (patternIter.hasNext()) {
                disjunction.append(Graql.Token.Char.SPACE).append(Graql.Token.Operator.OR).append(Graql.Token.Char.SPACE);
            }
        }

        disjunction.append(Graql.Token.Char.SEMICOLON);

        return disjunction.toString();
    }
}
