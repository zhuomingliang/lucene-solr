package org.apache.lucene.queryparser.flexible.core.nodes;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.queryparser.flexible.core.parser.EscapeQuerySyntax;

import java.util.List;


/**
 * A {@link ParametricRangeQueryNode} represents LE, LT, GE, GT, EQ, NE query.
 * Example: date >= "2009-10-10" OR price = 200
 */
public class ParametricRangeQueryNode extends QueryNodeImpl implements
    FieldableNode {

  public ParametricRangeQueryNode(ParametricQueryNode lowerBound,
      ParametricQueryNode upperBound) {

    if (upperBound.getOperator() != ParametricQueryNode.CompareOperator.LE
        && upperBound.getOperator() != ParametricQueryNode.CompareOperator.LT) {
      throw new IllegalArgumentException("upper bound should have "
          + ParametricQueryNode.CompareOperator.LE + " or " + ParametricQueryNode.CompareOperator.LT);
    }

    if (lowerBound.getOperator() != ParametricQueryNode.CompareOperator.GE
        && lowerBound.getOperator() != ParametricQueryNode.CompareOperator.GT) {
      throw new IllegalArgumentException("lower bound should have "
          + ParametricQueryNode.CompareOperator.GE + " or " + ParametricQueryNode.CompareOperator.GT);
    }

    if (upperBound.getField() != lowerBound.getField()
        || (upperBound.getField() != null && !upperBound.getField().equals(
            lowerBound.getField()))) {

      throw new IllegalArgumentException(
          "lower and upper bounds should have the same field name!");

    }

    allocate();
    setLeaf(false);

    add(lowerBound);
    add(upperBound);

  }

  public ParametricQueryNode getUpperBound() {
    return (ParametricQueryNode) getChildren().get(1);
  }

  public ParametricQueryNode getLowerBound() {
    return (ParametricQueryNode) getChildren().get(0);
  }

  public CharSequence toQueryString(EscapeQuerySyntax escapeSyntaxParser) {
    return getLowerBound().toQueryString(escapeSyntaxParser) + " AND "
        + getUpperBound().toQueryString(escapeSyntaxParser);
  }

  public CharSequence getField() {
    return getLowerBound().getField();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("<parametricRange>\n\t");
    sb.append(getUpperBound()).append("\n\t");
    sb.append(getLowerBound()).append("\n");
    sb.append("</parametricRange>\n");

    return sb.toString();

  }

  @Override
  public ParametricRangeQueryNode cloneTree() throws CloneNotSupportedException {
    ParametricRangeQueryNode clone = (ParametricRangeQueryNode) super
        .cloneTree();

    // nothing to do here

    return clone;
  }

  public void setField(CharSequence fieldName) {
    List<QueryNode> children = getChildren();

    if (children != null) {

      for (QueryNode child : getChildren()) {

        if (child instanceof FieldableNode) {
          ((FieldableNode) child).setField(fieldName);
        }

      }

    }

  }

}
