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

package graql.lang.pattern.variable;

import graql.lang.pattern.constraint.Constraint;
import graql.lang.pattern.constraint.ThingConstraint;
import graql.lang.pattern.constraint.TypeConstraint;
import graql.lang.pattern.variable.builder.ThingVariableBuilder;
import graql.lang.pattern.variable.builder.TypeVariableBuilder;

import java.util.stream.Stream;

public class UnboundVariable extends Variable implements TypeVariableBuilder,
                                                         ThingVariableBuilder.Common<ThingVariable.Thing>,
                                                         ThingVariableBuilder.Thing,
                                                         ThingVariableBuilder.Relation,
                                                         ThingVariableBuilder.Attribute {

    UnboundVariable(Reference reference) {
        super(reference);
    }

    public static UnboundVariable of(Reference reference) {
        return new UnboundVariable(reference);
    }

    public static UnboundVariable named(String name) {
        return of(Reference.named(name));
    }

    public static UnboundVariable anonymous() {
        return of(Reference.anonymous(true));
    }

    public static UnboundVariable hidden() {
        return of(Reference.anonymous(false));
    }

    public TypeVariable toType() {
        return new TypeVariable(reference, null);
    }

    public ThingVariable<?> toThing() {
        return new ThingVariable.Thing(reference, null);
    }

    @Override
    public Stream<Constraint<?>> constraints() {
        return Stream.of();
    }

    @Override
    public TypeVariable asTypeWith(TypeConstraint.Singular constraint) {
        if (!isVisible() && constraint instanceof TypeConstraint.Label) {
            return new TypeVariable(Reference.label(((TypeConstraint.Label) constraint).scopedLabel()), constraint);
        } else {
            return new TypeVariable(reference, constraint);
        }
    }

    @Override
    public TypeVariable asTypeWith(TypeConstraint.Repeatable constraint) {
        return new TypeVariable(reference, constraint);
    }

    @Override
    public ThingVariable.Thing asSameThingWith(ThingConstraint.Singular constraint) {
        return new ThingVariable.Thing(reference, constraint);
    }

    @Override
    public ThingVariable.Thing asSameThingWith(ThingConstraint.Repeatable constraint) {
        return new ThingVariable.Thing(reference, constraint);
    }

    @Override
    public ThingVariable.Thing asThingWith(ThingConstraint.Singular constraint) {
        return new ThingVariable.Thing(reference, constraint);
    }

    @Override
    public ThingVariable.Attribute asAttributeWith(ThingConstraint.Value<?> constraint) {
        return new ThingVariable.Attribute(reference, constraint);
    }

    @Override
    public ThingVariable.Relation asRelationWith(ThingConstraint.Relation.RolePlayer rolePlayer) {
        return asRelationWith(new ThingConstraint.Relation(rolePlayer));
    }

    public ThingVariable.Relation asRelationWith(ThingConstraint.Relation constraint) {
        return new ThingVariable.Relation(reference, constraint);
    }

    @Override
    public String toString() {
        return reference.syntax();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnboundVariable that = (UnboundVariable) o;
        return this.reference.equals(that.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }
}
