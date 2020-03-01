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

package graql.lang.statement.builder;

import graql.lang.Graql;
import graql.lang.property.ValueProperty;
import graql.lang.property.ValueProperty.Operation.Assignment;
import graql.lang.property.ValueProperty.Operation.Comparison;
import graql.lang.property.VarProperty;
import graql.lang.statement.Statement;
import graql.lang.statement.StatementAttribute;

import javax.annotation.CheckReturnValue;
import java.time.LocalDateTime;
import java.util.function.BiFunction;

public interface StatementAttributeBuilder extends StatementInstanceBuilder {

    // Attribute value assignment property

    @CheckReturnValue
    default StatementAttribute val(long value) {
        return operation(new Assignment.Number<>(value));
    }

    @CheckReturnValue
    default StatementAttribute val(double value) {
        return operation(new Assignment.Number<>(value));
    }

    @CheckReturnValue
    default StatementAttribute val(boolean value) {
        return operation(new Assignment.Boolean(value));
    }

    @CheckReturnValue
    default StatementAttribute val(String value) {
        return operation(new Assignment.String(value));
    }

    @CheckReturnValue
    default StatementAttribute val(LocalDateTime value) {
        return operation(new Assignment.DateTime(value));
    }

    // Attribute value equality property

    @CheckReturnValue
    default StatementAttribute eq(long value) {
        return eq(Comparison.Number::new, value);
    }

    @CheckReturnValue
    default StatementAttribute eq(double value) {
        return eq(Comparison.Number::new, value);
    }

    @CheckReturnValue
    default StatementAttribute eq(boolean value) {
        return eq(Comparison.Boolean::new, value);
    }

    @CheckReturnValue
    default StatementAttribute eq(String value) {
        return eq(Comparison.String::new, value);
    }

    @CheckReturnValue
    default StatementAttribute eq(LocalDateTime value) {
        return eq(Comparison.DateTime::new, value);
    }

    @CheckReturnValue
    default StatementAttribute eq(Statement variable) {
        return eq(Comparison.Variable::new, variable);
    }

    @CheckReturnValue
    default <T> StatementAttribute eq(BiFunction<Graql.Token.Comparator, T, Comparison<T>> constructor, T value) {
        return operation(constructor.apply(Graql.Token.Comparator.EQV, value));
    }

    // Attribute value inequality property

    @CheckReturnValue
    default StatementAttribute neq(long value) {
        return neq(Comparison.Number::new, value);
    }

    @CheckReturnValue
    default StatementAttribute neq(double value) {
        return neq(Comparison.Number::new, value);
    }

    @CheckReturnValue
    default StatementAttribute neq(boolean value) {
        return neq(Comparison.Boolean::new, value);
    }

    @CheckReturnValue
    default StatementAttribute neq(String value) {
        return neq(Comparison.String::new, value);
    }

    @CheckReturnValue
    default StatementAttribute neq(LocalDateTime value) {
        return neq(Comparison.DateTime::new, value);
    }

    @CheckReturnValue
    default StatementAttribute neq(Statement variable) {
        return neq(Comparison.Variable::new, variable);
    }

    @CheckReturnValue
    default <T> StatementAttribute neq(BiFunction<Graql.Token.Comparator, T, Comparison<T>> constructor, T value) {
        return operation(constructor.apply(Graql.Token.Comparator.NEQV, value));
    }

    // Attribute value greater-than property

    @CheckReturnValue
    default StatementAttribute gt(long value) {
        return gt(Comparison.Number::new, value);
    }

    @CheckReturnValue
    default StatementAttribute gt(double value) {
        return gt(Comparison.Number::new, value);
    }

    @CheckReturnValue
    default StatementAttribute gt(boolean value) {
        return gt(Comparison.Boolean::new, value);
    }

    @CheckReturnValue
    default StatementAttribute gt(String value) {
        return gt(Comparison.String::new, value);
    }

    @CheckReturnValue
    default StatementAttribute gt(LocalDateTime value) {
        return gt(Comparison.DateTime::new, value);
    }

    @CheckReturnValue
    default StatementAttribute gt(Statement variable) {
        return gt(Comparison.Variable::new, variable);
    }

    @CheckReturnValue
    default <T> StatementAttribute gt(BiFunction<Graql.Token.Comparator, T, Comparison<T>> constructor, T value) {
        return operation(constructor.apply(Graql.Token.Comparator.GT, value));
    }

    // Attribute value greater-than-or-equals property

    @CheckReturnValue
    default StatementAttribute gte(long value) {
        return gte(Comparison.Number::new, value);
    }

    @CheckReturnValue
    default StatementAttribute gte(double value) {
        return gte(Comparison.Number::new, value);
    }

    @CheckReturnValue
    default StatementAttribute gte(boolean value) {
        return gte(Comparison.Boolean::new, value);
    }

    @CheckReturnValue
    default StatementAttribute gte(String value) {
        return gte(Comparison.String::new, value);
    }

    @CheckReturnValue
    default StatementAttribute gte(LocalDateTime value) {
        return gte(Comparison.DateTime::new, value);
    }

    @CheckReturnValue
    default StatementAttribute gte(Statement variable) {
        return gte(Comparison.Variable::new, variable);
    }

    @CheckReturnValue
    default <T> StatementAttribute gte(BiFunction<Graql.Token.Comparator, T, Comparison<T>> constructor, T value) {
        return operation(constructor.apply(Graql.Token.Comparator.GTE, value));
    }

    // Attribute value less-than property

    @CheckReturnValue
    default StatementAttribute lt(long value) {
        return lt(Comparison.Number::new, value);
    }

    @CheckReturnValue
    default StatementAttribute lt(double value) {
        return lt(Comparison.Number::new, value);
    }

    @CheckReturnValue
    default StatementAttribute lt(boolean value) {
        return lt(Comparison.Boolean::new, value);
    }

    @CheckReturnValue
    default StatementAttribute lt(String value) {
        return lt(Comparison.String::new, value);
    }

    @CheckReturnValue
    default StatementAttribute lt(LocalDateTime value) {
        return lt(Comparison.DateTime::new, value);
    }

    @CheckReturnValue
    default StatementAttribute lt(Statement variable) {
        return lt(Comparison.Variable::new, variable);
    }

    @CheckReturnValue
    default <T> StatementAttribute lt(BiFunction<Graql.Token.Comparator, T, Comparison<T>> constructor, T value) {
        return operation(constructor.apply(Graql.Token.Comparator.LT, value));
    }

    // Attribute value less-than-or-equals property

    @CheckReturnValue
    default StatementAttribute lte(long value) {
        return lte(Comparison.Number::new, value);
    }

    @CheckReturnValue
    default StatementAttribute lte(double value) {
        return lte(Comparison.Number::new, value);
    }

    @CheckReturnValue
    default StatementAttribute lte(boolean value) {
        return lte(Comparison.Boolean::new, value);
    }

    @CheckReturnValue
    default StatementAttribute lte(String value) {
        return lte(Comparison.String::new, value);
    }

    @CheckReturnValue
    default StatementAttribute lte(LocalDateTime value) {
        return lte(Comparison.DateTime::new, value);
    }

    @CheckReturnValue
    default StatementAttribute lte(Statement variable) {
        return lte(Comparison.Variable::new, variable);
    }

    @CheckReturnValue
    default <T> StatementAttribute lte(BiFunction<Graql.Token.Comparator, T, Comparison<T>> constructor, T value) {
        return operation(constructor.apply(Graql.Token.Comparator.LTE, value));
    }

    // Attribute value contains (in String) property

    @CheckReturnValue
    default StatementAttribute contains(String value) {
        return contains(Comparison.String::new, value);
    }

    @CheckReturnValue
    default StatementAttribute contains(Statement variable) {
        return contains(Comparison.Variable::new, variable);
    }

    @CheckReturnValue
    default <T> StatementAttribute contains(BiFunction<Graql.Token.Comparator, T, Comparison<T>> constructor, T value) {
        return operation(constructor.apply(Graql.Token.Comparator.CONTAINS, value));
    }

    // Attribute value like (regex) property

    @CheckReturnValue
    default StatementAttribute like(String value) {
        return operation(new Comparison.String(Graql.Token.Comparator.LIKE, value));
    }

    // Attribute Statement builder methods

    @CheckReturnValue
    default StatementAttribute operation(ValueProperty.Operation<?> operation) {
        return attribute(new ValueProperty<>(operation));
    }

    @CheckReturnValue   // TODO: will be made "private" once we upgrade to Java 9 or higher
    StatementAttribute attribute(VarProperty property);
}
