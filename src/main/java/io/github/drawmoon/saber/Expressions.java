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
package io.github.drawmoon.saber;

import static io.github.drawmoon.saber.common.Preconditions.*;

import io.github.drawmoon.saber.engine.DataType;
import io.github.drawmoon.saber.engine.EventBus;
import io.github.drawmoon.saber.engine.ExpressionContext;
import io.github.drawmoon.saber.engine.SaberOptions;
import io.github.drawmoon.saber.impl.*;
import java.util.Arrays;
import java.util.Objects;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

@SuppressWarnings({"DoNotCallSuggester"}) // TODO: remove
public final class Expressions implements SqlGenerator {

  private final ExpressionContext ctx;

  /**
   * Constructor.
   *
   * @param ctx the context, not null
   */
  private Expressions(ExpressionContext ctx) {
    this.ctx = Objects.requireNonNull(ctx);
  }

  /**
   * Create a new {@link Expressions}.
   *
   * @param catalog the catalog, not null
   * @return the new {@link Expressions}
   */
  public static Expressions create(Catalog catalog) {
    return create(catalog, new SaberOptions());
  }

  /**
   * Create a new {@link Expressions}.
   *
   * @param catalog the catalog, not null
   * @param options the options, not null
   * @return the new {@link Expressions}
   */
  public static Expressions create(Catalog catalog, SaberOptions options) {
    ExpressionContext ctx = new ExpressionContext(catalog, options, new EventBus());
    return new Expressions(ctx);
  }

  /**
   * Create a new {@link Expressions}.
   *
   * @param ctx the context, not null
   * @return the new {@link Expressions}
   */
  public static Expressions create(ExpressionContext ctx) {
    return new Expressions(ctx);
  }

  // -----------------------------------------------------------------------
  /**
   * Gets a keyword.
   *
   * @param keyword The keyword string, not null
   * @return the {@link Keyword} object, not null
   */
  @Nonnull
  public static Keyword keyword(@CheckForNull String keyword) {
    return Keyword.of(keyword);
  }

  /**
   * Gets a variable.
   *
   * @param i the value
   * @return the new expression
   */
  @Nonnull
  public static Variable<Integer> intVal(Integer i) {
    return val(i);
  }

  /**
   * Gets a variable.
   *
   * @param s the value
   * @return the new expression
   */
  @Nonnull
  public static Variable<String> strVal(String s) {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets a variable.
   *
   * @param s the value
   * @return the new expression
   */
  @Nonnull
  public static Variable<Boolean> boolVal(Boolean s) {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets a variable.
   *
   * @param <T> the type of the value.
   * @param val the value.
   * @return the new variable.
   */
  @SuppressWarnings("unchecked")
  public static <T> Variable<T> val(T val) {
    if (val instanceof Integer)
      return new VariableExpression<>(val, (DataType<T>) DataType.INTEGER);
    throw new UnsupportedOperationException();
  }

  // -----------------------------------------------------------------------
  /**
   * Create a new select statement.
   *
   * @param f the field, not null
   * @return the new select
   */
  @Nonnull
  public Select select(@CheckForNull Object... f) {
    Objects.requireNonNull(f);
    collectionNullClean(Arrays.asList(f));

    MemberExpression member = new MemberExpression();
    for (Object e : f) {
      Field field;
      if (e instanceof Field) field = (Field) e;
      else field = val(e);
      member.append(field);
    }

    return SelectExpression.of(this.ctx, member);
  }

  @Override
  @Nonnull
  public String render() {
    return this.ctx.render();
  }
}
