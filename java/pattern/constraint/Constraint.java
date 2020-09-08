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

package graql.lang.pattern.constraint;

import graql.lang.common.exception.GraqlException;
import graql.lang.pattern.variable.BoundVariable;

import java.util.Set;

import static grakn.common.util.Objects.className;
import static graql.lang.common.exception.ErrorMessage.INVALID_CASTING;

public abstract class Constraint<VARIABLE extends BoundVariable> {

    public abstract Set<VARIABLE> variables();

    public boolean isType() {
        return false;
    }

    public boolean isThing() {
        return false;
    }

    public TypeConstraint asType() {
        throw GraqlException.of(INVALID_CASTING.message(className(this.getClass()), className(TypeConstraint.class)));
    }

    public ThingConstraint asThing() {
        throw GraqlException.of(INVALID_CASTING.message(className(this.getClass()), className(ThingConstraint.class)));
    }

    @Override
    public abstract String toString();
}
