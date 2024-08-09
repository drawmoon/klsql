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
package io.github.drawmoon.saber.engine;

import io.github.drawmoon.saber.*;
import io.github.drawmoon.saber.common.Sequence;
import io.github.drawmoon.saber.impl.AsteriskExpression;
import io.github.drawmoon.saber.impl.ComparisonExpression;
import io.github.drawmoon.saber.impl.DistinctExpression;
import io.github.drawmoon.saber.impl.JoinExpression;
import io.github.drawmoon.saber.impl.LogicalExpression;
import io.github.drawmoon.saber.impl.MemberExpression;
import io.github.drawmoon.saber.impl.SelectExpression;
import io.github.drawmoon.saber.impl.TableExpression;
import io.github.drawmoon.saber.impl.TableFieldExpression;
import io.github.drawmoon.saber.impl.VariableExpression;
import java.util.Objects;
import javax.annotation.Nonnull;

public class BasicSqlBuilder implements ExpressionVisitor<BasicSqlBuilder> {

  protected final ExpressionContext ctx;
  protected final SaberEventListener listener;

  public BasicSqlBuilder(ExpressionContext ctx) {
    this.ctx = Objects.requireNonNull(ctx);
    this.listener = ctx.getListener();
  }

  @Override
  @Nonnull
  public BasicSqlBuilder visitSelect(SelectExpression select) {
    ctx.writeKeyword(Expressions.keyword("SELECT")).writeSql(" ");
    Sequence.it(select).find(e -> e instanceof Field).ifPresent(e -> e.accept(this));
    Sequence.it(select)
        .find(e -> e instanceof Table)
        .ifPresent(
            e -> {
              ctx.writeSql(" ").writeKeyword(Expressions.keyword("FROM")).writeSql(" ");
              e.accept(this);
            });
    return this;
  }

  @Override
  @Nonnull
  public BasicSqlBuilder visitDistinct(DistinctExpression distinct) {
    ctx.writeKeyword(Expressions.keyword("DISTINCT")).writeSql("(");
    for (Expression e : distinct) e.accept(this);
    ctx.writeSql(")");
    return this;
  }

  @Override
  @Nonnull
  public BasicSqlBuilder visitAsterisk(AsteriskExpression asterisk) {
    ctx.writeSql("*");
    return this;
  }

  @Override
  @Nonnull
  public BasicSqlBuilder visitMember(MemberExpression member) {
    int i = 0;
    for (Expression e : member) {
      if (i > 0) ctx.writeSql(", ");
      e.accept(this);
      i++;
    }
    return this;
  }

  @Override
  @Nonnull
  public BasicSqlBuilder visitTableField(TableFieldExpression tableField) {
    return this;
  }

  @Override
  @Nonnull
  public BasicSqlBuilder visitTable(TableExpression table) {
    return this;
  }

  @Override
  @Nonnull
  public BasicSqlBuilder visitJoin(JoinExpression join) {
    return this;
  }

  @Override
  @Nonnull
  public BasicSqlBuilder visitComparison(ComparisonExpression comparison) {
    return this;
  }

  @Override
  @Nonnull
  public BasicSqlBuilder visitLogical(LogicalExpression logical) {
    return this;
  }

  @Override
  @Nonnull
  public <V> BasicSqlBuilder visitVariable(VariableExpression<V> variable) {
    DataType<V> type = variable.getType();
    if (type == DataType.INTEGER) {
      ctx.writeSql(String.valueOf(variable.getValue()));
    }
    return this;
  }

  @Override
  public String toString() {
    return ctx.render();
  }
}
