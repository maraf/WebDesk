/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.data.jpa;

import com.neptuo.data.QueryBuilder;
import com.neptuo.data.QueryBuilderException;
import com.neptuo.data.QueryComparator;
import com.neptuo.data.QueryOrder;

/**
 *
 * @author Mara
 */
public class JpaQueryBuilder implements QueryBuilder {
    private String result = "";
    private boolean appendToSelectAlias = false;
    private boolean distinct = false;
    private String alias;

    @Override
    public QueryBuilder select() throws QueryBuilderException {
        appendToSelectAlias = true;
        return this;
    }

    @Override
    public QueryBuilder select(boolean distinct) throws QueryBuilderException {
        appendToSelectAlias = true;
        this.distinct = distinct;
        return this;
    }

    @Override
    public QueryBuilder select(String alias) throws QueryBuilderException {
        return select(alias, false);
    }

    @Override
    public QueryBuilder select(String alias, boolean distinct) throws QueryBuilderException {
        result += "select " + (distinct ? "distinct " : "") + alias + " ";
        return this;
    }

    @Override
    public QueryBuilder from(Class<?> clazz, String alias) throws QueryBuilderException {
        if("".equals(result) && !appendToSelectAlias) throw new QueryBuilderException("First use select!");
        if(appendToSelectAlias) {
            select(alias, distinct);
        }
        this.alias = alias;
        result += "from " + clazz.getName() + " " + alias + " ";
        return this;
    }

    @Override
    public QueryBuilder join(String property, String alias) throws QueryBuilderException {
        return join(this.alias, property, alias);
    }

    @Override
    public QueryBuilder join(String fromAlias, String property, String alias) throws QueryBuilderException {
        if("".equals(result)) throw new QueryBuilderException("First use select!");
        result += "join " + fromAlias + "." + property + " " + alias + " ";
        return this;
    }

    @Override
    public <T> QueryBuilder where(String field, QueryComparator comp, T value) throws QueryBuilderException {
        return where(this.alias, field, comp, value);
    }

    @Override
    public <T> QueryBuilder where(String field, QueryComparator comp) throws QueryBuilderException {
        return where(this.alias, field, comp);
    }

    @Override
    public <T> QueryBuilder where(String alias, String field, QueryComparator comp, T value) throws QueryBuilderException {
        if("".equals(result)) throw new QueryBuilderException("First use select!");
        if(!result.contains("where")) {
            result += "where ";
        }
        if(comp.equals(QueryComparator.ISNULL) || comp.equals(QueryComparator.ISNOTNULL)) {
            result += " " + alias + "." + field + " " + escapeComparator(comp);
        } else {
            result += " " + (!"".equals(alias) ? alias + "." : "") + field + " " + escapeComparator(comp) + " " + escapeValue(value, comp);
        }
        return this;
    }

    @Override
    public <T> QueryBuilder where(String alias, String field, QueryComparator comp) throws QueryBuilderException {
        return where(alias, field, comp, ":" + field);
    }

    @Override
    public QueryBuilder and() throws QueryBuilderException {
        if("".equals(result)) throw new QueryBuilderException("First use select!");
        result += " and";
        return this;
    }

    @Override
    public QueryBuilder or() throws QueryBuilderException {
        if("".equals(result)) throw new QueryBuilderException("First use select!");
        result += " or";
        return this;
    }

    @Override
    public QueryBuilder order(String property, QueryOrder order) throws QueryBuilderException {
        return order(this.alias, property, order);
    }

    @Override
    public QueryBuilder order(String alias, String property, QueryOrder order) throws QueryBuilderException {
        if("".equals(result)) throw new QueryBuilderException("First use select!");
        if(!result.contains("order by")) {
            result += " order by";
        } else {
            result += ",";
        }
        result += " " + alias + "." + property + " ";
        return this;
    }

    @Override
    public String getResult() throws QueryBuilderException {
        return result;
    }

    private <T> String escapeValue(T value, QueryComparator comp) {
        String finalresult = "";
        if(comp.equals(QueryComparator.IN)) {
            finalresult += "(";
        }
        if(value instanceof String && !((String) value).contains(":") && !comp.equals(QueryComparator.LIKE)) {
            finalresult += "\"" + value + "\"";
        } else {
            finalresult += String.valueOf(value);
        }
        if(comp.equals(QueryComparator.IN)) {
            finalresult += ")";
        }
        return finalresult;
    }
    private String escapeComparator(QueryComparator comp) throws QueryBuilderException {
        switch(comp) {
            case EQ: return "=";
            case NE: return "!=";
            case LT: return "<";
            case GT: return ">";
            case LE: return "<=";
            case GE: return ">=";
            case LIKE: return "like";
            case IN: return "in";
            case ISNULL: return "is null";
            case ISNOTNULL: return "is not null";
            default: throw new QueryBuilderException("Not yet supported!");
        }
    }
    private String escapeOrder(QueryOrder order) throws QueryBuilderException {
        switch(order) {
            case ASC: return "";
            case DESC: return " desc";
            default: throw new QueryBuilderException("Not yet supported");
        }
    }

    @Override
    public QueryBuilder clear() {
        result = "";
        return this;
    }
}
