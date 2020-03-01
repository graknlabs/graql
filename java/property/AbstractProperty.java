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

package graql.lang.property;

import graql.lang.Graql;
import graql.lang.statement.StatementType;

/**
 * Represents the {@code abstract} property on a Type.
 * This property can be matched or inserted.
 * This property states that a type cannot have direct instances.
 */
public class AbstractProperty extends VarProperty {

    private static final AbstractProperty INSTANCE = new AbstractProperty();

    private AbstractProperty() {}

    public static AbstractProperty get() {
        return INSTANCE;
    }

    @Override
    public String keyword() {
        return Graql.Token.Property.ABSTRACT.toString();
    }

    @Override
    public String property() {
        return null;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public Class statementClass() {
        return StatementType.class;
    }

    @Override
    public String toString() {
        return keyword();
    }
}
