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

package graql.lang.query.test;

import graql.lang.Graql;
import graql.lang.query.GraqlInsert;
import graql.lang.query.MatchClause;
import graql.lang.statement.Statement;
import graql.lang.util.Collections;
import org.junit.Test;

import java.util.Set;

import static graql.lang.Graql.var;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class GraqlInsertTest {

    private final MatchClause match1 = Graql.match(var("x").isa("movie"));
    private final MatchClause match2 = Graql.match(var("y").isa("movie"));

    private final Set<Statement> vars1 = Collections.set(var("x"));
    private final Set<Statement> vars2 = Collections.set(var("y"));

    @Test
    public void insertQueriesWithTheSameVarsAndQueryAreEqual() {
        GraqlInsert query1 = new GraqlInsert(match1, Collections.list(vars1));
        GraqlInsert query2 = new GraqlInsert(match1, Collections.list(vars1));

        assertEquals(query1, query2);
        assertEquals(query1.hashCode(), query2.hashCode());
    }

    @Test
    public void insertQueriesWithTheSameVarsAndGraphAreEqual() {
        GraqlInsert query1 = new GraqlInsert(null, Collections.list(vars1));
        GraqlInsert query2 = new GraqlInsert(null, Collections.list(vars1));

        assertEquals(query1, query2);
        assertEquals(query1.hashCode(), query2.hashCode());
    }

    @Test
    public void insertQueriesWithDifferentMatchesAreDifferent() {
        GraqlInsert query1 = new GraqlInsert(match1, Collections.list(vars1));
        GraqlInsert query2 = new GraqlInsert(match2, Collections.list(vars1));

        assertNotEquals(query1, query2);
    }

    @Test
    public void insertQueriesWithDifferentVarsAreDifferent() {
        GraqlInsert query1 = new GraqlInsert(match1, Collections.list(vars1));
        GraqlInsert query2 = new GraqlInsert(match1, Collections.list(vars2));

        assertNotEquals(query1, query2);
    }
}