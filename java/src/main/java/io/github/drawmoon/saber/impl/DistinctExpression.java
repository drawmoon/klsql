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

import io.github.drawmoon.saber.Expression;
import io.github.drawmoon.saber.ExpressionIterator;
import io.github.drawmoon.saber.Field;
import io.github.drawmoon.saber.Visitor;
import io.github.drawmoon.saber.common.Enumerable;
import java.util.Iterator;
import java.util.function.Function;
import javax.annotation.Nonnull;

/** A distinct expression. */
@SuppressWarnings("unused")
public class DistinctExpression implements Field, Expression {

  Field field;

  // -----------------------------------------------------------------------
  @Override
  public String getName() {
    return this.field.getName();
  }

  @Nonnull
  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visitDistinct(this);
  }

  @Nonnull
  @Override
  public <R> Enumerable<R> collect(Function<? super Expression, ? extends R> function) {
    throw new UnsupportedOperationException();
  }

  @Nonnull
  @Override
  public Iterator<Expression> iterator() {
    return ExpressionIterator.sameAsExpression(this.field);
  }
}
