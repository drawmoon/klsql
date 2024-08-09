/*
 *            _
 *  ___  __ _| |__   ___ _ __
 * / __|/ _` | '_ \ / _ \ '__|
 * \__ \ (_| | |_) |  __/ |
 * |___/\__,_|_.__/ \___|_|
 *
 * Copyright 2024 drash
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.drawmoon.saber.impl;

import static io.github.drawmoon.saber.common.Preconditions.*;

import io.github.drawmoon.saber.*;
import io.github.drawmoon.saber.common.Sequence;
import io.github.drawmoon.saber.engine.ExpressionIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** A table expression. */
@SuppressWarnings("DoNotCallSuggester") // TODO: remove
public final class TableExpression implements Table {

  private final String name;
  private final List<Field> fields;
  private final Schema schema;
  private String alias;

  /**
   * Create a new table expression.
   *
   * @param name the table name, not null
   * @param schema the schema of the table, not null
   * @param fields the fields of the table, not null
   */
  public TableExpression(String name, Schema schema, List<Field> fields) {
    this.name = checkNotWhiteSpace(name);
    this.schema = Objects.requireNonNull(schema);
    this.fields = Objects.requireNonNull(fields);
  }

  // -----------------------------------------------------------------------
  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String getAlias() {
    return this.alias;
  }

  @Override
  @CheckForNull
  public Schema getSchema() {
    return this.schema;
  }

  @Override
  public int fieldCount() {
    return this.fields.size();
  }

  @Override
  @CheckForNull
  public List<Field> getFields() {
    return this.fields;
  }

  @Override
  @CheckForNull
  public Field getField(String f) {
    checkNotWhiteSpace(f);

    if (this.fields == null) return null;
    return (Field)
        Sequence.it(this)
            .filter(o -> o instanceof TableField)
            .find(o -> f.equals(((TableField) o).getName()))
            .orElse(null);
  }

  @Override
  @Nonnull
  public Table as(String alias) {
    this.alias = checkNotWhiteSpace(alias);
    return this;
  }

  @Override
  @Nonnull
  public Field asterisk() {
    return new AsteriskExpression(this);
  }

  @Override
  @Nonnull
  public JoinExpression join(Table t, JoinType jt, @Nullable JoinHint jh) {
    checkNotNull(t, jt);

    throw new UnsupportedOperationException();
  }

  @Override
  @Nonnull
  public Table useIndex(String... i) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Nonnull
  public Table useIndexForJoin(String... i) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Nonnull
  public Table useIndexForOrderBy(String... i) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Nonnull
  public Table useIndexForGroupBy(String... i) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Nonnull
  public Table ignoreIndex(String... i) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Nonnull
  public Table ignoreIndexForJoin(String... i) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Nonnull
  public Table ignoreIndexForOrderBy(String... i) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Nonnull
  public Table ignoreIndexForGroupBy(String... i) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Nonnull
  public Table forceIndex(String... i) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Nonnull
  public Table forceIndexForJoin(String... i) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Nonnull
  public Table forceIndexForOrderBy(String... i) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Nonnull
  public Table forceIndexForGroupBy(String... i) {
    throw new UnsupportedOperationException();
  }

  // -----------------------------------------------------------------------
  @Override
  @Nonnull
  public <T> T accept(ExpressionVisitor<T> visitor) {
    return visitor.visitTable(this);
  }

  @Override
  @Nonnull
  public Iterator<Expression> iterator() {
    return ExpressionIterator.sameAsExpression(this.fields.toArray());
  }
}
