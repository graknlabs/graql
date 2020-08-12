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

package graql.lang.pattern.property;

import grakn.common.collection.Either;
import grakn.common.collection.Pair;
import graql.lang.common.GraqlArg;
import graql.lang.common.GraqlToken;
import graql.lang.common.exception.GraqlException;
import graql.lang.pattern.Conjunction;
import graql.lang.pattern.Pattern;
import graql.lang.pattern.variable.TypeVariable;
import graql.lang.pattern.variable.UnboundVariable;
import graql.lang.pattern.variable.Variable;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static graql.lang.common.GraqlToken.Char.COLON;
import static graql.lang.common.GraqlToken.Char.CURLY_CLOSE;
import static graql.lang.common.GraqlToken.Char.CURLY_OPEN;
import static graql.lang.common.GraqlToken.Char.SEMICOLON;
import static graql.lang.common.GraqlToken.Char.SPACE;
import static graql.lang.common.GraqlToken.Property.AS;
import static graql.lang.common.GraqlToken.Property.IS_KEY;
import static graql.lang.common.GraqlToken.Property.OWNS;
import static graql.lang.common.GraqlToken.Property.PLAYS;
import static graql.lang.common.GraqlToken.Property.REGEX;
import static graql.lang.common.GraqlToken.Property.RELATES;
import static graql.lang.common.GraqlToken.Property.SUB;
import static graql.lang.common.GraqlToken.Property.SUBX;
import static graql.lang.common.GraqlToken.Property.THEN;
import static graql.lang.common.GraqlToken.Property.TYPE;
import static graql.lang.common.GraqlToken.Property.VALUE_TYPE;
import static graql.lang.common.GraqlToken.Property.WHEN;
import static graql.lang.common.exception.ErrorMessage.INVALID_CAST_EXCEPTION;
import static graql.lang.common.util.Strings.escapeRegex;
import static graql.lang.common.util.Strings.quoteString;
import static graql.lang.pattern.variable.UnboundVariable.hidden;
import static java.util.stream.Collectors.joining;

public abstract class TypeProperty extends Property {

    public boolean isSingular() {
        return false;
    }

    public boolean isRepeatable() {
        return false;
    }

    public TypeProperty.Singular asSingular() {
        throw GraqlException.create(INVALID_CAST_EXCEPTION.message(
                Repeatable.class.getCanonicalName(), Singular.class.getCanonicalName()
        ));
    }

    public TypeProperty.Repeatable asRepeatable() {
        throw GraqlException.create(INVALID_CAST_EXCEPTION.message(
                Singular.class.getCanonicalName(), Repeatable.class.getCanonicalName()
        ));
    }

    public TypeProperty.Label asLabel() {
        throw GraqlException.create(INVALID_CAST_EXCEPTION.message(
                TypeProperty.class.getCanonicalName(), Label.class.getCanonicalName()
        ));
    }

    public TypeProperty.Sub asSub() {
        throw GraqlException.create(INVALID_CAST_EXCEPTION.message(
                TypeProperty.class.getCanonicalName(), Sub.class.getCanonicalName()
        ));
    }

    public TypeProperty.Abstract asAbstract() {
        throw GraqlException.create(INVALID_CAST_EXCEPTION.message(
                TypeProperty.class.getCanonicalName(), Abstract.class.getCanonicalName()
        ));
    }

    public TypeProperty.ValueType asValueType() {
        throw GraqlException.create(INVALID_CAST_EXCEPTION.message(
                TypeProperty.class.getCanonicalName(), ValueType.class.getCanonicalName()
        ));
    }

    public TypeProperty.Regex asRegex() {
        throw GraqlException.create(INVALID_CAST_EXCEPTION.message(
                TypeProperty.class.getCanonicalName(), Regex.class.getCanonicalName()
        ));
    }

    public TypeProperty.Then asThen() {
        throw GraqlException.create(INVALID_CAST_EXCEPTION.message(
                TypeProperty.class.getCanonicalName(), Then.class.getCanonicalName()
        ));
    }

    public TypeProperty.When asWhen() {
        throw GraqlException.create(INVALID_CAST_EXCEPTION.message(
                TypeProperty.class.getCanonicalName(), When.class.getCanonicalName()
        ));
    }

    public Owns asHas() {
        throw GraqlException.create(INVALID_CAST_EXCEPTION.message(
                TypeProperty.class.getCanonicalName(), Owns.class.getCanonicalName()
        ));
    }

    public TypeProperty.Plays asPlays() {
        throw GraqlException.create(INVALID_CAST_EXCEPTION.message(
                TypeProperty.class.getCanonicalName(), Plays.class.getCanonicalName()
        ));
    }

    public TypeProperty.Relates asRelates() {
        throw GraqlException.create(INVALID_CAST_EXCEPTION.message(
                TypeProperty.class.getCanonicalName(), Relates.class.getCanonicalName()
        ));
    }

    public static abstract class Singular extends TypeProperty {

        @Override
        public boolean isSingular() {
            return true;
        }

        @Override
        public TypeProperty.Singular asSingular() {
            return this;
        }
    }

    public static abstract class Repeatable extends TypeProperty {

        @Override
        public boolean isRepeatable() {
            return true;
        }

        @Override
        public TypeProperty.Repeatable asRepeatable() {
            return this;
        }
    }

    public static class Label extends TypeProperty.Singular {

        private final String label;
        private final String scope;
        private final int hash;

        public Label(String label) {
            this(null, label);
        }

        public Label(@Nullable String scope, String label) {
            if (label == null) throw new NullPointerException("Null label");
            this.scope = scope;
            this.label = label;
            this.hash = Objects.hash(this.scope, this.label);
        }

        public Optional<String> scope() {
            return Optional.ofNullable(scope);
        }

        public String label() {
            return label;
        }

        public String scopedLabel() {
            if (scope != null) return scope + COLON + label;
            else return label;
        }

        @Override
        public Stream<Variable> variables() {
            return Stream.of();
        }

        @Override
        public TypeProperty.Label asLabel() {
            return this;
        }

        @Override
        public String toString() {
            return TYPE.toString() + SPACE + scopedLabel();
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Label that = (Label) o;
            return (this.label.equals(that.label));
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

    public static class Sub extends TypeProperty.Singular {

        private final TypeVariable type;
        private final boolean isExplicit;
        private final int hash;

        public Sub(String typeLabe, boolean isExplicit) {
            this(hidden().type(typeLabe), isExplicit);
        }

        public Sub(String typeScope, String typeLabel, boolean isExplicit) {
            this(hidden().type(typeScope, typeLabel), isExplicit);
        }

        public Sub(UnboundVariable typeVar, boolean isExplicit) {
            this(typeVar.asType(), isExplicit);
        }

        public Sub(Either<Pair<String, String>, UnboundVariable> typeArg, boolean isExplicit) {
            this(typeArg.apply(scoped -> hidden().asTypeWith(new TypeProperty.Label(scoped.first(), scoped.second())),
                               UnboundVariable::asType), isExplicit);
        }

        private Sub(TypeVariable typeVar, boolean isExplicit) {
            if (typeVar == null) throw new NullPointerException("Null superType");
            this.type = typeVar;
            this.isExplicit = isExplicit;
            this.hash = Objects.hash(typeVar, isExplicit);
        }

        public TypeVariable type() {
            return type;
        }

        @Override
        public Stream<Variable> variables() {
            return Stream.of(type);
        }

        @Override
        public TypeProperty.Sub asSub() {
            return this;
        }

        @Override
        public String toString() {
            return (isExplicit ? SUBX.toString() : SUB.toString()) + SPACE + type();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Sub that = (Sub) o;
            return (this.type.equals(that.type) && this.isExplicit == that.isExplicit);
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

    public static class Abstract extends TypeProperty.Singular {

        private final int hash;

        public Abstract() {
            this.hash = Objects.hash(Abstract.class);
        }

        @Override
        public Stream<Variable> variables() {
            return Stream.of();
        }

        @Override
        public TypeProperty.Abstract asAbstract() {
            return this;
        }

        @Override
        public String toString() {
            return GraqlToken.Property.ABSTRACT.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            return o != null && getClass() == o.getClass();
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

    public static class ValueType extends TypeProperty.Singular {

        private final GraqlArg.ValueType valueType;
        private final int hash;

        public ValueType(GraqlArg.ValueType valueType) {
            if (valueType == null) throw new NullPointerException("Null ValueType");
            this.valueType = valueType;
            this.hash = Objects.hash(this.valueType);
        }

        public GraqlArg.ValueType valueType() {
            return valueType;
        }

        @Override
        public Stream<Variable> variables() {
            return Stream.of();
        }

        @Override
        public TypeProperty.ValueType asValueType() {
            return this;
        }

        @Override
        public String toString() {
            return VALUE_TYPE.toString() + SPACE + valueType.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ValueType that = (ValueType) o;
            return (this.valueType.equals(that.valueType));
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

    public static class Regex extends TypeProperty.Singular {

        private final String regex;
        private final int hash;

        public Regex(String regex) {
            if (regex == null) throw new NullPointerException("Null regex");
            this.regex = regex;
            this.hash = Objects.hash(regex);
        }

        public String regex() {
            return regex;
        }

        @Override
        public Stream<Variable> variables() {
            return Stream.of();
        }

        @Override
        public TypeProperty.Regex asRegex() {
            return this;
        }

        @Override
        public String toString() {
            return REGEX.toString() + SPACE + quoteString(escapeRegex(regex()));
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Regex that = (Regex) o;
            return (this.regex.equals(that.regex));
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

    // TODO: Move this out of TypeProperty and create its own class
    public static class Then extends TypeProperty.Singular {

        private final Pattern pattern;
        private final int hash;

        public Then(Pattern pattern) {
            if (pattern == null) throw new NullPointerException("Null pattern");
            this.pattern = pattern;
            this.hash = Objects.hash(pattern);
        }

        public Pattern pattern() {
            return pattern;
        }

        @Override
        public Stream<Variable> variables() {
            return Stream.of();
        }

        @Override
        public TypeProperty.Then asThen() {
            return this;
        }

        @Override
        public String toString() {
            StringBuilder syntax = new StringBuilder();
            syntax.append(THEN).append(SPACE).append(CURLY_OPEN).append(SPACE);
            if (pattern instanceof Conjunction) {
                syntax.append(((Conjunction<?>) pattern).patterns()
                                      .stream().map(Object::toString)
                                      .collect(joining("" + SEMICOLON + SPACE)));
            } else {
                syntax.append(pattern.toString());
            }
            syntax.append(SEMICOLON).append(SPACE).append(CURLY_CLOSE);
            return syntax.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Then that = (Then) o;
            return (this.pattern.equals(that.pattern));
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

    // TODO: Move this out of TypeProperty and create its own class
    public static class When extends TypeProperty.Singular {

        private final Pattern pattern;
        private final int hash;

        public When(Pattern pattern) {
            if (pattern == null) throw new NullPointerException("Null Pattern");
            this.pattern = pattern;
            this.hash = Objects.hash(pattern);
        }

        public Pattern pattern() {
            return pattern;
        }

        @Override
        public Stream<Variable> variables() {
            return Stream.of();
        }

        @Override
        public TypeProperty.When asWhen() {
            return this;
        }

        @Override
        public String toString() {
            StringBuilder syntax = new StringBuilder();
            syntax.append(WHEN).append(SPACE).append(CURLY_OPEN).append(SPACE);
            if (pattern instanceof Conjunction) {
                syntax.append(((Conjunction<?>) pattern).patterns()
                                      .stream().map(Object::toString)
                                      .collect(joining("" + SEMICOLON + SPACE)));
            } else {
                syntax.append(pattern);
            }
            syntax.append(SEMICOLON).append(SPACE).append(CURLY_CLOSE);
            return syntax.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (o == null || getClass() != o.getClass()) return false;
            When that = (When) o;
            return (this.pattern.equals(that.pattern));
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

    public static class Owns extends TypeProperty.Repeatable {

        private final TypeVariable attributeType;
        private final TypeVariable overriddenAttributeType;
        private final boolean isKey;
        private final int hash;

        public Owns(String attributeType, boolean isKey) {
            this(hidden().type(attributeType), null, isKey);
        }

        public Owns(UnboundVariable attributeTypeVar, boolean isKey) {
            this(attributeTypeVar.asType(), null, isKey);
        }

        public Owns(String attributeType, String overriddenAttributeType, boolean isKey) {
            this(hidden().type(attributeType), overriddenAttributeType == null ? null : hidden().type(overriddenAttributeType), isKey);
        }

        public Owns(UnboundVariable attributeTypeVar, String overriddenAttributeType, boolean isKey) {
            this(attributeTypeVar.asType(), overriddenAttributeType == null ? null : hidden().type(overriddenAttributeType), isKey);
        }

        public Owns(String attributeType, UnboundVariable overriddenAttributeTypeVar, boolean isKey) {
            this(hidden().type(attributeType), overriddenAttributeTypeVar == null ? null : overriddenAttributeTypeVar.asType(), isKey);
        }

        public Owns(UnboundVariable attributeTypeVar, UnboundVariable overriddenAttributeTypeVar, boolean isKey) {
            this(attributeTypeVar.asType(), overriddenAttributeTypeVar == null ? null : overriddenAttributeTypeVar.asType(), isKey);
        }

        public Owns(Either<String, UnboundVariable> attributeTypeArg, Either<String, UnboundVariable> overriddenAttributeTypeArg, boolean isKey) {
            this(attributeTypeArg.apply(label -> hidden().type(label), UnboundVariable::asType),
                 overriddenAttributeTypeArg == null ? null : overriddenAttributeTypeArg.apply(label -> hidden().type(label), UnboundVariable::asType),
                 isKey);
        }

        private Owns(TypeVariable attributeType, @Nullable TypeVariable overriddenAttributeType, boolean isKey) {
            this.attributeType = attributeType;
            this.overriddenAttributeType = overriddenAttributeType;
            this.isKey = isKey;
            this.hash = Objects.hash(attributeType, isKey);
        }

        public TypeVariable attribute() {
            return attributeType;
        }

        public Optional<TypeVariable> overridden() {
            return Optional.ofNullable(overriddenAttributeType);
        }

        public boolean isKey() {
            return isKey;
        }

        @Override
        public Stream<Variable> variables() {
            return Stream.of(attributeType);
        }

        @Override
        public TypeProperty.Owns asHas() {
            return this;
        }

        @Override
        public String toString() {
            return OWNS.toString() + SPACE + attributeType + (isKey ? "" + SPACE + IS_KEY : "");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Owns that = (Owns) o;
            return (this.attributeType.equals(that.attributeType) && this.isKey == that.isKey);
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

    public static class Plays extends TypeProperty.Repeatable {

        private final TypeVariable roleType;
        private final TypeVariable overriddenRoleType;
        private final int hash;

        public Plays(String relationType, String roleType) {
            this(hidden().type(relationType, roleType), null);
        }

        public Plays(UnboundVariable var) {
            this(var.asType(), null);
        }

        public Plays(String relationType, String roleType, String overriddenRoleType) {
            this(hidden().type(relationType, roleType), overriddenRoleType == null ? null : hidden().type(overriddenRoleType));
        }

        public Plays(UnboundVariable roleTypeVar, String overriddenRoleType) {
            this(roleTypeVar.asType(), overriddenRoleType == null ? null : hidden().type(overriddenRoleType));
        }

        public Plays(String relationType, String roleType, UnboundVariable overriddenRoleTypeVar) {
            this(hidden().type(relationType, roleType), overriddenRoleTypeVar == null ? null : overriddenRoleTypeVar.asType());
        }

        public Plays(UnboundVariable roleTypeVar, UnboundVariable overriddenRoleTypeVar) {
            this(roleTypeVar.asType(), overriddenRoleTypeVar == null ? null : overriddenRoleTypeVar.asType());
        }

        public Plays(Either<Pair<String, String>, UnboundVariable> roleTypeArg, Either<String, UnboundVariable> overriddenRoleTypeArg) {
            this(roleTypeArg.apply(scoped -> hidden().asTypeWith(new TypeProperty.Label(scoped.first(), scoped.second())), UnboundVariable::asType),
                 overriddenRoleTypeArg == null ? null : overriddenRoleTypeArg.apply(label -> hidden().type(label), UnboundVariable::asType));
        }

        private Plays(TypeVariable roleType, @Nullable TypeVariable overriddenRoleType) {
            if (roleType == null) throw new NullPointerException("Null role");
            this.roleType = roleType;
            this.overriddenRoleType = overriddenRoleType;
            this.hash = Objects.hash(roleType, overriddenRoleType);
        }

        public TypeVariable role() {
            return roleType;
        }

        public Optional<TypeVariable> overridden() {
            return Optional.ofNullable(overriddenRoleType);
        }

        @Override
        public Stream<Variable> variables() {
            return Stream.of(roleType);
        }

        @Override
        public TypeProperty.Plays asPlays() {
            return this;
        }

        @Override
        public String toString() {
            return PLAYS.toString() + SPACE + roleType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Plays that = (Plays) o;
            return (this.roleType.equals(that.roleType));
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

    public static class Relates extends TypeProperty.Repeatable {

        private final TypeVariable roleType;
        private final TypeVariable overriddenRoleType;
        private final int hash;

        public Relates(String roleType) {
            this(hidden().type(roleType), null);
        }

        public Relates(UnboundVariable roleTypeVar) {
            this(roleTypeVar.asType(), null);
        }

        public Relates(String roleType, String overriddenRoleType) {
            this(hidden().type(roleType), overriddenRoleType == null ? null : hidden().type(overriddenRoleType));
        }

        public Relates(UnboundVariable roleTypeVar, String overriddenRoleType) {
            this(roleTypeVar.asType(), overriddenRoleType == null ? null : hidden().type(overriddenRoleType));
        }

        public Relates(String roleType, UnboundVariable overriddenRoleTypeVar) {
            this(hidden().type(roleType), overriddenRoleTypeVar == null ? null : overriddenRoleTypeVar.asType());
        }

        public Relates(UnboundVariable roleTypeVar, UnboundVariable overriddenRoleTypeVar) {
            this(roleTypeVar.asType(), overriddenRoleTypeVar == null ? null : overriddenRoleTypeVar.asType());
        }

        public Relates(Either<String, UnboundVariable> roleTypeArg, Either<String, UnboundVariable> overriddenRoleTypeArg) {
            this(roleTypeArg.apply(label -> hidden().type(label), UnboundVariable::asType),
                 overriddenRoleTypeArg == null ? null : overriddenRoleTypeArg.apply(label -> hidden().type(label), UnboundVariable::asType));
        }

        private Relates(TypeVariable roleType, @Nullable TypeVariable overriddenRoleType) {
            if (roleType == null) throw new NullPointerException("Null role");
            this.roleType = roleType;
            this.overriddenRoleType = overriddenRoleType;
            this.hash = Objects.hash(roleType, overriddenRoleType);
        }

        public TypeVariable role() {
            return roleType;
        }

        public Optional<TypeVariable> overridden() {
            return Optional.ofNullable(overriddenRoleType);
        }

        @Override
        public Stream<Variable> variables() {
            return overriddenRoleType == null ? Stream.of(roleType) : Stream.of(roleType, overriddenRoleType);
        }

        @Override
        public TypeProperty.Relates asRelates() {
            return this;
        }

        @Override
        public String toString() {
            StringBuilder syntax = new StringBuilder();
            syntax.append(RELATES).append(SPACE);
            if (!roleType.labelProperty().isPresent()) syntax.append(roleType);
            else syntax.append(roleType.labelProperty().get().label());
            if (overriddenRoleType != null) {
                syntax.append(SPACE).append(AS).append(SPACE);
                if (!overriddenRoleType.labelProperty().isPresent()) syntax.append(overriddenRoleType);
                else syntax.append(overriddenRoleType.labelProperty().get().label());
            }
            return syntax.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Relates that = (Relates) o;
            return (this.roleType.equals(that.roleType) &&
                    Objects.equals(this.overriddenRoleType, that.overriddenRoleType));
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }
}
